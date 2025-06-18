package com.exe.carenest.gateway.controller;

import com.exe.carenest.gateway.config.GatewayRouteRefresher;
import com.exe.carenest.gateway.model.GatewayRoute;
import com.exe.carenest.gateway.model.GatewayRouteRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.result.view.Rendering;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Controller
@RequestMapping("/admin/gateway")
public class GatewayManagementController {

    @Autowired
    private GatewayRouteRepository routeRepository;

    @Autowired
    private GatewayRouteRefresher gatewayRouteRefresher;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping
    public Mono<Rendering> index() {
        return routeRepository.findAll()
            .collectList()
            .map(routes -> Rendering.view("admin/gateway/index")
                .modelAttribute("routes", routes)
                .build());
    }

    @GetMapping("/create")
    public Mono<Rendering> createForm() {
        return Mono.just(Rendering.view("admin/gateway/form")
            .modelAttribute("route", new GatewayRoute())
            .build());
    }

    @GetMapping("/edit/{id}")
    public Mono<Rendering> editForm(@PathVariable String id) {
        return routeRepository.findById(id)
            .map(route -> Rendering.view("admin/gateway/form")
                .modelAttribute("route", route)
                .build())
            .defaultIfEmpty(Rendering.redirectTo("/admin/gateway").build());
    }

    @PostMapping("/save")
    public Mono<String> save(@ModelAttribute GatewayRoute route, ServerWebExchange exchange) {
        try {
            // Validate JSON format
            objectMapper.readTree(route.getPredicates());
            objectMapper.readTree(route.getFilters());

            return routeRepository.save(route)
                .doOnSuccess(savedRoute -> {
                    // Refresh routes sau khi save thành công
                    gatewayRouteRefresher.refreshRoutes();
                })
                .then(Mono.just("redirect:/admin/gateway"))
                .onErrorReturn("redirect:/admin/gateway");
        } catch (JsonProcessingException e) {
            return Mono.just("redirect:/admin/gateway");
        }
    }

    @PostMapping("/toggle/{id}")
    public Mono<String> toggleStatus(@PathVariable String id) {
        return routeRepository.findById(id)
            .flatMap(route -> {
                route.setEnabled(!route.getEnabled());
                return routeRepository.save(route);
            })
            .doOnSuccess(savedRoute -> {
                // Refresh routes sau khi toggle status thành công
                gatewayRouteRefresher.refreshRoutes();
            })
            .then(Mono.just("redirect:/admin/gateway"));
    }

    @PostMapping("/delete/{id}")
    public Mono<String> delete(@PathVariable String id) {
        return routeRepository.deleteById(id)
            .doOnSuccess(result -> {
                // Refresh routes sau khi delete thành công
                gatewayRouteRefresher.refreshRoutes();
            })
            .then(Mono.just("redirect:/admin/gateway"));
    }
}
