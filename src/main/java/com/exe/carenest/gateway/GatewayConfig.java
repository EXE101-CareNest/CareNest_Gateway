package com.exe.carenest.gateway;

import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Configuration
public class GatewayConfig {

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    // Filter gán correlation-id và timestamp
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public GlobalFilter preRoutingFilter() {
        return (exchange, chain) -> {
            var request = exchange.getRequest();
            String existing = request.getHeaders().getFirst("X-Correlation-Id");
            final String correlationId = (existing == null || existing.isBlank())
                    ? java.util.UUID.randomUUID().toString()
                    : existing;

            var mutatedRequest = request.mutate()
                    .header("X-Correlation-Id", correlationId)
                    .header("X-Gateway-Timestamp", String.valueOf(System.currentTimeMillis()))
                    .build();

            var mutatedExchange = exchange.mutate().request(mutatedRequest).build();

            return chain.filter(mutatedExchange)
                    .then(Mono.fromRunnable(() -> {
                        var response = mutatedExchange.getResponse();
                        response.getHeaders().add("X-Correlation-Id", correlationId);
                    }));
        };
    }

    // Filter kiểm tra quyền qua auth-service
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE + 1)
    public GlobalFilter authPermissionFilter(WebClient.Builder webClientBuilder) {
        return (exchange, chain) -> {
            var request = exchange.getRequest();
            String token = request.getHeaders().getFirst("Authorization");
            String path = request.getPath().toString();

            return webClientBuilder.build()
                    .get()
                    .uri("lb://authorize-service/api/permission/check?path=" + path)
                    .header("Authorization", token)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .flatMap(allowed -> {
                        if (!allowed) {
                            exchange.getResponse().setStatusCode(org.springframework.http.HttpStatus.FORBIDDEN);
                            return exchange.getResponse().setComplete();
                        }
                        return chain.filter(exchange);
                    });
        };
    }
}
