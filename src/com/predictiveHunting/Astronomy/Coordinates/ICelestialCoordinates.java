package com.predictiveHunting.Astronomy.Coordinates;

import com.predictiveHunting.Astronomy.Angle;

public interface ICelestialCoordinates {
    /*
    public abstract double getRadialDistance();
    public abstract void setRadialDistance(double radialDistance);
    */

    public abstract double getHorizontalAngle();
    public abstract void setHorizontalAngle(double polarAngle);

    public abstract double getVerticalAngle();
    public abstract void setVerticalAngle(double verticalAngle);
}
