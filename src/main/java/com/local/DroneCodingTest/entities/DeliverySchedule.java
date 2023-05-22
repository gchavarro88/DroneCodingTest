package com.local.DroneCodingTest.entities;

import java.util.List;

public class DeliverySchedule {
    private Drone drone;
    private List<Location> locations;

    public DeliverySchedule(Drone drone, List<Location> locations) {
        this.drone = drone;
        this.locations = locations;
    }

    public Drone getDrone() {
        return drone;
    }

    public void setDrone(Drone drone) {
        this.drone = drone;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }
}
