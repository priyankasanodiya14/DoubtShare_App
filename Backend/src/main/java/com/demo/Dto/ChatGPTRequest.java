package com.demo.Dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class ChatGPTRequest {

    private String model;
    private List<Message> messages;

    public ChatGPTRequest(String model,String prompt ) {
        this.model=model;
        this.messages= new ArrayList<>();
        this.messages.add(new Message("user",prompt));
    }
}
