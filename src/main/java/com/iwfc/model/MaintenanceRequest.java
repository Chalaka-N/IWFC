package com.iwfc.model;

import com.iwfc.pattern.MaintenanceObserver;
import java.util.ArrayList;
import java.util.List;

public class MaintenanceRequest {
    public enum Urgency { LOW, MEDIUM, HIGH }
    public enum Status { PENDING, ASSIGNED, COMPLETED }

    private final String requestId;
    private final String equipmentId;
    private final String description;
    private final Urgency urgency;
    private Status status;
    private final List<MaintenanceObserver> observers = new ArrayList<>();

    public MaintenanceRequest(String requestId, String equipmentId, String description, Urgency urgency) {
        this.requestId = requestId;
        this.equipmentId = equipmentId;
        this.description = description;
        this.urgency = urgency;
        this.status = Status.PENDING;
    }

    public String getRequestId() { return requestId; }
    public String getEquipmentId() { return equipmentId; }
    public String getDescription() { return description; }
    public Urgency getUrgency() { return urgency; }
    public Status getStatus() { return status; }

    public void addObserver(MaintenanceObserver observer) { observers.add(observer); }

    public void updateStatus(Status newStatus) {
        this.status = newStatus;
        for (MaintenanceObserver observer : observers) {
            observer.update(this);
        }
    }
}
