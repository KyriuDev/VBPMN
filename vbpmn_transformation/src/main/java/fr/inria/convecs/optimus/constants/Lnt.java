package fr.inria.convecs.optimus.constants;

public class Lnt
{
	public static final String ALT = "alt";
	public static final String ALT_OPERATOR = Constant.LEFT_SQUARE_BRACKET + Constant.RIGHT_SQUARE_BRACKET;
	public static final String AND = "and";
	public static final String ANY = "any";
	public static final String BASE_INDENT = Constant.SPACE + Constant.SPACE + Constant.SPACE;
	public static final String BOOLEAN_TYPE = "Bool";
	public static final String CASE = "case";
	public static final String CLOSE_MULTILINE_COMMENTARY = "*)";
	public static final String ELSE = "else";
	public static final String ELLIPSIS = "...";
	public static final String EMPTY_LIST = "nil";
	public static final String END_ALT = "end alt";
	public static final String END_CASE = "end case";
	public static final String END_FUNCTION = "end function";
	public static final String END_HIDE = "end hide";
	public static final String END_IF = "end if";
	public static final String END_LOOP = "end loop";
	public static final String END_MODULE = "end module";
	public static final String END_PAR = "end par";
	public static final String END_PROCESS = "end process";
	public static final String END_TYPE = "end type";
	public static final String END_VAR = "end var";
	public static final String EQUALS_OPERATOR = "==";
	public static final String FALSE = "False";
	public static final String FILE_EXTENSION = ".lnt";
	public static final String FUNCTION = "function";
	public static final String GET = "get";
	public static final String HIDE = "hide";
	public static final String HIDE_ALL_BUT = "hide all but";
	public static final String IF = "if";
	public static final String IN = "in";
	public static final String IS = "is";
	public static final String LOOP = "loop";
	public static final String LOWER_THAN_OPERATOR = "<";
	public static final String MAIN = "MAIN";
	public static final String MODULE = "module";
	public static final String NATURAL_NUMBER_TYPE = "Nat";
	public static final String NOT_EQUALS_OPERATOR = "!=";
	public static final String OF = "of";
	public static final String OPEN_MULTILINE_COMMENTARY = "(*";
	public static final String OUT_PARAMETER = "?";
	public static final String PAR = "par";
	public static final String PAR_OPERATOR = "||";
	public static final String PATTERN_MATCHING_OPERATOR = Constant.RIGHT_ARROW;
	public static final String PRAGMA_CARDINAL = "!card";
	public static final String PRAGMA_NAT_BITS = "!nat_bits";
	public static final String PREDEFINED_FUNCTION_CARDINAL = "card";
	public static final String PREDEFINED_FUNCTION_EMPTY = "empty";
	public static final String PREDEFINED_FUNCTION_INSERT = "insert";
	public static final String PREDEFINED_FUNCTION_INTERSECTION = "inter";
	public static final String PREDEFINED_FUNCTION_MEMBER = "member";
	public static final String PREDEFINED_FUNCTION_MINUS = "minus";
	public static final String PREDEFINED_FUNCTION_REMOVE = "remove";
	public static final String PREDEFINED_FUNCTION_UNION = "union";
	public static final String PROCESS = "process";
	public static final String RETURN = "return";
	public static final String SEQUENTIAL_COMPOSITION_OPERATOR = ";";
	public static final String SEQUENTIAL_COMPOSITION_OPERATOR_AND_SPACE = Lnt.SEQUENTIAL_COMPOSITION_OPERATOR + Constant.SPACE;
	public static final String SET = "set";
	public static final String SPACED_EQUALS_OPERATOR = Constant.SPACE + Lnt.EQUALS_OPERATOR + Constant.SPACE;
	public static final String SPACED_IN = Constant.SPACE + Lnt.IN + Constant.SPACE;
	public static final String SPACED_IS = Constant.SPACE + Lnt.IS + Constant.SPACE;
	public static final String SPACED_OF = Constant.SPACE + Lnt.OF + Constant.SPACE;
	public static final String SPACED_PATTERN_MATCHING_OPERATOR = Constant.SPACE + Lnt.PATTERN_MATCHING_OPERATOR + Constant.SPACE;
	public static final String SPACED_VARIABLE_ASSIGNATION_OPERATOR = Constant.SPACE + ":=" + Constant.SPACE;
	//79 chars correspond to the standard line size in LNT.
	public static final String STANDARD_SEPARATOR = "----------------------------------------------------------------" +
			"--------------";
	public static final String THEN = "then";
	public static final String TRUE = "True";
	public static final String TYPE = "type";
	public static final String VAR = "var";
	public static final String VARIABLE_ASSIGNATION_OPERATOR = ":=";
	public static final String WHERE = "where";
	public static final String WHILE = "while";
	public static final String WITH = "with";

	public static final int MAX_CHAR_PER_LINE = 79;
	public static final int BASE_INDENT_LENGTH = Lnt.BASE_INDENT.length();

	private Lnt()
	{

	}
}
