package com.predictiveHunting.Astronomy.Coordinates;

public abstract class CelestialBase {

    public abstract double getRadialDistance();
    public abstract void setRadialDistance(double radialDistance);

    public abstract double getPolarAngle();
    public abstract void setPolarAngle(double polarAngle);

    public abstract double getAzimuthalAngle();
    public abstract void setAzimuthalAngle(double azimuthalAngle);
}
