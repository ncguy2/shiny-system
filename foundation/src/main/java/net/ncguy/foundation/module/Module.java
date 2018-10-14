package net.ncguy.foundation.module;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class Module {

    private final String name;
    protected long lastTime = 0;
    protected boolean alive = true;

    public Module(String name) {
        this.name = name;
    }

    public boolean isBackgroundModule() {
        return false;
    }

    public boolean isThreadedModule() {
        return true;
    }

    public boolean shouldThrottleModuleSteps() {
        return true;
    }

    public int getThrottleMilliseconds() {
        return 16;
    }

    public float _calculateDeltaTime() {
        long currentTime = System.currentTimeMillis();
        long deltaTime = currentTime - lastTime;
        lastTime = currentTime;
        return deltaTime / 1000f;
    }

    /**
     * Starts work on the off-thread
     */
    public void start() {
        if (shouldThrottleModuleSteps()) {
            ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
            executor.scheduleAtFixedRate(() -> {
                if (alive) {
                    _step();
                } else {
                    if (!executor.isShutdown() && !executor.isTerminated()) {
                        executor.shutdown();
                    }
                }
            }, 0, getThrottleMilliseconds(), TimeUnit.MILLISECONDS);
        } else {
            while (alive) {
                _step();
            }
        }
    }

    public void _step() {
        step(_calculateDeltaTime());
    }

    /**
     * Steps the work in the event of a looping module
     *
     * @param delta Seconds passed since the last step
     */
    public void step(float delta) {

    }

    public ModuleThread prepare() {
        if (!isThreadedModule())
            throw new UnsupportedOperationException(toString() + " is not a threaded module, unable to prepare thread");
        return new ModuleThread(this);
    }

    public ModuleThread dispatch() {
        ModuleThread thread = prepare();
        thread.run();
        return thread;
    }

    @Override
    public String toString() {
        return name;
    }

    public void stop() {
        alive = false;
    }
}
