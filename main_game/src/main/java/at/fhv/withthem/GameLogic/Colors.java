package at.fhv.withthem.GameLogic;

public enum Colors {
    RED("#ff0000"),
    BROWN("#994C00"),
    ORANGE("#ff8000"),
    YELLOW("#ffff00"),
    LIME("#80ff00"),
    GREEN("#1FA61A"),
    CYAN("#00ffff"),
    BLUE("#0080ff"),
    NAVY("#0000ff"),
    PURPLE("#8000ff"),
    MAGENTA("#ff0080"),
    PINK("#ff8080"),
    GRAY("#808080");

    private String hexValue;

    Colors(String hexValue) {
        this.hexValue = hexValue;
    }

    public String getHexValue() {
        return hexValue;
    }

    // Method to get Color enum from hex string
    public static Colors fromHex(String hex) {
        for (Colors color : Colors.values()) {
            if (color.getHexValue().equalsIgnoreCase(hex)) {
                return color;
            }
        }
        throw new IllegalArgumentException("Invalid hex value: " + hex);
    }
}
