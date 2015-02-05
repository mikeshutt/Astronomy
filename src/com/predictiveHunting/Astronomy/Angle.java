package com.predictiveHunting.Astronomy;

public class Angle {
    private double decimal;
    private int degrees;
    private int minutes;
    private double seconds;

    public Angle() {
        this(0, 0, 0);
    }

    public Angle(double degrees) {
        this.setValue(degrees);
    }

    public Angle(int hours, int minutes, double seconds) {
        this.setValue(hours, minutes, seconds);
    }

    public void setValue(double decimal) {
        boolean isNegative = (decimal < 0);
        this.decimal = decimal;
        decimal = Math.abs(decimal);
        this.degrees = (int)decimal;
        double remainder = decimal - this.degrees;
        this.minutes = (int)(remainder * 60d);
        remainder -= ((double)this.minutes / 60d);
        this.seconds = (remainder) * 3600;
        if (isNegative) {
            this.degrees = -this.degrees;
        }
    }
    public void setValue(int degrees, int minutes, double seconds) {
        this.degrees = degrees;
        this.minutes = minutes;
        this.seconds = seconds;
        if (this.degrees < 0) {
            this.decimal = this.degrees - ((double)this.minutes / 60d) - (this.seconds / 3600);
        }
        else {
            this.decimal = this.degrees + ((double)this.minutes / 60d) + (this.seconds / 3600);
        }
    }

    public int getDegrees() { return this.degrees; }

    public int getMinutes() {
        return this.minutes;
    }

    public double getSeconds() {
        return this.seconds;
    }

    public double toDecimal() {
        return this.decimal;
    }

    @Override public String toString() {
        return String.format("%d° %d' %f'' (%f)", this.getDegrees(), this.getMinutes(), this.getSeconds(), this.toDecimal());
    }

    public static Angle fromDegrees(double degrees) {
        return new Angle(degrees);
    }

    public static Angle fromDegrees(int degrees, int minutes, double seconds) {
        return new Angle(degrees, minutes, seconds);
    }

}
