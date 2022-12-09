import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class Order {
    /**
     * Attributes: LocalDate date, String orderNumber, arraylist of orderlines called lines
     */

    private LocalDate date;
    private String orderNumber;
    ArrayList<OrderLine> lines = new ArrayList<>();

    /**
     * Initialized the order with only the orderNumber.
     * @param orderNumber
     */
    public Order(String orderNumber){
        this.orderNumber = orderNumber;
    }

    /**
     * Initializes the Order with the order number and date
     * @param orderNumber
     * @param date
     */
    public Order(String orderNumber, LocalDate date){
        this.orderNumber = orderNumber;
        this.date = date;
    }

    /**
     * Getters and Setters
     */

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public ArrayList<OrderLine> getLines(){
        return this.lines;
    }

    /**
     * @return the total amount of the order by using the line subtotal.
     */
    public double total(){
        double subTotal = 0;
        for (OrderLine line : lines){
            subTotal += line.subTotal();
        }
        return subTotal;
    }

    /**
     * @return a printout of the order.
     */
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("------------------------------------------------------------------------------------------------------------\n");
        sb.append(String.format("Order:[%-20s|%-10s]\n",this.getOrderNumber(),this.getDate()));
        for (OrderLine line: this.getLines()){
            sb.append(String.format("--->%-50s\n",line.toString()));
        }
        sb.append(String.format("---> Order Total: $%10.2f\n", this.total()));
        sb.append("============================================================================================================");
        return sb.toString();
    }

    /**
     * Loads the current object from the database.
     * @param db the database connection
     * Hint:
     *           once loaded the object's data, use the static method from OrderLine to load the lines of the order.
     */
    public void load(Connection db){
        try {
            // Prepare SQL statement to retrieve the object's data
            PreparedStatement productQuery = db.prepareStatement("SELECT order_number, order_date FROM store.purchase_order WHERE order_number = ?");
            productQuery.setString(1, this.orderNumber);
            ResultSet rows = productQuery.executeQuery(); // Execute the SQL Statement

            while (rows.next()){
                this.orderNumber = rows.getString("order_number");
                this.date = rows.getDate("order_date").toLocalDate();
            }
            this.lines = OrderLine.loadLinesForOrder(db, this);

            rows.close();
            productQuery.close();
        }
        catch (SQLException throwables){
            throwables.printStackTrace();
        }
    }

    /**
     * Loads all the orders in the DB
     * @param db the database connection
     * @return an arraylist of orders.
     * HINT:
     *  Use the order Load method.
     */
    public static ArrayList<Order> loadAll(Connection db){
        ArrayList<Order> orders = new ArrayList<>();
        try {
            // Prepare SQL statement to retrieve the object's data
            PreparedStatement productQuery = db.prepareStatement("SELECT order_number, order_date FROM store.purchase_order");
            ResultSet rows = productQuery.executeQuery(); // Execute the SQL Statement

            // Get a ResultSet of all order
            // for order in the orders
                // create an order object with the current order
            while (rows.next()){
                Order anOrder = new Order(rows.getString("order_number")); // , rows.getDate("order_Date").toLocalDate()
                anOrder.load(db);
                orders.add(anOrder);
            }

            rows.close();
            productQuery.close();
            return orders;
        }
        catch (SQLException throwables){
            throwables.printStackTrace();
        }
        return orders;
    }

    /**
     * Loads all the orders in the DB with the order date between a period of time.
     * @param db the database connection
     * @param start the period start (should be included)
     * @param end the period end (should be included)
     * @return an arraylist of orders
     * HINT:
     *  Use the order Load method.
     */
    public static ArrayList<Order> loadInPeriod(Connection db, LocalDate start, LocalDate end){
        ArrayList<Order> ordersInPeriod = new ArrayList<>();
        try {
            // Prepare SQL statement to retrieve the object's data
            PreparedStatement productQuery = db.prepareStatement("SELECT order_number, order_date FROM store.purchase_order WHERE order_date BETWEEN ? and ?");
            productQuery.setDate(1, Date.valueOf(start));
            productQuery.setDate(2, Date.valueOf(end));

            ResultSet rows = productQuery.executeQuery(); // Execute the SQL Statement

            while (rows.next()){
                Order anOrder = new Order(rows.getString("order_number"), rows.getDate("order_date").toLocalDate());
                anOrder.load(db);
                ordersInPeriod.add(anOrder);
            }

            rows.close();
            productQuery.close();
        }
        catch (SQLException throwables){
            throwables.printStackTrace();
        }
        return ordersInPeriod;
    }

    /**
     * Loads all the orders in the DB that contain a specific product
     * @param db the database connection
     * @param product the product. Can be shallow loaded.
     * @return an arraylist of orders.
     * HINT:
     *  Use the order Load method.
     */
    public static ArrayList<Order> loadOrdersWithProduct(Connection db, Product product){
        ArrayList<Order> ordersInPeriod = new ArrayList<>();
        try {
            // Prepare SQL statement to retrieve the object's data
            PreparedStatement productQuery = db.prepareStatement("SELECT PO.order_number FROM store.purchase_order as PO, store.order_line L WHERE PO.order_number = L.order_number and L.product_code = ?");
            productQuery.setString(1, product.getCode());

            ResultSet rows = productQuery.executeQuery(); // Execute the SQL Statement

            while (rows.next()){
                Order anOrder = new Order(rows.getString("order_number"));
                anOrder.load(db);
                ordersInPeriod.add(anOrder);
            }

            rows.close();
            productQuery.close();
        }
        catch (SQLException throwables){
            throwables.printStackTrace();
        }
        return ordersInPeriod;
    }


}
