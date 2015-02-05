package com.predictiveHunting.Astronomy.Coordinates;

import com.predictiveHunting.Astronomy.Angle;

public class Ecliptic {

    class CelestialCoordinates implements ICelestialCoordinates {
        private Ecliptic coordinates;
        protected CelestialCoordinates(Ecliptic coordinates) {
            this.coordinates = coordinates;
        }
        public double getHorizontalAngle() {
            return this.coordinates.longitude.toDecimal();
        }
        public void setHorizontalAngle(double horizontalAngle) {
            this.coordinates.longitude = Angle.fromDegrees(horizontalAngle);
        }
        public double getVerticalAngle() {
            return this.coordinates.latitude.toDecimal();
        }
        public void setVerticalAngle(double verticalAngle) {
            this.coordinates.latitude = Angle.fromDegrees(verticalAngle);
        }
    }

    public Ecliptic() {
        this(new Angle(), new Angle());
    }
    public Ecliptic(Angle longitude, Angle latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }
    public Angle longitude;
    public Angle latitude;

    @Override public String toString() {
        return String.format("longitude=%s, latitude=%s", this.longitude.toString(), this.latitude.toString());
    }
    public ICelestialCoordinates asCelestialCoordinates() {
        return new CelestialCoordinates(this);
    }

}
