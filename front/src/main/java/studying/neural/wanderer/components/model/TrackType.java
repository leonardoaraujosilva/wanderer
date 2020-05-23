package studying.neural.wanderer.components.model;

import java.awt.*;

public enum TrackType {

    BACKGROUND(null),
    TRACK(Color.WHITE),
    START(Color.GREEN),
    END(Color.RED);

    private final Color color;

    TrackType(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}
