package gr.trapatsas.exercise;

/**
 * A withdraw runnable makes periodic withdrawals from a bank account.
 */

public class WithdrawRunnable implements Runnable {
    private SavingsAccount account;
    private double amount;
    private boolean isPreferred;

    /**
     Constructs a withdraw runnable.
     @param account the account from which to withdraw money
     @param withdrawAmount the amount to withdraw
     */
    public WithdrawRunnable(SavingsAccount account, double withdrawAmount, boolean preferred) {
        this.account = account;
        amount = withdrawAmount;
        isPreferred = preferred;
    }

    public WithdrawRunnable(SavingsAccount anAccount, double anAmount) {
        account = anAccount;
        amount = anAmount;
        isPreferred = false;
    }

    public void run() {
        try {
            account.withdraw(isPreferred, amount);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

