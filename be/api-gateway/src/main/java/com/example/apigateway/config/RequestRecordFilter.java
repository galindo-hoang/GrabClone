package com.example.apigateway.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.example.apigateway.config.logger.model.Logger;
import com.example.apigateway.config.logger.service.LoggerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DefaultDataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.ByteArrayOutputStream;
import java.nio.channels.Channels;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.Predicate;

@Slf4j
@Component
public class RequestRecordFilter implements GlobalFilter, Ordered, GatewayFilter {
    private Logger logger;
    @Autowired
    private LoggerService loggerService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().toString();
        ServerHttpResponse response = exchange.getResponse();
        ServerHttpRequest request = exchange.getRequest();
        final List<String> apiEndpoints = List.of("http://localhost:8085/refresh-token", "" +
                "http://localhost:8085/login",
                "http://localhost:8085/register",
                "http://localhost:8085/api/v1/sms/register",
                "http://localhost:8085/api/v1/sms/validate");
        if (!apiEndpoints.contains(path)) {
            if (!request.getHeaders().containsKey("Authorization")) {
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
                response.setStatusCode(HttpStatus.BAD_REQUEST);
                return response.setComplete();
            }
        }
        DataBufferFactory dataBufferFactory = response.bufferFactory();
        // log the request body
        ServerHttpRequest decoratedRequest = getDecoratedRequest(request);
        // log the response body
        ServerHttpResponseDecorator decoratedResponse = getDecoratedResponse(path, response, request, dataBufferFactory);
        ServerWebExchange serverWebExchange = exchange.mutate().request(decoratedRequest).response(decoratedResponse).build();
        return chain.filter(serverWebExchange);
    }

    private ServerHttpResponseDecorator getDecoratedResponse(String path, ServerHttpResponse response, ServerHttpRequest request, DataBufferFactory dataBufferFactory) {
        return new ServerHttpResponseDecorator(response) {
            @Override
            public Mono<Void> writeWith(final Publisher<? extends DataBuffer> body) {
                logger.setHttpResult(response.getStatusCode().value());
                logger.setHeaderResponse(response.getHeaders().toString());
                if (body instanceof Flux) {
                    Flux<? extends DataBuffer> fluxBody = (Flux<? extends DataBuffer>) body;
                    return super.writeWith(fluxBody.buffer().map(dataBuffers -> {
                        DefaultDataBuffer joinedBuffers = new DefaultDataBufferFactory().join(dataBuffers);
                        byte[] content = new byte[joinedBuffers.readableByteCount()];
                        joinedBuffers.read(content);
                        String responseBody = new String(content, StandardCharsets.UTF_8);//MODIFY RESPONSE and Return the Modified response
                        log.info("requestId: {}, method: {}, url: {}, \nresponse body :{}", request.getId(), request.getMethodValue(), request.getURI(), responseBody);
                        logger.setResponseData(responseBody);
                        System.out.println(logger);
                        loggerService.saveLogger(logger);
                        Logger logger1=loggerService.findLogger(40).orElse(null);
                        System.out.println("HUY Mo"+logger1);
                        return dataBufferFactory.wrap(responseBody.getBytes());
                    })).onErrorResume(err -> {
                        logger.setResponseData(err.getMessage());
                        log.error("error while decorating Response: {}", err.getMessage());
                        loggerService.saveLogger(logger);
                        return Mono.empty();
                    });
                }
                loggerService.saveLogger(logger);
                return super.writeWith(body);
            }
        };
    }

    private ServerHttpRequest getDecoratedRequest(ServerHttpRequest request) {

        return new ServerHttpRequestDecorator(request) {
            @Override
            public Flux<DataBuffer> getBody() {
                logger = new Logger();
                logger.setRequestId(request.getId());
                logger.setRequestIP(request.getRemoteAddress().getAddress().getHostAddress());
                logger.setMethod(request.getMethodValue());
                logger.setUri(request.getURI().toString());
                logger.setParameter(request.getQueryParams().toString());
                logger.setHeaderRequest(request.getHeaders().toString());
                log.info("requestId: {}, method: {} , url: {}", request.getId(), request.getMethodValue(), request.getURI());
                return super.getBody().publishOn(Schedulers.boundedElastic()).doOnNext(dataBuffer -> {
                    try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
                        Channels.newChannel(byteArrayOutputStream).write(dataBuffer.asByteBuffer().asReadOnlyBuffer());
                        String requestBody = IOUtils.toString(byteArrayOutputStream.toByteArray(),
                                StandardCharsets.UTF_8.toString());//MODIFY REQUEST and Return the Modified request
                        logger.setRequestData(requestBody);
                        log.info("for requestId: {}, request body :{}", request.getId(), requestBody);
                    } catch (Exception e) {
                        logger.setRequestData(e.getMessage());
                        log.error(e.getMessage());
                    }
                });
            }
        };
    }

    @Override
    public int getOrder() {
        return -2;
    }
}