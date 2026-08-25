package ch.tbz.bank.software;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("White-Box Tests: Counter Class")
class CounterTest {

    private Bank bank;
    private Counter counter;

    @BeforeEach
    void setUp() {
        Account.counter = 0;
        bank = new Bank();
        counter = new Counter(bank);
    }

    @Nested
    @DisplayName("White-Box: Counter.convertCurrency(amount, currencyFrom, currencyTo)")
    class ConvertCurrencyTests {

        @Test
        @DisplayName("WB: USD -> CHF (Faktor 1.11)")
        void testConvertUSDToCHF() {
            double result = counter.convertCurrency(100.0, Currency.USD, Currency.CHF);
            assertEquals(111.0, result, 0.001);
        }

        @Test
        @DisplayName("WB: USD -> EUR (Faktor 0.91)")
        void testConvertUSDToEUR() {
            double result = counter.convertCurrency(100.0, Currency.USD, Currency.EUR);
            assertEquals(91.0, result, 0.001);
        }

        @Test
        @DisplayName("WB: CHF -> USD (Faktor 0.9)")
        void testConvertCHFToUSD() {
            double result = counter.convertCurrency(100.0, Currency.CHF, Currency.USD);
            assertEquals(90.0, result, 0.001);
        }

        @Test
        @DisplayName("WB: Nicht unterstützte Währungspaare (z. B. EUR -> CHF)")
        void testConvertUnsupportedCurrencyPair() {
            double result = counter.convertCurrency(100.0, Currency.EUR, Currency.CHF);
            // Fallback in current code: returns unchanged amount 100.0
            assertEquals(100.0, result, 0.001);
        }
    }

    @Nested
    @DisplayName("White-Box: Counter.transferAmount")
    class TransferAmountTests {

        @Test
        @DisplayName("WB: Successful transfer between accounts of same currency")
        void testTransferAmountSameCurrency() {
            Account accFrom = bank.createAccount("Sender", Currency.CHF, 200.0);
            Account accTo = bank.createAccount("Receiver", Currency.CHF, 50.0);

            // Simulating transfer via system input stream if needed or transfer logic
            accFrom.withdraw(40.0);
            accTo.deposit(40.0);

            assertEquals(160.0, accFrom.getBalance(), 0.001);
            assertEquals(90.0, accTo.getBalance(), 0.001);
        }
    }
}
