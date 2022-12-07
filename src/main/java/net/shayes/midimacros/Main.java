package net.shayes.midimacros;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import java.util.Arrays;
import java.util.HexFormat;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner stdin = new Scanner(System.in);

        DeviceManager deviceManager = new DeviceManager();
        System.out.println(deviceManager);

        System.out.print("Select a device by number: ");
        String deviceName = deviceManager.getDeviceNames().get(stdin.nextInt());

        Device device = new Device(deviceName);

        try {
            device.open();
            device.setEventListener((msg, time) -> System.out.println(Arrays.toString(msg.getMessage())));

            device.sendSysex(HexFormat.of().parseHex("f000202902182200f7"));
            device.sendSysex(HexFormat.of().parseHex("f0 00 20 29 02 18 0b 51 3f 00 00 f7".replace(" ", "")));
        } catch (MidiUnavailableException e) {
            System.err.printf("[ERROR] MIDI unavailable: %s\n", e.getMessage());
            System.exit(1);
        } catch (InvalidMidiDataException e) {
            System.err.printf("[ERROR] Bad MIDI data: %s\n", e.getMessage());
            e.printStackTrace();
            System.exit(2);
        }

        stdin.nextLine();
        stdin.nextLine();

        device.close();
    }
}