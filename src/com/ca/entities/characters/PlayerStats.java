package com.ca.entities.characters;

import com.ca.constants.Basic;
import com.ca.errors.general.InputNotValid;
import com.ca.errors.Logger;

import java.util.*;

/**
 * This class implements the basic entity's characteristics.
 */
public class PlayerStats {

    /**
     * All the player stats.
     */
    private final List<Statistic> statistics;

    public PlayerStats() {
        statistics = new LinkedList<>();

        statistics.add(new Statistic("strength"));
        statistics.add(new Statistic("resistance"));
        statistics.add(new Statistic("speed"));
        statistics.add(new Statistic("health"));
        statistics.add(new Statistic("regeneration"));
        statistics.add(new Statistic("knockback"));
    }

    /**
     * Copy constructor
     * @param stats a {@link PlayerStats} instance to copy.
     */
    public PlayerStats(PlayerStats stats) {
        statistics = new LinkedList<>(stats.statistics);
    }

    /**
     * @return all the keys in the map.
     */
    public List<String> getKeySet() {
        return statistics.stream().map(stat -> stat.name).toList();
    }

    /**
     * Sums to the current statistics all the values of another {@link PlayerStats} instance. <br>
     * @param playerStats the statistics to sum to the current ones.
     */
    public void add(PlayerStats playerStats) {
        for(String key : playerStats.getKeySet()) {
            add(key, playerStats.get(key).getCurrentValue());
        }
    }

    /**
     * Gets the multiplier value of the requested statistic.
     * @param key the multiplier's key.
     * @return the multiplier value assigned with the given key.
     */
    public double getMultiplier(String key) {

        Optional<Statistic> statistic = statistics.stream().filter(stat -> stat.name.equals(key)).findFirst();

        if (statistic.isEmpty()) {
            Logger.log(Logger.WARNING, "The requested multiplier is not valid: " + key);
            return Basic.STATS_DEFAULT_MULTIPLIER;
        }

        return statistic.get().multiplier;
    }

    /**
     * @param key the statistics you want to look for.
     * @return the value of the statistic.
     */
    public Statistic get(String key) {

        for(Statistic stat : statistics){
            if(stat.name.equals(key)) {
                return stat;
            }
        }

        Logger.log(Logger.WARNING, "The requested key '" + key + "' is not part of the key set: " + getKeySet());
        return null;
    }

    /**
     * Sets the value of the chosen statistic.
     * @param key the statistic to modify.
     * @param newValue the new value of the statistic.
     */
    public void set(String key, double newValue) {

        for (Statistic statistic : statistics) {
            if (statistic.name.equals(key)) {

                if (statistic.getMaxValue() == -1) {
                    statistic.setMaxValue(newValue);
                }

                statistic.setValue(newValue);
                return;
            }
            else if(statistic.getMultiplierName().equals(key)){
                statistic.setMultiplier(newValue);
                return;
            }
        }

        Logger.log(Logger.MODE_SALVAGE, new InputNotValid(getKeySet(), key));
    }

    public List<Statistic> getStatisticsList(){
        return statistics;
    }

    /**
     * Adds a value to the current statistic. This method cannot modify
     * @param key the statistic you want to change
     * @param amount the amount you want to add to it. Use a negative amount if you want to subtract.
     */
    public void add(String key, double amount) {
        set(key, get(key).getCurrentValue() + amount);
    }

    public static class Statistic {

        private final String name;
        private double value = 0.0;
        private double multiplier = Basic.STATS_DEFAULT_MULTIPLIER;
        private double maxValue = -1;

        public Statistic(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public double getCurrentValue() {
            return value;
        }

        public double getMultiplier() {
            return multiplier;
        }

        public void setMaxValue(double value) {
            maxValue = value;
        }

        public double getMaxValue() {
            return maxValue;
        }

        public void setValue(double value) {
            this.value = value;
        }

        public void setMultiplier(double multiplier) {
            this.multiplier = multiplier;
        }

        public boolean equals(Object target) {
            if (!(target instanceof Statistic stat)) {
                return false;
            }

            return this.name.equals(stat.name);
        }

        public String getMultiplierName(){
            return "multiplier_".concat(name);
        }
    }
}