package bankmodel;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.testkit.javadsl.TestKit;
import bankmodel.AccountActor.AccountBalance;
import bankmodel.AccountActor.AccountTransactions;
import bankmodel.AccountActor.Deposit;
import bankmodel.AccountActor.GetBalance;
import bankmodel.AccountActor.GetTransactions;
import bankmodel.AccountActor.Withdraw;
import bankmodel.core.Account;

public class AccountActorSupervisorTest {
	static ActorSystem system;

	@BeforeClass
	public static void setupClass() {
		system = ActorSystem.create();
	}

	@AfterClass
	public static void teardownClass() {
		TestKit.shutdownActorSystem(system);
		system = null;
	}

	@Test
	public void testConcurrency() throws Exception {
		Account account = new Account();
		account.deposit(200000);
		TestKit probe = new TestKit(system);
		ActorRef supervisor = system.actorOf(AccountActorSupervisor.props(account), "supervisor");

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
		supervisor.tell(new GetBalance(), probe.getRef());
		AccountBalance balance = probe.expectMsgClass(AccountBalance.class);
		assertThat(balance.getAmount()).isEqualTo(200000);
		supervisor.tell(new GetTransactions(), probe.getRef());
		AccountTransactions transactions = probe.expectMsgClass(AccountTransactions.class);
		assertThat(transactions.getTransactions().size()).isEqualTo(51);
	}
}
