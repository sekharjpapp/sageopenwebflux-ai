/*
package com.psaidev.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Controller
@Slf4j
public class ChatController {
    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final WebClient webClient;
    @Value("${openai.model}")
    private String model;

    public ChatController(@Qualifier("openaiRestClient") WebClient webClient) {
        this.webClient = webClient;
    }


    @GetMapping("/")
    public String showForm(Model model) {
        return "form";
    }


    @PostMapping(value = "/v1/chat/askQuestion")
    @ResponseBody
    public Mono<JsonNode> sendAnswer(@RequestBody JsonNode jsonNode) {
        ObjectNode objectNode = OBJECT_MAPPER.createObjectNode().put("message", jsonNode.get("question").asText()).set("options", OBJECT_MAPPER.createObjectNode().put("model", model));
        return webClient.post().bodyValue(objectNode).retrieve().bodyToMono(JsonNode.class).flatMap(data -> {
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
*/
