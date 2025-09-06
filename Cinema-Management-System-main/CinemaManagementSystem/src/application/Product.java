package application;
public class Product {
    private int id;
    private String name;
    private double price;
    private int stock;
    // private String type; // "beverage", "biscuit", "toy"

    public Product() {
        this.id = 0;
        this.name = null;
        this.price = 0;
        this.stock = 0;
        // this.type = null;
    }

    public Product(int id, String name, double price, int stock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    // public String getType() {
    //     return type;
    // }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    // public void setType(String type) {
    //     this.type = type;
    // }
}