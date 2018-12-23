package net.ncguy.foundation.data.components.mesh;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import net.ncguy.foundation.data.aspect.Aspect;
import net.ncguy.foundation.data.aspect.AspectKey;
import net.ncguy.foundation.data.components.SceneComponent;
import net.ncguy.foundation.data.components.modifiers.ShaderComponent;

import java.util.List;
import java.util.Optional;

public abstract class MeshComponent<T extends MeshComponent> extends SceneComponent<T> implements RenderableProvider {

    protected ModelInstance instance;

    public Optional<ModelInstance> getInstance() {
        return Optional.ofNullable(_getInstance());
    }

    public ModelInstance _getInstance() {
        if (instance == null) {
            instance = buildInstance();
        }

        if (instance != null) {
            instance.transform.set(this.transform.worldTransform());
            if (instance.userData == null) {
                instance.userData = this;
            }
        }

        return instance;
    }

    public abstract RendererType getRenderType();

    public abstract ModelInstance buildInstance();

    @Override
    public <T> Aspect<T> provideAspect(AspectKey<T> key) {

        if(key.type == ModelInstance.class && instance != null) {
            return Aspect.of(this, key, (T) instance);
        }

        return super.provideAspect(key);
    }

    @Override
    public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool) {
        getInstance().ifPresent(m -> {
            Array<Renderable> r = new Array<>();
            m.getRenderables(r, pool);
            renderables.addAll(r);

            List<ShaderComponent> c = get(ShaderComponent.class, true);
            if (c.isEmpty()) return;
            r.forEach(renderable -> renderable.shader = c.get(0).shader);
        });
    }

    public static enum RendererType {
        STATIC,
        SKELETAL,
//        CUSTOM
    }

}
