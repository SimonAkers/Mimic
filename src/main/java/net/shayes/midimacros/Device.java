package net.shayes.midimacros;

import javax.sound.midi.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A wrapper class for communicating with MIDI devices.
 * (Currently only supports input from a device)
 */
public class Device {
    private boolean initialized = false;
    private EventListener eventListener;

    private final String name;
    private final List<MidiDevice.Info> infos;
    private final List<MidiDevice> devices;
    private final Receiver receiver;

    /**
     * Constructs a new device specified by its name.
     *
     * @param name the name of the device on the system
     */
    public Device(String name) {
        this.name = name;
        this.infos = new ArrayList<>();
        this.devices = new ArrayList<>();

        // Create a receiver to pass messages to the event listener
        receiver = new Receiver() {
            @Override
            public void send(MidiMessage message, long timeStamp) {
                if (eventListener != null) {
                    eventListener.onEvent(message, timeStamp);
                }
            }

            @Override
            public void close() { }
        };
    }

    /**
     * Initializes the device.
     *
     * @throws MidiUnavailableException if the device is unavailable
     */
    private void init() throws MidiUnavailableException {
        MidiDevice.Info[] allDevicesInfo = MidiSystem.getMidiDeviceInfo();

        for (MidiDevice.Info info : allDevicesInfo) {
            if (info.getName().equals(this.name)) {
                this.infos.add(info);
                this.devices.add(MidiSystem.getMidiDevice(info));
            }
        }

        initialized = true;
    }

    /**
     * Initializes the device and opens a connection to it.
     *
     * @throws MidiUnavailableException if the device is unavailable
     */
    public void open() throws MidiUnavailableException {
        if (!initialized) {
            init();
        }

        for (MidiDevice device : this.devices) {
            device.open();

            if (device.getMaxTransmitters() != 0) {
                device.getTransmitter().setReceiver(receiver);
            }
        }
    }

    /**
     * Closes the connection to the device.
     */
    public void close() {
        for (MidiDevice device : this.devices) {
            device.close();
        }
    }

    public void send(MidiMessage message, long timeStamp) throws MidiUnavailableException {
        for (MidiDevice device : devices) {
            if (device.getMaxReceivers() != 0) {
                device.getReceiver().send(message, -1);
            }
        }
    }

    public void sendSysex(byte[] bytes) throws MidiUnavailableException, InvalidMidiDataException {
        SysexMessage msg = new SysexMessage(bytes, bytes.length);
        send(msg, -1);
    }

    public void sendShort(int status, int data1, int data2) throws MidiUnavailableException, InvalidMidiDataException {
        ShortMessage msg = new ShortMessage(status, data1, data2);
        send(msg, -1);
    }

    /**
     * Sets the event listener for the device.
     *
     * @param eventListener the event listener for the device
     */
    public void setEventListener(EventListener eventListener) {
        this.eventListener = eventListener;
    }

    /**
     * An interface to listen for device events.
     */
    public interface EventListener {
        /**
         * Called when the device sends an event.
         *
         * @param message the message from the device
         * @param timeStamp the time stamp corresponding to the message
         */
        void onEvent(MidiMessage message, long timeStamp);
    }
}
