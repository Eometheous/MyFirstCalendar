package calendar;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * An {@code Event} on a specific date and time.
 * @see calendar.Event
 * @author Jonathan Stewart Thomas
 * @version 1.0.0.230209
 */
public class OneTimeEvent extends Event implements Comparable<OneTimeEvent>{
    private final LocalDate date;

    /**
     * Creates a {@code OneTimeEvent}
     * @param name      name of event
     * @param date      date event takes place
     * @param startTime start time of event
     * @param endTime   end time of event
     * @throws java.time.DateTimeException  if the start time is after the end time
     */
    public OneTimeEvent(String name, LocalDate date, LocalTime startTime, LocalTime endTime) {
        super(name, startTime, endTime);
        this.date = date;
    }

    /**
     * Gets the date the {@code OneTimeEvent} takes place on.
     * @return  the date
     */
    public LocalDate getDate() {return date;}

    /**
     * Outputs {@code OneTimeEvent} as a String using the format {@code M/d/yy}
     * pattern for the date.
     * @return  string of event
     */
    public String inFormatMonthDayYear() {
        DateTimeFormatter monthDayYear = DateTimeFormatter.ofPattern("M/d/yy");
        return String.format("%s\n%s %s\n",name, monthDayYear.format(date), timeInterval);
    }

    /**
     * From implementation of Comparable.
     * Compares this {@code OneTimeEvent} with another {@code OneTimeEvent}.
     * If this {@code OneTimeEvent} is before the other {@code OneTimeEvent},
     * it takes place before the other one. If {@code OneTimeEvent} is after the
     * other {@code OneTimeEvent}, it takes place after the other one.
     * @see Comparable
     * @param otherEvent the other {@code OneTimeEvent} being compared to this one.
     * @return  -1 if before and 1 if after
     */
    @Override
    public int compareTo(OneTimeEvent otherEvent) {
        if (date.isBefore(otherEvent.getDate())) {return -1;}
        else if (date.isAfter(otherEvent.getDate())) {return 1;}
        else if (timeIsBefore(otherEvent )){return -1;}
        else if (!timeIsBefore(otherEvent)) {return 1;}
        return 0;   // cant really happen, but is needed anyway
    }

    /**
     * Helper function used in {@code compareTo} to see if the start time is before
     * the other {@code OneTimeEvent}'s start time
     * @param otherEvent other event being compared
     * @return  true if before, false if after
     */
    private boolean timeIsBefore(OneTimeEvent otherEvent) {
        return timeInterval.getStart().isBefore(otherEvent.timeInterval.getStart());
    }

    /**
     * Outputs {@code OneTimeEvent} as a String, such as Doctor's Appointment: 10:15-11:30
     * @return  String of {@code OneTimeEvent}
     */
    @Override
    public String toString() {
        return String.format("%s: %s\n", name, timeInterval);
    }
}
