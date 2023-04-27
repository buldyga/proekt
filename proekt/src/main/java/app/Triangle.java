package app;


import io.github.humbleui.skija.Canvas;
import io.github.humbleui.skija.Paint;
import misc.Vector2d;

public class Triangle{
    public final Vector2d A;
    public final Vector2d B;
    public final Vector2d C;
    public Triangle(Vector2d A, Vector2d B, Vector2d C) {
        this.A = A;
        this.B = B;
        this.C = C;
    }
    public void paint(Canvas canvas, Paint p)
    {
        canvas.drawLine((float) A.x, (float) A.y, (float) B.x, (float) B.y, p);
        canvas.drawLine((float) B.x, (float) B.y, (float) C.x, (float) C.y, p);
        canvas.drawLine((float) C.x, (float) C.y, (float) A.x, (float) A.y, p);
    }
}