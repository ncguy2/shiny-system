package net.ncguy.core;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.kotcrab.vis.ui.VisUI;
import net.ncguy.foundation.data.Entity;
import net.ncguy.foundation.data.World;
import net.ncguy.foundation.data.components.SceneComponent;
import net.ncguy.foundation.data.components.camera.PerspectiveCameraComponent;
import net.ncguy.foundation.data.components.mesh.CubeComponent;
import net.ncguy.foundation.data.components.modifiers.MaterialComponent;
import net.ncguy.foundation.data.input.FirstPersonFlyingController;
import net.ncguy.render.RenderWrapper;
import net.ncguy.render.TestRenderer;
import net.ncguy.world.WorldModule;

public class GameLauncher extends Game {

    RenderWrapper renderWrapper;
    WorldModule worldModule;
    Entity cubeEntity;
    MaterialComponent mtlComp;
    FirstPersonFlyingController ctrlr;

    @Override
    public void create() {
        VisUI.load();

        World world = new World();

        cubeEntity = world.add(new CubeComponent());

        mtlComp = cubeEntity.getRootComponent().add(new MaterialComponent());
        mtlComp.mtl.set(ColorAttribute.createDiffuse(Color.RED));

        TestRenderer renderer = new TestRenderer();
        renderWrapper = new RenderWrapper(world, renderer, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        worldModule = new WorldModule(world);
        worldModule.dispatch();

        Entity camera = world.add(new PerspectiveCameraComponent());

        SceneComponent<?> rootComponent = camera.getRootComponent();
        Camera c = ((PerspectiveCameraComponent) rootComponent).getCamera();
        renderer.camera = (PerspectiveCamera) c;

//        renderer.camera.position.set(10, 10, 10);
//        renderer.camera.lookAt(0, 0, 0);
//        renderer.camera.update();

        c.position.set(10, 10, 10);
//        c.lookAt(0, 0, 0);
        c.direction.set(1, 1, 0).nor();
        c.up.set(0, 1, 0);
        c.update();



        Matrix4 mat = new Matrix4().setToWorld(c.position, c.direction, c.up);
//        mat.setToLookAt(c.position, c.position.cpy().add(c.direction), c.up);
//        camera.transform().translation.set(10, 10, 10);
        mat.getRotation(camera.transform().rotation);
        camera.transform().translation.set(10, 10, 10);

        Vector3 forward = camera.transform().forward();

        ctrlr = new FirstPersonFlyingController(camera.transform());
        camera.onUpdate(ctrlr::update);

        Gdx.input.setInputProcessor(ctrlr);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        renderWrapper.resize(width, height);
    }

    @Override
    public void render() {

        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            ColorAttribute attr = (ColorAttribute) mtlComp.mtl.get(ColorAttribute.Diffuse);
            attr.color.set(Color.RED);
        }else if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            ColorAttribute attr = (ColorAttribute) mtlComp.mtl.get(ColorAttribute.Diffuse);
            attr.color.set(Color.GREEN);
        }else if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
            ColorAttribute attr = (ColorAttribute) mtlComp.mtl.get(ColorAttribute.Diffuse);
            attr.color.set(Color.BLUE);
        }

        super.render();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        renderWrapper.render();
    }

    @Override
    public void dispose() {
        super.dispose();
        renderWrapper.dispose();
        worldModule.stop();
    }
}
