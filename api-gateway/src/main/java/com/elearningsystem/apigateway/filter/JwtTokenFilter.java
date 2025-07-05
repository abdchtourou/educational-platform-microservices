package com.elearningsystem.apigateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class JwtTokenFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            log.info("🔐 Forwarding JWT token to downstream service: {}", 
                     authHeader.substring(0, Math.min(authHeader.length(), 20)) + "...");
            
            // Create a new request with the Authorization header
            ServerWebExchange modifiedExchange = exchange.mutate()
                    .request(exchange.getRequest().mutate()
                            .header(HttpHeaders.AUTHORIZATION, authHeader)
                            .build())
                    .build();
            
            return chain.filter(modifiedExchange);
        } else {
            log.debug("🔐 No Bearer token found in request to: {}", 
                     exchange.getRequest().getPath().value());
        }
        
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -2; // Execute before LoggingFilter
    }
} 