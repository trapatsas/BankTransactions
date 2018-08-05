package gr.trapatsas.exercise;

/**
 * A bank account has a balance that can be changed by
 * deposits and withdrawals.
 */

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class SavingsAccount {
    private double balance;
    private String name;
    private int preferredWaiting;
    private Lock transactionLock = new ReentrantLock();
    private Lock lock = new ReentrantLock();
    private final Condition sufficientFundsPriorityCondition = transactionLock.newCondition();
    private final Condition sufficientFundsCondition = transactionLock.newCondition();

    private static final Logger logger = LogManager.getLogger(SavingsAccount.class);

    SavingsAccount(double initialBalance) {
        setBalance(initialBalance);
    }

    SavingsAccount() {
        setBalance(0.00);
    }

    void withdraw(boolean preferred, double amount) throws InterruptedException {
        transactionLock.lock();
        try {
            if (preferred) {
                preferredWaiting++;
                while (balance < amount) {
                    logger.info(String.format("[%s] +1 in priority queue!", name));
                    sufficientFundsPriorityCondition.await();
                }
                preferredWaiting--;
                logger.info(String.format("[%s] -1 in priority queue!", name));
                balance -= amount;
                logger.info(String.format("[%s] Balance (priority) decreased by %f (Total: %f)", name, amount, balance));
                notifyNextThread();
            } else {
                while (balance < amount) {
                    logger.info(String.format("[%s] +1 in ordinary queue!", name));
                    sufficientFundsCondition.await();
                }
                balance -= amount;
                logger.info(String.format("[%s] -1 in ordinary queue!", name));
                logger.info(String.format("[%s] Balance (ordinary) decreased by %f (Total: %f)", name, amount, balance));
                notifyNextThread();
            }
        } finally {
            transactionLock.unlock();
        }
    }

    /**
     * @param amount
     */
    void deposit(double amount) {
        transactionLock.lock();
        try {
            balance += amount;
            logger.info(String.format("[%s] Balance increased by %f (Total: %f)", name, amount, balance));
            notifyNextThread();
        } finally {
            transactionLock.unlock();
        }
    }

    /**
     * Select a thread and notify it
     */
    private void notifyNextThread() {
        if (preferredWaiting == 0) {
            sufficientFundsCondition.signal();
        } else {
            sufficientFundsPriorityCondition.signal();
        }
    }

    /**
     * Notify all threads waiting for the transactionLock.
     */
    private void notifyRestThreads() {
        if (preferredWaiting == 0) {
            sufficientFundsCondition.signalAll();
        } else {
            sufficientFundsPriorityCondition.signalAll();
        }
    }

    void transfer(SavingsAccount from, double amount, boolean preferred) throws InterruptedException {
        lock.lock();
        try {
            logger.info(String.format("Transfer started from %s to %s [%f]", from.getName(), name, amount));
            from.withdraw(preferred, amount);
            deposit(amount);
            logger.info(String.format("Transfer finished from %s to %s [%f]", from.getName(), name, amount));
        } finally {
            lock.unlock();
        }
    }

    double getBalance() {
        return balance;
    }

    private void setBalance(double balance) {
        this.balance = balance;
    }

    private String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }
}
