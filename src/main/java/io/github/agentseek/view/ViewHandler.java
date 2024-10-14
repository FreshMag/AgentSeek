package ryleh.view;

import java.util.List;

import javafx.scene.Scene;
import ryleh.view.graphics.GraphicComponent;

public interface ViewHandler {

    /**
     * A method to remove a GraphicComponent from the list of GraphicComponents and
     * removes it from the scene.
     * 
     * @param graphic The GraphicComponents that needs to be removed from the list
     *                of GraphicComponents and from the scene.
     */
    void removeGraphicComponent(GraphicComponent graphic);

    /**
     * A method that adds a GraphicCimponent to the list of GraphicComponents and
     * adds it on the scene.
     * 
     * @param graphic The GraphicComponent to be added to the list of
     *                GraphicComponents and to the scene.
     */
    void addGraphicComponent(GraphicComponent graphic);

    /**
     * A method that returns the list of GraphicComponents.
     * 
     * @return The list of GraphicComponents.
     */
    List<GraphicComponent> getGraphicComponents();

    /**
     * A method to set scene of the current level.
     */
    void displayLevelScene();

    /**
     * A method that returns the current scene.
     * 
     * @return The current scene.
     */
    Scene getScene();

    /**
     * Method to call every GameLoop that resizes every texture.
     */
    void onUpdate();
}
