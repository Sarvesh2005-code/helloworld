package com.springboot.helloworld.config;

import com.springboot.helloworld.model.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderWebSocketHandler extends TextWebSocketHandler {

    private final List<WebSocketSession> sessions = new ArrayList<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);
    }

    public void sendOrder(Order order) {

        String message = "{"
                + "\"id\":" + order.getId() + ","
                + "\"customerName\":\"" + order.getCustomerName() + "\","
                + "\"productName\":\"" + order.getProductName() + "\","
                + "\"quantity\":" + order.getQuantity() + ","
                + "\"status\":\"" + order.getStatus() + "\""
                + "}";

        for (WebSocketSession session : sessions) {

            try {

                if (session.isOpen()) {
                    session.sendMessage(
                            new TextMessage(message)
                    );
                }

            } catch (Exception e) {
                System.out.println(
                        "WebSocket error: " + e.getMessage()
                );
            }
        }
    }
}