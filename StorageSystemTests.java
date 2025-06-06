import org.junit.Assert;
import org.junit.Test;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class StorageSystemTests {

    @Test
    public void test1() {

        SimulationTime.setTime(LocalTime.of(8, 0));

        Product hinkal = new Product("Хинкаль", 50.0);
        Warehouse storage = new Warehouse(new HashMap<>(), new Coordinates(0, 0));

        Warehouseman keeper = new Warehouseman(LocalTime.of(8, 0), LocalTime.of(16, 0));
        Courier courier = new Courier(LocalTime.of(10, 0), LocalTime.of(19, 0));
        storage.addStorekeeper(keeper);
        storage.addCourier(courier);

        SimulationTime.setTime(LocalTime.of(8, 30));
        User user = new User("user@mail.com", "Abacab Abacabovich", new Coordinates(15, 15));

        // Заказ 5 хинкалей
        Product[] fiveHinkals = new Product[5];

        Arrays.fill(fiveHinkals, hinkal);
        user.makeOrder(fiveHinkals, storage);

        SimulationTime.setTime(LocalTime.of(9, 0));
        Producer supplier = new Producer("Старик Хинкалыч", hinkal);
        supplier.deliverToStorage(storage, 5);

        // Первая обработка (10:00)
        SimulationTime.setTime(LocalTime.of(10, 0));
        storage.completeOrders();

        supplier.deliverToStorage(storage, 3);


        storage.completeOrders();
        keeper.calculateSalary();
        courier.calculateSalary();

        LocalTime expectedTime = LocalTime.of(10, 26, 57);
        Assert.assertEquals(SimulationTime.getCurrentTime(), expectedTime.truncatedTo(ChronoUnit.SECONDS));
        Assert.assertEquals(courier.status, Worker.Status.SHIFT_ENDED);
        Assert.assertEquals(keeper.status, Worker.Status.SHIFT_ENDED);
        Assert.assertEquals(storage.getOrders().size(), 0);
    }

    @Test
    public void test2() {
        SimulationTime.setTime(LocalTime.of(7, 0));

        Product hinkal = new Product("Хинкаль", 500.0);
        Warehouse storage = new Warehouse(new HashMap<>(), new Coordinates(0, 0));

        Warehouseman keeper = new Warehouseman(LocalTime.of(8, 0), LocalTime.of(16, 0));
        Courier courier = new Courier(LocalTime.of(10, 0), LocalTime.of(19, 0));
        Assert.assertEquals(keeper.status, Worker.Status.NOT_WORKING);

        storage.addStorekeeper(keeper);
        storage.addCourier(courier);

        SimulationTime.setTime(LocalTime.of(8, 30));
        User user = new User("user@mail.com", "Abacab Abacabovich", new Coordinates(15, 15));

        Product[] fiveHinkals = new Product[4];
        Arrays.fill(fiveHinkals, hinkal);
        user.makeOrder(fiveHinkals, storage);
        SimulationTime.setTime(LocalTime.of(9, 0));
        storage.completeOrders();
        System.out.println(SimulationTime.getCurrentTime());
        Assert.assertEquals(courier.status, Worker.Status.NOT_WORKING);
        Assert.assertEquals(keeper.status, Worker.Status.NOT_WORKING);
        Assert.assertEquals(storage.getOrders().size(), 1);

    }

    @Test
    public void test3() {
        // Устанавливаем начальное время симуляции
        SimulationTime.setTime(LocalTime.of(12, 0)); // Полдень

        // Создаем товар и склад
        Product pizza = new Product("Пицца", 750.0);
        Product cola = new Product("Кола", 250.0);
        Warehouse storage = new Warehouse(new HashMap<>(), new Coordinates(0, 0));

        // Нанимаем персонал
        Warehouseman keeper = new Warehouseman(LocalTime.of(8, 0), LocalTime.of(18, 0));
        Courier courier = new Courier(LocalTime.of(12, 0), LocalTime.of(20, 0));
        storage.addStorekeeper(keeper);
        storage.addCourier(courier);

        // Регистрируем поставщика
        Producer supplier = new Producer("Пиццерия", pizza);
        Producer supplier2 = new Producer("Колер", cola);
        storage.addSupplier(supplier);
        storage.addSupplier(supplier2);
        // Пользователь создает заказ в 12:05
        SimulationTime.setTime(LocalTime.of(12, 5));
        User user = new User("pizza_lover@mail.com", "Пицца Фан", new Coordinates(3, 4));
        user.makeOrder(new Product[]{pizza, cola}, storage);

        Assert.assertEquals(storage.getOrders().size(), 1);

        // Поставка товара
        supplier.deliverToStorage(storage, 1);
        supplier2.deliverToStorage(storage, 1);
        // Обработка заказа
        storage.completeOrders();

        System.out.println("Текущее время симуляции: " + SimulationTime.getCurrentTime());
        keeper.calculateSalary();

    }
}