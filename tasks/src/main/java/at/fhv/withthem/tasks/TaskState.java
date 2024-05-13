package at.fhv.withthem.tasks;

public class TaskState{
    private int id;
    private String state;
    private String task;

    public TaskState(int id, String state, String task){
        this.id = id;
        this.state = state;
        this.task = task;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }
}