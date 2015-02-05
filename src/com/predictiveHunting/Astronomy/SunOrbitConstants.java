package com.predictiveHunting.Astronomy;

import com.predictiveHunting.Helpers;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

public class SunOrbitConstants {
    private ZonedDateTime dateTime;
    private double julianDate;
    private Angle meanEclipticLongitude;
    private Angle perigeeLongitude;
    private double earthOrbitEccentricity;
    private double centuriesSinceDublinEpoch;

    public SunOrbitConstants(ZonedDateTime dateTime) {
        this.dateTime = dateTime;
        //this.julianDate = DateTime.toJulianDate(dateTime);
        ZonedDateTime utcDateTime = dateTime.withZoneSameInstant(ZoneId.of("UTC"));
        this.julianDate = DateTime.toJulianDate(utcDateTime.truncatedTo(ChronoUnit.DAYS));
        double hours = DateTime.timeToDecimalHours(utcDateTime.toLocalTime());
        this.centuriesSinceDublinEpoch = (this.julianDate - DateTime.DUBLIN_EPOCH) / 36525d +  hours / 876600d;
        this.meanEclipticLongitude = getMeanEclipticLongitude(this.centuriesSinceDublinEpoch);
        this.perigeeLongitude = getPerigeeLongitude(this.centuriesSinceDublinEpoch);
        this.earthOrbitEccentricity = getEarthOrbitEccentricity(this.centuriesSinceDublinEpoch);
    }

    public double getT() {
        return this.centuriesSinceDublinEpoch;
    }
    public ZonedDateTime getDateTime() {
        return this.dateTime;
    }

    public double getJulianDate() {
        return this.julianDate;
    }

    public Angle getMeanEclipticLongitude() {
        return this.meanEclipticLongitude;
    }

    public Angle getPerigeeLongitude() {
        return this.perigeeLongitude;
    }

    public double getEarthOrbitEccentricity() {
        return this.earthOrbitEccentricity;
    }

    public static Angle getMeanEclipticLongitude(double T) {
        double A = 100.0021359 * T;
        double B = 360 * (A - (int)A);
        double degrees = Helpers.bringIntoRange(279.69667788 + B + 0.0003025 * Math.pow(T, 2), 360);
        //double degrees = Helpers.bringIntoRange(279.69667788 + 36000.76892 * T + 0.0003025 * Math.pow(T, 2), 360);
        return new Angle(degrees);
    }

    public static Angle getPerigeeLongitude(double T) {
        double degrees = Helpers.bringIntoRange(281.2208444 + 1.719175 * T + 0.000452778 * Math.pow(T, 2), 360);
        return new Angle(degrees);
    }
    public static double getEarthOrbitEccentricity(double T) {
        return 0.01675104 - 0.0000418 * T - 0.000000126 * Math.pow(T, 2);
    }


}
