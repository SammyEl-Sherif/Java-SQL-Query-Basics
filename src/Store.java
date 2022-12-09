import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Scanner;

public class Store {
    /**
     * Attributes: the list of Products, the list of Orders, and the db Connection (null by default)
     */
    private ArrayList<Product> products;
    private ArrayList<Order> orders;
    private Connection dbConnection;

    /**
     * Initializes the lists.
     * Will NOT connect the database here.,
     */
    public Store(){
        this.products = new ArrayList<>();
        this.orders = new ArrayList<>();
    }

    /**
     * Connects the store to the database and stores the connection in the instance attribute.
     * Reads from the file "db.cfg" (see working sample) the database connection settings.
     * HINT:
     *         Use a Map to store the db config.
     */
    public void connect() throws SQLException {
        HashMap<String,String> config = new HashMap<>();
        try {
            Scanner input = new Scanner(Paths.get("db.cfg"));
            while (input.hasNext()){
                String line = input.nextLine();
                String[] params =line.split(":");
                config.put(params[0].trim(),params[1].trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String connStr = "jdbc:mysql://"+config.get("HOSTNAME")+":"+config.get("PORT")+"/"+config.get("DATABASE");
        this.dbConnection = DriverManager.getConnection(connStr, config.get("USERNAME"), config.get("PASSWORD"));

    }

    /**
     * Disconnects the store from the database.
     * If the store is not connected, don't do anything.
     * In case an error, ignore and make sure no connection object is stored.
     */
    public void disconnect(){
        if (this.dbConnection != null) {
            try {
                this.dbConnection.close();
            } catch (SQLException ignored) {}
            finally {
                this.dbConnection = null;
            }
        }
    }

    /**
     * Searches in the product list for a product with the code in the parameter object.
     * If a product with that code is found in the list, will return that product object (from the list).
     * If not, will load the product object argument, add it to the product list, and return the loaded object.
     * @param product product to search
     * @return the product matching the code, loaded and stored in the store product list.
     */
    private Product searchAndLoad(Product product){
        for (Product p : this.products){
            if (Objects.equals(p.getCode(), product.getCode())){
                return p;
            }
        }
        product.load(this.dbConnection);
        this.products.add(product);
        return product;
    }

    /**
     * This method will load objects into the store instance attribute.
     * @return the loaded product list (instance attribute)
     * HINT:
     *      delegate to Product class.
     */
    public ArrayList<Product> loadAllProducts(){
        this.products = Product.loadAll(this.dbConnection);
        return this.products;
    }

    /**
     * This method will load objects into the store instance attribute.
     * @return the loaded list of orders (instance attribute)
     * HINT:
     *      Once the orders are loaded, make sure that the product objects associated with those are loaded into
     *      the store product list (inst. attr).
     *      Use the searchAndLoad method above. REUSE!
     */
    public ArrayList<Order> loadAllOrders() {
        this.orders = Order.loadAll(this.dbConnection);
        for (Order o : this.orders){
            for (OrderLine line: o.getLines()){
                line.setProduct(this.searchAndLoad(line.getProduct()));
            }
        }
        return this.orders;
    }

    /**
     * Loads the orders with order date between a period.
     * This method will load objects into the store instance attribute.
     * @param start period start (include)
     * @param end period end (include)
     * @return the loaded list of orders (instance attribute)
     * HINT:
     *      Once the orders are loaded, make sure that the product objects associated with those are loaded into
     *      the store product list (inst. attr).
     *      Use the searchAndLoad method above. REUSE!
     */
    public ArrayList<Order> loadOrdersInPeriod(LocalDate start, LocalDate end) {
        this.orders = Order.loadInPeriod(this.dbConnection, start, end);
        for (Order o : this.orders){
            for (OrderLine line: o.getLines()){
                line.setProduct(this.searchAndLoad(line.getProduct()));
            }
        }
        return this.orders;
    }
    /**
     * Loads the orders that contain a specific product.
     * This method will load objects into the store instance attribute.
     * @param productCode the code of the product to search for.
     * @return the loaded list of orders (instance attribute)
     * HINT:
     *      Once the orders are loaded, make sure that the product objects associated with those are loaded into
     *      the store product list (inst. attr).
     *      Use the searchAndLoad method above. REUSE!
     */
    public ArrayList<Order> loadOrdersForProduct(String productCode) {
        Product product = this.searchAndLoad(new Product(productCode));

        this.orders = Order.loadOrdersWithProduct(this.dbConnection, product);
        for (Order o : this.orders){
            for (OrderLine line: o.getLines()){
                line.setProduct(this.searchAndLoad(line.getProduct()));
            }
        }
        return this.orders;
    }
}
