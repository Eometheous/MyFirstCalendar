package calendar;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

public class MyCalendar {
    private final HashMap<LocalDate, ArrayList<Event>> events;
    private final LocalDate today;
    private LocalDate firstDay;
    private int lastDay;
    public MyCalendar() {
        events = new HashMap<>();
        today = LocalDate.now();
        firstDay = LocalDate.of(today.getYear(), today.getMonth(), 1);
        lastDay = firstDay.lengthOfMonth();
    }

    public boolean addEvent(String name, LocalDate date, LocalTime sT, LocalTime eT) {
        TimeInterval eventTimeInterval = new TimeInterval(sT, eT);
        ArrayList<Event> eventList;

        // grab the eventList for this date
        eventList = events.get(date);

        // if there is an event list for this date
        // go through each event to make sure the eventTimeInterval isn't conflicting
        if (eventList != null) {
            for (Event i : eventList) {
                // the eventTimeInterval is conflicting, don't add the event
                if (eventTimeInterval.isConflicting(i.getTimeInterval())) {
                    return false;
                }
            }
        }
        // there isn't an eventList for this date, create a new one
        else {
            eventList = new ArrayList<>();
        }

        // add the new Event to the eventList
        eventList.add(new Event(name, date, eventTimeInterval));
        events.put(date, eventList);
        return true;
    }

    public boolean addRecurringEvent(String name, String days, LocalDate firstDay,
                                     LocalDate lastDay, LocalTime startTime, LocalTime endTime) {

        char[] daysArr = days.toCharArray();
        int[] recurringDays = new int[daysArr.length];

        int dayInt = firstDay.getDayOfWeek().getValue();

//        TimeInterval eventTimeInterval = new TimeInterval(startTime, endTime);
//        ArrayList<Event> eventList;
//
//        // grab the eventList for this date
//        eventList = events.get(firstDay);
//
//        // if there is an event list for this date
//        // go through each event to make sure the eventTimeInterval isn't conflicting
//        if (eventList != null) {
//            for (Event i : eventList) {
//                // the eventTimeInterval is conflicting, don't add the event
//                if (eventTimeInterval.isConflicting(i.getTimeInterval())) {
//                    return false;
//                }
//            }
//        }
//        // there isn't an eventList for this date, create a new one
//        else {
//            eventList = new ArrayList<>();
//        }
//        // TODO not currently recurring
//        // add the new Event to the eventList
//        eventList.add(new Event(name, firstDay, eventTimeInterval));
//        events.put(firstDay, eventList);
        return true;
    }

    public void prevMonth() {
        firstDay = firstDay.minusMonths(1);
        lastDay = firstDay.lengthOfMonth();
    }

    public void nextMonth() {
        firstDay = firstDay.plusMonths(1);
        lastDay = firstDay.lengthOfMonth();
    }

    @Override
    public String toString() {
        DateTimeFormatter monthYear = DateTimeFormatter.ofPattern("MMM yyyy");
        DateTimeFormatter day = DateTimeFormatter.ofPattern("d");

        StringBuilder stringBuilder = new StringBuilder(monthYear.format(firstDay));
        stringBuilder.append("\nSun Mon Tue Wed Thu Fri Sat\n");

        String spaces = "    ";

        int dayInt = firstDay.getDayOfWeek().getValue();

        if (dayInt != 7) {
            // place first day correctly on the calendar
            stringBuilder.append(spaces.repeat(Math.max(0, dayInt % 7)));
        }
        LocalDate nextDay = firstDay;

        for (int i = 0; i < lastDay; i++) {
            // add a new line for the next week
            if (nextDay.getDayOfWeek().getValue() % 7 == 0) {
                stringBuilder.append("\n");
            }

            // add brackets to this day if it is today's date
            if (nextDay.equals(today)) {
                stringBuilder.append(String.format("[%-2s]", day.format(nextDay)));
            }
            else {
                stringBuilder.append(String.format(" %-2s", day.format(nextDay)));
                stringBuilder.append(" ");
            }

            // go to the next day
            nextDay = nextDay.plusDays(1);
        }
        stringBuilder.append("\n\n");
        stringBuilder.append("Today's Events:\n");
        stringBuilder.append(events.get(today));
        return stringBuilder.toString();
    }

}
