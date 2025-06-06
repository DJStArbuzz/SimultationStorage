import java.time.LocalTime;

/**
 * Класс Курьер (Courier)
 *
 * Работает на складе, занимается доставкой заказов.
 * Наследуется от абстрактного класса Worker.
 */
public class Courier extends Worker {
    /**
     * Конструктор курьера
     * @param startShift время начала смены
     * @param endShift время окончания смены
     */
    public Courier(LocalTime startShift, LocalTime endShift) {
        super(startShift, endShift);
    }

    /**
     * Выполнение работы по доставке заказа
     *
     * Логика работы:
     * 1. Проверяет наличие активного заказа и работает ли курьер в текущее время
     * 2. Рассчитывает расстояние между складом и пользователем
     * 3. Вычисляет время доставки: (расстояние * 30 сек) + 120 сек (фиксированное время на операции)
     * 4. Обновляет статус заказа на "Доставлен"
     * 5. Выводит информацию о доставке
     * 6. Освобождает курьера и обновляет его статус
     */
    @Override
    public void completeWork() {
        if (currentOrder == null || !isWorking()) {
            return;
        }

        double distance = calculateDistance(
                currentOrder.getStorageCoordinates(),
                currentOrder.getUserCoordinates()
        );

        int deliverySeconds = (int) (distance * 30 + 120);
        SimulationTime.addSeconds(deliverySeconds);
        LocalTime deliveryTime = SimulationTime.getCurrentTime();

        currentOrder.updateStatus(Order.OrderStatus.DELIVERED);
        System.out.println("Курьер " + id + " доставил заказ " +
                currentOrder.getOrderId() + " в " + deliveryTime);

        int returnSeconds = (int) (distance * 30);
        SimulationTime.addSeconds(returnSeconds);
        LocalTime returnTime = SimulationTime.getCurrentTime();
        System.out.println("Курьер " + id + " вернулся на склад в " + returnTime);

        currentOrder = null;
        setStatus(Status.NOT_WORKING);
    }

    /**
     * Расчет расстояния между двумя точками
     * @param storage координаты склада
     * @param user координаты пользователя
     * @return расстояние в условных единицах
     */
    private double calculateDistance(Coordinates storage, Coordinates user) {
        return storage.distanceTo(user);
    }
}