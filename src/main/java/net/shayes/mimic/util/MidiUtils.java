package net.shayes.mimic.util;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MidiUtils {
    public static final String[] NOTE_NAMES = { "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B" };
    public static final Pattern NOTE_PATTERN = Pattern.compile("([A-G]#?)(-?\\d)");

    public static List<String> deviceNames() {
        MidiDevice.Info[] allDevicesInfo = MidiSystem.getMidiDeviceInfo();

        List<String> names = new ArrayList<>();

        for (MidiDevice.Info info : allDevicesInfo) {
            String name = info.getName();

            if (!names.contains(name)) {
                names.add(name);
            }
        }

        return names;
    }

    public static String noteName(int note) {
        int octave = (note / 12) - 1;
        int index = note % 12;
        return NOTE_NAMES[index] + octave;
    }

    /**
     * Converts a note name to its MIDI note value.
     *
     * <p>
     * The note name must be in the form [note][octave] and must use sharp notation (i.e. no flats).
     * In more technical terms, it must match this regex pattern: /[A-G]#?-?\d/
     *
     * <p>
     * Proper format examples: C4, D#3, f7
     * Bad format examples: C, Eb3, f
     *
     * @param noteName The name of the note
     * @return The MIDI value corresponding to the note, or -1 if the note is not
     * properly formatted or does not exist
     */
    public static int note(String noteName) {
        noteName = noteName.toUpperCase();

        Matcher matcher = NOTE_PATTERN.matcher(noteName);

        String note;
        int octave;

        if (matcher.matches()) {
            note = matcher.group(1);
            octave = Integer.parseInt(matcher.group(2));
        } else {
            return -1;
        }

        for (int i = 0; i < NOTE_NAMES.length; i++) {
            if (NOTE_NAMES[i].equals(note)) {
                return (octave + 1) * 12 + i;
            }
        }

        return -1;
    }
}
