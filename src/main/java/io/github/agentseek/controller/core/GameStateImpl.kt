package io.github.agentseek.controller.core

import io.github.agentseek.model.WorldImpl
import io.github.agentseek.model.components.PlayerComponent
import java.util.*

/**
 * This is an implementation of the GameState interface.
 */
class GameStateImpl(mainStage: Stage?) : GameState {
    private val view: ViewHandlerImpl
    override val world: World
    override val entities: MutableList<Entity>

    /**
     * {@inheritDoc}
     */
    override var isGameOver: Boolean = false
        private set

    /**
     * {@inheritDoc}
     */
    override var isVictory: Boolean = false
        private set
    private val eventHandler: ryleh.controller.events.EventHandler
    private val input: InputController
    private val levelHandler: LevelHandler
    override val player: Entity

    /**
     * Constructor method.
     *
     * @param mainStage Stage instance.
     */
    init {
        this.view = ViewHandlerImpl(mainStage)
        this.eventHandler = ryleh.controller.events.EventHandler(this)
        this.world = WorldImpl(eventHandler)
        this.entities = ArrayList<Entity>()
        this.levelHandler = LevelHandlerImpl(this)
        this.player = BasicFactory.getInstance().createPlayer(
            this,
            levelHandler.getPosition(levelHandler.getPlayerSpawn())
        )
        this.input = InputControllerImpl(this)
    }

    /**
     * {@inheritDoc}
     */
    override fun getPlayer(): Entity {
        return this.player
    }

    /**
     * {@inheritDoc}
     */
    override fun addEntity(entity: Entity) {
        entities.add(entity)
    }

    /**
     * {@inheritDoc}
     */
    override fun generateNewLevel() {
        world.getGameObjects().clear()
        entities.clear()
        view.getGraphicComponents().clear()
        view.displayLevelScene()
        levelHandler.generateNewLevel()
        view.addGraphicComponent(player.view)
        world.addGameObject(player.gameObject)
        entities.add(player)

        (player.gameObject.getComponent(PlayerComponent::class.java).get() as PlayerComponent)
            .setPosition(levelHandler.getPosition(levelHandler.getPlayerSpawn()))

        entities.addAll(levelHandler.getEntities())

        /**
         * Sorting the entities based on their GraphicComponent's zIndex, to correctly
         * set the order of rendering.
         */
        Collections.sort<T>(entities,
            Comparator<Any> { o1, o2 -> o1.view.getZindex() - o2.view.getZindex() })
        input.initInput()
        GameEngine.runDebugger { (levelHandler as LevelHandlerImpl).printSpawnPoints() }
    }

    /**
     * {@inheritDoc}
     */
    override fun removeEntity(entity: Entity) {
        entities.remove(entity)
        view.removeGraphicComponent(entity.view)
        world.removeGameObject(entity.gameObject)
    }

    /**
     * {@inheritDoc}
     */
    override fun updateState(deltaTime: Double) {
        input.updateInput()
        for (`object` in this.entities) {
            `object`.gameObject.onUpdate(deltaTime)
            `object`.view.render(
                GameMath.toPoint2D(
                    Point2d(
                        `object`.gameObject.getPosition().getX(),
                        `object`.gameObject.getPosition().getY()
                    )
                ), deltaTime
            )
        }
        view.onUpdate()
        eventHandler.checkEvents()
    }

    /**
     * {@inheritDoc}
     */
    override fun callGameOver(victory: Boolean) {
        this.isGameOver = true
        this.isVictory = victory
    }

    /**
     * {@inheritDoc}
     */
    override fun getView(): ViewHandlerImpl {
        return view
    }

    /**
     * {@inheritDoc}
     */
    override fun getLevelHandler(): LevelHandler {
        return this.levelHandler
    }

    /**
     * {@inheritDoc}
     */
    override fun getEntityByType(type: Type?): Optional<Entity> {
        return entities.stream().filter { i: Entity -> i.gameObject.getType().equals(type) }.findAny()
    }

    /**
     * {@inheritDoc}
     */
    override fun getWorld(): World {
        return this.world
    }

    /**
     * {@inheritDoc}
     */
    override fun getEntities(): List<Entity> {
        return this.entities
    }
}
