package at.fhv.withthem.tasks.task;

import at.fhv.withthem.tasks.TaskMessage;

import java.util.Random;

public class TaskConnectingWires extends Task{
    private final int _amountOfWires = 4;
    private final short[][] _wires;
    private int _counter = 0;

    public TaskConnectingWires() {
        super("TaskConnectingWires");
        _wires = new short[_amountOfWires][2];
        createWires();
        shuffleWires();
    }

    @Override
    public void playerAction(TaskMessage msg, TaskCompletedListener listener){
        IncomingConnectingWiresMessage msg_cw = (IncomingConnectingWiresMessage) msg;
        if(_wires[msg_cw.getWire1()][1] == msg_cw.getWire2()){
            _counter++;
        }else{
            _counter = 0;
        }

        if(_counter == _amountOfWires){
            listener.taskCompleted();
        }

    }

    @Override
    public TaskMessage getCurrentState(){
        return new OutgoingConnectingWiresMessage(_wires, _counter);
    }

    private void createWires(){
        for(short i = 0; i < _amountOfWires; i++) {
            _wires[i][0] = i;
            _wires[i][1] = i;
        }
    }

    private void shuffleWires(){
        Random random = new Random();
        for(int i = 1; i < _amountOfWires; i++){
            int randIndex = random.nextInt(i + 1);
            short temp = _wires[i][1];
            _wires[i][1] = _wires[randIndex][1];
            _wires[randIndex][1] = temp;
        }
    }
}
