package at.fhv.withthem.GameLogic.Requests;

import at.fhv.withthem.GameLogic.Colors;
import at.fhv.withthem.GameLogic.Direction;
import com.fasterxml.jackson.annotation.JsonCreator;

public class MapRequest {

    private String _gameId;

    @JsonCreator
    public MapRequest(String gameId) {
        _gameId=gameId;
    }

    public String getGameId() {
        return _gameId;
    }

    public void setGameId(String gameId) {
        _gameId = gameId;
    }
}
