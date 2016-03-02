package de.sevenfactory.mia.modules.error;

import de.sevenfactory.mia.common.Failure;

interface ErrorUi {

    void showFailure(Failure failure);

    void dismissFailure();
}
