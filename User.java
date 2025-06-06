/**
 * Класс Пользователь (User)
 *
 * Регистрируется в системе, указывает адрес и создает заказы.
 */
public class User {
    private final String id;
    private final String email;
    private final String name;
    private final Coordinates coordinates;

    /**
     * Конструктор пользователя
     * @param email почта пользователя
     * @param name имя пользователя
     * @param coordinates координаты пользователя
     */
    public User(String email, String name, Coordinates coordinates) {
        this.id = IdGenerator.generateId(IdGenerator.ObjectType.USER);
        this.email = email;
        this.name = name;
        this.coordinates = coordinates;
    }

    // Геттеры
    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    /**
     * Создание нового заказа
     * @param products список товаров
     * @param storage целевой склад обработки
     */
    public void makeOrder(Product[] products, Warehouse storage) {
        Order order = new Order(products, this.coordinates, storage);
        storage.addOrder(order);
    }
}