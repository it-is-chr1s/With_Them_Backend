package at.fhv.withthem.MeetingLogic;
import com.fasterxml.jackson.annotation.JsonCreator;

public class VoteRequest {
    private String _gameId;
    private String _voter;
    private String _nominated;

    @JsonCreator
    public VoteRequest(String gameId, String voter, String nominated) {
        _gameId=gameId;
        _voter=voter;
        _nominated=nominated;
    }

    public String getGameId() {
        return _gameId;
    }

    public void setGameId(String _gameId) {
        this._gameId = _gameId;
    }

    public String getVoter() {
        return _voter;
    }

    public void setVoter(String voter) {
        this._voter = voter;
    }

    public String getNominated() {
        return _nominated;
    }

    public void setNominated(String nominated) {
        this._nominated = nominated;
    }
}

