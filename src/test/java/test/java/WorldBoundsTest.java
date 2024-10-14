package test.java;


import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ryleh.common.Circle2d;
import ryleh.common.Rectangle2d;
import ryleh.common.Vector2d;
import ryleh.controller.events.Event;
import ryleh.controller.events.EventListener;
import ryleh.model.GameObject;
import ryleh.model.GameObjectImpl;
import ryleh.model.Type;
import ryleh.model.World;
import ryleh.model.WorldImpl;
import ryleh.model.components.PlayerComponent;
import ryleh.model.physics.CircleHitBox;
import ryleh.model.physics.Direction;
import ryleh.model.physics.HitBoxType;

class WorldBoundsTest implements EventListener {

    private static final int TEST_FRAMES = 1000;
    private static final double DELTA_TIME_TEST = 400.0;
    private World world;
    private GameObject object;
    private PlayerComponent physics;
    @BeforeEach
    void setUp() throws Exception {
        world = new WorldImpl(this);
        physics = new PlayerComponent(world, 10);
        object = new GameObjectImpl();
        object.setType(Type.PLAYER);
        object.addComponent(physics);
        physics.setPosition(world.getBounds().getCenter());
        object.setHitBox(new CircleHitBox(new Circle2d(HitBoxType.PLAYER.getBoxRadius())));
        physics.setVelocity(new Vector2d(1, 0));
        physics.setDirection(Direction.RIGHT);
        world.addGameObject(object);
        object.onAdded(world);
    }
    @Test
    void test() {
        for (int testCount = 0; testCount < TEST_FRAMES; testCount++) {
            physics.onUpdate(DELTA_TIME_TEST);
        }
        assertTrue(object.getPosition().getX() <= ((Rectangle2d) world.getBounds()).getLowerRight().getX());
        assertFalse(world.isOutOfBounds(object.getPosition()));
    }

    @Override
    public void notifyEvent(final Event e) {
    }

}
