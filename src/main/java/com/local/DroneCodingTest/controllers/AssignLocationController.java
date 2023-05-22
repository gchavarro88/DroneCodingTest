package com.local.DroneCodingTest.controllers;

import com.local.DroneCodingTest.services.AssignLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/assign-location")
public class AssignLocationController {

    @Autowired
    private AssignLocationService service;

    @GetMapping("/run-example")
    public ResponseEntity<String> runExample () {
        StringBuilder append = service.getDemo();
        return ResponseEntity.ok(append.toString());
    }

    @PostMapping()
    public ResponseEntity<String> assignLocation (@RequestBody String payload) {
        if (payload.isEmpty()) {
            return ResponseEntity.status(400).body("Empty Input, data for drones and locations don't should be empty");
        } else {
            StringBuilder append = service.assignLocationsByInput(payload);
            return ResponseEntity.ok(append.toString());
        }
    }
}
