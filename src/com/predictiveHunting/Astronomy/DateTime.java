package com.predictiveHunting.Astronomy;

import com.predictiveHunting.Helpers;

import java.time.*;
import java.time.temporal.ChronoUnit;

/**
 * Created by Mike on 12/31/2014.
 */
public class DateTime {

    public static int JULIAN_TO_GREGORIAN = 15 + 31 * (10 + 12 * 1582); // date on which Gregorian calendar was adopted (10/15/1582)

    private static int ONE_BILLION = 1000000000;

    public static int DUBLIN_EPOCH = 2415020; // 12h Dec 31, 1899

    public static double toJulianDate(ZonedDateTime date) {

        ZonedDateTime utcDateTime = ZonedDateTime.ofInstant(date.toInstant(), ZoneOffset.UTC);

        int year = utcDateTime.getYear();;
        int month = utcDateTime.getMonthValue();
        double hours = timeToDecimalHours(utcDateTime.toLocalTime());

        if (utcDateTime.getMonthValue() < 3) {
            year--;
            month += 12;
        }

        boolean isAfterGregorianCalendarAdopted =
            (utcDateTime.getYear() > 1582)
            ||
            ( (utcDateTime.getYear() == 1582) && (utcDateTime.getMonthValue() > 10) )
            ||
            ( (utcDateTime.getYear() == 1582) && (utcDateTime.getMonthValue() == 10) && (utcDateTime.getDayOfMonth() >= 15) );

        // calculate the number of days added by the adoption of the gregorian calendar
        int gregorianCalendarAdjustment = 0;
        if (isAfterGregorianCalendarAdopted) {
            int wholeCenturies = (int)(year / 100d);
            gregorianCalendarAdjustment = 2 - wholeCenturies + (int)(wholeCenturies / 4d);
        }

        // calculate the number of days included the the span of years
        int daysFromYear = 0;
        if (year < 0) {
            daysFromYear = (int)((365.25 * year) - 0.75);
        }
        else {
            daysFromYear = (int)(365.25 * year);
        }

        // calculate the number of days included in the span of months
        int daysFromMonth = (int)(30.6001 * (month + 1));

        // add various components days
        return gregorianCalendarAdjustment + daysFromYear + daysFromMonth + utcDateTime.getDayOfMonth() + (timeToDecimalHours(utcDateTime.toLocalTime()) / 24d) + 1720994.5;
    }

    public static double toDublinJulianDate(ZonedDateTime date) {
        return toJulianDate(date) - DateTime.DUBLIN_EPOCH;
    }

    public static LocalTime greenwichSiderealTimeToLocalSiderealTime(LocalTime greenwichSiderealTime, double longitude) {
        double gstHours = timeToDecimalHours(greenwichSiderealTime);
        double longitudeHours = longitude / 15;
        double localSiderealHours = gstHours + longitudeHours;
        if (localSiderealHours > 24) {
            localSiderealHours -= 24;
        }
        else if (localSiderealHours < 0) {
            localSiderealHours += 24;
        }
        return decimalHoursToTime(localSiderealHours);
    }

    public static LocalTime localSiderealTimeToGreenwichSiderealTime(LocalTime localSiderealTime, double longitude) {
        double lstHours = timeToDecimalHours(localSiderealTime);
        double longitudeHours = longitude / 15;
        double gstHours = lstHours - longitudeHours;
        if (gstHours > 24) {
            gstHours -= 24;
        }
        else if (gstHours < 0) {
            gstHours += 24;
        }
        return decimalHoursToTime(gstHours);
    }

    public static LocalTime utcToGreenwichSiderealTime(ZonedDateTime utcDateTime) {
        ZonedDateTime utcCalendarDate = DateTime.toUtc(utcDateTime).truncatedTo(ChronoUnit.DAYS);
        double s = DateTime.toJulianDate(utcCalendarDate) - 2451545d;  // not sure what this value represents?
        double t = s / 36525d;
        double t0 = 6.697374558 + (2400.051336 * t) + (0.000025862 * t * t);
        t0 = Helpers.bringIntoRange(t0, 24);
        double decimalHours = DateTime.timeToDecimalHours(utcDateTime.toLocalTime());
        decimalHours *= 1.0027379089;
        double gstDecimalHours = t0 + decimalHours;
        gstDecimalHours = Helpers.bringIntoRange(gstDecimalHours, 24);
        return decimalHoursToTime(gstDecimalHours);
    }

    public static ZonedDateTime greenwichSiderealTimeToUtc(LocalDateTime greenwichSiderealTime) {
        ZonedDateTime greenwichDate = ZonedDateTime.of(greenwichSiderealTime.getYear(), greenwichSiderealTime.getMonthValue(), greenwichSiderealTime.getDayOfMonth(), 0, 0, 0, 0, ZoneId.of("UTC"));
        double julianDate = DateTime.toJulianDate(greenwichDate);
        double S = julianDate - 2451545d;
        double T = S / 36525d;
        double T0 = 6.697374558 + 2400.051336 * T + 0.000025862 * Math.pow(T, 2);
        T0 = Helpers.bringIntoRange(T0, 24);
        double timeHours = timeToDecimalHours(greenwichSiderealTime.toLocalTime());
        double A = timeHours - T0;
        A = Helpers.bringIntoRange(A, 24);
        double utcHours = A * 0.9972695663;
        LocalTime utcTime = decimalHoursToTime(utcHours);
        return ZonedDateTime.of(greenwichDate.toLocalDate(), utcTime, ZoneId.of("UTC"));
    }

    public static ZonedDateTime localSiderealTimeToUtc(LocalDateTime localSiderealTime, double longitude) {
        LocalTime gst = localSiderealTimeToGreenwichSiderealTime(localSiderealTime.toLocalTime(), longitude);
        return greenwichSiderealTimeToUtc(LocalDateTime.of(localSiderealTime.toLocalDate(), gst));
    }

    public static LocalTime utcTolocalSiderealTime(ZonedDateTime utcTime, double longitude) {
        LocalTime gst = utcToGreenwichSiderealTime(utcTime);
        return greenwichSiderealTimeToLocalSiderealTime(gst, longitude);
    }

    public static double hoursMinutesSecondsToDecimalHours(int hours, int minutes, double seconds) {
        return (double)hours + ((double)minutes / 60d) + (seconds / 3600d);
    }

    public static double timeToDecimalHours(LocalTime time) {
        return (double)(time.toNanoOfDay() / (3600d * ONE_BILLION) );
    }

    public static LocalTime decimalHoursToTime(double hours) {
        return LocalTime.ofNanoOfDay((long)(hours * 3600 * ONE_BILLION));
    }

    public static ZonedDateTime toUtc(ZonedDateTime dateTime) {
        return dateTime.withZoneSameInstant(ZoneId.of("UTC"));
    }
}
