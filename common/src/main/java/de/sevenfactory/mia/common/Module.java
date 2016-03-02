package de.sevenfactory.mia.common;

public interface Module {
    void onStart();
    
    void onStop();

    int getId();

    String[] getNeededPermissions();
}
