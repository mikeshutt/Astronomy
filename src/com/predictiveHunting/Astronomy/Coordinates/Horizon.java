package com.predictiveHunting.Astronomy.Coordinates;

import com.predictiveHunting.Astronomy.Angle;

public class Horizon {

    public Angle altitude;
    public Angle azimuth;

    class CelestialCoordinates implements ICelestialCoordinates {
        private Horizon coordinates;
        protected CelestialCoordinates(Horizon coordinates) {
            this.coordinates = coordinates;
        }
        public double getHorizontalAngle() {
            return this.coordinates.azimuth.toDecimal();
        }
        public void setHorizontalAngle(double horizontalAngle) {
            this.coordinates.azimuth = Angle.fromDegrees(horizontalAngle);
        }
        public double getVerticalAngle() {
            return this.coordinates.altitude.toDecimal();
        }
        public void setVerticalAngle(double verticalAngle) {
            this.coordinates.altitude = Angle.fromDegrees(verticalAngle);
        }
    }

    public Horizon() {
        this(new Angle(), new Angle());
    }

    public Horizon(Angle altitude, Angle azimuth) {
        this.altitude = altitude;
        this.azimuth = azimuth;
    }
    public Horizon(double altitude, double azimuth) {
        this.altitude = new Angle(altitude);
        this.azimuth = new Angle(azimuth);
    }

    @Override public String toString() {
        return String.format("altitude=%s, azimuth=%s", this.altitude.toString(), this.azimuth.toString());
    }

    public ICelestialCoordinates asCelestialCoordinates() {
        return new CelestialCoordinates(this);
    }

}
