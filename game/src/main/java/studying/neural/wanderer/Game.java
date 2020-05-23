package studying.neural.wanderer;

import studying.neural.wanderer.components.Coordinates;
import studying.neural.wanderer.components.Direction;
import studying.neural.wanderer.components.LocationInfo;
import studying.neural.wanderer.components.Matrix;
import studying.neural.wanderer.components.Sensor;
import studying.neural.wanderer.components.Snake;
import studying.neural.wanderer.tools.DefaultSensorListCreator;

public class Game implements Runnable {

    private final DefaultSensorListCreator defaultSensorListCreator
            = DefaultSensorListCreator.getInstance();

    private final int maxLifeTime;
    private final int width;
    private final int height;
    private final OnGameEnded onGameEnded;

    private final Matrix matrix;
    private final Snake snake;
    private Coordinates feed;

    private int points = 0;
    private int lifeTime;

    public Game(int width, int height, int maxLifeTime, OnGameEnded onGameEnded) {
        this.maxLifeTime = maxLifeTime;
        this.width = width;
        this.height = height;
        this.matrix = new Matrix(width, height);
        this.onGameEnded = onGameEnded;

        var spawnPoint = new Coordinates(width / 10, height / 2);
        this.snake = new Snake(spawnPoint, Direction.EAST);
        this.lifeTime = maxLifeTime;

        this.snake.getSensorList().addAll(
                defaultSensorListCreator.createDefault(matrix, snake)
        );

        spawnFeed();
    }

    @Override
    public void run() {
        while(isAlive()) {
            lifeTime--;

            // TODO add input (turnLeft and turnRight)

            eraseTail();
            snake.move();
            printHead();

            if(isEating()) {
                points++;
                snake.eat();
                spawnFeed();
                lifeTime = maxLifeTime;
            }
        }

        // TODO send neural network settings
        onGameEnded.onGameEnded(points);
    }

    private void spawnFeed() {
        var freeArea = width * height - snake.size();
        var randomSerialFeed = Math.random() * freeArea;
        int x = 0, y = 0;

        for(x = 0; x < width && randomSerialFeed > 0; x++) {
            for (y = 0; y < height && randomSerialFeed > 0; y++) {
                if (matrix.getPoint(x, y) == LocationInfo.BACKGROUND)
                    randomSerialFeed--;
            }
        }

        feed = new Coordinates(x, y);
        matrix.setPoint(x, y, LocationInfo.FEED);
    }

    private boolean isAlive() {
        if(lifeTime < 0)
            return false;

        var head = snake.getHead();
        var pointValue = matrix.getPoint(head.getX(), head.getY());

        return pointValue == LocationInfo.BACKGROUND ||
                pointValue == LocationInfo.FEED;
    }

    private boolean isEating() {
        var head = snake.getHead();
        return feed.getX() == head.getX() &&
                feed.getY() == head.getY();
    }

    private void eraseTail() {
        var tail = snake.getTail();
        matrix.setPoint(tail.getX(), tail.getY(), LocationInfo.BACKGROUND);
    }

    private void printHead() {
        var tail = snake.getHead();
        matrix.setPoint(tail.getX(), tail.getY(), LocationInfo.SNAKE);
    }

    public LocationInfo[][] getMatrix() {
        return matrix.getMatrix();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getPoints() {
        return points;
    }
}
