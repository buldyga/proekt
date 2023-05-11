package app;

import misc.Misc;
import misc.Vector2d;

import java.util.Objects;

/**
 * Класс точки
 */
public class Point {
    public final Vector2d pos;

    public Point(Vector2d pos) {
        this.pos = pos;
    }

    public int getColor() {
        return Misc.getColor(0xCC, 0x00, 0xFF, 0x0);
    }

    public Vector2d getPos() {
        return pos;
    }


    @Override
    public String toString() {
        return "Point{" +
                pos +
                '}';
    }
}
