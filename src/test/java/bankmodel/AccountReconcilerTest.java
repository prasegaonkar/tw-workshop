package bankmodel;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class AccountReconcilerTest {
	private AccountReconciler reconciler = null;

	@Before
	public void setup() {
		reconciler = new AccountReconciler();
	}

	@Test
	public void checkWithNoTransactions() {
		boolean isReconciled = reconciler.validate(null, 0);
		assertThat(isReconciled).isEqualTo(true);
	}

	@Test
	public void checkWithTransactions() {
		List<Xn> transactions = new ArrayList<>();
		transactions.add(new Xn(XnType.DEPOSIT, 20));
		transactions.add(new Xn(XnType.WITHDRAW, 20));
		boolean isReconciled = reconciler.validate(transactions, 0);
		assertThat(isReconciled).isEqualTo(true);
	}

	@Test
	public void checkWithTransactionsMismatch() {
		List<Xn> transactions = new ArrayList<>();
		transactions.add(new Xn(XnType.DEPOSIT, 20));
		transactions.add(new Xn(XnType.WITHDRAW, 20));
		boolean isReconciled = reconciler.validate(transactions, 20);
		assertThat(isReconciled).isEqualTo(false);
	}
}
