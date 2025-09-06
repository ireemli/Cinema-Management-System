package database;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;

import application.Movie;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseMovies implements DatabaseSource{
    
    private Connection connection; 

    //Database'e bağlan
    public void connectDatabase(){

        try{

            connection = DriverManager.getConnection(url, username, password);

        }
        catch(SQLException sqlException){
            sqlException.printStackTrace();
        }

    }

    //Database query hazırlama
    public ResultSet executeQuery(String query){

        try{
            Statement statement = connection.createStatement();
            return statement.executeQuery(query);
        }
        catch(SQLException sqlException){
            sqlException.printStackTrace();
            return null;
        }
    }

    //Database query execute
    public int executeUpdate(String query){

        try(Statement statement = connection.createStatement()){

            return statement.executeUpdate(query);
        }
        catch(SQLException sqlException){
            sqlException.printStackTrace();
            return -1;
        }
    }

    public ArrayList<Movie> getMovies() {
        ArrayList<Movie> movies = new ArrayList<Movie>();
        String query = "SELECT * FROM movies";

        try {
            ResultSet rs = executeQuery(query);

            while(rs != null && rs.next()) {
                Movie movie = new Movie();

                movie.setId(rs.getInt("idMovie"));
                movie.setTitle(rs.getString("title"));
                movie.setYear(rs.getInt("year"));
                movie.setGenre(rs.getString("genre"));
                movie.setSummary(rs.getString("summary"));

                byte[] posterData = rs.getBytes("poster_images");
                System.out.println("Loading poster for " + movie.getTitle() +
                        ": " + (posterData != null ? posterData.length + " bytes" : "null")); // Debug satırı

                movie.setPosterImage(posterData);
                movies.add(movie);
            }
        } catch(SQLException sqlException) {
            sqlException.printStackTrace();
        }

        return movies;
    }


    public ArrayList<Movie> getMovieGenre(String genre){

        ArrayList<Movie> movies = new ArrayList<Movie>();
        String query = "SELECT * FROM movies WHERE genre LIKE ?";
        Movie movie;

        try(PreparedStatement prStatement = connection.prepareStatement(query)){

            prStatement.setString(1, "%" + genre + "%");

            ResultSet rs = prStatement.executeQuery();

            while(rs != null && rs.next()){

                movie = new Movie();
                movie.setId(rs.getInt("idMovie"));
                movie.setTitle(rs.getString("title"));
                movie.setYear(rs.getInt("year"));
                movie.setGenre(rs.getString("genre"));
                movie.setSummary(rs.getString("summary"));
                movie.setPosterImage(rs.getBytes("poster_images"));
                movies.add(movie);

            }
        }

        catch(SQLException sqlException){
            sqlException.printStackTrace();
        }

        return movies;
    }

    public ArrayList<Movie> getMoviePartial(String str){

        ArrayList<Movie> movies = new ArrayList<Movie>();
        String query = "SELECT * FROM movies WHERE title LIKE '%" + str + "%'";

        try{

            ResultSet rs = executeQuery(query);

            while(rs != null && rs.next()){

                Movie movie = new Movie();

                movie.setId(rs.getInt("idMovie"));
                movie.setTitle(rs.getString("title"));
                movie.setYear(rs.getInt("year"));
                movie.setGenre(rs.getString("genre"));
                movie.setSummary(rs.getString("summary"));
                movie.setPosterImage(rs.getBytes("poster_images"));
                movies.add(movie);

            }
        }
        catch(SQLException sqlException){
            sqlException.printStackTrace();
        }

        return movies;
    }

    public ArrayList<Movie> getMovieTitle(String str){

        ArrayList<Movie> movies = new ArrayList<Movie>();
        String query = "SELECT * FROM movies WHERE title = '" + str + "'";

        try{

            ResultSet rs = executeQuery(query);

            while(rs != null && rs.next()){

                Movie movie = new Movie();

                movie.setId(rs.getInt("idMovie"));
                movie.setTitle(rs.getString("title"));
                movie.setYear(rs.getInt("year"));
                movie.setGenre(rs.getString("genre"));
                movie.setSummary(rs.getString("summary"));
                movie.setPosterImage(rs.getBytes("poster_images"));
                movies.add(movie);

            }
        }
        catch(SQLException sqlException){
            sqlException.printStackTrace();
        }

        return movies;
    }

    public Movie get1Movie(int id){

        Movie movie = new Movie();
        String query = "SELECT * FROM movies WHERE idMovie = '" + id + "'";

        try{

            ResultSet rs = executeQuery(query);

            if (rs != null && rs.next()) {
                movie.setId(rs.getInt("idMovie"));
                movie.setTitle(rs.getString("title"));
                movie.setYear(rs.getInt("year"));
                movie.setGenre(rs.getString("genre"));
                movie.setSummary(rs.getString("summary"));
                movie.setPosterImage(rs.getBytes("poster_images"));
            }
            else{
                System.out.println("No hall found with id: " + id);
            }

                
        }
        catch(SQLException sqlException){
            sqlException.printStackTrace();
        }

        return movie;
    }

    public void insertMovie(Movie movie){
        String query = "INSERT INTO movies (title, year, genre, summary, poster_images) VALUES (?,?,?,?,?)";

        try(PreparedStatement pStatement = connection.prepareStatement(query)){
            pStatement.setString(1, movie.getTitle());
            pStatement.setInt(2,movie.getYear());
            pStatement.setString(3,movie.getGenre());
            pStatement.setString(4,movie.getSummary());

            // Poster verisi için BLOB kullan
            byte[] posterData = movie.getPosterImage();
            if (posterData != null) {
                System.out.println("Saving image, size: " + posterData.length + " bytes");
                pStatement.setBlob(5, new ByteArrayInputStream(posterData), posterData.length);
            } else {
                pStatement.setNull(5, java.sql.Types.BLOB);
            }

            int result = pStatement.executeUpdate();
            System.out.println("Insert result: " + (result > 0 ? "Success" : "Failed"));
        }
        catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
    }

    public void updateMovie(Movie movie, String column, String value){

        int ID = movie.getId();
        String query = "UPDATE employee SET " + column + " = ? WHERE idMovie = ? ";
        try(PreparedStatement pStatement = connection.prepareStatement(query);){

            pStatement.setString(1, value);
            pStatement.setInt(2, ID);

            if (pStatement.executeUpdate() > 0)
                System.out.println("Movie updated successfully!");
            else
                System.out.println("Update failed.");

        }
        catch(SQLException sqlException){
            sqlException.printStackTrace();
        }

    }

    public void updateMovie(Movie movie){

        String query = "UPDATE movies SET title = ? , year = ? , genre = ? , summary = ? , poster_url = ? WHERE idMovie = ? ";
        try(PreparedStatement pStatement = connection.prepareStatement(query);){

            pStatement.setString(1, movie.getTitle());
            pStatement.setInt(2, movie.getYear());
            pStatement.setString(3, movie.getGenre());
            pStatement.setString(4, movie.getSummary());

            byte[] posterData = movie.getPosterImage();
            if (posterData != null) {
                System.out.println("Saving image, size: " + posterData.length + " bytes");
                pStatement.setBlob(5, new ByteArrayInputStream(posterData), posterData.length);
            } else {
                pStatement.setNull(5, java.sql.Types.BLOB);
            }            
            pStatement.setInt(6, movie.getId());

            if (pStatement.executeUpdate() > 0)
                System.out.println("Movie updated successfully!");
            else
                System.out.println("Update failed.");

        }
        catch(SQLException sqlException){
            sqlException.printStackTrace();
        }

    }

    public void deleteMovie(Movie movie){

        int ID = movie.getId();
        String query = "DELETE FROM movies WHERE idMovie = ?";
        try(PreparedStatement pStatement = connection.prepareStatement(query)){
            pStatement.setInt(1, ID);
            if (pStatement.executeUpdate() > 0)
                System.out.println("Movie deleted successfully!");
            else
                System.out.println("Delete failed!");
        }
        catch(SQLException sqlException){
            sqlException.printStackTrace();
        }

    }

    public void loadImageToDatabase(int movieId, String imageName) {
        String imagePath = "src/images/" + imageName; // örn: "shawshank.png"

        try {
            // Resim dosyasını oku
            File imageFile = new File(imagePath);
            if (!imageFile.exists()) {
                System.out.println("HATA: " + imagePath + " bulunamadı!");
                return;
            }

            FileInputStream fis = new FileInputStream(imageFile);
            byte[] imageData = new byte[(int) imageFile.length()];
            fis.read(imageData);
            fis.close();

            // Veritabanına kaydet
            String query = "UPDATE movies SET poster_images = ? WHERE idMovie = ?";

            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setBytes(1, imageData);
                pstmt.setInt(2, movieId);

                int result = pstmt.executeUpdate();
                if (result > 0) {
                    System.out.println(movieId + " ID'li filme resim yüklendi. Boyut: " + imageData.length + " bytes");
                } else {
                    System.out.println("Resim yüklenemedi! Film ID: " + movieId);
                }
            }
        } catch (IOException | SQLException e) {
            System.out.println("HATA: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Tüm resimleri yüklemek için yardımcı metod
    public void loadAllMovieImages() {
        connectDatabase();

        // // Her film için resim yükle
        // loadImageToDatabase(1, "shawshank.png");
        // loadImageToDatabase(2, "godfather.png");
        // loadImageToDatabase(3, "dark_knight.png");
        // loadImageToDatabase(4, "pulp.png");
        // loadImageToDatabase(5, "forrest_gump.png");
        // loadImageToDatabase(6, "inception.png");
        // loadImageToDatabase(7, "fight_club.png");
        // loadImageToDatabase(8, "matrix.png");


        // ... diğer filmler için de ekleyin

        disconnectDatabase();
    }

    public void disconnectDatabase(){

        try{
            if(connection!=null && !connection.isClosed())
                connection.close();
            else
                System.out.println("Disconnection error!");
        }
        catch(SQLException sqlException){
        }
    }
}