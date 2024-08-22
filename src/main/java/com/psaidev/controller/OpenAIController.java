package com.psaidev.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
public class OpenAIController {

    private final ObjectMapper objectMapper;
    private final WebClient webClient;
    @Value("${openai.model}")
    private String model;

    @Autowired
    public OpenAIController(ObjectMapper objectMapper, WebClient.Builder webClientBuilder,
                            @Value("${openai.api.url}") String apiUrl,
                            @Value("${openai.api.key}") String apiKey) {
        this.objectMapper = objectMapper;
        this.webClient = webClientBuilder.baseUrl(apiUrl)
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .build();
    }

    @PostMapping(value = "/v1/chat/askQuestion")
    public Mono<JsonNode> sendAnswer(@RequestBody JsonNode jsonNode) {
        ObjectNode objectNode = objectMapper.createObjectNode()
        .put("message", jsonNode.get("question").asText())
        .set("options", objectMapper.createObjectNode().put("model", model));
        return webClient.post()
                .bodyValue(objectNode)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .flatMap(data -> {
            JsonNode messages = data.get("data").get("messages");
            for (JsonNode message : messages) {
                if ("assistant".equals(message.get("role").asText())) {
                    return Mono.just(message.get("content"));
                }
            }
            return Mono.empty();
        });
    }


}
