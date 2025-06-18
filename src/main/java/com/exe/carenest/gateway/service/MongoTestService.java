package com.exe.carenest.gateway.service;

import com.exe.carenest.gateway.model.GatewayRoute;
import com.exe.carenest.gateway.model.GatewayRouteRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class MongoTestService {
    
    @Autowired
    private GatewayRouteRepository routeRepository;
    
    @PostConstruct
    public void testConnection() {
        routeRepository.count()
            .doOnNext(count -> {
                System.out.println("✅ MongoDB connection successful! Found " + count + " routes.");
                if (count == 0) {
                    createSampleRoute();
                }
            })
            .doOnError(e -> {
                System.err.println("❌ MongoDB connection failed: " + e.getMessage());
                e.printStackTrace();
            })
            .subscribe();
    }
    
    private void createSampleRoute() {
        GatewayRoute sampleRoute = new GatewayRoute(
            "auth-service",
            "http://localhost:8082",
            "[{\"name\":\"Path\",\"args\":{\"pattern\":\"/api/auth/**\"}}]",
            "[{\"name\":\"StripPrefix\",\"args\":{\"parts\":\"1\"}}]"
        );
        
        routeRepository.save(sampleRoute)
            .doOnSuccess(saved -> System.out.println("✅ Sample route created successfully!"))
            .doOnError(e -> System.err.println("❌ Failed to create sample route: " + e.getMessage()))
            .subscribe();
    }
}