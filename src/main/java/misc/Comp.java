package misc;

import misc.Vector2d;
import java.util.Comparator;
public class Comp implements Comparator<Vector2d>
{
    private final Vector2d origin;

    public Comp(Vector2d o)
    {
        this.origin = o;
    }

    @Override
    public int compare(Vector2d a, Vector2d b) {
        double angle1 = Math.atan2(a.y - origin.y, a.x - origin.x);
        double angle2 = Math.atan2(b.y - origin.y, b.x - origin.x);

        if (angle1 < angle2)
            return 1;
        else if (angle2 < angle1)
            return -1;
        return (int)(a.length() - b.length());
    }
}
