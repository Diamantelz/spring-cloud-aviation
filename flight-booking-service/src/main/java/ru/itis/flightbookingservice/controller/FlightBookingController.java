package ru.itis.flightbookingservice.controller;

import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itis.flightbookingservice.service.FlightBookingKafkaProducer;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/booking")
public class FlightBookingController {

    private final FlightBookingKafkaProducer producer;
    private final Cache<String, Map<String, String>> bookingStatusCache;

    @Autowired
    public FlightBookingController(FlightBookingKafkaProducer producer, Cache<String, Map<String, String>> bookingStatusCache) {
        this.producer = producer;
        this.bookingStatusCache = bookingStatusCache;
    }

    @PostMapping("/{flightNumber}")
    public ResponseEntity<Map<String, String>> bookFlight(@PathVariable String flightNumber) {
        return producer.sendMessage(flightNumber);
    }

    @GetMapping("/status/{flightNumber}")
    public ResponseEntity<Map<String, String>> getBookingStatus(@PathVariable String flightNumber) {
        Map<String, String> bookingStatus = bookingStatusCache.getIfPresent(flightNumber);
        if (bookingStatus == null) {
            bookingStatus = new HashMap<>();
            bookingStatus.put("message", "Бронирование с номером " + flightNumber + " свободно.");
        }
        return new ResponseEntity<>(bookingStatus, HttpStatus.OK);
    }

    @GetMapping("/last-message")
    public ResponseEntity<String> getLastMessage() {
        Map<String, String> lastMessageMap = bookingStatusCache.getIfPresent("lastMessage");
        String lastMessage = lastMessageMap != null ? lastMessageMap.toString() : "Статус бронирования недоступен.";
        return ResponseEntity.ok(lastMessage);
    }
}