package calendar;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

/**
 * Personal implementation of a Calendar similar to one that might be seen
 * on a phone. {@code MyCalendar} can add, remove, and display events.
 * {@code MyCalendar} can be outputted as a String showing a day view a month view,
 * or a combination of both.
 * @author Jonathan Stewart Thomas
 * @version 1.0.1.230215
 */
public class MyCalendar {
    private final HashMap<LocalDate, TreeSet<Event>> events;
    private final ArrayList<RecurringEvent> recurringEventsList;
    private final ArrayList<Event> oneTimeEventsList;
    private final LocalDate today;
    private LocalDate firstDay;
    private LocalDate selectedDay;
    private int lastDay;

    /**
     * Creates a new {@code MyCalendar}.
     * Gets today's date and uses it to get the first day and last day of today's
     * month.
     */
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
     * Gets an {@code ArrayList} of {@code OneTimeEvent}.
     * @return  the {@code ArrayList}
     */
    public ArrayList<Event> getOneTimeEventsList() {return oneTimeEventsList;}

    /**
     * Gets an {@code ArrayList} of {@code RecurringEvent}.
     * @return  the {@code ArrayList}
     */
    public ArrayList<RecurringEvent> getRecurringEventsList() {return  recurringEventsList;}

    /**
     * Adds a {@code OneTimeEvent} to the event list. If the event conflicts with an existing event
     * it is not added to the list.
     * @param newEvent  the event being added
     * @return          true if the event is successfully added and false if it conflicts with an event
     */
    public boolean add(Event newEvent) {
        // grab the eventList for this date
        TreeSet<Event> eventList = events.get(newEvent.getStartDate());

        // if there is an event list for this date
        // go through each event to make sure the event TimeInterval isn't conflicting
        if (eventList != null) {
            for (Event event : eventList) {
                // the eventTimeInterval is conflicting, don't add the event
                if (newEvent.getTimeInterval().isConflicting(event.getTimeInterval())) {
                    return false;   // the event is conflicting, don't add it
                }
            }
        }
        // there isn't an eventList for this date, create a new one
        else {
            eventList = new TreeSet<>();
        }

        eventList.add(newEvent);                            // add event to eventList for this date
        oneTimeEventsList.add(newEvent);                    // add event to oneTimeEvents list
        oneTimeEventsList.sort(Event.DATE_TIME_ORDER);      // sort by start date and time
        events.put(newEvent.getStartDate(), eventList);     // add event to events list
        return true;
    }

    /**
     * Adds a {@code RecurringEvent} to the calendar.
     * @param newEvent  the event being added
     */
    public void add(RecurringEvent newEvent) {
        // for every date the event recurs on, add it to the calendar
        for (LocalDate date : newEvent.getDates()) {
            TreeSet<Event> eventList = events.get(date);
            if (eventList == null) {
                eventList = new TreeSet<>();
            }
            eventList.add(newEvent);
            events.put(date, eventList);
        }
        recurringEventsList.add(newEvent);
        recurringEventsList.sort(Event.DATE_TIME_ORDER);
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
     * Outputs a String of the event list.
     * @return  String of the events list
     */
    public String displayEventsList() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("One Time Events:\n");
        LocalDate date = oneTimeEventsList.get(0).getStartDate();
        DateTimeFormatter dayMonthDay = DateTimeFormatter.ofPattern("EEEE, MMMM d");
        stringBuilder.append(dayMonthDay.format(date)).append("\n");
        for (Event event : oneTimeEventsList) {
            if (!date.equals(event.getStartDate())) {
                date = event.getStartDate();
                stringBuilder.append("\n").append(dayMonthDay.format(date)).append("\n");
            }
            stringBuilder.append(event);
        }
        stringBuilder.append("\n");
        stringBuilder.append(displayRecurringEventList());
        stringBuilder.append("\n");
        return stringBuilder.toString();
    }

    public String displayRecurringEventList() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Recurring Events:\n");
        for (RecurringEvent e : recurringEventsList) {
            stringBuilder.append(e);
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
        TreeSet<Event> eventList = events.get(date);
        oneTimeEventsList.removeIf(event -> event.getName().equals(name));
        return eventList.removeIf(event -> event.getName().equals(name));
    }

    /**
     * Deletes every event on {@code MyCalendar}.
     */
    public void deleteAllEvents() {
        events.clear();
        oneTimeEventsList.clear();
        recurringEventsList.clear();
    }

    /**
     * Deletes all events on a specific date.
     * @param date  the date the events are being deleted on
     */
    public void deleteAllEventsOn(LocalDate date) {
        TreeSet<Event> eventList = events.remove(date);
        if (eventList != null) {
            eventList.clear();
            oneTimeEventsList.removeIf(event -> event.getStartDate().equals(date));
        }
    }

    /**
     * Deletes a {@code RecurringEvent} with the name provided.
     * @param name  name of the {@code RecurringEvent} being removed.
     * @return      True if the {@code RecurringEvent} event got removed
     */
    public boolean deleteRecurringEvent(String name) {
        for (RecurringEvent recurringEvent: recurringEventsList) {
            if (recurringEvent.getName().equals(name)) {
                for (LocalDate date : recurringEvent.getDates()) {
                    TreeSet<Event> eventList = events.remove(date);
                    eventList.removeIf(event -> event.getName().equals(name));
                }
                return recurringEventsList.removeIf(event -> event.getName().equals(name));
            }
        }
        return false;
    }

    /**
     * Outputs a month view of {@code MyCalendar} as a String with the month and year at the top.
     * @return  the String of the month view.
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
     * Outputs today's events as a String
     * @return  a String of today's events
     */
    public String displayTodaysEvents() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Today's Events:\n");
        if  (events.get(today) != null) {
            if (events.get(today).size() > 0) {
                for (Event event : events.get(today)) {
                    stringBuilder.append(event);
                }
            }
            else stringBuilder.append("No Events Today\n");
        }
        else stringBuilder.append("No Events Today\n");
        return stringBuilder.toString();
    }

    /**
     * Outputs a String of events on the selected day
     * @return  String of the selected day's events
     */
    public String displaySelectedDay() {
        StringBuilder stringBuilder = new StringBuilder();
        DateTimeFormatter dayMonthDay = DateTimeFormatter.ofPattern("EEEE, MMMM d");
        stringBuilder.append(dayMonthDay.format(selectedDay)).append("\n");
        if  (events.get(selectedDay) != null) {
            if (events.get(selectedDay).size() > 0) {
                for (Event event : events.get(selectedDay)) {
                    stringBuilder.append(event);
                }
            }
            else stringBuilder.append("No Events Today\n");
        }
        else stringBuilder.append("No Events Today\n");
        stringBuilder.append("\n");
        return stringBuilder.toString();
    }

    /**
     * Outputs {@code MyCalendar} as a String with the month view and today's events.
     * @return  String of {@code MyCalendar}
     */
    @Override
    public String toString() {
        return displayMonth() + displayTodaysEvents();
    }

}
