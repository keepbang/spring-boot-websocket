package com.bang.websocket.controller;

import com.bang.websocket.dto.RequestDto;
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
    public void receive(RequestDto requestDto) {
        log.info("receive chatting message : {}", requestDto.toString());
        simpMessagingTemplate.convertAndSend("/sub/room/" + requestDto.getRoomId(), requestDto);
    }


}
