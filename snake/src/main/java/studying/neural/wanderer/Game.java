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
    private int feedAte = 0;
    private Random random;
    private boolean running = true;
    private int maxDistance;
    private int leftCounter = 0;
    private int rightCounter = 0;

    public static int sleep = 0;

    public Game(int width, int height, int maxLifeTime, NeuralNetwork neuralNetwork, int seedToRandomizeFeed, OnGameEnded onGameEnded) {
        this.maxLifeTime = maxLifeTime;
        this.width = width;
        this.height = height;
        this.neuralNetwork = neuralNetwork;
        this.onGameEnded = onGameEnded;
        this.random =  new Random(seedToRandomizeFeed);
        this.maxDistance = (int) Math.sqrt(width * width + height * height);

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
        boolean isEating;
        do {
            lifeTime--;

            calculate();

            if(!isAlive(snake.getNextMove()) || leftCounter >= 12 || rightCounter >= 12)
                break;

            if(leftCounter >= 4 || rightCounter >= 4) {
                points -= maxDistance / 10;
            }

            snake.move();
            isEating = isEating();

            if (isEating) {
                feedAte++;
                points += feedAte * maxDistance;
                snake.eat();
                spawnFeed();
                lifeTime = maxLifeTime;
                rightCounter = leftCounter = 0;
            }
            Thread.sleep(sleep);
        } while (true);

        var head = getSnake().getHead();
        if(getPoint(head.getX(), head.getY()) == LocationInfo.INVALID)
            points = (int) (points * 0.75);

        var distanceToFeed = calculateDistanceToFeed();
        points += maxDistance - distanceToFeed;
        running = false;
        onGameEnded.onGameEnded(this, neuralNetwork);
    }

    private void spawnFeed() {
        int x = 0, y = 0;

        do {
            var freeArea = width * height - snake.size();
            var randomSerialFeed = random.nextDouble() * freeArea;//Math.random() * freeArea;

            for (x = 0; x < width && randomSerialFeed > 0; x++) {
                for (y = 0; y < height && randomSerialFeed > 0; y++) {
                    if (getPoint(x, y) == LocationInfo.BACKGROUND)
                        randomSerialFeed--;
                }
            }
        } while(getPoint(x, y) != LocationInfo.BACKGROUND);

        feed = new Coordinates(x, y);
    }

    public boolean isRunning() {
        return running;
    }

    public boolean isAlive(Coordinates nextMove) {
        if (lifeTime < 0)
            return false;

        var pointValue = getPoint(nextMove.getX(), nextMove.getY());

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

        var head = snake.getHead();
        input.put("feedX", (double) (feed.getX() - head.getX()));
        input.put("feedY", (double) (feed.getY() - head.getY()));
        input.put("leftCounter", (double) leftCounter);
        input.put("rightCounter", (double) rightCounter);
        input.put("currentDirectionX", (double) snake.getCurrentDirection().getMovementX());
        input.put("currentDirectionY", (double) snake.getCurrentDirection().getMovementY());

        var response = neuralNetwork.calculate(input);
        var left = response.get("turnLeft") > 0;
        var right = response.get("turnRight") > 0;

        if (left) {
            snake.turnLeft();
            leftCounter++;
            rightCounter = 0;
        }

        if (right) {
            snake.turnRight();
            rightCounter++;
            leftCounter = 0;
        }
    }

    public LocationInfo getPoint(int x, int y) {

        if (feed != null && feed.getX() == x && feed.getY() == y)
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

    public int getFeedAte() {
        return feedAte;
    }
}
