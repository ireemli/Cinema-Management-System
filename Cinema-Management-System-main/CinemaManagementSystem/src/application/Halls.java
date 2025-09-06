package application;

public class Halls {
    
    private int id_halls;
    private String name;
    private int capacity;

    public Halls() {
        this.id_halls = 0;
        this.name = null;
        this.capacity = 0;
    }
    
    public Halls(int id_halls, String name, int capacity) {
        this.id_halls = id_halls;
        this.name = name;
        this.capacity = capacity;
    }


    public int getId_halls() {
        return id_halls;
    }
    public void setId_halls(int id_halls) {
        this.id_halls = id_halls;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getCapacity() {
        return capacity;
    }
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
    
}
