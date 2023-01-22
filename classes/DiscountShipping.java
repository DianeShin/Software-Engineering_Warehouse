package classes;

public class DiscountShipping implements Discount {
    int DISCOUNT_CODE = 10000;
    int discountAmount;

    public int getDiscountCode(){
        return DISCOUNT_CODE;
    }

    // percentage
    public DiscountShipping(int discountAmount){
        this.discountAmount = discountAmount;
    }

    public int calculateShipping(int shippingFee){
        return shippingFee*discountAmount/100; 
    }

    @Override
    public String toString(){
        return "Discount shipping fee by " + Integer.toString(discountAmount) + "%";
    }
}
