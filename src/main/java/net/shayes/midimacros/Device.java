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
    private final List<MidiDevice.Info> info;
    private final List<MidiDevice> device;
    private final Receiver receiver;

    /**
     * Constructs a new device specified by its name.
     *
     * @param name the name of the device on the system
     */
    public Device(String name) {
        this.name = name;
        this.info = new ArrayList<>();
        this.device = new ArrayList<>();

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
                this.info.add(info);
                this.device.add(MidiSystem.getMidiDevice(info));
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

        for (MidiDevice device : this.device) {
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
        for (MidiDevice device : this.device) {
            device.close();
        }
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
