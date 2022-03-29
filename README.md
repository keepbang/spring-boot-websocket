# Spring Boot WebSocket

---

## EndPoint
- **/ws/chat/connection**

## Destination
| destination          | from(publish) | to(subscribe) | description                            |
|----------------------|:-------------:|:-------------:|----------------------------------------|
| */pub/send*          |    client     |    server     | client 에서 server 로 보내는 message         |
| */sub/room/{roomId}* |    server     |    client     | server 에서 roomId로 구독하고있는 사용자에게 메세지를 보냄 |

### Message Parameter
- RequestMessage.java

| name       | type   | description |
|------------|--------|-------------|
| senderId   | Long   | 보내는 사람      |
| receiverId | Long   | 받는 사람       |
| roomId     | Long   | 채팅방 아이디     |
| message    | String | 보내는 메세지     |
