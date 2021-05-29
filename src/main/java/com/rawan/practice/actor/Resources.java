package com.rawan.practice.actor;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import akka.japi.function.Function;

import java.util.Random;

public class Resources extends AbstractBehavior<Resources.Provide> {
    private Random random =new Random();
    public Resources(ActorContext<Provide> context) {
        super(context);
    }

    public static Behavior<Provide> create() {
        return Behaviors.setup(Resources::new);
    }
    @Override
    public Receive<Provide> createReceive() {
        return newReceiveBuilder().onMessage(Provide.class,onProvide()).build();
    }

    private Function<Provide, Behavior<Provide>> onProvide() {
        return command -> {
            System.out.println("Request new resources");
            int randomInt = random.nextInt(3 - 1 + 1) + 1;
            command.from.tell(new HotDrinks.Add(randomInt));
            return this;
        };
    }
    public static final class Provide {
        public final ActorRef<HotDrinks.Command> from;

        public Provide(ActorRef<HotDrinks.Command> from) {
            this.from = from;
        }
    }

}
