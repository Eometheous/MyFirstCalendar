package calendar;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

/**
 * An event with a name, start date, and time interval.
 * {@code Event} implements {@code Comparable} to compare
 * {@code Event} start times. {@code Event} also utilizes
 * a Comparator to sort by start date and start time.
 * @author Jonathan Stewart Thomas
 * @version 1.0.1.230214
 */
public class Event implements Comparable<Event>{
    public static final Comparator<Event> DATE_TIME_ORDER = (e1, e2) -> {
        if (e1.getStartDate().isBefore(e2.getStartDate())) return -1;
        else if (e1.getStartDate().isAfter(e2.getStartDate())) return 1;
        else if (e1.getTimeInterval().getStart().isBefore(e2.getTimeInterval().getStart())) return -1;
        else if (e1.getTimeInterval().getStart().isAfter(e2.getTimeInterval().getStart())) return 1;
        return 0;
    };
    private final String name;
    private final LocalDate startDate;
    private final TimeInterval timeInterval;

    /**
     * Creates a {@code Event}
     * @param name      name of event
     * @param startDate date event takes place
     * @param startTime start time of event
     * @param endTime   end time of event
     * @throws java.time.DateTimeException  if the end time is not after the start time
     */
    public Event(String name, LocalDate startDate, LocalTime startTime, LocalTime endTime) {
        this.name = name;
        this.startDate = startDate;
        this.timeInterval = new TimeInterval(startTime, endTime);
    }

    protected String getName() {return name;}
    protected LocalDate getStartDate() {return startDate;}
    protected TimeInterval getTimeInterval() {return timeInterval;}

    /**
     * From implementation of Comparable.
     * Compares this {@code Event} with another {@code Event}.
     * @see Comparable
     * @param otherEvent the other {@code Event} being compared to this one.
     * @return  -1 if before and 1 if after
     */
    public int compareTo(Event otherEvent) {
        if (timeIsBefore(otherEvent)){return -1;}
        else if (!timeIsBefore(otherEvent)) {return 1;}
        return 0;   // cant really happen, but is needed anyway
    }

    /**
     * Helper function used in {@code compareTo} to see if the start time is before
     * the other {@code Event}'s start time
     * @param otherEvent other event being compared
     * @return  true if before, false if after
     */
    private boolean timeIsBefore(Event otherEvent) {
        return timeInterval.getStart().isBefore(otherEvent.getTimeInterval().getStart());
    }

    /**
     * Outputs {@code Event} as a String using the format {@code M/d/yy}
     * pattern for the date.
     * @return  string of event
     */
    public String inFormatMonthDayYear() {
        DateTimeFormatter monthDayYear = DateTimeFormatter.ofPattern("M/d/yy");
        return String.format("%s\n%s %s\n", getName(), monthDayYear.format(startDate), getTimeInterval());
    }

    /**
     * Outputs {@code Event} as a String, such as Doctor's Appointment: 10:15-11:30
     * @return  String of {@code OneTimeEvent}
     */
    @Override
    public String toString() {
        return String.format("%s: %s\n", getName(), getTimeInterval());
    }
}
