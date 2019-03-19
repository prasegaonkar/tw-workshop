package bankmodel.core;

import static bankmodel.core.XnType.DEPOSIT;
import static bankmodel.core.XnType.WITHDRAW;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

public class Account {
	@Getter
	private int balance;
	@Getter
	private List<Xn> transactions = new ArrayList<>();
	private AccountReconciler reconciler = new AccountReconciler();

	public void deposit(int amount) {
		validateAmount(amount);
		transactions.add(new Xn(DEPOSIT, amount));
		balance += amount;
		reconciler.validate(transactions, balance);
	}

	public void withdraw(int amount) {
		validateAmount(amount);
		if (balance < amount) {
			throw new InsufficientFundsException();
		}
		transactions.add(new Xn(WITHDRAW, amount));
		balance -= amount;
		reconciler.validate(transactions, balance);
	}

	private void validateAmount(int amount) {
		if (amount <= 0) {
			throw new InvalidAmountException();
		}
	}
}
