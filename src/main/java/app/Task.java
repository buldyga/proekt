package app;

import io.github.humbleui.jwm.MouseButton;
import io.github.humbleui.skija.Canvas;
import io.github.humbleui.skija.Paint;
import io.github.humbleui.skija.Rect;
import misc.CoordinateSystem2d;
import misc.CoordinateSystem2i;
import misc.Vector2d;
import misc.Vector2i;
import panels.PanelLog;

import java.util.ArrayList;

/**
 * Класс задачи
 */
public class Task {
    /**
     * Текст задачи
     */
    public static final String TASK_TEXT = """
            ПОСТАНОВКА ЗАДАЧИ:
            На плоскости задано множество треугольников. Найти
            такие два треугольника, что площадь фигуры,
            находящейся внутри обоих треугольников,
            будет максимальна. В качестве ответа: выделить
            найденные два треугольника, выделить контур фигуры,
            находящейся внутри обоих треугольников, желательно
            "залить цветом" внутреннее пространство этой фигуры.""";

    /**
     * Вещественная система координат задачи
     */
    private final CoordinateSystem2d ownCS;
    /**
     * Список точек
     */
    private final ArrayList<Vector2d> points;
    private final ArrayList<Triangle> triangles;

    private boolean isSolved;
    private Polygon Solution;
    private Vector2i solutionT;
    /**
     * Размер точки
     */
    private static final int POINT_SIZE = 3;
    /**
     * Последняя СК окна
     */
    private CoordinateSystem2i lastWindowCS;

    /**
     * Задача
     *
     * @param ownCS  СК задачи
     * @param points массив точек
     */
    public Task(CoordinateSystem2d ownCS, ArrayList<Vector2d> points, ArrayList<Triangle> triangles) {
        this.ownCS = ownCS;
        this.points = points;
        this.triangles = triangles;
        this.isSolved = false;
        this.Solution = null;
        this.solutionT = null;
    }

    /**
     * Рисование задачи
     *
     * @param canvas   область рисования
     * @param windowCS СК окна
     */
    public void paint(Canvas canvas, CoordinateSystem2i windowCS) {
        // Сохраняем последнюю СК
        lastWindowCS = windowCS;

        canvas.save();
        // создаём перо
        try (var paint = new Paint())
        {
            for (Vector2d p: points)
            {
                if (points.size() % 3 == 0)
                {
                    int newPoints = points.size() - triangles.size() * 3;
                    for (int i = points.size() - 1; i >= points.size() - newPoints; i -= 3)
                    {
                        triangles.add(new Triangle(points.get(i), points.get(i - 1), points.get(i - 2)));
                    }
                }

                Vector2i windowPos = windowCS.getCoords(p.x, p.y, ownCS);
                canvas.drawRect(Rect.makeXYWH(windowPos.x - POINT_SIZE, windowPos.y - POINT_SIZE, POINT_SIZE * 2, POINT_SIZE * 2), paint);
            }

            for (Triangle t: triangles)
            {
                t.paint(canvas, windowCS, ownCS, paint);
            }

            if (isSolved && Solution != null)
            {
                try (var paintT = new Paint())
                {
                    paintT.setColor(0xFFFF0000);
                    triangles.get(solutionT.x).paint(canvas, windowCS, ownCS, paintT);
                    triangles.get(solutionT.y).paint(canvas, windowCS, ownCS, paintT);
                }
                try (var paintS = new Paint())
                {
                    paintS.setColor(0x800000FF);
                    Solution.paint(canvas, windowCS, ownCS, paintS);
                }
            }
        }

        canvas.restore();
    }

    /**
     * Добавить точку
     *
     * @param pos      положение
     */
    public void addPoint(Vector2d pos)
    {
        points.add(pos);
        PanelLog.info("точка " + pos + " добавлена");
    }

    public void addRandomPoint()
    {
        Vector2d pos = ownCS.getRandomCoords();
        addPoint(pos);
    }

    public void addRandomTriangles(int cnt)
    {
        for (int i = 0; i < 3 * cnt; i++)
        {
            addRandomPoint();
        }
        PanelLog.info("добавлено " + cnt + " случайных треугольников");
    }

    /*public void click(Vector2i pos, MouseButton mouseButton)
    {
        if (lastWindowCS == null) return;
        // получаем положение на экране
        Vector2d taskPos = ownCS.getCoords(pos, lastWindowCS);
        addPoint(taskPos);
    }*/

    Vector2d linesIntersection(Vector2d A, Vector2d B, Vector2d C, Vector2d D)
    {
        return new Vector2d(((A.x*B.y - A.y*B.x) * (C.x-D.x) - (A.x-B.x) * (C.x*D.y - C.y*D.x)) / ((A.x-B.x) * (C.y-D.y) - (A.y-B.y) * (C.x-D.x)),
                ((A.x*B.y - A.y*B.x) * (C.y-D.y) - (A.y-B.y) * (C.x*D.y - C.y*D.x)) / ((A.x-B.x) * (C.y-D.y) - (A.y-B.y) * (C.x-D.x)));
    }

    Polygon clip(Polygon P, Vector2d A, Vector2d B)
    {
        ArrayList<Vector2d> tmp = new ArrayList<Vector2d>();
        for (int i = 0; i < P.vertices.size(); i++)
        {
            int k = (i + 1) % P.vertices.size();
            Vector2d iv = P.vertices.get(i);
            Vector2d kv = P.vertices.get(k);

            double iPos = (B.x - A.x) * (iv.y - A.y) - (B.y - A.y) * (iv.x - A.x);
            double kPos = (B.x - A.x) * (kv.y - A.y) - (B.y - A.y) * (kv.x - A.x);

            if (iPos < 0 && kPos < 0)
            {
                tmp.add(kv);
            }
            else if (iPos >= 0 && kPos < 0)
            {
                tmp.add(linesIntersection(A, B, iv, kv));
                tmp.add(kv);
            }
            else if (iPos < 0 && kPos >= 0)
            {
                tmp.add(linesIntersection(A, B, iv, kv));
            }
        }
            return new Polygon(tmp);
    }

    Polygon SutherlandClip(Polygon P, Triangle T)
    {
        for (int i = 0; i < 3; i++)
        {
            int k = (i + 1) % 3;
            P = clip(P, T.vertices.get(i), T.vertices.get(k));
            if (P == null)
                return null;
        }
        return P;
    }

    public void solve()
    {
        double max_area = 0.0;
        Polygon maxPoly = null;
        Vector2i maxIndx = new Vector2i(0, 1);
        for (int i = 0; i < triangles.size(); i++)
        {
            Polygon P = new Polygon(triangles.get(i));
            for (int j = i + 1; j < triangles.size(); j++)
            {
                Triangle T = triangles.get(j);
                Polygon C = SutherlandClip(P, T);
                if (C == null)
                {
                    continue;
                }
                double S = C.area();
                if (S > max_area)
                {
                    max_area = S;
                    maxPoly = C;
                    maxIndx.x = i;
                    maxIndx.y = j;
                }
            }
        }
        isSolved = true;
        Solution = maxPoly;
        solutionT = maxIndx;
        if (maxPoly != null)
            PanelLog.info("Максимальная площадь пересечения равна " + String.format("%.2f", max_area).replace(",", "."));
        else
            PanelLog.info("Никакие два треугольника не имеют общей площади");
    }

    public void clear()
    {
        points.clear();
        triangles.clear();
        isSolved = false;
        Solution = null;
        solutionT = null;
    }
}
