package bankmodel;

import java.util.List;

import akka.actor.AbstractLoggingActor;
import akka.actor.Props;
import bankmodel.core.Account;
import bankmodel.core.Transaction;

public class AccountActor extends AbstractLoggingActor {
	private final Account account;

	AccountActor(Account account) {
		this.account = account;
	}

	public static Props props(Account account) {
		return Props.create(AccountActor.class, account);
	}

	public static class GetBalance {
	}

	public static class GetTransactions {
	}

	public static class AccountBalance {
		private final int amount;

		public AccountBalance(int amount) {
			this.amount = amount;
		}

		public int getAmount() {
			return amount;
		}

	}

	public static class AccountTransactions {
		private final List<Transaction> transactions;

		public AccountTransactions(List<Transaction> transactions) {
			this.transactions = transactions;
		}

		public List<Transaction> getTransactions() {
			return transactions;
		}

	}

	public static class Deposit {
		private final int amount;

		public Deposit(int amount) {
			this.amount = amount;
		}

		public int getAmount() {
			return amount;
		}

	}

	public static class Withdraw {
		private final int amount;

		public Withdraw(int amount) {
			this.amount = amount;
		}

		public int getAmount() {
			return amount;
		}

	}

	private void getBalance(GetBalance r) {
		getSender().tell(new AccountBalance(account.getBalance()), getSelf());
	}

	private void getTransactions(GetTransactions r) {
		getSender().tell(new AccountTransactions(account.getTransactions()), getSelf());
	}

	private void deposit(Deposit r) {
		account.deposit(r.getAmount());
	}

	private void withdraw(Withdraw r) {
		account.withdraw(r.getAmount());
	}

	@Override
	public Receive createReceive() {
		return receiveBuilder().match(GetBalance.class, this::getBalance)
				.match(GetTransactions.class, this::getTransactions).match(Deposit.class, this::deposit)
				.match(Withdraw.class, this::withdraw).build();
	}

}
