package com.predictiveHunting;

import com.predictiveHunting.Astronomy.DateTime;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class Helpers {
    public static double bringIntoRange(double value, int maxValue) {
        if (value > maxValue) {
            return value % maxValue;
        } else if (value < 0) {
            return maxValue - (Math.abs(value) % maxValue);
        } else {
            return value;
        }
    }
    public static double unwind(double W) {
        double radiansInFullCircle = 2 * Math.PI;
        return (W - radiansInFullCircle * (int)(W / radiansInFullCircle) );
    }

    public static double NutatLong(LocalDate gmtDate) {

        double daysSinceDublinEpoch = DateTime.toDublinJulianDate(ZonedDateTime.of(gmtDate, LocalTime.of(0, 0), ZoneId.of("UTC")));
        double T = daysSinceDublinEpoch / 36525d;
        double T2 = T * T;

        double A, B;

        A = 100.0021358 * T;
        B = 360 * (A - (int)A);
        double L1 = 279.6967 + 0.000303 * T2 + B;
        double L2 = 2d * Math.toRadians(L1);
        A = 1336.855231 * T;
        B = 360 * (A - (int)A);
        double D1 = 270.4342 - 0.001133 * T2 + B;
        double D2 = 2 * Math.toRadians(D1);
        A = 99.99736056 * T;
        B = 360 * (A - (int)A);
        double M1 = 358.4758 - 0.00015 * T2 + B;
        M1 = Math.toRadians(M1);
        A = 1325.552359 * T;
        B = 360 * (A - (int)A);
        double M2 = 296.1046 + 0.009192 * T2 + B;
        M2 = Math.toRadians(M2);
        A = 5.372616667 * T;
        B = 360 * (A - (int)A);
        double N1 = 259.1833 + 0.002078 * T2 - B;
        N1 = Math.toRadians(N1);
        double N2 = 2 * N1;

        double DP =
              (-17.2327 - 0.01737 * T) * Math.sin(N1)
            + (-1.2729 - 0.00013 * T) * Math.sin(L2) + 0.2088 * Math.sin(N2)
            - 0.2037 * Math.sin(D2) + (0.1261 - 0.00031 * T) * Math.sin(M1)
            + 0.0675 * Math.sin(M2) - (0.0497 - 0.00012 * T) * Math.sin(L2 + M1)
            - 0.0342 * Math.sin(D2 - N1) - 0.0261 * Math.sin(D2 + M2)
            + 0.0214 * Math.sin(L2 - M1) - 0.0149 * Math.sin(L2 - D2 + M2)
            + 0.0124 * Math.sin(L2 - N1) + 0.0114 * Math.sin(D2 - M2);

        return DP / 3600d;
    }

    public static boolean isGstUtcAmbiguity(LocalTime gst, LocalDate date) {
        double A = DateTime.toJulianDate(ZonedDateTime.of(date, LocalTime.of(0, 0), ZoneId.of("UTC")));
        double B = A - 2451545d;
        double C = B / 36525d;
        double D = 6.697374558 + (2400.051336 * C) + (0.000025862 * C * C);
        double E = D - (24 * (int)(D / 24d));
        double F = DateTime.timeToDecimalHours(gst);
        double G = F - E;
        double H = Helpers.bringIntoRange(G, 24); // G - (24d * (int)(G / 24d));
        if ((H * 0.9972695663) < (4 / 60)) {
            return true;
        }
        else {
            return false;
        }
    }

}
