package net.ncguy.foundation.data.components.camera;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import net.ncguy.foundation.data.components.SceneComponent;

public abstract class CameraComponent<T extends Camera, P extends CameraComponent.CameraProps> extends SceneComponent<CameraComponent> {

    public boolean bIsCameraAttached;
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

        if (bIsCameraAttached) {
//            camera.view.setToLookAt(position, tmp.set(position).add(direction), up);
        } else {
            camera.update();
        }

    }

    public void resize(P props) {
        camera.viewportWidth = props.fViewportWidth;
        camera.viewportHeight = props.fViewportHeight;
    }

    public abstract T buildCamera(P props);

    public Camera getCamera() {
        return camera;
    }

    public static class CameraProps {
        public boolean bIsCameraAttached = false;
        public float fNear = 0.1f;
        public float fFar = 1024.f;
        public float fViewportWidth = Gdx.graphics.getWidth();
        public float fViewportHeight = Gdx.graphics.getHeight();
    }

}
