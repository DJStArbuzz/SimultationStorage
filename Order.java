import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Класс Заказ (Order)

 * Представляет заказ, созданный пользователем. Содержит информацию о составе заказа,
 * статусе выполнения, участниках процесса обработки и географических координатах.
 */
public class Order {
    // Идентификатор заказа (генерируется случайно)
    private final String orderId;
    // Идентификатор склада, обрабатывающего заказ
    private final String storageId;
    // Ссылка на склад, обрабатывающий заказ
    private final Warehouse storage;
    // Координаты склада
    private final Coordinates storageCoordinates;
    // Координаты пользователя (место доставки)
    private final Coordinates userCoordinates;
    // Сохраняем исходный состав товаров
    private final Map<Product, Integer> originalProducts;

    // Текущие товары (могут изменяться)
    private final Map<Product, Integer> products;
    // Идентификатор курьера, назначенного для доставки (-1 если не назначен)
    private String courierId = "NONE";
    // Флаг наличия достаточного количества товаров на складе
    private boolean enoughInStorage;
    // Флаг частичного выполнения заказа
    private boolean isPartial;
    // Текущий статус заказа
    private OrderStatus status = OrderStatus.CREATED;

    /**
     * Статусы жизненного цикла заказа:
     * CREATED - создан, ожидает обработки
     * PROCESSING - собирается кладовщиком
     * DELIVERED - доставлен курьером
     */
    public enum OrderStatus {
        CREATED, PROCESSING, DELIVERED
    }

    /**
     * Конструктор заказа
     *
     * @param products массив товаров в заказе
     * @param userCoordinates координаты пользователя (место доставки)
     * @param storage склад, на котором будет обрабатываться заказ
     */
    public Order(Product[] products, Coordinates userCoordinates, Warehouse storage) {
        this.orderId = IdGenerator.generateId(IdGenerator.ObjectType.ORDER);
        this.storage = storage;
        this.storageId = storage.getId();
        this.userCoordinates = userCoordinates;
        this.storageCoordinates = storage.getCoordinates();

        this.originalProducts = new HashMap<>();
        for (Product p : products) {
            originalProducts.put(p, originalProducts.getOrDefault(p, 0) + 1);
        }
        this.products = new HashMap<>(originalProducts);
    }

    // Геттеры
    public String getOrderId() {
        return orderId;
    }

    public String getStorageId() {
        return storageId;
    }

    public Warehouse getStorage() {
        return storage;
    }

    public Coordinates getStorageCoordinates() {
        return storageCoordinates;
    }

    public Coordinates getUserCoordinates() {
        return userCoordinates;
    }

    public String getCourierId() {
        return courierId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public boolean getEnoughInStorage() {
        return enoughInStorage;
    }

    public Map<Product, Integer> getProducts() {
        return products;
    }

    public Map<Product, Integer> getOriginalProducts() {
        return originalProducts;
    }

    public boolean isPartial() {
        return isPartial;
    }

    // Сеттеры
    public void setEnoughInStorage(boolean enoughInStorage) {
        this.enoughInStorage = enoughInStorage;
    }

    /**
     * Назначить курьера для доставки заказа
     * @param courierId идентификатор курьера
     */
    public void assignCourier(String courierId) {
        this.courierId = courierId;
    }

    /**
     * Обновить статус заказа
     * @param orderStatus новый статус (из перечисления OrderStatus)
     */
    public void updateStatus(OrderStatus orderStatus) {
        status = orderStatus;
    }

    /**
     * Пометить заказ как частично выполненный
     */
    public void markAsPartial() {
        this.isPartial = true;
    }

    /**
     * Сбросить заказ для повторной обработки
     * Используется при возникновении проблем с выполнением
     */
    public void resetForRetry() {
        this.status = OrderStatus.CREATED;
        this.courierId = "NONE";
    }

    /**
     * Удалить отсутствующие товары из заказа
     * @param storage склад для проверки наличия товаров
     * @return true если в заказе остались доступные товары
     */
    public boolean removeUnavailableProducts(Warehouse storage) {
        Map<Product, Integer> stock = storage.getStock();
        boolean hasAvailableItems = false;
        Iterator<Map.Entry<Product, Integer>> it = products.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<Product, Integer> entry = it.next();
            Product p = entry.getKey();
            int required = entry.getValue();
            int available = stock.getOrDefault(p, 0);

            if (available < required) {
                if (available > 0) {
                    entry.setValue(available);
                    hasAvailableItems = true;
                } else {
                    it.remove();
                }
            } else {
                hasAvailableItems = true;
            }
        }
        return hasAvailableItems;
    }
}