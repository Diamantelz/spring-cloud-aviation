package ru.itis.flightbookingservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class FlightBookingKafkaProducer {

    private static final String TOPIC = "flight-booking-requests";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public ResponseEntity<Map<String, String>> sendMessage(String flightNumber) {
        Map<String, String> response = new HashMap<>();
        response.put("flightNumber", flightNumber);
        kafkaTemplate.send(TOPIC, flightNumber);
        response.put("message", "Запрос для бронирования отправлен, номер: " + flightNumber);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
