package de.sevenfactory.mia.util;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.sevenfactory.mia.modules.R;

public class Utils {
    public static String join(String separator, String... strs) {
        StringBuilder builder = new StringBuilder();
        
        for (int i = 0; i < strs.length; i++) {
            builder.append(strs[i]);
            
            if (i < strs.length - 1) {
                builder.append(separator);
            }
        }
        
        return builder.toString();
    }

    public static String getFormattedDateDurationString(Context context, long airDate, long duration) {
        if (airDate > 0 && duration > 0) {
            long minutes = duration / 1000 / 60;
            long seconds = (duration / 1000) % 60;

            SimpleDateFormat sdf = new SimpleDateFormat(context.getString(R.string.date_format), Locale.getDefault());
            String date = sdf.format(new Date(airDate * 1000));

            if (minutes > 0) {
                if (minutes < 10 && seconds > 0) {
                    return String.format(context.getString(R.string.format_duration_min_sec), date, minutes, seconds);
                } else {
                    return String.format(context.getString(R.string.format_duration_min), date, minutes);
                }
            } else {
                return String.format(context.getString(R.string.format_duration_sec), date, seconds);
            }
        } else {
            return null;
        }
    }
}
    