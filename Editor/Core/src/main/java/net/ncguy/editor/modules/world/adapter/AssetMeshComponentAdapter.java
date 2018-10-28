package net.ncguy.editor.modules.world.adapter;

import com.kotcrab.vis.ui.widget.VisTable;
import net.ncguy.editor.modules.world.adapter.widget.Layout;
import net.ncguy.editor.modules.world.adapter.widget.StringInput;
import net.ncguy.foundation.data.components.mesh.AssetMeshComponent;

public class AssetMeshComponentAdapter extends SceneComponentAdapter<AssetMeshComponent> {

    @Override
    public void buildControls(VisTable table, AssetMeshComponent component) {
        Layout.section(table, "Asset Mesh", true, t -> {
            Layout.addLabeledWidget(t, "Reference", StringInput.textField("Path", component::getAssetPath, component::setAssetPath));
        });

        super.buildControls(table, component);
    }

}
