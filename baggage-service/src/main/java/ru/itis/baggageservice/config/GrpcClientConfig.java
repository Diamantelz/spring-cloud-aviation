package ru.itis.baggageservice.config;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.itis.baggage.service.BaggageServiceGrpc;

@Configuration
public class GrpcClientConfig {

    @Value("${grpc.server.port}")
    private int port;

    @Bean
    public BaggageServiceGrpc.BaggageServiceBlockingStub baggageServiceGrpcBlockingStub() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", port)
                .usePlaintext()
                .build();
        return BaggageServiceGrpc.newBlockingStub(channel);
    }
}