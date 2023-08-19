package com.gym.proxy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Component
public class WebFluxAuthProxy {
	
	private WebClient.Builder webClientBuilder;
	
	@Autowired
	public WebFluxAuthProxy(WebClient.Builder webClientBuilder) {
		this.webClientBuilder = webClientBuilder;
	}
	
	public Mono<String> validateToken(String token) {
        return webClientBuilder.build().get()
                .uri("http://auth-service/auth/validate?token=" + token)
                .retrieve()
                .bodyToMono(String.class)
                .subscribeOn(Schedulers.boundedElastic());
    }
}
