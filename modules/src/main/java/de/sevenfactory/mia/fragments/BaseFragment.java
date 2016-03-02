package de.sevenfactory.mia.fragments;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.v4.app.Fragment;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

import de.sevenfactory.mia.common.Module;
import de.sevenfactory.mia.common.Observable;
import de.sevenfactory.mia.modules.error.ErrorModule;
import de.sevenfactory.mia.modules.loading.LoadingModule;

public class BaseFragment extends Fragment implements MiaActivity.PermissionHandler {
    private Map<Integer, Module> mStartedModules;
    private Map<Integer, Module> mUnStartedModules;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStartedModules = new HashMap<>(10);
        mUnStartedModules = new HashMap<>(10);
    }

    @CallSuper
    @Override
    public void onStart() {
        super.onStart();

        getMiaActivity().setActionBarVisibility(getActionBarVisibility());
        for (Module module : mStartedModules.values()) {
            mUnStartedModules.put(module.getId(), module);
        }
        mStartedModules.clear();

        for (Module module : mUnStartedModules.values()) {
            if (module.getNeededPermissions() == null
                    || getMiaActivity().requestPermissions(module.getNeededPermissions())) {
                module.onStart();
                mStartedModules.put(module.getId(), module);
            } else {
                getMiaActivity().registerPermissionHandler(this);
            }
        }
        for (Module module : mStartedModules.values()) {
            mUnStartedModules.remove(module.getId());
        }
    }

    @CallSuper
    @Override
    public void onStop() {
        for (Module module : mStartedModules.values()) {
            module.onStop();
        }
        for (Module module : mStartedModules.values()) {
            mUnStartedModules.put(module.getId(), module);
        }
        mStartedModules.clear();
        getMiaActivity().unregisterPermissionHandler(this);
        super.onStop();
    }

    protected final void registerModule(Module... modules) {
        for (Module module : modules) {
            mUnStartedModules.put(module.getId(), module);
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    // Error module must be able to handle observables of several types.
    protected final void setupErrorModule(Observable... observables) {
        ErrorModule module = new ErrorModule(getMiaActivity().getContainerView(), observables);
        registerModule(module);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    // Loading module must be able to handle observables of several types.
    protected final void setupLoadingModule(Observable... observables) {
        LoadingModule module = getMiaActivity().getLoadingModule();
        module.setUp(observables);
        registerModule(module);
    }

    protected int getActionBarVisibility() {
        return View.VISIBLE;
    }
    
    protected final MiaActivity getMiaActivity() {
        return (MiaActivity) getActivity();
    }

    @CallSuper
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mStartedModules.clear();
        mUnStartedModules.clear();
    }

    protected final Module getModule(int id) {
        Module out = mStartedModules.get(id);
        if (out != null) {
            return out;
        } else {
            return mUnStartedModules.get(id);
        }
    }

    @Override
    public void onPermissionGranted(String inPermisson) {
        for (Module module : mUnStartedModules.values()) {
            String[] permissions = module.getNeededPermissions();
            if (permissions != null) {
                for (int i = 0; i < permissions.length; i++) {
                    if (permissions[i].equalsIgnoreCase(inPermisson)) {
                        if (getMiaActivity().requestPermissions(module.getNeededPermissions())) {
                            module.onStart();
                            mStartedModules.put(module.getId(), module);
                            break;
                        }
                    }
                }
            }
        }
        for (Module module : mStartedModules.values()) {
            mUnStartedModules.remove(module.getId());
        }
    }
}
