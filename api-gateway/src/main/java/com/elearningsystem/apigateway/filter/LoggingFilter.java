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
public class LoggingFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getPath().value();
        String method = exchange.getRequest().getMethod().name();
        String host = exchange.getRequest().getHeaders().getFirst("Host");
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        
        log.info("🌐 Gateway Request: {} {} from host: {}", method, path, host);
        if (authHeader != null) {
            log.debug("🔐 Authorization header present: {}", 
                     authHeader.substring(0, Math.min(authHeader.length(), 20)) + "...");
        } else {
            log.debug("🔐 No Authorization header found");
        }
        
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            int statusCode = exchange.getResponse().getStatusCode().value();
            log.info("🔄 Gateway Response: {} {} -> Status: {}", method, path, statusCode);
        }));
    }

    @Override
    public int getOrder() {
        return -1; 
    }
} 