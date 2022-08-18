package com.example.apigateway.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.example.apigateway.config.logger.model.Logger;
import com.example.apigateway.config.logger.service.LoggerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DefaultDataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.ByteArrayOutputStream;
import java.nio.channels.Channels;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;

@Slf4j
@Component
public class RequestRecordFilter implements GlobalFilter, Ordered, GatewayFilter {
    private Logger logger;
    long startTime;
    @Autowired
    private LoggerService loggerService;
    @Value("${api.constant.elementWithoutToken}")
    private List<String> apiEndpointsWithoutToken;
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String uri = exchange.getRequest().getURI().toString();
        String path = exchange.getRequest().getPath().toString();
        ServerHttpResponse response = exchange.getResponse();
        startTime = System.currentTimeMillis();
        ServerHttpRequest request = exchange.getRequest();
        if (!apiEndpointsWithoutToken.contains(path)) {
            if (!request.getHeaders().containsKey("Authorization")) {
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                logger = new Logger();
                logger.setHttpResult(HttpStatus.UNAUTHORIZED.value());
                logger.setUri(uri);
                logger.setRequestId(request.getId());
                logger.setHeaderRequest(request.getHeaders().toString());
                logger.setHeaderResponse(response.getHeaders().toString());
                logger.setParameter(request.getQueryParams().toString());
                logger.setRequestData(request.getBody().toString());
                logger.setResponseData("Authorization header is missing");
                String elapsedTime = String.valueOf(System.currentTimeMillis() - startTime);
                logger.setProcessTime(elapsedTime);
                logger.setMethod(request.getMethodValue());
                logger.setParameter(request.getQueryParams().toString());
                logger.setCreatedAt(new Date());
                loggerService.saveLogger(logger);
                return response.writeWith(Mono.just(response.bufferFactory().wrap(
                        "Authorization header is missing".getBytes())));
            }
            final String token = request.getHeaders().getOrEmpty("Authorization").get(0);
            try {
                String tokenGetter = token.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                verifier.verify(tokenGetter); //decode jwt
            } catch (Exception e) {
                response.setStatusCode(HttpStatus.BAD_REQUEST);
                logger = new Logger();
                logger.setHttpResult(HttpStatus.BAD_REQUEST.value());
                logger.setUri(uri);
                logger.setRequestId(request.getId());
                logger.setHeaderRequest(request.getHeaders().toString());
                logger.setHeaderResponse(response.getHeaders().toString());
                logger.setParameter(request.getQueryParams().toString());
                logger.setRequestData(request.getBody().toString());
                logger.setResponseData("Authorization header is missing");
                String elapsedTime = String.valueOf(System.currentTimeMillis() - startTime);
                logger.setProcessTime(elapsedTime);
                logger.setMethod(request.getMethodValue());
                logger.setParameter(request.getQueryParams().toString());
                logger.setCreatedAt(new Date());
                loggerService.saveLogger(logger);
                return response.writeWith(Mono.just(response.bufferFactory().wrap(
                        "Invalid token".getBytes())));
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
                        String elapsedTime = String.valueOf(System.currentTimeMillis() - startTime);
                        logger.setProcessTime(elapsedTime);
                        logger.setCreatedAt(new Date());
                        loggerService.saveLogger(logger);
                        return dataBufferFactory.wrap(responseBody.getBytes());
                    })).onErrorResume(err -> {
                        logger.setResponseData(err.getMessage());
                        log.error("error while decorating Response: {}", err.getMessage());
                        String elapsedTime = String.valueOf(System.currentTimeMillis() - startTime);
                        logger.setProcessTime(elapsedTime);
                        logger.setCreatedAt(new Date());
                        loggerService.saveLogger(logger);
                        return Mono.empty();
                    });
                }
                String elapsedTime = String.valueOf(System.currentTimeMillis() - startTime);
                logger.setProcessTime(elapsedTime);
                logger.setCreatedAt(new Date());
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
