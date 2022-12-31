package net.shayes.mimic.macro;

import javax.script.*;

public class ScriptMacro extends Macro {
    public static void main(String[] args) throws ScriptException {
        ScriptMacro parser = new ScriptMacro("groovy");
    }

    public ScriptMacro(String lang) throws ScriptException {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName(lang);
    }

    @Override
    public void execute() {

    }
}