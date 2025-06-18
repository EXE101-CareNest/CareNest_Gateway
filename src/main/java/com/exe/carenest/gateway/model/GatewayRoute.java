package com.exe.carenest.gateway.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "gateway_routes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GatewayRoute {
    
    @Id
    private String id;

    @Field("route_id")
    private String routeId;
    
    @Field("uri")
    private String uri;
    
    @Field("predicates")
    private String predicates; // JSON string
    
    @Field("filters") 
    private String filters;    // JSON string
    
    @Field("order_no")
    private Integer orderNo = 0;
    
    @Field("enabled")
    private Boolean enabled = true;
    
    // Constructor để dễ tạo object
    public GatewayRoute(String routeId, String uri, String predicates, String filters) {
        this.routeId = routeId;
        this.uri = uri;
        this.predicates = predicates;
        this.filters = filters;
        this.enabled = true;
        this.orderNo = 0;
    }
}
