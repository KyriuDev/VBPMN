package fr.inria.convecs.optimus.constants;

import java.util.Arrays;
import java.util.List;

public class Comparison
{
	public static final String CONSERVATIVE = "conservative";
	public static final String EXCLUSIVE = "exclusive";
	public static final String INCLUSIVE = "inclusive";
	public static final List<String> LIST = Arrays.asList(
		CONSERVATIVE,
		INCLUSIVE,
		EXCLUSIVE
	);

	private Comparison()
	{

	}
}
