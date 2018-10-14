package net.ncguy.render;

import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import net.ncguy.foundation.data.ComponentWalker;
import net.ncguy.foundation.data.Entity;
import net.ncguy.foundation.data.World;

import java.util.List;

public class WorldRenderProvider implements RenderableProvider {

    protected final World world;

    public WorldRenderProvider(World world) {
        this.world = world;
    }

    @Override
    public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool) {
        List<Entity> entities = world.getEntities();
        entities.forEach(e -> {
            ComponentWalker.traverse(e, c -> c instanceof RenderableProvider, c -> {
                ((RenderableProvider) c).getRenderables(renderables, pool);
            });
        });
    }
}
