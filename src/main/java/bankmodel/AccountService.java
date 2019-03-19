package bankmodel;

import java.util.List;

import bankmodel.core.Account;
import bankmodel.core.Xn;

public class AccountService {
	private final Account account;

	public AccountService(Account account) {
		this.account = account;
	}

	public void deposit(int amount) {
		synchronized (account) {
			account.deposit(amount);
		}
	}

	public void withdraw(int amount) {
		synchronized (account) {
			account.withdraw(amount);
		}
	}

	public int getBalance() {
		return account.getBalance();
	}

	public List<Xn> getTransactions() {
		return account.getTransactions();
	}

}
