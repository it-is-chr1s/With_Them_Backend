package at.fhv.withthem.GameLogic.tasks.task;

public class ConnectingWiresMessage {
    private int _wire1;
    private int _wire2;

    public ConnectingWiresMessage(int wire1, int wire2) {
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
