package at.fhv.withthem.tasks.task.connecting_wires;

import at.fhv.withthem.tasks.TaskMessage;
import at.fhv.withthem.tasks.task.Reaction;
import at.fhv.withthem.tasks.task.Task;
import at.fhv.withthem.tasks.task.connecting_wires.IncomingConnectingWiresMessage;
import at.fhv.withthem.tasks.task.connecting_wires.OutgoingConnectingWiresMessage;

import java.util.Random;

public class TaskConnectingWires extends Task {
    private final int _amountOfWires = 4;
    private final short[][] _plugs;
    private final boolean[] _connectedPlugs;

    public TaskConnectingWires(int id) {
        super("Connecting Wires", id);
        _plugs = new short[_amountOfWires][2];
        _connectedPlugs = new boolean[_amountOfWires];
        resetConnectedPlugs();
        createPlugs();
        shufflePlugs();
    }

    @Override
    public void playerAction(TaskMessage msg, Reaction reaction){
        IncomingConnectingWiresMessage msg_cw = (IncomingConnectingWiresMessage) msg;
        System.out.print("Plug1: " + msg_cw.getPlug1() + " Plug2: " + msg_cw.getPlug2() + "\n");
        if(msg_cw.getPlug2() == msg_cw.getPlug1()){
            short pos = 0;
            while(_plugs[pos][1] != msg_cw.getPlug1()){
                pos++;
            }
            _connectedPlugs[pos] = true;
            System.out.println("Correct");
        }else{
            resetConnectedPlugs();
            System.out.println("Wrong");
        }

        reaction.react();
    }

    @Override
    public void reset(){
        setPlayer(null);
        resetConnectedPlugs();
        shufflePlugs();
    }

    @Override
    public TaskMessage getCurrentState(){
        return new OutgoingConnectingWiresMessage(_plugs, _connectedPlugs);
    }

    @Override
    public boolean taskCompleted(){
        for(short i = 0; i < _amountOfWires; i++) {
            if(!_connectedPlugs[i]){
                return false;
            }
        }
        return true;
    }

    private void resetConnectedPlugs(){
        for(short i = 0; i < _amountOfWires; i++) {
            _connectedPlugs[i] = false;
        }
    }

    private void createPlugs(){
        for(short i = 0; i < _amountOfWires; i++) {
            _plugs[i][0] = i;
            _plugs[i][1] = i;
        }
    }

    private void shufflePlugs(){
        Random random = new Random();
        for(int i = 1; i < _amountOfWires; i++){
            int randIndex = random.nextInt(i + 1);
            short temp = _plugs[i][1];
            _plugs[i][1] = _plugs[randIndex][1];
            _plugs[randIndex][1] = temp;
        }
    }
}
