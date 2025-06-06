import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
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

        supplier.deliverToStorage(storage, 3);

        // Первая обработка (10:00)
        SimulationTime.setTime(LocalTime.of(10, 0));
        storage.completeOrders();

        // Вторая поставка (11:00)
        SimulationTime.setTime(LocalTime.of(11, 0));
        supplier.deliverToStorage(storage, 3);
        // Вторая обработка (11:05)
        SimulationTime.setTime(LocalTime.of(11, 5));

        storage.completeOrders();

        keeper.calculateSalary();
        courier.calculateSalary();
    }
}
