package net.ncguy.foundation.data.components.modifiers;

import com.badlogic.gdx.graphics.g3d.Shader;
import net.ncguy.foundation.data.aspect.Aspect;
import net.ncguy.foundation.data.aspect.CommonAspectKeys;

/**
 * Sets the shader used to render the owning components material instance, if it exists
 */
public class ShaderComponent extends ModifierComponent<ShaderComponent> {

    public Shader shader;

    public ShaderComponent() {
    }

    @Override
    public void applyModification(float delta) {
        if (shader != null) {
            Aspect.of(parentComponent, CommonAspectKeys.INTERCEPT_GET_RENDERABLES)
                    .ifPresent(asp -> {
                        CommonAspectKeys.GetRenderableInterception inst = asp.getObject();
                        if (inst == null)
                            return;

                        inst.renderablesArray.forEach(renderable -> renderable.shader = shader);
                    });
        }

    }
}
