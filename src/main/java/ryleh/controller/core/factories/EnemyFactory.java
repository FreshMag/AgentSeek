package ryleh.controller.core.factories;

import ryleh.common.Circle2d;
import ryleh.common.GameMath;
import ryleh.common.Point2d;
import ryleh.controller.Entity;
import ryleh.controller.core.GameEngine;
import ryleh.controller.core.GameState;
import ryleh.model.Type;
import ryleh.model.components.CollisionComponent;
import ryleh.model.components.DrunkComponent;
import ryleh.model.components.HealthIntComponent;
import ryleh.model.components.LurkerComponent;
import ryleh.model.physics.CircleHitBox;
import ryleh.model.physics.HitBoxType;
import ryleh.view.graphics.PlayerGraphicComponent;
import ryleh.view.graphics.enemies.EnemyDrunkGraphicComponent;
import ryleh.view.graphics.enemies.EnemyDrunkSpinnerGraphicComponent;
import ryleh.view.graphics.enemies.EnemyLurkerGraphicComponent;
import ryleh.view.graphics.enemies.EnemyShooterGraphicComponent;
import ryleh.view.graphics.enemies.EnemySpinnerGraphicComponent;
import ryleh.model.components.ShooterComponent;
import ryleh.model.components.SpinnerComponent;

/**
 * A factory class for enemy entities.
 */
public final class EnemyFactory {
    private static EnemyFactory instance;
    private static final int ENEMY_HEALTH = 1;

    /**
     * Constructor method.
     */
    private EnemyFactory() {
    }

    /**
     * Gets Singleton for EnemyFactory.
     * 
     * @return EnemyFactory instance.
     */
    public static EnemyFactory getInstance() {
        if (instance == null) {
            instance = new EnemyFactory();
        }
        return instance;
    }

    /**
     * Method that creates a shooter entity given a GameState instance and a Point2d
     * position.
     * 
     * @param state    GameState instance.
     * @param position Point2d instance.
     * @return Entity type instance.
     */
    public Entity createEnemyShooter(final GameState state, final Point2d position) {
        final EnemyShooterGraphicComponent shooter = new EnemyShooterGraphicComponent(GameMath.toPoint2D(position));
        shooter.setZindex(1);
        final Entity e = GameEngine.entityBuilder().type(Type.ENEMY_SHOOTER).position(position)
                .with(new ShooterComponent(state.getWorld(), state.getPlayer()))
                .with(new HealthIntComponent(state.getWorld(), ENEMY_HEALTH))
                .with(new CollisionComponent(state.getWorld(), Type.ENEMY_SHOOTER)).view(shooter)
                .bbox(new CircleHitBox(new Circle2d(HitBoxType.ENEMY.getBoxRadius()))).build();
        state.getWorld().addGameObject(e.getGameObject());
        state.getView().addGraphicComponent(e.getView());
        return e;
    }

    /**
     * Method that creates a spinner entity given a GameState instance and a Point2d
     * position.
     * 
     * @param state    GameState instance.
     * @param position Point2d instance.
     * @return Entity type instance.
     */
    public Entity createEnemySpinner(final GameState state, final Point2d position) {
        final EnemySpinnerGraphicComponent spinner = new EnemySpinnerGraphicComponent(GameMath.toPoint2D(position));
        spinner.setZindex(1);
        final Entity e = GameEngine.entityBuilder().type(Type.ENEMY_SPINNER).position(position)
                .with(new SpinnerComponent(state.getWorld())).with(new HealthIntComponent(state.getWorld(), ENEMY_HEALTH))
                .with(new CollisionComponent(state.getWorld(), Type.ENEMY_SPINNER)).view(spinner)
                .bbox(new CircleHitBox(new Circle2d(HitBoxType.ENEMY.getBoxRadius()))).build();
        state.getWorld().addGameObject(e.getGameObject());
        state.getView().addGraphicComponent(e.getView());
        return e;
    }

    /**
     * Method that creates a drunk entity given a GameState instance and a Point2d
     * position.
     * 
     * @param state    GameState instance.
     * @param position Point2d instance.
     * @return Entity type instance.
     */
    public Entity createEnemyDrunk(final GameState state, final Point2d position) {
        final EnemyDrunkGraphicComponent drunk = new EnemyDrunkGraphicComponent(GameMath.toPoint2D(position));
        drunk.setZindex(0);
        final Entity e = GameEngine.entityBuilder().type(Type.ENEMY_DRUNK).position(position)
                .with(new DrunkComponent(state.getWorld())).with(new HealthIntComponent(state.getWorld(), ENEMY_HEALTH))
                .with(new CollisionComponent(state.getWorld(), Type.ENEMY_DRUNK)).view(drunk)
                .bbox(new CircleHitBox(new Circle2d(HitBoxType.ENEMY.getBoxRadius()))).build();
        state.getWorld().addGameObject(e.getGameObject());
        state.getView().addGraphicComponent(e.getView());
        return e;
    }

    /**
     * Method that creates a lurker entity given a GameState instance and a Point2d
     * position.
     * 
     * @param state    GameState instance.
     * @param position Point2d instance.
     * @return Entity type instance.
     */
    public Entity createEnemyLurker(final GameState state, final Point2d position) {
        final EnemyLurkerGraphicComponent lurker = new EnemyLurkerGraphicComponent(
                (PlayerGraphicComponent) state.getPlayer().getView(), GameMath.toPoint2D(position));
        lurker.setZindex(1);
        final Entity e = GameEngine.entityBuilder().type(Type.ENEMY_LURKER).position(position)
                .with(new LurkerComponent(state.getWorld(), state.getPlayer()))
                .with(new HealthIntComponent(state.getWorld(), ENEMY_HEALTH))
                .with(new CollisionComponent(state.getWorld(), Type.ENEMY_LURKER)).view(lurker)
                .bbox(new CircleHitBox(new Circle2d(HitBoxType.ENEMY.getBoxRadius()))).build();
        state.getWorld().addGameObject(e.getGameObject());
        state.getView().addGraphicComponent(e.getView());
        return e;
    }

    /**
     * Method that creates a drunk spinner entity given a GameState instance and a
     * Point2d position.
     * 
     * @param state    GameState instance.
     * @param position Point2d instance.
     * @return Entity type instance.
     */
    public Entity createEnemyDrunkSpinner(final GameState state, final Point2d position) {
        final EnemyDrunkSpinnerGraphicComponent drunkSpinner = new EnemyDrunkSpinnerGraphicComponent(
                GameMath.toPoint2D(position));
        drunkSpinner.setZindex(1);
        final Entity e = GameEngine.entityBuilder().type(Type.ENEMY_DRUNKSPINNER).position(position)
                .with(new DrunkComponent(state.getWorld())).with(new SpinnerComponent(state.getWorld()))
                .with(new HealthIntComponent(state.getWorld(), ENEMY_HEALTH))
                .with(new CollisionComponent(state.getWorld(), Type.ENEMY_DRUNKSPINNER)).view(drunkSpinner)
                .bbox(new CircleHitBox(new Circle2d(HitBoxType.ENEMY.getBoxRadius()))).build();
        state.getWorld().addGameObject(e.getGameObject());
        state.getView().addGraphicComponent(e.getView());
        return e;
    }
}
