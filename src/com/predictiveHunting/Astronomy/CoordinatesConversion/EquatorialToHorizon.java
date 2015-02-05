package com.predictiveHunting.Astronomy.CoordinatesConversion;

import com.predictiveHunting.Astronomy.Coordinates.EquatorialRightAscension;
import com.predictiveHunting.Astronomy.Coordinates.Horizon;

public class EquatorialToHorizon {
    public static Horizon convert(EquatorialRightAscension coordinates, double latitude) {
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
                    Math.cos(latitudeRadians)
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
