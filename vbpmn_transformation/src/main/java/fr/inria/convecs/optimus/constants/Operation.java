package fr.inria.convecs.optimus.constants;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Operation
{
	public static final String DEFAULT = Comparison.CONSERVATIVE;
	public static final String EQUAL = "equal";
	public static final String GREATER = "greater";
	public static final String HIDING = "_"; //NOT IN Python CODE
	public static final List<String> LIST = Arrays.asList(
		Comparison.CONSERVATIVE,
		Comparison.INCLUSIVE,
		Comparison.EXCLUSIVE,
		Property.AND,
		Property.IMPLIED
	);
	public static final HashMap<String, String> LIST_BISIMULATOR = new HashMap<>()
	{{
		put(Comparison.CONSERVATIVE, Operation.EQUAL);
		put(Comparison.INCLUSIVE, Operation.SMALLER);
		put(Comparison.EXCLUSIVE, Operation.GREATER);
	}};
	public static final String SMALLER = "smaller";

	private Operation()
	{

	}
}
