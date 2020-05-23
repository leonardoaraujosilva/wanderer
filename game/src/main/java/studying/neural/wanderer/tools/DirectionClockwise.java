package studying.neural.wanderer.tools;

import studying.neural.wanderer.components.Direction;

import java.util.ArrayList;

public class DirectionClockwise {

    private static final DirectionClockwise INSTANCE = new DirectionClockwise();
    public static DirectionClockwise getInstance() {
        return INSTANCE;
    }

    private final CircularList<Direction> circularList = new CircularList<>();

    private DirectionClockwise() {
        circularList.add(Direction.NORTH);
        circularList.add(Direction.EAST);
        circularList.add(Direction.SOUTH);
        circularList.add(Direction.WEST);
    }

    public Direction convertRelative(Direction headDirection, Direction relativeToHead) {
        var headPos = circularList.indexOf(headDirection);
        var relativePos = circularList.indexOf(relativeToHead);
        return circularList.get(headPos + relativePos);
    }

    private static class CircularList<T> extends ArrayList<T> {
        @Override
        public T get(int index) {
            return super.get(index % size());
        }
    }

}
