package at.fhv.withthem.sabotages.sabotage;

import at.fhv.withthem.sabotages.SabotageMessage;

public abstract class Sabotage {
    private String _player;
    private String _type;
    private int _id;

    public Sabotage(String type, int id) {
        _type = type;
        _id = id;
    }

    public String getType() {
        return _type;
    }

    public void setPlayer(String player) {
        _player = player;
    }

    public void setId(int id) {
        _id = id;
    }

    public int getId(){
        return _id;
    }

    public String getPlayer(){
        return _player;
    }

    public abstract void playerAction(SabotageMessage msg, Reaction reaction);

    public abstract void reset();

    public abstract boolean taskCompleted();

    public abstract SabotageMessage getCurrentState();

    @Override
    public boolean equals(Object obj){
        if(obj instanceof Sabotage task){
            return task.getType().equals(_type);
        }else if(obj instanceof String type){
            return type.equals(_type);
        }

        return false;
    }
}
