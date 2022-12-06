package net.shayes.midimacros;

import javax.sound.midi.MidiUnavailableException;
import java.util.Arrays;
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
        } catch (MidiUnavailableException e) {
            System.err.printf("[ERROR] MIDI unavailable: %s\n", e.getMessage());
            System.exit(1);
        }

        stdin.nextLine();
        stdin.nextLine();

        device.close();
    }
}