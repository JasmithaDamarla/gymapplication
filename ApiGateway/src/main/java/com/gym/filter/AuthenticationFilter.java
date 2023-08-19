package com.gym.filter;

import java.util.Objects;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;

import com.gym.exceptions.UnAuthorizedException;
import com.gym.proxy.WebFluxAuthProxy;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

	private final RouteValidator validator;
	private final WebFluxAuthProxy authenticationProxy;

	public AuthenticationFilter(RouteValidator validator, WebFluxAuthProxy authenticationProxy) {
		super(Config.class);
		this.validator = validator;
		this.authenticationProxy = authenticationProxy;
	}

	@Override
	public GatewayFilter apply(Config config) {
		return (exchange, chain) -> {
			log.info("Received request " + exchange.getRequest().toString());
			if (validator.isSecured.test(exchange.getRequest())) {
				if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
					throw new UnAuthorizedException("Missing authorization header");
				}

				String authHeader = Objects
						.requireNonNull(exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION)).get(0);
				if (authHeader != null && authHeader.startsWith("Bearer ")) {
					authHeader = authHeader.substring(7);
				}
				log.info("Received token : " + authHeader);

				return validateToken(authHeader).flatMap(valid -> {
					if (valid) {
						log.info("Valid token received");
						return chain.filter(exchange);
					} else {
						log.info("Token validation failed");
						ServerHttpResponse response = exchange.getResponse();
						response.setStatusCode(HttpStatus.UNAUTHORIZED);
						return response.setComplete();
					}
				});
			}

			return chain.filter(exchange);
		};
	}

	private Mono<Boolean> validateToken(String token) {
		log.info("Validating token : " + token);
		return authenticationProxy.validateToken(token).map(response -> response.equals("Token is valid")); 
	}

	public static class Config {
		// Empty class for configuration if needed
	}
}
