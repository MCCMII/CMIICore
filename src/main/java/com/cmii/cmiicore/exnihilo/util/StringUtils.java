package com.cmii.cmiicore.exnihilo.util;

import java.text.DecimalFormat;

/**
 * Formats a percent, displays no more than 2 decimal digits, doesn't show trailing zeros
 */
public final class StringUtils {
    private static final DecimalFormat PERCENT_FORMAT = new DecimalFormat("#.##%");

    private StringUtils() {
    }

    public static String formatPercent(double num) {
        return PERCENT_FORMAT.format(num);
    }
}