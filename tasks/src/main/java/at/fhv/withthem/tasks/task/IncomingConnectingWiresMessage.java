package at.fhv.withthem.tasks.task;

import at.fhv.withthem.tasks.TaskMessage;
import com.fasterxml.jackson.annotation.JsonCreator;

public class IncomingConnectingWiresMessage extends TaskMessage {
    private int _wire1;
    private int _wire2;

    @JsonCreator
    public IncomingConnectingWiresMessage(String lobby, String task, String player, int wire1, int wire2) {
        super(lobby, task, player);
        _wire1 = wire1;
        _wire2 = wire2;
    }

    public int getWire1() {
        return _wire1;
    }

    public int getWire2() {
        return _wire2;
    }
}
