package fr.inria.convecs.optimus.constants;

public class Constant
{
	public static final int MAX_VARS_PER_LINE = 8; //After 8 variables, the line size necessarily exceeds 79 chars thus multiple lines are required
	public static final int PROCESS_INDENT_LENGTH = 8;

	public static final String CADP = "cadp";
	public static final String COLON = ":";
	public static final String COLON_AND_SPACE = ": ";
	public static final String COMA = ",";
	public static final String COMA_AND_SPACE = ", ";
	public static final String COMMENTS_DASHES = "-----";
	public static final String DOUBLE_LINE_FEED = "\n\n";
	public static final String DOUBLE_QUOTATION_MARK = "\"";
	public static final String DOUBLE_QUOTATION_MARK_AND_SPACE = "\" ";
	public static final String EMPTY_STRING = "";
	public static final String ID_FILENAME = "id.lnt";
	public static final String LEFT_CURVY_BRACKET = "{";
	public static final String LEFT_PARENTHESIS = "(";
	public static final String LEFT_SQUARE_BRACKET = "[";
	public static final String LINE_FEED = "\n";
	public static final String LNT_SUFFIX = ".lnt";
	public static final String LTS_SUFFIX = ".bcg";
	public static final String MCL_FORMULA_FILENAME = "formula.mcl";
	public static final String RIGHT_ARROW = "->";
	public static final String RIGHT_CURVY_BRACKET = "}";
	public static final String RIGHT_CURVY_BRACKET_AND_SPACE = "} ";
	public static final String RIGHT_PARENTHESIS = ")";
	public static final String RIGHT_PARENTHESIS_AND_SPACE = ") ";
	public static final String RIGHT_SQUARE_BRACKET = "]";
	public static final String RIGHT_SQUARE_BRACKET_AND_SPACE = "] ";
	public static final String SPACE = " ";
	public static final String SPACE_AND_DOUBLE_QUOTATION_MARK = Constant.SPACE + Constant.DOUBLE_QUOTATION_MARK;
	public static final String SPACE_AND_LEFT_CURVY_BRACKET = Constant.SPACE + Constant.LEFT_CURVY_BRACKET;
	public static final String SPACE_AND_LEFT_PARENTHESIS = Constant.SPACE + Constant.LEFT_PARENTHESIS;
	public static final String SPACE_AND_LEFT_SQUARE_BRACKET = Constant.SPACE + Constant.LEFT_SQUARE_BRACKET;
	public static final String UNDERSCORE = "_";
	public static final String WORK_SUFFIX = "_work";

	private Constant()
	{

	}
}
