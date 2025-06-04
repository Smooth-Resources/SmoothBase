package com.smoothresources.smoothbase.common.time;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.temporal.ChronoUnit;

/**
 * Wrapper class for ChronoUnit that includes information about whether the unit is plural.
 */
public class ChronoUnitWrap {

    private final ChronoUnit unit;
    private final boolean isPlural;

    /**
     * Creates a new ChronoUnitWrap.
     *
     * @param unit    The ChronoUnit.
     * @param isPlural Whether the unit is plural.
     */
    public ChronoUnitWrap(@NotNull ChronoUnit unit, boolean isPlural) {
        this.unit = unit;
        this.isPlural = isPlural;
    }

    /**
     * Gets the ChronoUnit.
     *
     * @return The ChronoUnit.
     */
    @NotNull
    public ChronoUnit getUnit() {
        return unit;
    }

    /**
     * Checks if the unit is plural.
     *
     * @return True if the unit is plural, false otherwise.
     */
    public boolean isPlural() {
        return isPlural;
    }

    @Override
    public int hashCode() {
        int result = unit.hashCode();
        result = 31 * result + (isPlural ? 1 : 0);
        return result;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        ChronoUnitWrap that = (ChronoUnitWrap) obj;

        if (isPlural != that.isPlural) return false;
        return unit == that.unit;
    }
}
