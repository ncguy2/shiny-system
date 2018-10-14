package net.ncguy.render;

import net.ncguy.foundation.module.Module;

public class RenderModule extends Module {

    public RenderModule() {
        super("Renderer");
    }

    @Override
    public boolean isThreadedModule() {
        return false;
    }

    @Override
    public void start() {
        // NOOP, requires the main thread
    }
}
