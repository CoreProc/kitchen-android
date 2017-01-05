package com.coreproc.android.kitchen;

import android.text.format.DateUtils;

import java.util.Date;

/**
 * Created by willm on 1/5/2017.
 */

public class Kitchen {

    public static String toHumanTime(long time) {
        long now = System.currentTimeMillis();
        return DateUtils.getRelativeTimeSpanString(time, now, DateUtils.DAY_IN_MILLIS).toString();
    }

}
