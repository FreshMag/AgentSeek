package ryleh.view;

import java.util.ArrayList;
import java.util.List;
import javafx.animation.FadeTransition;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import ryleh.Ryleh;

/**
 * This class is used to handle game UI.
 */
public class GameUI {
    /**
     * To adjust lives UI's text indentation.
     */
    private static final int LIVES_INDENTATION = 11;
    /**
     * Duration of fading transition for item effect text.
     */
    private static final double FT_DURATION = 4000;
    /**
     * Screen width ratio value.
     */
    private static final int SCREEN_WIDTH_RATIO = 16;
    /**
     * Screen height ratio value.
     */
    private static final int SCREEN_HEIGHT_RATIO = 9;
    /**
     * Pixel correction on X,Y coordinates of Text objects (Lives and Levels).
     */
    private static final int PIXEL_CORRECTION = 75;
    /**
     * Font scale.
     */
    private static final int FONT_SCALE = 37;
    private Text lives;
    private Text level;
    private Text item;
    private final List<Text> tutorialUi;
    private Font font;

    /**
     * Creates a new Instance of GameUI.
     */
    public GameUI() {
        this.tutorialUi = new ArrayList<>();
        this.font = Font.loadFont(Ryleh.class.getResource("/assets/fonts/manaspc.ttf").toExternalForm(),
                FONT_SCALE * ViewHandlerImpl.getScaleModifier());
        this.setLives();
        this.setLevel();
        this.setTutorial();
        this.setItemPickUp();
    }

    /**
     * A method to set the text that represents the current level.
     */
    private void setLevel() {
        this.level = new Text();
        this.level.setFont(this.font);
        this.level.setX((ViewHandlerImpl.getStandardWidth() / SCREEN_WIDTH_RATIO) * 2
                + PIXEL_CORRECTION * ViewHandlerImpl.getScaleModifier());
        this.level.setY((ViewHandlerImpl.getStandardHeight() / SCREEN_HEIGHT_RATIO) * 1);
        this.level.setFill(Color.WHITE);
    }

    /**
     * A method to set the tutorial text that will appear on the first level.
     */
    private void setTutorial() {
        final Text tutorialObjective;
        final Text tutorialCommands = new Text(
                "List of commands:\n W -> move forward \n A -> move left \n S -> move downward \n D -> move right \n Spacebar -> shoot");
        tutorialCommands.setFont(this.font);
        tutorialCommands.setX((ViewHandlerImpl.getStandardWidth() / SCREEN_WIDTH_RATIO) * 2
                + PIXEL_CORRECTION * ViewHandlerImpl.getScaleModifier());
        tutorialCommands.setY((ViewHandlerImpl.getStandardHeight() / SCREEN_HEIGHT_RATIO) * 3);
        tutorialCommands.setFill(Color.WHITE);
        tutorialObjective = new Text(
                "In order to win you must \nbeat level 30.\nAfter defeating all \nenemies on a level a \ndoor will open and bring \nyou to the next level.\n"
                        + "Every 3 levels an \nitem will spawn.\n"
                        + "If you lose all your \nlives you will lose.\nGood luck and have fun!");
        tutorialObjective.setFont(this.font);
        tutorialObjective.setX((ViewHandlerImpl.getStandardWidth() / SCREEN_WIDTH_RATIO) * 8
                + PIXEL_CORRECTION * ViewHandlerImpl.getScaleModifier());
        tutorialObjective.setY((ViewHandlerImpl.getStandardHeight() / SCREEN_HEIGHT_RATIO) * 3);
        tutorialObjective.setFill(Color.WHITE);
        tutorialUi.add(tutorialCommands);
        tutorialUi.add(tutorialObjective);
    }

    /**
     * A method to set the text that represents the current item effect.
     */
    private void setItemPickUp() {
        this.item = new Text();
        this.item.setFont(this.font);
        this.item.setX((ViewHandlerImpl.getStandardWidth() / SCREEN_WIDTH_RATIO) * 2);
        this.item.setY((ViewHandlerImpl.getStandardHeight() / SCREEN_HEIGHT_RATIO) * 8);
        this.item.setFill(Color.WHITE);
        this.item.setVisible(false);
    }

    /**
     * A method to set the text that represents the remaining lives.
     */
    private void setLives() {
        this.lives = new Text("");
        this.lives.setFont(this.font);
        this.lives.setX((ViewHandlerImpl.getStandardWidth() / SCREEN_WIDTH_RATIO) * LIVES_INDENTATION
                + PIXEL_CORRECTION * ViewHandlerImpl.getScaleModifier());
        this.lives.setY((ViewHandlerImpl.getStandardHeight() / SCREEN_HEIGHT_RATIO) * 1);
        this.lives.setFill(Color.WHITE);
    }

    /**
     * A method that sets visibility of item to true and plays a fading transition
     * for the item effects text.
     */
    public void playFt() {
        this.item.setVisible(true);
        final FadeTransition ft = new FadeTransition(Duration.millis(FT_DURATION), this.item);
        ft.setFromValue(1.0);
        ft.setToValue(0);
        ft.setCycleCount(1);
        ft.setAutoReverse(false);
        ft.play();
    }

    /**
     * This method updates the position of gameUI elements after the ScaleModifier
     * has been changed.It also calls updateFontScale() method.
     */
    public final void updateScale() {
        this.tutorialUi.get(0).setX((ViewHandlerImpl.getStandardWidth() / SCREEN_WIDTH_RATIO) * 2
                + PIXEL_CORRECTION * ViewHandlerImpl.getScaleModifier());
        this.tutorialUi.get(0).setY((ViewHandlerImpl.getStandardHeight() / SCREEN_HEIGHT_RATIO) * 3);
        this.tutorialUi.get(1).setX((ViewHandlerImpl.getStandardWidth() / SCREEN_WIDTH_RATIO) * 8
                + PIXEL_CORRECTION * ViewHandlerImpl.getScaleModifier());
        this.tutorialUi.get(1).setY((ViewHandlerImpl.getStandardHeight() / SCREEN_HEIGHT_RATIO) * 3);
        this.level.setX((ViewHandlerImpl.getStandardWidth() / SCREEN_WIDTH_RATIO) * 2
                + PIXEL_CORRECTION * ViewHandlerImpl.getScaleModifier());
        this.level.setY((ViewHandlerImpl.getStandardHeight() / SCREEN_HEIGHT_RATIO) * 1);
        this.lives.setX((ViewHandlerImpl.getStandardWidth() / SCREEN_WIDTH_RATIO) * LIVES_INDENTATION
                + PIXEL_CORRECTION * ViewHandlerImpl.getScaleModifier());
        this.lives.setY((ViewHandlerImpl.getStandardHeight() / SCREEN_HEIGHT_RATIO) * 1);
        this.item.setX((ViewHandlerImpl.getStandardWidth() / SCREEN_WIDTH_RATIO) * 2);
        this.item.setY((ViewHandlerImpl.getStandardHeight() / SCREEN_HEIGHT_RATIO) * 8);
        updateFontScale();
    }

    /**
     * This method updates font size after the ScaleModifier has been changed.
     */
    private void updateFontScale() {
        this.font = Font.loadFont(Ryleh.class.getResource("/assets/fonts/manaspc.ttf").toExternalForm(),
                FONT_SCALE * ViewHandlerImpl.getScaleModifier());
        this.level.setFont(this.font);
        this.item.setFont(this.font);
        this.lives.setFont(this.font);
        this.tutorialUi.get(0).setFont(this.font);
        this.tutorialUi.get(1).setFont(this.font);
    }

    /**
     * A method that returns the number of remaining lives Text.
     * 
     * @return the number of remaining lives.
     */
    public Text getLives() {
        return this.lives;
    }

    /**
     * A method that returns the last item effect Text.
     * 
     * @return the number of remaining lives.
     */
    public Text getItemPickUp() {
        return this.item;
    }

    /**
     * A method that returns the current level Text.
     * 
     * @return the current level number.
     */
    public Text getLevel() {
        return this.level;
    }

    /**
     * A method that returns the tutorial Ui elements Texts.
     * 
     * @return the current level number.
     */
    public List<Text> getTutorial() {
        return this.tutorialUi;
    }
}
