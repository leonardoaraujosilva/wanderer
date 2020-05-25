package studying.neural.wanderer.domain;

import studying.neural.wanderer.Game;
import studying.neural.wanderer.tools.DirectionClockwise;

public class Sensor {

    private final DirectionClockwise directionClockwise;

    // Sensor data needs to be relative to snake's head
    private final Coordinates position;
    private final Direction direction;

    private final Game game;
    private final Snake snake;

    public Sensor(String id, Coordinates relativeToHeadOnNorthPosition, Direction relativeToHeadOnNorthDirection, Game game, Snake snake) {
        this.game = game;
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
        while (game.getPoint(current.getX(), current.getY()) == LocationInfo.BACKGROUND ||
            game.getPoint(current.getX(), current.getY()) == LocationInfo.FEED) {
            current.set(
                    current.getX() + directionOnMatrix.getMovementX(),
                    current.getY() + directionOnMatrix.getMovementY()
            );
            distance++;
        }

        return distance; //game.getPoint(current.getX(), current.getY()) == LocationInfo.INVALID? game.getWidth() : distance;
    }
}
