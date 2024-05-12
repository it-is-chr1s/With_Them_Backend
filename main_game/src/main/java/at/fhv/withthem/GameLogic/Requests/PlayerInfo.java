package at.fhv.withthem.GameLogic.Requests;

public class PlayerInfo {
    private String name;
    private String color;

    public PlayerInfo(String id, String color) {
        this.name = id;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }
}
