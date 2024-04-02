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
    //tests if all tasks are loaded correctly
    void LoadLobby() {
        List<String> taskNames = new ArrayList<>();
        taskNames.add("TaskConnectingWires");
        taskNames.add("TaskFileDownloadUpload");

        tasksHandler.loadLobby("TestLobby");
        List<String> availableTasks = tasksHandler.getAvailableTasks("TestLobby");

        Assertions.assertTrue(availableTasks.containsAll(taskNames));
    }

    @Test
    //compares available tasks before and after starting a task
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
    //compares counter change before and after action
    void PlayerAction() throws JsonProcessingException {
        tasksHandler.loadLobby("TestLobby");
        tasksHandler.startTask("TestLobby", "TaskConnectingWires", "TestPlayer");

        //before action
        int counter = ((OutgoingConnectingWiresMessage) tasksHandler.getCurrentState("TestLobby", "TestPlayer")).getCounter();
        Assertions.assertEquals(0, counter);

        //action
        int wire2 = ((OutgoingConnectingWiresMessage) tasksHandler.getCurrentState("TestLobby", "TestPlayer")).getWires()[0][1];
        TaskMessage taskMessage = new IncomingConnectingWiresMessage("TestLobby", "TaskConnectingWires", "TestPlayer", 0, wire2);
        tasksHandler.playerAction(taskMessage);

        //after action
        ObjectMapper mapper = new ObjectMapper();
        OutgoingConnectingWiresMessage msg = (OutgoingConnectingWiresMessage) tasksHandler.getCurrentState("TestLobby", "TestPlayer");
        Assertions.assertEquals(1, msg.getCounter());
        System.out.println(mapper.writeValueAsString(msg));
    }

    @Test
    //tests if task is finished after all wires are connected
    void TaskCompleted(){
        System.out.println("Wire 0 was already connected in playerAction() -> Finished tasks: " + tasksHandler.getFinishedTasks("TestLobby"));

        for(int i = 1; i < 4; i++){
            int wire2 = ((OutgoingConnectingWiresMessage) tasksHandler.getCurrentState("TestLobby", "TestPlayer")).getWires()[i][1];
            TaskMessage taskMessage = new IncomingConnectingWiresMessage("TestLobby", "TaskConnectingWires", "TestPlayer", i, wire2);
            tasksHandler.playerAction(taskMessage);

            System.out.println(i + ". wire connected -> Finished tasks: " + tasksHandler.getFinishedTasks("TestLobby"));
            Assertions.assertEquals(tasksHandler.getFinishedTasks("TestLobby"), i / 3);
        }
    }

    @Test
    void cancelTask(){
        tasksHandler.startTask("TestLobby", "TaskFileDownloadUpload", "TestPlayer");

        List<String> availableTasks = tasksHandler.getAvailableTasks("TestLobby");
        int sizeBefore = availableTasks.size();

        tasksHandler.cancelTask("TestLobby", "TaskFileDownloadUpload", "TestPlayer");

        availableTasks = tasksHandler.getAvailableTasks("TestLobby");
        int sizeAfter = availableTasks.size();

        System.out.println("Before cancel: " + sizeBefore);
        System.out.println("After cancel: " + sizeAfter);
        Assertions.assertEquals(sizeBefore + 1, sizeAfter);
    }
}
