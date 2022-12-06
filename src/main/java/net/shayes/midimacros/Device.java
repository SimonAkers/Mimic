package net.shayes.midimacros;

import javax.sound.midi.*;
import java.util.ArrayList;
import java.util.List;

public class Device {
    private boolean initialized = false;
    private EventListener eventListener;

    private final String name;
    private final List<MidiDevice.Info> info;
    private final List<MidiDevice> device;
    private final Receiver receiver;

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

    public void close() {
        for (MidiDevice device : this.device) {
            device.close();
        }
    }

    public void setEventListener(EventListener eventListener) {
        this.eventListener = eventListener;
    }

    public interface EventListener {
        void onEvent(MidiMessage message, long timeStamp);
    }
}
