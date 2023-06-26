package com.example.serverside.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
public class ServerController {

    @Autowired
    WebClient webClient;

    @GetMapping("server")
    public String serverData() {
        return "Hello from server";
    }


    @GetMapping("aa")
    public String she() {

        Mono<String> data = webClient.get()
                .uri("https://localhost:8081/hel")
                .retrieve()
                .bodyToMono(String.class);

        return data.block();
    }
}
