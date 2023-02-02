package calendar;

import java.time.LocalTime;

public class TimeInterval {
    LocalTime startTime;
    LocalTime endTime;

    public TimeInterval() {
        startTime = LocalTime.MIN;
        endTime = LocalTime.MAX;
    }

    public TimeInterval(LocalTime sT, LocalTime eT) {
        startTime = sT;
        endTime = eT;
    }

    public LocalTime getStart() {return startTime;}
    public LocalTime getEnd() {return endTime;}
    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }
    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    private boolean isBetween(TimeInterval timeInterval) {
        return startTime.isAfter(timeInterval.startTime) && endTime.isBefore(timeInterval.endTime);
    }
    private boolean isBetween(LocalTime startTime, LocalTime endTime) {
        return (this.startTime.isAfter(startTime) && this.startTime.isBefore(endTime))
                || (this.endTime.isAfter(startTime) && this.endTime.isBefore(endTime));
    }

    public boolean isConflicting(TimeInterval timeInterval) {
        if (this.isBetween(timeInterval)) return true;
        else if (timeInterval.isBetween(this)) return true;
        else return timeInterval.isBetween(startTime, endTime);
    }

    @Override
    public String toString() {
        return startTime + "-" + endTime;
    }
}
