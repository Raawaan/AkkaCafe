package com.rawan.practice.actor;

import akka.actor.testkit.typed.javadsl.TestKitJunitResource;
import akka.actor.testkit.typed.javadsl.TestProbe;
import akka.actor.typed.ActorRef;
import org.junit.ClassRule;
import org.junit.Test;

public class HotDrinksTest {

    @ClassRule
    public static final TestKitJunitResource testKit = new TestKitJunitResource();

    @Test
    public void testRequestingResources() {
        TestProbe<HotDrinks.Add> testProbe = testKit.createTestProbe();
        ActorRef<Resources.Provide> underTest = testKit.spawn(Resources.create(), "prepare");
        underTest.tell(new Resources.Provide(testProbe.getRef().unsafeUpcast()));
        testProbe.expectMessageClass(HotDrinks.Add.class);
    }

}
