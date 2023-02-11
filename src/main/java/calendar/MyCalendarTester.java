package calendar;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

/**
 * The front-end of {@code MyCalendar}. {@code MyCalendarTester} implements user
 * interaction through the command line to interact with a Calendar.
 * <p>
 * The user can view the calendar events on a specific date, or view entire months. The user can cycle through the days
 * and months or jump to a specific date. Today's date is shown with [] brackets and dates with an event have {} brackets.
 * Events can be added and deleted on {@code MyCalendar}.
 * @author Jonathan Stewart Thomas
 * @version 1.0.1.230211
 */
public class MyCalendarTester {
    private static final String MAIN_MENU_OPTIONS = """
            Main Menu\s
            Select one of the following options\s
            [V]iew by  [C]reate [G]o to [E]vent list [D]elete  [Q]uit\s
            """;
    private static final String VIEW_BY_OPTIONS = """
            View by...\s
            [D]ay or [M]onth\s
            """;
    private static final String PREVIOUS_AND_NEXT_OPTIONS = """
            Go to...
            [P]revious [N]ext [M]ain Menu\s
            """;
    private static final String DELETE_OPTIONS = """
            Delete...
            [S]elected [A]ll [E]vents on... [R]ecurring
            """;
    private static MyCalendar myCalendar;
    private static Scanner stdio;

    /**
     * The main method of this program.
     * It starts by reading the events.txt containing recurring and one time events,
     * and adding them to the calendar. It then runs the main menu. Once the user selects
     * [Q]uit, the events get saved to output.txt.
     * @param args  arguments for the command line
     */
    public static void main(String[] args) {
        myCalendar = new MyCalendar();
        stdio = new Scanner(System.in);
        if (readFile()) {
            System.out.println("Loading is done!");
        }
        runMainMenu();
        System.out.println("Goodbye!");
        stdio.close();
        if(saveFile()) {
            System.out.println("Events saved to output.txt");
        }
    }

    /**
     * The main menu for {@code MyCalendarTester}. It first prints out the calendar and
     * today's events. Afterwards it prompts the user to select one of the following options.
     * This repeats until the user enters Q to quit the program.
     */
    public static void runMainMenu() {
        String option;
        System.out.println(myCalendar);
        do {
            System.out.println(MAIN_MENU_OPTIONS);
            option = stdio.nextLine().toUpperCase();
            switch (option) {
                case "V" -> viewBy();
                case "C" -> createNewEvent();
                case "G" -> goTo();
                case "E" -> showEventList();
                case "D" -> delete();
            }
        } while (!option.equals("Q"));

    }

    /**
     * The menu for selecting how the user wants to view the calendar.
     * This repeats until the user either enters D or M.
     */
    public static void viewBy() {
        String option;
        do {
            System.out.println(VIEW_BY_OPTIONS);
            option = stdio.nextLine().toUpperCase();
            switch (option) {
                case "D" -> viewByDay();
                case "M" -> viewByMonth();
            }
        } while (!option.equals("D") && !option.equals("M"));
    }

    /**
     * Displays a day view of the calendar, displaying a specific date as well as the events
     * taking place on this date. The user can choose to go to the previous day, next day,
     * or return to the main menu.
     */
    public static void viewByDay() {
        String option;
        do {
            System.out.print(myCalendar.displaySelectedDay());
            System.out.println(PREVIOUS_AND_NEXT_OPTIONS);
            option = stdio.nextLine().toUpperCase();
            switch (option) {
                case "P" -> myCalendar.previousDay();
                case "N" -> myCalendar.nextDay();
            }
        } while (!option.equals("M"));
    }

    /**
     * Displays a month view of the calendar. This displays the month and year as well
     * as all the days in the month. It highlights today's date with [] brackets and
     * dates with an event by using {} brackets. The user can choose to go to the
     * previous month, next month, or return to the main menu.
     */
    public static void viewByMonth() {
        String option;
        do {
            System.out.println(myCalendar.displayMonth());
            System.out.println(PREVIOUS_AND_NEXT_OPTIONS);
            option = stdio.nextLine().toUpperCase();
            switch (option) {
                case "P" -> myCalendar.prevMonth();
                case "N" -> myCalendar.nextMonth();
            }
        } while (!option.equals("M"));
    }

    /**
     * The user can create a new {@code OneTimeEvent}. The user enters
     * the name of the event and then date it is going to take place on.
     * Once the date is entered, all events already happening on that
     * date are listed to help the user create an event that isn't
     * going to conflict with an existing one.
     */
    public static void createNewEvent() {
        String name;
        LocalDate date;
        LocalTime startTime, endTime;
        System.out.print("Name of event: ");
        name = stdio.nextLine();
        DateTimeFormatter monthDayYear = DateTimeFormatter.ofPattern("M/d/yyyy");
        System.out.print("Date of event (mm/dd/yyyy): ");
        date = LocalDate.parse(stdio.nextLine(), monthDayYear);
        myCalendar.goTo(date);
        System.out.println("Events on " + myCalendar.displaySelectedDay());
        DateTimeFormatter hourMinute = DateTimeFormatter.ofPattern("H:m");
        System.out.print("Start time (Hour:minute): ");
        startTime = LocalTime.parse(stdio.nextLine(), hourMinute);
        System.out.print("End time (Hour:minute): ");
        endTime = LocalTime.parse(stdio.nextLine(), hourMinute);
        OneTimeEvent event = new OneTimeEvent(name, date, startTime, endTime);
        if (myCalendar.add(event)) {
            System.out.println("Event added\n");
        }
        else System.out.println("Failed to add event: Event conflicted with another.\n");
    }

    /**
     * Allows the user to go to a specific date on the calendar. The calendar will then
     * display all the events on that date.
     */
    public static void goTo() {
        LocalDate date;
        DateTimeFormatter monthDayYear = DateTimeFormatter.ofPattern("M/d/yyyy");
        System.out.print("Date of event (mm/dd/yyyy): ");
        date = LocalDate.parse(stdio.nextLine(), monthDayYear);
        myCalendar.goTo(date);
        viewByDay();
    }

    /**
     * Displays all the events on the calendar. It will first show one time events
     * sorted by start date and time, then recurring events sorted by start date and time.
     */
    public static void showEventList() {
        System.out.print(myCalendar.displayEventsList());
    }

    /**
     * The user can delete events from the calendar.
     * They can delete a specific event by entering the event's date and name,
     * all events, all events on a certain date, and all recurring events.
     */
    public static void delete() {
        String option;
        do {
            System.out.println(DELETE_OPTIONS);
            option = stdio.nextLine().toUpperCase();
            switch (option) {
                case "S" -> deleteSelected();
                case "A" -> myCalendar.deleteAllEvents();
                case "E" -> deleteEventsOn();
                case "R" -> myCalendar.deleteAllRecurringEvents();
            }
        } while(!option.equals("S") && !option.equals("A") && !option.equals("E") && !option.equals("R"));
    }

    /**
     * Deletes a specific event.
     * The user is prompted to enter the date of the event they want to delete.
     * The user is shown all events happening on that date.
     * They are then asked to enter the name o the event they want to delete.
     */
    public static void deleteSelected() {
        System.out.print("Enter Event Date (mm/dd/yyyy): ");
        DateTimeFormatter monthDayYear = DateTimeFormatter.ofPattern("M/d/yyyy");
        LocalDate date = LocalDate.parse(stdio.nextLine(), monthDayYear);
        myCalendar.goTo(date);
        System.out.print(myCalendar.displaySelectedDay());
        System.out.print("Enter the name of the event to delete: ");
        String name = stdio.nextLine();
        if (myCalendar.deleteEvent(name, date)) System.out.println("Event Deleted\n");
        else System.out.println("Deletion Failed!\n");
    }

    /**
     * Deletes all events on a date.
     * The user is prompted to enter the date they want to delete all the events on.
     */
    public static void deleteEventsOn() {
        System.out.print("Enter Event Date (mm/dd/yyyy): ");
        DateTimeFormatter monthDayYear = DateTimeFormatter.ofPattern("M/d/yyyy");
        LocalDate date = LocalDate.parse(stdio.nextLine(), monthDayYear);
        myCalendar.deleteAllEventsOn(date);
    }

    /**
     * Reads events.txt to load saved events onto the calendar.
     * @return  true if the file was found and read successfully and false if it failed
     */
    public static boolean readFile() {
        try {
            Scanner scannerTextFile = new Scanner(new File("events.txt"));
            while (scannerTextFile.hasNextLine()) {
                String name = scannerTextFile.nextLine();

                String information = scannerTextFile.nextLine();

                information = information.toUpperCase();

                DateTimeFormatter monthDayYear = DateTimeFormatter.ofPattern("M/d/yy");
                DateTimeFormatter hourMinute = DateTimeFormatter.ofPattern("H:m");

                String[] splitInfo = information.split(" ");
                if (splitInfo[0].contains("S") || splitInfo[0].contains("M") || splitInfo[0].contains("T") || splitInfo[0].contains("W")
                        || splitInfo[0].contains("R") || splitInfo[0].contains("F") || splitInfo[0].contains("A") ) {

                    LocalTime startTime = LocalTime.parse(splitInfo[1],hourMinute);
                    LocalTime endTime = LocalTime.parse(splitInfo[2], hourMinute);

                    LocalDate startDate = LocalDate.parse(splitInfo[3], monthDayYear);
                    LocalDate endDate = LocalDate.parse(splitInfo[4], monthDayYear);
                    RecurringEvent event = new RecurringEvent(name, splitInfo[0], startDate, endDate, startTime, endTime);
                    myCalendar.add(event);
                }
                else {
                    LocalDate date = LocalDate.parse(splitInfo[0], monthDayYear);
                    LocalTime startTime = LocalTime.parse(splitInfo[1], hourMinute);
                    LocalTime endTime = LocalTime.parse(splitInfo[2], hourMinute);
                    OneTimeEvent event = new OneTimeEvent(name, date, startTime, endTime);
                    myCalendar.add(event);
                }

            }
            scannerTextFile.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
        }
        return true;
    }

    /**
     * Saves an output file called output.txt
     * @return  true if it was successful and false if it wasn't
     */
    public static boolean saveFile() {
        try (FileWriter fileWriter = new FileWriter("output.txt")) {
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            for (OneTimeEvent e: myCalendar.getOneTimeEventsList()) {
                bufferedWriter.write(e.inFormatMonthDayYear());
            }
            for (RecurringEvent e: myCalendar.getRecurringEventsList()) {
                bufferedWriter.write(e.inFormatMonthDayYear());
            }
            bufferedWriter.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }
}
