package net.smoothplugins.smoothbase.common.time;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Configuration class for formatting time.
 */
public class TimeFormatConfig {

    private final int maxDifferentUnits;
    private final boolean addSpaceBeforeUnitName;
    private final HashMap<ChronoUnitWrap, String> availableUnits;

    /**
     * Creates a new TimeFormatConfig.
     *
     * @param maxDifferentUnits       The maximum number of different units to display.
     * @param addSpaceBeforeUnitName  Whether to add a space before the unit name.
     * @param availableUnits          The map of available units and their names.
     */
    public TimeFormatConfig(int maxDifferentUnits, boolean addSpaceBeforeUnitName, @NotNull HashMap<ChronoUnitWrap, String> availableUnits) {
        this.maxDifferentUnits = maxDifferentUnits;
        this.addSpaceBeforeUnitName = addSpaceBeforeUnitName;
        this.availableUnits = availableUnits;
    }

    /**
     * Gets the unique units used in the configuration.
     *
     * @return A set of unique ChronoUnits.
     */
    @NotNull
    public Set<ChronoUnit> getUniqueUnits() {
        return availableUnits.keySet().stream().map(ChronoUnitWrap::getUnit).collect(Collectors.toSet());
    }

    /**
     * Gets the unique units used in the configuration, sorted by duration.
     *
     * @return A list of unique ChronoUnits, sorted by duration.
     */
    @NotNull
    public List<ChronoUnit> getUniqueUnitsSorted() {
        return getUniqueUnits()
                .stream()
                .sorted(Comparator.comparingLong(unit -> unit.getDuration().getSeconds()))
                .sorted(Comparator.reverseOrder())
                .toList();
    }

    /**
     * Gets the defined name for a ChronoUnitWrap.
     *
     * @param unit The ChronoUnitWrap.
     * @return The defined name, or null if not defined.
     */
    @Nullable
    public String getDefinedUnitName(@NotNull ChronoUnitWrap unit) {
        return availableUnits.get(unit);
    }

    /**
     * Gets the defined name for a ChronoUnit and amount.
     *
     * @param unit   The ChronoUnit.
     * @param amount The amount of the unit.
     * @return The defined name, or null if not defined.
     */
    @Nullable
    public String getDefinedUnitName(@NotNull ChronoUnit unit, long amount) {
        boolean isPlural = amount > 1;
        return getDefinedUnitName(new ChronoUnitWrap(unit, isPlural));
    }

    /**
     * Gets the maximum number of different units to display.
     *
     * @return The maximum number of different units.
     */
    public int getMaxDifferentUnits() {
        return maxDifferentUnits;
    }

    /**
     * Checks if a space is added before the unit name.
     *
     * @return True if a space is added, false otherwise.
     */
    public boolean isAddSpaceBeforeUnitName() {
        return addSpaceBeforeUnitName;
    }

    /**
     * Gets the map of available units and their names.
     *
     * @return The map of available units.
     */
    @NotNull
    public HashMap<ChronoUnitWrap, String> getAvailableUnits() {
        return availableUnits;
    }

    /**
     * Builder class for creating TimeFormatConfig instances.
     */
    public static class Builder {

        private int maxDifferentUnits;
        private boolean addSpaceBeforeUnitName;
        private final HashMap<ChronoUnitWrap, String> availableUnits;

        /**
         * Creates a new Builder instance.
         */
        public Builder() {
            this.maxDifferentUnits = 0;
            this.addSpaceBeforeUnitName = false;
            this.availableUnits = new HashMap<>();
        }

        /**
         * Sets the maximum number of different units to display.
         *
         * @param maxDifferentUnits The maximum number of different units.
         * @return The current Builder instance.
         */
        @NotNull
        public Builder setMaxDifferentUnits(int maxDifferentUnits) {
            this.maxDifferentUnits = maxDifferentUnits;
            return this;
        }

        /**
         * Sets whether to add a space before the unit name.
         *
         * @param addSpaceBeforeUnitName True to add a space, false otherwise.
         * @return The current Builder instance.
         */
        @NotNull
        public Builder setAddSpaceBeforeUnitName(boolean addSpaceBeforeUnitName) {
            this.addSpaceBeforeUnitName = addSpaceBeforeUnitName;
            return this;
        }

        /**
         * Adds a unit to the configuration.
         *
         * @param unit     The ChronoUnit.
         * @param unitName The name of the unit.
         * @return The current Builder instance.
         */
        @NotNull
        public Builder addUnit(@NotNull ChronoUnit unit, @NotNull String unitName) {
            availableUnits.put(new ChronoUnitWrap(unit, false), unitName);
            availableUnits.put(new ChronoUnitWrap(unit, true), unitName + "s");
            return this;
        }

        /**
         * Adds a unit to the configuration with separate singular and plural names.
         *
         * @param unit          The ChronoUnit.
         * @param unitName      The singular name of the unit.
         * @param pluralUnitName The plural name of the unit.
         * @return The current Builder instance.
         */
        @NotNull
        public Builder addUnit(@NotNull ChronoUnit unit, @NotNull String unitName, @NotNull String pluralUnitName) {
            availableUnits.put(new ChronoUnitWrap(unit, false), unitName);
            availableUnits.put(new ChronoUnitWrap(unit, true), pluralUnitName);
            return this;
        }

        /**
         * Removes a unit from the configuration.
         *
         * @param unit The ChronoUnit to remove.
         * @return The current Builder instance.
         */
        @NotNull
        public Builder removeUnit(@NotNull ChronoUnit unit) {
            availableUnits.remove(new ChronoUnitWrap(unit, false));
            availableUnits.remove(new ChronoUnitWrap(unit, true));
            return this;
        }

        /**
         * Builds and returns a TimeFormatConfig instance with the current configuration.
         *
         * @return The created TimeFormatConfig instance.
         */
        @NotNull
        public TimeFormatConfig build() {
            return new TimeFormatConfig(maxDifferentUnits, addSpaceBeforeUnitName, availableUnits);
        }
    }
}
