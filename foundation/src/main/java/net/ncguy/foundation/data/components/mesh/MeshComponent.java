package net.ncguy.foundation.data.components.mesh;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import net.ncguy.foundation.data.aspect.Aspect;
import net.ncguy.foundation.data.aspect.AspectKey;
import net.ncguy.foundation.data.components.SceneComponent;

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

        if(instance != null) {
            instance.transform.set(this.transform.worldTransform());
        }

        return instance;
    }

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
        getInstance().ifPresent(m -> m.getRenderables(renderables, pool));
    }
}
