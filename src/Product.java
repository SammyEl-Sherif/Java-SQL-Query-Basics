import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Product {
    /**
     * Attributes code and description (both strings) and the product price (double)
     */
    private String code;
    private String description;
    private double price;

    /**
     * Initializes the object with only the code (for later load)
     * @param code
     */
    public Product(String code){
        this.code = code;
    }

    /**
     * Initializes the objects with all code, description and price.
     * Price should be a non-negative number.
     * @param code
     * @param description
     * @param price
     */
    public Product(String code, String description, double price){
        this.code = code;
        this.description = description;
        if (price < 0){
            throw new NumberFormatException();
        }
        else{
            this.price = price;
        }
    }

    /**
     * Getters and Setters.
     * price must be non-negative. IllegalArgumentException if the price is negative.
     */
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        if (price < 0){
            throw new NumberFormatException();
        }
        else{
            this.price = price;
        }
    }

    /**
     * @return Product price representation.
     */
    public String toString(){
        return String.format("Product:[%-20s|%-30s|%10.2f]",this.getCode(),this.getDescription(),this.getPrice());
    }

    /**
     * Loads the current product object using the code.
     * @param db the database connection
     */
    public void load(Connection db){ // load a single product
        try {
            Connection dbConnection = db;
            // Prepare SQL statement to retrieve the object's data
            PreparedStatement productQuery = dbConnection.prepareStatement("SELECT code, description, price FROM store.product WHERE code = ?");
            productQuery.setString(1, this.code);
            ResultSet rows = productQuery.executeQuery(); // Execute the SQL Statement

            while (rows.next()){
                this.code = rows.getString("code");
                this.description = rows.getString("description");
                this.price = rows.getDouble("price");
            }

            rows.close();
            productQuery.close();
        }
        catch (SQLException throwables){
            throwables.printStackTrace();
        }
    }

    /**
     * Loads all the products in the DB
     * @param db the database connection
     * @return an arraylist of products
     * HINT:
     *      don't use the object load.
     */
    public static ArrayList<Product> loadAll(Connection db){
        ArrayList<Product> products = new ArrayList<>();

        try {
            // Prepare SQL statement to retrieve the object's data
            PreparedStatement productQuery = db.prepareStatement("SELECT code, description, price FROM store.product");

            ResultSet rows = productQuery.executeQuery(); // Execute the SQL Statement

            while (rows.next()){
                Product aProduct = new Product(rows.getString("code"));
                aProduct.load(db);
                products.add(aProduct);
            }

            rows.close();
            productQuery.close();
        }
        catch (SQLException throwables){
            throwables.printStackTrace();
        }
        return products;
    }


}
