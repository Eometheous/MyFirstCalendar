package calendar;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class DateTimeExample {

    public static void main(String[] args) {
        LocalDate cal = LocalDate.now(); // capture today
        Scanner sc = new Scanner(System.in);
        System.out.print("Today: ");

        printCalendar(cal);

        while (sc.hasNextLine()) {
            String input = sc.nextLine();
            if (input.equals("p")) {
                cal = cal.minusMonths(1); // LocalDateTime is immutable
                printCalendar(cal);
            }
            else if (input.equals("n")) {
                cal = cal.plusMonths(1); // LocalDateTime is immutable
                printCalendar(cal);
            }
            else break;
        }
        System.out.println("Bye!");
    }

    public static void printCalendar(LocalDate d) {
        System.out.print(d.getDayOfWeek() + " ");
        System.out.print(d.getDayOfMonth() + " ");
        System.out.print(d.getDayOfYear());

        // To print a calendar in a specified format.
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E, MMM d yyyy");
        System.out.println(" " + formatter.format(d));

        // to figure out the day of the week of the 1st day of the given month
        LocalDate x = LocalDate.of(d.getYear(), d.getMonth(), 1);
        System.out.println(x.getDayOfWeek() + " is the day of " + d.getMonth() + " 1."); // enum value as it is
        System.out.println(x.getDayOfWeek().getValue() + " is an integer value corresponding "
        + " to " + x.getDayOfWeek()); // int value corresponding to the enum value
    }

}
