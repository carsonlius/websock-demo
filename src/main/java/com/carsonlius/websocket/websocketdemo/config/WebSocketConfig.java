package com.carsonlius.websocket.websocketdemo.config;

import com.carsonlius.websocket.websocketdemo.handler.MyHandler;
import com.carsonlius.websocket.websocketdemo.interceptor.MyHandshakeInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    @Autowired
    private MyHandshakeInterceptor myHandshakeInterceptor;

    /**
     * Register {@link WebSocketHandler WebSocketHandlers} including SockJS fallback options if desired.
     *
     * @param registry
     */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandler(), "/ws")
                .addInterceptors(myHandshakeInterceptor)
                .setAllowedOrigins("*");
    }

    @Bean
    public WebSocketHandler webSocketHandler(){
        return new MyHandler();
    }

    @Bean
    public ServerEndpointExporter serverEndpointExporter(){
        return new ServerEndpointExporter();
    }
}
