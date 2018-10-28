package net.ncguy.editor.plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;
import java.util.function.Consumer;

public class PluginManager {

    private static PluginManager instance;

    public static PluginManager get() {
        if (instance == null) {
            instance = new PluginManager();
        }
        return instance;
    }

    List<IPlugin> plugins;

    private PluginManager() {
        plugins = new ArrayList<>();
    }

    private static boolean isJar(File file) {
        return file.getName().endsWith(".jar");
    }

    public void load() {
        ServiceLoader<IPlugin> loader = ServiceLoader.load(IPlugin.class);
        loader.iterator().forEachRemaining(this::load);
    }

    public void load(IPlugin plugin) {
        System.out.println("Loaded plugin with name: " + plugin.name());
        plugins.add(plugin);
    }

    public void forEach(Consumer<IPlugin> task) {
        plugins.forEach(task);
    }
}
