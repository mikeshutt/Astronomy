package com.predictiveHunting.Astronomy.Coordinates;

import com.predictiveHunting.Astronomy.Angle;

public class Geographic {

    private Angle longitude;
    private Angle latitude;

    public Geographic() {
        this(new Angle(), new Angle());
    }
    public Geographic(Angle longitude, Angle latitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Angle getLongitude() {
        return longitude;
    }

    @Override public String toString() {
        return String.format("longitude=%s, latitude=%s", this.longitude.toString(), this.latitude.toString());
    }

    public Angle getLatitude() {
        return latitude;
    }
}

