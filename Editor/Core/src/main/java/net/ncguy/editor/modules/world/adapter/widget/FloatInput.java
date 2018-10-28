package net.ncguy.editor.modules.world.adapter.widget;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.widget.spinner.SimpleFloatSpinnerModel;
import com.kotcrab.vis.ui.widget.spinner.Spinner;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class FloatInput {

    public static Spinner spinner(float step, Supplier<Float> valueSupplier, Consumer<Float> onChange) {
        return spinner(-65536f, 65536f, step, valueSupplier, onChange);
    }
    public static Spinner spinner(float min, float max, float step, Supplier<Float> valueSupplier, Consumer<Float> onChange) {
        SimpleFloatSpinnerModel model = new SimpleFloatSpinnerModel(valueSupplier.get(), min, max, step);
        Spinner spinner = new Spinner("", model);
        spinner.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                onChange.accept(model.getValue());
            }
        });
        return spinner;
    }

}
