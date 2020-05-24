package studying.neural.wanderer.service;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.ImageView;
import lombok.SneakyThrows;
import studying.neural.wanderer.Game;
import studying.neural.wanderer.NeuralNetwork;
import studying.neural.wanderer.OnGameEnded;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// TODO refactor, it's only a preview for tests
public class CreateTooManyGames implements OnGameEnded {

    private static final int WIDTH = 50;
    private static final int HEIGHT = 50;
    private static final int MAX_LIFE_TIME = 250;
    private static final int SCALE = 10;
    private static final int GAMES_COUNT = 100;

    private int generation = 1;
    private int betterGen = 0;

    private int running;
    private int betterPoints = 1;
    private NeuralNetwork betterNetwork = null;

    private Map<Integer, Integer> counter;

    private NeuralNetworkCreator neuralNetworkCreator = new NeuralNetworkCreator();
    private List<Game> gameList;

    public void createAndStart(ImageView imageView) {

        counter = new HashMap<>();
        gameList = new ArrayList<Game>();

        if (betterNetwork != null)
            gameList.add(new Game(WIDTH, HEIGHT, MAX_LIFE_TIME, betterNetwork, this));

        while (gameList.size() < GAMES_COUNT)
            gameList.add(new Game(WIDTH, HEIGHT, MAX_LIFE_TIME, neuralNetworkCreator.create(betterNetwork), this));

        running = gameList.size();
        new Thread(() -> render(imageView)).start();

        for (var each : gameList)
            new Thread(each).start();
    }

    @Override
    public synchronized void onGameEnded(Game game, NeuralNetwork neuralNetwork) {
        var points = game.getPoints();

        running--;
        if (points > betterPoints || points == betterPoints && generation > betterGen + 20) {
            betterNetwork = neuralNetwork;
            betterPoints = points;
            betterGen = generation;
            System.out.println("NEW RECORD = Points: " + points + " neuralNetwork:" + neuralNetwork);
        }

        counter.put(points, counter.getOrDefault(points, 0) + 1);

    }

    @SneakyThrows
    private void render(ImageView imageView) {
        System.out.println("Generation: " + generation++);

        while (running > 0) {
            var image = new BufferedImage(WIDTH * SCALE, HEIGHT * SCALE, BufferedImage.TYPE_INT_RGB);

            for (var game : gameList) {
                if (game.isRunning()) {
                    var body = game.getSnake().getBody();
                    for (var each : body)
                        printWithScale(image, each.getX(), each.getY(), Color.GREEN);

                    printWithScale(image, game.getFeed().getX(), game.getFeed().getY(), Color.RED);
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
