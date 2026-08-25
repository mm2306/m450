package ch.tbz.bank.software;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Black-Box Tests: Bank Software (BB-01 bis BB-08)")
class BankSoftwareBlackBoxTest {

    private Bank bank;

    @BeforeEach
    void setUp() {
        Account.counter = 0;
        bank = new Bank();
    }

    @Test
    @DisplayName("BB-01: Neuanlage eines Kontos (Name: Muster, Währung: CHF)")
    void testBB01_CreateAccount() {
        Account account = bank.createAccount("Muster", Currency.CHF, 0.00);

        assertNotNull(account, "Konto sollte angelegt werden");
        assertEquals(1, account.getId(), "Erstes Konto sollte ID 1 haben");
        assertEquals("Muster", account.getUserLastName(), "Nachname sollte Muster sein");
        assertEquals(Currency.CHF, account.getCurrency(), "Währung sollte CHF sein");
        assertEquals(0.00, account.getBalance(), 0.001, "Startguthaben sollte 0.00 CHF sein");
        assertEquals(1, bank.getNumberOfAccounts(), "Bank sollte genau 1 Konto enthalten");
    }

    @Test
    @DisplayName("BB-02: Geldeinzahlung - Gültiger Betrag (150.00 CHF)")
    void testBB02_DepositValidAmount() {
        Account account = bank.createAccount("Muster", Currency.CHF, 0.00);
        account.deposit(150.00);

        assertEquals(150.00, account.getBalance(), 0.001, "Kontostand sollte auf 150.00 CHF steigen");
    }

    @Test
    @DisplayName("BB-03: Geldeinzahlung - Ungültiger Betrag / Text ('abc') via Counter")
    void testBB03_DepositInvalidTextAmount() {
        Account account = bank.createAccount("Muster", Currency.CHF, 100.00);
        double initialBalance = account.getBalance();

        // Simulate user entering invalid text "abc" then valid amount "50"
        String input = "abc\n50\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();

        InputStream sysIn = System.in;
        PrintStream sysOut = System.out;
        try {
            System.setIn(in);
            System.setOut(new PrintStream(outStream));

            Counter counter = new Counter(bank);
            // Simulate calling deposit indirectly or directly handling exceptions
            try {
                java.lang.reflect.Method depositMethod = Counter.class.getDeclaredMethod("deposit", Account.class);
                depositMethod.setAccessible(true);
                depositMethod.invoke(counter, account);
            } catch (Exception e) {
                // Method reflection execution
            }

            String output = outStream.toString();
            assertTrue(output.contains("! Ungültige Eingabe, bitte nochmals!"), "Sollte Ungültige Eingabe-Meldung anzeigen");
            assertEquals(150.00, account.getBalance(), 0.001, "Nach korrigierter Eingabe sollte Guthaben 150.00 sein");
        } finally {
            System.setIn(sysIn);
            System.setOut(sysOut);
        }
    }

    @Test
    @DisplayName("BB-04: Geldabhebung - Guthaben ausreichend (50.00 bei 150.00 CHF)")
    void testBB04_WithdrawSufficientBalance() {
        Account account = bank.createAccount("Muster", Currency.CHF, 150.00);
        boolean success = account.withdraw(50.00);

        assertTrue(success, "Abhebung von 50.00 CHF sollte erfolgreich sein");
        assertEquals(100.00, account.getBalance(), 0.001, "Neuer Kontostand sollte 100.00 CHF sein");
    }

    @Test
    @DisplayName("BB-05: Geldabhebung - Guthaben unzureichend (200.00 bei 100.00 CHF)")
    void testBB05_WithdrawInsufficientBalance() {
        Account account = bank.createAccount("Muster", Currency.CHF, 100.00);
        boolean success = account.withdraw(200.00);

        assertFalse(success, "Abhebung sollte fehlschlagen bei unzureichendem Guthaben");
        assertEquals(100.00, account.getBalance(), 0.001, "Kontostand sollte unverändert bei 100.00 CHF bleiben");
    }

    @Test
    @DisplayName("BB-06: Kontoüberweisung - Gleiche Währung (Von Konto 1 zu Konto 2, Betrag 40.00)")
    void testBB06_TransferSameCurrency() {
        Account acc1 = bank.createAccount("Muster", Currency.CHF, 150.00);
        Account acc2 = bank.createAccount("Meier", Currency.CHF, 0.00);

        // Execute transfer logic
        boolean withdrawSuccess = acc1.withdraw(40.00);
        assertTrue(withdrawSuccess);
        acc2.deposit(40.00);

        assertEquals(110.00, acc1.getBalance(), 0.001, "Konto 1 sollte neu 110.00 CHF haben");
        assertEquals(40.00, acc2.getBalance(), 0.001, "Konto 2 sollte neu 40.00 CHF haben");
    }

    @Test
    @DisplayName("BB-07: Überweisung auf eigenes Konto (Zielkonto == Quellkonto)")
    void testBB07_TransferToSameAccount() {
        Account acc1 = bank.createAccount("Muster", Currency.CHF, 100.00);

        String input = "1\n"; // Inputting same account number
        InputStream in = new ByteArrayInputStream(input.getBytes());
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();

        InputStream sysIn = System.in;
        PrintStream sysOut = System.out;
        try {
            System.setIn(in);
            System.setOut(new PrintStream(outStream));

            Counter counter = new Counter(bank);
            try {
                java.lang.reflect.Method transferMethod = Counter.class.getDeclaredMethod("transfer", Account.class);
                transferMethod.setAccessible(true);
                transferMethod.invoke(counter, acc1);
            } catch (Exception e) {
                // Method invocation
            }

            String output = outStream.toString();
            assertTrue(output.contains("! Bitte ein anderes Konto als das momentane Konto auswählen!"),
                    "Fehlermeldung für Überweisung auf eigenes Konto muss ausgegeben werden");
            assertEquals(100.00, acc1.getBalance(), 0.001, "Kontostand darf nicht verändert werden");
        } finally {
            System.setIn(sysIn);
            System.setOut(sysOut);
        }
    }

    @Test
    @DisplayName("BB-08: Wechselkursabfrage (CHF zu USD)")
    void testBB08_ExchangeRateQuery() {
        Counter counter = new Counter(bank);
        double converted = counter.convertCurrency(1.0, Currency.CHF, Currency.USD);

        assertEquals(0.9, converted, 0.001, "1 CHF sollte zu 0.9 USD umgerechnet werden");
    }
}
