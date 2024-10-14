package ryleh.controller.menu;

import javafx.application.Platform;
import javafx.stage.Stage;
import ryleh.controller.core.GameEngine;

public class MenuControllerImpl implements MenuController {

    private final GameEngine engine = new GameEngine();

    /**
     * {@inheritDoc}
     */
    @Override
    public void startGame(final Stage primaryStage) {
        engine.initGame(primaryStage);
        engine.mainLoop();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resumeGame() {
        GameEngine.resumeEngine();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDeveloperMode() {
        GameEngine.setDeveloper(!GameEngine.isDeveloper());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void quitGame() {
        Platform.exit();
        System.exit(0);
    }
}
