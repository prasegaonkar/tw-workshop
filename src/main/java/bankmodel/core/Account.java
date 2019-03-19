package bankmodel.core;

import static bankmodel.core.TransactionType.DEPOSIT;
import static bankmodel.core.TransactionType.WITHDRAW;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

public class Account {
	@Getter
	private int balance;
	@Getter
	private List<Transaction> transactions = new ArrayList<>();
	private AccountReconciler reconciler = new AccountReconciler();

	public void deposit(int amount) {
		validateAmount(amount);
		transactions.add(new Transaction(DEPOSIT, amount));
		balance += amount;
		reconciler.validate(transactions, balance);
	}

	public void withdraw(int amount) {
		validateAmount(amount);
		if (balance < amount) {
			throw new InsufficientFundsException();
		}
		transactions.add(new Transaction(WITHDRAW, amount));
		balance -= amount;
		reconciler.validate(transactions, balance);
	}

	private void validateAmount(int amount) {
		if (amount <= 0) {
			throw new InvalidAmountException();
		}
	}
}
