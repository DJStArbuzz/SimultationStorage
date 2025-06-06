import java.time.Duration;
import java.time.LocalTime;

/**
 * Абстрактный класс Работник (Worker)
 *
 * Базовый класс для всех сотрудников (курьеров, кладовщиков).
 * Содержит общую логику учета рабочего времени, расчета зарплаты и управления статусами.
 */
public abstract class Worker {
    /**
     * Статусы работника:
     * NOT_WORKING - не работает (вне смены)
     * BUSY - занят (выполняет заказ)
     * SHIFT_ENDED - смена завершена
     */
    public enum Status {
        NOT_WORKING("Не работает"),
        BUSY("Занят"),
        SHIFT_ENDED("Смена закончилась");

        private final String displayName;

        Status(String displayName) {
            this.displayName = displayName;
        }

        @Override
        public String toString() {
            return displayName;
        }
    }

    protected final String id;              // Уникальный ID работника
    protected final LocalTime startShift;   // Начало рабочей смены
    protected final LocalTime endShift;     // Конец рабочей смены

    protected Status status;                // Текущий статус
    protected Order currentOrder;           // Текущий обрабатываемый заказ
    protected float money;                  // Заработанные деньги

    /**
     * Конструктор работника
     * @param startShift начало смены
     * @param endShift конец смены
     */
    public Worker(LocalTime startShift, LocalTime endShift) {
        this.id = IdGenerator.generateId(IdGenerator.ObjectType.WORKER);
        this.startShift = startShift;
        this.endShift = endShift;
        this.status = Status.NOT_WORKING;
        this.money = 0;
    }

    // Геттеры
    public String getId() {
        return id;
    }

    public Order getCurrentOrder() {
        return currentOrder;
    }

    public LocalTime getEndShift() {
        return endShift;
    }

    public LocalTime getStartShift() {
        return startShift;
    }

    public Status getStatus() {
        return status;
    }

    public float getMoney() {
        return money;
    }

    // Сеттеры
    public void setCurrentOrder(Order currentOrder) {
        this.currentOrder = currentOrder;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * Проверить, работает ли сотрудник в текущее время
     * @return true если текущее время внутри рабочей смены
     */
    public boolean isWorking() {
        LocalTime currentTime = SimulationTime.getCurrentTime();
        return !currentTime.isBefore(startShift) &&
                !currentTime.isAfter(endShift);
    }

    /**
     * Рассчитать заработную плату
     *
     * Формула: длительность смены (часы) × 300 руб/час
     * После расчета автоматически устанавливает статус SHIFT_ENDED
     */
    public void calculateSalary() {
        int hours = (int) Duration.between(startShift, endShift).toHours();
        money = hours * 300;
        status = Status.SHIFT_ENDED;
        System.out.println("Работник " + id + " заработал: " + money + " руб.");
    }

    /**
     * Абстрактный метод для выполнения работы
     * Реализуется в конкретных классах-наследниках
     */
    public abstract void completeWork();
}