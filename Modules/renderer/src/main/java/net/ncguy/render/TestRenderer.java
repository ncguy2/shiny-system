package net.ncguy.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.DefaultShaderProvider;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
import net.ncguy.foundation.tools.profile.ProfilerHost;
import net.ncguy.photon.debug.PhotonSampleDebugRenderer;
import net.ncguy.render.shader.MaterialShader;

public class TestRenderer extends BasicRenderer {

    ModelBatch batch;
    public PerspectiveCamera camera;
    Environment environment;
    PhotonSampleDebugRenderer renderer;

    @Override
    public void init() {
        ShaderProgram.pedantic = false;
        ShaderProgram.prependVertexCode = "#version 130\n";
        ShaderProgram.prependFragmentCode = "#version 130\n";
        batch = new ModelBatch(new DefaultShaderProvider() {
            @Override
            protected Shader createShader(Renderable renderable) {
                config.vertexShader = Gdx.files.internal("shaders/default.vertex.glsl").readString();
                config.fragmentShader = Gdx.files.internal("shaders/default.fragment.glsl").readString();
                return new MaterialShader(renderable, config);
            }
        });
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, .4f, .4f, .4f, 1f));
        environment.add(new DirectionalLight().set(.8f, .8f, .8f, -1f, -.8f, -.2f));

        camera = new PerspectiveCamera(67, 1, 1);
        camera.position.set(0, 0, 0);
        camera.lookAt(Vector3.X);
        camera.near = 0.1f;
        camera.far = 1024.f;

        renderer = new PhotonSampleDebugRenderer();
    }

    @Override
    public void render(RenderableProvider provider) {
        ProfilerHost.Start("TestRenderer::render");
        batch.begin(camera);
        batch.render(provider, environment);

        if(Gdx.input.isKeyPressed(Input.Keys.TAB)) {
            renderer.render(batch, environment);
        }

        batch.end();
        ProfilerHost.End();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    @Override
    public void doResize(int width, int height) {
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();
    }

    @Override
    public Camera camera() {
        return camera;
    }

    @Override
    public void camera(Camera c) {
        camera = (PerspectiveCamera) c;
    }
}
