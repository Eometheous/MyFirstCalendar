package calendar;

import java.time.LocalTime;

/**
 * An event without a specific date.
 * {@code Event} holds a name and a {@code TimeInterval}
 * @author Jonathan Stewart Thomas
 * @version 1.0.0.230209
 */
abstract class Event {
    protected String name;
    protected TimeInterval timeInterval;

    /**
     * Creates an {@code Event} with a name, start time and end time
     * @param name      the name of the event
     * @param startTime start time of event
     * @param endTime   end time of event
     */
    public Event(String name, LocalTime startTime, LocalTime endTime) {
        this.name = name;
        this.timeInterval = new TimeInterval(startTime, endTime);
    }
}
