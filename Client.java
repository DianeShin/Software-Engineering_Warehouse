import java.util.ArrayList;
import classes.*;
import java.util.Scanner;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class clientGui implements WindowListener, ActionListener{
    static String userEmail = "";
    public static void main(String args[]){
        Server server = new Server();
        Server.serverInit();

        // define frame
        JFrame frame = new JFrame("Client");
        frame.pack();
        frame.setSize(500,500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // define main panel
        JPanel initPanel = new JPanel();

        JButton loginButton = new JButton("Login");
        JButton signUpButton = new JButton("Sign Up");

        initPanel.add(loginButton);
        initPanel.add(signUpButton);

        // define login panel
        JPanel loginPanel = new JPanel();
        JLabel userEmailLabel = new JLabel();
        JTextField userEmailTextField = new JTextField(20);
        JLabel passwordLabel = new JLabel();
        JTextField passwordTextField = new JTextField(20);

        userEmailLabel.setText("Enter email");
        passwordLabel.setText("Enter password");
        JButton loginAuthenticateButton = new JButton("Login");
        
        loginPanel.add(userEmailLabel);
        loginPanel.add(userEmailTextField);
        loginPanel.add(passwordLabel);
        loginPanel.add(passwordTextField);
        loginPanel.add(loginAuthenticateButton);

        loginPanel.setLayout(new GridLayout(4,1));

        // define main menu panel
        JPanel mainMenuPanel = new JPanel();
        JLabel titleLabel = new JLabel();
        titleLabel.setText("Choose option");
        JButton viewProductListButton = new JButton("View product list");
        JButton addProductButton = new JButton("Add product");
        JButton removeProductButton = new JButton("Remove product from basket");
        JButton viewBasketButton = new JButton("View basket");
        JButton orderProductButton = new JButton("Order Product");
        JButton viewPastOrderButton = new JButton("View Past Order");
        JButton exitButton = new JButton("Exit");
        
        mainMenuPanel.setLayout(new GridLayout(4,2));
        mainMenuPanel.add(titleLabel);
        mainMenuPanel.add(viewProductListButton);
        mainMenuPanel.add(addProductButton);
        mainMenuPanel.add(removeProductButton);
        mainMenuPanel.add(viewBasketButton);
        mainMenuPanel.add(orderProductButton);
        mainMenuPanel.add(viewPastOrderButton);
        mainMenuPanel.add(exitButton);

        // define order panel
        JPanel orderPanel = new JPanel();
        JLabel resultLabel = new JLabel();
        JButton mainMenuButton = new JButton("Back to main menu");
        
        orderPanel.add(resultLabel);
        orderPanel.add(mainMenuButton);

        // define cardLayout
        JPanel cards = new JPanel(new CardLayout());
        cards.add(initPanel);
        cards.add(loginPanel);
        cards.add(mainMenuPanel);
        cards.add(orderPanel);

        // define button function
        loginButton.addActionListener(new ActionListener() { 
            @Override
            public void actionPerformed(ActionEvent e) { 
                CardLayout cl = (CardLayout) cards.getLayout();
                cl.next(cards);
            } 
        });

        loginAuthenticateButton.addActionListener(new ActionListener() { 
            @Override
            public void actionPerformed(ActionEvent e) { 
                String email = userEmailTextField.getText();
                String password = passwordTextField.getText();
                if(server.login(email, password)){
                    userEmail = email;
                    CardLayout cl = (CardLayout) cards.getLayout();
                    cl.next(cards);                   
                }
                else{
                    CardLayout cl = (CardLayout) cards.getLayout();
                    cl.previous(cards);
                }
            }
        });
        
        orderProductButton.addActionListener(new ActionListener() { 
            @Override
            public void actionPerformed(ActionEvent e) { 
                if (server.makeOrder(userEmail)){
                    resultLabel.setText("Order Succeed!");
                    CardLayout cl = (CardLayout) cards.getLayout();
                    cl.next(cards);                    
                }
                else{
                    resultLabel.setText("Order Failed!");
                    CardLayout cl = (CardLayout) cards.getLayout();
                    cl.next(cards);                   
                }

            } 
        });

        mainMenuButton.addActionListener(new ActionListener() { 
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

public class Client {
    // save logged in user
    static String currUserEmail;

    // return true if login success
    private static boolean login (Server server, Scanner sc){
        String email, password;

        // init console
        clearConsole();
        System.out.println("Login\n");

        // Input email
        System.out.println("Enter your Email : ");
        email = sc.next();     

        // Input password
        System.out.println("Enter your password : ");
        password = sc.next();  
        
        // save info and return result
        currUserEmail = email;
        return server.login(email, password);        
    }

    // return true if sign in success
    private static boolean signUp(Server server, Scanner sc){
        String email, password;

        // init console
        clearConsole();
        System.out.println("Sign Up\n");

        // Input email
        System.out.println("Enter your Email : ");
        email = sc.next();

        // Input password
        System.out.println("Enter your password : ");
        password = sc.next();      

        // return result
        return Server.signUp(email, password);
    }

    private static void getProductList(Server server, Scanner sc){
        /*
        When it sends userCode, product list should be printed by the method,
        and return nothing.
         */
        
        clearConsole();

        System.out.println("Product List\n");
        System.out.println(Server.getProductList());
        pressEnterToContinue(sc);
    }

    // print past order
    private static void getPastOrder(Server server, Scanner sc){
        clearConsole();

        ArrayList<Order> userOrder = server.getOrderListOfUser(currUserEmail);
        System.out.println(userOrder.subList(0, userOrder.size()-1));

        pressEnterToContinue(sc);
    }

    // print user basket
    private static void viewBasket(Server server, Scanner sc){
        clearConsole();

        System.out.println("Current Basket\n");
        System.out.println("Product : " + server.getOpenOrderOfUser(currUserEmail).getProductList());
        System.out.println("Discount Coupons : " + server.getOpenOrderOfUser(currUserEmail).getDiscountList() + "\n");
        pressEnterToContinue(sc);
    }

    // adds product to open basket
    private static boolean addProduct(Server server, Scanner sc){
        // clear console
        clearConsole();

        // get product info
        System.out.println("Please enter the product name to add : ");
        String productName = sc.next();
        System.out.println("Please enter how many you want to add to basket : ");
        int stock = Integer.parseInt(sc.next());

        // return add result
        if (server.addToBasket(currUserEmail, productName, stock)){
            System.out.println("Add success!");
            pressEnterToContinue(sc);
            return true;
        }
        else return false;
    }

    // removes products from basket
    private static boolean removeProduct(Server server, Scanner sc){

        clearConsole();

        // get product info
        System.out.println("Please enter the product name to remove : ");
        String productName = sc.next();
        System.out.println("Please enter how many you want to remove from basket : ");
        int stock = Integer.parseInt(sc.next());

        // return remove result
        if (server.removeFromBasket(currUserEmail, productName, stock)){
            System.out.println("Remove success!");
            pressEnterToContinue(sc);
            return true;
        }
        else return false;
    }

    // puts order
    private static boolean order(Server server, Scanner sc){

        clearConsole();

        // return order result
        if (server.makeOrder(currUserEmail)){
            System.out.println("Order made!");
            pressEnterToContinue(sc);
            return true;
        }
        else return false;
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
        System.out.println("1. View product list");
        System.out.println("2. Add product");
        System.out.println("3. Remove product from basket");
        System.out.println("4. View basket");
        System.out.println("5. Order Product");
        System.out.println("6. View Past Order");
        System.out.println("7. Exit");
    }

    public static void main(String[] args) {
        // initialize server for testing purpose
        Server server = new Server();
        Server.serverInit();

        boolean fin = false;
        boolean log = false;
        Scanner sc = new Scanner(System.in);

        // Login or Sign up
        do {
            clearConsole();
            System.out.println("Welcome to our service!");
            System.out.println("1. Login");
            System.out.println("2. Sign in");

            // error handling when input is not integer parseable
            try{
                // parse input
                int num = Integer.parseInt(sc.next());
                    
                // 1. Login
                if (num == 1){
                    fin = log = login(server, sc);
                // 2. Sign Up
                } else if(num == 2){
                    fin = signUp(server, sc);
                }
            // 3. Invalid Number     
            } catch(Exception e){
                System.out.println("Please enter a valid number!\n");
            }
                        
            // print error message if error occured
            if (!fin){
                System.out.println("Error occrued. Please retry.");
                pressEnterToContinue(sc);
            }
        } while(!(fin && log));

        // Login success -> use site.
        fin = false;
        while(!fin){
            // print main menu
            printMain();

            // detects error
            boolean error = true;
            // error handling when input is not integer parseable
            try{
                // parse input
                int num = Integer.parseInt(sc.next());

                //  1. View product List
                if (num == 1){   
                    getProductList(server, sc);

                // 2. Add Product
                }else if (num == 2){  
                    error = addProduct(server, sc);

                // 3. Remove product from basket
                }else if(num == 3){     
                    error = removeProduct(server, sc);

                }else if(num == 4){     // 4. View products in the basket
                    viewBasket(server, sc);

                }else if(num == 5){     // 5. Order products in the basket
                    error = order(server, sc);

                }else if(num == 6){     // 6. List past orders
                    getPastOrder(server, sc);

                }else if(num == 7){     // 7. Exit
                    clearConsole();
                    System.out.println("Thank you! See you next time.");
                    fin = true;
                }
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