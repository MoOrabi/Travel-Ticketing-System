package com.example.demo.dto;

import com.example.demo.entity.AppUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;


@Getter
@Setter
@Builder
@AllArgsConstructor
public class FlightData {

    private long flightNumber;

    private String fromCity;

    private String toCity;

    private LocalDateTime arrivalTime;

    private LocalDateTime departureTime;

    private String airportCode;

    private String aircraftId;

    private double ticketPrice;

    private String ticketPriceCurrency;

    private Long creatorId;
}
