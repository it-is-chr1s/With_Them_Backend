package at.fhv.withthem.tasks.task;

import at.fhv.withthem.tasks.TaskMessage;

public abstract class Task {
    private String _player;
    private String _type;
    private int _id;

    public Task(String type) {
        _type = type;
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

    public abstract void playerAction(TaskMessage msg, Reaction reaction);

    public abstract void reset();

    public abstract boolean taskCompleted();

    public abstract TaskMessage getCurrentState();

    @Override
    public boolean equals(Object obj){
        if(obj instanceof Task task){
            return task.getType().equals(_type);
        }else if(obj instanceof String type){
            return type.equals(_type);
        }

        return false;
    }
}
