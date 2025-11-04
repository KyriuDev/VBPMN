package fr.inria.convecs.optimus.constants;

import fr.inria.convecs.optimus.util.Utils;

import java.util.Collection;
import java.util.List;

public class Lnt
{
    //Integers
    public static final int MAX_CHAR_PER_LINE = 79;
    public static final int BASE_INDENT_LENGTH = Lnt.BASE_INDENT.length();

    //Strings
	public static final String ALT = "alt";
	public static final String ALT_OPERATOR = Constant.LEFT_SQUARE_BRACKET + Constant.RIGHT_SQUARE_BRACKET;
	public static final String ANY = "any";
	public static final String BASE_INDENT = Constant.SPACE + Constant.SPACE + Constant.SPACE;
	public static final String BOOLEAN_TYPE = "Bool";
	public static final String CASE = "case";
	public static final String CASE_OPERATOR = "|";
	public static final String CLOSE_MULTILINE_COMMENTARY = "*)";
	public static final String PREDEFINED_CONSTRUCTOR_FUNCTION = "cons";
	public static final String ELSE = "else";
	public static final String ELSE_IF = "elsif";
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
	private static final String INPUT_OUTPUT_PARAMETER = "?";
	public static final String IS = "is";
	public static final String LOGICAL_AND = "and";
	public static final String LOOP = "loop";
	public static final String LOWER_THAN_OPERATOR = "<";
	public static final String MAIN = "MAIN";
	public static final String MAX_DASHES_IN_MULTILINE_COMMENTARY = "------------------------------------------------" +
            "-------------------------";
	public static final String MODULE = "module";
	public static final String NATURAL_NUMBER_TYPE = "Nat";
	public static final String NOT_EQUALS_OPERATOR = "!=";
	public static final String OF = "of";
	public static final String OPEN_MULTILINE_COMMENTARY = "(*";
	private static final String OUT_PARAMETER = "?";
	public static final String PAR = "par";
	public static final String PAR_OPERATOR = "||";
	public static final String PARAMETER_MODE_IN_OUT = "in out";
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
    public static final String SPACED_AND = Constant.SPACE + Lnt.LOGICAL_AND + Constant.SPACE;
	public static final String SPACED_EQUALS_OPERATOR = Constant.SPACE + Lnt.EQUALS_OPERATOR + Constant.SPACE;
	public static final String SPACED_IN = Constant.SPACE + Lnt.IN + Constant.SPACE;
	public static final String SPACED_IS = Constant.SPACE + Lnt.IS + Constant.SPACE;
	public static final String SPACED_OF = Constant.SPACE + Lnt.OF + Constant.SPACE;
	public static final String SPACED_PATTERN_MATCHING_OPERATOR = Constant.SPACE + Lnt.PATTERN_MATCHING_OPERATOR + Constant.SPACE;
	public static final String SPACED_VARIABLE_ASSIGNATION_OPERATOR = Constant.SPACE + ":=" + Constant.SPACE;
	//78 chars correspond to the standard line size in LNT.
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

    //Helper functions
    public static String markAsInputParameter(final String parameter)
    {
        return parameter;
    }

    public static String markAsInputOutputParameter(final String parameter)
    {
        return parameter + Lnt.INPUT_OUTPUT_PARAMETER;
    }

    public static String markAsOutputParameter(final String parameter)
    {
        return Lnt.OUT_PARAMETER + parameter;
    }

    public static String generateFunctionCall(final String functionName,
											  final String... args)
    {
        if (functionName == null
            || functionName.isEmpty())
        {
            throw new RuntimeException("Function name must be non-null and non-empty!");
        }

        final StringBuilder builder = new StringBuilder(functionName)
                .append(Constant.SPACE_AND_LEFT_PARENTHESIS);

        String argsSeparator = Constant.EMPTY_STRING;

        for (final String arg : args)
        {
            builder.append(argsSeparator)
                    .append(arg);

            argsSeparator = Constant.COMA_AND_SPACE;
        }

        builder.append(Constant.RIGHT_PARENTHESIS);

        return builder.toString();
    }

    public static String generateVariableAssignation(final String variable,
													 final String value)
    {
        if (variable == null
            || value == null
            || variable.isEmpty()
            || value.isEmpty())
        {
            throw new RuntimeException("Variable and value must be non-null and non-empty!");
        }

        return variable + Lnt.SPACED_VARIABLE_ASSIGNATION_OPERATOR + value;
    }

    public static String generateIfStatement(final String comparisonValue)
    {
        if (comparisonValue == null
            || comparisonValue.isEmpty())
        {
            throw new RuntimeException("Comparison value must be non-null and non-empty!");
        }

        return Lnt.IF + Constant.SPACE_AND_LEFT_PARENTHESIS + comparisonValue + Constant.RIGHT_PARENTHESIS_AND_SPACE +
                Lnt.THEN;
    }

	public static String generateElsifStatement(final String comparisonValue)
	{
		if (comparisonValue == null
			|| comparisonValue.isEmpty())
		{
			throw new RuntimeException("Comparison value must be non-null and non-empty!");
		}

		return Lnt.ELSE_IF + Constant.SPACE_AND_LEFT_PARENTHESIS + comparisonValue + Constant.RIGHT_PARENTHESIS_AND_SPACE +
				Lnt.THEN;
	}

	public static String generateWhileStatement(final String comparisonValue)
	{
		if (comparisonValue == null
			|| comparisonValue.isEmpty())
		{
			throw new RuntimeException("Comparison value must be non-null and non-empty!");
		}

		return Lnt.WHILE + Constant.SPACE_AND_LEFT_PARENTHESIS + comparisonValue + Constant.RIGHT_PARENTHESIS_AND_SPACE +
				Lnt.LOOP;
	}

    public static String generateObjectWithArguments(final String objectIdentifier,
                                                     final String... arguments)
    {
        if (objectIdentifier == null
            || objectIdentifier.isEmpty())
        {
            throw new RuntimeException("Object identifier must be non-null and non-empty!");
        }

        final StringBuilder builder = new StringBuilder(objectIdentifier)
                .append(Constant.SPACE_AND_LEFT_PARENTHESIS);
        String argumentSeparator = Constant.EMPTY_STRING;

        for (final String argument : arguments)
        {
            builder.append(argumentSeparator)
                    .append(argument);
            argumentSeparator = Constant.COMA_AND_SPACE;
        }

        builder.append(Constant.RIGHT_PARENTHESIS);

        return builder.toString();
    }

	public static String generateObjectConstructor(final String objectIdentifier,
												   final ParametersAndType... listOfParametersAndTypes)
	{
		if (objectIdentifier == null
			|| objectIdentifier.isEmpty()
			|| listOfParametersAndTypes == null
			|| listOfParametersAndTypes.length == 0)
		{
			throw new RuntimeException("Object constructor should have an identifier, and a non-empty list of arguments!");
		}

		final StringBuilder builder = new StringBuilder(objectIdentifier)
				.append(Constant.SPACE_AND_LEFT_PARENTHESIS);
		String typeSeparator = Constant.EMPTY_STRING;

		for (final ParametersAndType parametersAndType : listOfParametersAndTypes)
		{
			final Collection<String> arguments = parametersAndType.getParameters();
			final String type = parametersAndType.getType();
			String argumentSeparator = Constant.EMPTY_STRING;
			builder.append(typeSeparator);
			typeSeparator = Constant.COMA_AND_SPACE;

			for (final String argument : arguments)
			{
				builder.append(argumentSeparator)
						.append(argument);
				argumentSeparator = Constant.COMA_AND_SPACE;
			}

			builder.append(Constant.COLON_AND_SPACE)
					.append(type);
		}

		builder.append(Constant.RIGHT_PARENTHESIS);

		return builder.toString();
	}

    public static String generateEqualsComparison(final String leftHandSide,
                                                  final String rightHandSide)
    {
        if (leftHandSide == null
            || rightHandSide == null
            || leftHandSide.isEmpty()
            || rightHandSide.isEmpty())
        {
            throw new RuntimeException("Left hand side and right hand side elements must be non-null and non-empty!");
        }

        return leftHandSide + Lnt.SPACED_EQUALS_OPERATOR + rightHandSide;
    }

    public static String generateReturnStatement(final String returnValue)
    {
        if (returnValue == null
            || returnValue.isEmpty())
        {
            throw new RuntimeException("Return value must be non-null and non-empty!");
        }

        return Lnt.RETURN + Constant.SPACE + returnValue;
    }

	public static String generateVariableDeclarationStatement(final VariablesAndType... listOfVariablesAndTypes)
	{
		if (listOfVariablesAndTypes == null
			|| listOfVariablesAndTypes.length == 0)
		{
			throw new RuntimeException("There must be at least one variable to declare!");
		}

		final StringBuilder builder = new StringBuilder(Lnt.VAR)
				.append(Constant.SPACE);

		String typesSeparator = Constant.EMPTY_STRING;

		for (final VariablesAndType variablesAndType : listOfVariablesAndTypes)
		{
			final Collection<String> variables = variablesAndType.getVariables();
			final String type = variablesAndType.getType();

			if (variables == null
				|| variables.isEmpty()
				|| type == null
				|| type.isEmpty())
			{
				throw new RuntimeException("There must be at least one variable in each given variable list, and this variable must have a type!");
			}

			builder.append(typesSeparator);
			typesSeparator = Constant.COMA_AND_SPACE;
			String variableSeparator = Constant.EMPTY_STRING;

			for (final String variable : variables)
			{
				builder.append(variableSeparator)
						.append(variable);
				variableSeparator = Constant.COMA_AND_SPACE;
			}

			builder.append(Constant.COLON_AND_SPACE)
					.append(type);
		}

		builder.append(Constant.SPACE)
				.append(Lnt.IN);

		return builder.toString();
	}

	public static String generateFunctionHeader(final String functionName,
												final String returnType,
												final boolean jumpLine,
												final ParametersAndType... listOfParametersAndTypes)
	{
		if (functionName == null
			|| functionName.isEmpty()
			|| returnType == null
			|| returnType.isEmpty()
			|| listOfParametersAndTypes == null
			|| listOfParametersAndTypes.length == 0)
		{
			throw new RuntimeException("Function should have a name, a return type, and a non-empty list of arguments!");
		}

		final StringBuilder builder = new StringBuilder(Lnt.FUNCTION)
				.append(jumpLine ? Constant.LINE_FEED : Constant.SPACE)
				.append(jumpLine ? Utils.indentLNT(1) : Constant.EMPTY_STRING)
				.append(functionName)
				.append(Constant.SPACE_AND_LEFT_PARENTHESIS);

		String typesSeparator = Constant.EMPTY_STRING;

		for (final ParametersAndType parametersAndType : listOfParametersAndTypes)
		{
			final Collection<String> arguments = parametersAndType.getParameters();
			final String type = parametersAndType.getType();

			if (arguments == null
				|| arguments.isEmpty()
				|| type == null
				|| type.isEmpty())
			{
				throw new RuntimeException("There must be at least one argument in each given variable list, and this argument must have a type!");
			}

			builder.append(typesSeparator);
			typesSeparator = Constant.COMA_AND_SPACE;
			String argumentSeparator = Constant.EMPTY_STRING;

			for (final String argument : arguments)
			{
				builder.append(argumentSeparator)
						.append(argument);
				argumentSeparator = Constant.COMA_AND_SPACE;
			}

			builder.append(Constant.COLON_AND_SPACE)
					.append(type);
		}

		builder.append(Constant.RIGHT_PARENTHESIS)
				.append(Constant.COLON_AND_SPACE)
				.append(returnType)
				.append(jumpLine ? Constant.LINE_FEED : Constant.SPACE)
				.append(Lnt.IS);

		return builder.toString();
	}

	public static String generateProcessHeader(final String processName,
											   final Collection<ParametersAndType> listOfParametersAndTypes,
											   final EventsAndType... listOfEventsAndTypes)
	{
		if (processName == null
			|| processName.isEmpty()
			|| listOfEventsAndTypes == null
			|| listOfEventsAndTypes.length == 0)
		{
			throw new RuntimeException("Processes should have a name, and a non-null and non-empty list of events!");
		}

		final StringBuilder builder = new StringBuilder(Lnt.PROCESS)
				.append(Constant.SPACE)
				.append(processName)
				.append(Constant.SPACE_AND_LEFT_SQUARE_BRACKET);

		String typesSeparator = Constant.EMPTY_STRING;

		for (final EventsAndType eventsAndType : listOfEventsAndTypes)
		{
			final Collection<String> events = eventsAndType.getEvents();
			final String type = eventsAndType.getType();

			if (events == null
				|| events.isEmpty()
				|| type == null
				|| type.isEmpty())
			{
				throw new RuntimeException("There must be at least one event in each given event list, and this event must have a type!");
			}

			builder.append(typesSeparator);
			typesSeparator = Constant.COMA_AND_SPACE;
			String eventSeparator = Constant.EMPTY_STRING;

			for (final String event : events)
			{
				builder.append(eventSeparator)
						.append(event);
				eventSeparator = Constant.COMA_AND_SPACE;
			}

			builder.append(Constant.COLON_AND_SPACE)
					.append(type);
		}

		builder.append(Constant.RIGHT_SQUARE_BRACKET);

		if (listOfParametersAndTypes != null
			&& !listOfParametersAndTypes.isEmpty())
		{
			typesSeparator = Constant.EMPTY_STRING;
			builder.append(Constant.SPACE_AND_LEFT_PARENTHESIS);

			for (final ParametersAndType parametersAndType : listOfParametersAndTypes)
			{
				final Collection<String> parameters = parametersAndType.getParameters();
				final String type = parametersAndType.getType();

				if (parameters == null
					|| parameters.isEmpty()
					|| type == null
					|| type.isEmpty())
				{
					throw new RuntimeException("There must be at least one parameter in each given parameter list, and this parameter must have a type!");
				}

				builder.append(typesSeparator);
				typesSeparator = Constant.COMA_AND_SPACE;
				String parameterSeparator = Constant.EMPTY_STRING;

				for (final String parameter : parameters)
				{
					builder.append(parameterSeparator)
							.append(parameter);
					parameterSeparator = Constant.COMA_AND_SPACE;
				}

				builder.append(Constant.COLON_AND_SPACE)
						.append(type);
			}

			builder.append(Constant.RIGHT_PARENTHESIS);
		}

		builder.append(Constant.SPACE)
				.append(Lnt.IS);

		return builder.toString();
	}

	public static String generateVariablesDefinition(final Collection<String> variables,
													 final String type)
	{
		if (variables == null
			|| variables.isEmpty()
			|| type == null
			|| type.isEmpty())
		{
			throw new RuntimeException("Variable and type must be non-null and non-empty!");
		}

		final StringBuilder builder = new StringBuilder();
		String variableSeparator = Constant.EMPTY_STRING;

		for (final String variable : variables)
		{
			builder.append(variableSeparator)
					.append(variable);
			variableSeparator = Constant.COMA_AND_SPACE;
		}

		builder.append(Constant.COLON_AND_SPACE)
				.append(type);

		return builder.toString();
	}

	public static String generateVariablesDefinition(final String variable,
													 final String type)
	{
		return Lnt.generateVariablesDefinition(List.of(variable), type);
	}

	//Sub-classes

	public static class VariablesAndType
	{
		private final Collection<String> variables;
		private final String type;

		public VariablesAndType(final String variable,
								final String type)
		{
			this(List.of(variable), type);
		}

		public VariablesAndType(final Collection<String> variables,
								final String type)
		{
			this.variables = variables;
			this.type = type;
		}

		public Collection<String> getVariables()
		{
			return this.variables;
		}

		public String getType()
		{
			return this.type;
		}
	}

	public static class ParametersAndType
	{
		//TODO: Dirty but allows distinguishing between events, parameters, and variables while avoiding code duplication
		private final VariablesAndType parametersAndType;

		public ParametersAndType(final String parameter,
								 final String type)
		{
			this(List.of(parameter), type);
		}

		public ParametersAndType(final Collection<String> parameters,
								 final String type)
		{
			this.parametersAndType = new VariablesAndType(parameters, type);
		}

		public Collection<String> getParameters()
		{
			return this.parametersAndType.getVariables();
		}

		public String getType()
		{
			return this.parametersAndType.getType();
		}
	}

	public static class EventsAndType
	{
		//TODO: Dirty but allows distinguishing between events, parameters, and variables while avoiding code duplication
		private final VariablesAndType eventsAndType;

		public EventsAndType(final String event,
							 final String type)
		{
			this(List.of(event), type);
		}

		public EventsAndType(final Collection<String> events,
							 final String type)
		{
			this.eventsAndType = new VariablesAndType(events, type);
		}

		public Collection<String> getEvents()
		{
			return this.eventsAndType.getVariables();
		}

		public String getType()
		{
			return this.eventsAndType.getType();
		}
	}

	private Lnt()
	{

	}
}
