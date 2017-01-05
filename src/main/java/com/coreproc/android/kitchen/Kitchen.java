package com.coreproc.android.kitchen;

import android.text.format.DateUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by willm on 1/5/2017.
 */

public class Kitchen {

    public static CharSequence getRelativeTime(final Date date) {
        long now = System.currentTimeMillis();
        int gmtOffset = TimeZone.getDefault().getRawOffset();
        long utcTimestamp = date.getTime();
        long localTimestamp = utcTimestamp + gmtOffset;
        Calendar c = Calendar.getInstance();
        long localNow = c.getTimeInMillis();

        return DateUtils.getRelativeTimeSpanString(
                    localTimestamp,
                    localNow,
                    DateUtils.MINUTE_IN_MILLIS,
                    DateUtils.FORMAT_ABBREV_RELATIVE);
    }

}
