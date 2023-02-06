package calendar;

import java.time.LocalTime;

abstract class Event implements Comparable<Event> {
    protected String name;
    protected TimeInterval timeInterval;

    public Event(String name, LocalTime sT, LocalTime eT) {
        this.name = name;
        this.timeInterval = new TimeInterval(sT, eT);
    }

    public Event(String name, TimeInterval timeInterval) {
        this.name = name;
        this.timeInterval = timeInterval;
    }

    /**
     * Compares this event with event e
     * @param e the object to be compared.
     * @return  -1 if it is before this event, 1 if it is after this event, and 0 if it is the same day as this event
     */
    public int compareTo(Event e) {
        if (timeInterval.getStart().isBefore(e.getTimeInterval().getStart())) return -1;
        else if (timeInterval.getStart().isAfter(e.getTimeInterval().getStart())) return 1;
        return 0;
    }

    public TimeInterval getTimeInterval() {return timeInterval;}
}
