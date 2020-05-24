package studying.neural.wanderer.service;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.ImageView;
import jdk.jshell.spi.ExecutionControlProvider;
import lombok.SneakyThrows;
import studying.neural.wanderer.Game;
import studying.neural.wanderer.NeuralNetwork;
import studying.neural.wanderer.OnGameEnded;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

// TODO refactor, it's only a preview for tests
public class CreateTooManyGames implements OnGameEnded {

    private static final int WIDTH = 50;
    private static final int HEIGHT = 50;
    private static final int MAX_LIFE_TIME = 250;
    private static final int SCALE = 10;
    private static final int GAMES_COUNT = 5000;

    private int generation = 1;
    private int betterGen = 0;

    private int running = 0;
    private int allBetterPoints = 0;
    private int betterPoints = 0;
    private int bestFeedAte = 0;
    private NeuralNetwork betterNetwork = null;

    private Map<Integer, Integer> counter;

    private NeuralNetworkCreator neuralNetworkCreator = new NeuralNetworkCreator();
    private List<Game> gameList;
    private Random randomFeedSeed = new Random();

    @SneakyThrows
    public void createAndStart(ImageView imageView) {

        System.out.println("Best Snake this gen: Feeds:" + bestFeedAte + " Points=" + betterPoints);

        counter = new HashMap<>();
        gameList = new ArrayList<Game>();
        betterPoints = 0;
        bestFeedAte = 0;

        var seedToRandomizeFeed = randomFeedSeed.nextInt();

        if (betterNetwork != null)
            gameList.add(new Game(WIDTH, HEIGHT, MAX_LIFE_TIME, betterNetwork, seedToRandomizeFeed, this));

        while (gameList.size() < GAMES_COUNT)
            gameList.add(new Game(WIDTH, HEIGHT, MAX_LIFE_TIME, neuralNetworkCreator.create(betterNetwork), seedToRandomizeFeed, this));

        running = gameList.size();
        new Thread(() -> render(imageView)).start();

        new Thread(() -> {
            for (var each : gameList)
                new Thread(each).start();
        }).start();
    }

    @Override
    public synchronized void onGameEnded(Game game, NeuralNetwork neuralNetwork) {
        var points = game.getPoints();
        var bestPoint = false;
        var bestAte = false;
        if (points > betterPoints) {
            betterNetwork = neuralNetwork;
            betterPoints = points;
            betterGen = generation;
            if(allBetterPoints < betterPoints) {
                System.out.println("NEW RECORD[Ate = " + game.getFeedAte() + "] = Points: " + points + " neuralNetwork:" + neuralNetwork);
                allBetterPoints = betterPoints;
            }
            bestPoint = true;
        }

        if(game.getFeedAte() >= bestFeedAte) {
            bestFeedAte = game.getFeedAte();
            bestAte = true;
        }

        counter.put(points, counter.getOrDefault(points, 0) + 1);
        running--;
    }

    @SneakyThrows
    private void render(ImageView imageView) {
        System.out.println("Generation: " + generation++);

        while (running > 0) {
            var image = new BufferedImage(WIDTH * SCALE, HEIGHT * SCALE, BufferedImage.TYPE_INT_RGB);

            for (var game : gameList) {
                if (game.isRunning()) {
                    try {
                        var body = game.getSnake().getBody();
                        var random = new Random(game.getFeedAte());
                        var randomColor = new Color(random.nextFloat(), random.nextFloat(), random.nextFloat());
                        for (var each : body)
                            printWithScale(image, each.getX(), each.getY(), randomColor);

                        printWithScale(image, game.getFeed().getX(), game.getFeed().getY(), Color.RED);
                    } catch (ConcurrentModificationException ignore) {
                    }
                }
            }

            var rendered = SwingFXUtils.toFXImage(image, null);
            Platform.runLater(() -> imageView.setImage(rendered));
        }

        System.out.println("Counter (Acertos, quantidade): " + counter + "Record=" + betterPoints + " at gen " + betterGen);
        createAndStart(imageView);
    }

    private void printWithScale(BufferedImage image, int x, int y, Color color) {
        int rgbColor = color.getRGB();

        for (int i = 0, posX = x * SCALE; i < SCALE && posX >= 0 && posX < image.getWidth(); posX++, i++) {
            for (int j = 0, posY = y * SCALE; j < SCALE && posY >= 0 && posY < image.getHeight(); posY++, j++)
                image.setRGB(posX, posY, rgbColor);
        }
    }
}
