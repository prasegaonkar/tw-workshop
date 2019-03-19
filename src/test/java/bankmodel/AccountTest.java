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
		account = new Account(100);
	}

	@Test
	public void canCreateAccountWithNonNegativeInitialBalanceOnly() {
		exception.expect(InvalidAmount.class);
		account = new Account(-100);
	}

	@Test
	public void checkOpeningBalance() {
		assertThat(account.getOpeningBalance()).isEqualTo(100);
		assertThat(account.getTransactions()).hasSize(1);
	}

	@Test
	public void canDepositIntoAccount() {
		account.deposit(20);
		assertThat(account.getCurrentBalance()).isEqualTo(120);
		assertThat(account.getOpeningBalance()).isEqualTo(100);
	}

	@Test
	public void canNotDepositInvalidAmountIntoAccount() {
		exception.expect(InvalidAmount.class);
		account.deposit(-20);
		assertThat(account.getOpeningBalance()).isEqualTo(100);
	}

	@Test
	public void canWithdrawFromAccountIfSufficientBalance() {
		account.withdraw(20);
		assertThat(account.getCurrentBalance()).isEqualTo(80);
		assertThat(account.getOpeningBalance()).isEqualTo(100);
	}

	@Test
	public void cannotWithdrawFromAccountIfInsufficientBalance() {
		exception.expect(InsufficientFunds.class);
		account.withdraw(120);
		assertThat(account.getCurrentBalance()).isEqualTo(100);
		assertThat(account.getOpeningBalance()).isEqualTo(100);
	}
}
