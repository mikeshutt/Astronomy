package com.predictiveHunting.Astronomy.CoordinatesConversion;

import com.predictiveHunting.Astronomy.Angle;
import com.predictiveHunting.Astronomy.Coordinates.Ecliptic;
import com.predictiveHunting.Astronomy.Coordinates.EquatorialRightAscension;
import com.predictiveHunting.Astronomy.DateTime;
import com.predictiveHunting.Helpers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;

public class EclipticToEquatorialRightAscension {
    public static EquatorialRightAscension convert(Ecliptic coordinates, LocalTime localSiderealTime, LocalDate utcDate, double latitude) {
        double obliquityOfEcliptic = Constants.meanObliquityOfEcliptic(DateTime.localSiderealTimeToUtc(LocalDateTime.of(utcDate, localSiderealTime), latitude));
        return convert(coordinates, obliquityOfEcliptic);
    }
    public static EquatorialRightAscension convert(Ecliptic coordinates, ZonedDateTime dateTime) {
        double obliquityOfEcliptic = Constants.meanObliquityOfEcliptic(dateTime);
        return convert(coordinates, obliquityOfEcliptic);
    }
    public static EquatorialRightAscension convert(Ecliptic coordinates, double obliquityOfTheElliptic) {

        double obliquityOfTheEllipticRadians = Math.toRadians(obliquityOfTheElliptic);
        double sourceLongitudeRadians = Math.toRadians(coordinates.longitude.toDecimal());
        double sourceLatitudeRadians = Math.toRadians(coordinates.latitude.toDecimal());

        double x = Math.cos(sourceLongitudeRadians);
        double y = Math.sin(sourceLongitudeRadians) * Math.cos(obliquityOfTheEllipticRadians) - Math.tan(sourceLatitudeRadians) * Math.sin(obliquityOfTheEllipticRadians);
        double rightAscensionRadians = Math.atan2(y, x);

        double declinationRadians = Math.asin(
                Math.sin(sourceLatitudeRadians) * Math.cos(obliquityOfTheEllipticRadians) + Math.cos(sourceLatitudeRadians) * Math.sin(obliquityOfTheEllipticRadians) * Math.sin(sourceLongitudeRadians)
        );

        return new EquatorialRightAscension(

                new Angle(Helpers.bringIntoRange(Math.toDegrees(rightAscensionRadians), 360)),
                new Angle(Math.toDegrees(declinationRadians))
                );
    }
    /*
    public static EquatorialRightAscension convert(Ecliptic coordinates, double obliquityOfTheElliptic) {
        double[] sourceCoordinatesVector = Matrix.coordinatesToColumnVector(coordinates.asCelestialCoordinates());
        double[] convertedVector = Matrix.transform(sourceCoordinatesVector, getTransformMatrix(obliquityOfTheElliptic));
        EquatorialRightAscension convertedCoordinates = new EquatorialRightAscension();
        Matrix.columnVectorToCoordinates(convertedVector, convertedCoordinates.asCelestialCoordinates());
        return convertedCoordinates;
    }
    */
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
                        -Math.sin(obliquityOfEclipticRadians)
                },
                {
                        0,
                        Math.sin(obliquityOfEclipticRadians),
                        Math.cos(obliquityOfEclipticRadians)
                }
        };
    }
}
