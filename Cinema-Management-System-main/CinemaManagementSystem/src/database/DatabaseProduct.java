package database;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;

import application.Product;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseProduct implements DatabaseSource {

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

    public ArrayList<Product> viewInventory(){
        
        ArrayList<Product> products = new ArrayList<Product>();
        String query = "SELECT * FROM products";

        try{
            ResultSet rs = executeQuery(query);

            while (rs.next()){ 

                Product product = new Product();

                product.setId(rs.getInt("idproducts"));
                product.setName(rs.getString("name"));
                product.setPrice(rs.getDouble("price"));
                product.setStock(rs.getInt("stock"));
                // product.setType(rs.getString("type"));
                products.add(product);
            }
            
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    
        return products;
    }

    // public ArrayList<String> getTypes(){

    //     ArrayList<String> types = new ArrayList<String>();
    //     String query = "SELECT DISTINCT type FROM products";

    //     try{

    //         ResultSet rs = executeQuery(query);

    //         while(rs != null && rs.next()){

    //             String str = rs.getString("type");

    //             types.add(str);
    //         }
    //     }

    //     catch(SQLException sqlException){
    //         sqlException.printStackTrace();
    //     }

    //     return types;
    // }

    public ArrayList<Product> getProducts(String type){

        ArrayList<Product> products = new ArrayList<Product>();
        String query = "SELECT * FROM products";

        try{

            ResultSet rs = executeQuery(query);

            while(rs != null && rs.next()){

                Product product = new Product();

                if(rs.getString("type").equalsIgnoreCase(type)){

                    product.setId(rs.getInt("idproducts"));
                    product.setName(rs.getString("name"));
                    product.setPrice(rs.getDouble("price"));
                    product.setStock(rs.getInt("stock"));
                    // product.setType(rs.getString("type"));
                    products.add(product);

                }
                
            }
        }

        catch(SQLException sqlException){
            sqlException.printStackTrace();
        }

        return products;
    }

    public void insertProducts(Product product){

        String query = "INSERT INTO products (name, price, stock) VALUES (?,?,?)";

        try(PreparedStatement pStatement = connection.prepareStatement(query)){
            
            pStatement.setString(1, product.getName());
            pStatement.setDouble(2,product.getPrice());
            pStatement.setInt(3,product.getStock());

            if (pStatement.executeUpdate() > 0)
                System.out.println("Product inserted successfully!");
            else
                System.out.println("Insert failed!");
        }
        catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
    }

    public void updateProduct(Product product, String column, int value){                 //!!!!!!!!!!!!!!!

        int ID = product.getId();
        String query = "UPDATE products SET " + column + " = ? WHERE idproducts = ? ";
        try(PreparedStatement pStatement = connection.prepareStatement(query);){

            pStatement.setInt(1, value);
            pStatement.setInt(2, ID);

            if (pStatement.executeUpdate() > 0)
                System.out.println("Product updated successfully!");
            else
                System.out.println("Update failed.");

        }
        catch(SQLException sqlException){
            sqlException.printStackTrace();
        }

    }

    public void updateStocks(Product product){

        String query = "UPDATE products SET stock = stock + ? WHERE name = ? ";

        try(PreparedStatement pStatement = connection.prepareStatement(query)){

            pStatement.setInt(1, product.getStock());
            pStatement.setString(2, product.getName());

            if (pStatement.executeUpdate() > 0)
                System.out.println("Product stock updated successfully!");
            else
                System.out.println("Update failed!");

        }
        catch(SQLException sqlException){
            sqlException.printStackTrace();
        }


    }

    public void deleteProduct(Product product){

        int ID = product.getId();
        String query = "DELETE FROM products WHERE idproducts = ?";

        try(PreparedStatement pStatement = connection.prepareStatement(query)){
            pStatement.setInt(1, ID);
            if (pStatement.executeUpdate() > 0)
                System.out.println("Product deleted successfully!");
            else
                System.out.println("Delete failed!");
        }
        catch(SQLException sqlException){
            sqlException.printStackTrace();
        }

    }

    public void editTicketPrice(){

    }

    public void returnProduct(String product, int quantity){

        String query = "UPDATE products SET stock = stock + ? WHERE name = ? ";

        try(PreparedStatement pStatement = connection.prepareStatement(query)){

            pStatement.setInt(1, quantity);
            pStatement.setString(2, product);

            if (pStatement.executeUpdate() > 0)
                System.out.println("Product stock updated successfully!");
            else
                System.out.println("Update failed!");

        }
        catch(SQLException sqlException){
            sqlException.printStackTrace();
        }
    }

    public double[] viewRevenue(){
        
        double[] revenue = new double[3];
        String query = "SELECT SUM(total_amount) AS totalAmount, SUM(tax_amount) AS totalTax FROM bills";

        try (PreparedStatement pStatement = connection.prepareStatement(query)) {

            ResultSet rs = executeQuery(query);

            if(rs.next()){
                double totalAmount = rs.getDouble("totalAmount");
                double totalTax = rs.getDouble("totalTax");
                double profit = totalAmount - totalTax;

                revenue[0] = totalAmount;
                revenue[1] = totalTax;
                revenue[2] = profit;
            }

        } catch(SQLException sqlException){
             sqlException.printStackTrace();
        }

        return revenue;
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