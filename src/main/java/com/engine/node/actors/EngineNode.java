package com.engine.node.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.cluster.Cluster;
import akka.cluster.ClusterEvent.MemberEvent;
import akka.cluster.ClusterEvent.UnreachableMember;
import akka.cluster.metrics.ClusterMetricsChanged;
import akka.cluster.metrics.ClusterMetricsExtension;
import akka.cluster.metrics.NodeMetrics;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.engine.node.extensions.SpringExtension;
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

    private final ActorSystem system = getContext().getSystem();

    private final Cluster cluster = Cluster.get(system);

    private final ClusterMetricsExtension extension = ClusterMetricsExtension.get(system);

    private final LoggingAdapter logger = Logging.getLogger(system, this);

    @Override
    public void preStart() {
        logger.info("Engine started");
        extension.subscribe(getSelf());
        cluster.subscribe(getSelf(), initialStateAsEvents(), MemberEvent.class, UnreachableMember.class);
    }

    @Override
    public void postStop() {
        logger.info("Engine stopped");
        extension.unsubscribe(getSelf());
        cluster.unsubscribe(getSelf());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(ClusterMetricsChanged.class, clusterMetrics -> {
                    for (NodeMetrics nodeMetrics : clusterMetrics.getNodeMetrics()) {
                        if (nodeMetrics.address().equals(cluster.selfAddress())) {
                            // logger.info(new Metrics(nodeMetrics).toString());
                        }
                    }
                })
                .matchEquals("Hello", s -> {
                    logger.info("Get Message: " + s + " | " + getSender());
                    getSender().tell("World", getSelf());
                })
                .build();
    }
}
