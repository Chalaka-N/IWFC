package com.iwfc.model;

import java.time.DayOfWeek;
import java.time.LocalTime;

public class FitnessSession {
    private final String sessionId;
    private final String name;
    private final DayOfWeek day;
    private final LocalTime startTime;
    private final LocalTime endTime;
    private final String studio;
    private final String equipmentId;
    private final int capacity;
    private final boolean recurringWeekly;

    public FitnessSession(String sessionId, String name, DayOfWeek day, LocalTime startTime,
                          LocalTime endTime, String studio, String equipmentId, int capacity,
                          boolean recurringWeekly) {
        this.sessionId = sessionId;
        this.name = name;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
        this.studio = studio;
        this.equipmentId = equipmentId;
        this.capacity = capacity;
        this.recurringWeekly = recurringWeekly;
    }

    public String getSessionId() { return sessionId; }
    public String getName() { return name; }
    public DayOfWeek getDay() { return day; }
    public LocalTime getStartTime() { return startTime; }
    public LocalTime getEndTime() { return endTime; }
    public String getStudio() { return studio; }
    public String getEquipmentId() { return equipmentId; }
    public int getCapacity() { return capacity; }
    public boolean isRecurringWeekly() { return recurringWeekly; }

    public boolean overlaps(FitnessSession other) {
        if (day != other.day) return false;
        boolean timeOverlap = startTime.isBefore(other.endTime) && endTime.isAfter(other.startTime);
        if (!timeOverlap) return false;

        boolean sameStudio = studio != null && studio.equalsIgnoreCase(other.studio);
        boolean sameEquipment = equipmentId != null && equipmentId.equalsIgnoreCase(other.equipmentId);
        return sameStudio || sameEquipment;
    }
}
