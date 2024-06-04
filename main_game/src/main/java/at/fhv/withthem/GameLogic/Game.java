package at.fhv.withthem.GameLogic;

import at.fhv.withthem.GameLogic.Maps.GameMap;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Game {
    private String _gameId;
    private Player _host;
    private GameMap _map;
    private ConcurrentHashMap<String, Player> _players = new ConcurrentHashMap<>();

    private Settings _settings;

    private boolean _isRunning;

    private boolean _isWon;

    public Game(String gameId, GameMap map, String host){
        _gameId=gameId;
        this._map =map;
        _host=new Player(host,new Position(6f,6f),Colors.BLUE);
        _players.put(host, _host);
        _settings=new Settings();
        _isRunning=false;
    }

    public boolean isWon() {
        return _isWon;
    }

    public void setWon(boolean won) {
        _isWon = won;
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

    public void setGameMap(GameMap map){
        _map=map;
    }

    public String getHost() {
        return _host.getName();
    }

    public List<Colors> getOccupiedColors(){
        List<Colors> usedColors = new LinkedList<>();
        for (Player player : _players.values()) {
            usedColors.add(player.getColor());
        }
        return usedColors;
    }
    public Colors getAvailableColor() {
        List<Colors>usedColors= getOccupiedColors();
        for (Colors color : Colors.values()) {
            if (!usedColors.contains(color)) {
                return color;
            }
        }
        return null; // No available color
    }
    public boolean icColorAvailable(Colors c){
        for (Player player : _players.values()) {
           if( player.getColor()== c)
               return false;
        }
        return  true;
    }
}