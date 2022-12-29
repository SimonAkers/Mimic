package net.shayes.mimic;

import net.shayes.mimic.macros.MacroManager;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import java.awt.*;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner stdin = new Scanner(System.in);

        LaunchpadMK2 device = new LaunchpadMK2();
        MacroManager manager = new MacroManager(device);

        try {
            device.open();
            device.sessionMode();

            device.loadColors("data/colors.toml");
            manager.loadMacros("data/macros.toml");

            //manager.addMacro("D#2", new KeyboardMacro("Discord Mute", "alt + `"));
        } catch (MidiUnavailableException e) {
            System.err.printf("[ERROR] MIDI unavailable: %s\n", e.getMessage());
            System.exit(1);
        } catch (InvalidMidiDataException e) {
            System.err.printf("[ERROR] Bad MIDI data: %s\n", e.getMessage());
            System.exit(2);
        } catch (AWTException e) {
            System.err.printf("[ERROR] Could not start robot: %s\n", e.getMessage());
            System.exit(3);
        } catch (IOException e) {
            System.err.printf("[ERROR] Could not load macros file: %s\n", e.getMessage());
            System.exit(4);
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