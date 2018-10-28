package net.ncguy.editor.modules.world.adapter.widget;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.widget.VisCheckBox;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class BooleanInput {

    public static VisCheckBox checkbox(Supplier<Boolean> valueSupplier, Consumer<Boolean> onChange) {
        VisCheckBox box = new VisCheckBox("");
        box.setChecked(valueSupplier.get());

        box.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                onChange.accept(box.isChecked());
            }
        });

        return box;
    }

}
