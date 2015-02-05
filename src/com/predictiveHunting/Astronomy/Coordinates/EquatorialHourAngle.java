package com.predictiveHunting.Astronomy.Coordinates;

import com.predictiveHunting.Astronomy.Angle;
import com.predictiveHunting.Astronomy.HourAngle;

/**
 * Created by Mike on 1/12/2015.
 */
public class EquatorialHourAngle {
    class CelestialCoordinates implements ICelestialCoordinates {
        private EquatorialHourAngle coordinates;
        protected CelestialCoordinates(EquatorialHourAngle coordinates) {
            this.coordinates = coordinates;
        }
        public double getHorizontalAngle() {
            return this.coordinates.hourAngle.toDecimalHours();
        }
        public void setHorizontalAngle(double horizontalAngle) {
            this.coordinates.hourAngle = new HourAngle(horizontalAngle);
        }
        public double getVerticalAngle() {
            return this.coordinates.declination.toDecimal();
        }
        public void setVerticalAngle(double verticalAngle) {
            this.coordinates.declination = Angle.fromDegrees(verticalAngle);
        }
    }

    public HourAngle hourAngle;
    public Angle declination;

    public EquatorialHourAngle()  {
        this(new HourAngle(), new Angle());
    }
    public EquatorialHourAngle(HourAngle hourAngle, Angle declination) {
        this.hourAngle = hourAngle;
        this.declination = declination;
    }

    /*
    public static Equatorial fromHourAngle(HourAngle ascension, Angle declination) {
        return new Equatorial(ascension.toAngle(), declination);
    }
    public static Equatorial fromHourAngle(double ascension, Angle declination) {
        return new Equatorial(HourAngle.fromDecimalHours(ascension).toAngle(), declination);
    }
    */

    @Override public String toString() {
        return String.format("ascension=%s, declination=%s", this.hourAngle.toString(), this.declination.toString());
    }
    public ICelestialCoordinates asCelestialCoordinates() {
        return new CelestialCoordinates(this);
    }

}
