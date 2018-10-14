package net.ncguy.render;

import com.badlogic.gdx.graphics.g3d.RenderableProvider;

public abstract class BasicRenderer {


    public abstract void init();
    public abstract void render(RenderableProvider provider);
    public abstract void dispose();
    public abstract void resize(int width, int height);


}
