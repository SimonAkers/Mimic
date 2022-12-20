package net.shayes.midimacros;

import net.shayes.midimacros.macros.KeyboardMacro;
import net.shayes.midimacros.macros.MacroManager;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import java.awt.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner stdin = new Scanner(System.in);

        LaunchpadMK2 device = new LaunchpadMK2();
        MacroManager manager = new MacroManager(device);

        try {
            device.open();
            device.sessionMode();

            manager.addMacro("D#2", new KeyboardMacro("Discord Mute", "alt + `"));
        } catch (MidiUnavailableException e) {
            System.err.printf("[ERROR] MIDI unavailable: %s\n", e.getMessage());
            System.exit(1);
        } catch (InvalidMidiDataException e) {
            System.err.printf("[ERROR] Bad MIDI data: %s\n", e.getMessage());
            System.exit(2);
        } catch (AWTException e) {
            System.err.printf("[ERROR] Could not start robot: %s\n", e.getMessage());
            System.exit(3);
        }

        stdin.nextLine();

        device.close();
    }
}

/*

device.setEventListener((msg, time) -> {
    int led = msg.getMessage()[1];
    int vel = msg.getMessage()[2];

    try {
        if (vel == 0) {
            device.setColor(led, 0, 0, 0);
        } else {
            device.setColor(led, 255, 20, 255);
        }
    } catch (MidiUnavailableException | InvalidMidiDataException ignored) { }

    if (vel > 64 && led == 39) {
        try {
            new KeyboardMacro("mute discord", "alt + `").execute();
        } catch (AWTException ignored) { }
    }
});

*/