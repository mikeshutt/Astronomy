package com.predictiveHunting.Astronomy;

import com.predictiveHunting.Astronomy.Coordinates.Ecliptic;
import com.predictiveHunting.Astronomy.Coordinates.EquatorialRightAscension;
import com.predictiveHunting.Astronomy.Coordinates.Geographic;
import com.predictiveHunting.Astronomy.CoordinatesConversion.EclipticToEquatorialRightAscension;
import com.predictiveHunting.Helpers;

import java.time.*;

public class Sun {

    // calculates the longitude of the sun's position at the specified date & utc
    public static Angle getEclipticLongitudeEstimated(ZonedDateTime dateTime) {
        SunOrbitConstants orbitConstants = new SunOrbitConstants(dateTime);
        double daysSinceEpoch = DateTime.toJulianDate(dateTime) - orbitConstants.getJulianDate();
        double meanAnomaly = (360d / 365.242191) * daysSinceEpoch + orbitConstants.getMeanEclipticLongitude().toDecimal() - orbitConstants.getPerigeeLongitude().toDecimal();
        double trueAnomaly = meanAnomaly + 360d / Math.PI * orbitConstants.getEarthOrbitEccentricity() * Math.sin(Math.toRadians(meanAnomaly));
        return new Angle(Helpers.bringIntoRange(trueAnomaly + orbitConstants.getPerigeeLongitude().toDecimal(), 360));
    }

    // calculates the longitude of the sun's position at the specified date & utc
    public static Angle getEclipticLongitude(ZonedDateTime dateTime) {
        SunOrbitConstants orbitConstants = new SunOrbitConstants(dateTime);
        double meanAnomaly = meanAnomaly(orbitConstants);
        double eccentricAnomaly = resolveEccentricAnomaly(meanAnomaly, orbitConstants.getEarthOrbitEccentricity());
        double orbitEccentricity = orbitConstants.getEarthOrbitEccentricity();
        double trueAnomaly = 2 * Math.atan( Math.sqrt((1 + orbitEccentricity) / (1 - orbitEccentricity)) * Math.tan(eccentricAnomaly / 2d) );
        trueAnomaly = Helpers.bringIntoRange(Math.toDegrees(trueAnomaly), 360);
        return new Angle(Helpers.bringIntoRange(trueAnomaly + orbitConstants.getPerigeeLongitude().toDecimal(), 360));
    }

    public static double meanAnomaly(ZonedDateTime dateTime) {
        return meanAnomaly(new SunOrbitConstants(dateTime));
    }

    private static double meanAnomaly(SunOrbitConstants orbitConstants) {
        return Helpers.bringIntoRange(orbitConstants.getMeanEclipticLongitude().toDecimal() - orbitConstants.getPerigeeLongitude().toDecimal(), 360);
    }

    private static double resolveEccentricAnomaly(double meanAnomaly, double earthOrbitEccentricity) {

        double requiredAccuracy = 0.000001;
        double meanAnomalyRadians = Math.toRadians(meanAnomaly);
        double eccentricAnomaly = meanAnomalyRadians;

        do {
            double delta = eccentricAnomaly - earthOrbitEccentricity * Math.sin(eccentricAnomaly) - meanAnomalyRadians;
            if (Math.abs(delta) <= requiredAccuracy) {
                break;
            }
            double eccentricityAdjustment = delta / (1 - earthOrbitEccentricity * Math.cos(eccentricAnomaly));
            eccentricAnomaly -= eccentricityAdjustment;
        }
        while (true);
        return eccentricAnomaly;
    }

    public static RiseAndSetTimesUtc sunRiseAndSet(LocalDate date, ZoneId timeZone, Geographic location) {

        ZonedDateTime midDay = ZonedDateTime.of(date, LocalTime.of(12, 0), timeZone);
        Angle midDayLongitude = getEclipticLongitudePrecise(midDay);

        Angle sunLongitude = midDayLongitude;
        RiseOrSetTime riseTime = riseOrSetTime(sunLongitude, date, timeZone, location, true);
        if (!riseTime.status.equals("OK")) return null;
        //if (!isGstUtcAmbiguity(riseTime.gst, date)) return null;

        sunLongitude = getEclipticLongitudePrecise(ZonedDateTime.of(date, riseTime.utc, ZoneId.of("UTC")));
        riseTime = riseOrSetTime(sunLongitude, date, timeZone, location, true);
        if (!riseTime.status.equals("OK")) return null;

        sunLongitude = midDayLongitude;
        RiseOrSetTime setTime = riseOrSetTime(sunLongitude, date, timeZone, location, false);
        if (!setTime.status.equals("OK")) return null;
        //if (!isGstUtcAmbiguity(setTime.gst, date)) return null;

        sunLongitude = getEclipticLongitudePrecise(ZonedDateTime.of(date, setTime.utc, ZoneId.of("UTC")));
        setTime = riseOrSetTime(sunLongitude, date, timeZone, location, false);
        if (!setTime.status.equals("OK")) return null;

        return new RiseAndSetTimesUtc(
                ZonedDateTime.of(date, riseTime.utc, ZoneId.of("UTC")).withZoneSameInstant(timeZone),
                ZonedDateTime.of(date, setTime.utc, ZoneId.of("UTC")).withZoneSameInstant(timeZone)
        );
    }

    private static RiseOrSetTime riseOrSetTime(Angle sunLongitude, LocalDate gmtDate, ZoneId timeZone, Geographic location, boolean isRiseTime) {

        double A = sunLongitude.toDecimal() + Helpers.NutatLong(gmtDate) - 0.005694;
        Ecliptic middaySunPositionEcliptic = new Ecliptic(new Angle(A), new Angle(0));
        ZonedDateTime midday = ZonedDateTime.of(gmtDate, LocalTime.of(12, 0), timeZone);
        EquatorialRightAscension middaySunPositionEquatorial = EclipticToEquatorialRightAscension.convert(middaySunPositionEcliptic, midday);

        double F, H, I;
        double rightAscensionRadians = Math.toRadians(middaySunPositionEquatorial.rightAscension.toDecimal());
        double declinationRadians = Math.toRadians(middaySunPositionEquatorial.declination.toDecimal());
        double D = Math.toRadians(0.8333333);
        double observationLatitudeRadians = Math.toRadians(location.getLatitude().toDecimal());
        F = -(Math.sin(D) + Math.sin(observationLatitudeRadians) * Math.sin(declinationRadians)) / (Math.cos(observationLatitudeRadians) * Math.cos(declinationRadians));

        RiseOrSetTime adjust = new RiseOrSetTime();
        if (Math.abs(F) < 1) {
            H = Math.acos(F);
            adjust.status = "OK";
        }
        else {
            H = 0;
            if (F > 1) {
                adjust.status = "** never rises";
            }
            else {
                adjust.status = "** circumpolar";
            }
        }

        if (isRiseTime) {
            I = HourAngle.fromAngle(Math.toDegrees(rightAscensionRadians - H)).toDecimalHours();
        }
        else {
            I = HourAngle.fromAngle(Math.toDegrees(rightAscensionRadians + H)).toDecimalHours();
        }

        double decimalHours = I - 24 * (int)(I / 24d);

        adjust.gst = DateTime.localSiderealTimeToGreenwichSiderealTime(DateTime.decimalHoursToTime(decimalHours), location.getLongitude().toDecimal());
        adjust.utc = DateTime.greenwichSiderealTimeToUtc(LocalDateTime.of(gmtDate, adjust.gst)).toLocalTime();

        return adjust;
    }

    public static Angle getEclipticLongitudePrecise(ZonedDateTime dateTime) {

        SunOrbitConstants orbitConstants = new SunOrbitConstants(dateTime);
        double T = orbitConstants.getT();
        double T2 = T * T;
        double meanAnomaly = Helpers.bringIntoRange(orbitConstants.getMeanEclipticLongitude().toDecimal() - orbitConstants.getPerigeeLongitude().toDecimal(), 360);

        double eccentricAnomaly = resolveEccentricAnomaly(meanAnomaly, orbitConstants.getEarthOrbitEccentricity());
        double orbitEccentricity = orbitConstants.getEarthOrbitEccentricity();
        double trueAnomaly = 2 * Math.atan( Math.sqrt((1 + orbitEccentricity) / (1 - orbitEccentricity)) * Math.tan(eccentricAnomaly / 2d) );

        double A, B;
        double A1, B1, C1, D1, E1;
        A = 62.55209472 * T;
        B = 360 * (A - (int)A);
        A1 = Math.toRadians(153.23 + B);
        A = 125.1041894 * T;
        B = 360 * (A - (int)A);
        B1 = Math.toRadians(216.57 + B);
        A = 91.56766028 * T;
        B = 360 * (A - (int)A);
        C1 = Math.toRadians(312.69 + B);
        A = 1236.853095 * T;
        B = 360 * (A - (int)A);
        D1 = Math.toRadians(350.74 - 0.00144 * T2 + B);
        E1 = Math.toRadians(231.19 + 20.2 * T);

        double D2;
        D2 = 0.00134 * Math.cos(A1) + 0.00154 * Math.cos(B1) + 0.002 * Math.cos(C1);
        D2 = D2 + 0.00179 * Math.sin(D1) + 0.00178 * Math.sin(E1);

        double SR, TP;
        SR = trueAnomaly + Math.toRadians(orbitConstants.getMeanEclipticLongitude().toDecimal() - meanAnomaly + D2);
        TP = 6.283185308;
        SR = SR - TP * (int)(SR / TP);
        return new Angle(Math.toDegrees(SR));
    }
}

class RiseOrSetTime {
    public LocalTime gst;
    public LocalTime utc;
    public String status;
}
