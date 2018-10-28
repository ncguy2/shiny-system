package net.ncguy.foundation.data.components.modifiers;

import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import net.ncguy.foundation.data.Transform;
import net.ncguy.foundation.data.aspect.Aspect;
import net.ncguy.foundation.data.aspect.CommonAspectKeys;

public class RotationComponent extends ModifierComponent<RotationComponent> {

    public Vector3 axis = Vector3.Y.cpy();
    public float speed = 0;

    public transient final Quaternion tmpQuat = new Quaternion();

    @Override
    public void applyModification(float delta) {
        Aspect.of(parentComponent, CommonAspectKeys.TRANSFORM).ifPresent(asp -> {
            Transform transform = asp.getObject();
            tmpQuat.set(axis.cpy().nor(), speed * delta);
            transform.rotation.mul(tmpQuat);
        });
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public Vector3 getAxis() {
        return axis;
    }

    public void setAxis(Vector3 axis) {
        this.axis.set(axis);
    }
}
