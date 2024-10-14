package ryleh;

import javafx.application.Application;
import javafx.stage.Stage;
import ryleh.view.menu.RylehMainMenu;

public class Ryleh extends Application {
    public static void main(final String[] args) {
        launch(args);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start(final Stage primaryStage) throws Exception {
        final RylehMainMenu menu = new RylehMainMenu(primaryStage);
        menu.show();
    }
}
