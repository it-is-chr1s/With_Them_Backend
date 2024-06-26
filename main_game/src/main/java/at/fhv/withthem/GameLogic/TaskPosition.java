package at.fhv.withthem.GameLogic;

public class TaskPosition extends Position{
    private String _taskType;
    private int _id;
    private boolean _isSabotage;

    public TaskPosition(float x, float y, String taskTypeWithIdAndSabotage) {
        super(x, y);

        String[] taskTypeAndIdWithSabotage = taskTypeWithIdAndSabotage.split("%id=");
        String[] idAndSabotage = taskTypeAndIdWithSabotage[1].split("%sabotage=");

        _taskType = taskTypeAndIdWithSabotage[0];
        _id = Integer.parseInt(idAndSabotage[0]);
        _isSabotage = Boolean.parseBoolean(idAndSabotage[1]);
    }

    public String getTaskType() {
        return _taskType;
    }

    public void setTaskType(String taskType) {
        _taskType = taskType;
    }

    public int getId() {
        return _id;
    }

    public boolean getIsSabotage() {
        return _isSabotage;
    }
}
