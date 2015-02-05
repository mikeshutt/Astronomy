package com.predictiveHunting.Astronomy.CoordinatesConversion;

import com.predictiveHunting.Astronomy.Angle;
import com.predictiveHunting.Astronomy.Coordinates.*;

public class EquatorialHourAngleToHorizon {

    public static Horizon convert(EquatorialHourAngle coordinates, Angle latitude) {
        return convert(coordinates, latitude.toDecimal());
    }

    public static Horizon convert(EquatorialHourAngle coordinates, double latitude) {
        double[] sourceCoordinatesVector = Matrix.coordinatesToColumnVector(coordinates.asCelestialCoordinates());
        double[] convertedVector = Matrix.transform(sourceCoordinatesVector, getTransformMatrix(latitude));
        Horizon convertedCoordinates = new Horizon();
        Matrix.columnVectorToCoordinates(convertedVector, convertedCoordinates.asCelestialCoordinates());
        return convertedCoordinates;
    }

    private static double[][] getTransformMatrix(double latitude) {
        double latitudeRadians = Math.toRadians(latitude);
        return new double[][] {
                {
                        -Math.sin(latitudeRadians),
                        0,
                        Math.cos(latitudeRadians),
                },
                {
                        0,
                        -1,
                        0
                },
                {
                        Math.cos(latitudeRadians),
                        0,
                        Math.sin(latitudeRadians)
                }
        };
    }

}
