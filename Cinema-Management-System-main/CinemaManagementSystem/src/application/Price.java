package application;

public class Price {
    
    private double price;
    private double taxPercentage;
    private double discountPercentage;

    public Price(){
        this.price = 0;
        this.taxPercentage = 0;
        this.discountPercentage = 0;
    }
    
    public Price(double price, int taxPercentage, int discountPercentage){
        this.price = price;
        this.taxPercentage = taxPercentage;
        this.discountPercentage = discountPercentage;
    }

    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public double getTaxPercentage() {
        return taxPercentage;
    }
    public void setTaxPercentage(double taxPercentage) {
        this.taxPercentage = taxPercentage;
    }
    public double getDiscountPercentage() {
        return discountPercentage;
    }
    public void setDiscountPercentage(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

}
