package com.predictiveHunting.Astronomy;

import com.predictiveHunting.Astronomy.Coordinates.EquatorialHourAngle;
import com.predictiveHunting.Astronomy.Coordinates.Geographic;
import com.predictiveHunting.Helpers;
import com.predictiveHunting.RiseAndSetTimesLocalTime;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class StarRiseAndSet {
    // verticalShift is in radians
    public static RiseAndSetTimesLocalTime getRiseAndSetTimesLst(EquatorialHourAngle starCoordinates, LocalDate date, double verticalShift, Geographic observationCoordinates) {

        double declinationDecimalDegrees = starCoordinates.declination.toDecimal();
        double declinationRadians = Math.toRadians(declinationDecimalDegrees);
        double latitudeRadians = Math.toRadians(observationCoordinates.getLatitude().toDecimal());
        double cosH = - ( Math.sin(verticalShift) + Math.sin(latitudeRadians) * Math.sin(declinationRadians) ) / (Math.cos(latitudeRadians) * Math.cos(declinationRadians));
        if (Math.abs(cosH) > 1) {
            return new RiseAndSetTimesLocalTime(); // star has no rise or set (circumpolar or never comes above horizon)
        }
        double H_radians = Math.acos(cosH);
        double H = HourAngle.fromAngle(Math.toDegrees(H_radians)).toDecimalHours();

        double riseTimeHours = Helpers.bringIntoRange(starCoordinates.hourAngle.toDecimalHours() - H, 24);
        double setTimeHours = Helpers.bringIntoRange(starCoordinates.hourAngle.toDecimalHours() + H, 24);

        LocalDateTime riseLst = LocalDateTime.of(date, DateTime.decimalHoursToTime(riseTimeHours));
        LocalDateTime setLst = LocalDateTime.of(date, DateTime.decimalHoursToTime(setTimeHours));

        /* azimuth, for later
        double cosA = ( Math.sin(declinationRadians) + Math.sin(verticalShift) * Math.sin(latitudeRadians) ) / ( Math.cos(verticalShift) * Math.cos(latitudeRadians) );
        double Ar = Helpers.bringIntoRange(Math.toDegrees(Math.acos(cosA)), 360);
        double As = 360 - Ar;
        */

        return new RiseAndSetTimesLocalTime(riseLst.toLocalTime(), setLst.toLocalTime());
    }
    public static RiseAndSetTimesUtc getRiseAndSetTimesUtc(EquatorialHourAngle starCoordinates, LocalDate date, double verticalShift, Geographic observationCoordinates) {
        RiseAndSetTimesLocalTime riseAndSetLst = getRiseAndSetTimesLst(starCoordinates, date, verticalShift, observationCoordinates);
        double observationLongitudeDegrees = observationCoordinates.getLongitude().toDecimal();
        return new RiseAndSetTimesUtc(
                DateTime.localSiderealTimeToUtc(LocalDateTime.of(date, riseAndSetLst.rise), observationLongitudeDegrees),
                DateTime.localSiderealTimeToUtc(LocalDateTime.of(date, riseAndSetLst.set), observationLongitudeDegrees)
        );
    }

    /*
    private static void SunRiseLCT(double LD, double LM, double LY, double DS, double ZC, double GL, double GP) {
        String status;

        double DI = 0.8333333;
        double GD = LctGDay(12, 0, 0, DS, ZC, LD, LM, LY);
        double GM = LctGMonth(12, 0, 0, DS, ZC, LD, LM, LY);
        double GY = LctGYear(12, 0, 0, DS, ZC, LD, LM, LY);
        double SR = SunLong(12, 0, 0, DS, ZC, LD, LM, LY);
        double X, XX;
        GoSub 3710

        if (status <> "OK"){
            XX = -99;
            GoTo 3740
        }

        X = LSTGST(utc, 0, 0, GL);
        double UT = GSTUT(X, 0, 0, GD, GM, GY);

        if (isGstUtcAmbiguity(X, 0, 0, GD, GM, GS) <> "OK") {
            XX = -99;
            GoTo 3740
        }

        double SR = SunLong(UT, 0, 0, 0, 0, GD, GM, GY);
        GoSub 3710

        if (status <> "OK"){
            XX = -99;
            GoTo 3740
        }

        double X = LSTGST(utc, 0, 0, GL);
        UT = GSTUT(X, 0, 0, GD, GM, GY);
        double XX = UTLct(UT, 0, 0, DS, ZC, GD, GM, GY);
        GoTo 3740

        3710
        double A = SR + NutatLong(GD, GM, GY) - 0.005694;
        X = ECRA(A, 0, 0, 0, 0, 0, GD, GM, GY);
        double Y = ECDec(A, 0, 0, 0, 0, 0, GD, GM, GY);
        double utc = RSLSTR(DDDH(X), 0, 0, Y, 0, 0, DI, GP);
        status = eRS(DDDH(X), 0, 0, Y, 0, 0, DI, GP);

        3740
        SunriseLCT = XX

    }
    private
    */
}
