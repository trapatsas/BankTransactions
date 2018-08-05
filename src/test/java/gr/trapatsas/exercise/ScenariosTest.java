package gr.trapatsas.exercise;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ScenariosTest {

    private static final Logger logger = LogManager.getRootLogger();

    @org.junit.jupiter.api.BeforeEach
    void setUp() {

    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
    }

    @Test
    @Disabled
    void randomScenarioTest() throws InterruptedException {

        int numberOfAccounts = 10;

        SavingsAccount[] accounts = new SavingsAccount[numberOfAccounts];
        Thread[] threads = new Thread[numberOfAccounts];

        for (int i = 0; i < numberOfAccounts; i++) {
            int randomAmount = ThreadLocalRandom.current().nextInt(0, 501);
            accounts[i] = new SavingsAccount(randomAmount);
            accounts[i].setName("Account #" + i);
            logger.info(String.format("Account %d has $%f", i, accounts[i].getBalance()));
        }

        for (int i = 0; i < numberOfAccounts; i++) {
            int randomAccount = i;
            while (randomAccount == i) {
                randomAccount = ThreadLocalRandom.current().nextInt(0, numberOfAccounts);
            }
            threads[i] = new Thread(new TransferRunnable(accounts[randomAccount], accounts[i], 100));
            logger.info(String.format("Account %d wants to transfer $%d from account %d", i, 100, randomAccount));
            threads[i].start();
        }

        Thread.sleep(6000); // Or wait an hour
        logger.info("");
        logger.info("Let's wait for an hour... .. .. .. .. ..");
        logger.info("");
        Thread.sleep(1000);

        Thread BossAccount = new Thread(() -> {
            for (int i = 0; i < numberOfAccounts; i++) {
                accounts[i].deposit(1000.00);
            }
        });

        BossAccount.start();
        BossAccount.join();

        Thread.sleep(2000);

        for (int i = 0; i < numberOfAccounts; i++) {
            logger.info(String.format("Account %d has *NEW* balance: $%f", i, accounts[i].getBalance()));
        }

    }

    @Test
    void blockScenario01Test() throws InterruptedException {

        int numberOfAccounts = 4;

        SavingsAccount[] accounts = new SavingsAccount[numberOfAccounts];
        Thread[] threads = new Thread[numberOfAccounts];


        accounts[0] = new SavingsAccount(482);
        accounts[0].setName("Account #" + 0);
        accounts[1] = new SavingsAccount(149);
        accounts[1].setName("Account #" + 1);
        accounts[2] = new SavingsAccount(347);
        accounts[2].setName("Account #" + 2);
        accounts[3] = new SavingsAccount(64);
        accounts[3].setName("Account #" + 3);


        threads[0] = new Thread(new TransferRunnable(accounts[2], accounts[0], 100));
        threads[1] = new Thread(new TransferRunnable(accounts[3], accounts[1], 100));
        threads[2] = new Thread(new TransferRunnable(accounts[3], accounts[2], 100));
        threads[3] = new Thread(new TransferRunnable(accounts[1], accounts[3], 100));

        threads[0].start();
        threads[1].start();
        threads[2].start();
        threads[3].start();


        Thread.sleep(5000); // Or wait an hour
        logger.info("");
        logger.info("Let's wait for an hour... .. .. .. .. ..");
        logger.info("");
        Thread.sleep(2000);

        Thread BossAccount = new Thread(() -> {
            for (int i = 0; i < numberOfAccounts; i++) {
                accounts[i].deposit(1000.00);
            }
        });

        BossAccount.start();
        BossAccount.join();

        Thread.sleep(2000);

        assertEquals(Thread.State.TERMINATED, threads[0].getState());
        assertEquals(Thread.State.TERMINATED, threads[1].getState());
        assertEquals(Thread.State.TERMINATED, threads[2].getState());
        assertEquals(Thread.State.TERMINATED, threads[3].getState());
        assertEquals(Thread.State.TERMINATED, BossAccount.getState());

        assertEquals(1582, accounts[0].getBalance(), 0.001);
        assertEquals(1149, accounts[1].getBalance(), 0.001);
        assertEquals(1347, accounts[2].getBalance(), 0.001);
        assertEquals(964, accounts[3].getBalance(), 0.001);

    }

    @Test
    void blockScenario02Test() throws InterruptedException {

        int numberOfAccounts = 4;

        SavingsAccount[] accounts = new SavingsAccount[numberOfAccounts];
        Thread[] threads = new Thread[numberOfAccounts];


        accounts[0] = new SavingsAccount(1);
        accounts[0].setName("Account #" + 0);
        accounts[1] = new SavingsAccount(2);
        accounts[1].setName("Account #" + 1);
        accounts[2] = new SavingsAccount(3);
        accounts[2].setName("Account #" + 2);
        accounts[3] = new SavingsAccount(4);
        accounts[3].setName("Account #" + 3);


        threads[0] = new Thread(new TransferRunnable(accounts[3], accounts[0], 100));
        threads[1] = new Thread(new TransferRunnable(accounts[3], accounts[1], 100));
        threads[2] = new Thread(new TransferRunnable(accounts[3], accounts[2], 100));
        threads[3] = new Thread(new TransferRunnable(accounts[1], accounts[3], 100));

        threads[0].start();
        threads[1].start();
        threads[2].start();
        threads[3].start();


        Thread.sleep(3000); // Or wait an hour
        logger.info("");
        logger.info("Let's wait for an hour... .. .. .. .. ..");
        logger.info("");
        Thread.sleep(2000);

        Thread BossAccount = new Thread(() -> {
            for (int i = 0; i < numberOfAccounts; i++) {
                accounts[i].deposit(1000.00);
            }
        });

        BossAccount.start();
        BossAccount.join();

        Thread.sleep(2000);

        assertEquals(Thread.State.TERMINATED, threads[0].getState());
        assertEquals(Thread.State.TERMINATED, threads[1].getState());
        assertEquals(Thread.State.TERMINATED, threads[2].getState());
        assertEquals(Thread.State.TERMINATED, threads[3].getState());
        assertEquals(Thread.State.TERMINATED, BossAccount.getState());

        assertEquals(1101, accounts[0].getBalance(), 0.001);
        assertEquals(1002, accounts[1].getBalance(), 0.001);
        assertEquals(1103, accounts[2].getBalance(), 0.001);
        assertEquals(804, accounts[3].getBalance(), 0.001);

    }

    @Test
    void blockScenarioDominoTest() throws InterruptedException {

        int numberOfAccounts = 100;

        SavingsAccount[] accounts = new SavingsAccount[numberOfAccounts];
        Thread[] threads = new Thread[numberOfAccounts];

        for (int i = 0; i < numberOfAccounts; i++) {
            accounts[i] = new SavingsAccount(0.5);
            accounts[i].setName("Account #" + i);
            logger.info(String.format("Account %d has $%f", i, accounts[i].getBalance()));
        }

        for (int i = 1; i < numberOfAccounts; i++) {
            threads[i] = new Thread(new TransferRunnable(accounts[i - 1], accounts[i], 100));
            logger.info(String.format("Account %d wants to transfer $%d from account %d", i, 100, i - 1));
            threads[i].start();
        }

        threads[0] = new Thread(new TransferRunnable(accounts[numberOfAccounts - 1], accounts[0], 100));
        logger.info(String.format("Account %d wants to transfer $%d from account %d", 0, 100, numberOfAccounts - 1));
        threads[0].start();

        Thread.sleep(6000); // Or wait an hour
        logger.info("");
        logger.info("Let's wait for an hour... .. .. .. .. ..");
        logger.info("");
        Thread.sleep(1000);

        int randomAccount = ThreadLocalRandom.current().nextInt(0, numberOfAccounts);

        Thread BossAccount = new Thread(() -> {
            accounts[randomAccount].deposit(1000.00);
        });

        BossAccount.start();
        BossAccount.join();
        Thread.sleep(5000); // Wait for the deposit

        assertEquals(Thread.State.TERMINATED, BossAccount.getState());

        for (int i = 0; i < numberOfAccounts; i++) {
            assertEquals(Thread.State.TERMINATED, threads[i].getState());
            if (i == randomAccount) {
                assertEquals(1000.5, accounts[i].getBalance(), 0.001);
            } else {
                assertEquals(0.5, accounts[i].getBalance(), 0.001);
            }
        }
    }
}
