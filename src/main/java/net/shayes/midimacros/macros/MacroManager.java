package net.shayes.midimacros.macros;

import net.shayes.midimacros.Device;
import net.shayes.midimacros.util.MidiUtils;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;

public class MacroManager implements Device.EventListener {
    Macro[] macros;

    public MacroManager() {
        macros = new Macro[128];
    }

    @Override
    public void onEvent(MidiMessage message, long timeStamp) {
        byte[] msg = message.getMessage();
        int status = message.getStatus();
        int velocity = msg[2];

        String note = MidiUtils.noteName(msg[1]);

         if (status == ShortMessage.NOTE_OFF || velocity == 0) {
             System.out.println("RELEASED " + note);
         } else if (status == ShortMessage.NOTE_ON || velocity > 0) {
             System.out.println("PRESSED " + note);
         }
    }
}
