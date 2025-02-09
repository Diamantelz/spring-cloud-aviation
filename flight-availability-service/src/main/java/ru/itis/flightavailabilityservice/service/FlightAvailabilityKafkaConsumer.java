package ru.itis.flightavailabilityservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

@Service
public class FlightAvailabilityKafkaConsumer {

    private static final String TOPIC = "flight-booking-requests";
    private static final String BOOKING_STATUS_TOPIC = "booking-status";
    private final Set<String> bookedFlights = ConcurrentHashMap.newKeySet();
    private final Predicate<String> flightNumberValidator = flightNumber ->
            flightNumber.startsWith("AA") || flightNumber.startsWith("UA") || flightNumber.matches("\\d{4}");

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private final Gson gson = new Gson();

    @KafkaListener(topics = TOPIC, groupId = "flight-availability-group")
    public void listen(String flightNumber) {
        Map<String, String> bookingStatus = new HashMap<>();
        bookingStatus.put("flightNumber", flightNumber);

        if (bookedFlights.contains(flightNumber)) {
            bookingStatus.put("message", "Авиарейс с номером " + flightNumber + " уже забронирован.");
        } else if (flightNumberValidator.test(flightNumber)) {
            bookedFlights.add(flightNumber);
            bookingStatus.put("message", "Авиарейс с номером " + flightNumber + " успешно забронирован.");
        } else {
            bookingStatus.put("message", "Авиарейс с номером " + flightNumber + " недоступен. Проверьте номер и повторите попытку.");
        }
        kafkaTemplate.send(BOOKING_STATUS_TOPIC, gson.toJson(bookingStatus));
        System.out.println("Статус бронирования: " + gson.toJson(bookingStatus));
    }
}
