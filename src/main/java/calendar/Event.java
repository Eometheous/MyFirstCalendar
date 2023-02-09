package calendar;

import java.time.LocalTime;

abstract class Event {
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

    public TimeInterval getTimeInterval() {return timeInterval;}
}
