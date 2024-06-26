package at.fhv.withthem;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = TasksApplication.class)
public class TaskConnectingWiresApplicationTests {
    /*

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
    void StartTaskTaskConnectingWires() {
        tasksHandler.loadLobby("TestLobby");

        List<String> availableTasks = tasksHandler.getAvailableTasks("TestLobby");
        int sizeBefore = availableTasks.size();
        System.out.println("Available tasks before starting TaskFileDownloadUpload: " + availableTasks);

        tasksHandler.startTask("TestLobby", "TaskConnectingWires", "TestPlayer");

        availableTasks = tasksHandler.getAvailableTasks("TestLobby");
        int sizeAfter = availableTasks.size();

        System.out.println("Available tasks after starting TaskFileDownloadUpload: " + availableTasks);
        Assertions.assertFalse(availableTasks.contains("TaskConnectingWires"));
        Assertions.assertEquals(sizeBefore - 1, sizeAfter);
    }

    @Test
    //compares counter change before and after action
    void PlayerActionConnectingWires() throws JsonProcessingException {
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
    void TaskCompletedConnectingWires(){
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
    void cancelTaskConnectingWires(){
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
     */
}
