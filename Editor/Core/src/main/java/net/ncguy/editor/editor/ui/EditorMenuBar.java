package net.ncguy.editor.editor.ui;

import com.kotcrab.vis.ui.widget.Menu;
import com.kotcrab.vis.ui.widget.MenuBar;
import net.ncguy.editor.editor.ui.registry.MenuItemRegistry;

import java.util.HashMap;
import java.util.Map;

public class EditorMenuBar extends MenuBar {

    Map<String, Menu> rootMenus;

    public EditorMenuBar(final EditorRegistry registry) {
        rootMenus = new HashMap<>();

        registry.streamOf(MenuItemRegistry.class).forEach(this::accept);
    }

    private void addRootMenu(String name) {
        Menu menu = new Menu(name);
        rootMenus.put(name, menu);
        this.addMenu(menu);
    }

    private void accept(MenuItemRegistry itemRegister) {
        String semanticPath = itemRegister.semanticPath;
        if(!rootMenus.containsKey(semanticPath)) {
            addRootMenu(semanticPath);
        }

        rootMenus.get(semanticPath)
                .addItem(itemRegister.build());
    }

}
