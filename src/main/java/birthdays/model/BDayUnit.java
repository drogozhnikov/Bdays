package birthdays.model;

import org.joda.time.DateTime;
import org.joda.time.Days;

import java.sql.Date;
import java.util.Objects;

public class BDayUnit implements Comparable<BDayUnit> {

    private int id;
    private String firstName;
    private String lastName;
    private String fullName;
    private DateTime date;
    private String phoneNumber;
    private String description = "Description";

    private int daysTo;

    public BDayUnit(int id, String firstName, String lastName, Date date, String phoneNumber) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.date = new DateTime(date);
        this.phoneNumber = phoneNumber;
        this.fullName = getFullName();

        daysTo = calculdateDayTo(date);
    }

    public BDayUnit(String firstName, String lastName, Date date, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.date = new DateTime(date);
        this.phoneNumber = phoneNumber;
        this.fullName = getFullName();

        daysTo = calculdateDayTo(date);
    }

    public BDayUnit() {}

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

    public Date getDate() {
        return new Date(date.getMillis());
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

    public void setDate(Date date) {
        this.date = new DateTime(date.getTime());
    }

    public void setDate(DateTime date) {
        this.date = date;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private int calculdateDayTo(Date date) {
        DateTime start = new DateTime();
        DateTime temp = new DateTime(date.getTime());
        DateTime end = new DateTime(start.getYear(),temp.getMonthOfYear(), temp.getDayOfMonth(),0,0,0);

        int daysBetwen = Days.daysBetween(start, end).getDays();
        if(daysBetwen<0){
            end = new DateTime(start.getYear()+1,temp.getMonthOfYear(), temp.getDayOfMonth(),0,0,0);
            return Days.daysBetween(start, end).getDays();
        }
        return daysBetwen;
    }

    @Override
    public String toString() {
        return fullName + " " + date;
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
