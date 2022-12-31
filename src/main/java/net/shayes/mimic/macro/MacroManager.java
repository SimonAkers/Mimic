package net.shayes.mimic.macro;

import net.shayes.mimic.device.Device;
import net.shayes.mimic.util.MidiUtils;

import org.tomlj.Toml;
import org.tomlj.TomlArray;
import org.tomlj.TomlParseResult;
import org.tomlj.TomlTable;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

// TODO: Add code comments to MacroManager class

/**
 * A class for managing macros for a given device.
 * <p>
 * This class does NOT manage device connections. For managing device connections,
 * refer to the following "see also" section.
 *
 * @see Device
 * @see Device#open()
 * @see Device#close()
 */
public class MacroManager implements Device.EventListener {
    private final Device device;
    private final HashMap<Integer, List<Macro>> macros;

    /**
     * Constructs a new MacroManager for a given device.
     *
     * @param device The device to manage macros for.
     */
    public MacroManager(Device device) {
        this.device = device;
        macros = new HashMap<>();

        device.setEventListener(this);
    }

    /**
     * {@inheritDoc}
     *
     * As of now, this implementation simply runs the macros registered to a
     * note if that note is pressed, as well as printing to stdout when a note
     * is pressed or released.
     */
    @Override
    public void onEvent(MidiMessage message, long timeStamp) {
        byte[] msg = message.getMessage();
        int status = message.getStatus();
        int note = msg[1];
        int velocity = msg[2];

        String noteName = MidiUtils.noteName(msg[1]);

         if (status == ShortMessage.NOTE_OFF || velocity == 0) {
             System.out.println("RELEASED " + noteName);
         } else if (status == ShortMessage.NOTE_ON || velocity > 0) {
             System.out.println("PRESSED " + noteName);
             runMacros(note);
         }
    }

    /**
     * Loads macros from a TOML file.
     *
     * @param file The name (path) of the file.
     *
     * @throws IOException If the file could not be accessed.
     * @throws AWTException If the macro could not be initialized.
     */
    public void loadMacros(String file) throws IOException, AWTException {
        loadMacros(Paths.get(file));
    }

    /**
     * Loads macros from a TOML file.
     *
     * @param file The Path object representing the file.
     *
     * @throws IOException If the file could not be accessed.
     * @throws AWTException If the macro could not be initialized.
     */
    public void loadMacros(Path file) throws IOException, AWTException {
        TomlParseResult result = Toml.parse(file);

        TomlArray keybinds = result.getArray("keybinds");

        if (keybinds != null) {
            for (int i = 0; i < keybinds.size(); i++) {
                TomlTable table = keybinds.getTable(i);

                String name = table.getString("name");
                String note = table.getString("note");
                String keybind = table.getString("keybind");

                Macro macro = new KeyboardMacro(name, keybind);
                addMacro(note, macro);
            }
        }
    }

    /**
     * Runs macros registered to a given note.
     *
     * @param note The note to run registered macros for.
     */
    private void runMacros(int note) {
        if (macros.containsKey(note)) {
            for (Macro macro : macros.get(note)) {
                macro.execute();
            }
        }
    }

    /**
     * Registers a macro to a given note. Notes may have multiple macros
     * registered to them.
     *
     * @param note The MIDI value of the note to register the macro to.
     * @param macro The macro to register.
     */
    public void addMacro(int note, Macro macro) {
        if (!macros.containsKey(note)) {
            List<Macro> list = new LinkedList<>();
            macros.put(note, list);
        }

        macros.get(note).add(macro);
    }

    /**
     * Registers a macro to a given note. Notes may have multiple macros
     * registered to them.
     *
     * @param noteName The name of the note to register the macro to.
     * @param macro The macro to register.
     */
    public void addMacro(String noteName, Macro macro) {
        addMacro(MidiUtils.note(noteName), macro);
    }
}
