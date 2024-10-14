package ryleh.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.collections.transformation.FilteredList;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import ryleh.view.graphics.GraphicComponent;

/**
 * A class to Completely handle the view.
 */
public class ViewHandlerImpl implements ViewHandler {
    /**
     * To calculate scale of window size.
     */
    private static final double STANDARD_SCALE = 1920.0;
    private double lastWidth;
    private double lastHeight;
    private final Stage stage;
    private final List<GraphicComponent> graphicComponents;
    private final Scene scene;
    private Parent root;
    private final Rectangle background;
    private final GameUI gameUi;
    private boolean isFirstRoom;
    private Textures bgTexture = Textures.BACKGROUND;
    private final double aspectRatio = 16.0 / 9.0;

    /**
     * The width of the screen.
     */
    private static double standardWidth = Screen.getPrimary().getBounds().getWidth();
    /**
     * The height of the screen.
     */
    private static double standardHeight = Screen.getPrimary().getBounds().getHeight();
    /**
     * The modifier to set the correct proportion of the view.
     */
    private static double scaleModifier = ViewHandlerImpl.standardWidth / STANDARD_SCALE;

    public static double getStandardWidth() {
        return standardWidth;
    }

    public static void setStandardWidth(final double d) {
        standardWidth = d;
    }

    public static double getStandardHeight() {
        return standardHeight;
    }

    public static void setStandardHeight(final double d) {
        standardHeight = d;
    }

    public static double getScaleModifier() {
        return scaleModifier;
    }

    public static void setScaleModifier(final double scalemodifier) {
        scaleModifier = scalemodifier;
    }

    /**
     * Creates a new Instance of ViewHandler with the given stage.
     * 
     * @param stage the stage that needs to be set.
     */
    public ViewHandlerImpl(final Stage stage) {
        this.gameUi = new GameUI();
        this.stage = stage;
        this.background = new Rectangle(bgTexture.getWidth(), bgTexture.getHeight());
        root = new AnchorPane();
        root.setStyle("-fx-background-color: black;");
        ((AnchorPane) root).getChildren().add(background);
        scene = new Scene(root);
        this.stage.setScene(scene);
        this.graphicComponents = new ArrayList<>();
        this.isFirstRoom = true;
        this.lastWidth = standardWidth;
        this.lastHeight = standardHeight;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeGraphicComponent(final GraphicComponent graphic) {
        graphic.onRemoved(e -> {
            final FilteredList<Node> list = ((AnchorPane) scene.getRoot()).getChildren()
                    .filtered(i -> graphic.getNode().equals(i));
            if (!list.isEmpty()) {
                list.get(0).setVisible(false);
            }
            this.graphicComponents.remove(graphic);
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void displayLevelScene() {
        root = new AnchorPane();
        root.setStyle("-fx-background-color: black;");
        final Random generator = new Random();
        switch (generator.nextInt(3)) {
        case 1:
            this.bgTexture = Textures.BACKGROUND2;
            break;
        case 2:
            this.bgTexture = Textures.BACKGROUND3;
            break;
        case 0:
        default:
            this.bgTexture = Textures.BACKGROUND;
            break;
        }
        this.background.setFill(bgTexture.getImagePattern());
        ((AnchorPane) root).getChildren().add(background);
        ((AnchorPane) root).getChildren().add(gameUi.getLevel());
        ((AnchorPane) root).getChildren().add(gameUi.getLives());
        ((AnchorPane) root).getChildren().add(gameUi.getItemPickUp());
        if (this.isFirstRoom) {
            for (final Text elem : gameUi.getTutorial()) {
                ((AnchorPane) root).getChildren().add(elem);
            }
            this.isFirstRoom = false;
        }
        scene.setRoot(root);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addGraphicComponent(final GraphicComponent graphic) {
        this.graphicComponents.add(graphic);
        graphic.onAdded(scene);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<GraphicComponent> getGraphicComponents() {
        return graphicComponents;
    }

    /**
     * A method that returns the current stage.
     * 
     * @return The current stage.
     */
    public Stage getStage() {
        return stage;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Scene getScene() {
        return scene;
    }

    /**
     * A method that returns the current gameUi.
     * 
     * @return a GameUI object.
     */
    public GameUI getGameUi() {
        return gameUi;
    }
    /**
     * {@inheritDoc}
     */
    public void onUpdate() {
        if (this.stage.getWidth() != this.lastWidth) {
            this.stage.setHeight(this.stage.getWidth() / aspectRatio);
        } else if (this.stage.getHeight() != this.lastHeight) {
            this.stage.setWidth(this.stage.getHeight() * aspectRatio);
        }
        setStandardHeight(this.stage.getHeight());
        setStandardWidth(this.stage.getWidth());
        this.lastHeight = getStandardHeight();
        this.lastWidth = getStandardWidth();
        setScaleModifier(standardWidth / STANDARD_SCALE);
        this.background.setWidth(bgTexture.getWidth());
        this.background.setHeight(bgTexture.getHeight());
        for (final GraphicComponent g : getGraphicComponents()) {
            g.getNode().setWidth(g.getTexture().getWidth());
            g.getNode().setHeight(g.getTexture().getHeight());
        }
        this.gameUi.updateScale();
    }
}
