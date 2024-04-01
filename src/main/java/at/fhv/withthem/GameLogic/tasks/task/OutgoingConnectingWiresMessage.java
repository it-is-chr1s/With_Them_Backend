package at.fhv.withthem.GameLogic.tasks.task;

import at.fhv.withthem.GameLogic.tasks.TaskMessage;
import com.fasterxml.jackson.annotation.JsonCreator;

public class OutgoingConnectingWiresMessage extends TaskMessage {
    private final short[][] _wires;

    @JsonCreator
    public OutgoingConnectingWiresMessage(short[][] wires){
        super("TaskConnectingWires");
        _wires = wires;
    }

    public short[][] getWires() {
        return _wires;
    }
}
