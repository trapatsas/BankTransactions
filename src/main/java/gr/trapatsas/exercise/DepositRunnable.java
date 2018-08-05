package gr.trapatsas.exercise;

/**
 * A deposit runnable makes deposits to a bank account.
 */

public class DepositRunnable implements Runnable {
    private SavingsAccount account;
    private double amount;


    /**
     * Constructs a deposit runnable.
     *
     * @param account       the account into which to deposit money
     * @param depositAmount the amount to deposit
     */
    public DepositRunnable(SavingsAccount account, double depositAmount) {
        this.account = account;
        amount = depositAmount;

    }

    public void run() {


        account.deposit(amount);

    }
}

