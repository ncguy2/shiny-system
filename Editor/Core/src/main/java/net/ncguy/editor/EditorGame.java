package net.ncguy.editor;

import com.badlogic.gdx.Game;
import com.kotcrab.vis.ui.VisUI;
import net.ncguy.editor.display.EditorScreen;
import net.ncguy.editor.plugin.PluginManager;
import net.ncguy.foundation.data.asset.AssetHandler;

public class EditorGame extends Game {

    @Override
    public void create() {
        VisUI.load();
        PluginManager.get().load();

        AssetHandler.Start();
        setScreen(new EditorScreen());
    }

    @Override
    public void render() {
        AssetHandler.WithInstanceIfExists(AssetHandler::Update);
        super.render();
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
