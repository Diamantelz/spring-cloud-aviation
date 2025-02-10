package ru.itis.baggageservice.controller;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itis.baggage.service.AddBaggageRequest;
import ru.itis.baggageservice.client.BaggageServiceClient;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/baggage")
@RequiredArgsConstructor
public class BaggageController {

    private final BaggageServiceClient baggageServiceClient;

    @GetMapping("/{user-id}")
    public ResponseEntity<List<String>> getUsersBaggage(@PathVariable("user-id") int id) {
        try {
            List<String> baggage = baggageServiceClient.getUsersBaggageById(id);
            return ResponseEntity.ok(baggage);
        } catch (StatusRuntimeException e) {
            if (e.getStatus().getCode() == Status.Code.NOT_FOUND) {
                return ResponseEntity.notFound().build();
            } else {
                log.error("GRPC error", e);
                return ResponseEntity.internalServerError().build();
            }
        }
    }

    @PostMapping("/add/{user-id}/{baggage}")
    public void addBaggage(@PathVariable("user-id") int userId, @PathVariable("baggage") String baggage) {
        AddBaggageRequest request = AddBaggageRequest.newBuilder()
                .setUserId(userId)
                .setBaggageItem(baggage)
                .build();
        baggageServiceClient.addBaggage(request);
        log.info("Added baggage: {} for user with id: {}", baggage, userId);
    }
}
