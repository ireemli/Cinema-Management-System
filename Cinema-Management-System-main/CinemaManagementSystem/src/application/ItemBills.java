package application;

public class ItemBills {
    
    private int item_id;
    private int bill_id;
    private String item_type;
    private String item_name;
    private int item_quantity;
    private double item_amount;
    private double item_tax;

    public ItemBills() {
        this.item_id = 0;
        this.bill_id = 0;
        this.item_type = null;
        this.item_name = null;
        this.item_quantity = 0;
        this.item_amount = 0;
        this.item_tax = 0;
    }

    public ItemBills(int item_id, int bill_id, String item_type, String item_name, int item_quantity,
            double item_amount, double item_tax) {
        this.item_id = item_id;
        this.bill_id = bill_id;
        this.item_type = item_type;
        this.item_name = item_name;
        this.item_quantity = item_quantity;
        this.item_amount = item_amount;
        this.item_tax = item_tax;
    }

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public int getBill_id() {
        return bill_id;
    }

    public void setBill_id(int bill_id) {
        this.bill_id = bill_id;
    }

    public String getItem_type() {
        return item_type;
    }

    public void setItem_type(String item_type) {
        this.item_type = item_type;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public int getItem_quantity() {
        return item_quantity;
    }

    public void setItem_quantity(int item_quantity) {
        this.item_quantity = item_quantity;
    }

    public double getItem_amount() {
        return item_amount;
    }

    public void setItem_amount(double item_amount) {
        this.item_amount = item_amount;
    }

    public double getItem_tax() {
        return item_tax;
    }

    public void setItem_tax(double item_tax) {
        this.item_tax = item_tax;
    }

}