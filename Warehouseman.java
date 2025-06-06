import java.time.LocalTime;
import java.util.Map;

/**
 * Класс Кладовщик (Сборщик)
 *
 * Отвечает за сборку товаров по заказам, проверку наличия товаров на складе
 * и обновление складских запасов. Наследуется от абстрактного класса Worker.
 */
public class Warehouseman extends Worker {
    /**
     * Конструктор кладовщика
     * @param startShift начало смены
     * @param endShift окончание смены
     */
    public Warehouseman(LocalTime startShift, LocalTime endShift) {
        super(startShift, endShift);
    }

    /**
     * Выполнить работу по сборке заказа
     *
     * Логика работы:
     * 1. Проверяет возможность обработки заказа
     * 2. При нехватке товаров - возвращает заказ на склад
     * 3. Рассчитывает время сборки (45 сек/товар)
     * 4. Уменьшает остатки на складе
     * 5. Передает заказ на следующий этап
     */
    @Override
    public void completeWork() {
        if (currentOrder == null) {
            return;
        }

        Warehouse storage = currentOrder.getStorage();
        Map<Product, Integer> stock = storage.getStock();
        Map<Product, Integer> orderProducts = currentOrder.getProducts();
        boolean canComplete = true;

        // Проверка наличия товаров перед списанием
        for (Map.Entry<Product, Integer> entry : orderProducts.entrySet()) {
            Product p = entry.getKey();
            int quantity = entry.getValue();
            if (stock.getOrDefault(p, 0) < quantity) {
                System.out.println("Недостаточно товара '" + p.getName() +
                        "' для заказа " + currentOrder.getOrderId());
                canComplete = false;
            }
        }

        if (!canComplete) {
            System.out.println("Сборка заказа " + currentOrder.getOrderId() + " приостановлена");
            currentOrder = null;
            return;
        }

        int totalItems = 0;
        for (Map.Entry<Product, Integer> entry : orderProducts.entrySet()) {
            Product p = entry.getKey();
            int quantity = entry.getValue();
            stock.put(p, stock.get(p) - quantity);
            totalItems += quantity;
        }

        currentOrder.updateStatus(Order.OrderStatus.PROCESSING);
        System.out.println("Заказ " + currentOrder.getOrderId() + " собран и готов к доставке");

        int assemblyTime = 45 * totalItems;
        SimulationTime.addSeconds(assemblyTime);

        currentOrder = null;
    }
}