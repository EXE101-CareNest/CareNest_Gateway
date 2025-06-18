package com.exe.carenest.gateway.config;

import com.exe.carenest.gateway.model.GatewayRoute;
import com.exe.carenest.gateway.model.GatewayRouteRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.net.URI;
import java.util.List;

@Component
public class DatabaseRouteLocator implements RouteDefinitionLocator {

    @Autowired
    private GatewayRouteRepository routeRepository;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        return routeRepository.findAllByEnabled(true)
            .map(this::convertToRouteDefinition)
            .doOnError(e -> System.err.println("Error loading routes: " + e.getMessage()));
    }

    private RouteDefinition convertToRouteDefinition(GatewayRoute gatewayRoute) {
        RouteDefinition definition = new RouteDefinition();
        definition.setId(gatewayRoute.getRouteId());
        definition.setUri(URI.create(gatewayRoute.getUri()));
        definition.setOrder(gatewayRoute.getOrderNo());

        try {
            // Parse predicates
            List<PredicateDefinition> predicates = objectMapper.readValue(
                gatewayRoute.getPredicates(), 
                new TypeReference<List<PredicateDefinition>>() {}
            );
            definition.setPredicates(predicates);

            // Parse filters
            List<FilterDefinition> filters = objectMapper.readValue(
                gatewayRoute.getFilters(), 
                new TypeReference<List<FilterDefinition>>() {}
            );
            definition.setFilters(filters);

        } catch (JsonProcessingException e) {
            System.err.println("Error parsing route " + gatewayRoute.getRouteId() + ": " + e.getMessage());
            // Return empty route definition on error
            definition.setPredicates(List.of());
            definition.setFilters(List.of());
        }

        return definition;
    }

    @EventListener(RefreshRoutesEvent.class)
    public void onRefreshRoutes(RefreshRoutesEvent event) {
        System.out.println("Routes refreshed from database");
    }

    public void refreshRoutes() {
        eventPublisher.publishEvent(new RefreshRoutesEvent(this));
    }
}