package com.demo.Controller;

import com.demo.Dto.ChatGPTRequest;
import com.demo.Dto.ChatGPTResponse;
import com.demo.Entity.MyUser;
import com.demo.Service.MyUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


@RestController
@Slf4j
@RequestMapping("/students")
public class ChatBotController {

    @Autowired
    private  MyUserService myUserService;
    @Value("${openai.model}")
    private String model;

    @Autowired
    private RestTemplate template;

    @Value("${openai.api.url}")
    private String apiURL;

    @GetMapping("/chat")
    public ResponseEntity<ChatGPTResponse> chat(Authentication auth,@RequestParam("prompt") String prompt) {
        log.info("Prompt: {}",prompt);
        MyUser student = myUserService.findByEmail(auth.getName()).get();


        ChatGPTRequest request = new ChatGPTRequest(model,prompt +" chat and clear doubts of the students");

        log.info("Request: {}",request);
        ChatGPTResponse chatgptResponse =template.postForObject(apiURL, request, ChatGPTResponse.class);
        return new ResponseEntity<>( chatgptResponse, HttpStatus.CREATED);
    }
}
