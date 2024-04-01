package at.fhv.withthem.GameLogic.tasks.task;

public abstract class Task {
    private String _player;

    public void setPlayer(String player) {
        if(_player == null)
            _player = player;
    }

    public abstract int playerAction(TaskMessage msg);
}
