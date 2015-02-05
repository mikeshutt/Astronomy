package com.predictiveHunting.Astronomy.CoordinatesConversion;

import com.predictiveHunting.Astronomy.Coordinates.Ecliptic;
import com.predictiveHunting.Astronomy.Coordinates.EquatorialHourAngle;
import com.predictiveHunting.Astronomy.Coordinates.EquatorialRightAscension;
import com.predictiveHunting.Astronomy.DateTime;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

public class EquatorialRightAscensionToHourAngle {
    public static EquatorialHourAngle convert(EquatorialRightAscension coordinates, LocalTime localSiderealTime) {
        double[] sourceCoordinatesVector = Matrix.coordinatesToColumnVector(coordinates.asCelestialCoordinates());
        double siderealDegrees = DateTime.timeToDecimalHours(localSiderealTime) * 15d;
        double[] convertedVector = Matrix.transform(sourceCoordinatesVector, getTransformMatrix(siderealDegrees));
        EquatorialHourAngle convertedCoordinates = new EquatorialHourAngle();
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
