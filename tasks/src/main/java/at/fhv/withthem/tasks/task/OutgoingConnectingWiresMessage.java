package at.fhv.withthem.tasks.task;

import at.fhv.withthem.tasks.TaskMessage;
import com.fasterxml.jackson.annotation.JsonCreator;

public class OutgoingConnectingWiresMessage extends TaskMessage {
    private final short[][] _plugs;
    private final short[][] _wires;

    @JsonCreator
    public OutgoingConnectingWiresMessage(short[][] plugs, short[][] wires){
        super("Connecting Wires");
        _plugs = plugs;
        _wires = wires;
    }

    public short[][] getPlugs() {
        return _plugs;
    }

    public short[][] getWires() {
        return _wires;
    }

}
