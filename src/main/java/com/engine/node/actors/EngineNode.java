package com.engine.node.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.engine.node.utils.SpringExtension;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("EngineNode")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class EngineNode extends AbstractActor {
    // add actor name for bean definition and actor definition
    public static final String ACTOR_NAME = "EngineNode";

    public static Props props(ActorSystem system) {
        return SpringExtension.SpringExtProvider.get(system).props(ACTOR_NAME);
    }

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    // private final ActorRef demoChildActor = getContext().actorOf(DemoActor.props(42), "demo-actor");

    @Override
    public void preStart() {
        log.info("Application preStart");
    }

    @Override
    public void postStop() {
        log.info("Application stopped");
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .matchAny(o -> log.info("received unknown message"))
                .build();
    }

}
