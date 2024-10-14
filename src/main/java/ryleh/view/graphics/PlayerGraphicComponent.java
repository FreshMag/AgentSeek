package ryleh.view.graphics;

import java.util.List;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import ryleh.model.physics.Direction;
import ryleh.view.AnimationLoop;
import ryleh.view.Textures;

/**
 * A class that provides the GraphicComponent of the view related to the Player
 * Entity.
 */
public class PlayerGraphicComponent implements GraphicComponent {
    private Rectangle rectangle;
    private Direction direction;
    private Direction lastDir;
    private FadeTransition playerFade;
    private Boolean invincible;
    private int zIndex;

    /**
     * Duration of each frame of the animation.
     */
    private static final int ANIM_DURATION = 90;
    /**
     * Duration of the fade animation.
     */
    private static final int FADE_DURATION = 200;

    private AnimationLoop currentLoop;
    private AnimationLoop animRight;
    private AnimationLoop animLeft;
    private AnimationLoop animUp;
    private AnimationLoop animDown;

    /**
     * Creates a new Instance of PlayerGraphicComponent.
     */
    public PlayerGraphicComponent() {
        this(new Point2D(0, 0));
    }

    /**
     * Creates a new Instance of PlayerGraphicComponent with the given initial
     * position.
     * 
     * @param position The position at witch the PlayerGraphicComponent needs to be
     *                 initialized.
     */
    public PlayerGraphicComponent(final Point2D position) {
        this.rectangle = new Rectangle(Textures.PLAYER_DOWN.getWidth(), Textures.PLAYER_DOWN.getHeight());
        this.rectangle.setX(position.getX() - rectangle.getWidth() / 2);
        this.rectangle.setY(position.getY() - rectangle.getHeight() / 2);
        this.rectangle.setFill(Textures.PLAYER_DOWN.getImagePattern());

        this.playerFade = new FadeTransition(Duration.millis(FADE_DURATION), rectangle);
        this.playerFade.setFromValue(1.0);
        this.playerFade.setToValue(0.0);
        this.playerFade.setCycleCount(4);
        this.playerFade.setAutoReverse(true);

        this.animRight = new AnimationLoop(List.of(Textures.PLAYER_RIGHT2.getImagePattern(),
                Textures.PLAYER_RIGHT.getImagePattern(), Textures.PLAYER_RIGHT4.getImagePattern()), ANIM_DURATION,
                rectangle);
        animLeft = new AnimationLoop(List.of(Textures.PLAYER_LEFT2.getImagePattern(),
                Textures.PLAYER_LEFT.getImagePattern(), Textures.PLAYER_LEFT4.getImagePattern()), ANIM_DURATION,
                rectangle);
        animUp = new AnimationLoop(List.of(Textures.PLAYER_UP2.getImagePattern(), Textures.PLAYER_UP.getImagePattern(),
                Textures.PLAYER_UP4.getImagePattern()), ANIM_DURATION, rectangle);
        animDown = new AnimationLoop(List.of(Textures.PLAYER_DOWN2.getImagePattern(),
                Textures.PLAYER_DOWN.getImagePattern(), Textures.PLAYER_DOWN4.getImagePattern()), ANIM_DURATION,
                rectangle);

        this.invincible = false;
        this.direction = Direction.IDLE;
        this.lastDir = Direction.IDLE;
        this.currentLoop = animDown;
    }

    /**
     * A method to update the state of this GraphicComponent in the view.
     * 
     * @param direction The direction at which the entity is in the current
     *                  GameLoop, to determine the right animation.
     */
    private void updateImage(final Direction direction) {
        switch (direction) {
        case RIGHT:
            if (lastDir != Direction.RIGHT) {
                currentLoop.stop();
                currentLoop = animRight;
                animRight.play();
                lastDir = Direction.RIGHT;
            }
            break;

        case LEFT:
            if (lastDir != Direction.LEFT) {
                currentLoop.stop();
                currentLoop = animLeft;
                animLeft.play();
                lastDir = Direction.LEFT;
            }
            break;

        case UP:
            if (lastDir != Direction.UP) {
                currentLoop.stop();
                currentLoop = animUp;
                animUp.play();
                lastDir = Direction.UP;
            }
            break;

        case DOWN:
            if (lastDir != Direction.DOWN) {
                currentLoop.stop();
                currentLoop = animDown;
                animDown.play();
                lastDir = Direction.DOWN;
            }
            break;

        case IDLE:
            currentLoop.stop();
            switch (lastDir) {
            case RIGHT:
                rectangle.setFill(Textures.PLAYER_RIGHT.getImagePattern());
                break;
            case LEFT:
                rectangle.setFill(Textures.PLAYER_LEFT.getImagePattern());
                break;
            case DOWN:
                rectangle.setFill(Textures.PLAYER_DOWN.getImagePattern());
                break;
            case UP:
                rectangle.setFill(Textures.PLAYER_UP.getImagePattern());
                break;
            default:
                break;
            }
            lastDir = Direction.IDLE;
        default:
            break;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onAdded(final Scene scene) {
        final Parent root = scene.getRoot();
        ((AnchorPane) root).getChildren().add(rectangle);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void render(final Point2D position, final double deltaTime) {
        rectangle.setX(position.getX() - rectangle.getWidth() / 2);
        rectangle.setY(position.getY() - rectangle.getHeight() / 2);
        this.checkInvincible();
        this.updateImage(direction);
    }

    /**
     * A method to check if the Invincible animations needs to be played.
     */
    private void checkInvincible() {
        if (invincible) {
            playerFade.play();
        }
    }

    /**
     * Sets the invincible field with the given parameter.
     * 
     * @param invincible The value at which the field invincible is to be set.
     */
    public void setInvincible(final Boolean invincible) {
        this.invincible = invincible;
    }

    /**
     * Sets the direction field with the given parameter.
     * 
     * @param direction The value at which the field direction is to be set.
     */
    public void setDirection(final Direction direction) {
        this.direction = direction;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Rectangle getNode() {
        return rectangle;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onRemoved(final EventHandler<ActionEvent> event) {
        event.handle(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setZindex(final int zIndex) {
        this.zIndex = zIndex;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getZindex() {
        return zIndex;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Textures getTexture() {
       return Textures.PLAYER_DOWN;
    }
}
