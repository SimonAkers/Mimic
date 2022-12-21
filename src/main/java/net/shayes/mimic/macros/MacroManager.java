package net.shayes.mimic.macros;

import net.shayes.mimic.Device;
import net.shayes.mimic.util.MidiUtils;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class MacroManager implements Device.EventListener {
    private final Device device;
    private final HashMap<Integer, List<Macro>> macros;

    public MacroManager(Device device) {
        this.device = device;
        macros = new HashMap<>();

        device.setEventListener(this);
    }

    @Override
    public void onEvent(MidiMessage message, long timeStamp) {
        byte[] msg = message.getMessage();
        int status = message.getStatus();
        int note = msg[1];
        int velocity = msg[2];

        String noteName = MidiUtils.noteName(msg[1]);

         if (status == ShortMessage.NOTE_OFF || velocity == 0) {
             System.out.println("RELEASED " + noteName);
         } else if (status == ShortMessage.NOTE_ON || velocity > 0) {
             System.out.println("PRESSED " + noteName);
             runMacros(note);
         }
    }

    private void runMacros(int note) {
        if (macros.containsKey(note)) {
            for (Macro macro : macros.get(note)) {
                macro.execute();
            }
        }
    }

    public void addMacro(int note, Macro macro) {
        if (!macros.containsKey(note)) {
            List<Macro> list = new LinkedList<>();
            macros.put(note, list);
        }

        macros.get(note).add(macro);
    }

    public void addMacro(String noteName, Macro macro) {
        addMacro(MidiUtils.note(noteName), macro);
    }
}
