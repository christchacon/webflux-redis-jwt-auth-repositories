package com.tutorial.tutorialwebflux.configurations;

import java.beans.JavaBean;

import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.boot.web.embedded.netty.NettyServerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import reactor.netty.http.server.HttpServer;
import reactor.netty.resources.LoopResources;


@Component
public class EventLoopNettyCustomizer implements NettyServerCustomizer {

    @Override
    public HttpServer apply(HttpServer httpServer) {
        // TODO Auto-generated method stub
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup(50);
        eventLoopGroup.register(new NioServerSocketChannel());
        //LoopResources loopResources = LoopResources.create("my-http-server", 20, true);
        return httpServer.runOn(eventLoopGroup);
    }
    
}
