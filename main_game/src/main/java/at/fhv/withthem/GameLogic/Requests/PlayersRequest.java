package at.fhv.withthem.GameLogic.Requests;

import java.util.List;

public class PlayersRequest {
    private List<PlayerInfo> alive;
    private List<String> dead;

    public PlayersRequest(List<PlayerInfo> alivePlayers, List<String> deadPlayers) {
        this.alive = alivePlayers;
        this.dead = deadPlayers;
    }

    public List<PlayerInfo> getAlive() {
        return alive;
    }

    public List<String> getDead() {
        return dead;
    }
}
