package com.engine.node.config;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.engine.node.actors.EngineNodeActor;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.engine.node.extensions.SpringExtension.SpringExtProvider;

/**
 * The application configuration.
 */
@Configuration
class ActorSystemConfiguration {
    // the application context is needed to initialize the Akka Spring Extension
    @Autowired
    private ApplicationContext applicationContext;

    /**
     * Actor system singleton for this application.
     */
    @Bean
    public ActorSystem actorSystem() {
        final Config config = ConfigFactory.load("conf/application.conf");
        ActorSystem system = ActorSystem.create("application", config);
        // initialize the application context in the Akka Spring Extension
        SpringExtProvider.get(system).initialize(applicationContext);
        return system;
    }

    @Bean
    public ActorRef engineNodeActor() {
        // get hold of the actor system
        final ActorSystem system = applicationContext.getBean(ActorSystem.class);
        // use the Spring Extension to create top level supervisor for a named actor bean
        return system.actorOf(EngineNodeActor.props(system), EngineNodeActor.ACTOR_NAME);
    }
}

