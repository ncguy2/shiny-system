package net.ncguy.foundation.data;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;

public class Transform {

    protected Transform parentTransform;

    public final Vector3 translation = new Vector3();
    public final Quaternion rotation = new Quaternion();
    public final Vector3 scale = new Vector3(1, 1, 1);

    protected transient final Matrix4 localTransform = new Matrix4();
    protected transient final Matrix4 worldTransform = new Matrix4();

    public void setParentTransform(Transform parent) {
        this.parentTransform = parent;
    }

    public Matrix4 localTransform() {
        localTransform.idt();
        localTransform.set(translation, rotation, scale);
        return localTransform;
    }

    public Matrix4 worldTransform() {
        worldTransform.set(localTransform());
        if(parentTransform != null) {
            worldTransform.mulLeft(parentTransform.worldTransform());
        }
        return worldTransform;
    }

    public void set(Matrix4 mat) {
        mat.getTranslation(translation);
        mat.getRotation(rotation);
        mat.getScale(scale);
    }

    public Vector3 getTranslation() {
        return translation;
    }

    public Vector3 getScale() {
        return scale;
    }

}
