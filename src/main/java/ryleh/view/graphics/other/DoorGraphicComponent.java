package ryleh.view.graphics.other;

import java.util.List;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import ryleh.view.AnimationLoop;
import ryleh.view.Textures;
import ryleh.view.graphics.GraphicComponent;

/**
 * A class that provides the GraphicComponent of the view related to the Door
 * Entity.
 */
public class DoorGraphicComponent implements GraphicComponent {

    private Rectangle rectangle;
    private boolean animPlayed;
    private int zIndex;
    private final Textures texture = Textures.DOOR1;

    /**
     * The duration of each frame of the animation.
     */
    private static final int ANIM_DURATION = 250;
    /**
     * The total duration of the animation.
     */
    private static final int TOTAL_ANIM_DURATION = 1250;
    private AnimationLoop animDoor;

    /**
     * Creates a new Instance of BulletGraphicComponent.
     */
    public DoorGraphicComponent() {
        this(new Point2D(0, 0));
    }

    /**
     * Creates a new Instance of DoorGraphicComponent with the given initial
     * position.
     * 
     * @param position The position at which the DoorGraphicComponent needs to be
     *                 initialized.
     */
    public DoorGraphicComponent(final Point2D position) {
        this.rectangle = new Rectangle(texture.getWidth(), texture.getHeight());
        this.rectangle.setX(position.getX() - rectangle.getWidth() / 2);
        this.rectangle.setY(position.getY() - rectangle.getHeight() / 2);
        this.rectangle.setFill(Textures.DOOR1.getImagePattern());
        this.animPlayed = false;
        this.animDoor = new AnimationLoop(List.of(Textures.DOOR1.getImagePattern(), Textures.DOOR2.getImagePattern(),
                Textures.DOOR3.getImagePattern(), Textures.DOOR4.getImagePattern(), Textures.DOOR5.getImagePattern()),
                ANIM_DURATION, rectangle);
    }

    /**
     * A method to set the animation to played.
     */
    public void setAnimPlayed() {
        animPlayed = true;
    }

    /**
     * A method that tells if the current animation is finished.
     * 
     * @return True if the current animation is finished.
     */
    public boolean isAnimFinished() {
        return animDoor.isCycleFinished();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void render(final Point2D position, final double deltaTime) {
        rectangle.setX(position.getX() - rectangle.getWidth() / 2);
        rectangle.setY(position.getY() - rectangle.getHeight() / 2);
        if (animPlayed && this.isAnimFinished()) {
            animDoor.stop();
            rectangle.setFill(animDoor.getLastFrame());
        }
    }

    /**
     * A method that returns the total duration of the animation.
     * 
     * @return The total duration of the animation.
     */
    public int getTotalAnimDuration() {
        return DoorGraphicComponent.TOTAL_ANIM_DURATION;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onAdded(final Scene scene) {
        final Parent root = scene.getRoot();
        ((AnchorPane) root).getChildren().add(rectangle);
        this.animPlayed = true;
        animDoor.play();
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
       return texture;
    }
}
