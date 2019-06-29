package org.springframework.boot.kubescaler.processor.configuration;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.hateoas.HypermediaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;

@Configuration
//@EnableWebFlux
//@EnableAutoConfiguration(exclude = HypermediaAutoConfiguration.class)
//@EnableHypermediaSupport(type = HypermediaType.HAL)
public class WebFluxConfiguration /*implements WebFluxConfigurer*/ {

//  @Bean
//  public WebSocketHandlerAdapter webSocketHandlerAdapter()
//  {
//    return new WebSocketHandlerAdapter();
//  }
}
