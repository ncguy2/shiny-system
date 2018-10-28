package net.ncguy.render.shader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Attribute;
import com.badlogic.gdx.graphics.g3d.Attributes;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import net.ncguy.Photon;

import static com.badlogic.gdx.graphics.GL20.*;
import static com.badlogic.gdx.graphics.GL30.GL_TEXTURE_3D;

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
    public void begin(Camera camera, RenderContext context) {
        super.begin(camera, context);

        program.setUniformi("u_photonEnabled", Photon.Offline.volumeTextureHandle > 0 ? GL_TRUE : GL_FALSE);

        if(Photon.Offline.CanRender()) {
            Gdx.gl.glActiveTexture(GL_TEXTURE8);
            Gdx.gl.glBindTexture(GL_TEXTURE_3D, Photon.Offline.volumeTextureHandle);
            program.setUniformi("u_photonVolume", 8);

            program.setUniformf("u_photonIntensity", Photon.Offline.globalLightIntensity);
            program.setUniformf("u_photonOrigin", Photon.Offline.actualOrigin);
            program.setUniformf("u_photonExtents", Photon.Offline.actualExtents);

            Gdx.gl.glActiveTexture(GL_TEXTURE0);
        }
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
