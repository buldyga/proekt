import app.Point;
import app.Task;
import app.Triangle;
import misc.CoordinateSystem2d;
import misc.Vector2d;
import org.junit.Test;
import panels.PanelLog;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
public class UnitTest {


    private static void test(ArrayList<Vector2d> points,  double Answer, ArrayList<Triangle> triangles) {
        Task task = new Task(new CoordinateSystem2d(10, 10, 20, 20), points, triangles);
        task.solve();
        assert (task.getS() - Answer < 0.0000001);
    }
    @Test
    public void test1() {
        ArrayList<Vector2d> points = new ArrayList<>();
        ArrayList<Triangle> triangles = new ArrayList<>();
        points.add(new Vector2d(1, 2));
        points.add( new Vector2d(3, 4));
        points.add(new Vector2d(0, 8));
        points.add(new Vector2d(0, 0));
        points.add(new Vector2d(-1, 9));
        points.add(new Vector2d(3, 7));

        double Answer = 4.89;

        test(points, Answer, triangles);
    }
    @Test
    public void test2() {
        ArrayList<Vector2d> points = new ArrayList<>();
        ArrayList<Triangle> triangles = new ArrayList<>();
        points.add(new Vector2d(1, 4));
        points.add( new Vector2d(8, 8));
        points.add(new Vector2d(2, 2));
        points.add(new Vector2d(8, 3));
        points.add(new Vector2d(2, 2));
        points.add(new Vector2d(8, 7));

        double Answer = 0;

        test(points, Answer, triangles);
    }
    @Test
    public void test3() {
        ArrayList<Vector2d> points = new ArrayList<>();
        ArrayList<Triangle> triangles = new ArrayList<>();
        points.add(new Vector2d(-10, 20));
        points.add( new Vector2d(20, 0));
        points.add(new Vector2d(0, -25));
        points.add(new Vector2d(1, 2));
        points.add(new Vector2d(3, 0));
        points.add(new Vector2d(5, 2));

        double Answer = 4;

        test(points, Answer, triangles);
    }
}
