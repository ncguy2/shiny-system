package net.ncguy.render;

import com.badlogic.gdx.utils.Disposable;
import net.ncguy.foundation.data.World;

public class RenderWrapper implements Disposable {

    private final World world;
    private final BasicRenderer renderer;
    private final WorldRenderProvider provider;

    public RenderWrapper(World world, BasicRenderer renderer, int width, int height) {
        this.world = world;
        this.renderer = renderer;
        this.provider = new WorldRenderProvider(world);

        renderer.init();
        renderer.resize(width, height);
    }

    public void resize(int width, int height) {
        renderer.resize(width, height);
    }

    public void render() {
        renderer.render(provider);
    }

    @Override
    public void dispose() {
        renderer.dispose();
    }
}
