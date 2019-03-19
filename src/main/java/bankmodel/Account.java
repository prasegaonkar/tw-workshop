package bankmodel;

import static bankmodel.XnType.DEPOSIT;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Value;

@Getter
public class Account {
	private int openingBalance;
	private int currentBalance;
	private List<Xn> transactions = new ArrayList<>();
	private Object lock = new Object();
	private AccountReconciler reconciler;

	public Account(int openingBalance) {
		validateAmount(openingBalance);
		this.openingBalance = openingBalance;
		this.currentBalance = openingBalance;
		transactions.add(new Xn(DEPOSIT, openingBalance));
		reconciler = new AccountReconciler();
	}

	public void deposit(int amount) {
		validateAmount(amount);
		synchronized (lock) {
			transactions.add(new Xn(DEPOSIT, amount));
			currentBalance += amount;
		}
		validateReconciliation();
	}

	public void withdraw(int amount) {
		validateAmount(amount);
		synchronized (lock) {
			transactions.add(new Xn(XnType.WITHDRAW, amount));
			currentBalance -= amount;
		}
		validateReconciliation();
	}

	private void validateReconciliation() {
		boolean isReconciled = reconciler.validate(transactions, currentBalance);
		if (!isReconciled) {
			throw new AccountNotBeongReconciled();
		}
	}

	private void validateAmount(int openingBalance) {
		if (openingBalance <= 0) {
			throw new InvalidAmount();
		}
	}

}

class AccountNotBeongReconciled extends RuntimeException {

	private static final long serialVersionUID = 1L;

}

class InvalidAmount extends RuntimeException {

	private static final long serialVersionUID = 1L;

}

@Value
class Xn {
	private final XnType type;
	private final int amount;
}

enum XnType {
	DEPOSIT, WITHDRAW;
}
