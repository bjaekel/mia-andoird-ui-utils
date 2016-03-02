package de.sevenfactory.mia.common;

import android.content.Context;

/**
 * Created by man0004t on 02/12/15.
 */
public class DeviceUtils {

    public static boolean isTablet(Context context) {
        return context.getResources().getBoolean(R.bool.is_tablet);
    }
}
