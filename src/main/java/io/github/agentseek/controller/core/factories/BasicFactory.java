package ryleh.controller.core.factories;

import ryleh.common.Circle2d;
import ryleh.common.GameMath;
import ryleh.common.Point2d;
import ryleh.common.Vector2d;
import io.github.agentseek.controller.Entity;
import ryleh.controller.core.GameEngine;
import ryleh.controller.core.GameState;
import ryleh.model.Type;
import ryleh.model.components.BulletComponent;
import io.github.agentseek.model.components.CollisionWithPlayerComponent;
import ryleh.model.components.DoorComponent;
import ryleh.model.components.HealthIntComponent;
import io.github.agentseek.model.components.PlayerComponent;
import ryleh.model.components.ShootingComponent;
import ryleh.model.physics.CircleHitBox;
import ryleh.model.physics.HitBoxType;
import ryleh.view.graphics.PlayerGraphicComponent;
import ryleh.view.graphics.other.BulletGraphicComponent;
import ryleh.view.graphics.other.DoorGraphicComponent;
import ryleh.view.graphics.other.FireGraphicComponent;
import ryleh.view.graphics.other.ItemGraphicComponent;
import ryleh.view.graphics.other.RockGraphicComponent;

/**
 * A factory class for basic entities such as player,bullet,rock,item and fire.
 */
public final class BasicFactory {
    private static BasicFactory instance;

    /**
     * Constructor method.
     */
    private BasicFactory() {
    }

    /**
     * Gets Singleton for BasicFactory.
     * 
     * @return BasicFactory instance.
     */
    public static BasicFactory getInstance() {
        if (instance == null) {
            instance = new BasicFactory();
        }
        return instance;
    }

    /**
     * Method that creates a player entity given a GameState instance and a Point2d
     * position.
     * 
     * @param state    GameState instance.
     * @param position Point2d instance.
     * @return Entity type instance.
     */
    public Entity createPlayer(final GameState state, final Point2d position) {
        final PlayerGraphicComponent player = new PlayerGraphicComponent(GameMath.toPoint2D(position));
        player.setZindex(1);
        final Entity e = GameEngine.entityBuilder().type(Type.PLAYER).position(position)
                .with(new PlayerComponent(state.getWorld(), 1000)).with(new HealthIntComponent(state.getWorld(), 3))
                .with(new ShootingComponent(state.getWorld(), 1.0)).view(player)
                .bbox(new CircleHitBox(new Circle2d(HitBoxType.PLAYER.getBoxRadius()))).build();
        state.getWorld().addGameObject(e.gameObject);
        state.getView().addGraphicComponent(e.view);
        ((PlayerGraphicComponent) e.view).setDirection(
                ((PlayerComponent) e.gameObject.getComponent(PlayerComponent.class).get()).getDirection());
        return e;
    }

    /**
     * Method that creates a bullet entity given a GameState instance,Point2d
     * origin,Vector2d direction and a Type value to determine bullet type.
     * 
     * @param state     GameState instance.
     * @param origin    Point2d instance.
     * @param direction Vector2d instance.
     * @param type      Type value.
     * @return Entity type instance.
     */
    public Entity createBullet(final GameState state, final Point2d origin, final Vector2d direction, final Type type) {
        final Type bulletType = type.equals(Type.PLAYER) ? Type.PLAYER_BULLET : Type.ENEMY_BULLET;
        final BulletGraphicComponent bullet = new BulletGraphicComponent(GameMath.toPoint2D(origin));
        bullet.setBulletType(bulletType);
        bullet.setZindex(0);
        final Entity e = GameEngine.entityBuilder().type(bulletType).position(origin)
                .with(new BulletComponent(state.getWorld(), origin, direction)).view(bullet)
                .bbox(new CircleHitBox(new Circle2d(HitBoxType.BULLET.getBoxRadius()))).build();
        state.getWorld().addGameObject(e.gameObject);
        state.getView().addGraphicComponent(e.view);
        return e;
    }

    /**
     * Method that creates a rock entity given a GameState instance and a Point2d
     * position.
     * 
     * @param state    GameState instance.
     * @param position Point2d instance.
     * @return Entity type instance.
     */
    public Entity createRock(final GameState state, final Point2d position) {
        final RockGraphicComponent rock = new RockGraphicComponent(GameMath.toPoint2D(position));
        rock.setZindex(1);
        final Entity e = GameEngine.entityBuilder().type(Type.ROCK).position(position).view(rock)
                .bbox(new CircleHitBox(HitBoxType.ROCK.getBoxRadius())).build();
        state.getWorld().addGameObject(e.gameObject);
        state.getView().addGraphicComponent(e.view);
        return e;
    }

    /**
     * Method that creates an item entity given a GameState instance and a Point2d
     * position.
     * 
     * @param state    GameState instance.
     * @param position Point2d instance.
     * @return Entity type instance.
     */
    public Entity createItem(final GameState state, final Point2d position) {
        final ItemGraphicComponent item = new ItemGraphicComponent(GameMath.toPoint2D(position));
        item.setZindex(0);
        final Entity e = GameEngine.entityBuilder().type(Type.ITEM).position(position)
                .with(new CollisionWithPlayerComponent(state.getWorld(), Type.ITEM)).view(item)
                .bbox(new CircleHitBox(new Circle2d(HitBoxType.ITEM.getBoxRadius()))).build();
        state.getWorld().addGameObject(e.gameObject);
        state.getView().addGraphicComponent(e.view);
        return e;
    }

    /**
     * Method that creates a fire entity given a GameState instance and a Point2d
     * position.
     * 
     * @param state    GameState instance.
     * @param position Point2d instance.
     * @return Entity type instance.
     */
    public Entity createFire(final GameState state, final Point2d position) {
        final FireGraphicComponent fire = new FireGraphicComponent(GameMath.toPoint2D(position));
        fire.setZindex(1);
        final Entity e = GameEngine.entityBuilder().type(Type.FIRE).position(position)
                .with(new CollisionWithPlayerComponent(state.getWorld(), Type.FIRE)).view(fire)
                .bbox(new CircleHitBox(new Circle2d(HitBoxType.FIRE.getBoxRadius()))).build();
        state.getWorld().addGameObject(e.gameObject);
        state.getView().addGraphicComponent(e.view);
        return e;
    }

    /**
     * Method that creates a door entity given a GameState instance and a Point2d
     * position.
     * 
     * @param state    GameState instance.
     * @param position Point2d instance.
     * @return Entity type instance.
     */
    public Entity createDoor(final GameState state, final Point2d position) {
        final DoorGraphicComponent door = new DoorGraphicComponent(GameMath.toPoint2D(position));
        door.setZindex(0);
        final Entity e = GameEngine.entityBuilder().type(Type.DOOR).position(position).view(door)
                .with(new DoorComponent(state.getWorld(), door.getTotalAnimDuration()))
                .bbox(new CircleHitBox(new Circle2d(HitBoxType.DOOR.getBoxRadius()))).build();
        state.getWorld().addGameObject(e.gameObject);
        state.getView().addGraphicComponent(e.view);
        return e;
    }
}
