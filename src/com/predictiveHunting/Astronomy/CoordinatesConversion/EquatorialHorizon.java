package com.predictiveHunting.Astronomy.CoordinatesConversion;

import com.predictiveHunting.Astronomy.Angle;
import com.predictiveHunting.Astronomy.Coordinates.EquatorialRightAscension;
import com.predictiveHunting.Astronomy.Coordinates.Horizon;
import com.predictiveHunting.Astronomy.Coordinates.ICelestialCoordinates;

public class EquatorialHorizon {

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
                Math.sin(verticalRadians) * Math.sin(latitudeRadians) + Math.cos(verticalRadians) * Math.cos(latitudeRadians) * Math.cos(horizontalRadians);
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
