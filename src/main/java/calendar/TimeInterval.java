package calendar;

import java.time.DateTimeException;
import java.time.LocalTime;

/**
 * A time interval containing a start time and end time.
 * For example, {@code 10:30-11:30}
 * @author Jonathan Stewart Thomas
 * @version 1.0.1.230214
 */
public class TimeInterval {
    private final LocalTime startTime;
    private final LocalTime endTime;

    /**
     * Creates a {@code TimeInterval} with a start and end time.
     * @param startTime             start time of the interval
     * @param endTime               end time of the interval
     * @throws DateTimeException    if the end time is not after the start time
     */
    public TimeInterval(LocalTime startTime, LocalTime endTime) {
        if (startTime.isAfter(endTime) || startTime.equals(endTime))
            throw new DateTimeException("endTime must be after start time");
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /**
     * Gets the start time of the {@code TimeInterval}.
     * @return  the start time
     */
    public LocalTime getStart() {return startTime;}

    /**
     * Gets the end time of the {@code TimeInterval}.
     * @return  the end time
     */
    public LocalTime getEnd() {return endTime;}

    /**
     * Finds if a {@code TimeInterval} is between the other.
     * @param timeInterval  the other TimeInterval
     * @return  true if the other is between this one
     */
    private boolean isBetween(TimeInterval timeInterval) {
        return startTime.isAfter(timeInterval.startTime) && endTime.isBefore(timeInterval.endTime);
    }

    /**
     * Finds if a start or end time a {@code TimeInterval} is between {@code this}.
     * @param startTime Interval start time
     * @param endTime   Interval end time
     * @return          true if either the start or end time is between {@code TimeInterval}
     */
    private boolean isBetween(LocalTime startTime, LocalTime endTime) {
        return (this.startTime.isAfter(startTime) && this.startTime.isBefore(endTime))
                || (this.endTime.isAfter(startTime) && this.endTime.isBefore(endTime));
    }

    /**
     * Checks if a {@code TimeInterval} is conflicting with this one.
     * @param timeInterval  the other TimeInterval
     * @return              true if it is conflicting, false if it isn't
     */
    public boolean isConflicting(TimeInterval timeInterval) {
        if (this.startTime.equals(timeInterval.startTime) || this.endTime.equals(timeInterval.endTime)) return true;
        else if (this.isBetween(timeInterval)) return true;
        else if (timeInterval.isBetween(this)) return true;
        else return timeInterval.isBetween(startTime, endTime);
    }

    /**
     * Outputs {@code TimeInterval} as a String.
     * For example, {@code 10:30-11:30}
     * @return  a String of {@code TimeInterval}
     */
    @Override
    public String toString() {
        return startTime + "-" + endTime;
    }
}
