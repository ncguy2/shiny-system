package net.ncguy.core;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.FirstPersonCameraController;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.Separator;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import net.ncguy.Photon;
import net.ncguy.foundation.data.Entity;
import net.ncguy.foundation.data.World;
import net.ncguy.foundation.data.asset.AssetHandler;
import net.ncguy.foundation.data.components.SceneComponent;
import net.ncguy.foundation.data.components.camera.PerspectiveCameraComponent;
import net.ncguy.foundation.data.components.mesh.AssetMeshComponent;
import net.ncguy.foundation.data.components.mesh.CubeComponent;
import net.ncguy.foundation.data.components.modifiers.MaterialComponent;
import net.ncguy.foundation.tools.profile.*;
import net.ncguy.foundation.utils.DeferredCalls;
import net.ncguy.photon.PLight;
import net.ncguy.photon.PWorld;
import net.ncguy.photon.ThreadPool;
import net.ncguy.render.RenderWrapper;
import net.ncguy.render.TestRenderer;
import net.ncguy.world.WorldModule;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class GameLauncher extends Game {

    RenderWrapper renderWrapper;
    WorldModule worldModule;
    Entity cubeEntity;
    MaterialComponent mtlComp;
//    FirstPersonFlyingController ctrlr;
    FirstPersonCameraController ctrlr;
    PWorld pWorld;
    Camera c;

    Stage stage;
    VisLabel label;

    VisTable container;
    VisLabel originLabel;
    VisLabel stepsizeLabel;
    VisLabel extentsLabel;
    VisLabel intensityLabel;

    public static class PollingLabel extends VisLabel {

        private final Supplier<String> supplier;
        public PollingLabel(Supplier<String> supplier) {
            this.supplier = supplier;
        }

        @Override
        public void act(float delta) {
            setText(supplier.get());
            super.act(delta);
        }
    }

    @Override
    public void create() {
        VisUI.load();
        AssetHandler.Start();
        ProfilerHost.SupportsGPUProfiling();

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
        c = ((PerspectiveCameraComponent) rootComponent).getCamera();
        renderer.camera = (PerspectiveCamera) c;

        c.position.set(10, 10, 10);
        c.lookAt(0, 0, 0);
        c.up.set(0, 1, 0);
        c.update();

        ctrlr = new FirstPersonCameraController(c);
        camera.onUpdate(ctrlr::update);

        Gdx.input.setInputProcessor(ctrlr);

        Entity add = world.add(AssetMeshComponent.of("mesh/mythra/mythra.g3dj"));
        add.transform().scale.set(.1f, .1f, .1f);
        add.transform().translation.set(0, 0, -2);
//        world.add(AssetMeshComponent.of("mesh/kurumi/Kurumi Fragmented.g3db"));

        pWorld = new PWorld(renderWrapper.getProvider());

        stage = new Stage(new ScreenViewport(new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight())));
        label = new VisLabel();
        label.setText("");
        stage.addActor(label);

        container = new VisTable();
        stage.addActor(container);

        originLabel = new PollingLabel(Photon.actualOrigin::toString);
        stepsizeLabel = new PollingLabel(Photon.actualStepSize::toString);
        extentsLabel = new PollingLabel(Photon.actualExtents::toString);
        intensityLabel = new PollingLabel(() -> Photon.globalLightIntensity + "");

        container.defaults().height(24);
        container.columnDefaults(0).growX().right();
        container.columnDefaults(1).fillY();
        container.columnDefaults(2).growX().left().padLeft(4);

        container.add("Origin");
        container.add(new Separator());
        container.add(originLabel).row();

        container.add("Step size");
        container.add(new Separator());
        container.add(stepsizeLabel).row();

        container.add("Extents");
        container.add(new Separator());
        container.add(extentsLabel).row();

        container.add("Intensity");
        container.add(new Separator());
        container.add(intensityLabel).row();
    }

    @Override
    public void resize(int width, int height) {

        container.setDebug(false, true);

        super.resize(width, height);
        renderWrapper.resize(width, height);
        stage.getViewport().update(width, height, true);
        stage.getCamera().viewportWidth = width;
        stage.getCamera().viewportHeight = height;
        stage.getCamera().update();
        setLabelText(label.getText().toString());
        container.setBounds(8, 8, 192, 96);
    }

    void setLabelText(final String t) {
        Gdx.app.postRunnable(() -> {
            label.setText(t);
            label.pack();
            label.setPosition(5, Gdx.graphics.getHeight() - (label.getHeight() + 5));
        });
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();

        DeferredCalls.Update(delta);

        Photon.globalLightIntensity = 8f;

        Gdx.graphics.setTitle("FPS: " + Gdx.graphics.getFramesPerSecond() + ", delta: " + (delta * 1000f) + "ms");

        ProfilerHost.StartFrame();

        AssetHandler.WithInstanceIfExists(AssetHandler::Update);

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

        stage.act(delta);
        stage.draw();

        ProfilerHost.EndFrame();
        ProfilerHost.Clear();

        GPUTaskProfile tp;
        while((tp = GPUProfiler.Get().GetFrameResults()) != null) {
            ProfilerHost.Post(new TaskStats(tp));
            GPUProfiler.Get().Recycle(tp);
        }

        CPUTaskProfile cp;
        while((cp = CPUProfiler.Get().GetFrameResults()) != null) {
            ProfilerHost.Post(new TaskStats(cp));
            CPUProfiler.Get().Recycle(cp);
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.T)) {
            c.position.set(10, 10, 10);
            c.lookAt(0, 0, 0);
            c.up.set(0, 1, 0);
            c.update();
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.F)) {
            System.out.println(ProfilerHost.instance().GetFullDump());
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.G)) {
            setLabelText("Rebuilding Photon volume");
            ThreadPool.submit(() -> {
                final long startTime = System.currentTimeMillis();
                final long startFrame = Gdx.graphics.getFrameId();
                pWorld.getWorldTris(tris -> {
                    List<PLight> lights = new ArrayList<>();
                    lights.add(new PLight(new Vector3(2.5f, 10f, 0), Color.RED));
                    lights.add(new PLight(new Vector3(0, 12.5f, 0), Color.GREEN));
                    lights.add(new PLight(new Vector3(0, 10f, 2.5f), Color.BLUE));
                    lights.add(new PLight(new Vector3(0, 15f, 3f), Color.MAGENTA));
                    Photon.targetOrigin.set(new Vector3(0, 5, 0));
                    Photon.targetStepSize.set(new Vector3(1, 3, 1));
                    Photon.BuildTexture(tris, lights, 16);
                    long endTime = System.currentTimeMillis();
                    long endFrame = Gdx.graphics.getFrameId();

                    setLabelText(String.format("Time elapsed: %dms, %d frames\n", endTime - startTime, endFrame - startFrame));
                    DeferredCalls.Post(() -> setLabelText(""), 5);
                });
            });
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        renderWrapper.dispose();
        worldModule.stop();
        AssetHandler.Dispose();
    }
}
