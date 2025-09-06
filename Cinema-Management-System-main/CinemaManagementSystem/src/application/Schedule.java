package application;

import java.time.LocalDate;

public class Schedule {
    private int id;
    private LocalDate date; 
    private String hall;
    private String session;
    private Movie movie;

    public Schedule(int id, LocalDate date, String hall, String session, Movie movie) {
        this.id = id;
        this.date = date;
        this.hall = hall;
        this.session = session;
        this.movie = movie;
    }

    // Getter ve Setter'lar
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getHall() {
        return hall;
    }

    public void setHall(String hall) {
        this.hall = hall;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }
}

