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
 * A class that provides the GraphicComponent of the view related to the Fire
 * Entity.
 */
public class FireGraphicComponent implements GraphicComponent {

    private Rectangle rectangle;
    private int zIndex;
    private final Textures texture = Textures.FIRE1;

    /**
     * The duration of each frame of the Animation.
     */
    private static final int ANIM_DURATION = 300;
    private AnimationLoop animFire;

    /**
     * Creates the new Instance of FireGraphicComponent.
     */
    public FireGraphicComponent() {
        this(new Point2D(0, 0));
    }

    /**
     * Creates a new Instance of FireGraphicCOmponent with the given initial
     * position.
     * 
     * @param position The position at which the FireGraphicComponent needs to be
     *                 initialized.
     */
    public FireGraphicComponent(final Point2D position) {
        this.rectangle = new Rectangle(texture.getWidth(), texture.getHeight());
        this.rectangle.setX(position.getX() - rectangle.getWidth() / 2);
        this.rectangle.setY(position.getY() - rectangle.getHeight() / 2);
        this.rectangle.setFill(Textures.FIRE1.getImagePattern());
        this.animFire = new AnimationLoop(List.of(texture.getImagePattern(), Textures.FIRE2.getImagePattern(),
                Textures.FIRE3.getImagePattern(), Textures.FIRE4.getImagePattern(), Textures.FIRE5.getImagePattern()),
                ANIM_DURATION, rectangle);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void render(final Point2D position, final double deltaTime) {
        rectangle.setX(position.getX() - rectangle.getWidth() / 2);
        rectangle.setY(position.getY() - rectangle.getHeight() / 2);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onAdded(final Scene scene) {
        final Parent root = scene.getRoot();
        ((AnchorPane) root).getChildren().add(rectangle);
        animFire.play();
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
       return texture;
    }
}
