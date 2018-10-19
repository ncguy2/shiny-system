package net.ncguy.render.shader;

import com.badlogic.gdx.graphics.g3d.Attribute;
import com.badlogic.gdx.graphics.g3d.Attributes;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class MaterialShader extends DefaultShader {
    public MaterialShader(Renderable renderable) {
        super(renderable);
    }

    public MaterialShader(Renderable renderable, Config config) {
        super(renderable, config);
    }

    public MaterialShader(Renderable renderable, Config config, String prefix) {
        super(renderable, config, prefix);
    }

    public MaterialShader(Renderable renderable, Config config, String prefix, String vertexShader, String fragmentShader) {
        super(renderable, config, prefix, vertexShader, fragmentShader);
    }

    public MaterialShader(Renderable renderable, Config config, ShaderProgram shaderProgram) {
        super(renderable, config, shaderProgram);
    }

    @Override
    protected void bindMaterial(Attributes attributes) {
        super.bindMaterial(attributes);
        for (final Attribute attr : attributes) {
            final long t = attr.type;
            if(ColorAttribute.is(t)) {
                ColorAttribute cAttr = (ColorAttribute) attr;
                if(t == ColorAttribute.Diffuse) {
                    set(u_diffuseColor, cAttr.color);
                }
            }
        }
    }



}
