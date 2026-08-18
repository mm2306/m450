package grundlagen;

public class Preisberechnung {

    public static double calculatePrice(double baseprice, double specialprice, double extraprice, int extras, double discount) {
        double addon_discount;
        double result;
        
        if (extras >= 5) 
            addon_discount = 15;
        else if (extras >= 3)
            addon_discount = 10;
        else 
            addon_discount = 0;
        
        if (discount > addon_discount)
            addon_discount = discount;
        
        result = baseprice/100.0 * (100-discount) + specialprice
                + extraprice/100.0 * (100-addon_discount);
        
        return result;
    }

    public static void main(String[] args) {
        testCalculatePrice();
    }

    public static boolean testCalculatePrice() {
        double price = calculatePrice(20000.0, 1000.0, 500.0, 1, 0.0);
        boolean test_ok = price == 21500.0;
        System.out.println("Test 1 Result: " + price + " | Passed: " + test_ok);

        price = calculatePrice(20000.0, 1000.0, 1000.0, 3, 0.0);
        boolean test2_ok = price == 21900.0;
        System.out.println("Test 2 Result: " + price + " | Passed: " + test2_ok);

        price = calculatePrice(20000.0, 1000.0, 1000.0, 6, 0.0);
        boolean test3_ok = price == 21850.0;
        System.out.println("Test 3 Result: " + price + " | Passed: " + test3_ok);

        return test_ok && test2_ok && test3_ok;
    }
}
