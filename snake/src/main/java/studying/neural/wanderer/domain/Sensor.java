package studying.neural.wanderer.domain;

import studying.neural.wanderer.tools.DirectionClockwise;

public class Sensor {

    private final DirectionClockwise directionClockwise;

    // Sensor data needs to be relative to snake's head
    private final Coordinates position;
    private final Direction direction;

    private final Matrix matrix;
    private final Snake snake;

    public Sensor(String id, Coordinates relativeToHeadOnNorthPosition, Direction relativeToHeadOnNorthDirection, Matrix matrix, Snake snake) {
        this.matrix = matrix;
        this.snake = snake;
        this.directionClockwise = DirectionClockwise.getInstance();
        this.position = relativeToHeadOnNorthPosition;
        this.direction = relativeToHeadOnNorthDirection;
    }

    public int calculateDistance() {
        var head = snake.getHead();

        // get position on matrix
        var current = new Coordinates(
                head.getX() + position.getX(),
                head.getY() + position.getY()
        );

        var distance = 0;
        var directionOnMatrix = directionClockwise.convertRelative(snake.getCurrentDirection(), direction);
        while (matrix.getPoint(current.getX(), current.getY()) == LocationInfo.BACKGROUND) {
            current.set(
                    current.getX() + directionOnMatrix.getMovementX(),
                    current.getY() + directionOnMatrix.getMovementY()
            );
            distance++;
        }

        return distance;
    }
}
