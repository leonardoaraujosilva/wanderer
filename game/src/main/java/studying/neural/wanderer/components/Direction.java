package studying.neural.wanderer.components;

import lombok.Getter;

@Getter
public enum Direction {
    NORTH(0, -1),
    SOUTH(0, 1),
    EAST(1, 0),
    WEST(-1, 0);

    private final int movementX;
    private final int movementY;

    private Direction(int movementX, int movementY) {
        this.movementX = movementX;
        this.movementY = movementY;
    }


}
