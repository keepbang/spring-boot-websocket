package com.bang.websocket.handler;

import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;

import java.lang.reflect.Type;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class StompFrameHandlerImpl<T> implements StompFrameHandler {

    private T payload;
    private BlockingQueue<T> blockingQueue = new LinkedBlockingQueue<>();

    public StompFrameHandlerImpl(T payload) {
        this.payload = payload;
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return payload.getClass();
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        blockingQueue.offer((T) payload);
    }

    public T pollMessageByQueue(long timeout, TimeUnit unit) throws InterruptedException {
        return blockingQueue.poll(timeout, unit);
    }
}
