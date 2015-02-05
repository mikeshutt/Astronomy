package com.predictiveHunting.Astronomy;

import java.time.LocalTime;

/**
 * Created by Mike on 1/7/2015.
 */
public class HourAngle {
    private double decimalHours;
    private int hours;
    private int minutes;
    private double seconds;

    public HourAngle() {
        this(0, 0, 0);
    }
    public HourAngle(double totalHours) {
        this.setValue(totalHours);
    }
    public HourAngle(int hours, int minutes, double seconds) {
        this.setValue(hours, minutes, seconds);
    }

    public void setValue(double totalHours) {
        boolean isNegative = (totalHours < 0);
        this.decimalHours = totalHours;
        totalHours = Math.abs(totalHours);
        this.hours = (int)totalHours;
        double remainder = totalHours - this.hours;
        this.minutes = (int)(remainder * 60d);
        remainder -= ((double)this.minutes / 60d);
        this.seconds = (remainder) * 3600;
        if (isNegative) {
            this.hours = -this.hours;
        }
    }
    public void setValue(int hours, int minutes, double seconds) {
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
        if (this.hours < 0) {
            this.decimalHours = this.hours - ((double)this.minutes / 60d) - (this.seconds / 3600);
        }
        else {
            this.decimalHours = this.hours + ((double)this.minutes / 60d) + (this.seconds / 3600);
        }
    }

    public int getHours() {
        return this.hours;
    }

    public int getMinutes() { return this.minutes; }

    public double getSeconds() {
        return this.seconds;
    }

    @Override public String toString() {
        return String.format("%dh %dm %fs (%f)", this.getHours(), this.getMinutes(), this.getSeconds(), this.toDecimalHours());
    }

    public double toDecimalHours() {
        return this.decimalHours;
    }

    public static HourAngle fromDecimalHours(double hours) {
        return new HourAngle(hours);
    }
    public static HourAngle fromAngle(double decimalDegrees) {
        double decimalHours = decimalDegrees / 15d;
        return new HourAngle(decimalHours);
    }

    public static HourAngle fromAngle(Angle angle)
    {
        return fromDecimalHours(angle.toDecimal());
    }

    public Angle toAngle() {
        double totalHours = this.getHours() + ((double)this.getMinutes() / 60d) + (this.getSeconds() / 3600d);
        return new Angle(360d * totalHours/ 24d);
    }

    /*
    public static HourAngle fromAngle(Angle angle) {
        double totalHours = angle.toDecimal() / 15; // 360 degrees / 24 hours
        int wholeHours = (int)totalHours;
        double remainder = totalHours - (double)wholeHours;
        int wholeMinutes = (int)(remainder * 60d);
        remainder -= ((double)wholeMinutes / 60d);
        double seconds = remainder * 3600d;
        return new HourAngle(wholeHours, wholeMinutes, seconds);
    }
    */
}
