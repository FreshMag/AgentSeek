package ryleh.controller.core.factories

import io.github.agentseek.common.Point2d
import io.github.agentseek.controller.core.GameState

/**
 * A factory class for basic entities such as player,bullet,rock,item and fire.
 */
object BasicFactory {
    var instance: BasicFactory? = null
        /**
         * Gets Singleton for BasicFactory.
         *
         * @return BasicFactory instance.
         */
        get() {
            if (field == null) {
                field = BasicFactory
            }
            return field
        }
        private set

    /**
     * Method that creates a player entity given a GameState [state] instance and a Point2d [position]
     */
    fun createPlayer(state: GameState, position: Point2d) {
        /* final PlayerGraphicComponent player = new PlayerGraphicComponent(GameMath.toPoint2D(position));
        player.setZindex(1);
        final Entity e = GameEngine.entityBuilder().type(Type.PLAYER).position(position)
                .with(new PlayerComponent(state.getWorld(), 1000)).with(new HealthIntComponent(state.getWorld(), 3))
                .with(new ShootingComponent(state.getWorld(), 1.0)).view(player)
                .bbox(new CircleHitBox(new Circle2d(HitBoxType.PLAYER.getBoxRadius()))).build();
        state.getWorld().addGameObject(e.gameObject);
        state.getView().addGraphicComponent(e.view);
        ((PlayerGraphicComponent) e.view).setDirection(
                ((PlayerComponent) e.gameObject.getComponent(PlayerComponent.class).get()).getDirection());
        return e;*/
    }
}

