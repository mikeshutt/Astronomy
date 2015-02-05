package com.predictiveHunting.Astronomy.CoordinatesConversion;

import com.predictiveHunting.Astronomy.Coordinates.*;
import com.predictiveHunting.Astronomy.DateTime;

import java.time.LocalTime;

public class EquatorialHourAngleToRightAscension {
    public static EquatorialRightAscension convert(EquatorialHourAngle coordinates, LocalTime localSiderealTime) {
        double[] sourceCoordinatesVector = Matrix.coordinatesToColumnVector(coordinates.asCelestialCoordinates());
        double siderealDegrees = DateTime.timeToDecimalHours(localSiderealTime) * 15d;
        double[] convertedVector = Matrix.transform(sourceCoordinatesVector, getTransformMatrix(siderealDegrees));
        EquatorialRightAscension convertedCoordinates = new EquatorialRightAscension();
        Matrix.columnVectorToCoordinates(convertedVector, convertedCoordinates.asCelestialCoordinates());
        return convertedCoordinates;
    }

    private static double[][] getTransformMatrix(double siderealTime) {
        double siderealTimeRadians = Math.toRadians(siderealTime);
        return new double[][] {
                {
                        Math.cos(siderealTimeRadians),
                        Math.sin(siderealTimeRadians),
                        0
                },
                {
                        Math.sin(siderealTimeRadians),
                        -Math.cos(siderealTimeRadians),
                        0
                },
                {
                        0,
                        0,
                        1
                }
        };
    }

}
