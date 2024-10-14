package ryleh.common;
/**
 * This class is an implementation of Timer interface and is used to measure a time range.
 */
public class TimerImpl implements Timer {

    private double startMills;
    private double elapsedMills;
    /**
     * Constructor method to instantiate a Timer given the amount of time to wait.
     * @param waitTime Time to wait.
     */
    public TimerImpl(final double waitTime) {
        this.reset();
        this.elapsedMills = waitTime;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isElapsed() {
        if (this.isStarted() && System.currentTimeMillis() - this.startMills > this.elapsedMills) {
            reset();
            return true;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startTimer() {
        if (!isStarted()) {
            this.startMills = System.currentTimeMillis();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setWaitTime(final double wait) {
        this.elapsedMills = wait;
    }

    /**
     * Checks if the timer is already working.
     * 
     * @return True if it's going on, false otherwise
     */
    private boolean isStarted() {
        return this.startMills != 0;
    }

    /**
     * Resets the Timer.
     */
    private void reset() {
        this.startMills = 0;
    }
}
