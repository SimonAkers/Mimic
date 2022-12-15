package net.shayes.midimacros.util;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import java.util.ArrayList;
import java.util.List;

public class MidiUtils {
    public static final String[] NOTE_NAMES = { "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B" };

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
}
