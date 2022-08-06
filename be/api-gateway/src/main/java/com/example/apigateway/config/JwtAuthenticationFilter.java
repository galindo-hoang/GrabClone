package com.example.apigateway.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Predicate;

@Component
public class JwtAuthenticationFilter implements GatewayFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        final List<String> apiEndpoints = List.of("/refresh-token", "/login");
        Predicate<ServerHttpRequest> isApiSecured = r -> apiEndpoints.stream()
                .noneMatch(uri -> r.getURI().getPath().contains(uri));
        if (isApiSecured.test(request)) {
            if (!request.getHeaders().containsKey("Authorization")) {
                ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.setComplete();
            }
            final String token = request.getHeaders().getOrEmpty("Authorization").get(0);
            try {
                String tokenGetter = token.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                verifier.verify(tokenGetter); //decode jwt
            } catch (Exception e) {
                ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.BAD_REQUEST);
                return response.setComplete();
            }
        }
        return chain.filter(exchange);
    }

}