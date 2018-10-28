package net.ncguy.editor.editor.ui.registry;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.widget.MenuItem;

public class MenuItemRegistry extends ItemRegistryElement<MenuItemRegistry, MenuItem> {

    public MenuItemRegistry(String name, String semanticPath, Runnable task) {
        super(name, semanticPath, r -> new MenuItem(name, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(task != null) {
                    task.run();
                }
            }
        }));
    }
}
