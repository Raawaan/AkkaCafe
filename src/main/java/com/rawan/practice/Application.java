package com.rawan.practice;

import com.rawan.practice.actor.CafeGuardian;
import akka.actor.typed.ActorSystem;

public class Application {
    public static void main(String[] args) throws InterruptedException {

        final ActorSystem<CafeGuardian.Command> akkaCafe = ActorSystem
                .create(CafeGuardian.create(), "HelloToAkkaCafe");

        akkaCafe.tell(new CafeGuardian.Starter());

        for (int i = 0; i < 10; i++) {
            Thread.sleep(10000L);
            akkaCafe.tell(new CafeGuardian.Starter());
        }
    }
}
