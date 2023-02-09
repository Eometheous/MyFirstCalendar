package calendar;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class OneTimeEvent extends Event implements Comparable<OneTimeEvent>{
    private final LocalDate date;
    public OneTimeEvent(String n, LocalDate d, LocalTime sT, LocalTime eT) {
        super(n, sT, eT);
        date = d;
    }
    public LocalDate getDate() {return date;}

    public String save() {
        DateTimeFormatter monthDayYear = DateTimeFormatter.ofPattern("M/d/yy");
        return String.format("%s\n%s %s\n",name, monthDayYear.format(date), timeInterval);
    }

    @Override
    public int compareTo(OneTimeEvent o) {
        if (date.isBefore(o.getDate()) && timeInterval.getStart().isBefore(o.getTimeInterval().startTime)) return -1;
        else if (date.isAfter(o.getDate()) && timeInterval.getStart().isBefore(o.getTimeInterval().startTime)) return 1;
        return 0;
    }

    @Override
    public String toString() {
        DateTimeFormatter dayMonthDay = DateTimeFormatter.ofPattern("EEEE, MMMM d");
        return String.format("%s\n\t%s on %s\n", name, timeInterval, dayMonthDay.format(date));
    }
}
