package app;
import io.github.humbleui.skija.Canvas;
import io.github.humbleui.skija.Paint;
import misc.*;

import java.util.ArrayList;
public class Triangle
{
    public Vector2d A;
    public Vector2d B;
    public Vector2d C;
    public ArrayList<Vector2d> vertices;
    public Triangle(Vector2d A, Vector2d B, Vector2d C)
    {
        this.A = A;
        this.B = B;
        this.C = C;
        ArrayList<Vector2d> tmp = new ArrayList<Vector2d>();
        tmp.add(A);
        tmp.add(B);
        tmp.add(C);
        this.vertices = tmp;
        this.sortCw();
    }
    public Triangle(ArrayList<Vector2d> v)
    {
        this.vertices = v;
        this.A = v.get(0);
        this.B = v.get(1);
        this.C = v.get(2);
        this.sortCw();
    }
    public void paint(Canvas canvas, CoordinateSystem2i winCS, CoordinateSystem2d trueCS, Paint p)
    {
        Vector2i winPosA = winCS.getCoords(A, trueCS);
        Vector2i winPosB = winCS.getCoords(B, trueCS);
        Vector2i winPosC = winCS.getCoords(C, trueCS);
        canvas.drawLine((float) winPosA.x, (float) winPosA.y, (float) winPosB.x, (float) winPosB.y, p);
        canvas.drawLine((float) winPosB.x, (float) winPosB.y, (float) winPosC.x, (float) winPosC.y, p);
        canvas.drawLine((float) winPosC.x, (float) winPosC.y, (float) winPosA.x, (float) winPosA.y, p);
    }
    public void sortCw()
    {
        Vector2d c = Vector2d.subtract(C, A);
        Vector2d b = Vector2d.subtract(B, A);
        double n = b.y * c.x - b.x * c.y;

        if (n < 0)
        {
            Vector2d tmp = B;
            B = C;
            C = tmp;
            ArrayList<Vector2d> _tmp = new ArrayList<Vector2d>();
            _tmp.add(A);
            _tmp.add(B);
            _tmp.add(C);
            vertices = _tmp;
        }
    }
}