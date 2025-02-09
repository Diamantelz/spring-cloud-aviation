package ru.itis.flightbookingservice.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class BookingStatusConsumer {
    private final Gson gson = new Gson();

    private final Cache<String, Map<String, String>> bookingStatusCache;

    @Autowired
    public BookingStatusConsumer(Cache<String, Map<String, String>> bookingStatusCache) {
        this.bookingStatusCache = bookingStatusCache;
    }

    @KafkaListener(topics = "booking-status", groupId = "booking-status-group")
    public void listen(String message) {
        java.lang.reflect.Type type = new TypeToken<Map<String,String>>(){}.getType();
        Map<String, String> bookingStatus = gson.fromJson(message, type);

        System.out.println("Получен статус бронирования: " + bookingStatus);
        bookingStatusCache.put(bookingStatus.get("flightNumber"), bookingStatus);
    }
}
