package bankmodel.core;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

class AccountReconciler {
	void validate(List<Xn> transactions, int expectedBalance) {
		AtomicInteger calculatedBalance = new AtomicInteger(0);
		if (transactions != null) {
			transactions.forEach(xn -> {
				if (XnType.DEPOSIT.equals(xn.getType())) {
					calculatedBalance.updateAndGet(x -> x + xn.getAmount());
				} else if (XnType.WITHDRAW.equals(xn.getType())) {
					calculatedBalance.updateAndGet(x -> x - xn.getAmount());
				}
			});
		}
		if (calculatedBalance.get() != expectedBalance) {
			throw new ReconciliationException();
		}
	}
}
