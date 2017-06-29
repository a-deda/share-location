package nl.adeda.sharelocation;

/**
 * Class object that is used for storing the date and time in one place.
 */

public class DateTime {

    private int day;
    private int month;
    private int year;

    private int hour;
    private int minute;

    public DateTime(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    // Constructor without arguments to load Firebase class in
    public DateTime() {};

    // Setters
    public void setHour(int hour) {
        this.hour = hour;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    // Getters
    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }
}
