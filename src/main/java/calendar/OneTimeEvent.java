package calendar;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class OneTimeEvent extends Event {
    private final LocalDate date;
    public OneTimeEvent(String n, LocalDate d, LocalTime sT, LocalTime eT) {
        super(n, sT, eT);
        date = d;
    }

    public OneTimeEvent(String n, LocalDate d, TimeInterval timeInterval) {
        super(n, timeInterval);
        date = d;
    }
    public LocalDate getDate() {return date;}
    @Override
    public String toString() {
        return String.format("%s %s", timeInterval, name);
    }
}
