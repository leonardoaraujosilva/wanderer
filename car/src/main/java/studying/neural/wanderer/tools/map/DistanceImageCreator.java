package studying.neural.wanderer.tools.map;

import lombok.SneakyThrows;
import studying.neural.wanderer.domain.model.Map;
import studying.neural.wanderer.domain.model.TrackType;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class DistanceImageCreator {

    @SneakyThrows
    public void saveImage(Map map) {
        BufferedImage newImage = new BufferedImage(map.getWidth(), map.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < map.getWidth(); i++) {
            for (int j = 0; j < map.getHeight(); j++) {

                if(map.isTrack(i, j) == TrackType.BACKGROUND)
                    continue;

                Integer a = map.getWeight(i, j);
                Color newColor = a == null ? Color.BLACK : mixColors(Color.RED, Color.YELLOW, (double) a / map.getMaxDistance());
                newImage.setRGB(i, j, newColor.getRGB());
            }
        }
        File output = new File("result.jpg");
        ImageIO.write(newImage, "jpg", output);
    }

    private Color mixColors(Color color1, Color color2, double percent) {
        double inverse_percent = 1.0 - percent;
        int redPart = (int) (color1.getRed() * percent + color2.getRed() * inverse_percent);
        int greenPart = (int) (color1.getGreen() * percent + color2.getGreen() * inverse_percent);
        int bluePart = (int) (color1.getBlue() * percent + color2.getBlue() * inverse_percent);
        return new Color(redPart, greenPart, bluePart);
    }
}
