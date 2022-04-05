package pl.lotto.configuration;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.TimeZone;

public class TimeConfiguration {
    private static Clock CLOCK = Clock.systemDefaultZone();
    private static final TimeZone DEFAULT_TIME_ZONE = TimeZone.getDefault();
    private static TimeZone REAL_TIME_ZONE = TimeZone.getDefault();;
    
    public static LocalDate currentDate() {
        return LocalDate.now(getClock());
    }

    public static LocalTime currentTime() {
        return LocalTime.now(getClock());
    }

    public static LocalDateTime currentDateTime() {
        return LocalDateTime.now(getClock());
    }

    public static OffsetDateTime currentOffsetDateTime() {
        return OffsetDateTime.now(getClock());
    }

    public static ZonedDateTime currentZonedDateTime() {
        return ZonedDateTime.now(getClock());
    }

    public static Instant currentInstant() {
        return Instant.now(getClock());
    }

    public static long currentTimeMillis() {
        return currentInstant().toEpochMilli();
    }

    public static void useMockTime(LocalDateTime dateTime, ZoneId zoneId) {
        Instant instant = dateTime.atZone(zoneId).toInstant();
        CLOCK = Clock.fixed(instant, zoneId);
        TimeZone.setDefault(TimeZone.getTimeZone(zoneId));
    }

    public static void useSystemDefaultZoneClock() {
        REAL_TIME_ZONE = DEFAULT_TIME_ZONE;
        TimeZone.setDefault(DEFAULT_TIME_ZONE);
        CLOCK = Clock.systemDefaultZone();
    }
    
    public static void setZoneClock(ZoneId zoneId) {
        REAL_TIME_ZONE = TimeZone.getTimeZone(zoneId);
        CLOCK = Clock.system(zoneId);
    }

    private static Clock getClock() {
        return CLOCK;
    }

    public static TimeZone getRealTimeZone() {
        return REAL_TIME_ZONE;
    }
}
