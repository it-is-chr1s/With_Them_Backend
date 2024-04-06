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
        super("FileDownloadUpload");
    }

    @Override
    public void playerAction(TaskMessage msg){
        IncomingFileDownloadUploadMessage msg_fdu = (IncomingFileDownloadUploadMessage) msg;
        if(msg_fdu.getMake().equals("download")){ //player is at downloadPosition
            if(counter == 0){
                counter++;
            }
        }else if(msg_fdu.getMake().equals("upload")){
            if(counter == 1){
                counter++;
            }
        }
    }

    @Override
    public void reset(){
        counter = 0;
    }

    @Override
    public boolean taskCompleted(){
        return counter == 2;
    }

    @Override
    public TaskMessage getCurrentState(){
        return new OutgoingFileDownloadUploadMessage((counter == 0) ? "readyForDownload" : (counter == 1) ? "readyForUpload" : "completed");
    }

    @Override
    public boolean equals(Object obj){
        if(obj instanceof Task task){
            return task.getType().equals(this.getType());
        }else if(obj instanceof String type){
            return type.equals("File Download") || type.equals("File Upload");
        }

        return false;
    }
}
