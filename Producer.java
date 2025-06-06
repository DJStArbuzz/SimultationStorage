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
    private int maxCount;

    /**
     * Конструктор поставщика
     * @param name название компании
     * @param product привязанный товар
     */
    public Producer(String name, Product product, int maxCount) {
        this.id = IdGenerator.generateId(IdGenerator.ObjectType.PRODUCER);
        this.name = name;
        this.product = product;
        this.maxCount = maxCount;
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

    public int getMaxCount() {
        return maxCount;
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
    public void deliverToStorage(Warehouse storage, Integer requestedAmount) {
        Map<Product, Integer> stock = storage.getStock();
        boolean flag = false;

        if (maxCount == 0){
            flag = true;
        }

        if (maxCount < requestedAmount){
            requestedAmount = maxCount;
        }

        if (!flag){
            int currentAmount = stock.getOrDefault(product, 0);
            stock.put(product, currentAmount + requestedAmount);

            System.out.printf("Поставщик '%s' доставил %d ед. товара %s%n",
                    name, requestedAmount, product.getName());
            maxCount -= requestedAmount;
        }
        else{
            System.out.printf("Поставщик '%s' ничего не доставил. Он впустую приехал.\n",
                    name);
        }
    }
}