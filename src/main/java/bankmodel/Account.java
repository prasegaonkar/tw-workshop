package bankmodel;

import static bankmodel.XnType.DEPOSIT;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Value;

@Getter
public class Account {
	private int balance;
	private List<Xn> transactions = new ArrayList<>();
	private Object lock = new Object();
	private AccountReconciler reconciler = new AccountReconciler();

	public void deposit(int amount) {
		validateAmount(amount);
		synchronized (lock) {
			transactions.add(new Xn(DEPOSIT, amount));
			balance += amount;
			reconciler.validate(transactions, balance);
		}
	}

	public void withdraw(int amount) {
		validateAmount(amount);
		synchronized (lock) {
			if (balance < amount) {
				throw new InsufficientFunds();
			}
			transactions.add(new Xn(XnType.WITHDRAW, amount));
			balance -= amount;
			reconciler.validate(transactions, balance);
		}
	}

	private void validateAmount(int amount) {
		if (amount <= 0) {
			throw new InvalidAmount();
		}
	}

}

class InsufficientFunds extends RuntimeException {

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
