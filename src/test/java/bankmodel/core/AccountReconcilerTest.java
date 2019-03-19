package bankmodel.core;

import static org.assertj.core.api.Assertions.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import bankmodel.core.AccountReconciler;
import bankmodel.core.Transaction;
import bankmodel.core.TransactionType;

public class AccountReconcilerTest {
	private AccountReconciler reconciler = null;

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Before
	public void setup() {
		reconciler = new AccountReconciler();
	}

	@Test
	public void checkWithNoTransactions() {
		try {
			reconciler.validate(null, 0);
		} catch (ReconciliationException ex) {
			fail(ex.getMessage());
		}
	}

	@Test
	public void checkWithTransactions() {
		List<Transaction> transactions = new ArrayList<>();
		transactions.add(new Transaction(TransactionType.DEPOSIT, 20));
		transactions.add(new Transaction(TransactionType.WITHDRAW, 20));
		try {
			reconciler.validate(transactions, 0);
		} catch (ReconciliationException ex) {
			fail(ex.getMessage());
		}
	}

	@Test
	public void checkWithTransactionsMismatch() {
		List<Transaction> transactions = new ArrayList<>();
		transactions.add(new Transaction(TransactionType.DEPOSIT, 20));
		transactions.add(new Transaction(TransactionType.WITHDRAW, 20));
		exception.expect(ReconciliationException.class);
		reconciler.validate(transactions, 20);
	}
}
