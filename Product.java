/**
 * Класс Товар (Product)
 *
 * Представляет товарную единицу в системе. Содержит информацию о товаре.
 */
public class Product {
    private final String name;  // Название товара
    private final double price; // Цена товара

    /**
     * Конструктор товара
     * @param name название товара
     * @param price цена за единицу
     */
    public Product(String name, double price) {
        this.name = name;
        this.price = price;
    }

    // Геттеры
    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }
}