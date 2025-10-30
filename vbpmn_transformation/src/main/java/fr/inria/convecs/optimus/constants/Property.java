package fr.inria.convecs.optimus.constants;

import java.util.Arrays;
import java.util.List;

public class Property
{
	public static final String AND = "property-and";
	public static final String IMPLIED = "property-implied";
	public static final List<String> LIST = Arrays.asList(
		AND,
		IMPLIED
	);

	private Property()
	{

	}
}
