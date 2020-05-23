package studying.neural.wanderer.service;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.ImageView;
import studying.neural.wanderer.Game;
import studying.neural.wanderer.OnGameEnded;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class CreateTooManyGames implements OnGameEnded {

    private static final int WIDTH = 500;
    private static final int HEIGHT = 500;
    private static final int MAX_LIFE_TIME = 2000;
    private static final int SCALE = 5;
    private static final int GAMES_COUNT = 50;

    private boolean finished = false;

    public void createAndStart(ImageView imageView) {

        finished = false;
        var gameList = new ArrayList<Game>();
        for(var i = 0; i < GAMES_COUNT; i++)
            gameList.add(new Game(WIDTH, HEIGHT, MAX_LIFE_TIME, this));

        new Thread(() -> render(gameList, imageView)).start();

        for(var each : gameList)
            new Thread(each).start();
    }

    @Override
    public void onGameEnded(int points) {
        System.out.println("Points: " + points);
        finished = true;
    }

    private void render(List<Game> gameList, ImageView imageView) {

        while(!finished) {
            var image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

            for(var game : gameList) {
                var body = game.getSnake().getBody();
                for (var each : body)
                    printWithScale(image, each.getX(), each.getY(), Color.GREEN);

                printWithScale(image, game.getFeed().getX(), game.getFeed().getY(), Color.RED);
            }

            var rendered = SwingFXUtils.toFXImage(image, null);
            Platform.runLater(() ->  imageView.setImage(rendered));
        }
    }

    private void printWithScale(BufferedImage image, int x, int y, Color color) {
        int rgbColor = color.getRGB();

        for(int i = 0, posX = x; i < SCALE && posX < image.getWidth(); posX++, i++) {
            for (int j = 0, posY = y; j < SCALE && posY < image.getHeight(); posY++, j++)
                image.setRGB(posX, posY, rgbColor);
        }
    }
}
