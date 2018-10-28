package net.ncguy.editor.editor;

import com.kotcrab.vis.ui.widget.VisWindow;
import net.ncguy.editor.display.viewports.Viewport;

public class ViewportFrame extends VisWindow {

    private final Viewport viewport;

    public ViewportFrame(String title, Viewport viewport) {
        super(title);
        this.viewport = viewport;
        setResizable(true);
        addActor(viewport);
    }

    @Override
    protected void sizeChanged() {
        super.sizeChanged();
        this.setDebug(false, true);
        if(viewport != null) {
            viewport.setPosition(getPadLeft(), getPadBottom());
            viewport.setSize(getWidth() - getPadX(), getHeight() - getPadY());
        }
    }
}
