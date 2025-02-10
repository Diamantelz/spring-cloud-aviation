package ru.itis.notificationservice.service;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import ru.itis.baggage.service.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@GrpcService
public class BaggageService extends BaggageServiceGrpc.BaggageServiceImplBase {

    private final Map<Integer, List<String>> usersBaggage = new ConcurrentHashMap<>(Map.of(
            1, List.of("baggage1"),
            2, List.of("baggage2"),
            3, List.of("baggage3")
    ));

    @Override
    public void getUsersBaggageById(UserRequest request, StreamObserver<UserResponse> responseObserver) {
        int userId = request.getId();
        List<String> baggage = usersBaggage.get(userId);
        if (baggage != null) {
            UserResponse response = UserResponse.newBuilder().addAllEvents(baggage).build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } else {
            responseObserver.onError(Status.NOT_FOUND
                    .withDescription("User with ID " + userId + " not found.")
                    .asRuntimeException());
            log.info("User with ID {} not found", userId);
        }
        log.info("User with ID {} found", userId);
    }

    @Override
    public void addBaggage(AddBaggageRequest request, StreamObserver<AddBaggageResponse> responseObserver) {
        int userId = request.getUserId();
        String baggageItem = request.getBaggageItem();

        usersBaggage.computeIfAbsent(userId, k -> new ArrayList<>()).add(baggageItem);

        AddBaggageResponse response = AddBaggageResponse.newBuilder().build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
        log.info("User with ID {} has been added.", userId);
    }
}
