package at.fhv.withthem.MeetingLogic;

import java.util.HashMap;
import java.util.List;

public class EmergencyMeeting {

    private String _gameId;
    private boolean _startable;
    private boolean _isRunning;
    private int _coolDownTime;
    private List<String> _alivePlayers;
    private boolean _votingStarted;

    private HashMap<String,String> _votes;
    public EmergencyMeeting(String gameId, List<String>playerNames){
        _startable=true;
        _isRunning=false;
        _gameId=gameId;
        _alivePlayers =playerNames;
        _votes =new HashMap<>();
    }

    public void addVote(String voter, String nominated){
        //didn't vote already and has right to vote = is alive
        if(_votes.get(voter)==null && _alivePlayers.contains(voter))
            _votes.put(voter,nominated);
    }
    public  void  startVoting(){
        deleteAllVotes();
        _votingStarted=true;
    }
    public boolean everyoneVoted(){
        return _alivePlayers.size() == _votes.size();
    }
    public void endVoting(){
        _votingStarted=false;
    }
    private void deleteAllVotes(){
        _votes.clear();
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
