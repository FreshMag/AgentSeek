package ryleh.view.menu;

import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import ryleh.controller.menu.MenuController;
import ryleh.controller.menu.MenuControllerImpl;
import ryleh.view.ViewHandlerImpl;

/**
 * Implementation of Menu Factory.
 */
public class MenuFactoryImpl implements MenuFactory {

    private static final int SIZE = 40;
    private static final int RADIUS = 200;
    private final MenuController controller = new MenuControllerImpl();;
    private int scaledSize;
    private Font levelFont;
    private Color startColor;
    private Color hoverColor;
    private Text description;

    public MenuFactoryImpl() {
        this(SIZE);
    }

    public MenuFactoryImpl(final int scale) {
        description = new Text("");
        hoverColor = Color.CADETBLUE;
        startColor = Color.DARKSLATEBLUE;
        this.scaledSize = (int) (ViewHandlerImpl.getScaleModifier() * scale);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createCustomAlert(final String text) {
        final Stage window = new Stage();
        final HBox confirm = new HBox(scaledSize);
        final VBox container = new VBox();
        final Text question = new Text(text);
        question.setFont(new Font(scaledSize));
        confirm.getChildren().add(createCustomButton("YES", "", () -> {
            controller.quitGame();
        }));
        confirm.getChildren().add(createCustomButton("NO", "", () -> window.close()));
        confirm.setAlignment(Pos.CENTER);
        container.getChildren().add(question);
        container.getChildren().add(confirm);
        container.setAlignment(Pos.CENTER);
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Quit Game");
        window.setWidth(Screen.getPrimary().getBounds().getWidth() / 3);
        window.setHeight(Screen.getPrimary().getBounds().getHeight() / 3);
        window.setScene(new Scene(container));
        window.setResizable(false);
        window.showAndWait();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Button createCustomButton(final String name, final String description, final Runnable action) {
        final Button button = new Button(name);
        button.setFont(levelFont);
        button.textFillProperty().bind(Bindings.when(button.hoverProperty()).then(hoverColor).otherwise(startColor));
        button.setBackground(Background.EMPTY);
        button.backgroundProperty()
                .bind(Bindings.when(button.hoverProperty())
                        .then(new Background(new BackgroundFill(Color.DARKGRAY, new CornerRadii(RADIUS), null)))
                        .otherwise(Background.EMPTY));
        button.setOnMouseEntered(e -> {
            this.description.setText(description);
        });
        button.setOnMouseExited(e -> {
            this.description.setText("");
        });
        button.setOnAction(e -> {
            action.run();
        });
        return button;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getScaledSize() {
        return scaledSize;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Color getStartColor() {
        return startColor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Color getHoverColor() {
        return hoverColor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Text getDescription() {
        return description;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLevelFont(final Font levelFont) {
        this.levelFont = levelFont;
    }

    /**
     * {@inheritDoc}
     */
    public MenuController getController() {
        return controller;
    }
}
