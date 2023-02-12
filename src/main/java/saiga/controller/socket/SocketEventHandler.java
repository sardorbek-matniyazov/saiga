package saiga.controller.socket;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import saiga.payload.MyResponse;

import java.util.Objects;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 12 Feb 2023
 **/
@Component
public record SocketEventHandler (
        SimpMessageSendingOperations messagingTemplate
) {
    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {}

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        String username = (String) Objects.requireNonNull(headerAccessor.getSessionAttributes()).get("username");
        if(username != null) {
            messagingTemplate.convertAndSend(
                    "/topic/disconnect-user",
                    MyResponse._UPDATED.setMessage("User Disconnected : " + username)
            );
        }
    }
}
