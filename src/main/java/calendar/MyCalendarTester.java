package calendar;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Locale;
import java.util.Scanner;

public class MyCalendarTester {
    private static final String FILEPATH = "src/main/java/calendar/events.txt";
    private static MyCalendar myCalendar;

    public static void main(String[] args) {
//        MyCalendar cal = new MyCalendar();
//        for (int i = 0; i < 12; i++) {
//            System.out.print(cal);
//            cal.nextMonth();
//        }
        myCalendar = new MyCalendar();
        if (readFile()) {
            System.out.println("Loading is done!");
        }
        System.out.println(myCalendar);
    }

    public static boolean readFile() {
        try {
            Scanner scannerTextFile = new Scanner(new File(FILEPATH));
            while (scannerTextFile.hasNextLine()) {
                String name = scannerTextFile.nextLine();

                String information = scannerTextFile.nextLine();

                information = information.toUpperCase();

                String[] splitInfo = information.split(" ");
                if (splitInfo[0].contains("S") || splitInfo[0].contains("M") || splitInfo[0].contains("T") || splitInfo[0].contains("W")
                        || splitInfo[0].contains("R") || splitInfo[0].contains("F") || splitInfo[0].contains("A") ) {
                    String[] splitStartTime = splitInfo[1].split(":");
                    String[] splitEndTime = splitInfo[2].split(":");

                    int hour = Integer.parseInt(splitStartTime[0]);
                    int minute = Integer.parseInt(splitStartTime[1]);
                    LocalTime startTime = LocalTime.of(hour, minute);

                    hour = Integer.parseInt(splitEndTime[0]);
                    minute = Integer.parseInt(splitEndTime[1]);
                    LocalTime endTime = LocalTime.of(hour, minute);

                    String[] splitStartDate = splitInfo[3].split("/");
                    int startMonth, startDay, startYear, endMonth, endDay, endYear;
                    startMonth = Integer.parseInt(splitStartDate[0]);
                    startDay = Integer.parseInt(splitStartDate[1]);
                    startYear = Integer.parseInt(splitStartDate[2]);

                    String[] splitEndDate = splitInfo[4].split("/");
                    endMonth = Integer.parseInt(splitEndDate[0]);
                    endDay = Integer.parseInt(splitEndDate[1]);
                    endYear = Integer.parseInt(splitEndDate[2]);

                    LocalDate startDate = LocalDate.of(startYear, startMonth, startDay);
                    LocalDate endDate = LocalDate.of(endYear, endMonth, endDay);

                    myCalendar.addRecurringEvent(name, splitInfo[0], startDate, endDate, startTime, endTime);
                }
                else {
                    LocalDate date;
                    int hour, minute, year, month, day;
                    String[] splitDate = splitInfo[0].split("/");
                    year = Integer.parseInt(splitDate[2]);
                    year += 2000;
                    month = Integer.parseInt(splitDate[0]);
                    day = Integer.parseInt((splitDate[1]));
                    date = LocalDate.of(year, month, day);

                    String[] splitStartTime = splitInfo[1].split(":");
                    hour = Integer.parseInt(splitStartTime[0]);
                    minute = Integer.parseInt(splitStartTime[1]);
                    LocalTime startTime = LocalTime.of(hour, minute);

                    String[] splitEndTime = splitInfo[2].split(":");
                    hour = Integer.parseInt(splitEndTime[0]);
                    minute = Integer.parseInt(splitEndTime[1]);
                    LocalTime endTime = LocalTime.of(hour, minute);

                    myCalendar.addEvent(name, date, startTime, endTime);
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
