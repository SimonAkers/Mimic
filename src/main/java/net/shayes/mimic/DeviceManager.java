package net.shayes.mimic;

import org.jetbrains.annotations.Nullable;

import javax.sound.midi.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

public class DeviceManager {
    private final List<MidiDevice.Info> deviceInfos;
    private final List<String> deviceNames;

    public DeviceManager() {
        deviceInfos = new ArrayList<>(Arrays.asList(MidiSystem.getMidiDeviceInfo()));
        deviceNames = new ArrayList<>();

        for (MidiDevice.Info device : deviceInfos) {
            if (!deviceNames.contains(device.getName())) {
                deviceNames.add(device.getName());
            }
        }
    }

    public List<String> getDeviceNames() {
        return deviceNames;
    }

    public List<MidiDevice.Info> getAllDeviceInfo() {
        return deviceInfos;
    }

    public List<MidiDevice.Info> getDeviceInfo(String name) {
        ArrayList<MidiDevice.Info> infos = new ArrayList<>();

        for (MidiDevice.Info info : deviceInfos) {
            if (info.getName().equals(name)) infos.add(info);
        }

        return infos.size() > 0 ? infos : null;
    }

    public List<MidiDevice.Info> getDeviceInfo(int index) {
        return getDeviceInfo(deviceNames.get(index));
    }

    public MidiDevice getDevice(@Nullable MidiDevice.Info info) {
        if (info == null) return null;

        try {
            return MidiSystem.getMidiDevice(info);
        } catch (MidiUnavailableException e) {
            return null;
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        List<String> deviceNames = getDeviceNames();
        ListIterator<String> iter = deviceNames.listIterator();

        while (iter.hasNext()) {
            sb.append(String.format("(%d) - %s\n", iter.nextIndex(), iter.next()));
        }

        return sb.toString();
    }
}
