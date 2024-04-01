package at.fhv.withthem;

import at.fhv.withthem.GameLogic.tasks.TaskMessage;
import at.fhv.withthem.GameLogic.tasks.TasksHandler;
import at.fhv.withthem.GameLogic.tasks.task.IncomingConnectingWiresMessage;
import at.fhv.withthem.GameLogic.tasks.task.OutgoingConnectingWiresMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes = TasksApplication.class)
public class TasksApplicationTests {

    @Autowired
    private TasksHandler tasksHandler;

    @Test
    void LoadLobby() {
        List<String> taskNames = new ArrayList<>();
        taskNames.add("TaskConnectingWires");
        taskNames.add("TaskFileDownloadUpload");

        tasksHandler.loadLobby("TestLobby");
        List<String> availableTasks = tasksHandler.getAvailableTasks("TestLobby");

        Assertions.assertTrue(availableTasks.containsAll(taskNames));
    }

    @Test
    void StartTask() {
        tasksHandler.loadLobby("TestLobby");

        List<String> availableTasks = tasksHandler.getAvailableTasks("TestLobby");
        int sizeBefore = availableTasks.size();

        tasksHandler.startTask("TestLobby", "TaskConnectingWires", "TestPlayer");

        availableTasks = tasksHandler.getAvailableTasks("TestLobby");
        int sizeAfter = availableTasks.size();

        Assertions.assertFalse(availableTasks.contains("TaskConnectingWires"));
        Assertions.assertEquals(sizeBefore - 1, sizeAfter);
    }

    @Test
    void PlayerAction() throws JsonProcessingException {
        tasksHandler.loadLobby("TestLobby");
        tasksHandler.startTask("TestLobby", "TaskConnectingWires", "TestPlayer");

        TaskMessage taskMessage = new IncomingConnectingWiresMessage("TestLobby", "TaskConnectingWires", "TestPlayer", 1, 1);

        int result = tasksHandler.playerAction(taskMessage);

        ObjectMapper mapper = new ObjectMapper();

        System.out.println(mapper.writeValueAsString((OutgoingConnectingWiresMessage) tasksHandler.getCurrentState("TestLobby", "TestPlayer")));
        //Assertions.assertEquals(1, result);
    }
}
