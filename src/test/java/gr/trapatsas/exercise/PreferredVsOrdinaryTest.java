package gr.trapatsas.exercise;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PreferredVsOrdinaryTest {

    @org.junit.jupiter.api.BeforeEach
    void setUp() {

    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
    }

    @Test
    void withdrawTestNoFunds() throws InterruptedException {
        SavingsAccount ac01 = new SavingsAccount(50.00);

        Thread depositBig = new Thread(() -> {
            ac01.deposit(40.00);
        });

        Thread ordinary01 = new Thread(() -> {
            try {
                ac01.withdraw(false, 10.00);
            } catch (InterruptedException ignored) {
            }
        });

        Thread ordinary02 = new Thread(() -> {
            try {
                ac01.withdraw(false, 55.00);
            } catch (InterruptedException ignored) {
            }
        });

        Thread priority01 = new Thread(() -> {
            try {
                ac01.withdraw(true, 70.00);
            } catch (InterruptedException ignored) {
            }
        });

        ordinary01.start();
        ordinary02.start();

        Thread.sleep(5000);

        assertEquals(Thread.State.WAITING, ordinary02.getState());
        assertEquals(40.00, ac01.getBalance(), 0.001);

        priority01.start();
        depositBig.start();

        Thread.sleep(5000);

        assertEquals(Thread.State.WAITING, ordinary02.getState());
        assertEquals(10.00, ac01.getBalance(), 0.001);

        ordinary02.interrupt();
    }

    @Test
    void withdrawTestManyOrdinary() throws InterruptedException {
        SavingsAccount ac01 = new SavingsAccount(0.00);

        Thread depositBig = new Thread(() -> {
            ac01.deposit(10.00);
        });

        Thread priority01 = new Thread(() -> {
            try {
                ac01.withdraw(true, 6.5);
            } catch (InterruptedException ignored) {
            }
        });

        int numberOfThreads = 20;

        Thread[] threads = new Thread[numberOfThreads];

        for (int i = 1; i < numberOfThreads; i++) {
            threads[i] = new Thread(new WithdrawRunnable(ac01, i));
            threads[i].start();
        }

        Thread.sleep(2000);

        priority01.start();

        Thread.sleep(2000);

        depositBig.start();

        Thread.sleep(5000);

        assertTrue(ac01.getBalance() <= 2.5);

        for (int i = 3; i < numberOfThreads; i++) {
            assertEquals(Thread.State.WAITING, threads[i].getState());
        }

    }

    /**
     * Should ONLY be run using the notifyRestThreads method of SavingsAccount.class
     * @throws InterruptedException
     */
    @Disabled
    @Test
    void withdrawTestZeroBalance() throws InterruptedException {
        SavingsAccount ac01 = new SavingsAccount(0.00);

        Thread depositBig = new Thread(() -> {
            ac01.deposit(10.00);
        });

        int numberOfThreads = 30;

        Thread[] threads = new Thread[numberOfThreads];

        for (int i = 0; i < numberOfThreads; i++) {
            threads[i] = new Thread(new WithdrawRunnable(ac01, 11));
            threads[i].start();
        }

        Thread.sleep(2000);

        Thread getNine = new Thread(() -> {
            try {
                ac01.withdraw(false,9.00);
            } catch (InterruptedException ignored) {
            }
        });

        Thread getOne = new Thread(() -> {
            try {
                ac01.withdraw(false,1.00);
            } catch (InterruptedException ignored) {
            }
        });

        getNine.start();
        getOne.start();

        Thread.sleep(2000);

        depositBig.start();

        Thread.sleep(5000);

        assertEquals(0.00, ac01.getBalance(), 0.001);

        for (int i = 0; i < numberOfThreads; i++) {
            assertEquals(Thread.State.WAITING, threads[i].getState());
        }

    }
}
