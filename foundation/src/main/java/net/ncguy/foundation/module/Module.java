package net.ncguy.foundation.module;

public abstract class Module {

    private final String name;

    public Module(String name) {
        this.name = name;
    }

    public boolean isBackgroundModule() {
        return false;
    }

    public boolean isThreadedModule() {
        return true;
    }

    /**
     * Starts work on the off-thread
     */
    public void start() {}

    public ModuleThread prepare() {
        if(!isThreadedModule())
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
}
