import java.sql.SQLException;
import java.time.LocalDate;

/**
 * This class is a sample program that is in running condition.
 */
public class Main {

    private static void printHeader(String hearder){
        //108
        hearder = " " + hearder + " ";
        int lenHeader = hearder.length();
        int open = (int) Math.ceil((108 - lenHeader)/2.0);
        int close = 108-lenHeader-open;
        for (int i=0; i<open;i++){ System.out.print(">");}
        System.out.print(hearder);
        for (int i=0; i<close;i++){ System.out.print("<");}
        System.out.println();
    }

    public static void main(String[] args) {
        Store store = new Store();
        try {
            store.connect();
        }
        catch (SQLException sqle) {
            sqle.printStackTrace();
            System.out.println("CANNOT CONNECT TO DATABASE");
            System.exit(1);
        }

        printHeader("ALL PRODUCTS");
        for (Product p:store.loadAllProducts()){
            System.out.println(p);
        }

        printHeader("ALL ORDERS");
        for (Order o: store.loadAllOrders()){
            System.out.println(o);
        }

        printHeader("2019 - 2020 ORDERS");
        for (Order o: store.loadOrdersInPeriod(LocalDate.of(2019,01,01),
                LocalDate.of (2020,12,31)))
            System.out.println(o);

        printHeader("iPad Pro 12 Orders");
        for (Order o: store.loadOrdersForProduct("ipadpro12"))
            System.out.println(o);

        store.disconnect();
    }
}


/***************** SAMPLE OUTPUT *************
>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> ALL PRODUCTS <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
Product:[ipad6               |Apple iPad 6th gen            |    250.00]
Product:[ipad8               |Apple iPad 8th gen            |    350.00]
Product:[ipadair             |Apple new iPad Air 64G        |    500.00]
Product:[ipadpro11           |Apple iPad Pro 11             |    750.00]
Product:[ipadpro12           |Apple iPad Pro 12             |    950.00]
>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> ALL ORDERS <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
------------------------------------------------------------------------------------------------------------
Order:[A0001               |2018-01-01]
--->Line:{    2 x Product:[ipad6               |Apple iPad 6th gen            |    250.00] = $    500.00}
---> Order Total: $    500.00
============================================================================================================
------------------------------------------------------------------------------------------------------------
Order:[A0002               |2019-01-01]
--->Line:{    1 x Product:[ipad8               |Apple iPad 8th gen            |    350.00] = $    350.00}
---> Order Total: $    350.00
============================================================================================================
------------------------------------------------------------------------------------------------------------
Order:[A0003               |2020-01-01]
--->Line:{    1 x Product:[ipadair             |Apple new iPad Air 64G        |    500.00] = $    500.00}
--->Line:{    2 x Product:[ipadpro11           |Apple iPad Pro 11             |    750.00] = $   1500.00}
--->Line:{    4 x Product:[ipadpro12           |Apple iPad Pro 12             |    950.00] = $   3800.00}
---> Order Total: $   5800.00
============================================================================================================
>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> 2019 - 2020 ORDERS <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
------------------------------------------------------------------------------------------------------------
Order:[A0002               |2019-01-01]
--->Line:{    1 x Product:[ipad8               |Apple iPad 8th gen            |    350.00] = $    350.00}
---> Order Total: $    350.00
============================================================================================================
------------------------------------------------------------------------------------------------------------
Order:[A0003               |2020-01-01]
--->Line:{    1 x Product:[ipadair             |Apple new iPad Air 64G        |    500.00] = $    500.00}
--->Line:{    2 x Product:[ipadpro11           |Apple iPad Pro 11             |    750.00] = $   1500.00}
--->Line:{    4 x Product:[ipadpro12           |Apple iPad Pro 12             |    950.00] = $   3800.00}
---> Order Total: $   5800.00
============================================================================================================
>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> iPad Pro 12 Orders <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
------------------------------------------------------------------------------------------------------------
Order:[A0003               |2020-01-01]
--->Line:{    1 x Product:[ipadair             |Apple new iPad Air 64G        |    500.00] = $    500.00}
--->Line:{    2 x Product:[ipadpro11           |Apple iPad Pro 11             |    750.00] = $   1500.00}
--->Line:{    4 x Product:[ipadpro12           |Apple iPad Pro 12             |    950.00] = $   3800.00}
---> Order Total: $   5800.00
============================================================================================================

Process finished with exit code 0
*/