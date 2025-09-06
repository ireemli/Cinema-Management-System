package database;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;

import application.Halls;
import application.Movie;
import application.Price;
import application.Seat;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabasePrice implements DatabaseSource{
    
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

    public Price getPrices(){

        Price price = new Price();
        String query = "SELECT * FROM ticketprices";

        try{
            ResultSet rs = executeQuery(query);

            while (rs.next()){ 

                price.setPrice(rs.getDouble("price"));
                price.setTaxPercentage(rs.getInt("tax"));
                price.setDiscountPercentage(rs.getInt("discount"));

            }
            
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } 
    
        return price;
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
