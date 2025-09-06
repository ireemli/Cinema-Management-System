package application;
import java.time.LocalDateTime;
import java.sql.Date;

// Session.java
public class Session {
    private int id;
    private Movie movie;
    private LocalDateTime dateTime;
    private Halls hall;
    private int availableSeats;

    public Session(){
        this.id = 0;
        this.movie = null;
        this.dateTime = null;
        this.hall = null;
        this.availableSeats = 0;
    }

    public Session(int id, Movie movie, LocalDateTime dateTime, Halls hall, int availableSeats) {
        this.id = id;
        this.movie = movie;
        this.dateTime = dateTime;
        this.hall = hall;
        this.availableSeats = availableSeats;
    }

    public String toString() {
        return "Session{id=" + id + ", availableSeats=" + availableSeats + ", dateTime=" + dateTime + "}";
    }

    // Getter ve Setter metodları
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Movie getMovie() { return movie; }
    public void setMovie(Movie movie) { this.movie = movie; }

    public LocalDateTime getDateTime() { return dateTime; }
    public void setDateTime(LocalDateTime dateTime) { this.dateTime = dateTime; }

    public Halls getHall() { return hall; }
    public void setHall(Halls hall) { this.hall = hall; }

    public int getAvailableSeats() { return availableSeats; }
    public void setAvailableSeats(int availableSeats) { this.availableSeats = availableSeats; }
}
