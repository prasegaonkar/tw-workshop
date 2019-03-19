/*package bankmodel;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.testkit.TestKit;
import bankmodel.AccountActor.Deposit;
import bankmodel.AccountActor.Withdraw;
import bankmodel.core.Account;

public class AccountActorSupervisorTest {
	private TestKit probe;
	private ActorRef supervisor;
	private static final int largeSumToEnsureNoUnderflow = 20000;

	@Before
	public void setup() {
		Account account = new Account();
		account.deposit(largeSumToEnsureNoUnderflow);
		TestKit probe = new TestKit(system);		
		supervisor = system.actorOf(AccountActorSupervisor.props(account), "supervisor");
	}

	@After
	public void teardown() {
		system.terminate();
	}

	@Test
	public void testConcurrency() throws Exception {
		Thread[] depositors = new Thread[5];
		Thread[] withdrawers = new Thread[5];
		Thread[] invalidators = new Thread[5];
		for (int i = 0; i < 5; i++) {
			depositors[i] = new Thread(() -> {
				for (int k = 0; k < 5; k++) {
					supervisor.tell(new Deposit(200), ActorRef.noSender());
				}
			});
			withdrawers[i] = new Thread(() -> {
				for (int k = 0; k < 5; k++) {
					supervisor.tell(new Withdraw(200), ActorRef.noSender());
				}
			});
			invalidators[i] = new Thread(() -> {
				for (int k = 0; k < 5; k++) {
					supervisor.tell(new Deposit(-20), ActorRef.noSender());
					supervisor.tell(new Withdraw(-20), ActorRef.noSender());
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
*/