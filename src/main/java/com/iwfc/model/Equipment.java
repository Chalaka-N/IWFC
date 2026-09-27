package com.iwfc.model;

public class Equipment {
    public enum Status { OPERATIONAL, FAULTY, UNDER_MAINTENANCE, INACTIVE }

    private final String equipmentId;
    private String name;
    private Status status;
    private String location;
    private double usageHours;
    private final double maintenanceThresholdHours;

    public Equipment(String equipmentId, String name, String location, double maintenanceThresholdHours) {
        this.equipmentId = equipmentId;
        this.name = name;
        this.location = location;
        this.maintenanceThresholdHours = maintenanceThresholdHours;
        this.status = Status.OPERATIONAL;
    }

    public String getEquipmentId() { return equipmentId; }
    public String getName() { return name; }
    public Status getStatus() { return status; }
    public String getLocation() { return location; }
    public double getUsageHours() { return usageHours; }
    public double getMaintenanceThresholdHours() { return maintenanceThresholdHours; }

    public void setName(String name) { this.name = name; }
    public void setStatus(Status status) { this.status = status; }
    public void setLocation(String location) { this.location = location; }

    public void addUsageHours(double hours) {
        if (hours < 0) throw new IllegalArgumentException("Usage hours cannot be negative.");
        usageHours += hours;
    }

    public boolean maintenanceDue() {
        return usageHours >= maintenanceThresholdHours;
    }
}
