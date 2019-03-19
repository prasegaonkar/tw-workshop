package bankmodel;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class AccountTest {

	private Account account = null;

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Before
	public void setup() {
		account = new Account();
	}

	@Test
	public void checkOpeningBalance() {
		assertThat(account.getBalance()).isEqualTo(0);
		assertThat(account.getTransactions()).hasSize(0);
	}

	@Test
	public void canDepositIntoAccount() {
		account.deposit(20);
		assertThat(account.getBalance()).isEqualTo(20);
	}

	@Test
	public void canNotDepositInvalidAmountIntoAccount() {
		exception.expect(InvalidAmount.class);
		account.deposit(-20);
		assertThat(account.getBalance()).isEqualTo(0);
	}

	@Test
	public void canWithdrawFromAccountIfSufficientBalance() {
		account.deposit(100);
		account.withdraw(20);
		assertThat(account.getBalance()).isEqualTo(80);
	}

	@Test
	public void cannotWithdrawFromAccountIfInsufficientBalance() {
		exception.expect(InsufficientFunds.class);
		account.withdraw(120);
		assertThat(account.getBalance()).isEqualTo(0);
	}

	@Test
	public void testConcurrency() throws Exception {
		final int largeSumToEnsureNoUnderflow = 20000;
		account.deposit(largeSumToEnsureNoUnderflow);
		final Runnable deposit = () -> {
			for (int k = 0; k < 5; k++) {
				account.deposit(200);
			}
		};
		final Runnable withdraw = () -> {
			for (int k = 0; k < 5; k++) {
				account.withdraw(200);
			}
		};
		Thread[] depositors = new Thread[5];
		Thread[] withdrawers = new Thread[5];
		for (int i = 0; i < 5; i++) {
			depositors[i] = new Thread(deposit);
			withdrawers[i] = new Thread(withdraw);
		}
		for (int i = 0; i < 5; i++) {
			depositors[i].start();
			withdrawers[i].start();
		}
		for (int i = 0; i < 5; i++) {
			depositors[i].join();
			withdrawers[i].join();
		}
		assertThat(account.getBalance()).isEqualTo(largeSumToEnsureNoUnderflow);

	}
}
