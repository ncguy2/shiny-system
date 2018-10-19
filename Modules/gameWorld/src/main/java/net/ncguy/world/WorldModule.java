package net.ncguy.world;

import net.ncguy.foundation.data.World;
import net.ncguy.foundation.module.Module;

public class WorldModule extends Module {

    protected final World world;

    public WorldModule(World world) {
        super("World Simulation");
        this.world = world;
    }

    public World getWorld() {
        return world;
    }

    @Override
    public void step(float delta) {
        world.update(delta);
    }
}
