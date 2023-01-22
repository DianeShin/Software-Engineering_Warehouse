package classes;
public class Product {
    Supplier supplier;

    // for client, stock means how many products the user wants to order.
    // for server, stock means how many products exist in the warehouse.
    int stock;
    
    int price;
    String name;

    // Constructor
    public Product(String name, int price, int stock, Supplier supplier){
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.supplier = supplier;
    }

    public String getName(){
        return name;
    }
    
    public int getPrice(){
        return price;
    }

    public int getStock(){
        return stock;
    }

    public Supplier getSupplier(){
        return supplier;
    }

    public void setPrice(int price){
        this.price = price;
    }

    public void setStock(int stock){
        this.stock = stock;
    }

    public void setSupplier(Supplier supplier){
        this.supplier = supplier;
    }

    public void restock(){
        System.out.println("Order made for supplier " + supplier.getName() + ".");
        stock += 7;
    }

    @Override
    public String toString() {
        return name + " : " + Integer.toString(stock);
    }

    @Override
    public boolean equals(Object o) {
 
        // If the object is compared with itself then return true 
        if (o == this) {
            return true;
        }
 
        /* Check if o is an instance of Complex or not
          "null instanceof [type]" also returns false */
        if (!(o instanceof Product)) {
            return false;
        }
         
        // typecast o to Complex so that we can compare data members
        Product castedO = (Product) o;
         
        // Compare the data members and return accordingly
        return this.name.equals(castedO.name);
    }
}