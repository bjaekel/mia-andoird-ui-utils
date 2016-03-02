package de.sevenfactory.mia.common;

import java.util.concurrent.FutureTask;

public interface Scheduler {
    <T> void execute(FutureTask<T> task, Observable<T> observable, Resolution resolution);
}
