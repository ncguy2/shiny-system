package net.ncguy.foundation.data.components.camera;

import com.badlogic.gdx.graphics.PerspectiveCamera;

import static net.ncguy.foundation.data.components.camera.PerspectiveCameraComponent.Props;

public class PerspectiveCameraComponent extends CameraComponent<PerspectiveCamera, Props> {

    @Override
    public Props defaultProps() {
        return new Props();
    }

    @Override
    public PerspectiveCamera buildCamera(Props props) {

        PerspectiveCamera camera = new PerspectiveCamera();
        camera.near = props.near;
        camera.far = props.far;
        camera.fieldOfView = props.fov;

        return camera;
    }

    public static class Props extends CameraProps {
        public float fov = 67;
    }

}
