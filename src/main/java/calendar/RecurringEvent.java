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
 * @version 1.0.1.230214
 */
public class RecurringEvent extends Event{
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
        super(name, startDate, startTime, endTime);
        if (startDate.isAfter(endDate) || startDate.equals(endDate))
            throw new DateTimeException("end date must be after start date");

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
    @Override
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
    @Override
    public String inFormatMonthDayYear() {
        DateTimeFormatter monthDayYear = DateTimeFormatter.ofPattern("M/d/yy");
        return String.format("%s\n%s %s %s %s\n", getName(),
                days, monthDayYear.format(getStartDate()), monthDayYear.format(getEndDate()), getTimeInterval());
    }

    /**
     * Outputs {@code RecurringEvent} as a String using the date format {@code M/d/yy}.
     * @return  String of {@code RecurringEvent}.
     */
    @Override
    public String toString() {
        DateTimeFormatter monthDayYear = DateTimeFormatter.ofPattern("M/d/yy");
        return String.format("%s: %s every %s from %s to %s\n", getName(), getTimeInterval(), days,
                monthDayYear.format(getStartDate()), monthDayYear.format(getEndDate()));
    }
}
