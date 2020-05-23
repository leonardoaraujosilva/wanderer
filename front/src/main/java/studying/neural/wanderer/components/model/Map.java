package studying.neural.wanderer.components.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Map {

    private final TrackType[][] track;
    private final Integer[][] distances;
    private final int width;
    private final int height;

    private int maxDistance = -1;

    private final List<Coordinates> startCoordinates = new ArrayList<>();
    private final List<Coordinates> endCoordinates = new ArrayList<>();

    public Map(int width, int height) {
        this.width = width;
        this.height = height;
        this.distances = new Integer[width][height];
        this.track = new TrackType[width][height];

        for(var x = 0; x < width; x++) {
            for (var y = 0; y < height; y++) {
                this.track[x][y] = TrackType.BACKGROUND;
                this.distances[x][y] = null;
            }
        }
    }

    public Integer getWeight(int x, int y) {
        if(x < 0 || x >= width || y < 0 || y >= height || distances[x][y] == null)
            return Integer.MAX_VALUE;
        return distances[x][y];
    }

    public void setWeight(int x, int y, int value) {
        if(value > maxDistance)
            maxDistance = value;
        this.distances[x][y] = value;
    }

    public TrackType isTrack(int x, int y) {
        return track[x][y];
    }

    public void setTrack(int x, int y, TrackType trackType) {
        this.track[x][y] = trackType;

        if(trackType == TrackType.START)
            startCoordinates.add(new Coordinates(x,y));
        else if(trackType == TrackType.END)
            endCoordinates.add(new Coordinates(x,y));
    }

    public int getMaxDistance() {
        return maxDistance;
    }
}
