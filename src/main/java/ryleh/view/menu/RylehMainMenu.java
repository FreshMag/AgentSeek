package ryleh.view.menu;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Pair;
import ryleh.Ryleh;
import ryleh.view.ViewHandlerImpl;

/**
 * This class represents the game's main menu. It uses MenuFactory to
 * instantiate graphic parts inside the constructor. To render it, call "show"
 * method.
 */
public class RylehMainMenu {
    /**
     * Standard scaling of the menu.
     */
    private static final double STANDARD_SCALE = 1920.0;
    private final Stage primaryStage;
    private MenuFactory factory;
    private static final List<Pair<Integer, Integer>> STANDARD_SCALES = List.of(new Pair<>(1920, 1080),
            new Pair<>(1280, 720), new Pair<>(800, 450));

    /**
     * 
     * @param primaryStage The stage that contains the Menu
     */
    public RylehMainMenu(final Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    /**
     * Shows Ryleh's Call main menu.
     */
    public void show() {
        this.factory = new MenuFactoryImpl();
        this.newMenu();
        this.primaryStage.show();
    }

    /**
     * Creates a new Main Menu with custom buttons and a description for every of
     * them.
     *
     */
    public void newMenu() {
        factory.setLevelFont(Font.loadFont(Ryleh.class.getResource("/assets/fonts/saturno.ttf").toExternalForm(),
                factory.getScaledSize()));
        final Separator separator = new Separator();
        final BorderPane pane = new BorderPane();
        final VBox leftBox = new VBox();
        final ImagePattern backgroundImage = new ImagePattern(new Image("/assets/texture/menu/menu.png"));
        final Rectangle bgRectangle = new Rectangle(backgroundImage.getWidth(), backgroundImage.getHeight());
        final Button startBtn = factory.createCustomButton("Start Game", "Start a new adventure", () -> {
            factory.getController().startGame(primaryStage);
        });
        final Button exitBtn = factory.createCustomButton("Quit Game", "Exit to desktop", () -> {
            factory.createCustomAlert("Do you really want to quit?");
        });
        bgRectangle.setFill(backgroundImage);
        bgRectangle.heightProperty().bind(this.primaryStage.heightProperty().asObject());
        bgRectangle.widthProperty().bind(this.primaryStage.widthProperty().asObject());
        separator.setOrientation(Orientation.HORIZONTAL);
        factory.getDescription().setFont(new Font(factory.getScaledSize()));
        factory.getDescription().setFill(factory.getStartColor());
        leftBox.setAlignment(Pos.CENTER);
        leftBox.getChildren().add(startBtn);
        leftBox.getChildren().add(exitBtn);
        leftBox.getChildren().addAll(separator, factory.getDescription(), this.createComboBox());
        pane.getChildren().add(bgRectangle);
        pane.setCenter(leftBox);
        this.primaryStage
                .setScene(new Scene(pane, ViewHandlerImpl.getStandardWidth(), ViewHandlerImpl.getStandardHeight()));
        this.primaryStage.setResizable(true);
        this.primaryStage.setTitle("Ryleh's Call");
        this.primaryStage.setOnCloseRequest(e -> factory.getController().quitGame());
    }

    private Node createComboBox() {
        final List<Pair<Integer, Integer>> resolutions = new ArrayList<>();
        final ComboBox<Pair<Integer, Integer>> combo = new ComboBox<>();
        resolutions.addAll(STANDARD_SCALES);
        combo.setPromptText("Select a Resolution");
        combo.getItems().addAll(resolutions);
        combo.setOnAction(e -> {
            ViewHandlerImpl.setStandardWidth(combo.getValue().getKey());
            ViewHandlerImpl.setStandardHeight(combo.getValue().getValue());
            ViewHandlerImpl.setScaleModifier(ViewHandlerImpl.getStandardWidth() / STANDARD_SCALE);
            this.show();
        });
        return combo;
    }
}
