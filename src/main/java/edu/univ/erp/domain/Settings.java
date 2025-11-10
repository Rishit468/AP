package edu.univ.erp.domain;

public class Settings {
    // This maps to the 'settings' table in the ERP DB
    private boolean maintenanceMode;

    public Settings() {
        this.maintenanceMode = false;
    }

    public Settings(boolean maintenanceMode) {
        this.maintenanceMode = maintenanceMode;
    }

    public boolean isMaintenanceMode() {
        return maintenanceMode;
    }

    public void setMaintenanceMode(boolean maintenanceMode) {
        this.maintenanceMode = maintenanceMode;
    }

    @Override
    public String toString() {
        return "Settings{" +
                "maintenanceMode=" + maintenanceMode +
                '}';
    }
}
