package com.local.DroneCodingTest.services;

import com.local.DroneCodingTest.entities.DeliverySchedule;
import com.local.DroneCodingTest.entities.Drone;
import com.local.DroneCodingTest.entities.Location;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@WebAppConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AssignLocationServiceTest {

    @Autowired
    private AssignLocationService service;


    @Test
    public void runExampleTest() {
        String text = """
                =====> Deliveries for Drone A Capacity: 200 ======>
                Trip #1
                 Location A 200 -  Total: 200
                Trip #2
                 Location B 150 - Location C 50 -  Total: 200
                Trip #3
                 Location M 50 - Location N 30 - Location O 20 - Location P 90 -  Total: 190
                =====> Deliveries for Drone B Capacity: 250 ======>
                Trip #1
                 Location D 150 - Location E 100 -  Total: 250
                Trip #2
                 Location F 200 - Location G 50 -  Total: 250
                Trip #3
                 Location H 80 - Location I 70 - Location J 50 - Location K 30 - Location L 20 -  Total: 250
                =====> Deliveries for Drone C Capacity: 100 ======>
                """;
        StringBuilder result = service.getDemo();
        assertNotNull(result);
        assertEquals(text, result.toString());
    }

    @Test
    public void assignLocationTest() {
        String response =
                """
                =====> Deliveries for Drone1 Capacity: 200 ======>
                Trip #1
                 LocationA 200 -  Total: 200
                Trip #2
                 LocationB 150 - LocationC 50 -  Total: 200
                =====> Deliveries for Drone2 Capacity: 250 ======>
                Trip #1
                 LocationD 150 - LocationE 100 -  Total: 250
                =====> Deliveries for Drone3 Capacity: 100 ======>
                """;

        String input =
                """
                Drone1,200,Drone2,250,Drone3,100
                LocationA,200
                LocationB,150
                LocationC,50
                LocationD,150
                LocationE,100
                """;

        StringBuilder result = service.assignLocationsByInput(input);
        assertNotNull(result);
        assertEquals(response, result.toString());
    }

    @Test
    public void getDronesTest() {
        String input = "Drone A,200,Drone B,250,Drone C,100";

        List<Drone> dronesExpected = List.of(new Drone("Drone A", 200),
                new Drone("Drone B", 250),
                new Drone("Drone C", 100));

        List<Drone> drones = service.getDrones(input);

        assertNotNull(drones);
        assertEquals(3, drones.size());
        for (int i = 0; i < dronesExpected.size(); i++) {
            assertEquals(dronesExpected.get(i).getName(), drones.get(i).getName());
        }
    }

    @Test
    public void getLocationsTest() {
        String input =
                """
                Location A,200
                Location B,150
                Location C,50
                Location D,150
                Location E,100
                """;

        List<Location> locationsExpected = List.of(
                new Location("Location A", 200),
                new Location("Location B", 150),
                new Location("Location C", 50),
                new Location("Location D", 150),
                new Location("Location E", 100));

        List<Location> locations = service.getLocations(input.split(System.lineSeparator()));

        assertNotNull(locations);
        assertEquals(5, locations.size());
        for (int i = 0; i < locationsExpected.size(); i++) {
            assertEquals(locationsExpected.get(i).getName(), locations.get(i).getName());
        }
    }

    @Test
    public void validateCapacityTest() {
        Drone drone = new Drone("Drone A", 200);
        List<Location> locations = List.of(
                new Location("Location A", 30),
                new Location("Location B", 60),
                new Location("Location C", 50),
                new Location("Location D", 150),
                new Location("Location E", 100));

        DeliverySchedule deliveryScheduleExpected = new DeliverySchedule(drone, List.of(
                new Location("Location A", 30),
                new Location("Location B", 60),
                new Location("Location C", 50)
        ));

        DeliverySchedule result = service.validateCapacityByDrone(drone, locations);
        assertNotNull(result);
        assertNotNull(result.getDrone());
        assertNotNull(result.getLocations());
        assertEquals(3, result.getLocations().size());
        assertTrue(result.getLocations().stream().mapToInt(Location::getWeight).sum() <= drone.getCapacity());

        for (int i = 0; i < deliveryScheduleExpected.getLocations().size(); i++) {
            assertEquals(deliveryScheduleExpected.getLocations().get(i).getName(),
                    result.getLocations().get(i).getName());
        }
    }

    @Test
    public void assignDeliveriesTest () {
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

        List<DeliverySchedule> result = service.assignDeliveries(drones, locations);
        assertNotNull(result);
        assertEquals(6, result.size());
        assertEquals(3L, result.stream().filter(d -> d.getDrone().getName().equals("Drone A")).count());
        assertEquals(3L, result.stream().filter(d -> d.getDrone().getName().equals("Drone B")).count());
        assertEquals(0L, result.stream().filter(d -> d.getDrone().getName().equals("Drone C")).count());
    }

    @Test
    public void doAssignTest () {
        List<Drone> drones = List.of(
                new Drone("Drone1", 200),
                new Drone("Drone2", 250),
                new Drone("Drone3", 100));

        List<Location> locations = List.of(
                new Location("LocationA", 200),
                new Location("LocationB", 150),
                new Location("LocationC", 50),
                new Location("LocationD", 150),
                new Location("LocationE", 100));

        String response =
                """
                =====> Deliveries for Drone1 Capacity: 200 ======>
                Trip #1
                 LocationA 200 -  Total: 200
                Trip #2
                 LocationB 150 - LocationC 50 -  Total: 200
                =====> Deliveries for Drone2 Capacity: 250 ======>
                Trip #1
                 LocationD 150 - LocationE 100 -  Total: 250
                =====> Deliveries for Drone3 Capacity: 100 ======>
                """;

        StringBuilder result = service.generateSchedules(drones, locations);
        assertNotNull(result);
        assertEquals(response, result.toString());
    }

    @Test
    public void doAssignEmptyDroneListTest () {
        List<Drone> drones = List.of();

        List<Location> locations = List.of(
                new Location("LocationA", 200),
                new Location("LocationB", 150),
                new Location("LocationC", 50),
                new Location("LocationD", 150),
                new Location("LocationE", 100));

        String response = "";

        StringBuilder result = service.generateSchedules(drones, locations);
        assertNotNull(result);
        assertEquals(response, result.toString());
    }

    @Test
    public void doAssignEmptyLocationListTest () {
        List<Drone> drones = List.of(
                new Drone("Drone1", 200),
                new Drone("Drone2", 250),
                new Drone("Drone3", 100));

        List<Location> locations = List.of();

        String response = """
                =====> Deliveries for Drone1 Capacity: 200 ======>
                =====> Deliveries for Drone2 Capacity: 250 ======>
                =====> Deliveries for Drone3 Capacity: 100 ======>
                """;

        StringBuilder result = service.generateSchedules(drones, locations);
        assertNotNull(result);
        assertEquals(response, result.toString());
    }
}
