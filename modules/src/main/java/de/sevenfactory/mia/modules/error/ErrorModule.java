package de.sevenfactory.mia.modules.error;

import android.support.design.widget.Snackbar;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import de.sevenfactory.mia.common.Failure;
import de.sevenfactory.mia.common.Module;
import de.sevenfactory.mia.common.Observable;
import de.sevenfactory.mia.common.Resolution;
import de.sevenfactory.mia.modules.R;

@SuppressWarnings({"unchecked", "rawtypes"})
// This module must be able to handle observables of several types.
public class ErrorModule implements Module, ErrorUi {

    private final View           mView;
    private final ErrorPresenter mPresenter;
    private       List<Failure>  mFailures;
    private       Snackbar       mSnackbar;

    public ErrorModule(View parent, Observable... observables) {
        mView = parent;
        mFailures = new ArrayList<>(observables.length);

        mPresenter = new ErrorPresenter(observables);
        mPresenter.setUi(this);
    }

    /* Module */

    @Override
    public void onStart() {
        mPresenter.register();
    }

    @Override
    public void onStop() {
        mPresenter.unregister();
    }

    @Override
    public int getId() {
        return -666;
    }

    /* Ui */

    @Override
    public void showFailure(Failure failure) {
        addFailure(failure);

        mSnackbar = createSnackbar();
        mSnackbar.show();
    }

    @Override
    public void dismissFailure() {
        if (mSnackbar != null && mSnackbar.isShownOrQueued()) {
            mSnackbar.dismiss();
        }
    }

    /* Helpers */

    private Snackbar createSnackbar() {
        Snackbar snackbar;

        if (shouldRetry()) { // if one of the failures received contains a retry, a retry button must be shown
            snackbar = Snackbar.make(mView, mView.getContext().getString(R.string.error_default), Snackbar.LENGTH_INDEFINITE)
                    .setAction(mView.getContext().getString(R.string.retry), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            resolveFailures();
                        }
                    });
        } else {
            snackbar = Snackbar.make(mView, mView.getContext().getString(R.string.error_default), Snackbar.LENGTH_LONG);
        }

        return snackbar;
    }

    void addFailure(Failure failure) {
        if (failure.hasResolution()) {
            mFailures.add(failure);
        }
    }

    private void resolveFailures() {
        for (Failure failure : mFailures) {
            failure.resolve();
        }
        mFailures.clear();
    }

    boolean shouldRetry() {
        for (Failure failure : mFailures) {
            if (failure.hasResolution() && failure.getResolutionType() == Resolution.RETRY) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String[] getNeededPermissions() {
        return null;
    }
}