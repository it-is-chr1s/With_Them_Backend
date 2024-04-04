package at.fhv.withthem.tasks.task;

import at.fhv.withthem.tasks.TaskMessage;

public class TaskFileDownloadUpload extends Task{
    private final float speed = 1.0f;
    private final int downloadPosition_x = 5;
    private final int downloadPosition_y = 5;

    private final int uploadPosition_x = 10;
    private final int uploadPosition_y = 10;

    private int counter = 0;

    public TaskFileDownloadUpload() {
        super("TaskFileDownloadUpload");
    }

    @Override
    public void playerAction(TaskMessage msg, TaskCompletedListener listener){
        IncomingFileDownloadUploadMessage msg_fdu = (IncomingFileDownloadUploadMessage) msg;
        if(msg_fdu.getMake().equals("download")){ //player is at downloadPosition
            if(counter == 0){
                counter++;
            }
        }else if(msg_fdu.getMake().equals("upload")){
            if(counter == 1){
                counter++;
                listener.taskCompleted();
            }
        }
    }

    @Override
    public TaskMessage getCurrentState(){
        return new OutgoingFileDownloadUploadMessage((counter == 0) ? "readyForDownload" : (counter == 1) ? "readyForUpload" : "completed");
    }
}
