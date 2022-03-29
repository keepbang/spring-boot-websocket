package com.bang.websocket.chatting;

import com.bang.websocket.dto.RequestMessage;
import com.bang.websocket.handler.StompFrameHandlerImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ChatWebSocketTest {

    @LocalServerPort
    private Integer port;

    private WebSocketStompClient client;

    @BeforeEach
    public void setUp() {
        // webSocket client 생성 & setting
        client = WebSocket_생성();
        client.setMessageConverter(new MappingJackson2MessageConverter());
    }

    private WebSocketStompClient WebSocket_생성() {
        StandardWebSocketClient standardWebSocketClient = new StandardWebSocketClient();
        WebSocketTransport webSocketTransport = new WebSocketTransport(standardWebSocketClient);
        List<Transport> transports = Collections.singletonList(webSocketTransport);
        SockJsClient sockJsClient = new SockJsClient(transports);

        return new WebSocketStompClient(sockJsClient);
    }

    @Test
    @DisplayName("메세지를 받을 수 있다.")
    public void WebSocketSendTest() throws Exception {
        String webSocketUrl = "ws://localhost:" + port + "/ws/chat/connection";
        long senderId = 1L;
        long receiverId = 2L;
        long roomId = 1L;
        String message = "hello~~";
        RequestMessage requestMessage = new RequestMessage(senderId, receiverId, roomId, message);
        StompFrameHandlerImpl<RequestMessage> stompHandler = new StompFrameHandlerImpl<>(requestMessage);

        // connection
        StompSession session = client.connect(webSocketUrl, new StompSessionHandlerAdapter() {
        }).get(60, TimeUnit.SECONDS);

        session.subscribe("/sub/room/" + roomId, stompHandler);

        StompHeaders headers = new StompHeaders();
        headers.setDestination("/pub/send");
        session.send(headers, requestMessage);

        RequestMessage response = stompHandler.pollMessageByQueue(5, TimeUnit.SECONDS);

        assertThat(response).isEqualTo(requestMessage);
    }

    @Test
    @DisplayName("채팅방을 구독하고 있지 않으면 메세지를 받을 수 없다.")
    public void WebSocketSendNotReceive() throws Exception {
        String webSocketUrl = "ws://localhost:" + port + "/ws/chat/connection";
        long senderId = 1L;
        long receiverId = 2L;
        long roomId = 2L;
        String message = "room number 2";
        RequestMessage requestMessage = new RequestMessage(senderId, receiverId, roomId, message);
        StompFrameHandlerImpl<RequestMessage> stompHandler = new StompFrameHandlerImpl<>(requestMessage);

        // connection
        StompSession session = client.connect(webSocketUrl, new StompSessionHandlerAdapter() {
        }).get(60, TimeUnit.SECONDS);

        session.subscribe("/sub/room/1", stompHandler);

        StompHeaders headers = new StompHeaders();
        headers.setDestination("/pub/send");
        session.send(headers, requestMessage);

        RequestMessage response = stompHandler.pollMessageByQueue(5, TimeUnit.SECONDS);

        assertThat(response).isNull();
    }

}
