import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * Класс Склад (Warehouse)
 *
 * Центральный компонент системы, который связывает курьеров, кладовщиков и поставщиков.
 * Отвечает за хранение товарных запасов, обработку входящих заказов и управление логистикой.
 */
public class Warehouse {
    private final String id;                    // Уникальный идентификатор склада
    private final Coordinates coordinates;      // Географические координаты склада
    private final Map<Product, Integer> stock;  // Товарные запасы: товар → количество

    private final List<Producer> suppliers = new ArrayList<>();      // Список поставщиков
    private final List<Warehouseman> storekeepers = new ArrayList<>(); // Работающие кладовщики
    private final List<Courier> couriers = new ArrayList<>();        // Работающие курьеры
    private final Queue<Order> orders = new LinkedList<>();          // Очередь заказов на обработку

    /**
     * Конструктор склада
     * @param stock начальные товарные запасы
     * @param coordinates географическое положение
     */
    public Warehouse(Map<Product, Integer> stock, Coordinates coordinates) {
        this.id = IdGenerator.generateId(IdGenerator.ObjectType.WAREHOUSE);
        this.stock = stock;
        this.coordinates = coordinates;
    }

    // Геттеры
    public String getId() {
        return id;
    }

    public Map<Product, Integer> getStock() {
        return stock;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public Queue<Order> getOrders() {
        return orders;
    }

    // Методы добавления персонала
    public void addCourier(Courier courier) {
        couriers.add(courier);
    }

    public void addSupplier(Producer supplier) {
        suppliers.add(supplier);
    }

    public void addStorekeeper(Warehouseman storekeeper) {
        storekeepers.add(storekeeper);
    }

    /**
     * Добавить заказ в очередь обработки
     * @param order новый заказ
     */
    public void addOrder(Order order) {
        orders.offer(order);
    }

    /**
     * Обработать все заказы в очереди
     *
     * Алгоритм:
     * 1. Проверяет наличие товаров для каждого заказа
     * 2. При нехватке - заказывает поставку и откладывает заказ
     * 3. При наличии - запускает обработку
     * 4. Необработанные заказы возвращаются в очередь
     */
    public void completeOrders() {
        Queue<Order> pendingOrders = new LinkedList<>();
        List<Order> incompleteOrders = new ArrayList<>();

        while (!orders.isEmpty()) {
            Order order = orders.poll();
            boolean needSupply = false;
            boolean missingSupplier = false;

            for (Map.Entry<Product, Integer> entry : order.getOriginalProducts().entrySet()) {
                Product p = entry.getKey();
                int required = entry.getValue();
                int available = stock.getOrDefault(p, 0);

                if (available < required) {
                    int deficit = required - available;
                    if (!requestSupply(p, deficit + 10)) {
                        missingSupplier = true;
                    }
                    needSupply = true;
                }
            }

            if (missingSupplier) {
                System.out.println("🚫 Заказ " + order.getOrderId() +
                        " неполный: отсутствуют поставщики для некоторых товаров");
                pendingOrders.offer(order);
            } else if (needSupply) {
                System.out.println("⏳ Заказ " + order.getOrderId() +
                        " отложен: ожидается поставка товаров");
                pendingOrders.offer(order);
            } else {
                if (order.removeUnavailableProducts(this)) {
                    processOrder(order);
                } else {
                    System.out.println("Заказ " + order.getOrderId() +
                            " отменен: товары отсутствуют");
                }
            }
        }

        orders.addAll(pendingOrders);

        if (!incompleteOrders.isEmpty()) {
            System.out.println("\n=== НЕПОЛНЫЕ ЗАКАЗЫ ТРЕБУЮТ ВНИМАНИЯ ===");
            for (Order order : incompleteOrders) {
                System.out.println("🛒 Заказ " + order.getOrderId() +
                        " имеет товары без поставщиков");
            }
        }
    }

    /**
     * Запросить поставку товара
     *
     * При нехватке товара на складе:
     * 1. Ищет подходящего поставщика
     * 2. Инициирует доставку
     *
     * @param product требуемый товар
     * @param requestedAmount необходимое количество
     * @return true если поставщик найден и доставка запрошена
     */
    private boolean requestSupply(Product product, int requestedAmount) {
        for (Producer supplier : suppliers) {
            if (supplier.getProduct().equals(product)) {
                supplier.deliverToStorage(this, requestedAmount);
                return true;
            }
        }
        System.out.println("⚠️ ВНИМАНИЕ: Для товара '" + product.getName() +
                "' нет зарегистрированных поставщиков!");
        return false;
    }

    /**
     * Обработать заказ
     *
     * 1. Находит свободного кладовщика для сборки
     * 2. После сборки передает заказ курьеру
     *
     * @param order заказ для обработки
     */
    private void processOrder(Order order) {
        // Поиск свободного кладовщика
        for (Warehouseman keeper : storekeepers) {
            if (keeper.isWorking() && keeper.getCurrentOrder() == null) {
                keeper.setCurrentOrder(order);
                keeper.completeWork();
                break;
            }
        }

        // Поиск курьера для собранного заказа
        if (order.getStatus() == Order.OrderStatus.PROCESSING) {
            for (Courier courier : couriers) {
                if (courier.isWorking() && courier.getCurrentOrder() == null) {
                    courier.setCurrentOrder(order);
                    courier.completeWork();
                    break;
                }
            }
        }
    }
}