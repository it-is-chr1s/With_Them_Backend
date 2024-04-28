package at.fhv.withthem.MeetingLogic.task;

import java.util.List;

public class EmergencyMeeting {

    private String _gameId;
    private boolean _startable;
    private boolean _isRunning;
    private int _coolDownTime;
    private List<String> _alivePlayers;
    private int _Votes;

    public EmergencyMeeting(String gameId, List<String>playerNames){
        _startable=true;
        _isRunning=false;
        _gameId=gameId;
        _alivePlayers =playerNames;
    }

    public String getGameId() {
        return _gameId;
    }

    public void setGameId(String _gameId) {
        this._gameId = _gameId;
    }

    public boolean isStartable() {
        return _startable;
    }

    public void setStartable(boolean _startable) {
        this._startable = _startable;
    }

    public boolean getIsRunning() {return _isRunning; }

    public void setIsRunning(boolean _isRunning) {
        this._isRunning = _isRunning;
    }

    public List<String> getAlivePlayers() {
        return _alivePlayers;
    }

    public void setAlivePlayers(List<String> _allAlivePlayers) {
        this._alivePlayers = _allAlivePlayers;
    }
}
