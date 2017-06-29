package com.javasampleapproach.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;


@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat")
        	.withSockJS();//abilita fallback 
    }

	
    @Override
	public void configureMessageBroker( MessageBrokerRegistry config) {
		//i messaggi per /topic sono gestiti dal broker             
		config.enableSimpleBroker("/topic");
		//quelli per /app sono gestiti da un controller
		config.setApplicationDestinationPrefixes("/app");
	}
	

}
