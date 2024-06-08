package at.fhv.withthem.tasks.task.connecting_wires;

import at.fhv.withthem.tasks.TaskMessage;
import com.fasterxml.jackson.annotation.JsonCreator;

public class OutgoingConnectingWiresMessage extends TaskMessage {
    private final short[][] _plugs;
    private final boolean[] _wires;

    @JsonCreator
    public OutgoingConnectingWiresMessage(short[][] plugs, boolean[] wires){
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
