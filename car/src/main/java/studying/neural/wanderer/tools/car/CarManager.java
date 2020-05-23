package studying.neural.wanderer.tools.car;

import studying.neural.wanderer.domain.model.Car;
import studying.neural.wanderer.domain.model.Coordinates;
import studying.neural.wanderer.domain.model.Map;

public class CarManager {

    public Car create(Map map) {
        var startPoints = map.getStartCoordinates();
        var random = (int) Math.floor(Math.random() * startPoints.size());
        var selected = startPoints.get(random);

        var coordinates = new Coordinates(selected.getX(), selected.getY());
        var car = new Car(coordinates, 16, 9);

        return car;
    }

}
