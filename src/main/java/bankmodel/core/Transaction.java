package bankmodel.core;

import lombok.Value;

@Value
public class Transaction {
	private final TransactionType type;
	private final int amount;
}