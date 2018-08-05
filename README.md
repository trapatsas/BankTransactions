Landoop Coding Exercise:
========================

A savings account object holds a non negative balance, and provides deposit(k) and withdraw(k) methods, where deposit(k) adds k to the balance,and
withdraw(k) subtracts k , if the balance is at least k , and otherwise blocks until the balance becomes k or greater.

1. Implement this savings account.

2. Now suppose there are two kinds of withdrawals: ordinary and preferred. Devise an implementation that ensures that no ordinary withdrawal occurs if there is a preferred withdrawal waiting to occur.

3. Now let us add a transfer() method that transfers a sum from one account to another:

```scala
def transfer(k: Int, reserve: Account): Unit = {
  lock.lock()
  try {
    reserve.withdraw(k)
    deposit(k)
  }
  finally {
    lock.unlock();
  }
}
```

We are given a set of 10 accounts, whose balances are unknown. At 1:00, each of n threads tries to transfer $100 from another account into its own account.
At 2:00, a Boss thread deposits $1000 to each account. Is every transfer method called at 1:00 certain to return?

Solution:
---------

1. The savings account is implemented in the `class SavingsAccount`.

2. Method `withdraw` inside `class SavingsAccount` contains the below implementation for this specification:

    ```java
        void withdraw(boolean preferred, double amount) throws InterruptedException {
            transactionLock.lock();
            try {
                if (preferred) {
                    preferredWaiting++;
                    while (balance < amount) {
                        sufficientFundsPriorityCondition.await();
                    }
                    preferredWaiting--;
                    balance -= amount;
                    notifyNextThread();
                } else {
                    while (balance < amount) {
                        sufficientFundsCondition.await();
                    }
                    balance -= amount;
                    notifyNextThread();
                }
            } finally {
                transactionLock.unlock();
            }
        }
        
        private void notifyNextThread() {
            if (preferredWaiting == 0) {
                sufficientFundsCondition.signal();
            } else {
                sufficientFundsPriorityCondition.signal();
            }
        }
    ```

    So, there are 2 conditions, one for ordinary threads and one for preferred ones. 
    If the account contains less than the amount asked, both ordinary and preferred threads await for the condition of satisfaction.
    When a new deposit is made all threads in preferred condition are being notified and if there is none, then the threads in the ordinary condition are being notified.

3. Test `randomScenarioTest` in `ScenariosTest` class is a simulation of the scenario asked. 
The rest tests in the same file prove that since the amount deposited is greater than the sum of all amounts that is possible to be withdrawn from a specific account, then *every transfer is certain to return.*

