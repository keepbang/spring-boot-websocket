package com.bang.websocket.chatting;

import com.bang.websocket.dto.RequestDto;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ChatWebSocketTest {

    @LocalServerPort
    private Integer port;

    @Test
    public void WebSocketConnectionTest() throws Exception {
        String webSocketUrl = "ws://localhost:" + port + "/ws/chat/connection";
        BlockingQueue<RequestDto> blockingQueue = new LinkedBlockingQueue<>();
        long senderId = 1L;
        long receiverId = 2L;
        long roomId = 1L;
        String message = "hello~~";
        RequestDto requestDto = new RequestDto(senderId, receiverId, roomId, message);

        // webSocket client 생성 & setting
        WebSocketStompClient client = WebSocket_생성();
        client.setMessageConverter(new MappingJackson2MessageConverter());

        // connection
        StompSession session = client.connect(webSocketUrl, new StompSessionHandlerAdapter() {
        }).get(60, TimeUnit.SECONDS);

        session.subscribe("/sub/room/" + roomId, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return RequestDto.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                blockingQueue.offer((RequestDto) payload);
            }
        });

        StompHeaders headers = new StompHeaders();
        headers.setDestination("/pub/send");
        session.send(headers, requestDto);

        RequestDto response = blockingQueue.poll(5, TimeUnit.SECONDS);

        assertThat(response).isEqualTo(requestDto);
    }

    private WebSocketStompClient WebSocket_생성() {
        StandardWebSocketClient standardWebSocketClient = new StandardWebSocketClient();
        WebSocketTransport webSocketTransport = new WebSocketTransport(standardWebSocketClient);
        List<Transport> transports = Collections.singletonList(webSocketTransport);
        SockJsClient sockJsClient = new SockJsClient(transports);

        return new WebSocketStompClient(sockJsClient);
    }

}
