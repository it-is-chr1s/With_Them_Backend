package at.fhv.withthem.GameLogic.Maps;

import org.springframework.stereotype.Component;

@Component
public class LobbyMap extends GameMap{

        public LobbyMap() {
                super(11, 11);

                //first row & column are not working correctly, so we need this
                setWalls(0, 0, 11, 1);
                setWalls(0, 1, 1, 10);
        }
}
