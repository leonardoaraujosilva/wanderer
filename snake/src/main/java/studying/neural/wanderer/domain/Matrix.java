package studying.neural.wanderer.domain;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Matrix {

    private final int width;
    private final int height;
    private final LocationInfo[][] matrix;

    public Matrix(int width, int height) {
        this.width = width;
        this.height = height;
        this.matrix = new LocationInfo[width][height];
        initializeMatrix();
    }

    private void initializeMatrix() {
        for (var x = 0; x < width; x++)
            for (var y = 0; y < height; y++)
                matrix[x][y] = LocationInfo.BACKGROUND;
    }

    public LocationInfo[][] getMatrix() {
        return matrix;
    }

    public void setPoint(int x, int y, LocationInfo value) {
        if(isValidPoint(x, y))
            matrix[x][y] = value;
    }

    public LocationInfo getPoint(int x, int y) {
        if(isValidPoint(x, y))
            return matrix[x][y];
        return LocationInfo.INVALID;
    }

    public boolean isValidPoint(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

}
