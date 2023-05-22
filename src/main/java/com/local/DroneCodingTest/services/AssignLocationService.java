package com.local.DroneCodingTest.services;

import com.local.DroneCodingTest.models.DeliverySchedule;
import com.local.DroneCodingTest.models.Drone;
import com.local.DroneCodingTest.models.Location;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Service
public class AssignLocationService {


    public DeliverySchedule validateCapacity(Drone drone, List<Location> locations) {
        int remainingCapacity = drone.getCapacity();
        DeliverySchedule deliverySchedule = new DeliverySchedule(drone, new ArrayList<>());
        for (Location location : locations) {
            if (!location.isDelivered() && remainingCapacity >= location.getWeight()) {
                deliverySchedule.getLocations().add(location);
                remainingCapacity -= location.getWeight();
            } else {
                break;
            }
        }

        return deliverySchedule;
    }

    public List<DeliverySchedule> assignDeliveries(List<Drone> drones, List<Location> locations) {
        int locationsIndex = 0;
        List<DeliverySchedule> schedule = new ArrayList<>();

        if(!drones.isEmpty()) {
            while (locationsIndex < (locations.size())) {
                List<DeliverySchedule> deliveries = new ArrayList<>();
                for (Drone drone : drones) {
                    deliveries.add(validateCapacity(drone, locations.subList(locationsIndex, locations.size())));
                }

                DeliverySchedule bestDelivery = deliveries.stream()
                        .max(Comparator.comparing(d -> d.getLocations().size())).get();
                locationsIndex += bestDelivery.getLocations().size();
                schedule.add(bestDelivery);
            }
        }

        return schedule;
    }

    public StringBuilder getDemo() {

        List<Drone> drones = List.of(
                new Drone("Drone A", 200),
                new Drone("Drone B", 250),
                new Drone("Drone C", 100));

        List<Location> locations = List.of(
                new Location("Location A", 200),
                new Location("Location B", 150),
                new Location("Location C", 50),
                new Location("Location D", 150),
                new Location("Location E", 100),
                new Location("Location F", 200),
                new Location("Location G", 50),
                new Location("Location H", 80),
                new Location("Location I", 70),
                new Location("Location J", 50),
                new Location("Location K", 30),
                new Location("Location L", 20),
                new Location("Location M", 50),
                new Location("Location N", 30),
                new Location("Location O", 20),
                new Location("Location P", 90));

        return doAssign(drones, locations);
    }

    public StringBuilder assignLocation(String input) {
        String[] data = input.split(System.getProperty("line.separator"));
        List<Drone> drones = getDrones(data[0]);
        List<Location> locations = getLocations(Arrays.copyOfRange(data, 1, data.length, String[].class));
        return doAssign(drones, locations);
    }

    public List<Drone> getDrones(String input) {
        String[] data = input.split(",");
        List<Drone> drones = new ArrayList<>();
        for (int i = 0; i < data.length; i = i + 2) {
            drones.add(new Drone(data[i], Integer.parseInt(data[i + 1])));
        }
        return drones;
    }

    public List<Location> getLocations(String[] input) {
        List<Location> locations = new ArrayList<>();

        for (String s : input) {
            String[] data = s.split(",");
            locations.add(new Location(data[0], Integer.parseInt(data[1])));
        }
        return locations;
    }

    public StringBuilder doAssign(List<Drone> drones, List<Location> locations) {
        StringBuilder result = new StringBuilder();
        List<DeliverySchedule> deliverySchedule = assignDeliveries(drones, locations);

        for (Drone drone : drones) {
            int trip = 1;
            result.append("=====> Deliveries for ").append(drone.getName()).append(" Capacity: ")
                    .append(drone.getCapacity()).append(" ======>")
                    .append(System.getProperty("line.separator"));
            List<DeliverySchedule> deliveriesByDrone = deliverySchedule.stream()
                    .filter(d -> d.getDrone().equals(drone)).toList();
            for (DeliverySchedule delivery : deliveriesByDrone) {
                result.append("Trip #").append(trip).append(System.getProperty("line.separator"));
                delivery.getLocations().forEach(p -> result.append(" ").append(p.getName())
                        .append(" ").append(p.getWeight()).append(" -"));
                result.append("  Total: ").append(delivery.getLocations().stream().mapToInt(Location::getWeight).sum());
                result.append(System.getProperty("line.separator"));
                trip++;
            }
        }
        return result;
    }
}