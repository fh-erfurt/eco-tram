package de.ecotram.backend.utilities;

public class Utilities {

    public static boolean isIntegerValid(int value, int min, int max) {
        if (value < min) return false;
        return max == -1 || value <= max;
    }

    public static boolean isIntegerValid(int value, int min) {
        return isIntegerValid(value, min, -1);
    }

    public static boolean isFloatValid(float value, int min, int max) {
        if (value < min) return false;
        return max == -1 || value <= max;
    }

    public static boolean isFloatValid(float value, int min) {
        return isFloatValid(value, min, -1);
    }

    public static boolean isStringValid(String value, int min, int max, boolean nullable) {
        if (value == null) return nullable;
        if (value.length() < min) return false;
        return max == -1 || value.length() <= max;
    }

    public static boolean isStringValid(String value, int min, int max) {
        return isStringValid(value, min, max, false);
    }

    public static boolean isStringValid(String value, int min) {
        return isStringValid(value, min, -1, false);
    }

}
