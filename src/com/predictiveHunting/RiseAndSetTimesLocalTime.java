package com.predictiveHunting;

import java.time.LocalTime;

public class RiseAndSetTimesLocalTime {

    public LocalTime rise;
    public LocalTime set;

    public RiseAndSetTimesLocalTime() { }
    public RiseAndSetTimesLocalTime(LocalTime rise, LocalTime set) {
        this.rise = rise;
        this.set = set;
    }

    @Override public String toString() {
        return String.format(
                "Rise -> %s, Set -> %s)",
                (this.rise == null) ? null : this.rise.toString(),
                (this.set == null) ? null : this.set.toString()
        );
    }

}
