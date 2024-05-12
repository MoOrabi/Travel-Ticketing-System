package com.example.demo.controller;

import com.example.demo.dto.FlightData;
import com.example.demo.entity.Flight;
import com.example.demo.repository.FlightRepository;
import com.example.demo.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class FlightController {

    @Autowired
    private FlightService flightService;

    @PostMapping("/flight")
    public ResponseEntity<?> addOrUpdateFlight(@RequestHeader HttpHeaders headers, @RequestBody FlightData flightData) {

        return flightService.addOrUpdateFlight(headers, flightData);
    }

    @GetMapping("/my-all")
    public ResponseEntity<?> getMyAllFlights(@RequestHeader HttpHeaders headers) {
        return flightService.getMyAllFlights(headers);
    }

    @DeleteMapping("/")
    public ResponseEntity<?> deleteFlight(@RequestHeader HttpHeaders headers, @RequestParam Long flightId) {
        return flightService.deleteFlight(headers, flightId);
    }
}
