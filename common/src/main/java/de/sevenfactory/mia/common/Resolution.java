package de.sevenfactory.mia.common;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public interface Resolution {

    @IntDef({RETRY})
    @Retention(RetentionPolicy.SOURCE)
    @interface Type {
    }

    int RETRY  = 0;

    void resolve();

    @Type
    int getType();
}