package bankmodel;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import bankmodel.core.Account;

public class SynchronizedAccountTest {
	private SynchronizedAccount syncAccount;
	private static final int largeSumToEnsureNoUnderflow = 20000;

	@Before
	public void setup() {
		Account account = new Account();
		account.deposit(largeSumToEnsureNoUnderflow);
		syncAccount = new SynchronizedAccount(account);
	}

	@Test
	public void testConcurrency() throws Exception {
		Thread[] depositors = new Thread[5];
		Thread[] withdrawers = new Thread[5];
		Thread[] invalidators = new Thread[5];
		for (int i = 0; i < 5; i++) {
			depositors[i] = new Thread(() -> {
				for (int k = 0; k < 5; k++) {
					syncAccount.deposit(200);
				}
			});
			withdrawers[i] = new Thread(() -> {
				for (int k = 0; k < 5; k++) {
					syncAccount.withdraw(200);
				}
			});
			invalidators[i] = new Thread(() -> {
				for (int k = 0; k < 5; k++) {
					syncAccount.withdraw(-20);
					syncAccount.deposit(-20);
				}
			});
		}
		for (int i = 0; i < 5; i++) {
			depositors[i].start();
			withdrawers[i].start();
			invalidators[i].start();
		}
		for (int i = 0; i < 5; i++) {
			depositors[i].join();
			withdrawers[i].join();
			invalidators[i].join();
		}
		assertThat(syncAccount.getBalance()).isEqualTo(largeSumToEnsureNoUnderflow);

	}
}
