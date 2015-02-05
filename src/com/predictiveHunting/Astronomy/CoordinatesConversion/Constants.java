package com.predictiveHunting.Astronomy.CoordinatesConversion;

import com.predictiveHunting.Astronomy.DateTime;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

public class Constants {
    public static double meanObliquityOfEcliptic_old(ZonedDateTime dateTime) {
        double julianDays = DateTime.toJulianDate(dateTime);
        double daysSinceEpoch = julianDays - 2451545d;
        double T = daysSinceEpoch / 36525d;
        double DE = 46.815 * T + .0006 * Math.pow(T ,2) + .00181 * Math.pow(T ,3);
        DE = DE / 3600d;
        double epsilon = 23.439292 - DE;
        return epsilon;
    }
    public static double meanObliquityOfEcliptic(ZonedDateTime dateTime) {
        //double julianDays = DateTime.toJulianDate(dateTime);
        double julianDays = DateTime.toJulianDate(dateTime.withZoneSameInstant(ZoneId.of("UTC")).truncatedTo(ChronoUnit.DAYS));
        double daysSinceEpoch = julianDays - 2415020d;
        double C = (daysSinceEpoch / 36525d) - 1;
        double D = C * (46.815 + C * (0.0006 - (C * 0.00181)));
        double E = D / 3600d;
        return 23.43929167 - E + NutatObl((daysSinceEpoch / 36525d));

        /*
        double T = daysSinceEpoch / 36525d;
        double DE = 46.815 * T + .0006 * Math.pow(T ,2) + .00181 * Math.pow(T ,3);
        DE = DE / 3600d;
        double epsilon = 23.43929167 - DE + NutatObl(T);
        return epsilon;
        */
    }

    private static double NutatObl(double T) {

        double T2 = T * T;

        double A, B;
        
        A = 100.0021358 * T;
        B = 360 * (A - (int)A);
        double L1 = 279.6967 + 0.000303 * T2 + B;
        double l2 = 2 * Math.toRadians(L1);
        
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

        double DDO = 
                  (9.21 + 0.00091 * T) * Math.cos(N1)
                + (0.5522 - 0.00029 * T) * Math.cos(l2) - 0.0904 * Math.cos(N2)
                          + 0.0884 * Math.cos(D2) + 0.0216 * Math.cos(l2 + M1)
                          + 0.0183 * Math.cos(D2 - N1) + 0.0113 * Math.cos(D2 + M2)
                          - 0.0093 * Math.cos(l2 - M1) - 0.0066 * Math.cos(l2 - N1);

        return DDO / 3600d;
    }
}
