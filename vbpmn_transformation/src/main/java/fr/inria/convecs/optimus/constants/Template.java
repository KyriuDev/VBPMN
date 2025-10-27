package fr.inria.convecs.optimus.constants;

public class Template
{
	/*
		Template for SVL scripts.
		First argument is the SVL contents.
	 */
	public static final String SVL_CAESAR =
		"% CAESAR_OPEN_OPTIONS=\"-silent -warning\"\n" +
		"% CAESAR_OPTIONS=\"-more cat\"\n" +
		"{0}\n"
	;

	/*
	 	Command to call SVL.
	 	First argument is the script, second one is the result file.
	 */
	public static final String SVL_CALL_COMMAND =
		"svl {0} -> {1}"
	;

	/*
		Template of the verification of a comparison between two models.
		First argument is the first model (LTS in BCG format).
		Second one is the comparison operation for bisimulator.
		Third one is the equivalence notion (strong, branching, ...).
		Fourth one is the second model (LTS in BCG format).
	 */
	public static final String SVL_COMPARISON_CHECKING =
		"% bcg_open \"{0}.bcg\" bisimulator -{1} -{2} -diag \"{3}.bcg\"\n"
	;

	/*
		Template for making a working copy in SVL.
		First argument is the source model file (LTS in BCG format).
		Second argument is the target model file (LTS in BCG format).
	 */
	public static final String SVL_COPY =
		"% bcg_io \"{0}.bcg\" \"{1}.bcg\"\n"
	;

	/*
		Template of the verification of formula over a model.
		First argument is the model file (LTS in BCG format).
		Second one is the formula (MCG) file.
	 */
	public static final String SVL_FORMULA_CHECKING =
		"% bcg_open \"{0}.bcg\" evaluator4 -diag \"{1}\"\n"
	;

	/*
		Template for hiding in SVL.
		First and fourth arguments are the model file (LTS in BCG format).
		Second argument is the hiding mode (hiding or hiding all but).
		Third argument is the list of elements to hide (or hide but).
	 */
	public static final String SVL_HIDING =
		"\"{0}.bcg\" = total {1} {2} in \"{0}.bcg\"\n"
	;

	/*
		Template for renaming in SVL.
		First and third arguments are the model file (LTS in BCG format).
		Second argument is the relabelling function.
	 */
	public static final String SVL_RENAMING =
		"\"{0}.bcg\" = total rename {1} in \"{0}.bcg\"\n"
	;

	private Template()
	{

	}
}
