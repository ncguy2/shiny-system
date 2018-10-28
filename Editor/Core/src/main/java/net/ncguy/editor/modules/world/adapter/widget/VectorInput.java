package net.ncguy.editor.modules.world.adapter.widget;

import com.badlogic.gdx.math.Vector3;
import com.kotcrab.vis.ui.widget.VisTable;
import net.ncguy.core.GameLauncher;

import java.util.function.Supplier;

import static net.ncguy.editor.modules.world.adapter.widget.FloatInput.spinner;

public class VectorInput {

    public static VisTable vector3(Supplier<Vector3> vectorSupplier) {
        VisTable table = new VisTable();

        table.add(spinner(1, () -> vectorSupplier.get().x, f -> vectorSupplier.get().x = f));
        table.add(spinner(1, () -> vectorSupplier.get().y, f -> vectorSupplier.get().y = f));
        table.add(spinner(1, () -> vectorSupplier.get().z, f -> vectorSupplier.get().z = f));
        table.row();

        return table;
    }

    public static VisTable direction(Supplier<Vector3> vectorSupplier) {
        VisTable table = new VisTable();

        table.add(spinner(1, () -> vectorSupplier.get().x, f -> vectorSupplier.get().x = f));
        table.add(spinner(1, () -> vectorSupplier.get().y, f -> vectorSupplier.get().y = f));
        table.add(spinner(1, () -> vectorSupplier.get().z, f -> vectorSupplier.get().z = f));
        table.row();

        // Readonly
        table.add(new GameLauncher.PollingLabel(() -> vectorSupplier.get().cpy().nor().x + ""));
        table.add(new GameLauncher.PollingLabel(() -> vectorSupplier.get().cpy().nor().y + ""));
        table.add(new GameLauncher.PollingLabel(() -> vectorSupplier.get().cpy().nor().z + ""));
        table.row();

        return table;
    }

}
