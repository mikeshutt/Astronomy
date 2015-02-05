package com.predictiveHunting.Astronomy;

import com.predictiveHunting.Astronomy.Coordinates.*;
import java.time.*;

public class Convert {
    public static HourAngle rightAscensionToHourAngle(HourAngle rightAscension, double longitude, ZonedDateTime localTime) {
        ZonedDateTime utcDateTime = DateTime.toUtc(localTime);
        LocalTime gst = DateTime.utcToGreenwichSiderealTime(utcDateTime);
        LocalTime lst = DateTime.greenwichSiderealTimeToLocalSiderealTime(gst, longitude);
        double hourAngleDecimal = DateTime.timeToDecimalHours(lst) - rightAscension.toDecimalHours();
        if (hourAngleDecimal < 0) {
            hourAngleDecimal += 24;
        }
        return HourAngle.fromDecimalHours(hourAngleDecimal);
    }
    public static HourAngle hourAngleToRightAscension(HourAngle hourAngle, double longitude, ZonedDateTime localTime) {
        // same exact logic, but want 2 signatures to avoid confusion
        return rightAscensionToHourAngle(hourAngle, longitude, localTime);
    }

    public static Horizon equatorialToHorizonCoordinates(EquatorialRightAscension equatorialCoordinates, double latitude) {
        Horizon horizon = new Horizon();
        equatorialHorizonConverter(equatorialCoordinates.asCelestialCoordinates(), horizon.asCelestialCoordinates(), latitude);
        return horizon;
    }

    public static EquatorialRightAscension horizonToEquatorialCoordinates(Horizon horizonCoordinates, double latitude) {
        EquatorialRightAscension equatorial = new EquatorialRightAscension();
        equatorialHorizonConverter(horizonCoordinates.asCelestialCoordinates(), equatorial.asCelestialCoordinates(), latitude);
        return equatorial;
    }
    private static void equatorialHorizonConverter(ICelestialCoordinates sourceCoordinates, ICelestialCoordinates destinationCoordinates, double latitude) {
        double horizontalDegrees = sourceCoordinates.getHorizontalAngle();
        double verticalDegrees = sourceCoordinates.getVerticalAngle();
        double horizontalRadians = Math.toRadians(horizontalDegrees);
        double verticalRadians = Math.toRadians(verticalDegrees);
        double latitudeRadians = Math.toRadians(latitude);

        double sinOfNewVertical =
                Math.sin(verticalRadians) * Math.sin(latitudeRadians)
                +
                Math.cos(verticalRadians) * Math.cos(latitudeRadians) * Math.cos(horizontalRadians);
        double newVertical = Math.asin(sinOfNewVertical);

        double cosOfNewHorizontal =
                (Math.sin(verticalRadians) - (Math.sin(latitudeRadians) * Math.sin(newVertical))) / (Math.cos(latitudeRadians) * Math.cos(newVertical));
        double newHorizontal = Math.acos(cosOfNewHorizontal);

        if (Math.sin(horizontalRadians) >= 0) {
            newHorizontal = Math.PI * 2 - newHorizontal;
        }

        destinationCoordinates.setHorizontalAngle(Math.toDegrees(newHorizontal));
        destinationCoordinates.setVerticalAngle(Math.toDegrees(newVertical));
    }

}
