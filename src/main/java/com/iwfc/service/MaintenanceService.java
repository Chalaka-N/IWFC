package com.iwfc.service;

import com.iwfc.exception.DuplicateDataException;
import com.iwfc.exception.UnauthorizedAccessException;
import com.iwfc.model.Administrator;
import com.iwfc.model.Equipment;
import com.iwfc.model.Instructor;
import com.iwfc.model.MaintenanceRequest;
import com.iwfc.repository.GenericRepository;

public class MaintenanceService {
    private final GenericRepository<MaintenanceRequest> requestRepository;

    public MaintenanceService(GenericRepository<MaintenanceRequest> requestRepository) {
        this.requestRepository = requestRepository;
    }

    public void reportIssue(Instructor instructor, Equipment equipment, String requestId,
                            String description, MaintenanceRequest.Urgency urgency,
                            NotificationService notificationService)
            throws DuplicateDataException {
        MaintenanceRequest request = new MaintenanceRequest(requestId, equipment.getEquipmentId(), description, urgency);
        request.addObserver(notificationService);
        requestRepository.add(requestId, request);
        equipment.setStatus(Equipment.Status.FAULTY);
    }

    public void updateStatus(Administrator administrator, String requestId,
                             MaintenanceRequest.Status newStatus)
            throws UnauthorizedAccessException {
        MaintenanceRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Maintenance request not found."));
        if (administrator == null) {
            throw new UnauthorizedAccessException("Only an Administrator can update maintenance status.");
        }
        request.updateStatus(newStatus);
    }
}
