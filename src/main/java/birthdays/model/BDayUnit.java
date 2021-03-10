package birthdays.model;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Objects;

public class BDayUnit implements Comparable<BDayUnit> {

    DateTimeFormatter formatter = DateTimeFormat.forPattern("dd.MM.yyyy");

    private int id;
    private String firstName;
    private String lastName;
    private String fullName;
    private DateTime date;
    private String phoneNumber;
    private String description = "Description";

    private int daysTo;

    public BDayUnit(int id, String firstName, String lastName, String date, String phoneNumber, String description) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.date = new DateTime(formatter.parseDateTime(date));
        this.phoneNumber = phoneNumber;
        this.fullName = getFullName();
        this.description = description;

        daysTo = calculdateDayTo(date);
    }

    public BDayUnit(String firstName, String lastName, String date, String phoneNumber, String description) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.date = new DateTime(formatter.parseDateTime(date));
        this.phoneNumber = phoneNumber;
        this.fullName = getFullName();
        this.description = description;

        daysTo = calculdateDayTo(date);
    }

    public BDayUnit() {
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFullName() {
        return lastName + " " + firstName;
    }

    public String getDate() {
        String day = String.valueOf(date.getDayOfMonth());
        String month = String.valueOf(date.getMonthOfYear());
        if (day.length() < 2) {
            day = "0" + day;
        }
        if (month.length() < 2) {
            month = "0" + month;
        }
        return day + "." + month + "." + date.getYear();
    }

    public DateTime getDateTime() {
        return date;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public int getDaysTo() {
        return daysTo;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setDate(String date) {
        this.date = new DateTime(formatter.parseDateTime(date));
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDescription() {
        return description;
    }

    private int calculdateDayTo(String date) {
        DateTime start = new DateTime();
        DateTime temp = new DateTime(formatter.parseDateTime(date));
        DateTime end = new DateTime(start.getYear(), temp.getMonthOfYear(), temp.getDayOfMonth(), 0, 0, 1);

        int daysBetwen = Days.daysBetween(start, end).getDays();
        if (daysBetwen == 0) {
            int today = start.getDayOfMonth();
            int bBoyday = temp.getDayOfMonth();
            if (today == bBoyday) {
                daysBetwen = 0;
            } else {
                daysBetwen = 1;
            }
        } else {
            daysBetwen++;
        }
        if (daysBetwen < 0) {
            end = new DateTime(start.getYear() + 1, temp.getMonthOfYear(), temp.getDayOfMonth(), 0, 0, 1);
            return Days.daysBetween(start, end).getDays();
        }
        return daysBetwen;
    }

    @Override
    public String toString() {
        return "BDayUnit{" +
                "formatter=" + formatter +
                ", id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", fullName='" + fullName + '\'' +
                ", date=" + date +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", description='" + description + '\'' +
                ", daysTo=" + daysTo +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BDayUnit bDayUnit = (BDayUnit) o;
        return id == bDayUnit.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public int compareTo(BDayUnit o1) {
        return daysTo - o1.getDaysTo();
    }
}
