package com.engine.node.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.cluster.Cluster;
import akka.cluster.ClusterEvent.MemberEvent;
import akka.cluster.ClusterEvent.UnreachableMember;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.engine.node.utils.SpringExtension;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import static akka.cluster.ClusterEvent.initialStateAsEvents;

@Component("EngineNode")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class EngineNode extends AbstractActor {
    // add actor name for bean definition and actor definition
    public static final String ACTOR_NAME = "EngineNode";

    public static Props props(ActorSystem system) {
        return SpringExtension.SpringExtProvider.get(system).props(ACTOR_NAME);
    }

    public static class Message {}

    private final ActorSystem system = getContext().getSystem();

    private final Cluster cluster = Cluster.get(system);

    private final LoggingAdapter log = Logging.getLogger(system, this);

    // private final ActorRef demoChildActor = getContext().actorOf(DemoActor.getProps(42), "demo-actor");

    // subscribe to cluster changes
    @Override
    public void preStart() {
        log.info("Application started");
        cluster.subscribe(getSelf(), initialStateAsEvents(), MemberEvent.class, UnreachableMember.class);
    }

    @Override
    public void postStop() {
        log.info("Application stopped");
        cluster.unsubscribe(getSelf());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Message.class, o -> log.info("Get Message"))
                .matchAny(m -> log.info("Test message: " + m))
                .build();
    }

}
