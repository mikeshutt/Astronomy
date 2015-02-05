package com.predictiveHunting.Astronomy.Coordinates;

import com.predictiveHunting.Astronomy.Angle;

public class EquatorialRightAscension {

    class CelestialCoordinates implements ICelestialCoordinates {
        private EquatorialRightAscension coordinates;
        protected CelestialCoordinates(EquatorialRightAscension coordinates) {
            this.coordinates = coordinates;
        }
        public double getHorizontalAngle() {
            return this.coordinates.rightAscension.toDecimal();
        }
        public void setHorizontalAngle(double horizontalAngle) {
            this.coordinates.rightAscension = Angle.fromDegrees(horizontalAngle);
        }
        public double getVerticalAngle() {
            return this.coordinates.declination.toDecimal();
        }
        public void setVerticalAngle(double verticalAngle) {
            this.coordinates.declination = Angle.fromDegrees(verticalAngle);
        }
    }

    public Angle declination;
    public Angle rightAscension;

    public EquatorialRightAscension()  {
        this(new Angle(), new Angle());
    }
    public EquatorialRightAscension(Angle rightAscension, Angle declination) {
        this.rightAscension = rightAscension;
        this.declination = declination;
    }

    @Override public String toString() {
        return String.format("ascension=%s, declination=%s", this.rightAscension.toString(), this.declination.toString());
    }
    public ICelestialCoordinates asCelestialCoordinates() {
        return new CelestialCoordinates(this);
    }
}
