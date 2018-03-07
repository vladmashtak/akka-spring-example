package com.engine.node.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.cluster.Cluster;
import akka.cluster.ClusterEvent.MemberEvent;
import akka.cluster.ClusterEvent.UnreachableMember;
import akka.cluster.metrics.ClusterMetricsExtension;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.engine.node.extensions.SpringExtension;
import com.engine.node.mongo.entities.SessionTraffic;
import com.engine.node.service.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import static akka.cluster.ClusterEvent.initialStateAsEvents;
import static akka.pattern.PatternsCS.pipe;

@Component("EngineNodeActor")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class EngineNodeActor extends AbstractActor {
    // add actor name for bean definition and actor definition
    public static final String ACTOR_NAME = "EngineNodeActor";

    public static Props props(ActorSystem system) {
        return SpringExtension.SpringExtProvider.get(system).props(ACTOR_NAME);
    }

    @Autowired
    private StatisticService statisticService;

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
                .matchEquals("GetStatisticService", s ->
                    pipe(supplyAsync(() -> statisticService
                            .getAllSessionTraffic()
                            .stream()
                            .map(SessionTraffic::toString)
                            .collect(Collectors.toList())), system.dispatcher()).to(getSender())
                )
                .build();
    }
}
