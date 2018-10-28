package net.ncguy.render;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Disposable;
import net.ncguy.foundation.data.World;
import net.ncguy.foundation.render.FBO;

public class RenderWrapper implements Disposable {

    private final World world;
    private final BasicRenderer renderer;
    private final WorldRenderProvider provider;
    private FBO fbo;

    public RenderWrapper(World world, BasicRenderer renderer, int width, int height) {
        this.world = world;
        this.renderer = renderer;
        this.provider = new WorldRenderProvider(world);

        fbo = new FBO(Pixmap.Format.RGBA8888, width, height, true);

        renderer.init();
        renderer.resize(width, height);
    }

    public WorldRenderProvider getProvider() {
        return provider;
    }

    public void resize(int width, int height) {
        fbo.Resize(width, height);
        renderer.resize(width, height);
    }

    public void render() {
        renderer.render(provider);
    }

    public Texture renderToTexture() {
        fbo.begin();
        fbo.clear(0, 0, 0, 1, true);
        render();
        fbo.end();
        return fbo.getColorBufferTexture();
    }

    @Override
    public void dispose() {
        renderer.dispose();
    }
}
