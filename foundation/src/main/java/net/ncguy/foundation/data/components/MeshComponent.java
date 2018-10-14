package net.ncguy.foundation.data.components;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import net.ncguy.foundation.data.aspect.Aspect;
import net.ncguy.foundation.data.aspect.AspectKey;

public abstract class MeshComponent<T extends MeshComponent> extends SceneComponent<T> implements RenderableProvider {

    protected ModelInstance instance;

    public ModelInstance getInstance() {
        if (instance == null) {
            instance = buildInstance();
        }

        instance.transform.set(this.transform.worldTransform());

        return instance;
    }

    public abstract ModelInstance buildInstance();

    @Override
    public <T> Aspect<T> provideAspect(AspectKey<T> key) {

        if(key.type == ModelInstance.class) {
            return Aspect.of(this, key, (T) instance);
        }

        return super.provideAspect(key);
    }
}
