package grundlagen;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PreisberechnungTest {

    private static final double DELTA = 0.001;

    @Test
    @DisplayName("Kein Zubehör-Rabatt bei weniger als 3 Extras")
    void testCalculatePriceNoAddonDiscount() {
        // baseprice: 20000, specialprice: 1000, extraprice: 500, extras: 1, discount: 0
        // expected: 20000 + 1000 + 500 = 21500.0
        double result = Preisberechnung.calculatePrice(20000.0, 1000.0, 500.0, 1, 0.0);
        assertEquals(21500.0, result, DELTA);
    }

    @Test
    @DisplayName("10% Zubehör-Rabatt bei 3 bis 4 Extras")
    void testCalculatePriceTenPercentAddonDiscount() {
        // baseprice: 20000, specialprice: 1000, extraprice: 1000, extras: 3, discount: 0
        // extraprice mit 10% Rabatt = 900
        // expected: 20000 + 1000 + 900 = 21900.0
        double result = Preisberechnung.calculatePrice(20000.0, 1000.0, 1000.0, 3, 0.0);
        assertEquals(21900.0, result, DELTA);
    }

    @Test
    @DisplayName("15% Zubehör-Rabatt bei 5 oder mehr Extras")
    void testCalculatePriceFifteenPercentAddonDiscount() {
        // baseprice: 20000, specialprice: 1000, extraprice: 1000, extras: 6, discount: 0
        // extraprice mit 15% Rabatt = 850
        // expected: 20000 + 1000 + 850 = 21850.0
        double result = Preisberechnung.calculatePrice(20000.0, 1000.0, 1000.0, 6, 0.0);
        assertEquals(21850.0, result, DELTA);
    }

    @Test
    @DisplayName("Händlerrabatt übersteigt Zubehör-Rabatt")
    void testCalculatePriceHigherDealerDiscount() {
        // baseprice: 20000, specialprice: 1000, extraprice: 1000, extras: 1 (addon=0%), discount: 20%
        // baseprice mit 20% = 16000, extraprice mit 20% = 800
        // expected: 16000 + 1000 + 800 = 17800.0
        double result = Preisberechnung.calculatePrice(20000.0, 1000.0, 1000.0, 1, 20.0);
        assertEquals(17800.0, result, DELTA);
    }

    @Test
    @DisplayName("Grenzwerttest bei genau 2, 3, 4 und 5 Extras")
    void testBoundaryValuesForExtras() {
        // 2 Extras -> 0% Zubehör-Rabatt
        assertEquals(22000.0, Preisberechnung.calculatePrice(20000.0, 1000.0, 1000.0, 2, 0.0), DELTA);

        // 3 Extras -> 10% Zubehör-Rabatt
        assertEquals(21900.0, Preisberechnung.calculatePrice(20000.0, 1000.0, 1000.0, 3, 0.0), DELTA);

        // 4 Extras -> 10% Zubehör-Rabatt
        assertEquals(21900.0, Preisberechnung.calculatePrice(20000.0, 1000.0, 1000.0, 4, 0.0), DELTA);

        // 5 Extras -> 15% Zubehör-Rabatt
        assertEquals(21850.0, Preisberechnung.calculatePrice(20000.0, 1000.0, 1000.0, 5, 0.0), DELTA);
    }
}
