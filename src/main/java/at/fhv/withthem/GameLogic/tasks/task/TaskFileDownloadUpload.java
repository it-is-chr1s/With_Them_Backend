package at.fhv.withthem.GameLogic.tasks.task;

public class TaskFileDownloadUpload extends Task{
    private final float speed = 1.0f;
    private final int downloadPosition_x = 5;
    private final int downloadPosition_y = 5;

    private final int uploadPosition_x = 10;
    private final int uploadPosition_y = 10;

    private int counter = 0;

    @Override
    public int playerAction(TaskMessage msg){
        //FileDownloadUpload msg_fdu = (FileDownloadUpload) msg;
        if(true){ //player is at downloadPosition
            if(counter == 0){
                counter++;
            }
            //download file
        }else{
            if(counter == 1){
                counter++;
            }
            //upload file
        }

        return counter;
    }
}
