package studying.neural.wanderer.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Coordinates {

    private int x;
    private int y;

    public void set(Coordinates coordinates) {
        this.x = coordinates.getX();
        this.y = coordinates.getY();
    }

    public void set(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
