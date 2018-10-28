package net.ncguy.editor.modules.world.adapter.widget;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.widget.VisTextField;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class StringInput {

    public static VisTextField textField(String messageText, Supplier<String> textSupplier, Consumer<String> onChange) {
        VisTextField field = new VisTextField(textSupplier.get());
        field.setMessageText(messageText);
        field.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                onChange.accept(field.getText());
            }
        });
        return field;
    }

}
