package app;


import io.github.humbleui.skija.Canvas;
import io.github.humbleui.skija.Paint;
import io.github.humbleui.skija.Point;
import misc.*;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.abs;

public class Polygon
{
    public final ArrayList<Vector2d> vertices;


    public Polygon(ArrayList<Vector2d> v)
    {

        this.vertices = v;
    }
    
    public Polygon(Triangle t)
    {
        this.vertices = t.vertices;
    }

    public void paint(Canvas canvas, CoordinateSystem2i winCS, CoordinateSystem2d trueCS, Paint p)
    {
        ArrayList<Vector2i> winPos = new ArrayList<>();
        for (Vector2d vertex : vertices) {
            winPos.add(winCS.getCoords(vertex, trueCS));
        }
        for (int i = 0; i < vertices.size(); i++)
        {
            canvas.drawLine((float) winPos.get(i).x, (float) winPos.get(i).y, (float) winPos.get((i + 1) % vertices.size()).x, (float) winPos.get((i + 1) % vertices.size()).y, p);;
        }

        int n = vertices.size();
        // 2n нужно для хранения для каждого треугольника двух опорных точек
        // ещё один элемент(с индексом 0) нужен для хранения общей третьей опорной
        // точки
        int[] renderColors = new int[n * 2 + 1];
        Point[] renderPoints = new Point[n * 2 + 1];

        // переменные для нахождения центра многоугольника
        float sumX = 0;
        float sumY = 0;

        // перебираем все вершины многоугольника кроме последней
        for (int i = 0; i < n - 1; i++) {
            // прибавляет координаты рассматриваемой точки к сумме
            sumX += vertices.get(i).x;
            sumY += vertices.get(i).y;
            // заполняем точки рисования
            renderPoints[i * 2 + 1] = new Point((float)vertices.get(i).x, (float)vertices.get(i).y);
            renderPoints[i * 2 + 2] = new Point((float)vertices.get(i + 1).x, (float)vertices.get(i + 1).y);
            // заполняем цвета рисования
            renderColors[i * 2 + 1] = 0xFF0000FF;
            renderColors[i * 2 + 2] = 0xFF0000FF;
        }

        // прибавляет координаты последней точки к сумме
        sumX += vertices.get(n - 1).x;
        sumY += vertices.get(n - 1).y;

        // заполняем точки рисования последнего треугольника
        renderPoints[n * 2 - 1] = new Point((float)vertices.get(n - 1).x, (float)vertices.get(n - 1).y);;
        renderPoints[n * 2] = new Point((float)vertices.get(0).x, (float)vertices.get(0).y);;
        // заполняем цвета рисования последнего треугольника
        renderColors[n * 2 - 1] = 0xFF0000FF;
        renderColors[n * 2] = 0xFF0000FF;

        // задаём общую опорную точку
        renderPoints[0] = new Point(sumX / n, sumY / n);
        renderColors[0] = 0xFF0000FF;

        Point[] renderPointsWindowCS = new Point[n * 2 + 1];

        for (int i = 0; i < n * 2 + 1; i++)
        {
            Vector2i displayC = winCS.getCoords(renderPoints[i].getX(), renderPoints[i].getY(), trueCS);
            renderPointsWindowCS[i] = new Point(displayC.x, displayC.y);
        }

        canvas.drawTriangleFan(renderPointsWindowCS, renderColors, p);
    }

    double area()
    {
        double tmp = 0.0;
        for (int i = 0; i < vertices.size(); i++)
        {
            tmp += vertices.get(i).x * vertices.get((i + 1) % vertices.size()).y - vertices.get(i).y * vertices.get((i + 1) % vertices.size()).x;
        }
        return 0.5 * abs(tmp);
    }
}