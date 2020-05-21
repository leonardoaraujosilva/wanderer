package studying.neural.wanderer.domain.model;

import lombok.Data;

@Data
public class Map {

    private final Integer[][] coordinates;
    private final int width;
    private final int height;

    public Map(int width, int height) {
        this.width = width;
        this.height = height;
        this.coordinates = new Integer[width][height];
    }

    public Integer getCoordinate(int x, int y) {
        return coordinates[x][y];
    }

    public void setCoordinate(int x, int y, Integer value) {
        this.coordinates[x][y] = value;
    }
}
