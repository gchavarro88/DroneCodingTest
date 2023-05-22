package com.local.DroneCodingTest.services;

import com.local.DroneCodingTest.entities.DeliverySchedule;
import com.local.DroneCodingTest.entities.Drone;
import com.local.DroneCodingTest.entities.Location;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Service
public class AssignLocationService {


    /**
     * Responsible to get the drones from the input
     * @param input
     * @return
     */
    public List<Drone> getDrones(String input) {
        String[] data = input.split(",");
        List<Drone> drones = new ArrayList<>();
        for (int i = 0; i < data.length; i = i + 2) {
            drones.add(new Drone(data[i], Integer.parseInt(data[i + 1])));
        }
        return drones;
    }

    /**
     * Responsible to get the locations from the input
     * @param input
     * @return
     */
    public List<Location> getLocations(String[] input) {
        List<Location> locations = new ArrayList<>();

        for (String s : input) {
            String[] data = s.split(",");
            locations.add(new Location(data[0], Integer.parseInt(data[1])));
        }
        return locations;
    }

    /**
     * Responsible to validate capacity by drone
     * @param drone
     * @param locations
     * @return
     */
    public DeliverySchedule validateCapacityByDrone(Drone drone, List<Location> locations) {
        int remainingCapacity = drone.getCapacity();
        DeliverySchedule deliverySchedule = new DeliverySchedule(drone, new ArrayList<>());
        for (Location location : locations) {
            if (remainingCapacity >= location.getWeight()) {
                deliverySchedule.getLocations().add(location);
                remainingCapacity -= location.getWeight();
            } else {
                break;
            }
        }

        return deliverySchedule;
    }

    /**
     * Responsible to assign the locations by drone
     * @param drones
     * @param locations
     * @return
     */
    public List<DeliverySchedule> assignDeliveries(List<Drone> drones, List<Location> locations) {
        int locationsIndex = 0;
        List<DeliverySchedule> schedule = new ArrayList<>();

        if(!drones.isEmpty()) {
            while (locationsIndex < (locations.size())) {
                List<DeliverySchedule> deliveries = new ArrayList<>();
                for (Drone drone : drones) {
                    deliveries.add(validateCapacityByDrone(drone, locations.subList(locationsIndex, locations.size())));
                }

                DeliverySchedule bestDelivery = deliveries.stream()
                        .max(Comparator.comparing(d -> d.getLocations().size())).get();
                locationsIndex += bestDelivery.getLocations().size();
                schedule.add(bestDelivery);
            }
        }

        return schedule;
    }

    /**
     * Responsible to receive and order with the input data and prepare date to call the schedules generator
     * @param input
     * @return
     */
    public StringBuilder assignLocationsByInput(String input) {
        String[] data = input.split(System.getProperty("line.separator"));
        List<Drone> drones = getDrones(data[0]);
        List<Location> locations = getLocations(Arrays.copyOfRange(data, 1, data.length, String[].class));
        return generateSchedules(drones, locations);
    }

    /**
     * Responsible to generate the delivery schedules using the input lists
     * @param drones
     * @param locations
     * @return
     */
    public StringBuilder generateSchedules(List<Drone> drones, List<Location> locations) {
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

    /**
     * Demo created using the example data
     * @return
     */
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

        return generateSchedules(drones, locations);
    }
}
