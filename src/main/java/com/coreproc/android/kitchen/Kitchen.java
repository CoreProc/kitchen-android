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

        CharSequence res = DateUtils.getRelativeTimeSpanString(
                localTimestamp,
                localNow,
                DateUtils.MINUTE_IN_MILLIS);
        res = ((res + "").replace("In ", ""));
        res = ((res + "").equalsIgnoreCase("0 minutes ago") ? "Just now" : res);
        res = ((res + "").equalsIgnoreCase("0 minutes") ? "Just now" : res);

        return res;
    }

}
