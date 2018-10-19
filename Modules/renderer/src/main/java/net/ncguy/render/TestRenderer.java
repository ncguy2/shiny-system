package net.ncguy.render;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.DefaultShaderProvider;
import com.badlogic.gdx.math.Vector3;
import net.ncguy.render.shader.MaterialShader;

import java.util.HashMap;
import java.util.Map;

public class TestRenderer extends BasicRenderer {

    ModelBatch batch;
    public PerspectiveCamera camera;
    Environment environment;

    @Override
    public void init() {
        batch = new ModelBatch(new DefaultShaderProvider() {
            Map<Renderable, Long> materialMasks = new HashMap<>();
            @Override
            public Shader getShader(Renderable renderable) {
                if(materialMasks.containsKey(renderable)) {
                    if(materialMasks.get(renderable) == renderable.material.getMask()) {
                        if(renderable.shader != null) {
                            return renderable.shader;
                        }
                    }
                }
                return createShader(renderable);
            }

            @Override
            protected Shader createShader(Renderable renderable) {
                if(renderable.shader != null) {
                    renderable.shader.dispose();
                    renderable.shader = null;
                }
                MaterialShader shader = new MaterialShader(renderable);
                shader.init();
                renderable.shader = shader;
                materialMasks.put(renderable, renderable.material.getMask());
                return shader;
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
    }

    @Override
    public void render(RenderableProvider provider) {
        batch.begin(camera);
        batch.render(provider, environment);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();
    }
}
