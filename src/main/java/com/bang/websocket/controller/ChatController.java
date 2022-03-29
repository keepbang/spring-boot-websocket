package com.bang.websocket.controller;

import com.bang.websocket.dto.RequestMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/send")
    public void send(RequestMessage requestMessage) {
        log.info("receive chatting message : {}", requestMessage.toString());
        simpMessagingTemplate.convertAndSend("/sub/room/" + requestMessage.getRoomId(), requestMessage);
    }


}
