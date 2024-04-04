package at.fhv.withthem.GameLogic;

public class TaskPosition extends Position{
    private String _taskType;

    public TaskPosition(float x, float y, String taskType) {
        super(x, y);
        _taskType = taskType;
    }

    public String getTaskType() {
        return _taskType;
    }

    public void setTaskType(String taskType) {
        _taskType = taskType;
    }
}
