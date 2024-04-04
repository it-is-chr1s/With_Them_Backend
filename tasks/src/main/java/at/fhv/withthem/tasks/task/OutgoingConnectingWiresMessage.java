package at.fhv.withthem.tasks.task;

import at.fhv.withthem.tasks.TaskMessage;
import com.fasterxml.jackson.annotation.JsonCreator;

public class OutgoingConnectingWiresMessage extends TaskMessage {
    private final short[][] _wires;
    private final int _counter;

    @JsonCreator
    public OutgoingConnectingWiresMessage(short[][] wires, int counter){
        super("TaskConnectingWires");
        _wires = wires;
        _counter = counter;
    }

    public short[][] getWires() {
        return _wires;
    }

    public int getCounter() {
        return _counter;
    }
}
