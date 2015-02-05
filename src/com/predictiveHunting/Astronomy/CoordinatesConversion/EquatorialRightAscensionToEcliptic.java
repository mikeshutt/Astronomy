package com.predictiveHunting.Astronomy.CoordinatesConversion;

import com.predictiveHunting.Astronomy.Coordinates.*;
import com.predictiveHunting.Astronomy.DateTime;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class EquatorialRightAscensionToEcliptic {
    public static Ecliptic convert(EquatorialRightAscension coordinates, LocalTime localSiderealTime, LocalDate utcDate, double latitude) {
        double obliquityOfEcliptic = Constants.meanObliquityOfEcliptic(DateTime.localSiderealTimeToUtc(LocalDateTime.of(utcDate, localSiderealTime), latitude));
        return convert(coordinates, obliquityOfEcliptic);
    }
    public static Ecliptic convert(EquatorialRightAscension coordinates, double obliquityOfTheElliptic) {
        double[] sourceCoordinatesVector = Matrix.coordinatesToColumnVector(coordinates.asCelestialCoordinates());
        double[] convertedVector = Matrix.transform(sourceCoordinatesVector, getTransformMatrix(obliquityOfTheElliptic));
        Ecliptic convertedCoordinates = new Ecliptic();
        Matrix.columnVectorToCoordinates(convertedVector, convertedCoordinates.asCelestialCoordinates());
        return convertedCoordinates;
    }

    private static double[][] getTransformMatrix(double obliquityOfEcliptic) {
        double obliquityOfEclipticRadians = Math.toRadians(obliquityOfEcliptic);
        return new double[][] {
                {
                        1,
                        0,
                        0
                },
                {
                        0,
                        Math.cos(obliquityOfEclipticRadians),
                        Math.sin(obliquityOfEclipticRadians)
                },
                {
                        0,
                        -Math.sin(obliquityOfEclipticRadians),
                        Math.cos(obliquityOfEclipticRadians)
                }
        };
    }

}
