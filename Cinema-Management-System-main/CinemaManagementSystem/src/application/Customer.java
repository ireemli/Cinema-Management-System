package application;
import java.time.LocalDate;
import java.time.Period;

// Customer.java
public class Customer {
    private int id;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;

    public Customer(int id, String firstName, String lastName, LocalDate birthDate) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
    }

    // Getter ve Setter metodları
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }

    // Yaş hesaplama metodu
    public int getAge() {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    // İndirim hesaplama metodu
    public boolean isEligibleForDiscount() {
        int age = getAge();
        return age < 18 || age > 60;
    }
}
