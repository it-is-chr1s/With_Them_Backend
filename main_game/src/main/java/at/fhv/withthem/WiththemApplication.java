package at.fhv.withthem;

import at.fhv.withthem.GameLogic.GameController;
import at.fhv.withthem.GameLogic.GameMap;
import at.fhv.withthem.GameLogic.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class WiththemApplication {
	@Autowired
	private final GameMap _map;
	@Autowired
	private final GameController _gameController;
	@Autowired
	private final GameService _gameService;

	public WiththemApplication(GameMap map, GameController gameController, GameService gameService){
        _map = map;
        _gameController = gameController;
        _gameService = gameService;
    }

	public static void main(String[] args) {
		SpringApplication.run(WiththemApplication.class, args);
	}
/*
	@Override
	public void run(String... args) throws Exception {
		_map.initializeMapLayout();
		//_gameController.loadTasks("123", _gameService.getTaskPositions());
	}*/
}
