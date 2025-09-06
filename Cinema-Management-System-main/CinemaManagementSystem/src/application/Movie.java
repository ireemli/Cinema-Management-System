
package application;public class Movie {
    private int id;
    private String title;
    private int year;
    private String genre;
    private String summary;

    //To store database as Binary Large Object
    private byte[] posterImage; //Binary format

    //private double price;

    public Movie(){
        this.id = 0;
        this.title = null;
        this.year = 0;
        this.genre = null;
        this.summary = null;
        this.posterImage = null;
        //this.price = price;
    }

    public Movie(int id, String title, int year, String genre,  String summary, byte[] posterImage) { //, double price
        this.id = id;
        this.title = title;
        this.year = year;
        this.genre = genre;
        this.summary = summary;
        this.posterImage = posterImage;
        //this.price = price;
    }

    // Getter ve Setter metodları

    //ID
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    //Title
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }


    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    //Summary
    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }

    //Poster
    public byte[] getPosterImage() { return posterImage; }
    public void setPosterImage(byte[] posterImage) { this.posterImage = posterImage; }

}
