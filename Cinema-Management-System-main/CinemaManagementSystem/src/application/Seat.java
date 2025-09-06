package application;
public class Seat {
    private int id;
    private String seat;
    private int session;
    private boolean isOccupied;

    public Seat() {
        this.id = 0;
        this.seat = null;
        this.session = 0;
        this.isOccupied = false;
    }

    public Seat(int id, String seat, int session, boolean isOccupied) {
        this.id = id;
        this.seat = seat;
        this.session = session;
        this.isOccupied = isOccupied;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSeat() {
        return seat;
    }

    public void setSeat(String seat) {
        this.seat = seat;
    }

    public int getSession() {
        return session;
    }

    public void setSession(int session) {
        this.session = session;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public void setOccupied(boolean isOccupied) {
        this.isOccupied = isOccupied;
    }

}