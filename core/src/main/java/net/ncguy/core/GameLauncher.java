package net.ncguy.core;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.kotcrab.vis.ui.VisUI;
import net.ncguy.foundation.data.Entity;
import net.ncguy.foundation.data.World;
import net.ncguy.foundation.data.components.mesh.CubeComponent;
import net.ncguy.foundation.data.components.modifiers.MaterialComponent;
import net.ncguy.render.RenderWrapper;
import net.ncguy.render.TestRenderer;
import net.ncguy.world.WorldModule;

public class GameLauncher extends Game {

    RenderWrapper renderWrapper;
    WorldModule worldModule;

    @Override
    public void create() {
        VisUI.load();

        World world = new World();

        Entity cubeEntity = world.add(new CubeComponent());

        MaterialComponent mtlComp = cubeEntity.getRootComponent().add(new MaterialComponent());
        mtlComp.mtl.set(ColorAttribute.createDiffuse(Color.RED));

        TestRenderer renderer = new TestRenderer();
        renderWrapper = new RenderWrapper(world, renderer, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        renderer.camera.position.set(10, 10, 10);
        renderer.camera.lookAt(0, 0, 0);
        renderer.camera.update();
        worldModule = new WorldModule(world);
        worldModule.dispatch();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        renderWrapper.resize(width, height);
    }

    @Override
    public void render() {
        super.render();
        renderWrapper.render();
    }

    @Override
    public void dispose() {
        super.dispose();
        renderWrapper.dispose();
        worldModule.stop();
    }
}
