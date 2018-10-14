package net.ncguy.foundation.module;

public class ModuleThread extends Thread {

    private final Module module;

    public ModuleThread(Module module) {
        super(module.toString());
        this.setDaemon(module.isBackgroundModule());
        this.module = module;
    }

    @Override
    public void run() {
        module.start();
    }
}
