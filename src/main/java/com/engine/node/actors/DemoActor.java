package com.engine.node.actors;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class DemoActor extends AbstractActor {
    public static Props props(Integer magicNumber) {
        return Props.create(DemoActor.class, () -> new DemoActor(magicNumber));
    }

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private final Integer magicNumber;

    private DemoActor(Integer magicNumber) {
        this.magicNumber = magicNumber;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Integer.class, this::addMagicNumber)
                .matchAny(o -> log.info("received unknown message"))
                .build();
    }

    private void addMagicNumber(Integer i) {
        log.info("DemoActor received message: " + i + " and add Magic number: " + magicNumber);
        getSender().tell(i + magicNumber, getSelf());
    }
}
