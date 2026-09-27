package com.iwfc.pattern;

import com.iwfc.model.MaintenanceRequest;

public interface MaintenanceObserver {
    void update(MaintenanceRequest request);
}
