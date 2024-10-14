package ryleh.view.menu;

import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import ryleh.view.ViewHandlerImpl;

/**
 * This class represents GameOver menu, both victory screen and loss screen.
 */
public class RylehGameOverMenu {
    private final Stage primaryStage;
    private final Rectangle bgRectangle;
    private final MenuFactoryImpl factory = new MenuFactoryImpl();

    public RylehGameOverMenu(final Stage primaryStage, final boolean victory) {
        final ImagePattern gameOverImage;
        this.primaryStage = primaryStage;
        this.bgRectangle = new Rectangle();
        final String imagePath = victory ? "/assets/texture/menu/victory.png" : "/assets/texture/menu/gameOver.png";
        gameOverImage = new ImagePattern(new Image(imagePath, ViewHandlerImpl.getStandardWidth(),
                ViewHandlerImpl.getStandardHeight(), true, true));
        this.bgRectangle.setFill(gameOverImage);
        bgRectangle.heightProperty().bind(this.primaryStage.heightProperty().asObject());
        bgRectangle.widthProperty().bind(this.primaryStage.widthProperty().asObject());
    }

    /**
     * Shows the game over menu into primary stage.
     */
    public void show() {
        final AnchorPane pane = new AnchorPane();
        pane.getChildren().add(bgRectangle);
        pane.setStyle("-fx-background-color: rgba(0, 0, 0, 1);");
        pane.setOnMouseClicked((e) -> {
            factory.getController().quitGame();
        });
        this.primaryStage.getScene().setRoot(pane);
    }

}
