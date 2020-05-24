package studying.neural.wanderer.tools;

import studying.neural.wanderer.Game;
import studying.neural.wanderer.domain.Coordinates;
import studying.neural.wanderer.domain.Direction;
import studying.neural.wanderer.domain.Sensor;
import studying.neural.wanderer.domain.Snake;

import java.util.ArrayList;
import java.util.List;

public class DefaultSensorListCreator {

    private static final DefaultSensorListCreator INSTANCE = new DefaultSensorListCreator();
    public static DefaultSensorListCreator getInstance() {
        return INSTANCE;
    }

    private DefaultSensorListCreator() { }

    public List<Sensor> createDefault(Game game, Snake snake) {
        var list = new ArrayList<Sensor>();

        var front = new Coordinates(0, -1);
        list.add(new Sensor("Sensor 01", front, Direction.NORTH, game, snake));

        var left = new Coordinates(-1, 0);
        list.add(new Sensor("Sensor 02", left, Direction.WEST, game, snake));

        var right = new Coordinates(1, 0);
        list.add(new Sensor("Sensor 03", right, Direction.EAST, game, snake));
//
//        var backLeft = new Coordinates(-1, 1);
//        list.add(new Sensor("Sensor 04", backLeft, Direction.SOUTH, game, snake));
//
//        var backRight = new Coordinates(1, 1);
//        list.add(new Sensor("Sensor 05", backRight, Direction.SOUTH, game, snake));

        return list;
    }
}
