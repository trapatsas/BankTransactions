package gr.trapatsas.exercise;

/**
 * A deposit runnable makes periodic deposits to a bank account.
 */

public class TransferRunnable implements Runnable {
    private SavingsAccount accountFrom;
    private SavingsAccount accountTo;
    private double amount;
    private boolean isPreferred;

    /**
     * Constructs a deposit runnable.
     *
     * @param toAccount   the account into which to deposit money
     * @param fromAccount the account from which to withdraw money
     * @param transferAmount    the amount to withdraw and deposit
     */
    public TransferRunnable(SavingsAccount fromAccount, SavingsAccount toAccount, double transferAmount) {
        accountFrom = fromAccount;
        accountTo = toAccount;
        amount = transferAmount;
        isPreferred = false;
    }

    public TransferRunnable(SavingsAccount fromAccount, SavingsAccount toAccount, double transferAmount, boolean preferred) {
        accountFrom = fromAccount;
        accountTo = toAccount;
        amount = transferAmount;
        isPreferred = preferred;
    }

    public void run() {
        try {
            accountTo.transfer(accountFrom, amount, isPreferred);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

