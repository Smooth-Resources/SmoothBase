package com.smoothresources.smoothbase.common.time;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.List;

/**
 * Utility class for time-related operations.
 */
public class TimeUtils {

    /**
     * Converts the duration between two temporal objects to seconds.
     *
     * @param newerDate The newer temporal object.
     * @param olderDate The older temporal object.
     * @return The duration in seconds.
     */
    public static long toSeconds(@NotNull Temporal newerDate, @NotNull Temporal olderDate) {
        return ChronoUnit.SECONDS.between(newerDate, olderDate);
    }

    /**
     * Gets the remaining seconds until a specified temporal object.
     *
     * @param olderDate The temporal object.
     * @return The remaining seconds.
     */
    public static long remainingSeconds(@NotNull Temporal olderDate) {
        LocalDateTime now = LocalDateTime.now();
        return ChronoUnit.SECONDS.between(now, olderDate);
    }

    /**
     * Gets the passed seconds since a specified temporal object.
     *
     * @param olderDate The temporal object.
     * @return The passed seconds.
     */
    public static long passedSeconds(@NotNull Temporal olderDate) {
        LocalDateTime now = LocalDateTime.now();
        return ChronoUnit.SECONDS.between(olderDate, now);
    }

    /**
     * Formats a duration in seconds to a human-readable string using the specified configuration.
     *
     * @param seconds The duration in seconds.
     * @param config  The TimeFormatConfig for formatting.
     * @return The formatted time string.
     */
    @NotNull
    public static String formatTime(long seconds, @NotNull TimeFormatConfig config) {
        int maxDifferentUnits = config.getMaxDifferentUnits();

        StringBuilder formattedTime = new StringBuilder();
        int differentUnits = 0;

        List<ChronoUnit> sortedUnits = config.getUniqueUnitsSorted();
        for (ChronoUnit unit : sortedUnits) {
            long unitSeconds = unit.getDuration().getSeconds();
            long unitAmount = seconds / unitSeconds;

            if (unitAmount > 0) {
                seconds -= unitAmount * unitSeconds;

                if (config.isAddSpaceBeforeUnitName()) {
                    formattedTime.append(unitAmount).append(" ").append(config.getDefinedUnitName(unit, unitAmount)).append(" ");
                } else {
                    formattedTime.append(unitAmount).append(config.getDefinedUnitName(unit, unitAmount)).append(" ");
                }

                differentUnits++;
            }

            if (differentUnits >= maxDifferentUnits) break;
        }

        if (formattedTime.isEmpty()) {
            formattedTime.append("0");
        }

        return formattedTime.toString().trim();
    }

    /**
     * Formats a duration in seconds to a human-readable string using a default configuration.
     *
     * @param seconds The duration in seconds.
     * @return The formatted time string.
     */
    @NotNull
    public static String formatTime(long seconds) {
        TimeFormatConfig config = new TimeFormatConfig.Builder()
                .setMaxDifferentUnits(3)
                .setAddSpaceBeforeUnitName(false)
                .addUnit(ChronoUnit.DAYS, "d", "d")
                .addUnit(ChronoUnit.HOURS, "h", "h")
                .addUnit(ChronoUnit.MINUTES, "m", "m")
                .addUnit(ChronoUnit.SECONDS, "s", "s")
                .build();

        return formatTime(seconds, config);
    }
}
