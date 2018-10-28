package net.ncguy.foundation;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class Initialiser {

    public static void Initialize() {
        ShaderProgram.prependVertexCode = "#version 330 core\n";
        ShaderProgram.prependFragmentCode = "#version 330 core\n";
    }

}
