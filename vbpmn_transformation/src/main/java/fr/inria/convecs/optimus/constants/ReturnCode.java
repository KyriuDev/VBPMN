package fr.inria.convecs.optimus.constants;

public class ReturnCode
{
	private ReturnCode()
	{

	}

	public static final String FALSE = "FALSE";
	public static final int TERMINATION_OK = 0;
	public static final int TERMINATION_ERROR = 1;
	public static final int TERMINATION_PROBLEM = 2;
	public static final int TERMINATION_UNBALANCED_INCLUSIVE_CYCLE = 3;
	public static final String TRUE = "TRUE";
}
