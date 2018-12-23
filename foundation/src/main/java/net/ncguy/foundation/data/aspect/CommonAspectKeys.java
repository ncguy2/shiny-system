package net.ncguy.foundation.data.aspect;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import net.ncguy.foundation.data.Transform;
import net.ncguy.foundation.data.World;

public class CommonAspectKeys {

    public static final AspectKey<World>            WORLD           = new AspectKey<>(World.class);
    public static final AspectKey<Transform>        TRANSFORM       = new AspectKey<>(Transform.class);
    public static final AspectKey<ModelInstance>    MODEL_INSTANCE  = new AspectKey<>(ModelInstance.class);

    public static final AspectKey<GetRenderableInterception> INTERCEPT_GET_RENDERABLES = new AspectKey<>(GetRenderableInterception.class);

    public interface Interception {
    }

    public static class GetRenderableInterception implements Interception {
        public final Array<Renderable> renderablesArray;
        public final Pool<Renderable> renderablesPool;

        public GetRenderableInterception(Array<Renderable> renderablesArray, Pool<Renderable> renderablesPool) {
            this.renderablesArray = renderablesArray;
            this.renderablesPool = renderablesPool;
        }
    }

}
