package net.ncguy.world;

import net.ncguy.foundation.data.Entity;
import net.ncguy.foundation.data.World;

import java.util.List;

public class WorldSimulation {

    private final World world;

    public WorldSimulation(World world) {
        this.world = world;
    }

    public void update(float delta) {
        List<Entity> entities = world.getEntities();
        entities.forEach(e -> e.update(delta));
    }

}
