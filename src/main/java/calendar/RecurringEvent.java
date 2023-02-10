package calendar;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * An {@code Event} that is recurring.
 * A {@code RecurringEvent} takes place on multiple days of the week and repeats weekly
 * @see calendar.Event
 * @author Jonathan Stewart Thomas
 * @version 1.0.0.230209
 */
public class RecurringEvent extends Event implements Comparable<RecurringEvent>{
    private final ArrayList<LocalDate> dates;
    private final String days;

    /**
     * Creates a {@code RecurringEvent}.
     * A recurring event has a name, days of the week it recurs on,
     * a start date, end date, and start  time, and end time.
     * @param name                  name of the event
     * @param days                  days of the week the event takes place on
     * @param startDate             date the event starts recurring
     * @param endDate               date the event stops recurring
     * @param startTime             time the event starts
     * @param endTime               time the event ends
     * @throws DateTimeException    if the start date is after the end date
     */
    public RecurringEvent(String name, String days, LocalDate startDate,
                          LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        super(name, startTime, endTime);
        if (startDate.isAfter(endDate)) throw new DateTimeException("Start date must be before end date");

        dates = new ArrayList<>();
        this.days = days;
        int[] recurringDays = convertDaysToDayOfWeekValue(days);
        LocalDate[] recurringDates = new LocalDate[recurringDays.length];
        recurringDates[0] = startDate;
        LocalDate date = startDate;
        int k = 1;
        for (int i = 1; i < 7; i++) {
            date = date.plusDays(1);
            for (int day : recurringDays) {
                if (date.getDayOfWeek().getValue() == day) {
                    recurringDates[k] = date;
                    k++;
                    break;
                }
            }
        }

        for (LocalDate d : recurringDates) {
            while (d.isBefore(endDate) || d.isEqual(endDate)) {
                dates.add(d);
                d = d.plusWeeks(1);
            }
        }
    }

    /**
     * Converts a String of day abbreviations into integers referring to the day of the week.
     * @param days  String of day abbreviations
     * @return  integer array of days of the week values
     */
    private int[] convertDaysToDayOfWeekValue(String days) {
        // Converting the String days into integers for the day of the week
        char[] daysToCharArr = days.toCharArray();
        int[] recurringDays = new int[daysToCharArr.length];
        for (int i = 0; i< daysToCharArr.length; i++) {
            if (daysToCharArr[i] == 'S') recurringDays[i] = 7;
            else if (daysToCharArr[i] == 'M') recurringDays[i] = 1;
            else if (daysToCharArr[i] == 'T') recurringDays[i] = 2;
            else if (daysToCharArr[i] == 'W') recurringDays[i] = 3;
            else if (daysToCharArr[i] == 'R') recurringDays[i] = 4;
            else if (daysToCharArr[i] == 'F') recurringDays[i] = 5;
            else recurringDays[i] = 6;
        }
        return recurringDays;
    }

    /**
     * Gets the dates the {@code RecurringEvent} takes place on.
     * @return  the dates.
     */
    public ArrayList<LocalDate> getDates() {return dates;}

    /**
     * Gets the date the {@code RecurringEvent} starts recurring
     * @return  the starting date
     */
    public LocalDate getStartDate() {return dates.get(0);}

    /**
     * Gets the date the {@code RecurringEvent} stops recurring
     * @return  the ending date
     */
    public LocalDate getEndDate() {return dates.get(dates.size() - 1);}

    /**
     * Outputs {@code RecurringEvent} as a String using the {@code M/d/yy} pattern for the
     * start and end dates.
     * @return  String of {@code RecurringEvent}
     */
    public String inFormatMonthDayYear() {
        DateTimeFormatter monthDayYear = DateTimeFormatter.ofPattern("M/d/yy");
        return String.format("%s\n%s %s %s %s\n", name,
                days, monthDayYear.format(getStartDate()), monthDayYear.format(getEndDate()), timeInterval);
    }

    /**
     * From implementation of {@code Comparable}.
     * Compares this {@code RecurringEvent} with another {@code RecurringEvent}.
     * If this {@code RecurringEvent} is before the other {@code RecurringEvent},
     * it takes place before the other one. If {@code RecurringEvent} is after the
     * other {@code RecurringEvent}, it takes place after the other one.
     * @see Comparable
     * @param otherEvent the other {@code RecurringEvent} being compared
     * @return {@code -1} if before and {@code 1} if after
     */
    @Override
    public int compareTo(RecurringEvent otherEvent) {
        if (getStartDate().isBefore(otherEvent.getStartDate()) || startTimeIsBefore(otherEvent)) return -1;
        else if (getStartDate().isAfter(otherEvent.getStartDate()) || startTimeIsAfter(otherEvent)) return 1;
        return 0;    // can't really happen, but is needed anyway
    }

    /**
     * A helper function used in {@code compareTo} to check if the start time of this
     * is after {@code otherEvent}.
     * @param otherEvent    the event being compared to.
     * @return              true if this {@code RecurringEvent} starts after the other event.
     */
    private boolean startTimeIsAfter(RecurringEvent otherEvent) {
        return timeInterval.getStart().isAfter(otherEvent.timeInterval.getStart());
    }

    /**
     * A helper function used in {@code compareTo} to check if the start time of this
     * is before {@code otherEvent}.
     * @param otherEvent    the event being compared to.
     * @return              true if this {@code RecurringEvent} starts before the other event.
     */
    private boolean startTimeIsBefore(RecurringEvent otherEvent) {
        return timeInterval.getStart().isBefore(otherEvent.timeInterval.getStart());
    }

    /**
     * Outputs {@code RecurringEvent} as a String using the date format {@code M/d/yy}.
     * @return  String of {@code RecurringEvent}.
     */
    @Override
    public String toString() {
        DateTimeFormatter monthDayYear = DateTimeFormatter.ofPattern("M/d/yy");
        return String.format("%s: %s every %s from %s to %s\n", name, timeInterval, days,
                monthDayYear.format(getStartDate()), monthDayYear.format(getEndDate()));
    }
}
