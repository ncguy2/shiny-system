package net.ncguy.foundation.data.components.modifiers;

import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import net.ncguy.foundation.data.aspect.Aspect;
import net.ncguy.foundation.data.aspect.CommonAspectKeys;

public class MaterialComponent extends ModifierComponent<MaterialComponent> {

    public Material mtl;

    public MaterialComponent() {
        mtl = new Material();
    }

    public void set(Material mtl) {
        this.mtl.set(mtl);
    }

    public void share(Material mtl) {
        this.mtl = mtl;
    }

    @Override
    public void applyModification(float delta) {
        if (mtl != null) {
            Aspect.of(parentComponent, CommonAspectKeys.MODEL_INSTANCE)
                    .ifPresent(asp -> {
                        ModelInstance inst = asp.getObject();
                        if (inst == null)
                            return;

                        int selfIndex = getSelfIndex();
                        inst.materials.get(selfIndex)
                                .set(mtl);
                    });
        }

    }
}
