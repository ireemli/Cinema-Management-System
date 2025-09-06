package database;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;

import application.Halls;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseHalls implements DatabaseSource {

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

    public Halls getHall(int id){

        Halls hall = new Halls();
        String query = "SELECT * FROM halls WHERE idhalls = '" + id + "'";

        try{
            ResultSet rs = executeQuery(query);

            if (rs != null && rs.next()) {
                hall.setId_halls(rs.getInt("idhalls"));
                hall.setName(rs.getString("name"));
                hall.setCapacity(rs.getInt("capacity"));
            } else {
                System.out.println("No hall found with id: " + id);
            }

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }    

        return hall;
    }

    public ArrayList<Halls> viewHalls(){
        
        ArrayList<Halls> halls = new ArrayList<Halls>();
        String query = "SELECT * FROM halls";

        try{
            ResultSet rs = executeQuery(query);

            while (rs.next()){ 

                Halls hall = new Halls();

                hall.setId_halls(rs.getInt("idhalls"));
                hall.setName(rs.getString("name"));
                hall.setCapacity(rs.getInt("capacity"));
                halls.add(hall);
            }
            
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } 
    
        return halls;
    }

    public String getHallNames(int hallId){

        String hall = "";
        String query = "SELECT DISTINCT name FROM halls WHERE idhalls = ?";

        try(PreparedStatement prStatement = connection.prepareStatement(query)){

            prStatement.setInt(1, hallId);

            ResultSet rs = prStatement.executeQuery();

            while (rs.next()){ 

                hall = rs.getString("name");
            }
            
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } 
    
        return hall;
    }

    public ArrayList<String> getHalls(){

        ArrayList<String> halls = new ArrayList<>();
        String query = "SELECT DISTINCT name FROM halls";

        try{
            ResultSet rs = executeQuery(query);

            while (rs.next()){ 

                String hall = "";
                hall = rs.getString("name");
                halls.add(hall);
            }
            
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } 
    
        return halls;
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