package net.ncguy.editor.display.viewports;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.graphics.g3d.utils.FirstPersonCameraController;
import net.ncguy.foundation.render.FBO;
import net.ncguy.render.BasicRenderer;

public class WorldViewport extends Viewport3D {

    private final BasicRenderer renderer;
    private final RenderableProvider renderProvider;
    private FirstPersonCameraController ctrlr;

    public WorldViewport(FBO.Builder fboBuilder, boolean autoAttachListeners, BasicRenderer renderer, RenderableProvider renderProvider) {
        super(fboBuilder, autoAttachListeners);
        this.renderer = renderer;
        this.renderProvider = renderProvider;
        renderer.init();
        if(this.mainCamera != null) {
            renderer.camera(this.mainCamera);
        }
        setDrawAxis(true);
    }

    @Override
    public void DoInit() {
        super.DoInit();
    }

    @Override
    public void DoResize(float width, float height) {
        renderer.resize(Math.round(width), Math.round(height));
    }

    @Override
    public void DoRender() {
        ctrlr.update(Gdx.graphics.getDeltaTime());
        renderer.render(renderProvider);
    }

    @Override
    public void dispose() {
        if(renderer != null) {
            renderer.dispose();
        }
        super.dispose();
    }

    @Override
    protected InputProcessor BuildController(Camera c) {
        return ctrlr = new FirstPersonCameraController(c) {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                if(screenX < 0 || screenX > getWidth())
                    return false;
                if(screenY < 0 || screenY > getHeight())
                    return false;
                return true;
            }
        };
    }

}
