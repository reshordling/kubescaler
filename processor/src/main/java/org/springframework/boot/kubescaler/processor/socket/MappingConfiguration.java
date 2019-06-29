package org.springframework.boot.kubescaler.processor.socket;

import java.util.HashMap;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;

@Configuration
public class MappingConfiguration {

//  private final PathVariableSocketHandler pathVariableSocketHandler;
//  private final WebSocketHandler webSocketHandler;
//
//  public MappingConfiguration(WebSocketHandler webSocketHandler) {
//    this.webSocketHandler = webSocketHandler;
//  }

  @Bean
  public HandlerMapping handlerMapping() {
    Map<String, WebSocketHandler> map = new HashMap<>();
    map.put("/websocket/echo", new EchoWebSocketHandler());
    SimpleUrlHandlerMapping simpleUrlHandlerMapping = new SimpleUrlHandlerMapping();
    simpleUrlHandlerMapping.setOrder(Ordered.HIGHEST_PRECEDENCE);
    simpleUrlHandlerMapping.setUrlMap(map);
    return simpleUrlHandlerMapping;
  }
  @Bean
  public WebSocketHandlerAdapter handlerAdapter() {
    return new WebSocketHandlerAdapter();
  }

//  @Bean
//  public HandlerMapping webSocketHandlerMapping() {
//    Map<String, WebSocketHandler> map = new HashMap<>();
//    map.put(pathVariableSocketHandler.getMappingTemplate(), pathVariableSocketHandler);
//
//    SimpleUrlHandlerMapping handlerMapping = new SimpleUrlHandlerMapping();
//    handlerMapping.setOrder(1);
//    handlerMapping.setUrlMap(map);
//    return handlerMapping;
//  }

//  @Bean
//  public WebSocketHandlerAdapter handlerAdapter() {
//    return new WebSocketHandlerAdapter(webSocketService());
//  }
//
//  @Bean
//  public WebSocketService webSocketService() {
//    return new HandshakeWebSocketService(new ReactorNettyRequestUpgradeStrategy());
//  }

}
