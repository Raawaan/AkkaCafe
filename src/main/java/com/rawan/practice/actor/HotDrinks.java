package com.rawan.practice.actor;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import akka.japi.function.Function;

public class HotDrinks extends AbstractBehavior<HotDrinks.Command> {
    private int currentResources;

    public HotDrinks(ActorContext<Command> context, int currentResources) {
        super(context);
        this.currentResources = currentResources;
    }

    public static Behavior<HotDrinks.Command> create(int maxDrinks) {
        return Behaviors.setup(context -> new HotDrinks(context, maxDrinks));
    }

   private ActorRef<Resources.Provide> actorRef =
            getContext().spawn(Resources.create(), "Provider");
    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onMessage(Prepare.class, onPrepare())
                .onMessage(Add.class, onAdd())
                .build();
    }

    private Function<Prepare, Behavior<Command>> onPrepare() {
        return command -> {
            if (currentResources > 1) {
                currentResources--;
                System.out.println("current resources " + currentResources);
                System.out.println(command.drink + " is ready!");
            } else {
                System.out.println("Running out of resources");
                actorRef.tell(new Resources.Provide(getContext().getSelf()));
            }
            return this;
        };
    }

    private Function<Add, Behavior<Command>> onAdd() {
        return command -> {
            System.out.println("got "+command.resources+ " new resources");
            currentResources += command.resources;
            return this;
        };
    }

    public static class Command { }

    public static final class Prepare extends Command {
        public final Drink drink;

        public Prepare(Drink drink) {
            this.drink = drink;
        }
    }

    public static final class Add extends Command {
        public final int resources;

        public Add(int resources) {
            this.resources = resources;
        }
    }
}

enum Drink {
    FLAT_WHITE,
    LATE,
    ESPRESSO
}
