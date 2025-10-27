package fr.inria.convecs.optimus.constants;

import java.util.Arrays;
import java.util.List;

public class Selection
{
	public static final String ALL = "all";
	public static final String DEFAULT = Selection.ALL;
	public static final String FIRST = "first";
	public static final List<String> LIST = Arrays.asList(
		Selection.FIRST,
		Selection.SECOND,
		Selection.ALL
	);
	public static final String SECOND = "second";

	private Selection()
	{

	}
}
