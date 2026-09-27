package com.iwfc.service;

import com.iwfc.model.MaintenanceRequest;
import com.iwfc.pattern.MaintenanceObserver;

import java.util.ArrayList;
import java.util.List;

public class NotificationService implements MaintenanceObserver {
    private final List<String> notifications = new ArrayList<>();

    @Override
    public void update(MaintenanceRequest request) {
        send("Maintenance " + request.getRequestId() +
                " status changed to " + request.getStatus());
    }

    public void send(String message) {
        notifications.add(message);
    }

    public List<String> getNotifications() {
        return List.copyOf(notifications);
    }
}
