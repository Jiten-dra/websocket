package com.learn.demowebsocket;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;

import java.security.Principal;

@org.springframework.stereotype.Controller
public class Controller {
    @MessageMapping("/news")
    @SendTo("/topic/news")
    public String broadcastNews(@Payload String message) {
        return message;
    }

//    @MessageMapping("/news")
//    public void broadcastNews(@Payload String message) {
//        this.simpMessagingTemplate.convertAndSend("/topic/news", message)
//    }

    @MessageMapping("/greetings")
    @SendToUser("/queue/greetings")
    public String reply(@Payload String message,
                        Principal user) {
        return  "Hello " + message;
    }
}
