package ryleh.controller.menu;

import javafx.stage.Stage;

public interface MenuController {

    /**
     * Starts a new game with the initializing of a new game loop.
     * 
     * @param primaryStage The visible stage used to show and interact with users
     */
    void startGame(Stage primaryStage);

    /**
     * Resumes the game.
     */
    void resumeGame();

    /**
     * Sets DeveloperMode on/off.
     */
    void setDeveloperMode();

    /**
     * Exits the game.
     */
    void quitGame();

}
