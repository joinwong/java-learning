package com.yw.learning.datetime;

import java.time.*;

public class Application {

    private final static Clock clock = Clock.systemUTC();

    public static void main(String... args) {

        System.out.println(clock.instant());
        System.out.println(clock.millis());
        System.out.println();

        //日期:默认时区
        System.out.println(LocalDate.now());
        System.out.println(LocalDate.now(clock));
        System.out.println();

        //时间：默认时区
        System.out.println(LocalTime.now());
        System.out.println(LocalTime.now(clock));
        System.out.println();

        System.out.println(LocalDateTime.now());
        System.out.println(LocalDateTime.now(clock));
        System.out.println();

        //带时区
        System.out.println(ZonedDateTime.now());
        System.out.println(ZonedDateTime.now(clock));
        System.out.println(ZonedDateTime.now(ZoneId.of("America/Los_Angeles")));
        System.out.println();

        //计算时间差
        final LocalDateTime from = LocalDateTime.of(2017,1,1,1,0);
        final LocalDateTime to = LocalDateTime.of(2018,2,2,1,0);
        final Duration duration = Duration.between(from,to);

        System.out.println(duration.toDays() + " days");
        System.out.println(duration.toHours() + " hours");

    }
}
