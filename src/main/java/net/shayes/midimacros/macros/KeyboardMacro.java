package net.shayes.midimacros.macros;

import java.awt.*;
import java.awt.event.KeyEvent;

public class KeyboardMacro extends Macro {
    private static Robot robot;

    private int[] macroKeys;

    public KeyboardMacro(String name, String macro) throws AWTException {
        super(name);

        if (robot == null) robot = new Robot();

        parseMacro(macro);
    }

    private void parseMacro(String macro) {
        macro = macro.replaceAll("\\s", "");

        String[] keys = macro.split("\\+");
        macroKeys = new int[keys.length];

        for (int i = 0; i < keys.length; i++) {
            String key = keys[i];

            switch (key) {
                case "control", "ctrl" ->   macroKeys[i] = KeyEvent.VK_CONTROL;
                case "shift", "shft" ->     macroKeys[i] = KeyEvent.VK_SHIFT;
                case "alt" ->               macroKeys[i] = KeyEvent.VK_ALT;
                case "tab" ->               macroKeys[i] = KeyEvent.VK_TAB;
                case "enter" ->             macroKeys[i] = KeyEvent.VK_ENTER;
                case "delete", "del" ->     macroKeys[i] = KeyEvent.VK_DELETE;
                case "insert", "ins" ->     macroKeys[i] = KeyEvent.VK_INSERT;
                case "end" ->               macroKeys[i] = KeyEvent.VK_END;
                case "home" ->              macroKeys[i] = KeyEvent.VK_HOME;
                case "pagedown", "pgdn" ->  macroKeys[i] = KeyEvent.VK_PAGE_DOWN;
                case "pageup", "pgup" ->    macroKeys[i] = KeyEvent.VK_PAGE_UP;
                default ->                  macroKeys[i] = KeyEvent.getExtendedKeyCodeForChar(key.charAt(0));
            }
        }
    }

    @Override
    public void execute() {
        for (int key : macroKeys) {
            robot.keyPress(key);
        }

        for (int key : macroKeys) {
            robot.keyRelease(key);
        }
    }
}
