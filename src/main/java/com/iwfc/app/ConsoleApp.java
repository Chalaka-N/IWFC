package com.iwfc.app;

import com.iwfc.exception.DuplicateDataException;
import com.iwfc.exception.InvalidBookingException;
import com.iwfc.exception.UnauthorizedAccessException;
import com.iwfc.model.*;
import com.iwfc.pattern.IWFCFacade;
import com.iwfc.pattern.UserFactory;

import java.time.DayOfWeek;
import java.time.LocalTime;

public class ConsoleApp {

    public static void main(String[] args) throws Exception {

        IWFCFacade iwfc = new IWFCFacade();

        // =====================================================
        // 1. CREATE USERS
        // =====================================================

        User admin = UserFactory.createUser(
                "administrator",
                "U001",
                "Admin One");

        User instructor = UserFactory.createUser(
                "instructor",
                "U002",
                "Instructor One");

        User member = UserFactory.createUser(
                "member",
                "U003",
                "Member One");

        iwfc.registerUser(admin);
        iwfc.registerUser(instructor);
        iwfc.registerUser(member);


        // =====================================================
        // 2. REGISTER EQUIPMENT
        // =====================================================

        Equipment treadmill = new Equipment(
                "EQ001",
                "Treadmill 01",
                "Cardio Zone",
                20);

        Equipment exerciseBike = new Equipment(
                "EQ002",
                "Exercise Bike 01",
                "Cardio Zone",
                25);

        Equipment rowingMachine = new Equipment(
                "EQ003",
                "Rowing Machine 01",
                "Cardio Zone",
                30);

        Equipment benchPress = new Equipment(
                "EQ004",
                "Bench Press 01",
                "Strength Zone",
                40);

        Equipment cableMachine = new Equipment(
                "EQ005",
                "Cable Machine 01",
                "Strength Zone",
                50);

        Equipment yogaMats = new Equipment(
                "EQ006",
                "Yoga Mat Set 01",
                "Studio A",
                15);

        iwfc.registerEquipment(admin, treadmill);
        iwfc.registerEquipment(admin, exerciseBike);
        iwfc.registerEquipment(admin, rowingMachine);
        iwfc.registerEquipment(admin, benchPress);
        iwfc.registerEquipment(admin, cableMachine);
        iwfc.registerEquipment(admin, yogaMats);


        // =====================================================
        // 3. CREATE FITNESS SESSIONS
        // =====================================================

        FitnessSession yoga = new FitnessSession(
                "S001",
                "Morning Yoga",
                DayOfWeek.MONDAY,
                LocalTime.of(8, 0),
                LocalTime.of(9, 0),
                "Studio A",
                "EQ006",
                10,
                true);

        FitnessSession spin = new FitnessSession(
                "S002",
                "Spin Class",
                DayOfWeek.MONDAY,
                LocalTime.of(9, 0),
                LocalTime.of(10, 0),
                "Cardio Studio",
                "EQ002",
                12,
                true);

        FitnessSession strength = new FitnessSession(
                "S003",
                "Strength Training",
                DayOfWeek.MONDAY,
                LocalTime.of(10, 0),
                LocalTime.of(11, 0),
                "Strength Studio",
                "EQ004",
                8,
                true);

        FitnessSession rowing = new FitnessSession(
                "S004",
                "Rowing Fitness",
                DayOfWeek.TUESDAY,
                LocalTime.of(8, 0),
                LocalTime.of(9, 0),
                "Cardio Studio",
                "EQ003",
                10,
                true);


        // =====================================================
        // 4. ADD VALID SESSIONS
        // =====================================================

        System.out.println("=== IWFC MANAGEMENT SYSTEM ===");

        System.out.println();
        System.out.println("=== SESSION SCHEDULING ===");

        try {
            iwfc.addSession(admin, yoga);
            System.out.println("S001 Morning Yoga added successfully.");

            iwfc.addSession(admin, spin);
            System.out.println("S002 Spin Class added successfully.");

            iwfc.addSession(admin, strength);
            System.out.println("S003 Strength Training added successfully.");

            iwfc.addSession(admin, rowing);
            System.out.println("S004 Rowing Fitness added successfully.");

        } catch (DuplicateDataException
                 | InvalidBookingException
                 | UnauthorizedAccessException ex) {

            System.out.println("Session error: " + ex.getMessage());
        }


        // =====================================================
        // 5. DISPLAY USERS
        // =====================================================

        System.out.println();
        System.out.println("=== USERS ===");

        System.out.println(admin.getProfileSummary());
        System.out.println(instructor.getProfileSummary());
        System.out.println(member.getProfileSummary());


        // =====================================================
        // 6. DISPLAY EQUIPMENT
        // =====================================================

        System.out.println();
        System.out.println("=== EQUIPMENT ===");

        iwfc.getEquipment().findAll().forEach(item ->
                System.out.println(
                        item.getEquipmentId()
                                + " - "
                                + item.getName()
                                + " | Location: "
                                + item.getLocation()
                                + " | Status: "
                                + item.getStatus()
                                + " | Maintenance Threshold: "
                                + item.getMaintenanceThresholdHours()
                                + " hours"
                )
        );


        // =====================================================
        // 7. DISPLAY SESSIONS
        // =====================================================

        System.out.println();
        System.out.println("=== FITNESS SESSIONS ===");

        iwfc.getSessions().findAll().forEach(session ->
                System.out.println(
                        session.getSessionId()
                                + " - "
                                + session.getName()
                                + " | "
                                + session.getDay()
                                + " "
                                + session.getStartTime()
                                + "-"
                                + session.getEndTime()
                                + " | Studio: "
                                + session.getStudio()
                                + " | Equipment: "
                                + session.getEquipmentId()
                                + " | Capacity: "
                                + session.getCapacity()
                )
        );


        // =====================================================
        // 8. TEST DOUBLE-BOOKING PREVENTION
        // =====================================================

        System.out.println();
        System.out.println("=== DOUBLE-BOOKING TEST ===");

        FitnessSession conflictingSession = new FitnessSession(
                "S005",
                "Cardio Blast",
                DayOfWeek.MONDAY,
                LocalTime.of(9, 30),
                LocalTime.of(10, 30),
                "Cardio Studio",
                "EQ002",
                10,
                true);

        try {

            iwfc.addSession(admin, conflictingSession);

            System.out.println(
                    "ERROR: Conflicting session was incorrectly accepted."
            );

        } catch (InvalidBookingException ex) {

            System.out.println(
                    "Double-booking prevented successfully: "
                            + ex.getMessage()
            );

        } catch (DuplicateDataException
                 | UnauthorizedAccessException ex) {

            System.out.println(
                    "Scheduling error: "
                            + ex.getMessage()
            );
        }


        // =====================================================
        // 9. BOOK FITNESS SESSION
        // =====================================================

        System.out.println();
        System.out.println("=== MEMBER BOOKING ===");

        try {

            iwfc.book(member, "S001", "B001");

            System.out.println(
                    "Booking B001 created successfully."
            );

        } catch (InvalidBookingException
                 | UnauthorizedAccessException ex) {

            System.out.println(
                    "Booking error: "
                            + ex.getMessage()
            );
        }


        // =====================================================
        // 10. TEST DUPLICATE MEMBER BOOKING
        // =====================================================

        System.out.println();
        System.out.println("=== DUPLICATE BOOKING TEST ===");

        try {

            iwfc.book(member, "S001", "B002");

            System.out.println(
                    "ERROR: Duplicate booking was incorrectly accepted."
            );

        } catch (InvalidBookingException ex) {

            System.out.println(
                    "Duplicate booking prevented successfully: "
                            + ex.getMessage()
            );

        } catch (UnauthorizedAccessException ex) {

            System.out.println(
                    "Authorization error: "
                            + ex.getMessage()
            );
        }


        // =====================================================
        // 11. EQUIPMENT USAGE & MAINTENANCE
        // =====================================================

        System.out.println();
        System.out.println("=== EQUIPMENT USAGE & MAINTENANCE ===");

        try {

            // EQ001 threshold = 20 hours.
            // Adding 21 hours should trigger the maintenance alert.
            iwfc.recordEquipmentUsage(
                    instructor,
                    "EQ001",
                    21);

            // Instructor reports maintenance issue.
            iwfc.reportMaintenance(
                    instructor,
                    "EQ001",
                    "M001",
                    "Treadmill belt inspection required",
                    MaintenanceRequest.Urgency.MEDIUM);

            // Administrator assigns the maintenance request.
            iwfc.updateMaintenance(
                    admin,
                    "M001",
                    MaintenanceRequest.Status.ASSIGNED);

            System.out.println("Maintenance process completed successfully.");

        } catch (DuplicateDataException
                 | UnauthorizedAccessException ex) {

            System.out.println(
                    "Maintenance error: "
                            + ex.getMessage()
            );
        }


        // =====================================================
        // 12. DISPLAY NOTIFICATIONS
        // =====================================================

        System.out.println();
        System.out.println("=== NOTIFICATIONS ===");

        iwfc.getNotificationService()
                .getNotifications()
                .forEach(System.out::println);


        // =====================================================
        // END
        // =====================================================

        System.out.println();
        System.out.println("=== SYSTEM DEMONSTRATION COMPLETED ===");
    }
}