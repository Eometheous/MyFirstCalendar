package calendar;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class RecurringEvent extends Event implements Comparable<RecurringEvent>{
    private final ArrayList<LocalDate> dates;
    private final String days;
    private final LocalDate startDate, endDate;

    public RecurringEvent(String n, String days, LocalDate sD, LocalDate eD, LocalTime sT, LocalTime eT) {
        super(n, sT, eT);
        dates = new ArrayList<>();
        this.days = days;
        startDate = sD;
        endDate = eD;
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

    public ArrayList<LocalDate> getDates() {return dates;}
    public LocalDate getStartDate() {return startDate;}

    @Override
    public int compareTo(RecurringEvent o) {
        if (startDate.isBefore(o.getStartDate()) && timeInterval.getStart().isBefore(o.getTimeInterval().getStart())) return -1;
        else if (startDate.isAfter(o.getStartDate()) && timeInterval.getStart().isAfter(o.getTimeInterval().getStart())) return 1;
        return 0;
    }

    @Override
    public String toString() {
        DateTimeFormatter monthDayYear = DateTimeFormatter.ofPattern("M/d/yy");
        return String.format("%s\n\tEvery %s from %s from %s to %s", name, days, timeInterval, monthDayYear.format(startDate), monthDayYear.format(endDate));
    }
}
