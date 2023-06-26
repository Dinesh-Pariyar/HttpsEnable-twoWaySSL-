package com.example.clientside.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
public class MyController {

    @Autowired
    WebClient webClient;


    @GetMapping("client")
    public String gatherDataFromServer(){
        System.out.println("hello");
        Mono<String > dateFromServer = webClient.get()
                .uri("https://localhost:8082/server")
                .retrieve()
                .bodyToMono(String.class);
        return dateFromServer.block();
    }


    @GetMapping("hel")
    public String just(){
        return "hello from client";
    }
}
