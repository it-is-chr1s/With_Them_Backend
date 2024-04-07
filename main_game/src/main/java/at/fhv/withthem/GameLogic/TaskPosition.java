package at.fhv.withthem.GameLogic;

public class TaskPosition extends Position{
    private String _taskType;
    private int _id;

    public TaskPosition(float x, float y, String taskTypeWithId) {
        super(x, y);
        String[] taskTypeAndId = taskTypeWithId.split("%id");
        _taskType = taskTypeAndId[0];
        _id = Integer.parseInt(taskTypeAndId[1]);
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
}
