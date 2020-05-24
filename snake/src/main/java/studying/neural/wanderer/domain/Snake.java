package studying.neural.wanderer.domain;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Snake {

    private final List<Coordinates> snake = new ArrayList<>();
    private final Queue<Coordinates> stomach = new LinkedList<>();

    private final List<Sensor> sensorList = new ArrayList<>();
    private Direction currentDirection;

    public Snake(Coordinates coordinates, Direction startDirection) {
        currentDirection = startDirection;
        snake.add(coordinates);
    }

    public void eat() {
        var head = getHead();
        stomach.add(new Coordinates(head.getX(), head.getY()));
    }

    public void move() {
        var tail = getTail();
        var feedOnStomach = stomach.peek();
        if(feedOnStomach != null &&
                tail.getX() == feedOnStomach.getX() &&
                tail.getY() == feedOnStomach.getY())
            snake.add(stomach.poll());

        for(var index = snake.size() - 1; index > 0; index--)
            snake.get(index).set(snake.get(index - 1));

        var head = getHead();
        head.set(
                head.getX() + currentDirection.getMovementX(),
                head.getY() + currentDirection.getMovementY()
        );
    }

    public Coordinates getNextMove() {
        var head = getHead();
        return new Coordinates(head.getX() + currentDirection.getMovementX(),
                head.getY() + currentDirection.getMovementY()
        );
    }

    public Coordinates getHead() {
        return snake.get(0);
    }

    public Coordinates getTail() {
        return snake.get(snake.size() - 1);
    }

    public Direction getCurrentDirection() {
        return currentDirection;
    }

    public List<Sensor> getSensorList() {
        return sensorList;
    }

    public int size() {
        return snake.size();
    }

    public void turnLeft() {
        switch (currentDirection) {
            case NORTH: currentDirection = Direction.WEST; break;
            case WEST: currentDirection = Direction.SOUTH; break;
            case SOUTH: currentDirection = Direction.EAST; break;
            case EAST: currentDirection = Direction.NORTH; break;
        }
    }

    public void turnRight() {
        switch (currentDirection) {
            case NORTH: currentDirection = Direction.EAST; break;
            case EAST: currentDirection = Direction.SOUTH; break;
            case SOUTH: currentDirection = Direction.WEST; break;
            case WEST: currentDirection = Direction.NORTH; break;
        }
    }

    public List<Coordinates> getBody() {
        return snake;
    }


}
