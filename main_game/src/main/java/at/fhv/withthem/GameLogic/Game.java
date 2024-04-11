package at.fhv.withthem.GameLogic;

import java.util.concurrent.ConcurrentHashMap;

public class Game {
    private String _gameId;
    private GameMap _map;
    private ConcurrentHashMap<String, Player> _players = new ConcurrentHashMap<>();

    public Game(String gameId, GameMap map){
        _gameId=gameId;
        this._map =map;
    }
    public Game(){

    }
    public ConcurrentHashMap<String, Player> getPlayers() {
        return _players;
    }

    public void setPlayers(ConcurrentHashMap<String, Player> players) {
        this._players = players;
    }
    public GameMap getMap() {
        return _map;
    }

}