package com.exe.carenest.gateway.model;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface GatewayRouteRepository extends ReactiveMongoRepository<GatewayRoute, String> {
    Flux<GatewayRoute> findAllByEnabled(Boolean enabled);
}