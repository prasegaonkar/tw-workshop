package bankmodel.core;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

class AccountReconciler {
	void validate(List<Transaction> transactions, int expectedBalance) {
		AtomicInteger calculatedBalance = new AtomicInteger(0);
		if (transactions != null) {
			transactions.forEach(xn -> {
				if (TransactionType.DEPOSIT.equals(xn.getType())) {
					calculatedBalance.updateAndGet(x -> x + xn.getAmount());
				} else if (TransactionType.WITHDRAW.equals(xn.getType())) {
					calculatedBalance.updateAndGet(x -> x - xn.getAmount());
				}
			});
		}
		if (calculatedBalance.get() != expectedBalance) {
			throw new ReconciliationException();
		}
	}
}
