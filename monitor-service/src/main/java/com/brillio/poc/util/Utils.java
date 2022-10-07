package com.brillio.poc.util;

import java.util.concurrent.TimeUnit;

public class Utils {

    public static TimeUnit getTimeUnit( long frequency) {
        if( frequency > 59 ) {
            return TimeUnit.HOURS;
        } else {
            return TimeUnit.MINUTES;
        }
    }

    public static long getFreqBasedOnMinAndHour( long frequency) {
        if( frequency > 59 ) {
            return frequency / 60;
        } else {
            return frequency;
        }
    }
}
