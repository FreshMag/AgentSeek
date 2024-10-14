package ryleh.controller.events;
/**
 * This interface manages Event instances.
 */
public interface EventListener {
    /**
     * Notify the happening of an Event.
     * 
     * @param e Event to be notified
     */
    void notifyEvent(Event e);
}
