package ryleh.controller;

import javafx.scene.Scene;
import ryleh.controller.core.GameEngine;
import ryleh.controller.core.GameState;
import ryleh.controller.events.NewLevelEvent;
import ryleh.controller.levels.LevelHandler;
import ryleh.controller.levels.LevelHandlerImpl;
import ryleh.model.World;
import ryleh.model.components.PlayerComponent;
import ryleh.model.components.ShootingComponent;
import ryleh.model.physics.Direction;
import ryleh.view.graphics.PlayerGraphicComponent;
import ryleh.view.menu.RylehPauseMenu;

/**
 * A class that manages all aspects of an input control and implements
 * InputController interface.
 */
public class InputControllerImpl implements InputController {

    private boolean isMoveUp;
    private boolean isMoveDown;
    private boolean isMoveLeft;
    private boolean isMoveRight;
    private boolean isShooting;
    private Direction currentDir;
    private final PlayerGraphicComponent graphic;
    private final PlayerComponent physics;
    private final Scene scene;
    private final Entity player;
    private final GameState state;
    private final World world;

    private final RylehPauseMenu pauseMenu;

    /**
     * Instantiates an Input controller given the current state of the game.
     * 
     * @param state Current state of the game.
     */
    public InputControllerImpl(final GameState state) {
        this.state = state;
        this.scene = state.getView().getScene();
        this.world = state.getWorld();
        this.player = state.getPlayer();
        this.graphic = (PlayerGraphicComponent) this.player.getView();
        this.physics = (PlayerComponent) this.player.getGameObject().getComponent(PlayerComponent.class).get();
        this.currentDir = this.physics.getDirection();
        this.pauseMenu = new RylehPauseMenu(this.state.getView().getStage());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initInput() {
        this.scene.setOnKeyPressed(key -> {
            switch (key.getCode()) {
            case A:
                this.currentDir = Direction.LEFT;
                isMoveLeft = true;
                break;
            case D:
                this.currentDir = Direction.RIGHT;
                isMoveRight = true;
                break;
            case W:
                this.currentDir = Direction.UP;
                isMoveUp = true;
                break;
            case S:
                this.currentDir = Direction.DOWN;
                isMoveDown = true;
                break;
            case L:
                if (GameEngine.isDeveloper()) {
                    world.notifyWorldEvent(new NewLevelEvent());
                }
                break;
            case K:
                if (GameEngine.isDeveloper()) {
                    final LevelHandler levels = state.getLevelHandler();
                    levels.setLevel(LevelHandlerImpl.getMaxLevel() - 1);
                    state.generateNewLevel();
                }
                break;
            case SPACE:
                this.isShooting = true;
                break;
            case P:
                this.pauseGame();
                break;
            case ESCAPE:
                this.pauseGame();
            default:
                break;
            }
        });
        scene.setOnKeyReleased(key -> {
            switch (key.getCode()) {
            case A:
                isMoveLeft = false;
                this.currentDir = isMoving() ? this.currentDir : Direction.IDLE;
                break;
            case D:
                isMoveRight = false;
                this.currentDir = isMoving() ? this.currentDir : Direction.IDLE;
                break;
            case W:
                isMoveUp = false;
                this.currentDir = isMoving() ? this.currentDir : Direction.IDLE;
                break;
            case S:
                isMoveDown = false;
                this.currentDir = isMoving() ? this.currentDir : Direction.IDLE;
                break;
            case SPACE:
                this.isShooting = false;
                break;
            default:
                break;
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateInput() {
        this.stillMoving();
        this.move(this.currentDir);
        if (this.isShooting) {
            this.spawnBullet(this.physics.getLastDirection());
        }
    }

    /**
     * Spawns a bullet with the same direction of the player.
     * 
     * @param direction Direction of spawned bullet
     */
    private void spawnBullet(final Direction direction) {
        ((ShootingComponent) player.getGameObject().getComponent(ShootingComponent.class).get())
                .shoot(direction.getVector());
    }

    /**
     * Moves the player in a possible direction.
     * 
     * @param direction Direction that the physics component has to use to move the
     *                  object
     */
    private void move(final Direction direction) {
        this.physics.setVelocity(direction.getVector());
        this.physics.setDirection(direction);
        if (!this.physics.getBlocked().equals(direction)) {
            this.physics.resetBlocked();
        }
        this.graphic.setDirection(direction);
    }

    /**
     * Checks if the player is moving in one of the possible directions.
     * 
     * @return True if he is moving, False otherwise
     */
    private boolean isMoving() {
        return this.isMoveDown || this.isMoveLeft || this.isMoveRight || this.isMoveUp;
    }

    /**
     * Method to avoid the player to move along the edge even without any key being
     * pressed.
     */
    private void stillMoving() {
        if (this.currentDir.equals(Direction.UP) && !isMoveUp) {
            this.currentDir = Direction.IDLE;
        } else if (this.currentDir.equals(Direction.DOWN) && !isMoveDown) {
            this.currentDir = Direction.IDLE;
        } else if (this.currentDir.equals(Direction.LEFT) && !isMoveLeft) {
            this.currentDir = Direction.IDLE;
        } else if (this.currentDir.equals(Direction.RIGHT) && !isMoveRight) {
            this.currentDir = Direction.IDLE;
        }
    }

    /**
     * method to pause the game and enter the game menu.
     */
    private void pauseGame() {
        GameEngine.pauseEngine();
        pauseMenu.renderPauseMenu();
    }
}
