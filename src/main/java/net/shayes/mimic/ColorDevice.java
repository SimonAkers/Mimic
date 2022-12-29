package net.shayes.mimic;

import net.shayes.mimic.util.MidiUtils;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import java.awt.*;

public abstract class ColorDevice extends Device {
    /**
     * Constructs a new device specified by its name.
     *
     * @param name the name of the device on the system
     */
    public ColorDevice(String name) {
        super(name);
    }

    public void setColor(String noteName, String hexColor) throws MidiUnavailableException, InvalidMidiDataException {
        setColor(MidiUtils.note(noteName), hexColor);
    }

    public void setColor(int note, String hexColor) throws MidiUnavailableException, InvalidMidiDataException {
        Color color = Color.decode(hexColor);
        setColor(note, color.getRed(), color.getGreen(), color.getBlue());
    }

    public abstract void setColor(int note, int red, int green, int blue) throws MidiUnavailableException, InvalidMidiDataException;
}
