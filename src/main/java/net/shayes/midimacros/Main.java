package net.shayes.midimacros;

import net.shayes.midimacros.macros.KeyboardMacro;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import java.awt.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner stdin = new Scanner(System.in);

        LaunchpadMK2 device = new LaunchpadMK2();

        try {
            device.open();

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

            device.sessionMode();

            /*
            int led = 81;
            for (int i = 1; i <= 8; i++) {
                for (int j = 0; j < 8; j++) {
                    int c = j * 6 + 8;
                    device.setColor(led, c, i * 2 + 8, 63 - c);
                    led -= 10;
                }
                led = 81 + i;
            }
             */
        } catch (MidiUnavailableException e) {
            System.err.printf("[ERROR] MIDI unavailable: %s\n", e.getMessage());
            System.exit(1);
        } catch (InvalidMidiDataException e) {
            System.err.printf("[ERROR] Bad MIDI data: %s\n", e.getMessage());
            System.exit(2);
        }

        stdin.nextLine();

        device.close();
    }
}