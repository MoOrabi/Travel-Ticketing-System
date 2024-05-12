package com.example.demo.service;

import com.example.demo.dto.FlightData;
import com.example.demo.entity.AppUser;
import com.example.demo.entity.Flight;
import com.example.demo.repository.AppUserRepository;
import com.example.demo.repository.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FlightService {

    @Autowired
    private FlightRepository flightRepository;
    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired AppUserService appUserService;

    public ResponseEntity<?> addOrUpdateFlight(HttpHeaders headers, FlightData flightData) {
        Long userId = appUserService.getUserIdFromHeaders(headers);
        if (userId != 0L){
            Flight flight = flightToFlightData(flightData, userId);
            if(flight != null && flight.getCreator().getId().equals(userId)) {
                flightRepository.save(flight);
                return new ResponseEntity<>(flightData, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
    }

    public ResponseEntity<?> deleteFlight(HttpHeaders headers, Long flightId) {
        Long userId = appUserService.getUserIdFromHeaders(headers);
        if (userId != 0L){
            Optional<Flight> optionalFlight = flightRepository.findById(flightId);
            if(optionalFlight.isPresent()) {
                Flight flight = optionalFlight.get();
                if(flight.getCreator().getId().equals(userId)){
                    flightRepository.delete(flight);
                    return new ResponseEntity<>(
                            String.format("Flight with Id: %d deleted successfully", flightId),
                            HttpStatus.OK);
                }
            }else {
                return new ResponseEntity<>("No such flight number exists", HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
    }

    private Flight flightToFlightData(FlightData flightData, Long userId) {
        Optional<AppUser> creator = appUserRepository.findById(userId);
        return creator.map(appUser -> Flight.builder()
                .flightNumber(flightData.getFlightNumber())
                .fromCity(flightData.getFromCity())
                .toCity(flightData.getToCity())
                .aircraftId(flightData.getAircraftId())
                .airportCode(flightData.getAirportCode())
                .ticketPrice(flightData.getTicketPrice())
                .arrivalTime(flightData.getArrivalTime())
                .departureTime(flightData.getDepartureTime())
                .creator(appUser)
                .build()).orElse(null);
    }

    public ResponseEntity<?> getMyAllFlights(HttpHeaders headers) {
        Long userId = appUserService.getUserIdFromHeaders(headers);
        if (userId != 0L){
            List<Flight> flights = flightRepository.findAllByCreatorId(userId);
            return new ResponseEntity<>(flights, HttpStatus.OK);
        }

        return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
    }
}
