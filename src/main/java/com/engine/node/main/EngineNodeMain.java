package com.engine.node.main;

import akka.actor.ActorSystem;
import com.engine.node.actors.EngineNode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@ImportResource("classpath:spring/root-context.xml")
@PropertySource("classpath:conf/application.properties")
public class EngineNodeMain {
    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(EngineNodeMain.class, args);

        // get hold of the actor system
        ActorSystem system = ctx.getBean(ActorSystem.class);

        // use the Spring Extension to create top level supervisor for a named actor bean
        system.actorOf(EngineNode.props(system), EngineNode.ACTOR_NAME);
    }
}
