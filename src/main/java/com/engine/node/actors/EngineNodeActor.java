package com.engine.node.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.cluster.Cluster;
import akka.cluster.ClusterEvent;
import akka.cluster.ClusterEvent.MemberUp;
import akka.cluster.ClusterEvent.MemberLeft;
import akka.cluster.ClusterEvent.MemberEvent;
import akka.cluster.ClusterEvent.MemberExited;
import akka.cluster.ClusterEvent.UnreachableMember;
import akka.cluster.Member;
import akka.cluster.metrics.ClusterMetricsChanged;
import akka.cluster.metrics.ClusterMetricsExtension;
import akka.cluster.metrics.NodeMetrics;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.engine.node.extensions.SpringExtension;
import com.engine.node.mongo.entities.SessionTraffic;
import com.engine.node.protocols.MetricProtocol;
import com.engine.node.service.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

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

    private boolean centralState;

    @Override
    public void preStart() {
        logger.info("Engine started");
        extension.subscribe(getSelf());
        cluster.subscribe(getSelf(), initialStateAsEvents(), MemberUp.class, UnreachableMember.class);
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
/*                .match(ClusterMetricsChanged.class, this::logNodeMetrics)
                .match(MemberUp.class, this::setCentralState)
                .match(UnreachableMember.class, this::setCentralState)*/
                .matchEquals("GetStatisticService", this::statisticService)
                .build();
    }

    private void logNodeMetrics(ClusterMetricsChanged clusterMetrics) {
        if (centralState) {
            for (NodeMetrics nodeMetrics : clusterMetrics.getNodeMetrics()) {
                if (nodeMetrics.address().equals(cluster.selfAddress())) {
                    getSender().tell(new MetricProtocol(nodeMetrics), getSelf());
                }
            }
        }
    }

    private void setCentralState(final MemberUp memberUp) {
        final Member member = memberUp.member();

        invertCentralState(member);
    }

    private void setCentralState(final UnreachableMember memberExited) {
        final Member member = memberExited.member();

        invertCentralState(member);
    }

    private void invertCentralState(final Member member) {
        if (member.hasRole("central")) {
            centralState = !centralState;
        }
    }


    private void statisticService(String s) {
        pipe(supplyAsync(() -> statisticService
                .getAllSessionTraffic()
                .stream()
                .map(SessionTraffic::toString)
                .collect(Collectors.toList())), system.dispatcher()).to(getSender());
    }
}
