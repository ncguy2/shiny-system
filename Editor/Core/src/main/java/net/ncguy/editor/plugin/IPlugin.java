package net.ncguy.editor.plugin;

import net.ncguy.editor.editor.ui.EditorRegistry;

public interface IPlugin {

    String name();
    void register(EditorRegistry registry);

}
