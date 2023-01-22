import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import classes.*;
import java.util.Scanner;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class serverGui implements WindowListener, ActionListener{
    public static void main(String args[]){
        // define frame
        JFrame frame = new JFrame("Server");
        frame.pack();
        frame.setSize(500,500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // define main menu panel
        JPanel mainMenuPanel = new JPanel();
        JLabel titleLabel = new JLabel();
        titleLabel.setText("Choose option");
        JButton listAllOrdersButton = new JButton("List All Orders");
        JButton listOpenOrdersButton = new JButton("List Open Orders");
        JButton listPastOrdersButton = new JButton("List Past Orders");
        JButton listUsersButton = new JButton("List Users");
        JButton viewProductListButton = new JButton("View Product List");
        JButton addProductButton = new JButton("Add Product");
        JButton removeProductButton = new JButton("Remove Product");
        JButton modifyShippingFeeButton = new JButton("Modify Shipping Fee");
        JButton exitButton = new JButton("Exit");
        

        mainMenuPanel.setLayout(new GridLayout(5,2));
        mainMenuPanel.add(titleLabel);
        mainMenuPanel.add(listAllOrdersButton);
        mainMenuPanel.add(listOpenOrdersButton);
        mainMenuPanel.add(listPastOrdersButton);
        mainMenuPanel.add(listUsersButton);
        mainMenuPanel.add(viewProductListButton);
        mainMenuPanel.add(addProductButton);
        mainMenuPanel.add(removeProductButton);
        mainMenuPanel.add(modifyShippingFeeButton);
        mainMenuPanel.add(exitButton);

        // define add product panel
        JPanel addProductPanel = new JPanel();
        JLabel productNameLabel = new JLabel("Enter product name : ");
        JTextField productNameTextField = new JTextField(20);
        JLabel productPriceLabel = new JLabel("Enter product price : ");
        JTextField productPriceTextField = new JTextField(20);
        JLabel productStockLabel = new JLabel("Enter product stock : ");
        JTextField productStockTextField = new JTextField(20);
        JLabel productSupplierLabel = new JLabel("Enter product supplier : ");
        JTextField productSupplierTextField = new JTextField(20);
        JButton addProductRequestButton = new JButton("Add product");

        addProductPanel.setLayout(new GridLayout(5,2));
        addProductPanel.add(productNameLabel);
        addProductPanel.add(productNameTextField);
        addProductPanel.add(productPriceLabel);
        addProductPanel.add(productPriceTextField);
        addProductPanel.add(productStockLabel);
        addProductPanel.add(productStockTextField);
        addProductPanel.add(productSupplierLabel);
        addProductPanel.add(productSupplierTextField);
        addProductPanel.add(addProductRequestButton);

        // define result panel
        JPanel addProductResultPanel = new JPanel();
        JLabel addProductResultLabel = new JLabel();
        JButton returnMainMenuButton = new JButton("Back to main menu");

        addProductResultPanel.setLayout(new GridLayout(2,1));
        addProductResultPanel.add(addProductResultLabel);
        addProductResultPanel.add(returnMainMenuButton);
        // define cardLayout
        JPanel cards = new JPanel(new CardLayout());
        cards.add(mainMenuPanel);
        cards.add(addProductPanel);
        cards.add(addProductResultPanel);

        // define button function
        addProductButton.addActionListener(new ActionListener() { 
            @Override
            public void actionPerformed(ActionEvent e) { 
                CardLayout cl = (CardLayout) cards.getLayout();
                cl.next(cards);
            } 
        });

        addProductRequestButton.addActionListener(new ActionListener() { 
            @Override
            public void actionPerformed(ActionEvent e) { 
                String productName = productNameTextField.getText();
                int productPrice = Integer.parseInt(productPriceTextField.getText());
                int productStock = Integer.parseInt(productStockTextField.getText());
                Supplier productSupplier = new Supplier(productSupplierTextField.getText());
                if (Server.addToProductList(new Product(productName, productPrice, productStock, productSupplier))){
                    addProductResultLabel.setText("Add success!");
                    CardLayout cl = (CardLayout) cards.getLayout();
                    cl.next(cards);                    
                }
                else{
                    addProductResultLabel.setText("Add fail!");
                    CardLayout cl = (CardLayout) cards.getLayout();
                    cl.next(cards);                       
                }
            }
        });

        returnMainMenuButton.addActionListener(new ActionListener() { 
            @Override
            public void actionPerformed(ActionEvent e) { 
                CardLayout cl = (CardLayout) cards.getLayout();
                cl.previous(cards);
            } 
        });
        
        // show cardLayout in frame
        frame.getContentPane().add(cards);
        frame.setLayout(new FlowLayout(FlowLayout.CENTER));
        frame.setSize(500,500);
        frame.setVisible(true);
    }
    public void actionPerformed(ActionEvent e) {
    }

    public void windowClosing(WindowEvent e) {}
    public void windowOpened(WindowEvent e) {}
    public void windowActivated(WindowEvent e) {}
    public void windowIconified(WindowEvent e) {}
    public void windowDeiconified(WindowEvent e) {}
    public void windowDeactivated(WindowEvent e) {}
    public void windowClosed(WindowEvent e) {}
}

public class Server {
    private static HashMap<User, ArrayList<Order>> userOrder = new HashMap<User, ArrayList<Order>>();
    private static ArrayList<Product> productList = new ArrayList<>();
    private ArrayList<Discount> discountList = new ArrayList<>();

    // const
    static int SHIPPING_FEE = 10;

    static HashMap<User, ArrayList<Order>> getUserOrder(){
        return userOrder;
    }

    static ArrayList<Order> getOrderListOfUser(String userEmail){
        User currUser = getUserByEmail(userEmail);
        return userOrder.get(currUser);
    }

    static Order getOpenOrderOfUser(String userEmail){
        ArrayList<Order> allOrderOfUser = getOrderListOfUser(userEmail);
        return allOrderOfUser.get(allOrderOfUser.size()-1);
    }

    static ArrayList<Product> getProductList(){
        return productList;
    }

    ArrayList<Discount> getDiscountList(){
        return discountList;
    }

    static ArrayList<User> getUserList(){
        ArrayList<User> userList = new ArrayList<User>();
        Set<User> userSet = userOrder.keySet();
        userList.addAll(userSet);
        return userList;
    }

    static ArrayList<Order> getOrderList(){
        ArrayList<Order> orderList = new ArrayList<Order>();
        for (ArrayList<Order> ordersPerUser : userOrder.values()){
            orderList.addAll(ordersPerUser);
        }
        return orderList;        
    }

    static HashMap<User, Order> getOpenOrderList(){
        HashMap<User, Order> orderList = new HashMap<>();
        userOrder.forEach((key, value) -> orderList.put(key, value.get(value.size()-1)));
        return orderList;
    }

    static HashMap<User, ArrayList<Order>> getClosedOrderList(){
        HashMap<User, ArrayList<Order>> orderList = new HashMap<>();
        userOrder.forEach((key, value) -> orderList.put(key, new ArrayList(value.subList(0, value.size()-1))));
        return orderList;
    }
    
    // returns user by email, used for server to keep user info
    static User getUserByEmail(String email){
        ArrayList<User> userList = getUserList();
        for (User userIter : userList){
            if (userIter.getEmail().equals(email)){
                return userIter;
            }
        }

        // never happens but exists for syntax
        return userList.get(0);
    }

    // function to initialize the server for testing.
    static public void serverInit(){
        // define Suppliers
        Supplier supplierA = new Supplier("A");
        Supplier supplierB = new Supplier("B");

        // add accounts
        signUp("abc@gmail.com", "abc");
        signUp("def@gmail.com", "def");

        // define products
        Product product1 = new Product("apple", 1000, 10, supplierA);
        Product product2 = new Product("banana", 500, 7, supplierB);
        Product product3 = new Product("garlic", 2000, 12, supplierA);

        // add products
        addToProductList(product1);
        addToProductList(product2);
        addToProductList(product3);

        // define discounts
        DiscountShipping discountShippingCoupon = new DiscountShipping(50);

        // discount to abc@gmail.com
        giveDiscountToUser("abc@gmail.com", discountShippingCoupon);
        addToBasket("abc@gmail.com", "apple", 2);
        // server init done
        System.out.println("Server initialized");
    }

    // login a user 
    public boolean login(String email, String password){ 
        for(User user : getUserList()){
            // check if email exist in user interface 
            if (email.equals(user.getEmail())){
                // password matches -> login success
                if(user.checkPassword(password)){
                    return true;
                } 
                // password incorrect -> login fail
                else return false;
            }
        }
        // email not found -> logic fail
        return false; 
    }

    // sign up user
    static public boolean signUp(String email, String password){
        // check if email already exists
        for(User user : getUserList()){
            // if email already exists, it will return false;
            if (email.equals(user.getEmail())) return false;
        }
        // email doesn't exist, so create user
        User user = new User(email, password);
        // create empty init basket
        ArrayList<Order> newOrder = new ArrayList<Order>(); 
        Order newBasket = new Order();
        newOrder.add(newBasket);
        userOrder.put(user, newOrder);
        return true;
    }

    // Add product to productList
    static public boolean addToProductList(Product product){
        // check if duplicate product exist
        for (Product iter : productList){
            if (iter == product) return false;
        }
        productList.add(product);
        return true;
    }

    // Remove product from productList
    static public boolean removeFromProductList(Product product){
        return productList.remove(product);
    }

    // Add a product to a basket
    public static boolean addToBasket(String userEmail, String productName, int stock){
        // get current user order
        Order basket = getOpenOrderOfUser(userEmail);

        Product basketProduct = new Product(productName, 0, stock, null); 
        Product serverProduct;

        // if product not in server, return false
        if (getProductList().indexOf(basketProduct) == -1) return false;
        else{
            serverProduct = getProductList().get(getProductList().indexOf(basketProduct));
        } 

        // if stock too big, return false
        if (stock > serverProduct.getStock()) return false;
        else{
            basketProduct.setPrice(serverProduct.getPrice());
            basketProduct.setSupplier(serverProduct.getSupplier());
            // add product to basket
            basket.addToOrder(basketProduct);
        } 

        // add to basket succeeded
        return true;
    }

    // Delete a product from a basket
    public boolean removeFromBasket(String userEmail, String productName, int stock){
        // get current user order
        Order basket = getOpenOrderOfUser(userEmail);

        // find appropriate product
        Product newBasketProduct = new Product(productName, 0, 0, null); 
        Product oldBasketProduct = basket.getProductList().get(basket.getProductList().indexOf(newBasketProduct));
        int newStock = oldBasketProduct.getStock() - stock;
        Product serverProduct;

        // if product not in server, return false
        if (getProductList().indexOf(oldBasketProduct) == -1) return false;
        else{
            serverProduct = getProductList().get(getProductList().indexOf(oldBasketProduct));
        } 

        // if the user tries to remove too much stock, return false
        if (newStock < 0) return false;
        else{
            newBasketProduct.setStock(newStock);
            newBasketProduct.setPrice(serverProduct.getPrice());
            newBasketProduct.setSupplier(serverProduct.getSupplier());

            // add product to basket
            basket.removeFromOrder(newBasketProduct);
        } 

        // add to basket succeeded
        return true;
    }

    // add discount to order
    public void addDiscount(Order order, Discount discount){
        order.addDiscount(discount);
    }

    // remove discount from order
    public void removeDiscount(Order order, Discount discount){
        order.removeDiscount(discount);
    }

    Product restock(Product product){
        /* invoked when stock is less than 5 after making an order. */

        // only order when the stock is lower than 5
        if (product.getStock() < 5){
            // make order to supplier
            System.out.println("Server log : Restock of " + product.getName() + " made for supplier " + product.getSupplier().getName() + ".");
            
            // change stock in server
            product.setStock(product.getStock() + 7);
        }
        return product;
    }

    // make order
    boolean makeOrder(String userEmail){

        // get user by email
        User currUser = getUserByEmail(userEmail);

        // retrieve all orders from the current user
        ArrayList<Order> userOrders = userOrder.get(currUser);
        
        // get the open order from the current user
        Order currentOrder = userOrders.get(userOrders.size()-1);

        // check if there are any products in open order
        if (currentOrder.getProductList().size() == 0){
            System.out.println("Server log : Empty basket, failed to order.\n");
            return false;
        }

        // check if the stock is valid
        ArrayList<Product> orderProductList = currentOrder.getProductList();
        for (Product currProduct : orderProductList){
            // find it in server product list
            int productIndex = productList.indexOf(currProduct);

            // product not found
            if (productIndex == -1) return false; 
            // product found, but trying to order too many
            else if (currProduct.getStock() > productList.get(productIndex).getStock()) return false;
            // do nothing if valid
        }

        // make order and restock if needed
        for (Product currProduct : orderProductList){
            // find it in server product list
            int productIndex = productList.indexOf(currProduct);

            // set new stock
            productList.get(productIndex).setStock(productList.get(productIndex).getStock()-currProduct.getStock());
            
            // restock when needed
            if (productList.get(productIndex).getStock() < 5) productList.get(productIndex).restock();
        }

        // calculate order price
        int priceSum = 0;
        for (Product productIter : orderProductList){
            priceSum += productIter.getPrice() * productIter.getStock();
        }
        System.out.println("Server log : order price = " + Integer.toString(priceSum));

        // calculate shipping fee
        int currOrderShippingFee = -1;
        for (Discount discountIter : currentOrder.getDiscountList()){
            if (discountIter.getDiscountCode() == 10000){
                currOrderShippingFee = ((DiscountShipping) discountIter).calculateShipping(SHIPPING_FEE);
            }
        }

        if (currOrderShippingFee == -1) currOrderShippingFee = SHIPPING_FEE;
        System.out.println("Server log : shipping price = " + currOrderShippingFee);

        // print total price
        System.out.println("Server log : total price = " + Integer.toString(priceSum + currOrderShippingFee));

        // close order
        currentOrder.closeOrder();
        // open new empty basket
        userOrders.add(new Order());
        return true;
    }

    Product changeStock(Product product, int newStock){
        /* invoked when we wish to change the stock of a product */
        product.setStock(newStock);
        return product;
    }

    static void giveDiscountToUser(String userEmail, Discount discountCoupon){
        User currUser = getUserByEmail(userEmail);
        userOrder.get(currUser).get(userOrder.get(currUser).size()-1).addDiscount(discountCoupon);
    }

    private static void clearConsole(){
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private static void pressEnterToContinue(Scanner sc){ 
           System.out.println("Press Enter key to continue...");
           try{
               System.in.read();
               sc.nextLine();
           } catch(Exception e){}  
    }

    private static void printMain(){
        clearConsole();

        System.out.println("Choose option");
        System.out.println("1. List All Orders");        
        System.out.println("2. List Open Orders");
        System.out.println("3. List Past Orders");
        System.out.println("4. List Users");
        System.out.println("5. View Product List");
        System.out.println("6. Add Product");
        System.out.println("7. Remove Product");
        System.out.println("8. Modify Shipping Fee");
        System.out.println("9. Exit");
    }

    public static void main(String[] args) {
        // Login success -> use site.
        Boolean fin = false;
        Scanner sc = new Scanner(System.in);
        serverInit();
        while(!fin){
            // print main menu
            printMain();

            // detects error
            boolean error = true;
            // error handling when input is not integer parseable
            try{
                // parse input
                int num = Integer.parseInt(sc.next());

                //  1. List All Orders
                if (num == 1){   
                    clearConsole();
                    userOrder.entrySet().forEach(entry -> {
                        System.out.println(entry.getKey() + " : " + entry.getValue());
                    });
                    pressEnterToContinue(sc);
                // 2. List Past Orders
                } else if (num == 2){   
                    clearConsole();
                    HashMap<User, Order> openOrderList = getOpenOrderList();
                    openOrderList.entrySet().forEach(entry -> {
                        System.out.println(entry.getKey() + " : " + entry.getValue());
                    });
                    pressEnterToContinue(sc);
                // 3. List Past Orders
                } else if (num == 3){  
                    clearConsole();
                    HashMap<User, ArrayList<Order>> closedOrderList = getClosedOrderList();
                    closedOrderList.entrySet().forEach(entry -> {
                        System.out.println(entry.getKey() + " : " + entry.getValue());
                    });
                    pressEnterToContinue(sc);
                // 4. List Users
                } else if(num == 4){   
                    clearConsole();  
                    System.out.println(getUserList());
                    pressEnterToContinue(sc);
                // 5. View Product List
                } else if(num == 5){    
                    clearConsole(); 
                    System.out.println(getProductList());
                    pressEnterToContinue(sc);                    
                // 6. Add Product
                } else if(num == 6){     
                    clearConsole();
                    // get product info
                    System.out.println("Please enter the product name to add : ");
                    String productName = sc.next();
                    System.out.println("Please enter price of product : ");
                    int price = Integer.parseInt(sc.next());                    
                    System.out.println("Please enter stock : ");
                    int stock = Integer.parseInt(sc.next());
                    System.out.println("Please enter supplier : ");
                    Supplier supplier = new Supplier(sc.next());
                    error = addToProductList(new Product(productName, price, stock, supplier));
                // 7. Remove Product
                } else if(num == 7){   
                    clearConsole();  
                    System.out.println("Please enter the product name to remove : ");
                    String productName = sc.next();
                    error = removeFromProductList(new Product(productName, 0, 0, null));
                // 8. Modify Shipping Fee
                } else if(num == 8){   
                    clearConsole();  
                    System.out.println("Please enter new shipping fee : ");
                    SHIPPING_FEE = Integer.parseInt(sc.next());     
                    pressEnterToContinue(sc);                                  
                // 9. Exit
                } else if(num == 9){     
                    clearConsole();
                    System.out.println("Thank you! See you next time.");
                    fin = true;
                }

                // Error
                if (!error){
                    System.out.println("Error occured. Please retry.");
                    error = true;
                    pressEnterToContinue(sc);                       
                }

            // Invalid Number     
            } catch(Exception e){
                System.out.println("Please enter a valid number!\n");
            }
        }
        sc.close();
    }
}