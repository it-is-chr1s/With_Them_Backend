package at.fhv.withthem.GameLogic;

import java.util.concurrent.ConcurrentHashMap;

public class Game {
    private String _gameId;
    private Player _host;
    private GameMap _map;
    private ConcurrentHashMap<String, Player> _players = new ConcurrentHashMap<>();

    private Settings _settings;

    private boolean _isRunning;

    public Game(String gameId, GameMap map, String host){
        _gameId=gameId;
        this._map =map;
        _host=new Player(host,new Position(1,1),Colors.BLUE);
        _players.put(host, _host);
        _settings=new Settings();
        _isRunning=false;
    }

    public boolean isRunning() {
        return _isRunning;
    }

    public void setRunning(boolean running) {
        _isRunning = running;
    }

    public Settings getSettings() {
        return _settings;
    }

    public void setSettings(Settings settings) {
        _settings = settings;
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

    public String getHost() {
        return _host.getName();
    }
}