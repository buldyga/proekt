package panels;

import app.Point;
import app.Task;
import app.Triangle;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.humbleui.jwm.Event;
import io.github.humbleui.jwm.EventMouseButton;
import io.github.humbleui.jwm.EventMouseScroll;
import io.github.humbleui.jwm.Window;
import io.github.humbleui.skija.Canvas;
import misc.CoordinateSystem2d;
import misc.CoordinateSystem2i;
import misc.Vector2d;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;


/**
 * Панель рисования
 */
public class PanelRendering extends GridPanel {
    /**
     * Представление проблемы
     */
    public static Task task;

    /**
     * Панель управления
     *
     * @param window     окно
     * @param drawBG     флаг, нужно ли рисовать подложку
     * @param color      цвет подложки
     * @param padding    отступы
     * @param gridWidth  кол-во ячеек сетки по ширине
     * @param gridHeight кол-во ячеек сетки по высоте
     * @param gridX      координата в сетке x
     * @param gridY      координата в сетке y
     * @param colspan    кол-во колонок, занимаемых панелью
     * @param rowspan    кол-во строк, занимаемых панелью
     */
    public PanelRendering(
            Window window, boolean drawBG, int color, int padding, int gridWidth, int gridHeight,
            int gridX, int gridY, int colspan, int rowspan
    ) {
        super(window, drawBG, color, padding, gridWidth, gridHeight, gridX, gridY, colspan, rowspan);

        CoordinateSystem2d cs = new CoordinateSystem2d(
                new Vector2d(-30.0, -30.0), new Vector2d(30.0, 30.0)
        );

        // создаём массив случайных точек
        ArrayList<Vector2d> points = new ArrayList<>();
        ArrayList<Triangle> triangles = new ArrayList<>();


        task = new Task(cs, points, triangles);
        triangles.add(new Triangle(new Vector2d(29, -28), new Vector2d(0, 27), new Vector2d(27, 0)));
        for (int i = 0; i < 10; i++)
            task.addRandomPoint();
    }

    /**
     * Обработчик событий
     * при перегрузке обязателен вызов реализации предка
     *
     * @param e событие
     */
    @Override
    public void accept(Event e) {
        // вызов обработчика предка
        super.accept(e);
        // если событие - это клик мышью
        if (e instanceof EventMouseButton ee) {
            // если последнее положение мыши сохранено и курсор был внутри
            if (lastMove != null && lastInside) {
                // обрабатываем клик по задаче
               // task.click(lastWindowCS.getRelativePos(lastMove), ee.getButton());
            }
        }
    }

    /**
     * Метод под рисование в конкретной реализации
     *
     * @param canvas   область рисования
     * @param windowCS СК окна
     */
    @Override
    public void paintImpl(Canvas canvas, CoordinateSystem2i windowCS) {
        task.paint(canvas, windowCS);
    }

    /**
     * Сохранить файл
     */
    public static void save() {
        PanelLog.info("save");
    }


    /**
     * Загрузить файл
     */
    public static void load() {
        PanelLog.info("load");
    }
}