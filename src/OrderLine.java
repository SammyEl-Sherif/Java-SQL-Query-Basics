import java.sql.*;
import java.util.ArrayList;

public class OrderLine {
    /**
     * Attributes: a Product, and a quantity (int)
     */

    private Product product;
    private int quantity;

    /**
     * Initializes the object with the product and quantity
     * @param product
     * @param quantity
     */
    public OrderLine(Product product, int quantity){
        this.product = product;
        this.quantity = quantity;
    }

    /**
     * Getters and Setters
     */
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * @return the subtotal of the line (item price x qty)
     */
    public double subTotal(){
        return this.getProduct().getPrice() * this.getQuantity();
    }

    /**
     * @return the string representation of the order line.
     */
    public String toString(){
        return String.format("Line:{%5d x %s = $%10.2f}",this.getQuantity(),this.getProduct(), this.subTotal());
    }

    /**
     * Loads all the OrderLine objects for a specific Order
     * @param db the database connection
     * @param order the order to search the lines for. (can be shallowed loaded)
     * @return an arraylist of order lines.
     * HINT:
     *     Don't deep load the product, as it will be replaced at the Store object.
     */
    public static ArrayList<OrderLine> loadLinesForOrder(Connection db, Order order){
        ArrayList<OrderLine> linesForOrder = new ArrayList<>();
        try {
            // Prepare SQL statement to retrieve the object's data
            PreparedStatement productQuery = db.prepareStatement("SELECT order_number, product_code, quantity FROM store.order_line WHERE order_number = ?");
            productQuery.setString(1, order.getOrderNumber()); // search for order given

            ResultSet rows = productQuery.executeQuery(); // Execute the SQL Statement

            // We have found the order we need, get all the order_line rows with order_number key
            // iterate through those and then get the product and quantity of each of them to make an order line
            while (rows.next()){ // each row is an purchase_order
                rows.getString("order_number");
                OrderLine anOrderLine = new OrderLine(new Product(rows.getString("product_code")), rows.getInt("quantity"));
                linesForOrder.add(anOrderLine);
            }

            rows.close();
            productQuery.close();
        }
        catch (SQLException throwables){
            throwables.printStackTrace();
        }
        return linesForOrder;
    }

}
