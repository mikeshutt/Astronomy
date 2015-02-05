package com.predictiveHunting.Astronomy.CoordinatesConversion;

import com.predictiveHunting.Astronomy.Angle;
import com.predictiveHunting.Astronomy.Coordinates.ICelestialCoordinates;
import com.predictiveHunting.Helpers;

/**
 * Created by Mike on 1/12/2015.
 */
public class Matrix {

    public static double[] transform(double[] sourceCoordinates, double[][] transformMatrix) {
        double[] result = new double[3];

        result[0] =
                transformMatrix[0][0] * sourceCoordinates[0] +
                transformMatrix[0][1] * sourceCoordinates[1] +
                transformMatrix[0][2] * sourceCoordinates[2];

        result[1] =
                transformMatrix[1][0] * sourceCoordinates[0] +
                transformMatrix[1][1] * sourceCoordinates[1] +
                transformMatrix[1][2] * sourceCoordinates[2];

        result[2] =
                transformMatrix[2][0] * sourceCoordinates[0] +
                transformMatrix[2][1] * sourceCoordinates[1] +
                transformMatrix[2][2] * sourceCoordinates[2];

        return result;

    }

    public static double[] coordinatesToColumnVector(ICelestialCoordinates coordinates) {
        double horizontalRadians = Math.toRadians(coordinates.getHorizontalAngle());
        double verticalRadians = Math.toRadians(coordinates.getVerticalAngle());
        return new double[] {
                Math.cos(horizontalRadians) * Math.cos(verticalRadians),
                Math.sin(horizontalRadians) * Math.cos(verticalRadians),
                Math.sin(verticalRadians)
        };
    }

    public static void columnVectorToCoordinates(double[] columnVector, ICelestialCoordinates coordinates) {
        double horizontalAngle = Math.toDegrees(Math.atan2(columnVector[1], columnVector[0]));
        coordinates.setHorizontalAngle(horizontalAngle);
        coordinates.setVerticalAngle(Math.toDegrees(Math.asin(columnVector[2])));
    }

}
