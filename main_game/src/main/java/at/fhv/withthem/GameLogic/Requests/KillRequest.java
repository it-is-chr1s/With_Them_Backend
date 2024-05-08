package at.fhv.withthem.GameLogic.Requests;

import com.fasterxml.jackson.annotation.JsonCreator;

public class KillRequest {

    private String _gameId;
    private String _killerId;

    public KillRequest() {
    }

    @JsonCreator
    public KillRequest(String gameId, String killerId) {
        _gameId = gameId;
        _killerId = killerId;
    }

    public String getGameId() {
        return _gameId;
    }

    public void setGameId(String gameId) {
        _gameId = gameId;
    }

    public String getKillerId() {
        return _killerId;
    }

    public void setKillerId(String killerId) {
        _killerId = killerId;
    }
}
