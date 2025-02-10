package ru.itis.notificationservice.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.itis.notificationservice.service.BaggageService;

@Slf4j
@Component
@RequiredArgsConstructor
public class GrpcServer {

    private Server server;

    @Value("${grpc.server.port}")
    private Integer port;

    @PostConstruct
    public void start() throws Exception {
        server = ServerBuilder.forPort(port)
                .addService(new BaggageService())
                .build()
                .start();
        log.info("Server started on port: {}", port);
    }

    @PreDestroy
    public void stop() {
        if (server != null) {
            server.shutdown();
            log.info("Server stopped");
        }
    }
}