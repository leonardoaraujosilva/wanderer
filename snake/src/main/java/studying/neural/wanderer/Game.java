package studying.neural.wanderer;

import lombok.SneakyThrows;
import studying.neural.wanderer.domain.Coordinates;
import studying.neural.wanderer.domain.Direction;
import studying.neural.wanderer.domain.LocationInfo;
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

    //private final Matrix matrix;
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
        this.neuralNetwork = neuralNetwork;
        this.onGameEnded = onGameEnded;

        var spawnPoint = new Coordinates(width / 10, height / 2);
        this.snake = new Snake(spawnPoint, Direction.EAST);
        this.lifeTime = maxLifeTime;

        this.snake.getSensorList().addAll(
                defaultSensorListCreator.createDefault(this, snake)
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

            snake.move();
            isAlive = isAlive();
            isEating = isEating();

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
                if (getPoint(x, y) == LocationInfo.BACKGROUND)
                    randomSerialFeed--;
            }
        }

        feed = new Coordinates(x, y);
    }

    public boolean isAlive() {
        if (lifeTime < 0)
            return false;

        var head = snake.getHead();
        var pointValue = getPoint(head.getX(), head.getY());

        return pointValue == LocationInfo.BACKGROUND ||
                pointValue == LocationInfo.FEED;
    }

    private boolean isEating() {
        var head = snake.getHead();
        return feed.getX() == head.getX() &&
                feed.getY() == head.getY();
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

        if (left && !right || right && !left)
            points++;
    }

    public LocationInfo getPoint(int x, int y) {

        if(feed.getX() == x && feed.getY() == y)
            return LocationInfo.FEED;

        if (x < 0 || x >= width || y < 0 || y >= height)
            return LocationInfo.INVALID;

        for (var each : snake.getBody())
            if (each.getX() == x && each.getY() == y)
                return LocationInfo.SNAKE;

        return LocationInfo.BACKGROUND;
    }

    private double calculateDistanceToFeed() {
        var head = snake.getHead();
        var diffX = Math.abs(head.getX() - feed.getX());
        var diffY = Math.abs(head.getY() - feed.getY());
        return Math.sqrt(diffX * diffX + diffY * diffY);
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
