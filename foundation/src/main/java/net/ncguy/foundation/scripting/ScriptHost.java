package net.ncguy.foundation.scripting;

import javax.script.*;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

public class ScriptHost {

    private static ScriptHost instance;
    ScriptEngine nashorn;
    Map<Object, ScriptContext> scopes;

    private ScriptHost() {
        ScriptEngineManager mgr = new ScriptEngineManager();
        nashorn = mgr.getEngineByName("nashorn");
        scopes = new HashMap<>();
        // Default scope
        SimpleScriptContext ctx = new SimpleScriptContext();
        ctx.setBindings(nashorn.createBindings(), ScriptContext.ENGINE_SCOPE);
        scopes.put(nashorn, ctx);
    }

    public static ScriptHost get() {
        if (instance == null) {
            instance = new ScriptHost();
        }
        return instance;
    }

    public ScriptContext getDefaultScope() {
        return scopes.get(nashorn);
    }

    public ScriptContext getScope(Object obj) {
        ScriptContext ctx = scopes.get(obj);
        if (ctx == null) {
            ctx = new SimpleScriptContext();
            ctx.setBindings(nashorn.createBindings(), ScriptContext.ENGINE_SCOPE);
            scopes.put(obj, ctx);
        }
        return ctx;
    }

    public ScriptEngine getEngine() {
        return nashorn;
    }

    public Object eval(String script) throws ScriptException {
        return getEngine().eval(script);
    }

    public Object eval(String script, Object scope) throws ScriptException {
        return getEngine().eval(script, getScope(scope));
    }

    public Object eval(Reader script) throws ScriptException {
        return getEngine().eval(script);
    }

    public Object eval(Reader script, Object scope) throws ScriptException {
        return getEngine().eval(script, getScope(scope));
    }

}
