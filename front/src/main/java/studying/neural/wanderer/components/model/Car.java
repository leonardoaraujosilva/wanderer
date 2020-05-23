package studying.neural.wanderer.components.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Car {

    private Coordinates center;
    private int width;
    private int height;
    private final String texture = "cars/car.png";

}
