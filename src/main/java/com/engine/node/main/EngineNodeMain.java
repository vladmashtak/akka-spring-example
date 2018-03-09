package com.engine.node.main;

import akka.actor.ActorRef;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@ImportResource("classpath:spring/root-context.xml")
public class EngineNodeMain {
    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(EngineNodeMain.class, args);

        ctx.getBean(ActorRef.class);
    }
}
