package calendar;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Event {
    private String name;
    private LocalDate date;
    private TimeInterval timeInterval;

    public Event() {
        name = "untitled";
        date = LocalDate.now();
        timeInterval = new TimeInterval();
    }

    public Event(String n, LocalDate d, LocalTime sT, LocalTime eT) {
        name = n;
        date = d;
        timeInterval = new TimeInterval(sT, eT);
    }

    public Event(String n, LocalDate d, TimeInterval timeInterval) {
        name = n;
        date = d;
        this.timeInterval = timeInterval;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public TimeInterval getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(TimeInterval timeInterval) {
        this.timeInterval = timeInterval;
    }

    @Override
    public String toString() {
        DateTimeFormatter year = DateTimeFormatter.ofPattern("yyyy");
        DateTimeFormatter day = DateTimeFormatter.ofPattern("d");
        DateTimeFormatter dayMonthDay = DateTimeFormatter.ofPattern("EEEE, MMMM d");
        return String.format("%s %s %s", dayMonthDay.format(date), timeInterval, name);
    }
}
