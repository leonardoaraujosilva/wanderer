package studying.neural.wanderer.service.map;

import studying.neural.wanderer.domain.dto.MapImageData;
import studying.neural.wanderer.domain.model.Coordinates;
import studying.neural.wanderer.domain.model.CoordinatesWeight;
import studying.neural.wanderer.domain.model.Map;
import studying.neural.wanderer.domain.model.TrackType;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.LinkedList;

public class MapLoader {

    private static final Color ENDLINE = TrackType.END.getColor();
    private static final Color START = TrackType.START.getColor();
    private static final Color TRACK = TrackType.TRACK.getColor();

    public MapImageData loadFromImage(File src) throws Exception {

        var image = ImageIO.read(src);
        var map = createMap(image);

        calculateDistances(map);

        return new MapImageData(map, image);
    }

    private void calculateDistances(Map map) {

        var end = map.getEndCoordinates();
        var queue = new LinkedList<CoordinatesWeight>();
        for (var each : end) {
            queue.add(new CoordinatesWeight(each, 0));
            while (!queue.isEmpty()) {
                var current = queue.poll();
                var coordinates = current.getCoordinates();

                if (current.getWeight() >= map.getWeight(coordinates.getX(), coordinates.getY()) ||
                        map.isTrack(coordinates.getX(), coordinates.getY()) == TrackType.BACKGROUND)
                    continue;

                map.setWeight(coordinates.getX(), coordinates.getY(), current.getWeight());
                for (var x = -1; x <= 1; x++) {
                    for (var y = -1; y <= 1; y++) {

                        var posX = coordinates.getX() + x;
                        var posY = coordinates.getY() + y;

                        if (posX < 0 || posX >= map.getWidth() || posY < 0 || posY >= map.getHeight() ||
                                map.isTrack(posX, posY) == TrackType.BACKGROUND)
                            continue;

                        if (current.getWeight() + 1 < map.getWeight(posX, posY))
                            queue.add(new CoordinatesWeight(new Coordinates(posX, posY), current.getWeight() + 1));
                    }
                }
            }
        }

    }

    private Map createMap(BufferedImage image) {
        var map = new Map(image.getWidth(), image.getHeight());
        for (var x = 0; x < image.getWidth(); x++) {
            for (var y = 0; y < image.getHeight(); y++) {
                var color = new Color(image.getRGB(x, y));

                if (color.equals(TRACK))
                    map.setTrack(x, y, TrackType.TRACK);

                if (color.equals(START))
                    map.setTrack(x, y, TrackType.START);

                if (color.equals(ENDLINE))
                    map.setTrack(x, y, TrackType.END);
            }
        }

        return map;
    }

}
