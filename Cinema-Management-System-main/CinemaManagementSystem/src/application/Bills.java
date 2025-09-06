package application;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Bills {

    private int bill_id;
    private LocalDateTime time;
    private String customer;
    private LocalDate customerBirthDate;
    private int sessionId;
    private double total_amount;
    private double tax_amount;

    public Bills(){
        this.bill_id = 0;
        this.time = null;
        this.customer = null;
        this.customerBirthDate = null;
        this.sessionId = 0;
        this.total_amount = 0;
        this.tax_amount = 0;
    }

    public Bills(int bill_id, LocalDateTime time, String customer, LocalDate customerBirthDate, int sessionId,
            double total_amount, double tax_amount) {
        this.bill_id = bill_id;
        this.time = time;
        this.customer = customer;
        this.customerBirthDate = customerBirthDate;
        this.sessionId = sessionId;
        this.total_amount = total_amount;
        this.tax_amount = tax_amount;
    }

    public int getBill_id() {
        return bill_id;
    }

    public void setBill_id(int bill_id) {
        this.bill_id = bill_id;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public LocalDate getCustomerBirthDate() {
        return customerBirthDate;
    }

    public void setCustomerBirthDate(LocalDate customerBirthDate) {
        this.customerBirthDate = customerBirthDate;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public double getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(double total_amount) {
        this.total_amount = total_amount;
    }

    public double getTax_amount() {
        return tax_amount;
    }

    public void setTax_amount(double tax_amount) {
        this.tax_amount = tax_amount;
    }

    
}
