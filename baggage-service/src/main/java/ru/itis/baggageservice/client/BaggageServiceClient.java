package ru.itis.baggageservice.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itis.baggage.service.*;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BaggageServiceClient {

    private final BaggageServiceGrpc.BaggageServiceBlockingStub baggageService;

    public List<String> getUsersBaggageById(int id) {
        return baggageService.getUsersBaggageById(UserRequest.newBuilder()
                .setId(id)
                .build()).getEventsList();
    }

    public AddBaggageResponse addBaggage(AddBaggageRequest request) {
        return baggageService.addBaggage(request);
    }
}
