package de.sevenfactory.mia.fragments;

import android.support.v4.app.Fragment;
import android.view.View;

import de.sevenfactory.mia.modules.loading.LoadingModule;

/**
 * Created by bernd on 22.02.2016.
 */
public interface MiaActivity {
    LoadingModule getLoadingModule();

    View getContainerView();

    void setActionBarVisibility(int actionBarVisibility);

    void showFragment(Fragment fragment);

    void showFragment(Fragment fragment, boolean addToBackStack);

    boolean requestPermissions(String[] inPermission);

    void unregisterPermissionHandler(PermissionHandler inPermissionHandler);

    void registerPermissionHandler(PermissionHandler inPermissionHandler);

    interface PermissionHandler {
        void onPermissionGranted(String inPermisson);
    }
}
