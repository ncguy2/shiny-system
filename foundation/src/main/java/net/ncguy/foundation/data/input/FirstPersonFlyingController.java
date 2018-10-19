package net.ncguy.foundation.data.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.IntIntMap;
import net.ncguy.foundation.data.Transform;

public class FirstPersonFlyingController extends InputAdapter {

    private final Transform transform;
    private final IntIntMap keys = new IntIntMap();
    private final Vector3 tmp = new Vector3();
    private int STRAFE_LEFT = Input.Keys.A;
    private int STRAFE_RIGHT = Input.Keys.D;
    private int FORWARD = Input.Keys.W;
    private int BACKWARD = Input.Keys.S;
    private int UP = Input.Keys.Q;
    private int DOWN = Input.Keys.E;
    private float velocity = 5;
    private float degreesPerPixel = 0.5f;

    public FirstPersonFlyingController(Transform transform) {
        this.transform = transform;
    }

    @Override
    public boolean keyDown(int keycode) {
        keys.put(keycode, keycode);
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        keys.remove(keycode, 0);
        return true;
    }

    /**
     * Sets the velocity in units per second for moving forward, backward and strafing left/right.
     *
     * @param velocity the velocity in units per second
     */
    public void setVelocity(float velocity) {
        this.velocity = velocity;
    }

    /**
     * Sets how many degrees to rotate per pixel the mouse moved.
     *
     * @param degreesPerPixel
     */
    public void setDegreesPerPixel(float degreesPerPixel) {
        this.degreesPerPixel = degreesPerPixel;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        float deltaX = (-Gdx.input.getDeltaX() * degreesPerPixel) * Gdx.graphics.getDeltaTime();
        float deltaY = (-Gdx.input.getDeltaY() * degreesPerPixel) * Gdx.graphics.getDeltaTime();

        Vector3 up = transform.up();
        transform.rotation.add(up.x, up.y, up.z, deltaX);
        tmp.set(transform.forward()).crs(transform.up()).nor();
        transform.rotation.add(tmp.x, tmp.y, tmp.z, deltaY);
        return true;
    }

    public void update() {
        update(Gdx.graphics.getDeltaTime());
    }

    public void update(float deltaTime) {
        if (keys.containsKey(FORWARD)) {
            tmp.set(transform.forward()).nor().scl(deltaTime * velocity);
            transform.translation.add(tmp);
        }
        if (keys.containsKey(BACKWARD)) {
            tmp.set(transform.forward()).nor().scl(-deltaTime * velocity);
            transform.translation.add(tmp);
        }
        if (keys.containsKey(STRAFE_LEFT)) {
            tmp.set(transform.forward()).crs(transform.up()).nor().scl(-deltaTime * velocity);
            transform.translation.add(tmp);
        }
        if (keys.containsKey(STRAFE_RIGHT)) {
            tmp.set(transform.forward()).crs(transform.up()).nor().scl(deltaTime * velocity);
            transform.translation.add(tmp);
        }
        if (keys.containsKey(UP)) {
            tmp.set(transform.up()).nor().scl(deltaTime * velocity);
            transform.translation.add(tmp);
        }
        if (keys.containsKey(DOWN)) {
            tmp.set(transform.up()).nor().scl(-deltaTime * velocity);
            transform.translation.add(tmp);
        }
//        transform.update(true);
    }
}

