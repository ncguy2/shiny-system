package net.ncguy.editor.modules.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.kotcrab.vis.ui.widget.Tooltip;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import net.ncguy.editor.utils.IMenuProvider;
import net.ncguy.foundation.data.components.EntityComponent;
import net.ncguy.foundation.data.components.SceneComponent;
import net.ncguy.foundation.data.tree.VisitableTree;
import net.ncguy.foundation.ui.MenuBuilder;
import net.ncguy.foundation.ui.SelectionDialog;
import org.atteo.classindex.ClassIndex;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class ComponentTreeActor<T extends EntityComponent<?>> extends VisTable implements IMenuProvider {

    protected T component;
    protected VisLabel nameLabel;

    public ComponentTreeActor(T component) {
        this.component = component;
        nameLabel = new VisLabel();
        add(nameLabel).padLeft(4).grow().row();
        if (component instanceof SceneComponent) {
            IMenuProvider.addMenuListener(this);
        }
    }

    public static void selectComponent(Stage stage, Consumer<Class<? extends EntityComponent>> onSelection) {
        SelectionDialog<Class<? extends EntityComponent>> d = new SelectionDialog<>("Components");
        Iterable<Class<? extends EntityComponent>> subclasses = ClassIndex.getSubclasses(EntityComponent.class);
        VisitableTree<Class<? extends EntityComponent>> data = new VisitableTree<>(EntityComponent.class);

        Map<Class<? extends EntityComponent>, VisitableTree<Class<? extends EntityComponent>>> map = new HashMap<>();

        map.put(EntityComponent.class, data);

        Set<Class<? extends EntityComponent>> classes = new HashSet<>();
        subclasses.forEach(classes::add);

        Set<Class<? extends EntityComponent>> processedClasses = new HashSet<>();
        while (!classes.isEmpty()) {
            for (Class<? extends EntityComponent> cls : classes) {
                Class<?> superclass = cls.getSuperclass();
                if (map.containsKey(superclass)) {
                    VisitableTree<Class<? extends EntityComponent>> node = map.get(superclass).child(cls);
                    map.put(cls, node);
                    processedClasses.add(cls);
                }
            }

            classes.removeAll(processedClasses);
            processedClasses.clear();
        }

        d.setData(data);

        d.setNodeBuilder(t -> {
            VisTable table = new VisTable();
            VisLabel label = new VisLabel(t.getSimpleName());
            table.add(label).padLeft(4).growX().row();

            if (Modifier.isAbstract(t.getModifiers())) {
                label.setColor(Color.RED);
            }

            String summary = ClassIndex.getClassSummary(t);

            if (summary != null && !summary.isEmpty()) {
                new Tooltip.Builder(summary)
                        .target(table)
                        .build();
            }

            return table;
        });

        d.addSelectionListener(t -> {
            if (Modifier.isAbstract(t.getModifiers())) {
                System.out.println("Cannot add abstract components");
                return;
            }

            onSelection.accept(t);
        });

        stage.addActor(d.show());
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        nameLabel.setText(component.Name());
    }

    @Override
    public void provideMenu(MenuBuilder.MenuNode node) {
        node.Add("Add Component", () -> {
            selectComponent(getStage(), t -> {
                try {
                    ((SceneComponent<?>) component).add(t);
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
                    e.printStackTrace();
                }
            });
        });
    }
}
