package gr.trapatsas.exercise;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SavingsAccountTest {

    @org.junit.jupiter.api.BeforeEach
    void setUp() {

    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
    }

    @Test
    void withdrawTestNoFunds() throws InterruptedException {
        SavingsAccount ac01 = new SavingsAccount(200.82);

        Thread mainRunner = new Thread(() -> {
            try {
                ac01.withdraw(false, 200.83);
            } catch (InterruptedException ignored) { }
        });

        mainRunner.start();

        Thread.sleep(5000);

        assertEquals(Thread.State.WAITING, mainRunner.getState());

        mainRunner.interrupt();
    }

    @Test
    void withdrawTestNormal() throws InterruptedException {
        SavingsAccount ac01 = new SavingsAccount(200.82);
        ac01.withdraw(false, 100.82);
        assertEquals(100.00, ac01.getBalance(), 0.001);
    }

    @Test
    void withdrawTestPreferred() throws InterruptedException {
        SavingsAccount ac01 = new SavingsAccount(200.82);
        ac01.withdraw(true, 100.82);
        assertEquals(100.00, ac01.getBalance(), 0.001);
    }

    @Test
    void depositTest() {
        SavingsAccount ac01 = new SavingsAccount(200.82);
        ac01.deposit(99.18);
        assertEquals(300.00, ac01.getBalance(), 0.001);

    }

    @Test
    void transferNormal() throws InterruptedException {
        SavingsAccount ac01 = new SavingsAccount(200.82);
        SavingsAccount ac02 = new SavingsAccount(199.18);
        ac01.transfer(ac02, 99.18, false);
        assertEquals(300.00, ac01.getBalance(), 0.001);
        assertEquals(100.00, ac02.getBalance(), 0.001);
    }

    @Test
    void transferPriority() throws InterruptedException {
        SavingsAccount ac01 = new SavingsAccount(200.82);
        SavingsAccount ac02 = new SavingsAccount(199.18);
        ac01.transfer(ac02, 99.18, true);
        assertEquals(300.00, ac01.getBalance(), 0.001);
        assertEquals(100.00, ac02.getBalance(), 0.001);
    }

}