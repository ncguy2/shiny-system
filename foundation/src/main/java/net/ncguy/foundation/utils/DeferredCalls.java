package net.ncguy.foundation.utils;

import java.util.ArrayList;
import java.util.List;

public class DeferredCalls {

    private static DeferredCalls instance;
    private static DeferredCalls get() {
        if (instance == null) {
            instance = new DeferredCalls();
        }
        return instance;
    }

    List<DeferredTask> tasks;

    private DeferredCalls() {
        tasks = new ArrayList<>();
    }

    public void _update(float delta) {
        tasks.stream().peek(t -> t.update(delta)).filter(DeferredTask::canRun).forEach(DeferredTask::run);
        tasks.removeIf(DeferredTask::hasRun);
    }

    public static void Post(Runnable task, float delay) {
        get().tasks.add(new DeferredTask(task, delay));
    }

    public static void Update(float delta) {
        get()._update(delta);
    }

    public static class DeferredTask {

        private final Runnable task;
        private float timeout;
        private boolean hasRun;

        public DeferredTask(Runnable task, float timeout) {
            this.task = task;
            this.timeout = timeout;
            this.hasRun = false;
        }

        public void update(float delta) {
            this.timeout -= delta;
        }

        public boolean canRun() {
            return this.timeout <= 0;
        }

        public void run() {
            this.task.run();
            this.hasRun = true;
        }

        public boolean hasRun() {
            return hasRun;
        }
    }

}
