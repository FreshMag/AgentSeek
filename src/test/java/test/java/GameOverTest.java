package test.java;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ryleh.common.Circle2d;
import ryleh.controller.events.Event;
import ryleh.controller.events.EventListener;
import ryleh.controller.events.GameOverEvent;
import ryleh.model.GameObject;
import ryleh.model.GameObjectImpl;
import ryleh.model.Type;
import ryleh.model.World;
import ryleh.model.WorldImpl;
import ryleh.model.components.HealthIntComponent;
import ryleh.model.physics.CircleHitBox;
import ryleh.model.physics.HitBoxType;

class GameOverTest implements EventListener {

    private static final int TEST_FRAMES = 50;
    private static final double DELTA_TIME_TEST = 1000.0;
    private GameObject object;
    private HealthIntComponent health;
    private boolean isGameOver;
    @BeforeEach
    void setUp() throws Exception {
        final World world = new WorldImpl(this);
        object = new GameObjectImpl();
        health = new HealthIntComponent(world, 3);
        object.setType(Type.PLAYER);
        object.setHitBox(new CircleHitBox(new Circle2d(HitBoxType.PLAYER.getBoxRadius())));
        object.addComponent(health);
        world.addGameObject(object);
        object.onAdded(world);
    }
    @Test
    void testGameOver() {
        for (int testCount = 0; testCount < TEST_FRAMES; testCount++) {
            health.damage(1);
            System.out.println(health.getCurrentHp());
            try {
                object.onUpdate(DELTA_TIME_TEST);
                Thread.sleep(100);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        assertTrue(isGameOver);
    }

    @Override
    public void notifyEvent(final Event e) {
        if (e instanceof GameOverEvent) {
            System.out.println("Game over");
            this.isGameOver = true;
        }
    }

}
