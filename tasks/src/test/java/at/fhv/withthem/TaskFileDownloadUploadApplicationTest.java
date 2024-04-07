package at.fhv.withthem;

import at.fhv.withthem.TasksApplication;
import at.fhv.withthem.tasks.TaskMessage;
import at.fhv.withthem.tasks.TasksHandler;
import at.fhv.withthem.tasks.task.IncomingFileDownloadUploadMessage;
import at.fhv.withthem.tasks.task.OutgoingFileDownloadUploadMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest(classes = TasksApplication.class)
public class TaskFileDownloadUploadApplicationTest {

    @Autowired
    private TasksHandler tasksHandler;

    ObjectMapper mapper = new ObjectMapper();

    @Test
    //compares available tasks before and after starting a task
    void StartTaskFileDownloadUpload() {
        tasksHandler.loadLobby("TestLobby");

        List<String> availableTasks = tasksHandler.getAvailableTasks("TestLobby");
        int sizeBefore = availableTasks.size();
        System.out.println("Available tasks before starting TaskFileDownloadUpload: " + availableTasks);

        tasksHandler.startTask("TestLobby", "TaskFileDownloadUpload", "TestPlayer");

        availableTasks = tasksHandler.getAvailableTasks("TestLobby");
        int sizeAfter = availableTasks.size();

        System.out.println("Available tasks after starting TaskFileDownloadUpload: " + availableTasks);
        Assertions.assertFalse(availableTasks.contains("TaskFileDownloadUpload"));
        Assertions.assertEquals(sizeBefore - 1, sizeAfter);
    }

    @Test
        //compares counter change before and after action
    void PlayerActionFileDownloadUpload() throws JsonProcessingException {
        tasksHandler.loadLobby("TestLobby");
        tasksHandler.startTask("TestLobby", "TaskFileDownloadUpload", "TestPlayer");

        //before action
        OutgoingFileDownloadUploadMessage msg = (OutgoingFileDownloadUploadMessage) tasksHandler.getCurrentState("TestLobby", "TestPlayer");
        Assertions.assertEquals("readyForDownload", msg.getStatus());
        System.out.println("before download: " + mapper.writeValueAsString(msg));

        //action
        TaskMessage taskMessage = new IncomingFileDownloadUploadMessage("TestLobby", "TaskFileDownloadUpload", "TestPlayer", "download");
        tasksHandler.playerAction(taskMessage);

        //after action
        msg = (OutgoingFileDownloadUploadMessage) tasksHandler.getCurrentState("TestLobby", "TestPlayer");
        Assertions.assertEquals("readyForUpload", msg.getStatus());
        System.out.println("before upload: " + mapper.writeValueAsString(msg));
    }

    @Test
    void TaskCompletedFileDownloadUpload() throws JsonProcessingException {
        Assertions.assertEquals(tasksHandler.getFinishedTasks("TestLobby"), 0);

        //action
        TaskMessage taskMessage = new IncomingFileDownloadUploadMessage("TestLobby", "TaskFileDownloadUpload", "TestPlayer", "upload");
        tasksHandler.playerAction(taskMessage);

        Assertions.assertEquals(tasksHandler.getFinishedTasks("TestLobby"), 1);
    }
}
