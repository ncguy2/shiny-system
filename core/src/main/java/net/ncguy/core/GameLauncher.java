package net.ncguy.core;

import com.badlogic.gdx.Game;
import com.kotcrab.vis.ui.VisUI;

public class GameLauncher extends Game {

    @Override
    public void create() {
        VisUI.load();
    }

}
