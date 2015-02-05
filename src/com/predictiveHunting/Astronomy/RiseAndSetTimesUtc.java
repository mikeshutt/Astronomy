package com.predictiveHunting.Astronomy;

import java.time.ZonedDateTime;

public class RiseAndSetTimesUtc {
    public ZonedDateTime rise;
    public ZonedDateTime set;

    public RiseAndSetTimesUtc() { }
    public RiseAndSetTimesUtc(ZonedDateTime rise, ZonedDateTime set) {
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
