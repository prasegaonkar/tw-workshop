package bankmodel;

import static akka.actor.SupervisorStrategy.resume;

import akka.actor.AbstractLoggingActor;
import akka.actor.ActorRef;
import akka.actor.OneForOneStrategy;
import akka.actor.Props;
import akka.actor.SupervisorStrategy;
import akka.japi.pf.DeciderBuilder;
import bankmodel.core.Account;
import bankmodel.core.InsufficientFundsException;
import bankmodel.core.InvalidAmountException;

public class AccountActorSupervisor extends AbstractLoggingActor {
	private final ActorRef child;

	AccountActorSupervisor(Account account) {
		this.child = getContext().actorOf(AccountActor.props(account));
	}

	public static Props props(Account account) {
		return Props.create(AccountActorSupervisor.class, account);
	}

	@Override
	public Receive createReceive() {
		return receiveBuilder().matchAny(any -> child.forward(any, getContext())).build();
	}

	@Override
	public SupervisorStrategy supervisorStrategy() {
		return new OneForOneStrategy(DeciderBuilder.match(InvalidAmountException.class, ex -> resume())
				.match(InsufficientFundsException.class, ex -> resume()).build());
	}

}
