package com.rawan.practice.actor;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import akka.japi.function.Function;

public class CafeGuardian extends AbstractBehavior<CafeGuardian.Command> {

    public CafeGuardian(ActorContext<Command> context) {
        super(context);
    }

    public static Behavior<Command> create() {
        return Behaviors.setup(CafeGuardian::new);
    }

    private ActorRef<HotDrinks.Command> actorRef =
            getContext().spawn(HotDrinks.create(2), "HotDrinks");
    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onMessage(Starter.class, onStart())
                .build();
    }

    private Function<Starter, Behavior<Command>> onStart() {
        return command -> {
            System.out.println("Cafe Started");
            for (Drink drink : Drink.values()) {
                actorRef.tell(new HotDrinks.Prepare(drink));
            }
            return this;
        };
    }

    public static class Command {
    }

    public static final class Starter extends Command {
    }
}
