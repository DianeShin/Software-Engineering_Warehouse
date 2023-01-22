package classes;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Order {
    // true for open, false for closed
    boolean openFlag = true;

    // contains list of products to be ordered
    ArrayList<Product> productList;

    // contains list of discounts to be applied
    ArrayList<Discount> discountList;

    // contains DateTime of when the order was made
    LocalDateTime orderDateTime;

    // constructor
    public Order(){
        productList = new ArrayList<Product>();
        discountList = new ArrayList<Discount>();
        orderDateTime = null;
    }

    // methods
    public boolean getOpenFlag(){
        return openFlag;
    }

    public ArrayList<Product> getProductList(){
        return productList;
    }

    public ArrayList<Discount> getDiscountList(){
        return discountList;
    }

    public LocalDateTime getOrderDateTime(){
        return orderDateTime;
    }

    public void addDiscount(Discount discount){
        discountList.add(discount);
    }

    public void removeDiscount(Discount discount){
        discountList.remove(discount);
    }

    // Adds a product to the order.
    public void addToOrder(Product product){
        productList.add(product);
    }

    // Removes the given product from the order
    public void removeFromOrder(Product product){
        // if removing entire stock, then remove product from list
        if (product.getStock() == 0) productList.remove(product);
        // if removing a part of stock, substitute old product to new product
        // this method is possible because equals is overriden.
        else{
            productList.remove(product);
            productList.add(product);
        }
    }

    // Closes the order and sets the time of purchase.
    public void closeOrder(){
        openFlag = false;
        orderDateTime = LocalDateTime.now();
    }

    @Override
    public String toString() {
        if (orderDateTime == null) return "Order date/time : open order | " + "Product list : " + productList.toString();
        else return "Order date/time : " + orderDateTime.toString() + " | " + "Product list : " + productList.toString();
    }
}
