package net.ncguy.editor.display;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import net.ncguy.editor.display.viewports.Viewport;
import net.ncguy.editor.display.viewports.Viewport3D;
import net.ncguy.editor.display.viewports.WorldViewport;
import net.ncguy.editor.editor.ViewportFrame;
import net.ncguy.editor.editor.ui.EditorRegistry;
import net.ncguy.editor.editor.ui.EditorRoot;
import net.ncguy.editor.editor.ui.registry.MenuItemRegistry;
import net.ncguy.editor.plugin.PluginManager;
import net.ncguy.foundation.data.Entity;
import net.ncguy.foundation.data.World;
import net.ncguy.foundation.data.components.mesh.AssetMeshComponent;
import net.ncguy.foundation.data.components.mesh.CubeComponent;
import net.ncguy.foundation.data.components.modifiers.MaterialComponent;
import net.ncguy.foundation.data.components.modifiers.RotationComponent;
import net.ncguy.foundation.render.FBO;
import net.ncguy.render.BasicRenderer;
import net.ncguy.render.DeferredRenderer;
import net.ncguy.render.WorldRenderProvider;
import net.ncguy.world.WorldModule;

public class EditorScreen implements Screen {


    EditorRoot root;
    Stage stage;
    ScreenViewport stageViewport;
    OrthographicCamera stageCamera;

    @Override
    public void show() {
        stageCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stageViewport = new ScreenViewport(stageCamera);
        stage = new Stage(stageViewport);
        World world = new World();

        EditorRegistry editorRegistry = new EditorRegistry(world);

        // TODO populate registry from plugins

        editorRegistry.register(new MenuItemRegistry("Add floating viewport", "Window", () -> {
            FBO.Builder builder = new FBO.Builder(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            builder.addBasicColorTextureAttachment(Pixmap.Format.RGBA8888);
            builder.addBasicDepthRenderBuffer();
            BasicRenderer renderer = new DeferredRenderer();
            Viewport3D v = new WorldViewport(builder, true, renderer, new WorldRenderProvider(world));
            v.setDrawAxis(true);

            ViewportFrame frame = new ViewportFrame("Viewport", v);
            frame.addCloseButton();
            frame.setBounds(128, 128, 128, 128);
            stage.addActor(frame);
        }));

        PluginManager.get().forEach(p -> p.register(editorRegistry));

        root = new EditorRoot(editorRegistry);
        stage.addActor(root);

        Entity cubeEntity = world.add(new CubeComponent());
        MaterialComponent mtlComp = cubeEntity.getRootComponent().add(new MaterialComponent());
        mtlComp.mtl.set(ColorAttribute.createDiffuse(Color.RED));
        RotationComponent rot = cubeEntity.getRootComponent().add(new RotationComponent());
        rot.speed = 30;

        Entity add = world.add(AssetMeshComponent.of("mesh/mythra/mythra.g3dj"));
        add.transform().scale.set(.1f, .1f, .1f);
        add.transform().translation.set(0, 0, -2);
        RotationComponent rot1 = add.getRootComponent().add(new RotationComponent());
        rot1.speed = 30;

        BasicRenderer renderer = new DeferredRenderer();

        WorldModule worldModule = new WorldModule(world);
        worldModule.dispatch();

        FBO.Builder builder = new FBO.Builder(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        builder.addBasicColorTextureAttachment(Pixmap.Format.RGBA8888);
        builder.addBasicDepthRenderBuffer();

        Viewport3D viewport = new WorldViewport(builder, true, renderer, new WorldRenderProvider(world));
        viewport.setDrawAxis(true);

        root.setContent(viewport);


        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Viewport.RenderViewports();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stageViewport.update(width, height, true);
        root.setBounds(0, 0, width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
