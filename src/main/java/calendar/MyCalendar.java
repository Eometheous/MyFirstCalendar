package calendar;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

public class MyCalendar {
    private final HashMap<LocalDate, ArrayList<Event>> events;
    private final ArrayList<RecurringEvent> recurringEventsList;
    private final ArrayList<OneTimeEvent> oneTimeEventsList;
    private final LocalDate today;
    private LocalDate firstDay;
    private LocalDate selectedDay;
    private int lastDay;

    public MyCalendar() {
        events = new HashMap<>();
        recurringEventsList = new ArrayList<>();
        oneTimeEventsList = new ArrayList<>();
        today = LocalDate.now();
        selectedDay = today;
        firstDay = LocalDate.of(today.getYear(), today.getMonth(), 1);
        lastDay = firstDay.lengthOfMonth();
    }

    /**
     * Adds a one time event to the event list. If the event conflicts with an existing event
     * it is not added to the list.
     * @param name  the name of the event
     * @param date  the date the event takes place
     * @param sT    the start time of the event
     * @param eT    the end time of the event
     * @return      true if the event is successfully added and false if it conflicts with an event
     */
    public boolean addEvent(String name, LocalDate date, LocalTime sT, LocalTime eT) {
        TimeInterval eventTimeInterval = new TimeInterval(sT, eT);

        // grab the eventList for this date
        ArrayList<Event> eventList = events.get(date);

        // if there is an event list for this date
        // go through each event to make sure the event TimeInterval isn't conflicting
        if (eventList != null) {
            for (Event e : eventList) {
                // the eventTimeInterval is conflicting, don't add the event
                if (eventTimeInterval.isConflicting(e.getTimeInterval())) {
                    return false;   // the event is conflicting, don't add it
                }
            }
        }
        // there isn't an eventList for this date, create a new one
        else {
            eventList = new ArrayList<>();
        }

        // add the new Event to the eventList
        OneTimeEvent event = new OneTimeEvent(name, date, eventTimeInterval);
        eventList.add(event);                   // add event to eventList for this date
        oneTimeEventsList.add(event);               // add event to oneTimeEvents list
        oneTimeEventsList.sort(Event::compareTo);   // sort by start time
        eventList.sort(Event::compareTo);       // sort by start time
        events.put(date, eventList);            // add event to events list
        return true;
    }

    /**
     * Adds a recurring event to the calendar. This is only done through loading events from
     * events.txt and is not currently possible through the user in the create event option
     * @param name      the name of the recurring event
     * @param days      the days the event recurs on
     * @param firstDay  the first day of the event
     * @param lastDay   the last day of the event
     * @param startTime the start time of the event
     * @param endTime   the end time of the event
     */
    public void addRecurringEvent(String name, String days, LocalDate firstDay,
                                     LocalDate lastDay, LocalTime startTime, LocalTime endTime) {
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

        RecurringEvent recurringEvent = new RecurringEvent(name, recurringDays, firstDay, lastDay, startTime, endTime);

        // for every date the event recurs on, add it to the calendar
        for (LocalDate d : recurringEvent.getDates()) {
            ArrayList<Event> eventList = events.get(d);
            if (eventList == null) {
                eventList = new ArrayList<>();
            }
            eventList.add(recurringEvent);
            events.put(d, eventList);
        }
        recurringEventsList.add(recurringEvent);
    }

    /**
     * Goes to the previous month
     */
    public void prevMonth() {
        firstDay = firstDay.minusMonths(1);
        lastDay = firstDay.lengthOfMonth();
    }

    /**
     * Goes to the next month
     */
    public void nextMonth() {
        firstDay = firstDay.plusMonths(1);
        lastDay = firstDay.lengthOfMonth();
    }

    /**
     * Goes to the previous day
     */
    public void previousDay() {
        selectedDay = selectedDay.minusDays(1);
        firstDay = LocalDate.of(today.getYear(), today.getMonth(), 1);
        lastDay = firstDay.lengthOfMonth();
    }

    /**
     * Goes to the next day
     */
    public void nextDay() {
        selectedDay = selectedDay.plusDays(1);
        firstDay = LocalDate.of(today.getYear(), today.getMonth(), 1);
        lastDay = firstDay.lengthOfMonth();
    }

    /**
     * Goes to a specific date on the calendar
     * @param date  the date we are going to
     */
    public void goTo(LocalDate date) {
        selectedDay = date;
        firstDay = LocalDate.of(today.getYear(), today.getMonth(), 1);
        lastDay = firstDay.lengthOfMonth();
    }

    /**
     * Displays all one time and recurring events.
     * @return  a string of the events list
     */
    public String displayEventsList() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("One Time Events:\n");
        for (Event i : oneTimeEventsList) {
            stringBuilder.append(i).append("\n");
        }
        stringBuilder.append("\n");
        stringBuilder.append("Recurring Events:\n");
        for (Event i : recurringEventsList) {
            stringBuilder.append(i).append("\n");
        }
        return stringBuilder.toString();
    }

    /**
     * Deletes an event on the specified date and with the specified name.
     * @param name  the name of the event being deleted
     * @param date  the date the event is on
     * @return      true if the event was successfully deleted and false if it wasn't
     */
    public boolean deleteEvent(String name, LocalDate date) {
        ArrayList<Event> eventList = events.remove(date);
        // there are events on this date
        if (eventList != null) {
            // go through each event and delete the one with the name specified
            for (Event e : eventList) {
                if (e.name.equals(name)) {
                    eventList.remove(e);
                    if (e.getClass().equals(OneTimeEvent.class)) {
                        oneTimeEventsList.remove(e);
                    }
//                    else if (e.getClass().equals(RecurringEvent.class)) {
//                        for (LocalDate d : ((RecurringEvent) e).getDates()) {
//                            d.
//                        }
//                    }
                    if (!eventList.isEmpty()) events.put(date, eventList); // re add the eventList
                    return true;
                }
            }
        }
        // there aren't any events on this date.
        return false;
    }

    /**
     * Deletes every event, both recurring and one time.
     */
    public void deleteAllEvents() {
        events.clear();
        oneTimeEventsList.clear();
        recurringEventsList.clear();
    }

    /**
     * Deletes on events on a specific date.
     * @param date  the date the events are being deleted on
     */
    public void deleteAllEventsOn(LocalDate date) {
        ArrayList<Event> eventList = events.remove(date);
        if (eventList != null) {
            eventList.clear();
        }
        oneTimeEventsList.removeIf(e -> e.getDate().equals(date));
//        recurringEvents.removeIf(e -> e.getDates().contains(date));
    }

    /**
     * Deletes all recurring events
     */
    public void deleteAllRecurringEvents() {
        for (RecurringEvent e: recurringEventsList) {
            for (LocalDate d: e.getDates()) {
                ArrayList<Event> eventList = events.remove(d);
                eventList.remove(e);
                // if there are still events on this date, add it back to the events list
                if (!eventList.isEmpty()) events.put(d, eventList);
            }
        }
        recurringEventsList.clear();
    }

    /**
     * Displays a month view of the calendar with the month and year at the top.
     * @return  the string of the month view.
     */
    public String displayMonth() {
        DateTimeFormatter monthYear = DateTimeFormatter.ofPattern("MMM yyyy");
        DateTimeFormatter day = DateTimeFormatter.ofPattern("d");

        StringBuilder stringBuilder = new StringBuilder(monthYear.format(firstDay));
        stringBuilder.append("\nSun Mon Tue Wed Thu Fri Sat\n");

        String spaces = "    "; // add spaces to make the first day line up with the day of the week

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
            // add curly brackets if this day has an event
            else if (events.containsKey(nextDay)) {
                stringBuilder.append(String.format("{%-2s}", day.format(nextDay)));
            }
            else {
                stringBuilder.append(String.format(" %-2s", day.format(nextDay)));
                stringBuilder.append(" ");
            }

            // go to the next day
            nextDay = nextDay.plusDays(1);
        }
        stringBuilder.append("\n\n");
        return stringBuilder.toString();
    }

    /**
     * Displays events scheduled today
     * @return  a string of today's events
     */
    public String displayTodaysEvents() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Today's Events:\n");
        if  (events.get(today) != null) {
            for (Event i : events.get(today)) {
                stringBuilder.append(i);
                stringBuilder.append("\n");
            }
        }
        else stringBuilder.append("No Events Today\n");
        return stringBuilder.toString();
    }

    /**
     * Displays events on the selected day
     * @return  a string of the selected day's events
     */
    public String displaySelectedDay() {
        StringBuilder stringBuilder = new StringBuilder();
        DateTimeFormatter dayMonthDay = DateTimeFormatter.ofPattern("EEEE, MMMM d");
        stringBuilder.append(dayMonthDay.format(selectedDay)).append("\n");
        if  (events.get(selectedDay) != null) {
            for (Event i : events.get(selectedDay)) {
                stringBuilder.append(i);
                stringBuilder.append("\n");
            }
        }
        else stringBuilder.append("No Events Today\n");
        return stringBuilder.toString();
    }
    @Override
    public String toString() {
        return displayMonth() + displayTodaysEvents();
    }

}
