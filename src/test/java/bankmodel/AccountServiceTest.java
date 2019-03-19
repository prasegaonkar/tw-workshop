package bankmodel;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import bankmodel.core.Account;
import lombok.Getter;

@Getter
public class AccountServiceTest {
	private AccountService service;
	private static final int largeSumToEnsureNoUnderflow = 20000;

	@Before
	public void setup() {
		Account account = new Account();
		account.deposit(largeSumToEnsureNoUnderflow);
		service = new AccountService(account);
	}

	@Test
	public void testConcurrency() throws Exception {
		Thread[] depositors = new Thread[5];
		Thread[] withdrawers = new Thread[5];
		for (int i = 0; i < 5; i++) {
			depositors[i] = new Thread(() -> {
				for (int k = 0; k < 5; k++) {
					service.deposit(200);
				}
			});
			withdrawers[i] = new Thread(() -> {
				for (int k = 0; k < 5; k++) {
					service.withdraw(200);
				}
			});
		}
		for (int i = 0; i < 5; i++) {
			depositors[i].start();
			withdrawers[i].start();
		}
		for (int i = 0; i < 5; i++) {
			depositors[i].join();
			withdrawers[i].join();
		}
		assertThat(service.getBalance()).isEqualTo(largeSumToEnsureNoUnderflow);

	}
}
