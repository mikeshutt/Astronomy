package com.predictiveHunting;

import com.predictiveHunting.Astronomy.*;
import com.predictiveHunting.Astronomy.Coordinates.*;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Main {

    public static void main(String[] args) {

        /*
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        do {
            try {
                System.out.println("Enter the date YYYY-MM-DD followed by the longitude,latitude of the location");
                String inputValue = reader.readLine();
                if (inputValue.equals("") || inputValue.equalsIgnoreCase("exit")) {
                    break;
                }
                String[] parts = inputValue.split("-");
                LocalDate date = LocalDate.of(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));

                inputValue = reader.readLine();
                if (inputValue.equals("") || inputValue.equalsIgnoreCase("exit")) {
                    break;
                }
                parts = inputValue.split(",");
                Geographic observationCoordinates = new Geographic(
                        new Angle(Double.parseDouble(parts[0])),
                        new Angle(Double.parseDouble(parts[1]))

                );

                ZonedDateTime midday = ZonedDateTime.of(date, LocalTime.of(12, 0), ZoneId.of("America/New_York"));
                Ecliptic middaySunPositionEcliptic = new Ecliptic(Sun.getEclipticLongitude(midday), new Angle(0));
                System.out.println("Midday sun position ecliptic -> " + middaySunPositionEcliptic.toString());

                EquatorialRightAscension middaySunPositionRightAscension = EclipticToEquatorialRightAscension.convert(middaySunPositionEcliptic, midday);
                System.out.println("Midday sun position equatorial (right ascension) -> " + middaySunPositionRightAscension.toString());

                EquatorialHourAngle middaySunPositionHourAngle =
                        new EquatorialHourAngle(
                                HourAngle.fromAngle(middaySunPositionRightAscension.rightAscension),
                                middaySunPositionRightAscension.declination
                        );
                System.out.println("Midday sun position equatorial (hour angle) -> " + middaySunPositionHourAngle.toString());

                RiseAndSetTimes timesUtc = StarRiseAndSet.getRiseAndSetTimes(middaySunPositionHourAngle, date, observationCoordinates);
                RiseAndSetTimes timesLocal = new RiseAndSetTimes(
                        ZonedDateTime.ofInstant(timesUtc.rise.toInstant(), ZoneId.of("America/New_York")),
                        ZonedDateTime.ofInstant(timesUtc.set.toInstant(), ZoneId.of("America/New_York"))
                );

                System.out.println(timesLocal.toString());

                //System.out.println(DateTime.greenwichSiderealTimeToLocalSiderealTime(utc, longitude));

            } catch (IOException e) {
                break;
            }
        } while (true);
        */

        /*
        Geographic location = new Geographic(new Angle(-149.855), new Angle(61.217));
        LocalDate date = LocalDate.of(2015, 2, 3);
        System.out.println(Moon.riseAndSet(date, ZoneId.of("America/Anchorage"), location));
        */

        TreeMap<LocalDate, RiseAndSetTimesLocalTime> riseAndSetTimes = new TreeMap<LocalDate, RiseAndSetTimesLocalTime>();

        Geographic location = new Geographic(new Angle(-149.8666667), new Angle(61.21666667));
        LocalDate date = LocalDate.of(2015, 1, 1);
        ZoneId timeZone = ZoneId.of("America/Anchorage");
        do {
            RiseAndSetTimesUtc riseAndSet = Moon.riseAndSet(date, timeZone, location);

            LocalDate riseDate = riseAndSet.rise.toLocalDate();
            RiseAndSetTimesLocalTime riseAndSetTime = riseAndSetTimes.get(riseDate);
            if (riseAndSetTime == null) {
                riseAndSetTime = new RiseAndSetTimesLocalTime();
                riseAndSetTimes.put(riseDate, riseAndSetTime);
            }
            if (riseAndSetTime.rise == null) {
                riseAndSetTime.rise = riseAndSet.rise.toLocalTime();
            }

            LocalDate setDate = riseAndSet.set.toLocalDate();
            riseAndSetTime = riseAndSetTimes.get(setDate);
            if (riseAndSetTime == null) {
                riseAndSetTime = new RiseAndSetTimesLocalTime();
                riseAndSetTimes.put(setDate, riseAndSetTime);
            }
            if (riseAndSetTime.set == null) {
                riseAndSetTime.set = riseAndSet.set.toLocalTime();
            }

            date = date.plusDays(1);
        } while (date.getMonthValue() == 1);

        System.out.println("Date\tSunrise\tSunset\tMoonrise\tMoonset\tMoon Phase");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a");
        Set<LocalDate> dates = riseAndSetTimes.keySet();
        //SortedSet<LocalDate> dateList = ;
        for (LocalDate dateKey: dates){
            System.out.print(dateKey);
            System.out.print("\t");

            RiseAndSetTimesUtc sunRiseAndSet = Sun.sunRiseAndSet(dateKey, timeZone, location);
            System.out.print(sunRiseAndSet.rise.format(timeFormatter));
            System.out.print("\t");
            System.out.print(sunRiseAndSet.set.format(timeFormatter));
            System.out.print("\t");

            RiseAndSetTimesLocalTime riseAndSetTime = riseAndSetTimes.get(dateKey);
            if (riseAndSetTime.rise != null) {
                System.out.print(riseAndSetTime.rise.format(timeFormatter));
            }
            else {
                System.out.print("none");
            }
            System.out.print("\t");
            if (riseAndSetTime.set != null) {
                System.out.print(riseAndSetTime.set.format(timeFormatter));
            }
            else {
                System.out.print("none");
            }
            System.out.print("\t");
            System.out.print(Moon.phase(ZonedDateTime.of(dateKey, LocalTime.of(12, 0), timeZone)));
            System.out.println();
        }

        /*
        LocalDate date = LocalDate.of(2015, 1, 15);
        Geographic location = new Geographic(new Angle(-84.473326), new Angle(34.032558));
        LocalTime sunrise = Sun.sunRiseAndSet(date, ZoneId.of("America/New_York"), location);

        System.out.println(date);
        System.out.println(location);
        System.out.println(sunrise);
        */

        /*
        try {
            System.in.read();
        }
        catch (IOException e) {

        }
        */
    }
}
