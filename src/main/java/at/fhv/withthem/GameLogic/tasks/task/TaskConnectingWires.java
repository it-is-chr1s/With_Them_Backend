package at.fhv.withthem.GameLogic.tasks.task;

import java.util.Random;

public class TaskConnectingWires {
    private String _player;
    private final int _amountOfWires = 4;
    private final int[][] _wires;
    private int counter = 0;

    public TaskConnectingWires() {
        _wires = new int[_amountOfWires][2];
        createWires();
        shuffleWires();
    }
    public void setPlayer(String player) {
        if(_player == null)
            _player = player;
    }

    public boolean playerAction(ConnectingWiresMessage msg){
        if(_wires[msg.getWire1()][1] == msg.getWire2()){
            counter++;
        }else{
            counter = 0;
        }

        return counter == _amountOfWires;
    }

    private void createWires(){
        for(int i = 0; i < _amountOfWires; i++) {
            _wires[i][0] = i;
            _wires[i][1] = i;
        }
    }

    private void shuffleWires(){
        Random random = new Random();
        for(int i = 1; i < _amountOfWires; i++){
            int randIndex = random.nextInt(i + 1);
            int temp = _wires[i][1];
            _wires[i][1] = _wires[randIndex][1];
            _wires[randIndex][1] = temp;
        }
    }
}
