package net.shayes.midimacros;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import java.util.HexFormat;

public class LaunchpadMK2 extends Device {
    public static final String DEVICE_NAME = "Launchpad MK2";

    public LaunchpadMK2() {
        super(DEVICE_NAME);
    }

    public void setColor(int led, int red, int green, int blue) throws MidiUnavailableException, InvalidMidiDataException {
        red = mapColor(red);
        green = mapColor(green);
        blue = mapColor(blue);

        byte[] msg = {
            (byte) 0xf0,
            (byte) 0x00,
            (byte) 0x20,
            (byte) 0x29,
            (byte) 0x02,
            (byte) 0x18,
            (byte) 0x0b,
            (byte) led,
            (byte) red,
            (byte) green,
            (byte) blue,
            (byte) 0xf7
        };

        sendSysex(msg);
    }

    public void sessionMode() throws MidiUnavailableException, InvalidMidiDataException {
        // TODO: Simplify
        sendSysex(HexFormat.of().parseHex("f000202902182200f7"));
    }

    private int mapColor(double value) {
        return (int) Math.round(value * 63.0 / 255.0);
    }
}
