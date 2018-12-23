package net.ncguy.editor.modules.world;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.widget.VisScrollPane;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import net.ncguy.editor.editor.ui.EditorRegistry;
import net.ncguy.editor.editor.ui.registry.TabPageRegistry;
import net.ncguy.editor.modules.world.adapter.AssetMeshComponentAdapter;
import net.ncguy.editor.modules.world.adapter.ComponentAdapter;
import net.ncguy.editor.modules.world.adapter.RotationComponentAdapter;
import net.ncguy.editor.plugin.IPlugin;
import net.ncguy.foundation.data.World;
import net.ncguy.foundation.data.aspect.CommonAspectKeys;
import net.ncguy.foundation.data.components.EntityComponent;
import net.ncguy.foundation.data.components.mesh.AssetMeshComponent;
import net.ncguy.foundation.data.components.modifiers.RotationComponent;

import java.util.HashMap;
import java.util.Map;

public class WorldPlugin implements IPlugin {

    @Override
    public String name() {
        return "World";
    }

    Map<Class<?>, ComponentAdapter<?>> adapters;

    <T extends EntityComponent<?>> void registerAdapter(Class<T> type, ComponentAdapter<T> adapter) {
        adapters.put(type, adapter);
    }

    public WorldPlugin() {
        adapters = new HashMap<>();
        registerAdapter(RotationComponent.class, new RotationComponentAdapter());
        registerAdapter(AssetMeshComponent.class, new AssetMeshComponentAdapter());
    }

    public <T extends EntityComponent<?>> ComponentAdapter<T> getAdapter(Class<T> type) {
        return (ComponentAdapter<T>) adapters.getOrDefault(type, new ComponentAdapter.Default());
    }

    @Override
    public void register(EditorRegistry registry) {

        VisTable detailPanel = new VisTable();

        registry.register(new TabPageRegistry("Details", "sidebar/right", () -> detailPanel));

        registry.register(new TabPageRegistry("Entities", "sidebar/left", () -> {

            World world = registry.provideAspect(CommonAspectKeys.WORLD).getObject();
            EntityTree tree = new EntityTree(world);
            VisTextButton updateBtn = new VisTextButton("Refresh entity tree");
            updateBtn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    tree.update();
                }
            });

            tree.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    System.out.println("Changed: " + event.toString());
                    detailPanel.clear();
                    Tree.Node node = tree.getSelection().getLastSelected();
                    if(node instanceof ComponentTreeNode) {
                        ComponentTreeNode<?> cNode = (ComponentTreeNode<?>) node;
                        // Raw type to bypass compiler uncertainty check
                        ComponentAdapter adapter = getAdapter(cNode.getComponentClass());
                        //noinspection unchecked
                        VisTable details = adapter.buildControls(cNode.getComponent());
                        detailPanel.add(details).grow().row();
                    }
                }
            });
            VisTable visTable = new VisTable();
            visTable.add(new VisScrollPane(tree)).grow().row();
            visTable.add(updateBtn).growX().row();
            return visTable;
        }));
    }
}
