package com.iwfc.service;

import com.iwfc.model.MaintenanceRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MaintenanceServiceTest {
    @Test
    void notificationShouldBeGeneratedWhenStatusChanges() {
        MaintenanceRequest request = new MaintenanceRequest(
                "M1", "EQ1", "Resistance failure", MaintenanceRequest.Urgency.HIGH);
        NotificationService notifications = new NotificationService();
        request.addObserver(notifications);

        request.updateStatus(MaintenanceRequest.Status.ASSIGNED);

        assertEquals(1, notifications.getNotifications().size());
    }
}
