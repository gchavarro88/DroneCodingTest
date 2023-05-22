package com.local.DroneCodingTest.controllers;

import com.local.DroneCodingTest.services.AssignLocationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@WebAppConfiguration
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AssignLocationControllerTest {

    @Mock
    private AssignLocationService service;

    @InjectMocks
    private AssignLocationController controller;

    @BeforeAll
    public void init(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void runExampleTest() {
        Mockito.when(service.getDemo()).thenReturn(new StringBuilder("RESPONSE"));
        ResponseEntity<String> result = controller.runExample();
        assertEquals(HttpStatus.OK, result.getStatusCode());
        Mockito.verify(service, Mockito.times(1)).getDemo();
    }

    @Test
    public void assignLocationsHappyPath() {

        Mockito.when(service.assignLocationsByInput(Mockito.eq("FAKE_PAYLOAD")))
                .thenReturn(new StringBuilder("RESPONSE"));
        ResponseEntity<String> result = controller.assignLocation("FAKE_PAYLOAD");

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("RESPONSE", result.getBody());
        Mockito.verify(service, Mockito.atLeastOnce()).assignLocationsByInput( Mockito.any());
    }

    @Test
    public void assignLocationsBadRequest() {

        ResponseEntity<String> result = controller.assignLocation("");

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals("Empty Input, data for drones and locations don't should be empty", result.getBody());
        Mockito.verify(service, Mockito.never()).assignLocationsByInput( Mockito.any());
    }

    @Test
    public void assignLocationsSadPath() {

        Assertions.assertThrows(Exception.class, () -> {
            controller.assignLocation("FAKE_PAYLOAD");
        });

        Mockito.verify(service, Mockito.never()).generateSchedules(Mockito.any(), Mockito.any());
    }
}
