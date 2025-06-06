import java.util.Map;

/**
 * Класс Поставщик (Producer)
 *
 * Отвечает за поставку товаров на склад. Каждый поставщик связан с конкретным товаром.
 * При доставке увеличивает количество товара на складе.
 */
public class Producer {
    private final String id;     // Уникальный идентификатор поставщика
    private final String name;   // Название компании-поставщика
    private final Product product; // Товар, который поставляет поставщик

    /**
     * Конструктор поставщика
     * @param name название компании
     * @param product привязанный товар
     */
    public Producer(String name, Product product) {
        this.id = IdGenerator.generateId(IdGenerator.ObjectType.PRODUCER);
        this.name = name;
        this.product = product;
    }

    // Геттеры
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Product getProduct() {
        return product;
    }

    /**
     * Доставка товара на склад
     *
     * Логика работы:
     * 1. Увеличивает количество указанного товара на складе
     * 2. Использует merge для безопасного обновления значения в Map
     * 3. Выводит информацию о поставке
     *
     * @param storage целевой склад
     * @param requestedAmount количество поставляемого товара
     */
    public void deliverToStorage(Warehouse storage, int requestedAmount) {
        Map<Product, Integer> stock = storage.getStock();
        int currentAmount = stock.getOrDefault(product, 0);
        stock.put(product, currentAmount + requestedAmount);

        System.out.printf("Поставщик '%s' доставил %d ед. товара %s%n",
                name, requestedAmount, product.getName());
    }
}