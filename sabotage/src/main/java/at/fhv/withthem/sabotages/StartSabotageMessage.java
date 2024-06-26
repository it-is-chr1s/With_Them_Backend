package at.fhv.withthem.sabotages;

import com.fasterxml.jackson.annotation.JsonCreator;

public class StartSabotageMessage {
    private String _gameId;
    private int _sabotageId;

    @JsonCreator
    public StartSabotageMessage(String gameId, int sabotageId){
        _gameId = gameId;
        _sabotageId = sabotageId;
    }

    public String getGameId() {
        return _gameId;
    }

    public int getSabotageId() {
        return _sabotageId;
    }

    public void setGameId(String gameId) {
        _gameId = gameId;
    }

    public void setSabotageId(int sabotageId) {
        _sabotageId = sabotageId;
    }
}
