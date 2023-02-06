package calendar;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class RecurringEvent extends Event {
    private final ArrayList<LocalDate> dates;
//    private final LocalDate startDate, endDate;

    public RecurringEvent(String n, int[] days, LocalDate sD, LocalDate eD, LocalTime sT, LocalTime eT) {
        super(n, sT, eT);
        dates = new ArrayList<>();
//        startDate = sD;
//        endDate = eD;
        LocalDate[] recurringDates = new LocalDate[days.length];
        recurringDates[0] = sD;
        LocalDate date = sD;
        int k = 1;
        for (int i = 1; i < 7; i++) {
            date = date.plusDays(1);
            for (int day : days) {
                if (date.getDayOfWeek().getValue() == day) {
                    recurringDates[k] = date;
                    k++;
                    break;
                }
            }
        }

        for (LocalDate d : recurringDates) {
            while (d.isBefore(eD) || d.isEqual(eD)) {
                dates.add(d);
                d = d.plusWeeks(1);
            }
        }
    }

    public ArrayList<LocalDate> getDates() {return dates;}

    @Override
    public String toString() {
        return String.format("%s %s", timeInterval, name);
//        DateTimeFormatter dayMonthDay = DateTimeFormatter.ofPattern("EEEE, MMMM d");
//        return String.format("%s to %s %s %s", dayMonthDay.format(startDate), dayMonthDay.format(endDate), timeInterval, name);
    }
}
