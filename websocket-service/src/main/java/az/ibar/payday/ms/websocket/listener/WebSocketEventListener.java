package az.ibar.payday.ms.websocket.listener;

import az.ibar.payday.ms.websocket.helper.CacheHelper;
import az.ibar.payday.ms.websocket.logger.SafeLogger;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.List;
import java.util.Map;

@Component
public class WebSocketEventListener {

    private static final SafeLogger logger = SafeLogger.getLogger(WebSocketEventListener.class);
    private final CacheHelper cacheHelper;

    public WebSocketEventListener(CacheHelper cacheHelper) {
        this.cacheHelper = cacheHelper;
    }

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        var session = StompHeaderAccessor.wrap(event.getMessage()).getSessionId();
        logger.info("web socket connect new session {}", session);
        var nativeHeaders = (Map<String, List<String>>) event.getMessage().getHeaders().get("nativeHeaders");
        var username = nativeHeaders.get("username").get(0);
        cacheHelper.cacheUserSession(cacheHelper.getWebsocketBucketKey(username), session);
        logger.info("web socket connect username: {}", username);
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        var session = StompHeaderAccessor.wrap(event.getMessage()).getSessionId();
        var nativeHeaders = (Map<String, List<String>>) event.getMessage().getHeaders().get("nativeHeaders");
        var username = nativeHeaders.get("username").get(0);
        cacheHelper.deleteUserSession(cacheHelper.getWebsocketBucketKey(username));
        logger.info("web socket disconnect username: {}", username);
    }
}
