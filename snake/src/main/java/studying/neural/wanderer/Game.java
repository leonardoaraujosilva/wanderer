package studying.neural.wanderer;

import lombok.SneakyThrows;
import studying.neural.wanderer.domain.Coordinates;
import studying.neural.wanderer.domain.Direction;
import studying.neural.wanderer.domain.LocationInfo;
import studying.neural.wanderer.domain.Matrix;
import studying.neural.wanderer.domain.Snake;
import studying.neural.wanderer.tools.DefaultSensorListCreator;

import java.util.HashMap;
import java.util.Random;

public class Game implements Runnable {

    private final DefaultSensorListCreator defaultSensorListCreator
            = DefaultSensorListCreator.getInstance();

    private final int maxLifeTime;
    private final int width;
    private final int height;
    private final OnGameEnded onGameEnded;

    private final Matrix matrix;
    private final Snake snake;
    private final NeuralNetwork neuralNetwork;
    private Coordinates feed;

    private int points = 0;
    private int lifeTime;
    private Random random = new Random(146231);

    public Game(int width, int height, int maxLifeTime, NeuralNetwork neuralNetwork, OnGameEnded onGameEnded) {
        this.maxLifeTime = maxLifeTime;
        this.width = width;
        this.height = height;
        this.matrix = new Matrix(width, height);
        this.neuralNetwork = neuralNetwork;
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
    @SneakyThrows
    public void run() {
        var isAlive = isAlive();
        boolean isEating;
        while (isAlive) {
            lifeTime--;

            calculate();

            eraseTail();
            snake.move();
            isAlive = isAlive();
            isEating = isEating();
            printHead();

            if (isEating) {
                points += Math.sqrt(width * width + height * height);
                snake.eat();
                spawnFeed();
                lifeTime = maxLifeTime;
            }
            Thread.sleep(20);
        }

        onGameEnded.onGameEnded(points, neuralNetwork);
    }

    private void spawnFeed() {
        var freeArea = width * height - snake.size();
        var randomSerialFeed = random.nextDouble() * freeArea;//Math.random() * freeArea;
        int x = 0, y = 0;

        for (x = 0; x < width && randomSerialFeed > 0; x++) {
            for (y = 0; y < height && randomSerialFeed > 0; y++) {
                if (matrix.getPoint(x, y) == LocationInfo.BACKGROUND)
                    randomSerialFeed--;
            }
        }

        feed = new Coordinates(x, y);
        matrix.setPoint(x, y, LocationInfo.FEED);
    }

    public boolean isAlive() {
        if (lifeTime < 0)
            return false;

        var head = snake.getHead(); // TODO remove matrix
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

    private void calculate() {
        var input = new HashMap<String, Double>();

        var sensorList = snake.getSensorList();
        for (var i = 0; i < sensorList.size(); i++)
            input.put(
                    String.format("Sensor %02d", i),
                    (double) sensorList.get(i).calculateDistance()
            );

        var distanceToFeed = calculateDistanceToFeed();
        input.put("distanceToFeed", distanceToFeed);
        input.put("lifeTime", (double) lifeTime);

        var response = neuralNetwork.calculate(input);
        var left = response.get("turnLeft") > 0;
        var right = response.get("turnRight") > 0;

        if (left)
            snake.turnLeft();

        if (right)
            snake.turnRight();

        if(left && !right || right && !left)
            points++;
    }

    private double calculateDistanceToFeed() {
        var head = snake.getHead();
        var diffX = Math.abs(head.getX() - feed.getX());
        var diffY = Math.abs(head.getY() - feed.getY());
        return Math.sqrt(diffX * diffX + diffY * diffY);
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

    public Snake getSnake() {
        return snake;
    }

    public Coordinates getFeed() {
        return feed;
    }
}
