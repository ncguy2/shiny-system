package net.ncguy.foundation.data.aspect;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import net.ncguy.foundation.data.Transform;

public class CommonAspectKeys {

    public static final AspectKey<Transform>        TRANSFORM       = new AspectKey<>(Transform.class);
    public static final AspectKey<ModelInstance>    MODEL_INSTANCE  = new AspectKey<>(ModelInstance.class);

}