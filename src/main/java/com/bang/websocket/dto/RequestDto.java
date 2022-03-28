package com.bang.websocket.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.json.JsonParser;

import java.util.Objects;

@Getter
@NoArgsConstructor
public class RequestDto {
    private Long senderId;
    private Long receiverId;
    private Long roomId;
    private String message;

    public RequestDto(Long senderId, Long receiverId, Long roomId, String message) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.roomId = roomId;
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RequestDto that = (RequestDto) o;
        return Objects.equals(senderId, that.senderId)
                && Objects.equals(receiverId, that.receiverId)
                && Objects.equals(roomId, that.roomId)
                && Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(senderId, receiverId, roomId, message);
    }

    @Override
    public String toString() {
        return "RequestDto{" +
                "senderId=" + senderId +
                ", receiverId=" + receiverId +
                ", roomId=" + roomId +
                ", message='" + message + '\'' +
                '}';
    }
}
