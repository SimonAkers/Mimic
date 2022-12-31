package net.shayes.mimic.device;

import net.shayes.mimic.util.MidiUtils;
import org.tomlj.Toml;
import org.tomlj.TomlArray;
import org.tomlj.TomlParseResult;
import org.tomlj.TomlTable;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class ColorDevice extends Device {
    /**
     * Constructs a new device specified by its name.
     *
     * @param name the name of the device on the system
     */
    public ColorDevice(String name) {
        super(name);
    }

    /**
     * Loads and applies colors to the device from a TOML file.
     *
     * @param file The name (path) of the file.
     *
     * @throws MidiUnavailableException If the device is unavailable.
     * @throws InvalidMidiDataException If the color data is invalid.
     * @throws IOException If the file could not be accessed.
     */
    public void loadColors(String file) throws MidiUnavailableException, InvalidMidiDataException, IOException {
        loadColors(Paths.get(file));
    }

    /**
     * Loads and applies colors to the device from a TOML file.
     *
     * @param file The Path object representing the file.
     *
     * @throws MidiUnavailableException If the device is not available.
     * @throws InvalidMidiDataException If the color data is invalid.
     * @throws IOException If the file could not be accessed.
     */
    public void loadColors(Path file) throws IOException, MidiUnavailableException, InvalidMidiDataException {
        TomlParseResult result = Toml.parse(file);

        TomlArray colors = result.getArray("colors");

        if (colors != null) {
            for (int i = 0; i < colors.size(); i++) {
                TomlTable table = colors.getTable(i);

                String note = table.getString("note");
                String color = table.getString("color");

                setColor(note, color);
            }
        }
    }

    /**
     * Sets the color of a given note.
     *
     * @param noteName The name of the note to color.
     * @param hexColor The color to apply, in hex form (e.g., A4DE67, #FFFFFF)
     *
     * @throws MidiUnavailableException If the device is not available.
     * @throws InvalidMidiDataException If the color data is invalid.
     */
    public void setColor(String noteName, String hexColor) throws MidiUnavailableException, InvalidMidiDataException {
        setColor(MidiUtils.note(noteName), hexColor);
    }

    /**
     * Sets the color of a given note.
     *
     * @param note The MIDI value of the note to color.
     * @param hexColor The color to apply, in hex form (e.g., A4DE67, #FFFFFF)
     *
     * @throws MidiUnavailableException If the device is not available.
     * @throws InvalidMidiDataException If the color data is invalid.
     */
    public void setColor(int note, String hexColor) throws MidiUnavailableException, InvalidMidiDataException {
        hexColor = hexColor.charAt(0) == '#' ? hexColor : '#' + hexColor;

        Color color = Color.decode(hexColor);
        setColor(note, color.getRed(), color.getGreen(), color.getBlue());
    }

    /**
     * Sets the color of a given note.
     *
     * @param note The MIDI value of the note to color.
     * @param red The red value of the color from 0-255.
     * @param green The green value of the color from 0-255.
     * @param blue The blue value of the color from 0-255.
     *
     * @throws MidiUnavailableException If the device is not available.
     * @throws InvalidMidiDataException If the color data is invalid.
     */
    public abstract void setColor(int note, int red, int green, int blue) throws MidiUnavailableException, InvalidMidiDataException;
}
