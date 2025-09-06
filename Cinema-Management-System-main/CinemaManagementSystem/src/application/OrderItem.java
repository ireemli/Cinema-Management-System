package application;
public class OrderItem {
    private int id;
    private String name;
    private int quantity;
    private double price;
    private double discount;
    private double total;
    private String type; // "ticket" veya "product"

    public OrderItem() {
        this.id = 0;
        this.name = null;
        this.quantity = 0;
        this.price = 0;
        this.discount = 0;
        this.type = null;
        this.total = 0;
    }

    public OrderItem(int id, String name, int quantity, double price, double discount, String type) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.discount = discount;
        this.type = type;
        calculateTotal();
    }

    private void calculateTotal() {
        double subtotal = price * quantity;
        this.total = subtotal - (subtotal * discount / 100);
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public double getDiscount() {
        return discount;
    }

    public double getTotal() {
        return total;
    }

    public String getType() {
        return type;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        calculateTotal();
    }

    public void setPrice(double price) {
        this.price = price;
        calculateTotal();
    }

    public void setDiscount(double discount) {
        this.discount = discount;
        calculateTotal();
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return String.format("%s - Quantity: %d, Price: %.2f TL, Discount: %.0f%%, Total: %.2f TL",
                name, quantity, price, discount, total);
    }
}