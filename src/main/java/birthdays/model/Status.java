package birthdays.model;

import javafx.scene.paint.Color;

public enum Status {

    READY(Color.GREEN, "Ready "),
    ERROR(Color.RED, "Error "),
    WARNING(Color.GRAY, "Warning "),
    INFO(Color.BLUE, "Info "),
    CANCELED(Color.BLACK, "Canceled ");

    private Color color;
    private String message;

    Status(Color color, String message) {
        this.color = color;
        this.message = message;
    }

    public Color getColor() {
        return color;
    }

    public String getMessage() {
        return message;
    }
}
