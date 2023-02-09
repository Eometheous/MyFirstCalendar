package calendar;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class MyCalendarTester {
    private static final String FILEPATH = "src/main/java/calendar/events.txt";
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

    private static final Scanner stdio = new Scanner(System.in);

    public static void main(String[] args) {
        myCalendar = new MyCalendar();
        if (readFile()) {
            System.out.println("Loading is done!");
        }
        mainMenu();
        System.out.println("Goodbye!");
        if(saveFile()) {
            System.out.println("Events saved to output.txt");
        }
    }
    public static void mainMenu() {
        String option;
        System.out.println(myCalendar);
        do {
            System.out.println(MAIN_MENU_OPTIONS);
            option = stdio.nextLine().toUpperCase();
            switch (option) {
                case "V" -> viewByMenu();
                case "C" -> createMenu();
                case "G" -> goTo();
                case "E" -> eventList();
                case "D" -> delete();
            }
        } while (!option.equals("Q"));

    }

    public static void viewByMenu() {
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
    public static void viewByDay() {
        String option;
        do {
            System.out.println(myCalendar.displaySelectedDay());
            System.out.println(PREVIOUS_AND_NEXT_OPTIONS);
            option = stdio.nextLine().toUpperCase();
            switch (option) {
                case "P" -> myCalendar.previousDay();
                case "N" -> myCalendar.nextDay();
            }
        } while (!option.equals("M"));
    }
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

    public static void createMenu() {
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
            System.out.println("Event added");
        }
        else System.out.println("Failed to add event: Event conflicted with another.");
    }

    public static void goTo() {
        LocalDate date;
        DateTimeFormatter monthDayYear = DateTimeFormatter.ofPattern("M/d/yyyy");
        System.out.print("Date of event (mm/dd/yyyy): ");
        date = LocalDate.parse(stdio.nextLine(), monthDayYear);
        myCalendar.goTo(date);
//        System.out.println(myCalendar.displaySelectedDay());
        viewByDay();
    }
    public static void eventList() {
        System.out.println(myCalendar.displayEventsList());
    }
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
    public static void deleteSelected() {
        System.out.print("Enter Event Date: ");
        DateTimeFormatter monthDayYear = DateTimeFormatter.ofPattern("M/d/yyyy");
        LocalDate date = LocalDate.parse(stdio.nextLine(), monthDayYear);
        myCalendar.goTo(date);
        System.out.println(myCalendar.displaySelectedDay());
        System.out.print("Enter the name of the event to delete: ");
        String name = stdio.nextLine();
        if (myCalendar.deleteEvent(name, date)) System.out.println("Event Deleted");
        else System.out.println("Deletion Failed!");
    }

    public static void deleteEventsOn() {
        System.out.print("Enter Event Date: ");
        DateTimeFormatter monthDayYear = DateTimeFormatter.ofPattern("M/d/yyyy");
        LocalDate date = LocalDate.parse(stdio.nextLine(), monthDayYear);
        myCalendar.deleteAllEventsOn(date);
    }

    public static boolean readFile() {
        try {
            Scanner scannerTextFile = new Scanner(new File(FILEPATH));
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
            throw new RuntimeException(e);
        }
        return true;
    }

    public static boolean saveFile() {
        return true;
    }
}
