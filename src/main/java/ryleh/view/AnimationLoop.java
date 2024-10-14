package ryleh.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Timer;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

/**
 * A class that provides the implementation of an Animation, given the list of
 * frames and the Rectangle to be filled.
 */
public class AnimationLoop {

    private final List<ImagePattern> frames;
    private boolean cycleFinished;
    private final Timer timer;
    private int currentFrame;

    /**
     * Creates a new Instance of AnimationLoop with the given list of Frames, the
     * duration of each frame and the rectangle.
     * 
     * @param frames    An ordered list of the frames that form the animation.
     * @param duration  The number of loops needed to wait between each frame of the
     *                  animation.
     * @param rectangle The rectangle that needs to be filled with the correct
     *                  frame.
     */
    public AnimationLoop(final List<ImagePattern> frames, final int duration, final Rectangle rectangle) {
        this.frames = new ArrayList<>(frames);
        this.cycleFinished = false;
        this.currentFrame = 0;
        this.timer = new Timer(duration, new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                getNextFrame(rectangle);
                resetTimer();
            }
        });
    }

    /**
     * A method to play the animation.
     */
    public void play() {
        timer.start();
    }

    /**
     * A method to stop the animation.
     */
    public void stop() {
        timer.stop();
    }

    /**
     * A method to reset the timer of the animation.
     */
    public void resetTimer() {
        timer.restart();
    }

    /**
     * A method to set the rectangle fillProperty with the correct frame based on
     * the current timer counter.
     * 
     * @param rectangle The rectangle that needs to update its fillProperty with the
     *                  current frame of the animation.
     * @return The given rectangle with the correct fillProperty and the correct
     *         frame of the animation.
     */
    public Rectangle getNextFrame(final Rectangle rectangle) {
        currentFrame++;
        if (currentFrame == frames.size()) {
            currentFrame = 0;
            cycleFinished = true;
        }
        rectangle.setFill(frames.get(currentFrame));
        return rectangle;
    }

    /**
     * A method to check if the complete animation has finished.
     * 
     * @return true if the current animation is finished,
     */
    public boolean isCycleFinished() {
        return this.cycleFinished;
    }

    /**
     * A method that returns the last frame of the animation.
     * 
     * @return the last frame of the animation.
     */
    public ImagePattern getLastFrame() {
        return frames.get(frames.size() - 1);
    }
}
