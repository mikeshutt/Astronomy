package com.predictiveHunting.Astronomy;

import com.predictiveHunting.Astronomy.Coordinates.Ecliptic;
import com.predictiveHunting.Astronomy.Coordinates.EquatorialHourAngle;
import com.predictiveHunting.Astronomy.Coordinates.EquatorialRightAscension;
import com.predictiveHunting.Astronomy.Coordinates.Geographic;
import com.predictiveHunting.Astronomy.CoordinatesConversion.EclipticToEquatorialRightAscension;
import com.predictiveHunting.Helpers;
import com.predictiveHunting.RiseAndSetTimesLocalTime;

import java.time.*;

public class Moon {
    public static double phase(ZonedDateTime dateTime) {
        Ecliptic moonPosition = position(dateTime);
        Angle sunPosition = Sun.getEclipticLongitudePrecise(dateTime);
        double CD = Math.cos(Math.toRadians(moonPosition.longitude.toDecimal() - sunPosition.toDecimal())) * Math.cos(Math.toRadians(moonPosition.latitude.toDecimal()));
        double D = Math.acos(CD);
        double SD = Math.sin(D);
        double I = 0.1468 * SD * (1 - 0.0549 * Math.sin(meanAnomaly(dateTime)));
        I = I / (1 - 0.0167 * Math.sin(Sun.meanAnomaly(dateTime)));
        I = 3.141592654 - D - Math.toRadians(I);
        double K = (1 + Math.cos(I)) / 2d;
        return K;
    }

    public static Ecliptic position(ZonedDateTime dateTime) {

        MoonCalcConstants moonCalcConstants = new MoonCalcConstants(dateTime);

        double E2 = moonCalcConstants.E * moonCalcConstants.E;
        double ML = Math.toRadians(moonCalcConstants.ML);
        double MS = Math.toRadians(moonCalcConstants.MS);
        double NA = Math.toRadians(moonCalcConstants.NA);
        double ME1 = Math.toRadians(moonCalcConstants.ME1);
        double MF = Math.toRadians(moonCalcConstants.MF);
        double MD = Math.toRadians(moonCalcConstants.MD);
        double E = moonCalcConstants.E;
        double C = moonCalcConstants.C;

        double L =
                  6.28875 * Math.sin(MD) + 1.274018 * Math.sin(2 * ME1 - MD)
                + 0.658309 * Math.sin(2 * ME1) + 0.213616 * Math.sin(2 * MD)
                - E * 0.185596 * Math.sin(MS) - 0.114336 * Math.sin(2 * MF)
                + 0.058793 * Math.sin(2 * (ME1 - MD))
                + 0.057212 * E * Math.sin(2 * ME1 - MS - MD) + 0.05332 * Math.sin(2 * ME1 + MD)
                + 0.045874 * E * Math.sin(2 * ME1 - MS) + 0.041024 * E * Math.sin(MD - MS)
                - 0.034718 * Math.sin(ME1) - E * 0.030465 * Math.sin(MS + MD)
                + 0.015326 * Math.sin(2 * (ME1 - MF)) - 0.012528 * Math.sin(2 * MF + MD)
                - 0.01098 * Math.sin(2 * MF - MD) + 0.010674 * Math.sin(4 * ME1 - MD)
                + 0.010034 * Math.sin(3 * MD) + 0.008548 * Math.sin(4 * ME1 - 2 * MD)
                - E * 0.00791 * Math.sin(MS - MD + 2 * ME1) - E * 0.006783 * Math.sin(2 * ME1 + MS)
                + 0.005162 * Math.sin(MD - ME1) + E * 0.005 * Math.sin(MS + ME1)
                + 0.003862 * Math.sin(4 * ME1) + E * 0.004049 * Math.sin(MD - MS + 2 * ME1)
                + 0.003996 * Math.sin(2 * (MD + ME1)) + 0.003665 * Math.sin(2 * ME1 - 3 * MD)
                + E * 0.002695 * Math.sin(2 * MD - MS) + 0.002602 * Math.sin(MD - 2 * (MF + ME1))
                + E * 0.002396 * Math.sin(2 * (ME1 - MD) - MS) - 0.002349 * Math.sin(MD + ME1)
                + E2 * 0.002249 * Math.sin(2 * (ME1 - MS)) - E * 0.002125 * Math.sin(2 * MD + MS)
                - E2 * 0.002079 * Math.sin(2 * MS) + E2 * 0.002059 * Math.sin(2 * (ME1 - MS) - MD)
                - 0.001773 * Math.sin(MD + 2 * (ME1 - MF)) - 0.001595 * Math.sin(2 * (MF + ME1))
                + E * 0.00122 * Math.sin(4 * ME1 - MS - MD) - 0.00111 * Math.sin(2 * (MD + MF))
                + 0.000892 * Math.sin(MD - 3 * ME1) - E * 0.000811 * Math.sin(MS + MD + 2 * ME1)
                + E * 0.000761 * Math.sin(4 * ME1 - MS - 2 * MD)
                + E2 * 0.000704 * Math.sin(MD - 2 * (MS + ME1))
                + E * 0.000693 * Math.sin(MS - 2 * (MD - ME1))
                + E * 0.000598 * Math.sin(2 * (ME1 - MF) - MS)
                + 0.00055 * Math.sin(MD + 4 * ME1) + 0.000538 * Math.sin(4 * MD)
                + E * 0.000521 * Math.sin(4 * ME1 - MS) + 0.000486 * Math.sin(2 * MD - ME1)
                + E2 * 0.000717 * Math.sin(MD - 2 * MS);
        double MM = Helpers.unwind(ML + Math.toRadians(L));

        double G =
                  5.128189 * Math.sin(MF) + 0.280606 * Math.sin(MD + MF)
                + 0.277693 * Math.sin(MD - MF) + 0.173238 * Math.sin(2 * ME1 - MF)
                + 0.055413 * Math.sin(2 * ME1 + MF - MD) + 0.046272 * Math.sin(2 * ME1 - MF - MD)
                + 0.032573 * Math.sin(2 * ME1 + MF) + 0.017198 * Math.sin(2 * MD + MF)
                + 0.009267 * Math.sin(2 * ME1 + MD - MF) + 0.008823 * Math.sin(2 * MD - MF)
                + E * 0.008247 * Math.sin(2 * ME1 - MS - MF) + 0.004323 * Math.sin(2 * (ME1 - MD) - MF)
                + 0.0042 * Math.sin(2 * ME1 + MF + MD) + E * 0.003372 * Math.sin(MF - MS - 2 * ME1)
                + E * 0.002472 * Math.sin(2 * ME1 + MF - MS - MD)
                + E * 0.002222 * Math.sin(2 * ME1 + MF - MS)
                + E * 0.002072 * Math.sin(2 * ME1 - MF - MS - MD)
                + E * 0.001877 * Math.sin(MF - MS + MD) + 0.001828 * Math.sin(4 * ME1 - MF - MD)
                - E * 0.001803 * Math.sin(MF + MS) - 0.00175 * Math.sin(3 * MF)
                + E * 0.00157 * Math.sin(MD - MS - MF) - 0.001487 * Math.sin(MF + ME1)
                - E * 0.001481 * Math.sin(MF + MS + MD) + E * 0.001417 * Math.sin(MF - MS - MD)
                + E * 0.00135 * Math.sin(MF - MS) + 0.00133 * Math.sin(MF - ME1)
                + 0.001106 * Math.sin(MF + 3 * MD) + 0.00102 * Math.sin(4 * ME1 - MF)
                + 0.000833 * Math.sin(MF + 4 * ME1 - MD) + 0.000781 * Math.sin(MD - 3 * MF)
                + 0.00067 * Math.sin(MF + 4 * ME1 - 2 * MD) + 0.000606 * Math.sin(2 * ME1 - 3 * MF)
                + 0.000597 * Math.sin(2 * (ME1 + MD) - MF)
                + E * 0.000492 * Math.sin(2 * ME1 + MD - MS - MF) + 0.00045 * Math.sin(2 * (MD - ME1) - MF)
                + 0.000439 * Math.sin(3 * MD - MF) + 0.000423 * Math.sin(MF + 2 * (ME1 + MD))
                + 0.000422 * Math.sin(2 * ME1 - MF - 3 * MD) - E * 0.000367 * Math.sin(MS + MF + 2 * ME1 - MD)
                - E * 0.000353 * Math.sin(MS + MF + 2 * ME1) + 0.000331 * Math.sin(MF + 4 * ME1)
                + E * 0.000317 * Math.sin(2 * ME1 + MF - MS + MD)
                + E2 * 0.000306 * Math.sin(2 * (ME1 - MS) - MF) - 0.000283 * Math.sin(MD + 3 * MF);
        double W1 = 0.0004664 * Math.cos(NA);
        double W2 = 0.0000754 * Math.cos(C);
        double BM = Math.toRadians(G) * (1 - W1 - W2);

        return new Ecliptic(new Angle(Math.toDegrees(MM)), new Angle(Math.toDegrees(BM)));
    }
    private static double meanAnomaly(ZonedDateTime dateTime) {

        double Q = DateTime.toDublinJulianDate(dateTime);
        double T = Q / 36525d;
        double T2 = T * T;

        double M1 = 27.55455094;
        double M2 = 6798.363307;

        M1 = Q / M1;
        M2 = Q / M2;

        M1 = 360 * (M1 - (int)M1);
        M2 = 360 * (M2 - (int)M2);

        double MD = 296.104608 + M1 + (0.009192 + 0.0000144 * T) * T2;
        double NA = 259.183275 - M2 + (0.002078 + 0.0000022 * T) * T2;
        double A = Math.toRadians(51.2 + 20.2 * T);
        double S1 = Math.sin(A);
        double S2 = Math.sin(Math.toRadians(NA));
        double B = 346.56 + (132.87 - 0.0091731 * T) * T;
        double S3 = 0.003964 * Math.sin(Math.toRadians(B));
        MD = MD + 0.000817 * S1 + S3 + 0.002541 * S2;

        return Math.toRadians(MD);

    }

    public static RiseAndSetTimesUtc riseAndSet(LocalDate date, ZoneId timeZone, Geographic location) {
        ZonedDateTime riseTime = riseOrSet(date, timeZone, location, true);
        ZonedDateTime setTime = riseOrSet(date, timeZone, location, false);
        return new RiseAndSetTimesUtc(riseTime, setTime);
    }

    public static ZonedDateTime riseOrSet(LocalDate date, ZoneId timeZone, Geographic location, boolean rise) {

        LocalTime middayLct = LocalTime.of(12, 0);
        LocalDate gmtDate = ZonedDateTime.of(date, middayLct, timeZone).withZoneSameInstant(ZoneId.of("UTC")).toLocalDate();

        RiseAndSetTimesLocalTime riseAndSetTimes = moonTimeSub(ZonedDateTime.of(gmtDate, middayLct, timeZone), location);

        LocalTime G1 = null;
        LocalTime GU = null;
        LocalTime riseOrSetTimeGst = null;
        LocalTime riseOrSetTimeUtc = null;
        double G1_decimalHours = 0;
        double UT_decimalHours = 0;
        for (int K = 1; K < 9; K++) {

            if (rise) {
                riseOrSetTimeGst = DateTime.localSiderealTimeToGreenwichSiderealTime(riseAndSetTimes.rise, location.getLongitude().toDecimal());
            }
            else {
                riseOrSetTimeGst = DateTime.localSiderealTimeToGreenwichSiderealTime(riseAndSetTimes.set, location.getLongitude().toDecimal());
            }
            riseOrSetTimeUtc = DateTime.greenwichSiderealTimeToUtc(LocalDateTime.of(gmtDate, riseOrSetTimeGst)).toLocalTime();

            if (K == 1) {
                G1 = riseOrSetTimeUtc;
            }
            else {
                G1 = GU;
            }

            GU = riseOrSetTimeUtc;

            G1_decimalHours = DateTime.timeToDecimalHours(G1);
            UT_decimalHours = DateTime.timeToDecimalHours(riseOrSetTimeUtc);

            if (Helpers.isGstUtcAmbiguity(riseOrSetTimeGst, gmtDate)) {
                if (Math.abs(G1_decimalHours - UT_decimalHours) > 0.5) {
                    UT_decimalHours += 23.93447;
                }
            }
            UT_decimalHours = UTDayAdjust(UT_decimalHours, G1_decimalHours);
            boolean addDay = (UT_decimalHours > 24);
            UT_decimalHours = Helpers.bringIntoRange(UT_decimalHours, 24);
            ZonedDateTime UT_DateTime = ZonedDateTime.of(gmtDate, DateTime.decimalHoursToTime(UT_decimalHours), ZoneId.of("UTC"));
            if (addDay) {
                UT_DateTime = UT_DateTime.plusDays(1);
            }
            ZonedDateTime localDateTime = UT_DateTime.withZoneSameInstant(timeZone);
            gmtDate = UT_DateTime.toLocalDate();
            //riseAndSetTimes = moonTimeSub(ZonedDateTime.of(gmtDate, localDateTime.toLocalTime(), timeZone), location);
            riseAndSetTimes = moonTimeSub(localDateTime, location);
        }

        if (rise) {
            riseOrSetTimeGst = DateTime.localSiderealTimeToGreenwichSiderealTime(riseAndSetTimes.rise, location.getLongitude().toDecimal());
        }
        else {
            riseOrSetTimeGst = DateTime.localSiderealTimeToGreenwichSiderealTime(riseAndSetTimes.set, location.getLongitude().toDecimal());
        }
        G1_decimalHours = DateTime.timeToDecimalHours(GU);
        UT_decimalHours = DateTime.timeToDecimalHours(riseOrSetTimeUtc);

        if (Helpers.isGstUtcAmbiguity(riseOrSetTimeGst, gmtDate)) {
            if (Math.abs(G1_decimalHours - UT_decimalHours) > 0.5) {
                UT_decimalHours += 23.93447;
            }
        }

        UT_decimalHours = UTDayAdjust(UT_decimalHours, G1_decimalHours);
        boolean addDay = (UT_decimalHours > 24);
        UT_decimalHours = Helpers.bringIntoRange(UT_decimalHours, 24);
        ZonedDateTime UT_DateTime = ZonedDateTime.of(gmtDate, DateTime.decimalHoursToTime(UT_decimalHours), ZoneId.of("UTC"));
        if (addDay) {
            UT_DateTime = UT_DateTime.plusDays(1);
        }

        return UT_DateTime.withZoneSameInstant(timeZone);
    }

    private static double UTDayAdjust(double UT_decimalHours, double G1_decimalHours) {
        if ((UT_decimalHours - G1_decimalHours) < -6d) {
            return UT_decimalHours + 24d;
        }
        else if ((UT_decimalHours - G1_decimalHours) > 6d) {
            return UT_decimalHours - 24d;
        }
        else {
            return UT_decimalHours;
        }
    }
    private static RiseAndSetTimesLocalTime moonTimeSub(ZonedDateTime dateTime, Geographic location) {
        Ecliptic moonPosition = position(dateTime);
        LocalDate gmtDate = dateTime.withZoneSameInstant(ZoneId.of("UTC")).toLocalDate();

        double PM = Math.toRadians(horizontalParallax(dateTime));
        double DP = Helpers.NutatLong(gmtDate);
        double TH = 0.27249 * Math.sin(PM);
        double DI = TH + 0.0098902 - PM;

        Ecliptic adjustedMoonPosition = new Ecliptic(
                new Angle(moonPosition.longitude.toDecimal() + DP),
                moonPosition.latitude
        );
        EquatorialRightAscension moonPositionEquatorial = EclipticToEquatorialRightAscension.convert(adjustedMoonPosition, dateTime);
        EquatorialHourAngle moonPositionHourAngle = new EquatorialHourAngle(
                HourAngle.fromAngle(moonPositionEquatorial.rightAscension.toDecimal()),
                moonPositionEquatorial.declination
        );
        return StarRiseAndSet.getRiseAndSetTimesLst(moonPositionHourAngle, gmtDate, DI, location);
    }

    private static double horizontalParallax(ZonedDateTime dateTime) {

        MoonCalcConstants moonCalcConstants = new MoonCalcConstants(dateTime);

        double E2 = moonCalcConstants.E * moonCalcConstants.E;
        double MS = Math.toRadians(moonCalcConstants.MS);
        double ME1 = Math.toRadians(moonCalcConstants.ME1);
        double MF = Math.toRadians(moonCalcConstants.MF);
        double MD = Math.toRadians(moonCalcConstants.MD);
        double E = moonCalcConstants.E;

        double PM =
                  0.950724 + 0.051818 * Math.cos(MD) + 0.009531 * Math.cos(2 * ME1 - MD)
                + 0.007843 * Math.cos(2 * ME1) + 0.002824 * Math.cos(2 * MD)
                + 0.000857 * Math.cos(2 * ME1 + MD) + E * 0.000533 * Math.cos(2 * ME1 - MS)
                + E * 0.000401 * Math.cos(2 * ME1 - MD - MS)
                + E * 0.00032 * Math.cos(MD - MS) - 0.000271 * Math.cos(ME1)
                - E * 0.000264 * Math.cos(MS + MD) - 0.000198 * Math.cos(2 * MF - MD)
                + 0.000173 * Math.cos(3 * MD) + 0.000167 * Math.cos(4 * ME1 - MD)
                - E * 0.000111 * Math.cos(MS) + 0.000103 * Math.cos(4 * ME1 - 2 * MD)
                - 0.000084 * Math.cos(2 * MD - 2 * ME1) - E * 0.000083 * Math.cos(2 * ME1 + MS)
                + 0.000079 * Math.cos(2 * ME1 + 2 * MD) + 0.000072 * Math.cos(4 * ME1)
                + E * 0.000064 * Math.cos(2 * ME1 - MS + MD) - E * 0.000063 * Math.cos(2 * ME1 + MS - MD)
                + E * 0.000041 * Math.cos(MS + ME1) + E * 0.000035 * Math.cos(2 * MD - MS)
                - 0.000033 * Math.cos(3 * MD - 2 * ME1) - 0.00003 * Math.cos(MD + ME1)
                - 0.000029 * Math.cos(2 * (MF - ME1)) - E * 0.000029 * Math.cos(2 * MD + MS)
                + E2 * 0.000026 * Math.cos(2 * (ME1 - MS)) - 0.000023 * Math.cos(2 * (MF - ME1) + MD)
                + E * 0.000019 * Math.cos(4 * ME1 - MS - MD);

        return PM;

    }
}
class MoonCalcConstants {
    double ML;
    double MS;
    double MD;
    double ME1;
    double MF;
    double NA;
    double E;
    double C;

    public MoonCalcConstants(ZonedDateTime dateTime) {
        double Q = DateTime.toDublinJulianDate(dateTime);
        double T = Q / 36525d;
        double T2 = T * T;

        double M1 = 27.32158213;
        double M2 = 365.2596407;
        double M3 = 27.55455094;
        double M4 = 29.53058868;
        double M5 = 27.21222039;
        double M6 = 6798.363307;

        M1 = Q / M1;
        M2 = Q / M2;
        M3 = Q / M3;
        M4 = Q / M4;
        M5 = Q / M5;
        M6 = Q / M6;

        M1 = 360 * (M1 - (int)M1);
        M2 = 360 * (M2 - (int)M2);
        M3 = 360 * (M3 - (int)M3);
        M4 = 360 * (M4 - (int)M4);
        M5 = 360 * (M5 - (int)M5);
        M6 = 360 * (M6 - (int)M6);

        this.ML = 270.434164 + M1 - (0.001133 - 0.0000019 * T) * T2;
        this.MS = 358.475833 + M2 - (0.00015 + 0.0000033 * T) * T2;
        this.MD = 296.104608 + M3 + (0.009192 + 0.0000144 * T) * T2;
        this.ME1 = 350.737486 + M4 - (0.001436 - 0.0000019 * T) * T2;
        this.MF = 11.250889 + M5 - (0.003211 + 0.0000003 * T) * T2;
        this.NA = 259.183275 - M6 + (0.002078 + 0.0000022 * T) * T2;
        double A = Math.toRadians(51.2 + 20.2 * T);
        double S1 = Math.sin(A);
        double S2 = Math.sin(Math.toRadians(NA));
        double B = 346.56 + (132.87 - 0.0091731 * T) * T;
        double S3 = 0.003964 * Math.sin(Math.toRadians(B));
        double C = Math.toRadians(NA + 275.05 - 2.3 * T);
        double S4 = Math.sin(C);

        this.ML = this.ML + 0.000233 * S1 + S3 + 0.001964 * S2;
        this.MS = this.MS - 0.001778 * S1;
        this.MD = this.MD + 0.000817 * S1 + S3 + 0.002541 * S2;
        this.MF = this.MF + S3 - 0.024691 * S2 - 0.004328 * S4;
        this.ME1 = this.ME1 + 0.002011 * S1 + S3 + 0.001964 * S2;
        this.E = 1 - (0.002495 + 0.00000752 * T) * T;
        this.C = Math.toRadians(NA + 275.05 - 2.3 * T);
   }
}