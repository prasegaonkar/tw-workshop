package bankmodel.core;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import bankmodel.core.Account;

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
		exception.expect(InvalidAmountException.class);
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
		exception.expect(InsufficientFundsException.class);
		account.withdraw(120);
		assertThat(account.getBalance()).isEqualTo(0);
	}
}
