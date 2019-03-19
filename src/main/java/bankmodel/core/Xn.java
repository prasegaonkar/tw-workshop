package bankmodel.core;

import lombok.Value;

@Value
public class Xn {
	private final XnType type;
	private final int amount;
}