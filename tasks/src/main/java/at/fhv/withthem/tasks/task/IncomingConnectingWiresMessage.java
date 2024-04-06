package at.fhv.withthem.tasks.task;

import at.fhv.withthem.tasks.TaskMessage;
import com.fasterxml.jackson.annotation.JsonCreator;

public class IncomingConnectingWiresMessage extends TaskMessage {
    private short _plug1;
    private short _plug2;

    @JsonCreator
    public IncomingConnectingWiresMessage(String lobby, String task, String player, short plug1, short plug2) {
        super(lobby, task, player);
        _plug1 = plug1;
        _plug2 = plug2;
    }

    public void setPlug1(short plug1) {
        _plug1 = plug1;
    }

    public void setPlug2(short plug2) {
        _plug2 = plug2;
    }

    public short getPlug1() {
        return _plug1;
    }

    public short getPlug2() {
        return _plug2;
    }
}
