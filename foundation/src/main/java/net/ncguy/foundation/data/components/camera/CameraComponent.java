package net.ncguy.foundation.data.components.camera;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import net.ncguy.foundation.data.components.SceneComponent;

public abstract class CameraComponent<T extends Camera, P extends CameraComponent.CameraProps> extends SceneComponent<CameraComponent> {

    public T camera;

    public CameraComponent() {
        initialize(defaultProps());
    }

    public abstract P defaultProps();

    public void initialize(P props) {
        this.camera = buildCamera(props);
        resize(props);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        Matrix4 transform = this.transform.worldTransform();
        transform.getTranslation(camera.position);

        camera.lookAt(this.transform.forward().add(camera.position));
        camera.up.set(Vector3.Y);


        camera.update();
    }

    public void resize(P props) {
        camera.viewportWidth = props.viewportWidth;
        camera.viewportHeight = props.viewportHeight;
        camera.update();
    }

    public abstract T buildCamera(P props);

    public Camera getCamera() {
        return camera;
    }

    public static class CameraProps {
        public float near = 0.1f;
        public float far = 1024.f;
        public float viewportWidth = Gdx.graphics.getWidth();
        public float viewportHeight = Gdx.graphics.getHeight();
    }

}
