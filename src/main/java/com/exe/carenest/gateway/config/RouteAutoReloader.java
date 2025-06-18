package com.exe.carenest.gateway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RouteAutoReloader {

//    @Autowired
//    private GatewayRouteRefresher refresher;
//
//    @Scheduled(fixedDelay = 10000) // mỗi 10 giây
//    public void reload() {
//        log.info("Refreshing routes from DB...");
//        refresher.refreshRoutes();
//    }
}