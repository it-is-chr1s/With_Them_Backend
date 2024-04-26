package at.fhv.withthem.MeetingLogic.task;

import at.fhv.withthem.MeetingLogic.EmergencyMeetingMessage;
import com.fasterxml.jackson.annotation.JsonCreator;

public class OutgoingConnectingWiresMessage extends EmergencyMeetingMessage {
    private final short[][] _plugs;
    private final boolean[] _wires;

    @JsonCreator
    public OutgoingConnectingWiresMessage(short[][] plugs, boolean[] wires) {
        super("Connecting Wires");
        _plugs = plugs;
        _wires = wires;
    }

    public short[][] getPlugs() {
        return _plugs;
    }

    public boolean[] getWires() {
        return _wires;
    }

}
