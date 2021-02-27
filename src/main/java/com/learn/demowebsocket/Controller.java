package com.learn.demowebsocket;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;

import java.security.Principal;

@org.springframework.stereotype.Controller
public class Controller {
    @MessageMapping("/greeting")
    @SendTo("/topic/greeting")
    public Greeting broadcastNews(@Payload Greeting message) {
        System.out.println(message.getContent());
        return message;
    }

    @MessageMapping("/onlylisten")
    public void listen(@Payload Greeting message){
        System.out.println(message.getContent());
    }



//    @MessageMapping("/news")
//    public void broadcastNews(@Payload String message) {
//        this.simpMessagingTemplate.convertAndSend("/topic/news", message)
//    }

    @MessageMapping("/sayhi")
    @SendToUser("/queue/sayhi")
    public String reply(@Payload String message,
                        Principal user) {
        return  "Hello " + message;
    }
}
