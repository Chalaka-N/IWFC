package com.iwfc.pattern;

import com.iwfc.exception.DuplicateDataException;
import com.iwfc.exception.InvalidBookingException;
import com.iwfc.exception.UnauthorizedAccessException;
import com.iwfc.model.*;
import com.iwfc.repository.GenericRepository;
import com.iwfc.service.BookingService;
import com.iwfc.service.MaintenanceService;
import com.iwfc.service.NotificationService;

public class IWFCFacade {
    private final GenericRepository<User> users = new GenericRepository<>();
    private final GenericRepository<Equipment> equipment = new GenericRepository<>();
    private final GenericRepository<FitnessSession> sessions = new GenericRepository<>();
    private final GenericRepository<Booking> bookings = new GenericRepository<>();
    private final GenericRepository<MaintenanceRequest> maintenanceRequests = new GenericRepository<>();

    private final NotificationService notificationService = new NotificationService();
    private final BookingService bookingService = new BookingService(sessions, bookings, notificationService);
    private final MaintenanceService maintenanceService = new MaintenanceService(maintenanceRequests);

    public void registerUser(User user) throws DuplicateDataException {
        users.add(user.getUserId(), user);
    }

    public void registerEquipment(User actor, Equipment item)
            throws DuplicateDataException, UnauthorizedAccessException {
        requireRole(actor, Administrator.class);
        equipment.add(item.getEquipmentId(), item);
    }

    public void editEquipment(User actor, String equipmentId, String name, String location)
            throws UnauthorizedAccessException {
        requireRole(actor, Administrator.class);
        Equipment item = equipment.findById(equipmentId)
                .orElseThrow(() -> new IllegalArgumentException("Equipment not found."));
        item.setName(name);
        item.setLocation(location);
    }

    public void deactivateEquipment(User actor, String equipmentId)
            throws UnauthorizedAccessException {
        requireRole(actor, Administrator.class);
        Equipment item = equipment.findById(equipmentId)
                .orElseThrow(() -> new IllegalArgumentException("Equipment not found."));
        item.setStatus(Equipment.Status.INACTIVE);
    }

    public void addSession(User actor, FitnessSession session)
            throws DuplicateDataException, InvalidBookingException, UnauthorizedAccessException {
        requireRole(actor, Administrator.class);
        boolean conflict = sessions.findAll().stream().anyMatch(session::overlaps);
        if (conflict) {
            throw new InvalidBookingException("Session conflicts with an existing studio/equipment schedule.");
        }
        sessions.add(session.getSessionId(), session);
    }

    public void recordEquipmentUsage(User actor, String equipmentId, double hours)
            throws UnauthorizedAccessException {
        requireRole(actor, Instructor.class);
        Equipment item = equipment.findById(equipmentId)
                .orElseThrow(() -> new IllegalArgumentException("Equipment not found."));
        item.addUsageHours(hours);
        if (item.maintenanceDue()) {
            notificationService.send("Maintenance alert: " + item.getEquipmentId() +
                    " has reached its preventative maintenance threshold.");
        }
    }

    public Booking book(User actor, String sessionId, String bookingId)
            throws InvalidBookingException, UnauthorizedAccessException {
        requireRole(actor, Member.class);
        return bookingService.bookSession((Member) actor, sessionId, bookingId);
    }

    public void reportMaintenance(User actor, String equipmentId, String requestId,
                                  String description, MaintenanceRequest.Urgency urgency)
            throws DuplicateDataException, UnauthorizedAccessException {
        requireRole(actor, Instructor.class);
        Equipment item = equipment.findById(equipmentId)
                .orElseThrow(() -> new IllegalArgumentException("Equipment not found."));
        maintenanceService.reportIssue((Instructor) actor, item, requestId, description, urgency, notificationService);
    }

    public void updateMaintenance(User actor, String requestId, MaintenanceRequest.Status status)
            throws UnauthorizedAccessException {
        requireRole(actor, Administrator.class);
        maintenanceService.updateStatus((Administrator) actor, requestId, status);
    }

    private void requireRole(User actor, Class<? extends User> requiredRole) throws UnauthorizedAccessException {
        if (actor == null || !requiredRole.isInstance(actor)) {
            throw new UnauthorizedAccessException(
                    "Only " + requiredRole.getSimpleName() + " users may perform this operation.");
        }
    }

    public GenericRepository<User> getUsers() { return users; }
    public GenericRepository<Equipment> getEquipment() { return equipment; }
    public GenericRepository<FitnessSession> getSessions() { return sessions; }
    public GenericRepository<Booking> getBookings() { return bookings; }
    public GenericRepository<MaintenanceRequest> getMaintenanceRequests() { return maintenanceRequests; }
    public NotificationService getNotificationService() { return notificationService; }
}
