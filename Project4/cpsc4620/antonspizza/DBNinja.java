package cpsc4620.antonspizza;

import java.io.*;
import java.sql.*;
import java.util.*;

/*
This file is where most of your code changes will occur
You will write the code to retrieve information from the database, or save information to the database

The class has several hard coded static variables used for the connection, you will need to change those to your connection information

This class also has static string variables for pickup, delivery and dine-in. If your database stores the strings differently (i.e "pick-up" vs "pickup") changing these static variables will ensure that the comparison is checking for the right string in other places in the program. You will also need to use these strings if you store this as boolean fields or an integer.


*/

/**
 * A utility class to help add and retrieve information from the database
 */

public final class DBNinja {
    //enter your user name here
    private static String user = "AntonPizza_frjz";
    //enter your password here
    private static String password = "4620estout";
    //enter your database name here
    private static String database_name = "AntonPizza_mffr";
    //Do not change the port. 3306 is the default MySQL port
    private static String port = "3306";
    private static Connection conn;

    //Change these variables to however you record dine-in, pick-up and delivery, and sizes and crusts
    public final static String pickup = "pickup";
    public final static String delivery = "delivery";
    public final static String dine_in = "dinein";

    public final static String size_s = "small";
    public final static String size_m = "medium";
    public final static String size_l = "Large";
    public final static String size_xl = "X-Large";

    public final static String crust_thin = "Thin";
    public final static String crust_orig = "Original";
    public final static String crust_pan = "Pan";
    public final static String crust_gf = "Gluten-Free";



    /**
     * This function will handle the connection to the database
     * @return true if the connection was successfully made
     * @throws SQLException
     * @throws IOException
     */
    private static boolean connect_to_db() throws SQLException, IOException
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println ("Could not load the driver");

            System.out.println("Message     : " + e.getMessage());


            return false;
        }

        conn = DriverManager.getConnection("jdbc:mysql://mysql1.cs.clemson.edu:"+port+"/"+database_name, user, password);
        return true;
    }

    private static int getOID() throws SQLException, IOException{

        Statement st = conn.createStatement();
        int r = -1;

        try{
            ResultSet result = st.executeQuery("SELECT MAX(id) from orders;");

            if(result.next()){
                r= result.getInt(1)+1;
            }
            if(result.wasNull()){
                r=0;
            }
        } catch(SQLException exp){
            System.out.println(exp.getMessage());
        }finally{
            return r;
        }
        
    }

    private static int getPID() throws SQLException, IOException{

        Statement st = conn.createStatement();
        int r = -1;

        try{
            ResultSet result = st.executeQuery("SELECT MAX(id) from pizza;");

            if(result.next()){
                r= result.getInt(1)+1;
            }
            if(result.wasNull()){
                r=0;
            }
        } catch(SQLException exp){
            System.out.println(exp.getMessage());
        }finally{
            return r;
        }
        
    }

    /**
     *
     * @param o order that needs to be saved to the database
     * @throws SQLException
     * @throws IOException
     * @requires o is not NULL. o's ID is -1, as it has not been assigned yet. The pizzas do not exist in the database
     *          yet, and the topping inventory will allow for these pizzas to be made
     * @ensures o will be assigned an id and added to the database, along with all of it's pizzas. Inventory levels
     *          will be updated appropriately
     */
    public static void addOrder(Order o) throws SQLException, IOException
    {
        connect_to_db();    
        String query= "INSERT INTO orders VALUES(?, ?, 123);";

        try {
            int id = getOID();
            if (id == -1) {
              return;
            }
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, id);
            ps.setDouble(2, o.calcPrice());
            ps.executeUpdate();
            ArrayList<Pizza> pizzas = o.getPizzas();

            for (Pizza p : pizzas) {
              int pzid = getPID();
              if (pzid == -1) {
                return;
              }

              query = "SELECT id FROM baseprice WHERE crust=? AND size=?";
              ps = conn.prepareStatement(query);
              ps.setString(1, p.getCrust());
              ps.setString(2, p.getSize());
              ResultSet r = ps.executeQuery();

              if (r.next()) {
                int bpid = r.getInt(1);
                query = "INSERT INTO pizza VALUES(?, \"Open\", ?, 2.00, ?, ?, ?);";
                ps = conn.prepareStatement(query);
                ps.setInt(1, pzid);
                java.util.Date dat = new java.util.Date();
                java.sql.Timestamp ts = new java.sql.Timestamp(dat.getTime());
                ps.setTimestamp(2, ts);
                ps.setDouble(3, p.calcPrice());
                ps.setInt(4, bpid);
                ps.setInt(5, id);
                ps.executeUpdate();
                ArrayList<Topping> t = p.getToppings();
                
                for (Topping tp : t) {
                  query = "INSERT INTO pizzatoppings VALUES(?, ?, ?);";
                  ps = conn.prepareStatement(query);
                  ps.setInt(1, pzid);
                  ps.setString(2, tp.getName());
                  ps.setBoolean(3, tp.getExtra());
                  ps.executeUpdate();

                  if (p.getSize() == size_s) {
                    query = "UPDATE toppings SET inventory=inventory-usmall;";
                  } else if (p.getSize() == size_m) {
                    query = "UPDATE toppings SET inventory=inventory-umedium;";
                  } else if (p.getSize() == size_l) {
                    query = "UPDATE toppings SET inventory=inventory-ularge;";
                  } else {
                    query = "UPDATE toppings SET inventory=inventory-uxlarge;";
                  }

                  Statement s = conn.createStatement();
                  s.executeUpdate(query);
                }

                ArrayList<Discount> disid = p.getDiscounts();
                for (Discount d : disid) {
                  query = "INSERT INTO pizzadiscount VALUES(?, ?);";
                  ps = conn.prepareStatement(query);
                  ps.setInt(1, pzid);
                  ps.setString(2, d.getName());
                  ps.executeUpdate();
                }
              }
  
            }

            ArrayList<Discount> oids = o.getDiscounts();

            for (Discount d : oids) {
              query = "INSERT INTO orderdiscount VALUES(?,?);";
              ps = conn.prepareStatement(query);
              ps.setInt(1, id);
              ps.setString(2, d.getName());
              ps.executeUpdate();
            }

            ICustomer C = o.getCustomer();

            if (C instanceof DineInCustomer) {
              DineInCustomer customer = (DineInCustomer) C;
              query = "INSERT INTO dinein VALUES(?,?,?);";
              ps = conn.prepareStatement(query);
              ps.setInt(1, id);
              ps.setInt(2, customer.getTableNum());
              ps.setInt(3, id);
              ps.executeUpdate();
              List<Integer> seats = customer.getSeats();

              for (int s : seats) {
                query = "INSERT INTO seat VALUES(?,?);";
                ps = conn.prepareStatement(query);
                ps.setInt(1, s);
                ps.setInt(2, id);
                ps.executeUpdate();
              }
            } else if (C instanceof DeliveryCustomer) {
              DeliveryCustomer customer = (DeliveryCustomer) C;
              query = "INSERT INTO delivery VALUES(?,?);";
              ps = conn.prepareStatement(query);
              ps.setInt(1, id);
              ps.setInt(2, customer.getID());
              ps.executeUpdate();
            } else {
              DineOutCustomer customer = (DineOutCustomer) C;
              query = "INSERT INTO pickup VALUES(?,?)";
              ps = conn.prepareStatement(query);
              ps.setInt(1, id);
              ps.setInt(2, customer.getID());
              ps.executeUpdate();
            }
          } catch (SQLException e){
            System.out.println(e.getMessage());
          } finally {
            conn.close();
          }
      }

    

    private static int getCID() throws SQLException, IOException {
        Statement st = conn.createStatement();
        int r = -1;
        try {
          ResultSet result = st.executeQuery("SELECT MAX(id) from customer;");
          if (result.next()) {
            r = result.getInt(1) + 1;
          }
          if (result.wasNull()) {
            r = 0;
          }
        } catch (SQLException exp) {
          System.out.println(exp.getMessage());
        } finally {
          return r;
        }
      }

    /**
     *
     * @param c the new customer to add to the database
     * @throws SQLException
     * @throws IOException
     * @requires c is not null. C's ID is -1 and will need to be assigned
     * @ensures c is given an ID and added to the database
     */
    public static void addCustomer(ICustomer c) throws SQLException, IOException
    {
        connect_to_db();
		//add code to add the customer to the DB.
        try {
            if (c instanceof DeliveryCustomer) {
              DeliveryCustomer dcust = (DeliveryCustomer) c;
              int id = getCID();
              if (id == -1) {
                return;
              }

              String query = "INSERT INTO customer VALUES(?, ?, ?);";
              PreparedStatement ps = conn.prepareStatement(query);

              ps.setInt(1, id);
              ps.setString(2, dcust.getName());
              ps.setString(3, dcust.getPhone());
              ps.executeUpdate();
    
              query = "INSERT INTO deliverycustomer VALUES(?, ?, ?);";
              ps = conn.prepareStatement(query);

              ps.setInt(1, id);
              ps.setInt(2, id);
              ps.setString(3, dcust.getAddress());
              ps.executeUpdate();
            } else if (c instanceof DineOutCustomer) {
              DineOutCustomer docust = (DineOutCustomer) c;
              int id = getCID();
              if (id == -1) {
                return;
              }

              String query = "INSERT INTO customer VALUES(?,?,?);";
              PreparedStatement ps = conn.prepareStatement(query);

              ps.setInt(1, id);
              ps.setString(2, docust.getName());
              ps.setString(3, docust.getPhone());
              ps.executeUpdate();
            }
          } catch (SQLException e) {
            System.out.println(e.getMessage());
          } finally {
            conn.close();
          }
    }

    /**
     *
     * @param o the order to mark as complete in the database
     * @throws SQLException
     * @throws IOException
     * @requires the order exists in the database
     * @ensures the order will be marked as complete
     */
    public static void CompleteOrder(Order o) throws SQLException, IOException
    {
        connect_to_db();
		/*add code to mark an order as complete in the DB. You may have a boolean field for this, or maybe a completed time timestamp. However you have it, */
      String query = "UPDATE pizza SET status='Completed' where id=?;";
      List<Pizza> pizzas = o.getPizzas();
      try {
        for (Pizza pz : pizzas) {
            PreparedStatement ps = conn.prepareStatement(query);
            
ps.setInt(1, pz.getID());
            ps.executeUpdate();
        }
      } catch (SQLException e) {
        System.out.println(e.getMessage());
      } finally {
        conn.close();
      }
    }

    /**
     *
     * @param t the topping whose inventory is being replenished
     * @param toAdd the amount of inventory of t to add
     * @throws SQLException
     * @throws IOException
     * @requires t exists in the database and toAdd > 0
     * @ensures t's inventory level is increased by toAdd
     */
    public static void AddToInventory(Topping t, double toAdd) throws SQLException, IOException
    {
        connect_to_db();
		/*add code to add toAdd to the inventory level of T. This is not adding a new topping, it is adding a certain amount of stock for a topping. This would be used to show that an order was made to replenish the restaurants supply of pepperoni, etc*/
        String query = "UPDATE toppings SET inventory=inventory + ? WHERE name=?;";
        try {
          PreparedStatement ps = conn.prepareStatement(query);
          ps.setDouble(1, toAdd);
          ps.setString(2, t.getName());
          ps.executeUpdate();
        } catch (SQLException e) {
          System.out.println(e.getMessage());
        } finally {
          conn.close();
        }
    }


    /*
        A function to get the list of toppings and their inventory levels. I have left this code "complete" as an example of how to use JDBC to get data from the database. This query will not work on your database if you have different field or table names, so it will need to be changed

        Also note, this is just getting the topping ids and then calling getTopping() to get the actual topping. You will need to complete this on your own

        You don't actually have to use and write the getTopping() function, but it can save some repeated code if the program were to expand, and it keeps the functions simpler, more elegant and easy to read. Breaking up the queries this way also keeps them simpler. I think it's a better way to do it, and many people in the industry would agree, but its a suggestion, not a requirement.
    */

    /**
     *
     * @return the List of all toppings in the database
     * @throws SQLException
     * @throws IOException
     * @ensures the returned list will include all toppings and accurate inventory levels
     */
     public static ArrayList<Topping> getInventory() throws SQLException, IOException
    {
        //start by connecting
        connect_to_db();
        ArrayList<Topping> ts = new ArrayList<Topping>();
        String query = "SELECT name FROM toppings;";
        try {
          Statement s = conn.createStatement();
          ResultSet r = s.executeQuery(query);
          ArrayList<String> names = new ArrayList<String>();
          
          while (r.next()) {
            names.add(r.getString(1));
          }
          
          if (!r.wasNull()) {
            for (String name : names) {
              ts.add(getTopping(name));
            }
          }
        } catch (SQLException e) {
          System.out.println(e.getMessage());
        } finally {
          conn.close();
          return ts;
        }
    }

    /**
     *
     * @return a list of all orders that are currently open in the kitchen
     * @throws SQLException
     * @throws IOException
     * @ensures all currently open orders will be included in the returned list.
     */
    public static ArrayList<Order> getCurrentOrders() throws SQLException, IOException
    {
        connect_to_db();

        ArrayList<Order> orders = new ArrayList<Order>();
		/*add code to get a list of all open orders. Only return Orders that have not been completed. If any pizzas are not completed, then the order is open.*/
        String query = "SELECT orders.id FROM orders JOIN pizza ON orders.id=pizza.ordernum WHERE status<>'Completed';";
        try {
          Statement s = conn.createStatement();
          ResultSet r = s.executeQuery(query);
          ArrayList<Integer> id = new ArrayList<Integer>();
          while (r.next()) {
            id.add(r.getInt(1));
          }
          if (!r.wasNull()) {
            for (int i : id) {
              orders.add(getOrder(i));
            }
          }
        } catch (SQLException e) {
          System.out.println(e.getMessage());
        } finally {
          conn.close();
          return orders;
        }
    }

    /**
     *
     * @param size the pizza size
     * @param crust the type of crust
     * @return the base price for a pizza with that size and crust
     * @throws SQLException
     * @throws IOException
     * @requires size = size_s || size_m || size_l || size_xl AND crust = crust_thin || crust_orig || crust_pan || crust_gf
     * @ensures the base price for a pizza with that size and crust is returned
     */
    public static double getBasePrice(String size, String crust) throws SQLException, IOException
    {
        connect_to_db();
        double bp = 0.0;

        //add code to get the base price for that size and crust pizza Depending on how you store size and crust in your database, you may have to do a conversion

        String query = "SELECT price FROM baseprice WHERE size=? AND crust=?;";
        try {
          PreparedStatement ps = conn.prepareStatement(query);
          ps.setString(1, size);
          ps.setString(2, crust);
          ResultSet r = ps.executeQuery();
          if (r.next()) {
            bp = r.getDouble(1);
          }
        } catch (SQLException e) {
          System.out.println(e.getMessage());
        } finally {
          conn.close();
          return bp;
        }
    }

    /**
     *
     * @return the list of all discounts in the database
     * @throws SQLException
     * @throws IOException
     * @ensures all discounts are included in the returned list
     */
    public static ArrayList<Discount> getDiscountList() throws SQLException, IOException
    {
        ArrayList<Discount> discount = new ArrayList<Discount>();
        connect_to_db();
        //add code to get a list of all discounts
        String query = "SELECT name FROM discounts;";

        try {
          Statement s = conn.createStatement();
          ResultSet r = s.executeQuery(query);
          ArrayList<String> id = new ArrayList<String>();

          while (r.next()) {
            id.add(r.getString(1));
          }
          if (!r.wasNull()) {
            for (String i : id) {
              discount.add(getDiscount(i));
            }
          }
        } catch (SQLException e) {
          System.out.println(e.getMessage());
        } finally {
          conn.close();
          return discount;
        }

    }

    /**
     *
     * @return the list of all delivery and carry out customers
     * @throws SQLException
     * @throws IOException
     * @ensures the list contains all carryout and delivery customers in the database
     */
    public static ArrayList<ICustomer> getCustomerList() throws SQLException, IOException
    {
        ArrayList<ICustomer> cust = new ArrayList<ICustomer>();
        connect_to_db();
        //add code to get a list of all customers
        String query = "SELECT id FROM customer;";
        try {
          Statement s = conn.createStatement();
          ResultSet r = s.executeQuery(query);
          ArrayList<Integer> id = new ArrayList<Integer>();

          while (r.next()) {
            id.add(r.getInt(1));
          }
          if (!r.wasNull()) {
            for (int i : id) {
              cust.add(getCustomer(i));
            }
          }
        } catch (SQLException e) {
          System.out.println(e.getMessage());
        } finally {
          conn.close();
          return cust;
        }

    }

	/*
	Note: The following incomplete functions are not strictly required, but could make your DBNinja class much simpler. For instance, instead of writing one query to get all of the information about an order, you can find the primary key of the order, and use that to find the primary keys of the pizzas on that order, then use the pizza primary keys individually to build your pizzas. We are no longer trying to get everything in one query, so feel free to break them up as much as possible

	You could also add functions that take in a Pizza object and add that to the database, or take in a pizza id and a topping id and add that topping to the pizza in the database, etc. I would recommend this to keep your addOrder function much simpler

	These simpler functions should still not be called from our menu class. That is why they are private

	We don't need to open and close the connection in these, since they are only called by a function that has opened the connection and will close it after
	*/

	
    private static Topping getTopping(String name) throws SQLException, IOException
    {
      Topping tops = new Topping("fake", 0.25, 100.0, -1);
      String query = "SELECT price, inventory FROM toppings WHERE name=?;";

      try {
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setString(1, name);
        ResultSet r = ps.executeQuery();

        if (r.next()) {
          tops = new Topping(name, r.getDouble(1), r.getDouble(2), -1);
        }
      } catch (SQLException e) {
        System.out.println("Error getting topping");
        System.out.println(e.getMessage());
        conn.close();
      } finally {
        return tops;
      }
    }

    private static Discount getDiscount(String name)  throws SQLException, IOException
    {

        //add code to get a discount

        Discount disc = new Discount("fake", 0.0, 0.0, -1);
        String query = "SELECT per, dollar FROM discounts WHERE name=?;";

        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, name);
            ResultSet rset = ps.executeQuery();
            if (rset.next()) {
              double percent_off = rset.getDouble(1);
              double cash_off = rset.getDouble(2);
              disc = new Discount(name, percent_off, cash_off, -1);
            }
        } catch (SQLException e) {
            System.out.println("Error getting discount");
            System.out.println(e.getMessage());
            conn.close();
        } finally {
            return disc;
        }

    }

    private static Pizza getPizza(int id)  throws SQLException, IOException
    {

        //add code to get Pizza Remember, a Pizza has toppings and discounts on it
        Pizza pz = new Pizza(-1, "Small", "Regular", 0);
        String query = "SELECT base FROM pizza WHERE id=?;";
        try {
          PreparedStatement ps = conn.prepareStatement(query);
          ps.setInt(1, id);
          ResultSet r = ps.executeQuery();
          if (r.next()) {
            query = "SELECT size, crust, price FROM baseprice WHERE id=?;";
            ps = conn.prepareStatement(query);
            ps.setInt(1, r.getInt(1));
            r = ps.executeQuery();
            if (r.next()) {
              pz = new Pizza(id, r.getString(1), r.getString(2), r.getDouble(3));
              query = "SELECT topping, extra FROM pizzatoppings WHERE pizza=?;";
              ps = conn.prepareStatement(query);
              ps.setInt(1, id);
              r = ps.executeQuery();
              ArrayList<Topping> top = new ArrayList<Topping>();
              while (r.next()) {
                Topping t = getTopping(r.getString(1));
                if (r.getBoolean(2)) {
                  t.makeExtra();
                }
                top.add(t);
              }
              if (!r.wasNull()) {
                for (Topping t : top) {
                  pz.addTopping(t);
                }
                query = "SELECT discount FROM pizzadiscount WHERE pizza=?;";
                ps = conn.prepareStatement(query);
                ps.setInt(1, id);
                r = ps.executeQuery();
                ArrayList<Discount> dis = new ArrayList<Discount>();
                while (r.next()) {
                  Discount d = getDiscount(r.getString(1));
                  dis.add(d);
                }
                if (!r.wasNull()) {
                  for (Discount d : dis) {
                    pz.addDiscount(d);
                  }
                }
              }
            }
          }
        } catch (SQLException e) {
          System.out.println("Error getting pizza");
          System.out.println(e.getMessage());
          conn.close();
        } finally {
          return pz;
        }

    }

    private static ICustomer getCustomer(int ID)  throws SQLException, IOException
    {

        //add code to get customer
        ICustomer C = new DineOutCustomer();

        String query = "Select name, phone From customer where id=?;";
        String deliveryQuery = "Select address from deliverycustomer where customer=?;";

        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, ID);
            ResultSet rset = stmt.executeQuery();
            if (rset.next()) {
              String name = rset.getString(1);
              String phone = rset.getString(2);
              C = new DineOutCustomer(ID, name, phone);
              stmt = conn.prepareStatement(deliveryQuery);
              stmt.setInt(1, ID);
              rset = stmt.executeQuery();
              if(rset.next()) {
                  String address = rset.getString(1);
                  C = new DeliveryCustomer(ID, name, phone, address);
              }
            }
        } catch (SQLException e) {
            System.out.println("Error getting customer");
            System.out.println(e.getMessage());
            conn.close();
        } finally {
          return C;
        }
    }

    private static Order getOrder(int ID)  throws SQLException, IOException
    {

        //add code to get an order. Remember, an order has pizzas, a customer, and discounts on it
        ICustomer C = new DineInCustomer(0, new ArrayList<Integer>(), -1);
        Order O = new Order(ID, C, dine_in);
        String query = "SELECT id FROM orders WHERE id=?;";
        try {
          PreparedStatement ps = conn.prepareStatement(query);
          ps.setInt(1, ID);
          ResultSet r = ps.executeQuery();
          if (r.next()) {
            ArrayList<Integer> pids = new ArrayList<Integer>();
            ArrayList<Pizza> pizzas = new ArrayList<Pizza>();
            query = "SELECT id FROM pizza WHERE ordernum=?;";
            ps = conn.prepareStatement(query);
            ps.setInt(1, ID);
            r = ps.executeQuery();
            while (r.next()) {
              pids.add(r.getInt(1));
            }
            if (!r.wasNull()) {
              for (int p : pids) {
                pizzas.add(getPizza(p));
              }
              ArrayList<String> dids = new ArrayList<String>();
              ArrayList<Discount> disc = new ArrayList<Discount>();
              query = "SELECT discount FROM orderdiscount WHERE ordernum=?;";
              ps = conn.prepareStatement(query);
              ps.setInt(1, ID);
              r = ps.executeQuery();
              while (r.next()) {
                dids.add(r.getString(1));
              }
              if (!r.wasNull()) {
                for (String d: dids) {
                  disc.add(getDiscount(d));
                }
              }
              query = "SELECT tablenum FROM dinein WHERE ordernum=?;";
              ps = conn.prepareStatement(query);
              ps.setInt(1, ID);
              r = ps.executeQuery();
              if (r.next()) {
                int table = r.getInt(1);
                ArrayList<Integer> seats = new ArrayList<Integer>();
                query = "SELECT seat.seat FROM seat WHERE ordernum=?;";
                ps = conn.prepareStatement(query);
                ps.setInt(1, ID);
                r = ps.executeQuery();
                while (r.next()) {
                  seats.add(r.getInt(1));
                }
                C = new DineInCustomer(table, seats, ID);
                O = new Order(ID, C, dine_in);
              } else {
                query = "SELECT customer FROM pickup WHERE ordernum=?;";
                ps = conn.prepareStatement(query);
                ps.setInt(1, ID);
                r = ps.executeQuery();
                if (r.next()) { 
                  int cid = r.getInt(1);
                  C = getCustomer(cid);
                  O = new Order(ID, C, pickup);
                } else {
                  query = "SELECT customer FROM delivery WHERE ordernum=?;";
                  ps = conn.prepareStatement(query);
                  ps.setInt(1, ID);
                  r = ps.executeQuery();
                  if (r.next()) {
                    int cid = r.getInt(1);
                    C = getCustomer(cid);
                    O = new Order(ID, C, delivery);
                  } else {
                    System.out.println("couldn't find the customer for that order");
                  }
                }
              }
              
              for (Pizza p: pizzas) {
                O.addPizza(p);
              }
              for (Discount D : disc) {
                O.addDiscount(D);
              }
            }
          }
        } catch (SQLException e) {
          conn.close();
          System.out.println("Error getting order");
          System.out.println(e.getMessage());
        } finally {
          return O;
        }
    }
}


