package com.tutorial.tutorialwebflux.configurations;

import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class NettyWebServerConfig {
	
    @Bean
	public NettyReactiveWebServerFactory nettyReactiveWebServerFactory() {
		NettyReactiveWebServerFactory webServerFactory = new NettyReactiveWebServerFactory();
		webServerFactory.addServerCustomizers(new EventLoopNettyCustomizer());
		webServerFactory.setPort(10300);
    return webServerFactory;
	}
    
}
