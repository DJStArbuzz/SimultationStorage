/*
    Класс Координаты

    есть координаты склада и координаты пользователя (давайте считать,
    что координаты принимают значения от 1 до 100, расстояние вычисляется как на координатной плоскости),
    для того, чтобы пройти 1 у.е. расстояния необходимо 30 секунд + для того,
    чтобы выйти со склада нужна 1 минута + процесс выдачи пользователю товара также длится 1 минута
 */
public class Coordinates {
    private final Integer x;
    private final Integer y;

    public Coordinates(Integer x, Integer y) {
        this.x = x;
        this.y = y;
    }

    // Геттеры
    public Integer getX() { return x; }
    public Integer getY() { return y; }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    public double distanceTo(Coordinates other) {
        return Math.sqrt(Math.pow(x - other.x, 2) + Math.pow(y - other.y, 2));
    }
}