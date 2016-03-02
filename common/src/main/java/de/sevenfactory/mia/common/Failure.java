package de.sevenfactory.mia.common;

import android.support.annotation.Nullable;

public class Failure {
    private Exception  mException;
    private Resolution mResolution;
    
    public Failure(Exception exception) {
        this(exception, null);
    }
    
    public Failure(Exception exception, @Nullable Resolution resolution) {
        mException = exception;
        mResolution = resolution;
    }
    
    public Exception getException() {
        return mException;
    }
    
    public boolean hasResolution() {
        return mResolution != null;
    }
    
    public void resolve() {
        if (mResolution != null) {
            mResolution.resolve();
        }
    }

    /**
     * Use {@link #hasResolution()} to check if failure has resolution before get resolution type.
     *
     * @throws IllegalStateException if no resolution available for this failure
     */
    @Resolution.Type
    public int getResolutionType() {
        if(!hasResolution()) throw new IllegalStateException("No resolution available for this failure");

        return mResolution.getType();
    }
}