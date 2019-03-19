package bankmodel;

import static org.assertj.core.api.Assertions.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

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
		} catch (AccountNotBeingReconciled ex) {
			fail(ex.getMessage());
		}
	}

	@Test
	public void checkWithTransactions() {
		List<Xn> transactions = new ArrayList<>();
		transactions.add(new Xn(XnType.DEPOSIT, 20));
		transactions.add(new Xn(XnType.WITHDRAW, 20));
		try {
			reconciler.validate(transactions, 0);
		} catch (AccountNotBeingReconciled ex) {
			fail(ex.getMessage());
		}
	}

	@Test
	public void checkWithTransactionsMismatch() {
		List<Xn> transactions = new ArrayList<>();
		transactions.add(new Xn(XnType.DEPOSIT, 20));
		transactions.add(new Xn(XnType.WITHDRAW, 20));
		exception.expect(AccountNotBeingReconciled.class);
		reconciler.validate(transactions, 20);
	}
}
