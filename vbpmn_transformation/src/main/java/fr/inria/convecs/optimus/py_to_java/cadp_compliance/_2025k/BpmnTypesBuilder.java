package fr.inria.convecs.optimus.py_to_java.cadp_compliance._2025k;

import fr.inria.convecs.optimus.constants.Bpmn;
import fr.inria.convecs.optimus.constants.Constant;
import fr.inria.convecs.optimus.constants.Lnt;
import fr.inria.convecs.optimus.py_to_java.cadp_compliance.generics.BpmnTypesBuilderGeneric;
import fr.inria.convecs.optimus.util.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;

public class BpmnTypesBuilder extends BpmnTypesBuilderGeneric
{
	public BpmnTypesBuilder()
	{

	}

	public void dumpBpmnTypesFile()
	{
		final StringBuilder bpmnTypesBuilder = new StringBuilder();
		this.writeFilePreamble(bpmnTypesBuilder);
		this.writeModulePrologue(bpmnTypesBuilder);
		this.writeIdsLntType(bpmnTypesBuilder);
		this.writeFlowLntType(bpmnTypesBuilder);
		this.writeSetOfFlowsLntType(bpmnTypesBuilder);
		this.writeTaskLntType(bpmnTypesBuilder);
		this.writeSetOfTasksLntType(bpmnTypesBuilder);
		this.writeInitialEventLntType(bpmnTypesBuilder);
		this.writeEndEventLntType(bpmnTypesBuilder);
		this.writeSetOfEndEventsLntType(bpmnTypesBuilder);
		this.writeGatewayTypeLntType(bpmnTypesBuilder);
		this.writeGatewayPatternLntType(bpmnTypesBuilder);
		this.writeGatewayLntType(bpmnTypesBuilder);
		this.writeSetOfGatewaysLntType(bpmnTypesBuilder);
		this.writeNodeLntType(bpmnTypesBuilder);
		this.writeSetOfNodesLntType(bpmnTypesBuilder);
		this.writeBpmnProcessLntType(bpmnTypesBuilder);
		this.writeIsMergePossibleLntFunction(bpmnTypesBuilder);
		this.writeFindIncfLntFunction(bpmnTypesBuilder);
		this.writeFindIncfNodesLntFunction(bpmnTypesBuilder);
		this.writeFindIncfGatewaysLntFunction(bpmnTypesBuilder);
		this.writeFindActiveTokensLntFunction(bpmnTypesBuilder);
		this.writeIsMergePossibleV2LntFunction(bpmnTypesBuilder);
		this.writeIsSyncDoneLntFunction(bpmnTypesBuilder);
		this.writeIsMergePossibleParLntFunction(bpmnTypesBuilder);
		this.writeCheckAfUpstreamLntFunction(bpmnTypesBuilder);
		this.writeFindFlowSourceLntFunction(bpmnTypesBuilder);
		this.writeTraverseFlowsLntFunction(bpmnTypesBuilder);
		this.writeGetIncfByIdLntFunction(bpmnTypesBuilder);
		this.writeTraverseNodesLntFunction(bpmnTypesBuilder);
		this.writeTraverseGatewaysLntFunction(bpmnTypesBuilder);
		this.writeTraverseFinalsLntFunction(bpmnTypesBuilder);
		this.writeTraverseTasksLntFunction(bpmnTypesBuilder);
		this.writeRemoveIncfLntFunction(bpmnTypesBuilder);
		this.writeRemoveSyncLntFunction(bpmnTypesBuilder);
		this.writeRemoveIdsFromSetLntFunction(bpmnTypesBuilder);
		this.writeFindIncfNodesAllLntFunction(bpmnTypesBuilder);
		this.writeFindIncfGatewaysV2LntFunction(bpmnTypesBuilder);
		this.writeFindIncfFinalsLntFunction(bpmnTypesBuilder);
		this.writeFindIncfTasksLntFunction(bpmnTypesBuilder);
		this.writeModuleEpilogue(bpmnTypesBuilder);

		final File bpmnTypesFile = new File(this.outputDirectory + File.separator + Bpmn.TYPES_FILENAME);
		final PrintWriter printWriter;

		try
		{
			printWriter = new PrintWriter(bpmnTypesFile);
		}
		catch (FileNotFoundException e)
		{
			throw new RuntimeException(e);
		}

		printWriter.print(bpmnTypesBuilder);
		printWriter.flush();
		printWriter.close();
	}

	// Private methods

	private void writeLntSeparation(final StringBuilder builder)
	{
		builder.append(Constant.DOUBLE_LINE_FEED)
				.append(Lnt.STANDARD_SEPARATOR)
				.append(Constant.DOUBLE_LINE_FEED);
	}

	private void writeFilePreamble(final StringBuilder builder)
	{
		builder // First line:
				// (* BPMN data types (FACS'16), necessary for encoding unbalanced workflows *)
				.append(Lnt.OPEN_MULTILINE_COMMENTARY)
				.append(Constant.SPACE)
				.append("BPMN data types (FACS'16), necessary for encoding unbalanced workflows")
				.append(Constant.SPACE)
				.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
				.append(Constant.LINE_FEED)

				// Second line:
				// (* AUTHOR: Gwen Salaun *)
				.append(Lnt.OPEN_MULTILINE_COMMENTARY)
				.append(Constant.SPACE)
				.append("AUTHOR: Gwen Salaun")
				.append(Constant.SPACE)
				.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
				.append(Constant.DOUBLE_LINE_FEED);
	}

	private void writeModulePrologue(final StringBuilder builder)
	{
		builder // First line:
				// module bpmntypes(id) with get, <, == is
				.append(Lnt.MODULE)
				.append(Constant.SPACE)
				.append(Bpmn.TYPES_LNT_MODULE)
				.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
				.append(Bpmn.ID_MODULE_NAME)
				.append(Constant.RIGHT_PARENTHESIS_AND_SPACE)
				.append(Lnt.WITH)
				.append(Constant.SPACE)
				.append(Lnt.GET)
				.append(Constant.COMA_AND_SPACE)
				.append(Lnt.LOWER_THAN_OPERATOR)
				.append(Constant.COMA_AND_SPACE)
				.append(Lnt.EQUALS_OPERATOR)
				.append(Constant.SPACE)
				.append(Lnt.IS);

		this.writeLntSeparation(builder);
	}

	private void writeIdsLntType(final StringBuilder builder)
	{
		builder // First line:
				// (*----- Set of BPMN Identifiers LNT Type -----*)
				.append(Lnt.OPEN_MULTILINE_COMMENTARY)
				.append(Utils.getDashesStringOfSize(5))
				.append(Constant.SPACE)
				.append("Set of BPMN Identifiers LNT Type")
				.append(Constant.SPACE)
				.append(Utils.getDashesStringOfSize(5))
				.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
				.append(Constant.DOUBLE_LINE_FEED)

				// Second line:
				// type IDS is !card 48000
				.append(Lnt.TYPE)
				.append(Constant.SPACE)
				.append(Bpmn.SET_OF_IDS_LNT_TYPE)
				.append(Constant.SPACE)
				.append(Lnt.IS)
				.append(Constant.SPACE)
				.append(Lnt.PRAGMA_CARDINAL)
				.append(Constant.SPACE)
				.append(Bpmn.SET_OF_IDS_LNT_TYPE_CARDINAL)
				.append(Constant.LINE_FEED)

				// Third line:
				// set of ID
				.append(Utils.indentLNT(1))
				.append(Lnt.SET)
				.append(Lnt.SPACED_OF)
				.append(Bpmn.ID_LNT_TYPE)
				.append(Constant.LINE_FEED)

				// Fourth line:
				// with ==, !=, inter, card, empty, member, insert, union, remove, minus
				.append(Utils.indentLNT(1))
				.append(Lnt.WITH)
				.append(Constant.SPACE)
				.append(Lnt.EQUALS_OPERATOR)
				.append(Constant.COMA_AND_SPACE)
				.append(Lnt.NOT_EQUALS_OPERATOR)
				.append(Constant.COMA_AND_SPACE)
				.append(Lnt.PREDEFINED_FUNCTION_INTERSECTION)
				.append(Constant.COMA_AND_SPACE)
				.append(Lnt.PREDEFINED_FUNCTION_CARDINAL)
				.append(Constant.COMA_AND_SPACE)
				.append(Lnt.PREDEFINED_FUNCTION_EMPTY)
				.append(Constant.COMA_AND_SPACE)
				.append(Lnt.PREDEFINED_FUNCTION_MEMBER)
				.append(Constant.COMA_AND_SPACE)
				.append(Lnt.PREDEFINED_FUNCTION_INSERT)
				.append(Constant.COMA_AND_SPACE)
				.append(Lnt.PREDEFINED_FUNCTION_UNION)
				.append(Constant.COMA_AND_SPACE)
				.append(Lnt.PREDEFINED_FUNCTION_REMOVE)
				.append(Constant.COMA_AND_SPACE)
				.append(Lnt.PREDEFINED_FUNCTION_MINUS)
				.append(Constant.LINE_FEED)

				// Fifth line:
				// end type
				.append(Lnt.END_TYPE);

		this.writeLntSeparation(builder);
	}

	private void writeFlowLntType(final StringBuilder builder)
	{
		builder // First line:
				// (*----- BPMN Sequence Flow LNT Type -----*)
				.append(Lnt.OPEN_MULTILINE_COMMENTARY)
				.append(Utils.getDashesStringOfSize(5))
				.append(Constant.SPACE)
				.append("BPMN Sequence Flow LNT Type")
				.append(Constant.SPACE)
				.append(Utils.getDashesStringOfSize(5))
				.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
				.append(Constant.DOUBLE_LINE_FEED)

				// Second line:
				// type FLOW is !card 100
				.append(Lnt.TYPE)
				.append(Constant.SPACE)
				.append(Bpmn.FLOW_LNT_TYPE)
				.append(Lnt.SPACED_IS)
				.append(Lnt.PRAGMA_CARDINAL)
				.append(Constant.SPACE)
				.append(Bpmn.FLOW_LNT_TYPE_CARDINAL)
				.append(Constant.LINE_FEED)

				// Third line:
				// flow (ident, source, target: ID)
				.append(Utils.indentLNT(1))
				.append(Lnt.generateObjectConstructor(
						Bpmn.FLOW,
						new Lnt.ParametersAndType(
								Arrays.asList(Bpmn.IDENT_VARIABLE, Bpmn.SOURCE_LNT_VARIABLE, Bpmn.TARGET_VARIABLE),
								Bpmn.ID_LNT_TYPE
						)
				))
				.append(Constant.LINE_FEED)

				// Fourth line:
				// end type
				.append(Lnt.END_TYPE);

		this.writeLntSeparation(builder);
	}

	private void writeSetOfFlowsLntType(final StringBuilder builder)
	{
		builder    // First line:
				// (*----- Set of BPMN Sequence Flows LNT Type -----*)
				.append(Lnt.OPEN_MULTILINE_COMMENTARY)
				.append(Utils.getDashesStringOfSize(5))
				.append(Constant.SPACE)
				.append("Set of BPMN Sequence Flows LNT Type")
				.append(Constant.SPACE)
				.append(Utils.getDashesStringOfSize(5))
				.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
				.append(Constant.DOUBLE_LINE_FEED)

				// Second line:
				// type FLOWS is !card 100
				.append(Lnt.TYPE)
				.append(Constant.SPACE)
				.append(Bpmn.SET_OF_FLOWS_LNT_TYPE)
				.append(Lnt.SPACED_IS)
				.append(Lnt.PRAGMA_CARDINAL)
				.append(Constant.SPACE)
				.append(Bpmn.SET_OF_FLOWS_LNT_TYPE_CARDINAL)
				.append(Constant.LINE_FEED)

				// Third line:
				// set of FLOW
				.append(Utils.indentLNT(1))
				.append(Lnt.SET)
				.append(Lnt.SPACED_OF)
				.append(Bpmn.FLOW_LNT_TYPE)
				.append(Constant.LINE_FEED)

				// Fourth line:
				// end type
				.append(Lnt.END_TYPE);

		this.writeLntSeparation(builder);
	}

	private void writeTaskLntType(final StringBuilder builder)
	{
		builder // First line:
				// (*----- BPMN Task LNT Type -----*)
				.append(Lnt.OPEN_MULTILINE_COMMENTARY)
				.append(Utils.getDashesStringOfSize(5))
				.append(Constant.SPACE)
				.append("BPMN Task LNT Type")
				.append(Constant.SPACE)
				.append(Utils.getDashesStringOfSize(5))
				.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
				.append(Constant.DOUBLE_LINE_FEED)

				// Second line:
				// type TASK is !card 100
				.append(Lnt.TYPE)
				.append(Constant.SPACE)
				.append(Bpmn.TASK_LNT_TYPE)
				.append(Lnt.SPACED_IS)
				.append(Lnt.PRAGMA_CARDINAL)
				.append(Constant.SPACE)
				.append(Bpmn.TASK_LNT_TYPE_CARDINAL)
				.append(Constant.LINE_FEED)

				// Third line:
				// task (ident: ID, incf, outf: IDS)
				.append(Utils.indentLNT(1))
				.append(Lnt.generateObjectConstructor(
						Bpmn.TASK,
						new Lnt.ParametersAndType(Bpmn.IDENT_VARIABLE, Bpmn.ID_LNT_TYPE),
						new Lnt.ParametersAndType(
								Arrays.asList(Bpmn.INCOMING_FLOW_VARIABLE, Bpmn.OUTGOING_FLOW_VARIABLE),
								Bpmn.SET_OF_IDS_LNT_TYPE
						)
				))
				.append(Constant.LINE_FEED)

				// Fourth line:
				// end type
				.append(Lnt.END_TYPE);

		this.writeLntSeparation(builder);
	}

	private void writeSetOfTasksLntType(final StringBuilder builder)
	{
		builder // First line:
				// (*----- Set of BPMN Tasks LNT Type -----*)
				.append(Lnt.OPEN_MULTILINE_COMMENTARY)
				.append(Utils.getDashesStringOfSize(5))
				.append(Constant.SPACE)
				.append("Set of BPMN Tasks LNT Type")
				.append(Constant.SPACE)
				.append(Utils.getDashesStringOfSize(5))
				.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
				.append(Constant.DOUBLE_LINE_FEED)

				// Second line:
				// type TASKS is !card 100
				.append(Lnt.TYPE)
				.append(Constant.SPACE)
				.append(Bpmn.SET_OF_TASKS_LNT_TYPE)
				.append(Lnt.SPACED_IS)
				.append(Lnt.PRAGMA_CARDINAL)
				.append(Constant.SPACE)
				.append(Bpmn.SET_OF_TASKS_LNT_TYPE_CARDINAL)
				.append(Constant.LINE_FEED)

				// Third line:
				// set of TASK
				.append(Utils.indentLNT(1))
				.append(Lnt.SET)
				.append(Lnt.SPACED_OF)
				.append(Bpmn.TASK_LNT_TYPE)
				.append(Constant.LINE_FEED)

				// Fourth line:
				// end type
				.append(Lnt.END_TYPE);

		this.writeLntSeparation(builder);
	}

	private void writeInitialEventLntType(final StringBuilder builder)
	{
		builder // First line:
				// (*----- BPMN Initial Event LNT Type -----*)
				.append(Lnt.OPEN_MULTILINE_COMMENTARY)
				.append(Utils.getDashesStringOfSize(5))
				.append(Constant.SPACE)
				.append("BPMN Initial Event LNT Type")
				.append(Constant.SPACE)
				.append(Utils.getDashesStringOfSize(5))
				.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
				.append(Constant.DOUBLE_LINE_FEED)

				// Second line:
				// type INITIAL is !card 100
				.append(Lnt.TYPE)
				.append(Constant.SPACE)
				.append(Bpmn.INITIAL_EVENT_LNT_TYPE)
				.append(Lnt.SPACED_IS)
				.append(Lnt.PRAGMA_CARDINAL)
				.append(Constant.SPACE)
				.append(Bpmn.INITIAL_EVENT_LNT_TYPE_CARDINAL)
				.append(Constant.LINE_FEED)

				// Third line:
				// initial (ident, outf: ID)  (*----- several outgoing flows (?) -----*)
				.append(Utils.indentLNT(1))
				.append(Lnt.generateObjectConstructor(
						Bpmn.INITIAL_VARIABLE,
						new Lnt.ParametersAndType(
								Arrays.asList(Bpmn.IDENT_VARIABLE, Bpmn.OUTGOING_FLOW_VARIABLE),
								Bpmn.ID_LNT_TYPE
						)
				))
				.append(Utils.indent(2))
				.append(Lnt.OPEN_MULTILINE_COMMENTARY)
				.append(Utils.getDashesStringOfSize(5))
				.append(Constant.SPACE)
				.append("Several outgoing flows (?)")
				.append(Constant.SPACE)
				.append(Utils.getDashesStringOfSize(5))
				.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
				.append(Constant.LINE_FEED)

				// Fourth line:
				// end type
				.append(Lnt.END_TYPE);

		this.writeLntSeparation(builder);
	}

	private void writeEndEventLntType(final StringBuilder builder)
	{
		builder // First line:
				// (*----- BPMN End Event LNT Type -----*)
				.append(Lnt.OPEN_MULTILINE_COMMENTARY)
				.append(Utils.getDashesStringOfSize(5))
				.append(Constant.SPACE)
				.append("BPMN End Event LNT Type")
				.append(Constant.SPACE)
				.append(Utils.getDashesStringOfSize(5))
				.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
				.append(Constant.DOUBLE_LINE_FEED)

				// Second line:
				// type FINAL is !card 100
				.append(Lnt.TYPE)
				.append(Constant.SPACE)
				.append(Bpmn.END_EVENT_LNT_TYPE)
				.append(Lnt.SPACED_IS)
				.append(Lnt.PRAGMA_CARDINAL)
				.append(Constant.SPACE)
				.append(Bpmn.END_EVENT_LNT_TYPE_CARDINAL)
				.append(Constant.LINE_FEED)

				// Third line:
				// final (ident: ID, incf: IDS)  (*----- several incoming flows (?) -----*)
				.append(Utils.indentLNT(1))
				.append(Lnt.generateObjectConstructor(
						Bpmn.FINAL,
						new Lnt.ParametersAndType(Bpmn.IDENT_VARIABLE, Bpmn.ID_LNT_TYPE),
						new Lnt.ParametersAndType(Bpmn.INCOMING_FLOW_VARIABLE, Bpmn.SET_OF_IDS_LNT_TYPE)
				))
				.append(Utils.indent(2))
				.append(Lnt.OPEN_MULTILINE_COMMENTARY)
				.append(Utils.getDashesStringOfSize(5))
				.append(Constant.SPACE)
				.append("Several incoming flows (?)")
				.append(Constant.SPACE)
				.append(Utils.getDashesStringOfSize(5))
				.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
				.append(Constant.LINE_FEED)

				// Fourth line:
				// end type
				.append(Lnt.END_TYPE);

		this.writeLntSeparation(builder);
	}

	private void writeSetOfEndEventsLntType(final StringBuilder builder)
	{
		builder // First line:
				// (*----- Set of BPMN End Events LNT Type -----*)
				.append(Lnt.OPEN_MULTILINE_COMMENTARY)
				.append(Utils.getDashesStringOfSize(5))
				.append(Constant.SPACE)
				.append("Set of BPMN End Events LNT Type")
				.append(Constant.SPACE)
				.append(Utils.getDashesStringOfSize(5))
				.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
				.append(Constant.DOUBLE_LINE_FEED)

				// Second line:
				// type FINALS is !card 100
				.append(Lnt.TYPE)
				.append(Constant.SPACE)
				.append(Bpmn.SET_OF_END_EVENTS_LNT_TYPE)
				.append(Lnt.SPACED_IS)
				.append(Lnt.PRAGMA_CARDINAL)
				.append(Constant.SPACE)
				.append(Bpmn.SET_OF_END_EVENTS_LNT_TYPE_CARDINAL)
				.append(Constant.LINE_FEED)

				// Third line:
				// set of FINAL
				.append(Utils.indentLNT(1))
				.append(Lnt.SET)
				.append(Lnt.SPACED_OF)
				.append(Bpmn.END_EVENT_LNT_TYPE)
				.append(Constant.LINE_FEED)

				// Fourth line:
				// end type
				.append(Lnt.END_TYPE);

		this.writeLntSeparation(builder);
	}

	private void writeGatewayTypeLntType(final StringBuilder builder)
	{
		builder // First line:
				// (*----- BPMN Gateway Type LNT Type -----*)
				.append(Lnt.OPEN_MULTILINE_COMMENTARY)
				.append(Utils.getDashesStringOfSize(5))
				.append(Constant.SPACE)
				.append("BPMN Gateway Type LNT Type")
				.append(Constant.SPACE)
				.append(Utils.getDashesStringOfSize(5))
				.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
				.append(Constant.DOUBLE_LINE_FEED)

				// Second line:
				// type GSORT is
				.append(Lnt.TYPE)
				.append(Constant.SPACE)
				.append(Bpmn.GATEWAY_TYPE_LNT_TYPE)
				.append(Constant.SPACE)
				.append(Lnt.IS)
				.append(Constant.LINE_FEED)

				// Third line:
				// xor, and, or
				.append(Utils.indentLNT(1))
				.append(Bpmn.EXCLUSIVE_TYPE)
				.append(Constant.COMA_AND_SPACE)
				.append(Bpmn.PARALLEL_TYPE)
				.append(Constant.COMA_AND_SPACE)
				.append(Bpmn.INCLUSIVE_TYPE)
				.append(Constant.LINE_FEED)

				// Fourth line:
				// end type
				.append(Lnt.END_TYPE);

		this.writeLntSeparation(builder);
	}

	private void writeGatewayPatternLntType(final StringBuilder builder)
	{
		builder // First line:
				// (*----- BPMN Gateway Pattern LNT Type -----*)
				.append(Lnt.OPEN_MULTILINE_COMMENTARY)
				.append(Utils.getDashesStringOfSize(5))
				.append(Constant.SPACE)
				.append("BPMN Gateway Pattern LNT Type")
				.append(Constant.SPACE)
				.append(Utils.getDashesStringOfSize(5))
				.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
				.append(Constant.DOUBLE_LINE_FEED)

				// Second line:
				// type GPATTERN is
				.append(Lnt.TYPE)
				.append(Constant.SPACE)
				.append(Bpmn.GATEWAY_PATTERN_LNT_TYPE)
				.append(Constant.SPACE)
				.append(Lnt.IS)
				.append(Constant.LINE_FEED)

				// Third line:
				// split, merge
				.append(Utils.indentLNT(1))
				.append(Bpmn.SPLIT_TYPE)
				.append(Constant.COMA_AND_SPACE)
				.append(Bpmn.MERGE_TYPE)
				.append(Constant.LINE_FEED)

				// Fourth line:
				// end type
				.append(Lnt.END_TYPE);

		this.writeLntSeparation(builder);
	}

	private void writeGatewayLntType(final StringBuilder builder)
	{
		builder // First line:
				// (*----- BPMN Gateway LNT Type -----*)
				.append(Lnt.OPEN_MULTILINE_COMMENTARY)
				.append(Utils.getDashesStringOfSize(5))
				.append(Constant.SPACE)
				.append("BPMN Gateway LNT Type")
				.append(Constant.SPACE)
				.append(Utils.getDashesStringOfSize(5))
				.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
				.append(Constant.DOUBLE_LINE_FEED)

				// Second line:
				// type GATEWAY is !card 100
				.append(Lnt.TYPE)
				.append(Constant.SPACE)
				.append(Bpmn.GATEWAY_LNT_TYPE)
				.append(Constant.SPACE)
				.append(Lnt.IS)
				.append(Constant.SPACE)
				.append(Lnt.PRAGMA_CARDINAL)
				.append(Constant.SPACE)
				.append(Bpmn.GATEWAY_LNT_TYPE_CARDINAL)
				.append(Constant.LINE_FEED)

				// Third line:
				// gateway (ident: ID, pattern: GPATTERN, sort: GSORT, incf, outf: IDS)
				.append(Utils.indentLNT(1))
				.append(Lnt.generateObjectConstructor(
						Bpmn.GATEWAY,
						new Lnt.ParametersAndType(Bpmn.IDENT_VARIABLE, Bpmn.ID_LNT_TYPE),
						new Lnt.ParametersAndType(Bpmn.PATTERN_VARIABLE, Bpmn.GATEWAY_PATTERN_LNT_TYPE),
						new Lnt.ParametersAndType(Bpmn.SORT_VARIABLE, Bpmn.GATEWAY_TYPE_LNT_TYPE),
						new Lnt.ParametersAndType(
								Arrays.asList(Bpmn.INCOMING_FLOW_VARIABLE, Bpmn.OUTGOING_FLOW_VARIABLE),
								Bpmn.SET_OF_IDS_LNT_TYPE
						)
				))
				.append(Constant.LINE_FEED)

				// Fourth line:
				// end type
				.append(Lnt.END_TYPE);

		this.writeLntSeparation(builder);
	}

	private void writeSetOfGatewaysLntType(final StringBuilder builder)
	{
		builder // First line:
				// (*----- Set of BPMN Gateways LNT Type -----*)
				.append(Lnt.OPEN_MULTILINE_COMMENTARY)
				.append(Utils.getDashesStringOfSize(5))
				.append(Constant.SPACE)
				.append("Set of BPMN Gateways LNT Type")
				.append(Constant.SPACE)
				.append(Utils.getDashesStringOfSize(5))
				.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
				.append(Constant.DOUBLE_LINE_FEED)

				// Second line:
				// type GATEWAYS is !card 100
				.append(Lnt.TYPE)
				.append(Constant.SPACE)
				.append(Bpmn.SET_OF_GATEWAYS_LNT_TYPE)
				.append(Lnt.SPACED_IS)
				.append(Lnt.PRAGMA_CARDINAL)
				.append(Constant.SPACE)
				.append(Bpmn.SET_OF_GATEWAYS_LNT_TYPE_CARDINAL)
				.append(Constant.LINE_FEED)

				// Third line:
				// set of GATEWAY
				.append(Utils.indentLNT(1))
				.append(Lnt.SET)
				.append(Lnt.SPACED_OF)
				.append(Bpmn.GATEWAY_LNT_TYPE)
				.append(Constant.LINE_FEED)

				// Fourth line:
				// end type
				.append(Lnt.END_TYPE);

		this.writeLntSeparation(builder);
	}

	private void writeNodeLntType(final StringBuilder builder)
	{
		builder // First line:
				// (*----- BPMN Generic Node LNT Type -----*)
				.append(Lnt.OPEN_MULTILINE_COMMENTARY)
				.append(Utils.getDashesStringOfSize(5))
				.append(Constant.SPACE)
				.append("BPMN Generic Node LNT Type")
				.append(Constant.SPACE)
				.append(Utils.getDashesStringOfSize(5))
				.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
				.append(Constant.DOUBLE_LINE_FEED)

				// Second line:
				// type NODE is !card 100          (*----- could it be simpler ? -----*)
				.append(Lnt.TYPE)
				.append(Constant.SPACE)
				.append(Bpmn.NODE_LNT_TYPE)
				.append(Lnt.SPACED_IS)
				.append(Lnt.PRAGMA_CARDINAL)
				.append(Constant.SPACE)
				.append(Bpmn.NODE_LNT_TYPE_CARDINAL)
				.append(Utils.indent(10))
				.append(Lnt.OPEN_MULTILINE_COMMENTARY)
				.append(Utils.getDashesStringOfSize(5))
				.append(Constant.SPACE)
				.append("Could it be simpler?")
				.append(Constant.SPACE)
				.append(Utils.getDashesStringOfSize(5))
				.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
				.append(Constant.LINE_FEED)

				// Third line:
				// i (initial: INITIAL),
				.append(Utils.indentLNT(1))
				.append(Lnt.generateObjectConstructor(
						Bpmn.INITIAL_NODES_IDENTIFIER,
						new Lnt.ParametersAndType(Bpmn.INITIAL_VARIABLE, Bpmn.INITIAL_EVENT_LNT_TYPE)
				))
				.append(Constant.COMA)
				.append(Constant.LINE_FEED)

				// Fourth line:
				// f (finals: FINALS),
				.append(Utils.indentLNT(1))
				.append(Lnt.generateObjectConstructor(
						Bpmn.END_EVENTS_IDENTIFIER,
						new Lnt.ParametersAndType(Bpmn.FINALS_VARIABLE, Bpmn.SET_OF_END_EVENTS_LNT_TYPE)
				))
				.append(Constant.COMA)
				.append(Constant.LINE_FEED)

				// Fifth line:
				// g (gateways: GATEWAYS),
				.append(Utils.indentLNT(1))
				.append(Lnt.generateObjectConstructor(
						Bpmn.GATEWAYS_IDENTIFIER,
						new Lnt.ParametersAndType(Bpmn.GATEWAYS_VARIABLE, Bpmn.SET_OF_GATEWAYS_LNT_TYPE)
				))
				.append(Constant.COMA)
				.append(Constant.LINE_FEED)

				// Sixth line:
				// t (tasks: TASKS)
				.append(Utils.indentLNT(1))
				.append(Lnt.generateObjectConstructor(
						Bpmn.TASKS_IDENTIFIER,
						new Lnt.ParametersAndType(Bpmn.TASKS_VARIABLE, Bpmn.SET_OF_TASKS_LNT_TYPE)
				))
				.append(Constant.LINE_FEED)

				// Seventh line:
				// end type
				.append(Lnt.END_TYPE);

		this.writeLntSeparation(builder);
	}

	private void writeSetOfNodesLntType(final StringBuilder builder)
	{
		builder // First line:
				// (*----- Set of BPMN Generic Nodes LNT Type -----*)
				.append(Lnt.OPEN_MULTILINE_COMMENTARY)
				.append(Utils.getDashesStringOfSize(5))
				.append(Constant.SPACE)
				.append("Set of BPMN Generic Nodes LNT Type")
				.append(Constant.SPACE)
				.append(Utils.getDashesStringOfSize(5))
				.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
				.append(Constant.DOUBLE_LINE_FEED)

				// Second line:
				// type NODES is !card 100
				.append(Lnt.TYPE)
				.append(Constant.SPACE)
				.append(Bpmn.SET_OF_NODES_LNT_TYPE)
				.append(Lnt.SPACED_IS)
				.append(Lnt.PRAGMA_CARDINAL)
				.append(Constant.SPACE)
				.append(Bpmn.SET_OF_NODES_LNT_TYPE_CARDINAL)
				.append(Constant.LINE_FEED)

				// Third line:
				// set of NODE
				.append(Utils.indentLNT(1))
				.append(Lnt.SET)
				.append(Lnt.SPACED_OF)
				.append(Bpmn.NODE_LNT_TYPE)
				.append(Constant.LINE_FEED)

				// Fourth line:
				// end type
				.append(Lnt.END_TYPE);

		this.writeLntSeparation(builder);
	}

	private void writeBpmnProcessLntType(final StringBuilder builder)
	{
		builder // First line:
				// (*----- BPMN Process LNT Type -----*)
				.append(Lnt.OPEN_MULTILINE_COMMENTARY)
				.append(Utils.getDashesStringOfSize(5))
				.append(Constant.SPACE)
				.append("BPMN Process LNT Type")
				.append(Constant.SPACE)
				.append(Utils.getDashesStringOfSize(5))
				.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
				.append(Constant.LINE_FEED)

				// Second line:
				// (*----- not the most optimized encoding for traversals -----*)
				.append(Lnt.OPEN_MULTILINE_COMMENTARY)
				.append(Utils.getDashesStringOfSize(5))
				.append(Constant.SPACE)
				.append("Not the most optimized encoding for traversals")
				.append(Constant.SPACE)
				.append(Utils.getDashesStringOfSize(5))
				.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
				.append(Constant.DOUBLE_LINE_FEED)

				// Third line:
				// type BPROCESS is !card 100
				.append(Lnt.TYPE)
				.append(Constant.SPACE)
				.append(Bpmn.BPMN_PROCESS_LNT_TYPE)
				.append(Constant.SPACE)
				.append(Lnt.IS)
				.append(Constant.SPACE)
				.append(Lnt.PRAGMA_CARDINAL)
				.append(Constant.SPACE)
				.append(Bpmn.BPMN_PROCESS_LNT_TYPE_CARDINAL)
				.append(Constant.LINE_FEED)

				// Fourth line:
				// proc (name: ID, nodes: NODES, flows: FLOWS)
				.append(Utils.indentLNT(1))
				.append(Lnt.generateObjectConstructor(
						Bpmn.PROCESS_IDENTIFIER,
						new Lnt.ParametersAndType(Bpmn.NAME_VARIABLE, Bpmn.ID_LNT_TYPE),
						new Lnt.ParametersAndType(Bpmn.NODES_VARIABLE, Bpmn.SET_OF_NODES_LNT_TYPE),
						new Lnt.ParametersAndType(Bpmn.FLOWS_VARIABLE, Bpmn.SET_OF_FLOWS_LNT_TYPE)
				))
				.append(Constant.LINE_FEED)

				// Fifth line:
				// end type
				.append(Lnt.END_TYPE);

		this.writeLntSeparation(builder);
	}

	private void writeIsMergePossibleLntFunction(final StringBuilder builder)
	{
		builder // First line:
				// (*----- "is_merge_possible()" LNT function -----*)
				.append(Lnt.OPEN_MULTILINE_COMMENTARY)
				.append(Utils.getDashesStringOfSize(5))
				.append(Constant.SPACE)
				.append(Constant.DOUBLE_QUOTATION_MARK)
				.append("is_merge_possible()")
				.append(Constant.DOUBLE_QUOTATION_MARK)
				.append(Constant.SPACE)
				.append("LNT Function")
				.append(Constant.SPACE)
				.append(Utils.getDashesStringOfSize(5))
				.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
				.append(Constant.DOUBLE_LINE_FEED)

				// Second, third, and fourth lines:
				// function
				//    is_merge_possible (p: BPROCESS, activeflows: IDS, mergeid: ID): Bool
				// is
				.append(Lnt.generateFunctionHeader(
						Bpmn.IS_MERGE_POSSIBLE_LNT_FUNCTION,
						Lnt.BOOLEAN_TYPE,
						true,
						new Lnt.ParametersAndType(Bpmn.PROCESS_VARIABLE, Bpmn.BPMN_PROCESS_LNT_TYPE),
						new Lnt.ParametersAndType(Bpmn.ACTIVE_FLOWS_VARIABLE, Bpmn.SET_OF_IDS_LNT_TYPE),
						new Lnt.ParametersAndType(Bpmn.MERGE_ID_VARIABLE, Bpmn.ID_LNT_TYPE)
				))
				.append(Constant.LINE_FEED)

				// Fifth line:
				// var incf: IDS, active_merge: Nat, status: Bool in
				.append(Utils.indentLNT(1))
				.append(Lnt.generateVariableDeclarationStatement(
						new Lnt.VariablesAndType(Bpmn.INCOMING_FLOW_VARIABLE, Bpmn.SET_OF_IDS_LNT_TYPE),
						new Lnt.VariablesAndType(Bpmn.ACTIVE_MERGE_VARIABLE, Lnt.NATURAL_NUMBER_TYPE),
						new Lnt.VariablesAndType(Bpmn.STATUS_VARIABLE, Lnt.BOOLEAN_TYPE)
				))
				.append(Constant.LINE_FEED)

				/*
					Sixth line:
					incf := find_incf (p, mergeid);
				 */
				.append(Utils.indentLNT(2))
				.append(Lnt.generateVariableAssignation(
						Bpmn.INCOMING_FLOW_VARIABLE,
						Lnt.generateFunctionCall(
								Bpmn.FIND_INCOMING_FLOWS_LNT_FUNCTION,
								Bpmn.PROCESS_VARIABLE,
								Bpmn.MERGE_ID_VARIABLE
						)
				))
				.append(Lnt.SEQUENTIAL_COMPOSITION_OPERATOR)
				.append(Constant.LINE_FEED)

				// Seventh line:
				// active_merge := find_active_tokens (activeflows, incf);
				.append(Utils.indentLNT(2))
				.append(Lnt.generateVariableAssignation(
						Bpmn.ACTIVE_MERGE_VARIABLE,
						Lnt.generateFunctionCall(
								Bpmn.FIND_ACTIVE_TOKENS_LNT_FUNCTION,
								Bpmn.ACTIVE_FLOWS_VARIABLE,
								Bpmn.INCOMING_FLOW_VARIABLE
						)
				))
				.append(Lnt.SEQUENTIAL_COMPOSITION_OPERATOR)
				.append(Constant.DOUBLE_LINE_FEED)

				// Eighth line:
				// if (active_merge == 0) then
				.append(Utils.indentLNT(2))
				.append(Lnt.generateIfStatement(
						Lnt.generateEqualsComparison(
								Bpmn.ACTIVE_MERGE_VARIABLE,
								String.valueOf(0)
						)
				))
				.append(Constant.LINE_FEED)

				// Ninth line:
				// status := False
				.append(Utils.indentLNT(3))
				.append(Lnt.generateVariableAssignation(
						Bpmn.STATUS_VARIABLE,
						Lnt.FALSE
				))
				.append(Constant.LINE_FEED)

				// Tenth line:
				// else
				.append(Utils.indentLNT(2))
				.append(Lnt.ELSE)
				.append(Constant.LINE_FEED)

				// Eleventh line
				// status := True
				.append(Utils.indentLNT(3))
				.append(Lnt.generateVariableAssignation(
						Bpmn.STATUS_VARIABLE,
						Lnt.TRUE
				))
				.append(Constant.LINE_FEED)

				// Twelfth line
				// end if;
				.append(Utils.indentLNT(2))
				.append(Lnt.END_IF)
				.append(Lnt.SEQUENTIAL_COMPOSITION_OPERATOR)
				.append(Constant.DOUBLE_LINE_FEED)

				// Thirteenth line
				// return status
				.append(Utils.indentLNT(2))
				.append(Lnt.generateReturnStatement(
						Bpmn.STATUS_VARIABLE
				))
				.append(Constant.LINE_FEED)

				// Fourteenth line
				// end var
				.append(Utils.indentLNT(1))
				.append(Lnt.END_VAR)
				.append(Constant.LINE_FEED)

				// Fifteenth line
				// end function
				.append(Lnt.END_FUNCTION);

		this.writeLntSeparation(builder);
	}

	private void writeFindIncfLntFunction(final StringBuilder builder)
	{
		builder // First line:
				// (*----- "find_incf()" LNT Function -----*)
				.append(Lnt.OPEN_MULTILINE_COMMENTARY)
				.append(Utils.getDashesStringOfSize(5))
				.append(Constant.SPACE)
				.append(Constant.DOUBLE_QUOTATION_MARK)
				.append("find_incf()")
				.append(Constant.DOUBLE_QUOTATION_MARK)
				.append(Constant.SPACE)
				.append("LNT Function")
				.append(Constant.SPACE)
				.append(Utils.getDashesStringOfSize(5))
				.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
				.append(Constant.DOUBLE_LINE_FEED)

				// Second line:
				// function find_incf (p: BPROCESS, mergeid: ID): IDS is
				.append(Lnt.generateFunctionHeader(
						Bpmn.FIND_INCOMING_FLOWS_LNT_FUNCTION,
						Bpmn.SET_OF_IDS_LNT_TYPE,
						false,
						new Lnt.ParametersAndType(Bpmn.PROCESS_VARIABLE, Bpmn.BPMN_PROCESS_LNT_TYPE),
						new Lnt.ParametersAndType(Bpmn.MERGE_ID_VARIABLE, Bpmn.ID_VARIABLE)
				))
				.append(Constant.LINE_FEED)

				// Third line:
				// case p
				.append(Utils.indentLNT(1))
				.append(Lnt.CASE)
				.append(Constant.SPACE)
				.append(Bpmn.PROCESS_VARIABLE)
				.append(Constant.LINE_FEED)

				// Fourth line:
				// var name: ID, nodes: NODES, flows: FLOWS in
				.append(Utils.indentLNT(2))
				.append(Lnt.generateVariableDeclarationStatement(
						new Lnt.VariablesAndType(Bpmn.NAME_VARIABLE, Bpmn.ID_LNT_TYPE),
						new Lnt.VariablesAndType(Bpmn.NODES_VARIABLE, Bpmn.SET_OF_NODES_LNT_TYPE),
						new Lnt.VariablesAndType(Bpmn.FLOWS_VARIABLE, Bpmn.SET_OF_FLOWS_LNT_TYPE)
				))
				.append(Constant.LINE_FEED)

				// Fifth line:
				// proc (name, nodes, flows) -> return find_incf_nodes (nodes, mergeid)
				.append(Utils.indentLNT(3))
				.append(Lnt.generateObjectWithArguments(
						Bpmn.PROCESS_IDENTIFIER,
						Bpmn.NAME_VARIABLE,
						Bpmn.NODES_VARIABLE,
						Bpmn.FLOWS_VARIABLE
				))
				.append(Lnt.SPACED_PATTERN_MATCHING_OPERATOR)
				.append(Lnt.generateReturnStatement(
						Lnt.generateFunctionCall(
								Bpmn.FIND_NODES_INCOMING_FLOWS_LNT_FUNCTION,
								Bpmn.NODES_VARIABLE,
								Bpmn.MERGE_ID_VARIABLE
						)
				))
				.append(Constant.LINE_FEED)

				// Sixth line:
				// end case
				.append(Utils.indentLNT(1))
				.append(Lnt.END_CASE)
				.append(Constant.LINE_FEED)

				// Seventh line:
				// end function
				.append(Lnt.END_FUNCTION);

		this.writeLntSeparation(builder);
	}

	private void writeFindIncfNodesLntFunction(final StringBuilder builder)
	{
		builder // First line:
				// (*----- "find_incf_nodes()" LNT Function -----*)
				.append(Lnt.OPEN_MULTILINE_COMMENTARY)
				.append(Utils.getDashesStringOfSize(5))
				.append(Constant.SPACE)
				.append(Constant.DOUBLE_QUOTATION_MARK)
				.append("find_incf_nodes()")
				.append(Constant.DOUBLE_QUOTATION_MARK)
				.append(Constant.SPACE)
				.append("LNT Function")
				.append(Constant.SPACE)
				.append(Utils.getDashesStringOfSize(5))
				.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
				.append(Constant.DOUBLE_LINE_FEED)

				// Second line:
				// function find_incf_nodes (nodes: NODES, mergeid: ID): IDS is
				.append(Lnt.generateFunctionHeader(
						Bpmn.FIND_NODES_INCOMING_FLOWS_LNT_FUNCTION,
						Bpmn.SET_OF_IDS_LNT_TYPE,
						false,
						new Lnt.ParametersAndType(Bpmn.NODES_VARIABLE, Bpmn.SET_OF_NODES_LNT_TYPE),
						new Lnt.ParametersAndType(Bpmn.MERGE_ID_VARIABLE, Bpmn.ID_LNT_TYPE)
				))
				.append(Constant.LINE_FEED)

				// Third line:
				// case nodes
				.append(Utils.indentLNT(1))
				.append(Lnt.CASE)
				.append(Constant.SPACE)
				.append(Bpmn.NODES_VARIABLE)
				.append(Constant.LINE_FEED)

				// Fourth line:
				// var
				.append(Utils.indentLNT(2))
				.append(Lnt.VAR)
				.append(Constant.LINE_FEED)

				// Fifth line:
				// gateways: GATEWAYS, initial: INITIAL, finals: FINALS, tasks: TASKS,
				.append(Utils.indentLNT(3))
				.append(Lnt.generateVariablesDefinition(
						Bpmn.GATEWAYS_VARIABLE,
						Bpmn.SET_OF_GATEWAYS_LNT_TYPE
				))
				.append(Constant.COMA_AND_SPACE)
				.append(Lnt.generateVariablesDefinition(
						Bpmn.INITIAL_VARIABLE,
						Bpmn.INITIAL_EVENT_LNT_TYPE
				))
				.append(Constant.COMA_AND_SPACE)
				.append(Lnt.generateVariablesDefinition(
						Bpmn.FINALS_VARIABLE,
						Bpmn.SET_OF_END_EVENTS_LNT_TYPE
				))
				.append(Constant.COMA_AND_SPACE)
				.append(Lnt.generateVariablesDefinition(
						Bpmn.TASKS_VARIABLE,
						Bpmn.SET_OF_TASKS_LNT_TYPE
				))
				.append(Constant.COMA)
				.append(Constant.LINE_FEED)

				// Sixth line:
				// tl: NODES
				.append(Utils.indentLNT(3))
				.append(Lnt.generateVariablesDefinition(
						Bpmn.TL_VARIABLE,
						Bpmn.SET_OF_NODES_LNT_TYPE
				))
				.append(Constant.LINE_FEED)

				// Seventh line:
				// in
				.append(Utils.indentLNT(2))
				.append(Lnt.IN)
				.append(Constant.LINE_FEED)

				// Eighth line:
				// cons (g (gateways), tl) -> return find_incf_gateways (gateways,
				.append(Utils.indentLNT(2))
				.append(Utils.indent(2))
				.append(Lnt.generateFunctionCall(
						Lnt.PREDEFINED_CONSTRUCTOR_FUNCTION,
						Lnt.generateObjectWithArguments(
								Bpmn.GATEWAYS_IDENTIFIER,
								Bpmn.GATEWAYS_VARIABLE
						),
						Bpmn.TL_VARIABLE
				))
				.append(Lnt.SPACED_PATTERN_MATCHING_OPERATOR)
				.append(Lnt.RETURN)
				.append(Constant.SPACE)
				.append(Bpmn.FIND_GATEWAYS_INCOMING_FLOWS_LNT_FUNCTION)
				.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
				.append(Bpmn.GATEWAYS_VARIABLE)
				.append(Constant.COMA)
				.append(Constant.LINE_FEED)

				// Ninth line:
				// mergeid)
				.append(Utils.indent(62))
				.append(Bpmn.MERGE_ID_VARIABLE)
				.append(Constant.RIGHT_PARENTHESIS)
				.append(Constant.LINE_FEED)

				// Tenth line:
				// | cons (i (initial), tl)
				.append(Utils.indentLNT(2))
				.append(Lnt.CASE_OPERATOR)
				.append(Constant.SPACE)
				.append(Lnt.generateFunctionCall(
						Lnt.PREDEFINED_CONSTRUCTOR_FUNCTION,
						Lnt.generateObjectWithArguments(
								Bpmn.INITIAL_NODES_IDENTIFIER,
								Bpmn.INITIAL_VARIABLE
						),
						Bpmn.TL_VARIABLE
				))
				.append(Constant.LINE_FEED)

				// Eleventh line:
				// | cons (f (finals), tl)
				.append(Utils.indentLNT(2))
				.append(Lnt.CASE_OPERATOR)
				.append(Constant.SPACE)
				.append(Lnt.generateFunctionCall(
						Lnt.PREDEFINED_CONSTRUCTOR_FUNCTION,
						Lnt.generateObjectWithArguments(
								Bpmn.END_EVENTS_IDENTIFIER,
								Bpmn.FINALS_VARIABLE
						),
						Bpmn.TL_VARIABLE
				))
				.append(Constant.LINE_FEED)

				// Twelfth line:
				// | cons (t (tasks), tl) -> return find_incf_nodes (tl, mergeid)
				.append(Utils.indentLNT(2))
				.append(Lnt.CASE_OPERATOR)
				.append(Constant.SPACE)
				.append(Lnt.generateFunctionCall(
						Lnt.PREDEFINED_CONSTRUCTOR_FUNCTION,
						Lnt.generateObjectWithArguments(
								Bpmn.TASKS_IDENTIFIER,
								Bpmn.TASKS_VARIABLE
						),
						Bpmn.TL_VARIABLE
				))
				.append(Lnt.SPACED_PATTERN_MATCHING_OPERATOR)
				.append(Lnt.generateReturnStatement(
						Lnt.generateFunctionCall(
								Bpmn.FIND_NODES_INCOMING_FLOWS_LNT_FUNCTION,
								Bpmn.TL_VARIABLE,
								Bpmn.MERGE_ID_VARIABLE
						)
				))
				.append(Constant.LINE_FEED)

				// Thirteenth line:
				// | nil -> return nil
				.append(Utils.indentLNT(2))
				.append(Lnt.CASE_OPERATOR)
				.append(Lnt.EMPTY_LIST)
				.append(Lnt.SPACED_PATTERN_MATCHING_OPERATOR)
				.append(Lnt.generateReturnStatement(
						Lnt.EMPTY_LIST
				))
				.append(Constant.LINE_FEED)

				// Fourteenth line:
				// end case
				.append(Utils.indentLNT(1))
				.append(Lnt.END_CASE)
				.append(Constant.LINE_FEED)

				// Fifteenth line:
				// end function
				.append(Lnt.END_FUNCTION);

		this.writeLntSeparation(builder);
	}

	private void writeFindIncfGatewaysLntFunction(final StringBuilder builder)
	{
		builder // First line:
				// (*----- "find_incf_gateways()" LNT Function -----*)
				.append(Lnt.OPEN_MULTILINE_COMMENTARY)
				.append(Utils.getDashesStringOfSize(5))
				.append(Constant.SPACE)
				.append(Constant.DOUBLE_QUOTATION_MARK)
				.append("find_incf_gateways()")
				.append(Constant.DOUBLE_QUOTATION_MARK)
				.append(Constant.SPACE)
				.append("LNT Function")
				.append(Constant.SPACE)
				.append(Utils.getDashesStringOfSize(5))
				.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
				.append(Constant.DOUBLE_LINE_FEED)

				// Second line:
				// function find_incf_gateways (gateways: GATEWAYS, mergeid: ID): IDS is
				.append(Lnt.generateFunctionHeader(
						Bpmn.FIND_GATEWAYS_INCOMING_FLOWS_LNT_FUNCTION,
						Bpmn.SET_OF_IDS_LNT_TYPE,
						false,
						new Lnt.ParametersAndType(Bpmn.GATEWAYS_VARIABLE, Bpmn.SET_OF_GATEWAYS_LNT_TYPE),
						new Lnt.ParametersAndType(Bpmn.MERGE_ID_VARIABLE, Bpmn.ID_LNT_TYPE)
				))
				.append(Constant.LINE_FEED)

				// Third line:
				// case gateways
				.append(Utils.indentLNT(1))
				.append(Lnt.CASE)
				.append(Constant.SPACE)
				.append(Bpmn.GATEWAYS_VARIABLE)
				.append(Constant.LINE_FEED)

				// Fourth line:
				// var
				.append(Utils.indentLNT(2))
				.append(Lnt.VAR)
				.append(Constant.LINE_FEED)

				// Fifth line:
				// ident: ID, pattern: GPATTERN, sort: GSORT, incf, outf: IDS,
				.append(Utils.indentLNT(3))
				.append(Lnt.generateVariablesDefinition(
						Bpmn.IDENT_VARIABLE,
						Bpmn.ID_LNT_TYPE
				))
				.append(Constant.COMA_AND_SPACE)
				.append(Lnt.generateVariablesDefinition(
						Bpmn.PATTERN_VARIABLE,
						Bpmn.GATEWAY_PATTERN_LNT_TYPE
				))
				.append(Constant.COMA_AND_SPACE)
				.append(Lnt.generateVariablesDefinition(
						Bpmn.SORT_VARIABLE,
						Bpmn.GATEWAY_TYPE_LNT_TYPE
				))
				.append(Constant.COMA_AND_SPACE)
				.append(Lnt.generateVariablesDefinition(
						Arrays.asList(Bpmn.INCOMING_FLOW_VARIABLE, Bpmn.OUTGOING_FLOW_VARIABLE),
						Bpmn.SET_OF_IDS_LNT_TYPE
				))
				.append(Constant.COMA)
				.append(Constant.LINE_FEED)

				// Sixth line:
				// tl: GATEWAYS
				.append(Utils.indentLNT(3))
				.append(Lnt.generateVariablesDefinition(
						Bpmn.TL_VARIABLE,
						Bpmn.SET_OF_GATEWAYS_LNT_TYPE
				))
				.append(Constant.LINE_FEED)

				// Seventh line:
				// in
				.append(Utils.indentLNT(2))
				.append(Lnt.IN)
				.append(Constant.LINE_FEED)

				// Eighth line:
				// cons (gateway (ident, pattern, sort, incf, outf), tl) ->
				.append(Utils.indentLNT(2))
				.append(Utils.indent(2))
				.append(Lnt.generateFunctionCall(
						Lnt.PREDEFINED_CONSTRUCTOR_FUNCTION,
						Lnt.generateObjectWithArguments(
								Bpmn.GATEWAY,
								Bpmn.IDENT_VARIABLE,
								Bpmn.PATTERN_VARIABLE,
								Bpmn.SORT_VARIABLE,
								Bpmn.INCOMING_FLOW_VARIABLE,
								Bpmn.OUTGOING_FLOW_VARIABLE
						),
						Bpmn.TL_VARIABLE
				))
				.append(Constant.SPACE)
				.append(Lnt.PATTERN_MATCHING_OPERATOR)
				.append(Constant.LINE_FEED)

				// Ninth line:
				// if (ident == mergeid) then
				.append(Utils.indentLNT(3))
				.append(Utils.indent(2))
				.append(Lnt.generateIfStatement(
						Lnt.generateEqualsComparison(
								Bpmn.IDENT_VARIABLE,
								Bpmn.MERGE_ID_VARIABLE
						)
				))
				.append(Constant.LINE_FEED)

				// Tenth line:
				// return incf
				.append(Utils.indentLNT(4))
				.append(Utils.indent(2))
				.append(Lnt.generateReturnStatement(
						Bpmn.INCOMING_FLOW_VARIABLE
				))
				.append(Constant.LINE_FEED)

				// Eleventh line:
				// else
				.append(Utils.indentLNT(3))
				.append(Utils.indent(2))
				.append(Lnt.ELSE)
				.append(Constant.LINE_FEED)

				// Twelfth line:
				// return find_incf_gateways (tl, mergeid)
				.append(Utils.indentLNT(4))
				.append(Utils.indent(2))
				.append(Lnt.generateReturnStatement(
						Lnt.generateFunctionCall(
								Bpmn.FIND_GATEWAYS_INCOMING_FLOWS_LNT_FUNCTION,
								Bpmn.TL_VARIABLE,
								Bpmn.MERGE_ID_VARIABLE
						)
				))
				.append(Constant.LINE_FEED)

				// Thirteenth line:
				// end if
				.append(Utils.indentLNT(3))
				.append(Utils.indent(2))
				.append(Lnt.END_IF)
				.append(Constant.LINE_FEED)

				// Fourteenth line:
				// | nil -> return nil
				.append(Utils.indentLNT(2))
				.append(Lnt.CASE_OPERATOR)
				.append(Constant.SPACE)
				.append(Lnt.EMPTY_LIST)
				.append(Lnt.SPACED_PATTERN_MATCHING_OPERATOR)
				.append(Lnt.generateReturnStatement(
						Lnt.EMPTY_LIST
				))
				.append(Constant.LINE_FEED)

				// Fifteenth line:
				// end case
				.append(Utils.indentLNT(1))
				.append(Lnt.END_CASE)
				.append(Constant.LINE_FEED)

				// Sixteenth line
				// end function
				.append(Lnt.END_FUNCTION);

		this.writeLntSeparation(builder);
	}

	private void writeFindActiveTokensLntFunction(final StringBuilder builder)
	{
		builder // First line:
				// (*----- "find_active_tokens()" LNT Function -----*)
				.append(Lnt.OPEN_MULTILINE_COMMENTARY)
				.append(Utils.getDashesStringOfSize(5))
				.append(Constant.SPACE)
				.append(Constant.DOUBLE_QUOTATION_MARK)
				.append("find_active_tokens()")
				.append(Constant.DOUBLE_QUOTATION_MARK)
				.append(Constant.SPACE)
				.append("LNT Function")
				.append(Constant.SPACE)
				.append(Utils.getDashesStringOfSize(5))
				.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
				.append(Constant.DOUBLE_LINE_FEED)

				// Second line:
				// function find_active_tokens (activeflows:IDS, incf:IDS): Nat is
				.append(Lnt.generateFunctionHeader(
						Bpmn.FIND_ACTIVE_TOKENS_LNT_FUNCTION,
						Lnt.NATURAL_NUMBER_TYPE,
						false,
						new Lnt.ParametersAndType(
								Arrays.asList(Bpmn.ACTIVE_FLOWS_VARIABLE, Bpmn.INCOMING_FLOW_VARIABLE),
								Bpmn.SET_OF_IDS_LNT_TYPE
						)
				))
				.append(Constant.LINE_FEED)

				// Third line:
				// var tokens: IDS, count: Nat in
				.append(Utils.indentLNT(1))
				.append(Lnt.generateVariableDeclarationStatement(
						new Lnt.VariablesAndType(Bpmn.TOKENS_VARIABLE, Bpmn.SET_OF_IDS_LNT_TYPE),
						new Lnt.VariablesAndType(Bpmn.COUNT_LNT_VARIABLE, Lnt.NATURAL_NUMBER_TYPE)
				))
				.append(Constant.LINE_FEED)

				// Fourth line:
				// tokens := inter (activeflows, incf);
				.append(Utils.indentLNT(2))
				.append(Lnt.generateVariableAssignation(
						Bpmn.TOKENS_VARIABLE,
						Lnt.generateFunctionCall(
								Lnt.PREDEFINED_FUNCTION_INTERSECTION,
								Bpmn.ACTIVE_FLOWS_VARIABLE,
								Bpmn.INCOMING_FLOW_VARIABLE
						)
				))
				.append(Lnt.SEQUENTIAL_COMPOSITION_OPERATOR)
				.append(Constant.LINE_FEED)

				// Fifth line:
				// count := card (tokens);
				.append(Utils.indentLNT(2))
				.append(Lnt.generateVariableAssignation(
						Bpmn.COUNT_LNT_VARIABLE,
						Lnt.generateFunctionCall(
								Lnt.PREDEFINED_FUNCTION_CARDINAL,
								Bpmn.TOKENS_VARIABLE
						)
				))
				.append(Lnt.SEQUENTIAL_COMPOSITION_OPERATOR)
				.append(Constant.DOUBLE_LINE_FEED)

				// Sixth line:
				// return count
				.append(Utils.indentLNT(2))
				.append(Lnt.generateReturnStatement(
						Bpmn.COUNT_LNT_VARIABLE
				))
				.append(Constant.LINE_FEED)

				// Seventh line:
				// end var
				.append(Utils.indentLNT(1))
				.append(Lnt.END_VAR)
				.append(Constant.LINE_FEED)

				// Eighth line:
				// end function
				.append(Lnt.END_FUNCTION);

		this.writeLntSeparation(builder);
	}

	private void writeIsMergePossibleV2LntFunction(final StringBuilder builder)
	{
		builder // First line:
				// (*----- "is_merge_possible_v2()" LNT Function -----*)
				.append(Lnt.OPEN_MULTILINE_COMMENTARY)
				.append(Utils.getDashesStringOfSize(5))
				.append(Constant.SPACE)
				.append(Constant.DOUBLE_QUOTATION_MARK)
				.append("is_merge_possible_v2()")
				.append(Constant.DOUBLE_QUOTATION_MARK)
				.append(Constant.SPACE)
				.append("LNT Function")
				.append(Constant.SPACE)
				.append(Utils.getDashesStringOfSize(5))
				.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
				.append(Constant.DOUBLE_LINE_FEED)

				// Second line:
				// (*-------------------------------------------------------------------------*)
				.append(Lnt.OPEN_MULTILINE_COMMENTARY)
				.append(Lnt.MAX_DASHES_IN_MULTILINE_COMMENTARY)
				.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
				.append(Constant.LINE_FEED)

				// Third line:
				// (*-----------------Check for merge with BPMN 1.x semantics-----------------*)
				.append(Lnt.OPEN_MULTILINE_COMMENTARY)
				.append(Utils.getDashesStringOfSize(16))
				.append(Constant.SPACE)
				.append("Check for merge with BPMN 1.x semantics")
				.append(Constant.SPACE)
				.append(Utils.getDashesStringOfSize(16))
				.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
				.append(Constant.LINE_FEED)

				// Fourth line
				// (*-------------------------------------------------------------------------*)
				.append(Lnt.OPEN_MULTILINE_COMMENTARY)
				.append(Lnt.MAX_DASHES_IN_MULTILINE_COMMENTARY)
				.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
				.append(Constant.DOUBLE_LINE_FEED)

				// Fifth, sixth, and seventh line
				// function
				//    is_merge_possible_v2 (p: BPROCESS, activeflows:IDS, mergeid:ID): Bool
				// is
				.append(Lnt.generateFunctionHeader(
						Bpmn.IS_MERGE_POSSIBLE_V2_LNT_FUNCTION,
						Lnt.BOOLEAN_TYPE,
						true,
						new Lnt.ParametersAndType(Bpmn.PROCESS_VARIABLE, Bpmn.BPMN_PROCESS_LNT_TYPE),
						new Lnt.ParametersAndType(Bpmn.ACTIVE_FLOWS_VARIABLE, Bpmn.SET_OF_IDS_LNT_TYPE),
						new Lnt.ParametersAndType(Bpmn.MERGE_ID_VARIABLE, Bpmn.ID_LNT_TYPE)
				))
				.append(Constant.LINE_FEED)

				// Eighth line:
				// var
				.append(Utils.indentLNT(1))
				.append(Lnt.VAR)
				.append(Constant.LINE_FEED)

				// Ninth line:
				// incf, inactiveincf, visited: IDS, active_merge: Nat, result1: Bool
				.append(Utils.indentLNT(2))
				.append(Lnt.generateVariablesDefinition(
						Arrays.asList(Bpmn.INCOMING_FLOW_VARIABLE, Bpmn.INACTIVE_INCOMING_FLOWS_VARIABLE, Bpmn.VISITED_VARIABLE),
						Bpmn.SET_OF_IDS_LNT_TYPE
				))
				.append(Constant.COMA_AND_SPACE)
				.append(Lnt.generateVariablesDefinition(
						Bpmn.ACTIVE_MERGE_VARIABLE,
						Lnt.NATURAL_NUMBER_TYPE)
				)
				.append(Constant.COMA_AND_SPACE)
				.append(Lnt.generateVariablesDefinition(
						Bpmn.RESULT_1_VARIABLE,
						Lnt.BOOLEAN_TYPE)
				)
				.append(Constant.LINE_FEED)

				// Tenth line:
				// in
				.append(Utils.indentLNT(1))
				.append(Lnt.IN)
				.append(Constant.LINE_FEED)

				// Eleventh line:
				// (*----- just iterate through gateways instead of all nodes -----*)
				.append(Utils.indentLNT(2))
				.append(Lnt.OPEN_MULTILINE_COMMENTARY)
				.append(Utils.getDashesStringOfSize(5))
				.append(Constant.SPACE)
				.append("just iterate through gateways instead of all nodes")
				.append(Constant.SPACE)
				.append(Utils.getDashesStringOfSize(5))
				.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
				.append(Constant.LINE_FEED)

				// Twelfth line:
				// visited := nil;
				.append(Utils.indentLNT(2))
				.append(Lnt.generateVariableAssignation(
						Bpmn.VISITED_VARIABLE,
						Lnt.EMPTY_LIST
				))
				.append(Lnt.SEQUENTIAL_COMPOSITION_OPERATOR)
				.append(Constant.LINE_FEED)

				// Thirteenth line:
				// incf := find_incf (p, mergeid);
				.append(Utils.indentLNT(2))
				.append(Lnt.generateVariableAssignation(
						Bpmn.INCOMING_FLOW_VARIABLE,
						Lnt.generateFunctionCall(
								Bpmn.FIND_INCOMING_FLOWS_LNT_FUNCTION,
								Bpmn.PROCESS_VARIABLE,
								Bpmn.MERGE_ID_VARIABLE
						)
				))
				.append(Lnt.SEQUENTIAL_COMPOSITION_OPERATOR)
				.append(Constant.LINE_FEED)

				// Fourteenth line:
				// active_merge := find_active_tokens (activeflows, incf);
				.append(Utils.indentLNT(2))
				.append(Lnt.generateVariableAssignation(
						Bpmn.ACTIVE_MERGE_VARIABLE,
						Lnt.generateFunctionCall(
								Bpmn.FIND_ACTIVE_TOKENS_LNT_FUNCTION,
								Bpmn.ACTIVE_FLOWS_VARIABLE,
								Bpmn.INCOMING_FLOW_VARIABLE
						)
				))
				.append(Lnt.SEQUENTIAL_COMPOSITION_OPERATOR)
				.append(Constant.DOUBLE_LINE_FEED)

				// Fifteenth line:
				// (*----- check if all the incf have tokens -----*)
				.append(Utils.indentLNT(2))
				.append(Lnt.OPEN_MULTILINE_COMMENTARY)
				.append(Utils.getDashesStringOfSize(5))
				.append(Constant.SPACE)
				.append("check if all the incf have tokens")
				.append(Constant.SPACE)
				.append(Utils.getDashesStringOfSize(5))
				.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
				.append(Constant.LINE_FEED)

				// Sixteenth line:
				// if (active_merge == card (incf)) then
				.append(Utils.indentLNT(2))
				.append(Lnt.generateIfStatement(
						Lnt.generateEqualsComparison(
								Bpmn.ACTIVE_MERGE_VARIABLE,
								Lnt.generateFunctionCall(
										Lnt.PREDEFINED_FUNCTION_CARDINAL,
										Bpmn.INCOMING_FLOW_VARIABLE
								)
						)
				))
				.append(Constant.LINE_FEED)

				// Seventeenth line:
				// return True
				.append(Utils.indentLNT(3))
				.append(Lnt.generateReturnStatement(
						Lnt.TRUE
				))
				.append(Constant.LINE_FEED)

				// Eighteenth line:
				// else
				.append(Utils.indentLNT(2))
				.append(Lnt.ELSE)
				.append(Constant.LINE_FEED)

				// Nineteenth line:
				// (*----- first remove incoming flows with active tokens -----*)
				.append(Utils.indentLNT(3))
				.append(Lnt.OPEN_MULTILINE_COMMENTARY)
				.append(Utils.getDashesStringOfSize(5))
				.append(Constant.SPACE)
				.append("first remove incoming flows with active tokens")
				.append(Constant.SPACE)
				.append(Utils.getDashesStringOfSize(5))
				.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
				.append(Constant.LINE_FEED)

				// Twentieth line:
				// inactiveincf := remove_ids_from_set (activeflows, incf);
				.append(Utils.indentLNT(3))
				.append(Lnt.generateVariableAssignation(
						Bpmn.INACTIVE_INCOMING_FLOWS_VARIABLE,
						Lnt.generateFunctionCall(
								Bpmn.REMOVE_IDS_FROM_SET_LNT_FUNCTION,
								Bpmn.ACTIVE_FLOWS_VARIABLE,
								Bpmn.INCOMING_FLOW_VARIABLE
						)
				))
				.append(Lnt.SEQUENTIAL_COMPOSITION_OPERATOR)
				.append(Constant.DOUBLE_LINE_FEED)

				// Twenty-first line:
				// (*----- then check upstream for remaining flows -----*)
				.append(Utils.indentLNT(3))
				.append(Lnt.OPEN_MULTILINE_COMMENTARY)
				.append(Utils.getDashesStringOfSize(5))
				.append(Constant.SPACE)
				.append("then check upstream for remaining flows")
				.append(Constant.SPACE)
				.append(Utils.getDashesStringOfSize(5))
				.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
				.append(Constant.LINE_FEED)

				// Twenty-second line:
				// result1 := check_af_upstream (visited?, activeflows, inactiveincf
				.append(Utils.indentLNT(3))
				.append(Bpmn.RESULT_1_VARIABLE)
				.append(Lnt.SPACED_VARIABLE_ASSIGNATION_OPERATOR)
				.append(Bpmn.CHECK_ALL_FLOWS_UPSTREAM_LNT_FUNCTION)
				.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
				.append(Lnt.markAsInputOutputParameter(Bpmn.VISITED_VARIABLE))
				.append(Constant.COMA_AND_SPACE)
				.append(Bpmn.ACTIVE_FLOWS_VARIABLE)
				.append(Constant.COMA_AND_SPACE)
				.append(Bpmn.INACTIVE_INCOMING_FLOWS_VARIABLE)
				.append(Constant.COMA)
				.append(Constant.LINE_FEED)

				// Twenty-fourth line:
				// p);
				.append(Utils.indent(39))
				.append(Bpmn.PROCESS_VARIABLE)
				.append(Constant.RIGHT_PARENTHESIS)
				.append(Lnt.SEQUENTIAL_COMPOSITION_OPERATOR)
				.append(Constant.LINE_FEED)

				// Twenty-fifth line:
				// return result1
				.append(Utils.indentLNT(3))
				.append(Lnt.generateReturnStatement(
						Bpmn.RESULT_1_VARIABLE
				))
				.append(Constant.LINE_FEED)

				// Twenty-sixth line:
				// end if
				.append(Utils.indentLNT(2))
				.append(Lnt.END_IF)
				.append(Constant.LINE_FEED)

				// Twenty-seventh line:
				// end var
				.append(Utils.indentLNT(1))
				.append(Lnt.END_VAR)
				.append(Constant.LINE_FEED)

				// Twenty-eighth line:
				// end function
				.append(Lnt.END_FUNCTION);

		this.writeLntSeparation(builder);
	}

	private void writeIsSyncDoneLntFunction(final StringBuilder builder)
	{
		builder // First line:
				// (*----- "is_sync_done()" LNT Function -----*)
				.append(Lnt.OPEN_MULTILINE_COMMENTARY)
				.append(Utils.getDashesStringOfSize(5))
				.append(Constant.SPACE)
				.append(Constant.DOUBLE_QUOTATION_MARK)
				.append("is_sync_done()")
				.append(Constant.DOUBLE_QUOTATION_MARK)
				.append(Constant.SPACE)
				.append("LNT Function")
				.append(Constant.SPACE)
				.append(Utils.getDashesStringOfSize(5))
				.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
				.append(Constant.DOUBLE_LINE_FEED)

				// Second, third, and fourth lines:
				// function
				//    is_sync_done (p: BPROCESS, activeflows, syncstore: IDS, mergeid:ID): Bool
				// is
				.append(Lnt.generateFunctionHeader(
						Bpmn.IS_SYNCHRONISATION_DONE_LNT_FUNCTION,
						Lnt.BOOLEAN_TYPE,
						true,
						new Lnt.ParametersAndType(Bpmn.PROCESS_VARIABLE, Bpmn.BPMN_PROCESS_LNT_TYPE),
						new Lnt.ParametersAndType(
								Arrays.asList(Bpmn.ACTIVE_FLOWS_VARIABLE, Bpmn.SYNC_STORE_VARIABLE),
								Bpmn.SET_OF_IDS_LNT_TYPE
						),
						new Lnt.ParametersAndType(Bpmn.MERGE_ID_VARIABLE, Bpmn.ID_LNT_TYPE)
				))
				.append(Constant.LINE_FEED)

				// Fifth line:
				// var incf, activesync: IDS in
				.append(Utils.indentLNT(1))
				.append(Lnt.generateVariableDeclarationStatement(
						new Lnt.VariablesAndType(
								Arrays.asList(Bpmn.INCOMING_FLOW_VARIABLE, Bpmn.ACTIVE_SYNC_VARIABLE),
								Bpmn.SET_OF_IDS_LNT_TYPE
						)
				))
				.append(Constant.LINE_FEED)

				// Sixth line:
				// (*----- just iterate through gateways instead of all nodes -----*)
				.append(Utils.indentLNT(2))
				.append(Lnt.OPEN_MULTILINE_COMMENTARY)
				.append(Utils.getDashesStringOfSize(5))
				.append(Constant.SPACE)
				.append("just iterate through gateways instead of all nodes")
				.append(Constant.SPACE)
				.append(Utils.getDashesStringOfSize(5))
				.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
				.append(Constant.LINE_FEED)

				// Seventh line:
				// incf := find_incf (p, mergeid);
				.append(Utils.indentLNT(2))
				.append(Lnt.generateVariableAssignation(
						Bpmn.INCOMING_FLOW_VARIABLE,
						Lnt.generateFunctionCall(
								Bpmn.FIND_INCOMING_FLOWS_LNT_FUNCTION,
								Bpmn.PROCESS_VARIABLE,
								Bpmn.MERGE_ID_VARIABLE
						)
				))
				.append(Lnt.SEQUENTIAL_COMPOSITION_OPERATOR)
				.append(Constant.LINE_FEED)

				// Eighth line:
				// activesync := inter (activeflows, incf);
				.append(Utils.indentLNT(2))
				.append(Lnt.generateVariableAssignation(
						Bpmn.ACTIVE_SYNC_VARIABLE,
						Lnt.generateFunctionCall(
								Lnt.PREDEFINED_FUNCTION_INTERSECTION,
								Bpmn.ACTIVE_FLOWS_VARIABLE,
								Bpmn.INCOMING_FLOW_VARIABLE
						)
				))
				.append(Lnt.SEQUENTIAL_COMPOSITION_OPERATOR)
				.append(Constant.DOUBLE_LINE_FEED)

				// Ninth line:
				// if (empty (activesync)) then
				.append(Utils.indentLNT(2))
				.append(Lnt.generateIfStatement(
						Lnt.generateFunctionCall(
								Lnt.PREDEFINED_FUNCTION_EMPTY,
								Bpmn.ACTIVE_SYNC_VARIABLE
						)
				))
				.append(Constant.LINE_FEED)

				// Tenth line:
				// return False
				.append(Utils.indentLNT(3))
				.append(Lnt.generateReturnStatement(
						Lnt.FALSE
				))
				.append(Constant.LINE_FEED)

				// Eleventh line:
				// elsif (inter (activesync, syncstore) == activesync) then
				.append(Utils.indentLNT(2))
				.append(Lnt.generateElsifStatement(
						Lnt.generateEqualsComparison(
								Lnt.generateFunctionCall(
										Lnt.PREDEFINED_FUNCTION_INTERSECTION,
										Bpmn.ACTIVE_SYNC_VARIABLE,
										Bpmn.SYNC_STORE_VARIABLE
								),
								Bpmn.ACTIVE_SYNC_VARIABLE
						)
				))
				.append(Constant.LINE_FEED)

				// Twelfth line:
				// return True
				.append(Utils.indentLNT(3))
				.append(Lnt.generateReturnStatement(
						Lnt.TRUE
				))
				.append(Constant.LINE_FEED)

				// Thirteenth line:
				// else
				.append(Utils.indentLNT(2))
				.append(Lnt.ELSE)
				.append(Constant.LINE_FEED)

				// Fourteenth line:
				// return False
				.append(Utils.indentLNT(3))
				.append(Lnt.generateReturnStatement(
						Lnt.FALSE
				))
				.append(Constant.LINE_FEED)

				// Fifteenth line:
				// end if
				.append(Utils.indentLNT(2))
				.append(Lnt.END_IF)
				.append(Constant.LINE_FEED)

				// Sixteenth line:
				// end var
				.append(Utils.indentLNT(1))
				.append(Lnt.END_VAR)
				.append(Constant.LINE_FEED)

				// Seventeenth line:
				// end function
				.append(Lnt.END_FUNCTION);

		this.writeLntSeparation(builder);
	}

	private void writeIsMergePossibleParLntFunction(final StringBuilder builder)
	{
		builder // First line:
				// (*----- "is_merge_possible_par()" LNT Function -----*)
				.append(Lnt.OPEN_MULTILINE_COMMENTARY)
				.append(Utils.getDashesStringOfSize(5))
				.append(Constant.SPACE)
				.append(Constant.DOUBLE_QUOTATION_MARK)
				.append("is_merge_possible_par()")
				.append(Constant.DOUBLE_QUOTATION_MARK)
				.append(Constant.SPACE)
				.append("LNT Function")
				.append(Constant.SPACE)
				.append(Utils.getDashesStringOfSize(5))
				.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
				.append(Constant.DOUBLE_LINE_FEED)

				// Second line:
				// (*----- Merge check for parallel gateways -----*)
				.append(Lnt.OPEN_MULTILINE_COMMENTARY)
				.append(Utils.getDashesStringOfSize(5))
				.append(Constant.SPACE)
				.append("Merge check for parallel gateways")
				.append(Constant.SPACE)
				.append(Utils.getDashesStringOfSize(5))
				.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
				.append(Constant.DOUBLE_LINE_FEED)

				// Third, fourth, and fifth lines:
				// function
				//    is_merge_possible_par (p: BPROCESS, syncstore: IDS, mergeid: ID): Bool
				// is
				.append(Lnt.generateFunctionHeader(
						Bpmn.IS_MERGE_POSSIBLE_PAR_LNT_FUNCTION,
						Lnt.BOOLEAN_TYPE,
						true,
						new Lnt.ParametersAndType(Bpmn.PROCESS_VARIABLE, Bpmn.BPMN_PROCESS_LNT_TYPE),
						new Lnt.ParametersAndType(Bpmn.SYNC_STORE_VARIABLE, Bpmn.SET_OF_IDS_LNT_TYPE),
						new Lnt.ParametersAndType(Bpmn.MERGE_ID_VARIABLE, Bpmn.ID_VARIABLE)
				))
				.append(Constant.LINE_FEED)

				// Sixth line:
				// var incf, activesync: IDS in
				.append(Utils.indentLNT(1))
				.append(Lnt.generateVariableDeclarationStatement(
						new Lnt.VariablesAndType(
								Arrays.asList(Bpmn.INCOMING_FLOW_VARIABLE, Bpmn.ACTIVE_SYNC_VARIABLE),
								Bpmn.SET_OF_IDS_LNT_TYPE
						)
				))
				.append(Constant.LINE_FEED)

				// Seventh line:
				// (*----- just iterate through gateways instead of all nodes -----*)
				.append(Utils.indentLNT(2))
				.append(Lnt.OPEN_MULTILINE_COMMENTARY)
				.append(Utils.getDashesStringOfSize(5))
				.append(Constant.SPACE)
				.append("just iterate through gateways instead of all nodes")
				.append(Constant.SPACE)
				.append(Utils.getDashesStringOfSize(5))
				.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
				.append(Constant.LINE_FEED)

				// Eighth line:
				// incf := find_incf (p, mergeid);
				.append(Utils.indentLNT(2))
				.append(Lnt.generateVariableAssignation(
						Bpmn.INCOMING_FLOW_VARIABLE,
						Lnt.generateFunctionCall(
								Bpmn.FIND_INCOMING_FLOWS_LNT_FUNCTION,
								Bpmn.PROCESS_VARIABLE,
								Bpmn.MERGE_ID_VARIABLE
						)
				))
				.append(Lnt.SEQUENTIAL_COMPOSITION_OPERATOR)
				.append(Constant.DOUBLE_LINE_FEED)

				// Ninth line:
				// if (inter (incf, syncstore) == incf) then
				.append(Utils.indentLNT(2))
				.append(Lnt.generateIfStatement(
						Lnt.generateEqualsComparison(
								Lnt.generateFunctionCall(
										Lnt.PREDEFINED_FUNCTION_INTERSECTION,
										Bpmn.INCOMING_FLOW_VARIABLE,
										Bpmn.SYNC_STORE_VARIABLE
								),
								Bpmn.INCOMING_FLOW_VARIABLE
						)
				))
				.append(Constant.LINE_FEED)

				// Tenth line:
				// return True
				.append(Utils.indentLNT(3))
				.append(Lnt.generateReturnStatement(
						Lnt.TRUE
				))
				.append(Constant.LINE_FEED)

				// Eleventh line:
				// else
				.append(Utils.indentLNT(2))
				.append(Lnt.ELSE)
				.append(Constant.LINE_FEED)

				// Twelfth line:
				// return False
				.append(Utils.indentLNT(3))
				.append(Lnt.generateReturnStatement(
						Lnt.FALSE
				))
				.append(Constant.LINE_FEED)

				// Thirteenth line:
				// end if
				.append(Utils.indentLNT(2))
				.append(Lnt.END_IF)
				.append(Constant.LINE_FEED)

				// Fourteenth line:
				// end var
				.append(Utils.indentLNT(1))
				.append(Lnt.END_VAR)
				.append(Constant.LINE_FEED)

				// Fifteenth line:
				// end function
				.append(Lnt.END_FUNCTION);

		this.writeLntSeparation(builder);
	}

	private void writeCheckAfUpstreamLntFunction(final StringBuilder builder)
	{
		builder // First line:
				// (*----- "check_af_upstream()" LNT Function -----*)
				.append(Lnt.OPEN_MULTILINE_COMMENTARY)
				.append(Utils.getDashesStringOfSize(5))
				.append(Constant.SPACE)
				.append(Constant.DOUBLE_QUOTATION_MARK)
				.append("check_af_upstream()")
				.append(Constant.DOUBLE_QUOTATION_MARK)
				.append(Constant.SPACE)
				.append("LNT Function")
				.append(Constant.SPACE)
				.append(Utils.getDashesStringOfSize(5))
				.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
				.append(Constant.DOUBLE_LINE_FEED)

				// Second line:
				// (*----- finds all the upstream flows and checks for tokens -----*)
				.append(Lnt.OPEN_MULTILINE_COMMENTARY)
				.append(Utils.getDashesStringOfSize(5))
				.append(Constant.SPACE)
				.append("Finds all the upstream flows and checks for tokens")
				.append(Constant.SPACE)
				.append(Utils.getDashesStringOfSize(5))
				.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
				.append(Constant.DOUBLE_LINE_FEED)

				// Third line:
				// function
				.append(Lnt.FUNCTION)
				.append(Constant.LINE_FEED)

				// Fourth line:
				// check_af_upstream (in out visited: IDS, activeflows, incf: IDS,
				.append(Utils.indentLNT(1))
				.append(Bpmn.CHECK_ALL_FLOWS_UPSTREAM_LNT_FUNCTION)
				.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
				.append(Lnt.generateVariablesDefinition(
						Lnt.PARAMETER_MODE_IN_OUT + Constant.SPACE + Bpmn.VISITED_VARIABLE,
						Bpmn.SET_OF_IDS_LNT_TYPE
				))
				.append(Constant.COMA_AND_SPACE)
				.append(Lnt.generateVariablesDefinition(
						Arrays.asList(Bpmn.ACTIVE_FLOWS_VARIABLE, Bpmn.INCOMING_FLOW_VARIABLE),
						Bpmn.SET_OF_IDS_LNT_TYPE
				))
				.append(Constant.COMA)
				.append(Constant.LINE_FEED)

				// Fifth line:
				// p: BPROCESS): Bool
				.append(Utils.indent(22))
				.append(Lnt.generateVariablesDefinition(
						Bpmn.PROCESS_VARIABLE,
						Bpmn.BPMN_PROCESS_LNT_TYPE
				))
				.append(Constant.RIGHT_PARENTHESIS)
				.append(Constant.COLON_AND_SPACE)
				.append(Lnt.BOOLEAN_TYPE)
				.append(Constant.LINE_FEED)

				// Sixth line:
				// is
				.append(Lnt.IS)
				.append(Constant.LINE_FEED)

				// Seventh line:
				// var count: Nat, result1, result2: Bool in
				.append(Utils.indentLNT(1))
				.append(Lnt.generateVariableDeclarationStatement(
						new Lnt.VariablesAndType(Bpmn.COUNT_LNT_VARIABLE, Lnt.NATURAL_NUMBER_TYPE),
						new Lnt.VariablesAndType(Arrays.asList(Bpmn.RESULT_1_VARIABLE, Bpmn.RESULT_2_VARIABLE), Lnt.BOOLEAN_TYPE)
				))
				.append(Constant.LINE_FEED)

				// Eighth line:
				// case incf
				.append(Utils.indentLNT(2))
				.append(Lnt.CASE)
				.append(Constant.SPACE)
				.append(Bpmn.INCOMING_FLOW_VARIABLE)
				.append(Constant.LINE_FEED)

				// Ninth line:
				// var hd, source: ID, tl, upflow: IDS in
				.append(Utils.indentLNT(3))
				.append(Lnt.generateVariableDeclarationStatement(
						new Lnt.VariablesAndType(Arrays.asList(Bpmn.HD_LNT_VARIABLE, Bpmn.SOURCE_LNT_VARIABLE), Bpmn.ID_LNT_TYPE),
						new Lnt.VariablesAndType(Arrays.asList(Bpmn.TL_VARIABLE, Bpmn.UPFLOW_LNT_VARIABLE), Bpmn.SET_OF_IDS_LNT_TYPE)
				))
				.append(Constant.LINE_FEED)

				// Tenth line:
				// cons(hd, tl) ->
				.append(Utils.indentLNT(4))
				.append(Lnt.generateFunctionCall(
						Lnt.PREDEFINED_CONSTRUCTOR_FUNCTION,
						Bpmn.HD_LNT_VARIABLE,
						Bpmn.TL_VARIABLE
				))
				.append(Lnt.PATTERN_MATCHING_OPERATOR)
				.append(Constant.LINE_FEED)

				// Eleventh line:
				// source := find_flow_source (p, hd);
				.append(Utils.indentLNT(5))
				.append(Lnt.generateVariableAssignation(
						Bpmn.SOURCE_LNT_VARIABLE,
						Lnt.generateFunctionCall(
								Bpmn.FIND_FLOW_SOURCE_LNT_FUNCTION,
								Bpmn.PROCESS_VARIABLE,
								Bpmn.HD_LNT_VARIABLE
						)
				))
				.append(Lnt.SEQUENTIAL_COMPOSITION_OPERATOR)
				.append(Constant.DOUBLE_LINE_FEED)

				// Twelfth line:
				// if (source == DummyId) then
				.append(Utils.indentLNT(5))
				.append(Lnt.generateIfStatement(
						Lnt.generateEqualsComparison(
								Bpmn.SOURCE_LNT_VARIABLE,
								Bpmn.DUMMY_ID
						)
				))
				.append(Constant.LINE_FEED)

				// Thirteenth line:
				// return True
				.append(Utils.indentLNT(6))
				.append(Lnt.generateReturnStatement(
						Lnt.TRUE
				))
				.append(Constant.LINE_FEED)

				// Fourteenth line:
				// elsif (member (source, visited)) then
				.append(Utils.indentLNT(5))
				.append(Lnt.generateElsifStatement(
						Lnt.generateFunctionCall(
								Lnt.PREDEFINED_FUNCTION_MEMBER,
								Bpmn.SOURCE_LNT_VARIABLE,
								Bpmn.VISITED_VARIABLE
						)
				))
				.append(Constant.LINE_FEED)

				// Fifteenth line:
				// result1 := check_af_upstream (visited?, activeflows, tl
				.append(Utils.indentLNT(6))
				.append(Bpmn.RESULT_1_VARIABLE)
				.append(Lnt.SPACED_VARIABLE_ASSIGNATION_OPERATOR)
				.append(Bpmn.CHECK_ALL_FLOWS_UPSTREAM_LNT_FUNCTION)
				.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
				.append(Lnt.markAsInputOutputParameter(Bpmn.VISITED_VARIABLE))
				.append(Constant.COMA_AND_SPACE)
				.append(Bpmn.ACTIVE_FLOWS_VARIABLE)
				.append(Constant.COMA_AND_SPACE)
				.append(Bpmn.TL_VARIABLE)
				.append(Constant.COMA)
				.append(Constant.LINE_FEED)

				// Sixteenth line:
				// p);
				.append(Utils.indent(48))
				.append(Bpmn.PROCESS_VARIABLE)
				.append(Constant.RIGHT_PARENTHESIS)
				.append(Lnt.SEQUENTIAL_COMPOSITION_OPERATOR)
				.append(Constant.LINE_FEED)

				// Seventeenth line:
				// return result1
				.append(Utils.indentLNT(6))
				.append(Lnt.generateReturnStatement(
						Bpmn.RESULT_1_VARIABLE
				))
				.append(Constant.LINE_FEED)

				// Eighteenth line:
				// else
				.append(Utils.indentLNT(5))
				.append(Lnt.ELSE)
				.append(Constant.LINE_FEED)

				// Nineteenth line:
				// visited := insert (source, visited);
				.append(Utils.indentLNT(6))
				.append(Lnt.generateVariableAssignation(
						Bpmn.VISITED_VARIABLE,
						Lnt.generateFunctionCall(
								Lnt.PREDEFINED_FUNCTION_INSERT,
								Bpmn.SOURCE_LNT_VARIABLE,
								Bpmn.VISITED_VARIABLE
						)
				))
				.append(Lnt.SEQUENTIAL_COMPOSITION_OPERATOR)
				.append(Constant.LINE_FEED)

				// Twentieth line:
				// upflow := get_incf_by_id(p, source);
				.append(Utils.indentLNT(6))
				.append(Lnt.generateVariableAssignation(
						Bpmn.UPFLOW_LNT_VARIABLE,
						Lnt.generateFunctionCall(
								Bpmn.GET_INCOMING_FLOWS_BY_ID_LNT_FUNCTION,
								Bpmn.PROCESS_VARIABLE,
								Bpmn.SOURCE_LNT_VARIABLE
						)
				))
				.append(Lnt.SEQUENTIAL_COMPOSITION_OPERATOR)
				.append(Constant.DOUBLE_LINE_FEED)

				// Twenty-first line:
				// if (upflow == nil) then
				.append(Utils.indentLNT(6))
				.append(Lnt.generateIfStatement(
						Lnt.generateEqualsComparison(
								Bpmn.UPFLOW_LNT_VARIABLE,
								Lnt.EMPTY_LIST
						)
				))
				.append(Constant.LINE_FEED)

				// Twenty-second line:
				// return True
				.append(Utils.indentLNT(7))
				.append(Lnt.generateReturnStatement(
						Lnt.TRUE
				))
				.append(Constant.LINE_FEED)

				// Twenty-third line:
				// end if;
				.append(Utils.indentLNT(6))
				.append(Lnt.END_IF)
				.append(Lnt.SEQUENTIAL_COMPOSITION_OPERATOR)
				.append(Constant.DOUBLE_LINE_FEED)

				// Twenty-fourth line:
				// count := find_active_tokens (activeflows, upflow);
				.append(Utils.indentLNT(6))
				.append(Lnt.generateVariableAssignation(
						Bpmn.COUNT_LNT_VARIABLE,
						Lnt.generateFunctionCall(
								Bpmn.FIND_ACTIVE_TOKENS_LNT_FUNCTION,
								Bpmn.ACTIVE_FLOWS_VARIABLE,
								Bpmn.UPFLOW_LNT_VARIABLE
						)
				))
				.append(Lnt.SEQUENTIAL_COMPOSITION_OPERATOR)
				.append(Constant.DOUBLE_LINE_FEED)

				// Twenty-fifth line:
				// if (count == 0 of Nat) then
				.append(Utils.indentLNT(6))
				.append(Lnt.generateIfStatement(
						Lnt.generateEqualsComparison(
								Bpmn.COUNT_LNT_VARIABLE,
								0 + Lnt.SPACED_OF + Lnt.NATURAL_NUMBER_TYPE
						)
				))
				.append(Constant.LINE_FEED)

				// Twenty-sixth line:
				// result1 := check_af_upstream (visited?, activeflows,
				.append(Utils.indentLNT(7))
				.append(Bpmn.RESULT_1_VARIABLE)
				.append(Lnt.SPACED_VARIABLE_ASSIGNATION_OPERATOR)
				.append(Bpmn.CHECK_ALL_FLOWS_UPSTREAM_LNT_FUNCTION)
				.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
				.append(Lnt.markAsInputOutputParameter(Bpmn.VISITED_VARIABLE))
				.append(Constant.COMA_AND_SPACE)
				.append(Bpmn.ACTIVE_FLOWS_VARIABLE)
				.append(Constant.COMA)
				.append(Constant.LINE_FEED)

				// Twenty-seventh line:
				// upflow, p);
				.append(Utils.indent(51))
				.append(Bpmn.UPFLOW_LNT_VARIABLE)
				.append(Constant.COMA_AND_SPACE)
				.append(Bpmn.PROCESS_VARIABLE)
				.append(Constant.RIGHT_PARENTHESIS)
				.append(Lnt.SEQUENTIAL_COMPOSITION_OPERATOR)
				.append(Constant.LINE_FEED)

				// Twenty-eighth line:
				// result2 := check_af_upstream (visited?, activeflows,
				.append(Utils.indentLNT(7))
				.append(Bpmn.RESULT_2_VARIABLE)
				.append(Lnt.SPACED_VARIABLE_ASSIGNATION_OPERATOR)
				.append(Bpmn.CHECK_ALL_FLOWS_UPSTREAM_LNT_FUNCTION)
				.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
				.append(Lnt.markAsInputOutputParameter(Bpmn.VISITED_VARIABLE))
				.append(Constant.COMA_AND_SPACE)
				.append(Bpmn.ACTIVE_FLOWS_VARIABLE)
				.append(Constant.COMA)
				.append(Constant.LINE_FEED)

				// Twenty-ninth line:
				// tl, p);
				.append(Utils.indent(51))
				.append(Bpmn.TL_VARIABLE)
				.append(Constant.COMA_AND_SPACE)
				.append(Bpmn.PROCESS_VARIABLE)
				.append(Constant.RIGHT_PARENTHESIS)
				.append(Lnt.SEQUENTIAL_COMPOSITION_OPERATOR)
				.append(Constant.LINE_FEED)

				// Thirtieth line:
				// return result1 and result2
				.append(Utils.indentLNT(7))
				.append(Lnt.generateReturnStatement(
						Bpmn.RESULT_1_VARIABLE + Constant.SPACE + Lnt.LOGICAL_AND + Constant.SPACE + Bpmn.RESULT_2_VARIABLE
				))
				.append(Constant.LINE_FEED)

				// Thirty-first line:
				// else
				.append(Utils.indentLNT(6))
				.append(Lnt.ELSE)
				.append(Constant.LINE_FEED)

				// Thirty-second line:
				// return False
				.append(Utils.indentLNT(7))
				.append(Lnt.generateReturnStatement(
						Lnt.FALSE
				))
				.append(Constant.LINE_FEED)

				// Thirty-third line:
				// end if
				.append(Utils.indentLNT(6))
				.append(Lnt.END_IF)
				.append(Constant.LINE_FEED)

				// Thirty-fourth line:
				// end if
				.append(Utils.indentLNT(5))
				.append(Lnt.END_IF)
				.append(Constant.LINE_FEED)

				// Thirty-fifth line:
				// | nil -> return True
				.append(Utils.indentLNT(3))
				.append(Lnt.CASE_OPERATOR)
				.append(Constant.SPACE)
				.append(Lnt.EMPTY_LIST)
				.append(Lnt.SPACED_PATTERN_MATCHING_OPERATOR)
				.append(Lnt.generateReturnStatement(
						Lnt.TRUE
				))
				.append(Constant.LINE_FEED)

				// Thirty-sixth line:
				// end case
				.append(Utils.indentLNT(2))
				.append(Lnt.END_CASE)
				.append(Constant.LINE_FEED)

				// Thirty-seventh line:
				// end var
				.append(Utils.indentLNT(1))
				.append(Lnt.END_VAR)
				.append(Constant.LINE_FEED)

				// Thirty-eighth line
				// end function
				.append(Lnt.END_FUNCTION);

		this.writeLntSeparation(builder);
	}

	private void writeFindFlowSourceLntFunction(final StringBuilder builder)
	{
		builder // First line:
				// (*----- "find_flow_source()" LNT Function -----*)
				.append(Lnt.OPEN_MULTILINE_COMMENTARY)
				.append(Utils.getDashesStringOfSize(5))
				.append(Constant.SPACE)
				.append(Constant.DOUBLE_QUOTATION_MARK)
				.append("find_flow_source()")
				.append(Constant.DOUBLE_QUOTATION_MARK)
				.append(Constant.SPACE)
				.append("LNT Function")
				.append(Constant.SPACE)
				.append(Utils.getDashesStringOfSize(5))
				.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
				.append(Constant.DOUBLE_LINE_FEED)

				// Second line:
				// function find_flow_source (bpmn: BPROCESS, flowid: ID): ID is
				.append(Lnt.generateFunctionHeader(
						Bpmn.FIND_FLOW_SOURCE_LNT_FUNCTION,
						Bpmn.ID_LNT_TYPE,
						false,
						new Lnt.ParametersAndType(Bpmn.BPMN_PROCESS_VARIABLE, Bpmn.BPMN_PROCESS_LNT_TYPE),
						new Lnt.ParametersAndType(Bpmn.FLOW_ID_VARIABLE, Bpmn.ID_LNT_TYPE)
				))
				.append(Constant.LINE_FEED)

				// Third line:
				// case bpmn
				.append(Utils.indentLNT(1))
				.append(Lnt.CASE)
				.append(Constant.SPACE)
				.append(Bpmn.BPMN_PROCESS_VARIABLE)
				.append(Constant.LINE_FEED)

				// Fourth line:
				// var name: ID, nodes: NODES, flows: FLOWS in
				.append(Utils.indentLNT(2))
				.append(Lnt.generateVariableDeclarationStatement(
						new Lnt.VariablesAndType(Bpmn.NAME_VARIABLE, Bpmn.ID_LNT_TYPE),
						new Lnt.VariablesAndType(Bpmn.NODES_VARIABLE, Bpmn.SET_OF_NODES_LNT_TYPE),
						new Lnt.VariablesAndType(Bpmn.FLOWS_VARIABLE, Bpmn.SET_OF_FLOWS_LNT_TYPE)
				))
				.append(Constant.LINE_FEED)

				// Fifth line:
				// proc (name, nodes, flows) -> return traverse_flows (flows, flowid)
				.append(Utils.indentLNT(3))
				.append(Lnt.generateObjectWithArguments(
						Bpmn.PROCESS_IDENTIFIER,
						Bpmn.NAME_VARIABLE,
						Bpmn.NODES_VARIABLE,
						Bpmn.FLOWS_VARIABLE
				))
				.append(Lnt.SPACED_PATTERN_MATCHING_OPERATOR)
				.append(Lnt.generateReturnStatement(
						Lnt.generateFunctionCall(
								Bpmn.TRAVERSE_FLOWS_LNT_FUNCTION,
								Bpmn.FLOWS_VARIABLE,
								Bpmn.FLOW_ID_VARIABLE
						)
				))
				.append(Constant.LINE_FEED)

				// Sixth line:
				// end case
				.append(Utils.indentLNT(1))
				.append(Lnt.END_CASE)
				.append(Constant.LINE_FEED)

				// Seventh line:
				// end function
				.append(Lnt.END_FUNCTION);

		this.writeLntSeparation(builder);
	}

	private void writeTraverseFlowsLntFunction(final StringBuilder builder)
	{
		builder // First line:
				// (*----- "traverse_flows()" LNT Function -----*)
				.append(Lnt.OPEN_MULTILINE_COMMENTARY)
				.append(Utils.getDashesStringOfSize(5))
				.append(Constant.SPACE)
				.append(Constant.DOUBLE_QUOTATION_MARK)
				.append("traverse_flows()")
				.append(Constant.DOUBLE_QUOTATION_MARK)
				.append(Constant.SPACE)
				.append("LNT Function")
				.append(Constant.SPACE)
				.append(Utils.getDashesStringOfSize(5))
				.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
				.append(Constant.DOUBLE_LINE_FEED)

				// Second line:
				// function traverse_flows (flows: FLOWS, flowid: ID): ID is
				.append(Lnt.generateFunctionHeader(
						Bpmn.TRAVERSE_FLOWS_LNT_FUNCTION,
						Bpmn.ID_LNT_TYPE,
						false,
						new Lnt.ParametersAndType(Bpmn.FLOWS_VARIABLE, Bpmn.SET_OF_FLOWS_LNT_TYPE),
						new Lnt.ParametersAndType(Bpmn.FLOW_ID_VARIABLE, Bpmn.ID_LNT_TYPE)
				))
				.append(Constant.LINE_FEED)

				// Third line:
				// var dummySource: ID in
				.append(Utils.indentLNT(1))
				.append(Lnt.generateVariableDeclarationStatement(
						new Lnt.VariablesAndType(Bpmn.DUMMY_SOURCE_LNT_VARIABLE, Bpmn.ID_LNT_TYPE)
				))
				.append(Constant.LINE_FEED)

				// Fourth line:
				// dummySource := DummyId;
				.append(Utils.indentLNT(2))
				.append(Lnt.generateVariableAssignation(
						Bpmn.DUMMY_SOURCE_LNT_VARIABLE,
						Bpmn.DUMMY_ID
				))
				.append(Lnt.SEQUENTIAL_COMPOSITION_OPERATOR)
				.append(Constant.DOUBLE_LINE_FEED)

				// Fifth line:
				// case flows
				.append(Utils.indentLNT(2))
				.append(Lnt.CASE)
				.append(Constant.SPACE)
				.append(Bpmn.FLOWS_VARIABLE)
				.append(Constant.LINE_FEED)

				// Sixth line:
				// var ident, source, target: ID, tl: FLOWS in
				.append(Utils.indentLNT(3))
				.append(Lnt.generateVariableDeclarationStatement(
						new Lnt.VariablesAndType(
								Arrays.asList(Bpmn.IDENT_VARIABLE, Bpmn.SOURCE_LNT_VARIABLE, Bpmn.TARGET_VARIABLE),
								Bpmn.ID_LNT_TYPE
						),
						new Lnt.VariablesAndType(Bpmn.TL_VARIABLE, Bpmn.SET_OF_FLOWS_LNT_TYPE)
				))
				.append(Constant.LINE_FEED)

				// Seventh line:
				// cons (flow (ident, source, target), tl) ->
				.append(Utils.indentLNT(4))
				.append(Lnt.generateFunctionCall(
						Lnt.PREDEFINED_CONSTRUCTOR_FUNCTION,
						Lnt.generateObjectWithArguments(
								Bpmn.FLOW,
								Bpmn.IDENT_VARIABLE,
								Bpmn.SOURCE_LNT_VARIABLE,
								Bpmn.TARGET_VARIABLE
						),
						Bpmn.TL_VARIABLE
				))
				.append(Constant.SPACE)
				.append(Lnt.PATTERN_MATCHING_OPERATOR)
				.append(Constant.LINE_FEED)

				// Eighth line:
				// if (ident == flowid) then
				.append(Utils.indentLNT(5))
				.append(Lnt.generateIfStatement(
						Lnt.generateEqualsComparison(
								Bpmn.IDENT_VARIABLE,
								Bpmn.FLOW_ID_VARIABLE
						)
				))
				.append(Constant.LINE_FEED)

				// Ninth line:
				// return source
				.append(Utils.indentLNT(6))
				.append(Lnt.generateReturnStatement(
						Bpmn.SOURCE_LNT_VARIABLE
				))
				.append(Constant.LINE_FEED)

				// Tenth line:
				// else
				.append(Utils.indentLNT(5))
				.append(Lnt.ELSE)
				.append(Constant.LINE_FEED)

				// Eleventh line:
				// return traverse_flows (tl, flowid)
				.append(Utils.indentLNT(6))
				.append(Lnt.generateReturnStatement(
						Lnt.generateFunctionCall(
								Bpmn.TRAVERSE_FLOWS_LNT_FUNCTION,
								Bpmn.TL_VARIABLE,
								Bpmn.FLOW_ID_VARIABLE
						)
				))
				.append(Constant.LINE_FEED)

				// Twelfth line:
				// end if
				.append(Utils.indentLNT(5))
				.append(Lnt.END_IF)
				.append(Constant.LINE_FEED)

				// Thirteenth line:
				// | nil -> return dummySource
				.append(Utils.indentLNT(3))
				.append(Lnt.CASE_OPERATOR)
				.append(Constant.SPACE)
				.append(Lnt.EMPTY_LIST)
				.append(Lnt.SPACED_PATTERN_MATCHING_OPERATOR)
				.append(Lnt.generateReturnStatement(
						Bpmn.DUMMY_SOURCE_LNT_VARIABLE
				))
				.append(Constant.LINE_FEED)

				// Fourteenth line:
				// end case
				.append(Utils.indentLNT(2))
				.append(Lnt.END_CASE)
				.append(Constant.LINE_FEED)

				// Fifteenth line:
				// end var
				.append(Utils.indentLNT(1))
				.append(Lnt.END_VAR)
				.append(Constant.LINE_FEED)

				// Sixteenth line:
				// end function
				.append(Lnt.END_FUNCTION);

		this.writeLntSeparation(builder);
	}

	private void writeGetIncfByIdLntFunction(final StringBuilder builder)
	{
		builder // First line:
				// (*----- "get_incf_by_id()" LNT Function -----*)
				.append(Lnt.OPEN_MULTILINE_COMMENTARY)
				.append(Utils.getDashesStringOfSize(5))
				.append(Constant.SPACE)
				.append(Constant.DOUBLE_QUOTATION_MARK)
				.append("get_incf_by_id()")
				.append(Constant.DOUBLE_QUOTATION_MARK)
				.append(Constant.SPACE)
				.append("LNT Function")
				.append(Constant.SPACE)
				.append(Utils.getDashesStringOfSize(5))
				.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
				.append(Constant.DOUBLE_LINE_FEED)

				// Second line:
				// (*----- Given a node id, gets its incoming flows -----*)
				.append(Lnt.OPEN_MULTILINE_COMMENTARY)
				.append(Utils.getDashesStringOfSize(5))
				.append(Constant.SPACE)
				.append("Given a node id, gets its incoming flows")
				.append(Constant.SPACE)
				.append(Utils.getDashesStringOfSize(5))
				.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
				.append(Constant.DOUBLE_LINE_FEED)

				// Third line:
				// function get_incf_by_id (p: BPROCESS, nodeid: ID): IDS is
				.append(Lnt.generateFunctionHeader(
						Bpmn.GET_INCOMING_FLOWS_BY_ID_LNT_FUNCTION,
						Bpmn.SET_OF_IDS_LNT_TYPE,
						false,
						new Lnt.ParametersAndType(Bpmn.PROCESS_VARIABLE, Bpmn.BPMN_PROCESS_LNT_TYPE),
						new Lnt.ParametersAndType(Bpmn.NODE_ID_VARIABLE, Bpmn.ID_LNT_TYPE)
				))
				.append(Constant.LINE_FEED)

				// Fourth line:
				// case p
				.append(Utils.indentLNT(1))
				.append(Lnt.CASE)
				.append(Constant.SPACE)
				.append(Bpmn.PROCESS_VARIABLE)
				.append(Constant.LINE_FEED)

				// Fifth line:
				// var name: ID, nodes: NODES, flows: FLOWS in
				.append(Utils.indentLNT(2))
				.append(Lnt.generateVariableDeclarationStatement(
						new Lnt.VariablesAndType(Bpmn.NAME_VARIABLE, Bpmn.ID_LNT_TYPE),
						new Lnt.VariablesAndType(Bpmn.NODES_VARIABLE, Bpmn.SET_OF_NODES_LNT_TYPE),
						new Lnt.VariablesAndType(Bpmn.FLOWS_VARIABLE, Bpmn.SET_OF_FLOWS_LNT_TYPE)
				))
				.append(Constant.LINE_FEED)

				// Sixth line:
				// proc (name, nodes, flows) -> return traverse_nodes (nodes, nodeid)
				.append(Utils.indentLNT(3))
				.append(Lnt.generateObjectWithArguments(
						Bpmn.PROCESS_IDENTIFIER,
						Bpmn.NAME_VARIABLE,
						Bpmn.NODES_VARIABLE,
						Bpmn.FLOWS_VARIABLE
				))
				.append(Lnt.SPACED_PATTERN_MATCHING_OPERATOR)
				.append(Lnt.generateReturnStatement(
						Lnt.generateFunctionCall(
								Bpmn.TRAVERSE_NODES_LNT_FUNCTION,
								Bpmn.NODES_VARIABLE,
								Bpmn.NODE_ID_VARIABLE
						)
				))
				.append(Constant.LINE_FEED)

				// Seventh line:
				// end case
				.append(Utils.indentLNT(1))
				.append(Lnt.END_CASE)
				.append(Constant.LINE_FEED)

				// Eighth line:
				// end function
				.append(Lnt.END_FUNCTION);

		this.writeLntSeparation(builder);
	}

	private void writeTraverseNodesLntFunction(final StringBuilder builder)
	{
		builder // First line:
				// (*----- "traverse_nodes()" LNT Function -----*)
				.append(Lnt.OPEN_MULTILINE_COMMENTARY)
				.append(Utils.getDashesStringOfSize(5))
				.append(Constant.SPACE)
				.append(Constant.DOUBLE_QUOTATION_MARK)
				.append("traverse_nodes()")
				.append(Constant.DOUBLE_QUOTATION_MARK)
				.append(Constant.SPACE)
				.append("LNT Function")
				.append(Constant.SPACE)
				.append(Utils.getDashesStringOfSize(5))
				.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
				.append(Constant.DOUBLE_LINE_FEED)

				// Second line:
				// (*----- Traverse across all nodes in search of the node -----*)
				.append(Lnt.OPEN_MULTILINE_COMMENTARY)
				.append(Utils.getDashesStringOfSize(5))
				.append(Constant.SPACE)
				.append("Traverse across all nodes in search of the node")
				.append(Constant.SPACE)
				.append(Utils.getDashesStringOfSize(5))
				.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
				.append(Constant.DOUBLE_LINE_FEED)

				// Third line:
				// function traverse_nodes (nodes: NODES, id: ID): IDS is
				.append(Lnt.generateFunctionHeader(
						Bpmn.TRAVERSE_NODES_LNT_FUNCTION,
						Bpmn.SET_OF_IDS_LNT_TYPE,
						false,
						new Lnt.ParametersAndType(Bpmn.NODES_VARIABLE, Bpmn.SET_OF_NODES_LNT_TYPE),
						new Lnt.ParametersAndType(Bpmn.ID_VARIABLE, Bpmn.ID_LNT_TYPE)
				))
				.append(Constant.LINE_FEED)

				// Fourth line:
				// case nodes
				.append(Utils.indentLNT(1))
				.append(Lnt.CASE)
				.append(Constant.LINE_FEED)
				.append(Bpmn.NODES_VARIABLE)
				.append(Constant.LINE_FEED)

				// Fifth line:
				// var
				.append(Utils.indentLNT(2))
				.append(Lnt.VAR)
				.append(Constant.LINE_FEED)

				// Sixth line:
				// gateways: GATEWAYS, initial: INITIAL, finals: FINALS, tasks: TASKS,
				.append(Utils.indentLNT(3))
				.append(Lnt.generateVariablesDefinition(
						Bpmn.GATEWAYS_VARIABLE,
						Bpmn.SET_OF_GATEWAYS_LNT_TYPE
				))
				.append(Constant.COMA_AND_SPACE)
				.append(Lnt.generateVariablesDefinition(
						Bpmn.INITIAL_VARIABLE,
						Bpmn.INITIAL_EVENT_LNT_TYPE
				))
				.append(Constant.COMA_AND_SPACE)
				.append(Lnt.generateVariablesDefinition(
						Bpmn.FINALS_VARIABLE,
						Bpmn.SET_OF_END_EVENTS_LNT_TYPE
				))
				.append(Constant.COMA_AND_SPACE)
				.append(Lnt.generateVariablesDefinition(
						Bpmn.TASKS_VARIABLE,
						Bpmn.SET_OF_TASKS_LNT_TYPE
				))
				.append(Constant.COMA)
				.append(Constant.LINE_FEED)

				// Seventh line:
				// tl: NODES, incf:IDS
				.append(Utils.indentLNT(3))
				.append(Lnt.generateVariablesDefinition(
						Bpmn.TL_VARIABLE,
						Bpmn.SET_OF_NODES_LNT_TYPE
				))
				.append(Constant.COMA_AND_SPACE)
				.append(Lnt.generateVariablesDefinition(
						Bpmn.INCOMING_FLOW_VARIABLE,
						Bpmn.SET_OF_IDS_LNT_TYPE
				))
				.append(Constant.LINE_FEED)

				// Eighth line:
				// in
				.append(Utils.indentLNT(2))
				.append(Lnt.IN)
				.append(Constant.LINE_FEED)

				// Ninth line:
				// cons (g (gateways), tl) ->
				.append(Utils.indentLNT(2))
				.append(Utils.indent(2))
				.append(Lnt.generateFunctionCall(
						Lnt.PREDEFINED_CONSTRUCTOR_FUNCTION,
						Lnt.generateObjectWithArguments(
								Bpmn.GATEWAYS_IDENTIFIER,
								Bpmn.GATEWAYS_VARIABLE
						),
						Bpmn.TL_VARIABLE
				))
				.append(Constant.SPACE)
				.append(Lnt.PATTERN_MATCHING_OPERATOR)
				.append(Constant.LINE_FEED)

				// Tenth line:
				// incf := traverse_gateways (gateways, id);
				.append(Utils.indentLNT(3))
				.append(Utils.indent(2))
				.append(Lnt.generateVariableAssignation(
						Bpmn.INCOMING_FLOW_VARIABLE,
						Lnt.generateFunctionCall(
								Bpmn.TRAVERSE_GATEWAYS_LNT_FUNCTION,
								Bpmn.GATEWAYS_VARIABLE,
								Bpmn.ID_VARIABLE
						)
				))
				.append(Lnt.SEQUENTIAL_COMPOSITION_OPERATOR)
				.append(Constant.DOUBLE_LINE_FEED)

				// Eleventh line:
				// if (incf == nil) then
				.append(Utils.indentLNT(3))
				.append(Utils.indent(2))
				.append(Lnt.generateIfStatement(
						Lnt.generateEqualsComparison(
								Bpmn.INCOMING_FLOW_VARIABLE,
								Lnt.EMPTY_LIST
						)
				))
				.append(Constant.LINE_FEED)

				// Twelfth line:
				// return traverse_nodes(tl, id)
				.append(Utils.indentLNT(4))
				.append(Utils.indent(2))
				.append(Lnt.generateReturnStatement(
						Lnt.generateFunctionCall(
								Bpmn.TRAVERSE_NODES_LNT_FUNCTION,
								Bpmn.TL_VARIABLE,
								Bpmn.ID_VARIABLE
						)
				))
				.append(Constant.LINE_FEED)

				// Thirteenth line:
				// else
				.append(Utils.indentLNT(3))
				.append(Utils.indent(2))
				.append(Lnt.ELSE)
				.append(Constant.LINE_FEED)

				// Fourteenth line:
				// return incf
				.append(Utils.indentLNT(4))
				.append(Utils.indent(2))
				.append(Lnt.generateReturnStatement(
						Bpmn.INCOMING_FLOW_VARIABLE
				))
				.append(Constant.LINE_FEED)

				// Fifteenth line:
				// end if
				.append(Utils.indentLNT(3))
				.append(Utils.indent(2))
				.append(Lnt.END_IF)
				.append(Constant.LINE_FEED)

				// Sixteenth line:
				// | cons (i (initial), tl) -> return traverse_nodes (tl, id)
				.append(Utils.indentLNT(2))
				.append(Lnt.CASE_OPERATOR)
				.append(Constant.SPACE)
				.append(Lnt.generateFunctionCall(
						Lnt.PREDEFINED_CONSTRUCTOR_FUNCTION,
						Lnt.generateObjectWithArguments(
								Bpmn.INITIAL_NODES_IDENTIFIER,
								Bpmn.INITIAL_VARIABLE
						),
						Bpmn.TL_VARIABLE
				))
				.append(Lnt.SPACED_PATTERN_MATCHING_OPERATOR)
				.append(Lnt.generateReturnStatement(
						Lnt.generateFunctionCall(
								Bpmn.TRAVERSE_NODES_LNT_FUNCTION,
								Bpmn.TL_VARIABLE,
								Bpmn.ID_VARIABLE
						)
				))
				.append(Constant.LINE_FEED)

				// Seventeenth line:
				// | cons (f (finals), tl) ->
				.append(Utils.indentLNT(2))
				.append(Lnt.CASE_OPERATOR)
				.append(Constant.SPACE)
				.append(Lnt.generateFunctionCall(
						Lnt.PREDEFINED_CONSTRUCTOR_FUNCTION,
						Lnt.generateObjectWithArguments(
								Bpmn.END_EVENTS_IDENTIFIER,
								Bpmn.FINALS_VARIABLE
						),
						Bpmn.TL_VARIABLE
				))
				.append(Constant.SPACE)
				.append(Lnt.PATTERN_MATCHING_OPERATOR)
				.append(Constant.LINE_FEED)

				// Eighteenth line:
				// incf := traverse_finals (finals, id);
				.append(Utils.indentLNT(3))
				.append(Utils.indent(2))
				.append(Lnt.generateVariableAssignation(
						Bpmn.INCOMING_FLOW_VARIABLE,
						Lnt.generateFunctionCall(
								Bpmn.TRAVERSE_FINALS_LNT_FUNCTION,
								Bpmn.FINALS_VARIABLE,
								Bpmn.ID_VARIABLE
						)
				))
				.append(Lnt.SEQUENTIAL_COMPOSITION_OPERATOR)
				.append(Constant.DOUBLE_LINE_FEED)

				// Nineteenth line:
				// if (incf == nil) then
				.append(Utils.indentLNT(3))
				.append(Utils.indent(2))
				.append(Lnt.generateIfStatement(
						Lnt.generateEqualsComparison(
								Bpmn.INCOMING_FLOW_VARIABLE,
								Lnt.EMPTY_LIST
						)
				))
				.append(Constant.LINE_FEED)

				// Twentieth line:
				// return traverse_nodes (tl, id)
				.append(Utils.indentLNT(4))
				.append(Utils.indent(2))
				.append(Lnt.generateReturnStatement(
						Lnt.generateFunctionCall(
								Bpmn.TRAVERSE_NODES_LNT_FUNCTION,
								Bpmn.TL_VARIABLE,
								Bpmn.ID_VARIABLE
						)
				))
				.append(Constant.LINE_FEED)

				// Twenty-first line:
				// else
				.append(Utils.indentLNT(3))
				.append(Utils.indent(2))
				.append(Lnt.ELSE)
				.append(Constant.LINE_FEED)

				// Twenty-second line:
				// return incf
				.append(Utils.indentLNT(4))
				.append(Utils.indent(2))
				.append(Lnt.generateReturnStatement(
						Bpmn.INCOMING_FLOW_VARIABLE
				))
				.append(Constant.LINE_FEED)

				// Twenty-third line:
				// end if
				.append(Utils.indentLNT(3))
				.append(Utils.indent(2))
				.append(Lnt.END_IF)
				.append(Constant.LINE_FEED)

				// Twenty-fourth line:
				// | cons (t (tasks), tl) ->
				.append(Utils.indentLNT(2))
				.append(Lnt.CASE_OPERATOR)
				.append(Constant.SPACE)
				.append(Lnt.generateFunctionCall(
						Lnt.PREDEFINED_CONSTRUCTOR_FUNCTION,
						Lnt.generateObjectWithArguments(
								Bpmn.TASKS_IDENTIFIER,
								Bpmn.TASKS_VARIABLE
						),
						Bpmn.TL_VARIABLE
				))
				.append(Constant.SPACE)
				.append(Lnt.PATTERN_MATCHING_OPERATOR)
				.append(Constant.LINE_FEED)

				// Twenty-fifth line:
				// incf := traverse_tasks (tasks, id);
				.append(Utils.indentLNT(3))
				.append(Utils.indent(2))
				.append(Lnt.generateVariableAssignation(
						Bpmn.INCOMING_FLOW_VARIABLE,
						Lnt.generateFunctionCall(
								Bpmn.TRAVERSE_TASKS_LNT_FUNCTION,
								Bpmn.TASKS_VARIABLE,
								Bpmn.ID_VARIABLE
						)
				))
				.append(Lnt.SEQUENTIAL_COMPOSITION_OPERATOR)
				.append(Constant.DOUBLE_LINE_FEED)

				// Twenty-sixth line:
				// if (incf == nil) then
				.append(Utils.indentLNT(3))
				.append(Utils.indent(2))
				.append(Lnt.generateIfStatement(
						Lnt.generateEqualsComparison(
								Bpmn.INCOMING_FLOW_VARIABLE,
								Lnt.EMPTY_LIST
						)
				))
				.append(Constant.LINE_FEED)

				// Twenty-seventh line:
				// return traverse_nodes (tl, id)
				.append(Utils.indentLNT(4))
				.append(Utils.indent(2))
				.append(Lnt.generateReturnStatement(
						Lnt.generateFunctionCall(
								Bpmn.TRAVERSE_NODES_LNT_FUNCTION,
								Bpmn.TL_VARIABLE,
								Bpmn.ID_VARIABLE
						)
				))
				.append(Constant.LINE_FEED)

				// Twenty-eighth line:
				// else
				.append(Utils.indentLNT(3))
				.append(Utils.indent(2))
				.append(Lnt.ELSE)
				.append(Constant.LINE_FEED)

				// Twenty-ninth line:
				// return incf
				.append(Utils.indentLNT(4))
				.append(Utils.indent(2))
				.append(Lnt.generateReturnStatement(
						Bpmn.INCOMING_FLOW_VARIABLE
				))
				.append(Constant.LINE_FEED)

				// Thirtieth line:
				// end if
				.append(Utils.indentLNT(3))
				.append(Utils.indent(2))
				.append(Lnt.END_IF)
				.append(Constant.LINE_FEED)

				// Thirty-first line:
				// | nil -> return nil
				.append(Utils.indentLNT(2))
				.append(Lnt.CASE_OPERATOR)
				.append(Constant.SPACE)
				.append(Lnt.EMPTY_LIST)
				.append(Lnt.SPACED_PATTERN_MATCHING_OPERATOR)
				.append(Lnt.generateReturnStatement(
						Lnt.EMPTY_LIST
				))
				.append(Constant.LINE_FEED)

				// Thirty-second line:
				// end case
				.append(Utils.indentLNT(1))
				.append(Lnt.END_CASE)
				.append(Constant.LINE_FEED)

				// Thirty-third line
				// end function
				.append(Lnt.END_FUNCTION);

		this.writeLntSeparation(builder);
	}

	private void writeTraverseGatewaysLntFunction(final StringBuilder builder)
	{
		builder // First line:
				// (*----- "traverse_gateways()" LNT Function -----*)
				.append(Lnt.OPEN_MULTILINE_COMMENTARY)
				.append(Utils.getDashesStringOfSize(5))
				.append(Constant.SPACE)
				.append(Constant.DOUBLE_QUOTATION_MARK)
				.append("traverse_gateways()")
				.append(Constant.DOUBLE_QUOTATION_MARK)
				.append(Constant.SPACE)
				.append("LNT Function")
				.append(Constant.SPACE)
				.append(Utils.getDashesStringOfSize(5))
				.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
				.append(Constant.DOUBLE_LINE_FEED)

				// Second line:
				// (*----- Find incoming flows of gateways -----*)
				.append(Lnt.OPEN_MULTILINE_COMMENTARY)
				.append(Utils.getDashesStringOfSize(5))
				.append(Constant.SPACE)
				.append("Find incoming flows of gateways")
				.append(Constant.SPACE)
				.append(Utils.getDashesStringOfSize(5))
				.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
				.append(Constant.DOUBLE_LINE_FEED)

				// Third line:
				// function traverse_gateways (gateways: GATEWAYS, id: ID): IDS is
				.append(Lnt.generateFunctionHeader(
						Bpmn.TRAVERSE_GATEWAYS_LNT_FUNCTION,
						Bpmn.SET_OF_IDS_LNT_TYPE,
						false,
						new Lnt.ParametersAndType(Bpmn.GATEWAYS_VARIABLE, Bpmn.SET_OF_GATEWAYS_LNT_TYPE),
						new Lnt.ParametersAndType(Bpmn.ID_VARIABLE, Bpmn.ID_LNT_TYPE)
				))
				.append(Constant.LINE_FEED)

				// Fourth line:
				// case gateways
				.append(Utils.indentLNT(1))
				.append(Lnt.CASE)
				.append(Constant.SPACE)
				.append(Bpmn.GATEWAYS_VARIABLE)
				.append(Constant.LINE_FEED)

				// Fifth line:
				// var
				.append(Utils.indentLNT(2))
				.append(Lnt.VAR)
				.append(Constant.LINE_FEED)

				// Sixth line:
				// ident: ID, pattern: GPATTERN, sort: GSORT, incf, outf: IDS,
				.append(Utils.indentLNT(3))
				.append(Lnt.generateVariablesDefinition(
						Bpmn.IDENT_VARIABLE,
						Bpmn.ID_LNT_TYPE
				))
				.append(Constant.COMA_AND_SPACE)
				.append(Lnt.generateVariablesDefinition(
						Bpmn.PATTERN_VARIABLE,
						Bpmn.GATEWAY_PATTERN_LNT_TYPE
				))
				.append(Constant.COMA_AND_SPACE)
				.append(Lnt.generateVariablesDefinition(
						Bpmn.SORT_VARIABLE,
						Bpmn.GATEWAY_TYPE_LNT_TYPE
				))
				.append(Constant.COMA_AND_SPACE)
				.append(Lnt.generateVariablesDefinition(
						Arrays.asList(Bpmn.INCOMING_FLOW_VARIABLE, Bpmn.OUTGOING_FLOW_VARIABLE),
						Bpmn.SET_OF_IDS_LNT_TYPE
				))
				.append(Constant.COMA)
				.append(Constant.LINE_FEED)

				// Seventh line:
				// tl: GATEWAYS
				.append(Utils.indentLNT(3))
				.append(Lnt.generateVariablesDefinition(
						Bpmn.TL_VARIABLE,
						Bpmn.SET_OF_GATEWAYS_LNT_TYPE
				))
				.append(Constant.LINE_FEED)

				// Eighth line:
				// in
				.append(Utils.indentLNT(2))
				.append(Lnt.IN)
				.append(Constant.LINE_FEED)

				// Ninth line:
				// cons (gateway (ident, pattern, sort, incf, outf), tl) ->
				.append(Utils.indentLNT(2))
				.append(Utils.indent(2))
				.append(Lnt.generateFunctionCall(
						Lnt.PREDEFINED_CONSTRUCTOR_FUNCTION,
						Lnt.generateObjectWithArguments(
								Bpmn.GATEWAY,
								Bpmn.IDENT_VARIABLE,
								Bpmn.PATTERN_VARIABLE,
								Bpmn.SORT_VARIABLE,
								Bpmn.INCOMING_FLOW_VARIABLE,
								Bpmn.OUTGOING_FLOW_VARIABLE
						),
						Bpmn.TL_VARIABLE
				))
				.append(Constant.SPACE)
				.append(Lnt.PATTERN_MATCHING_OPERATOR)
				.append(Constant.LINE_FEED)

				// Tenth line:
				// if (ident == id) then
				.append(Utils.indentLNT(3))
				.append(Utils.indent(2))
				.append(Lnt.generateIfStatement(
						Lnt.generateEqualsComparison(
								Bpmn.IDENT_VARIABLE,
								Bpmn.ID_VARIABLE
						)
				))
				.append(Constant.LINE_FEED)

				// Eleventh line:
				// return incf
				.append(Utils.indentLNT(4))
				.append(Utils.indent(2))
				.append(Lnt.generateReturnStatement(
						Bpmn.INCOMING_FLOW_VARIABLE
				))
				.append(Constant.LINE_FEED)

				// Twelfth line:
				// else
				.append(Utils.indentLNT(3))
				.append(Utils.indent(2))
				.append(Lnt.ELSE)
				.append(Constant.LINE_FEED)

				// Thirteenth line:
				// return traverse_gateways (tl, id)
				.append(Utils.indentLNT(4))
				.append(Utils.indent(2))
				.append(Lnt.generateReturnStatement(
						Lnt.generateFunctionCall(
								Bpmn.TRAVERSE_GATEWAYS_LNT_FUNCTION,
								Bpmn.TL_VARIABLE,
								Bpmn.ID_VARIABLE
						)
				))
				.append(Constant.LINE_FEED)

				// Fourteenth line:
				// end if
				.append(Utils.indentLNT(3))
				.append(Utils.indent(2))
				.append(Lnt.END_IF)
				.append(Constant.LINE_FEED)

				// Fifteenth line:
				// | nil -> return nil
				.append(Utils.indentLNT(2))
				.append(Lnt.CASE_OPERATOR)
				.append(Constant.SPACE)
				.append(Lnt.EMPTY_LIST)
				.append(Lnt.SPACED_PATTERN_MATCHING_OPERATOR)
				.append(Lnt.generateReturnStatement(
						Lnt.EMPTY_LIST
				))
				.append(Constant.LINE_FEED)

				// Sixteenth line:
				// end case
				.append(Utils.indentLNT(1))
				.append(Lnt.END_CASE)
				.append(Constant.LINE_FEED)

				// Seventeenth line:
				// end function
				.append(Lnt.END_FUNCTION);

		this.writeLntSeparation(builder);
	}

	private void writeTraverseFinalsLntFunction(final StringBuilder builder)
	{
		builder // First line:
				// (*----- "traverse_finals()" LNT Function -----*)
				.append(Lnt.OPEN_MULTILINE_COMMENTARY)
				.append(Utils.getDashesStringOfSize(5))
				.append(Constant.SPACE)
				.append(Constant.DOUBLE_QUOTATION_MARK)
				.append("traverse_finals()")
				.append(Constant.DOUBLE_QUOTATION_MARK)
				.append(Constant.SPACE)
				.append("LNT Function")
				.append(Constant.SPACE)
				.append(Utils.getDashesStringOfSize(5))
				.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
				.append(Constant.DOUBLE_LINE_FEED)

				// Second line:
				// (*----- Find incoming flows of end events -----*)
				.append(Lnt.OPEN_MULTILINE_COMMENTARY)
				.append(Utils.getDashesStringOfSize(5))
				.append(Constant.SPACE)
				.append("Find incoming flows of end events")
				.append(Constant.SPACE)
				.append(Utils.getDashesStringOfSize(5))
				.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
				.append(Constant.DOUBLE_LINE_FEED)

				// Third line:
				// function traverse_finals (finals: FINALS, id: ID): IDS is
				.append(Lnt.generateFunctionHeader(
						Bpmn.TRAVERSE_FINALS_LNT_FUNCTION,
						Bpmn.SET_OF_IDS_LNT_TYPE,
						false,
						new Lnt.ParametersAndType(Bpmn.FINALS_VARIABLE, Bpmn.SET_OF_END_EVENTS_LNT_TYPE),
						new Lnt.ParametersAndType(Bpmn.ID_VARIABLE, Bpmn.ID_LNT_TYPE)
				))
				.append(Constant.LINE_FEED)

				// Fourth line:
				// case finals
				.append(Utils.indentLNT(1))
				.append(Lnt.CASE)
				.append(Constant.SPACE)
				.append(Bpmn.FINALS_VARIABLE)
				.append(Constant.LINE_FEED)

				// Fifth line:
				// var ident: ID, incf: IDS, tl: FINALS in
				.append(Utils.indentLNT(2))
				.append(Lnt.generateVariableDeclarationStatement(
						new Lnt.VariablesAndType(Bpmn.IDENT_VARIABLE, Bpmn.ID_LNT_TYPE),
						new Lnt.VariablesAndType(Bpmn.INCOMING_FLOW_VARIABLE, Bpmn.SET_OF_IDS_LNT_TYPE),
						new Lnt.VariablesAndType(Bpmn.TL_VARIABLE, Bpmn.SET_OF_END_EVENTS_LNT_TYPE)
				))
				.append(Constant.LINE_FEED)

				// Sixth line:
				// cons (final (ident, incf), tl) ->
				.append(Utils.indentLNT(2))
				.append(Utils.indent(2))
				.append(Lnt.generateFunctionCall(
						Lnt.PREDEFINED_CONSTRUCTOR_FUNCTION,
						Lnt.generateObjectWithArguments(
								Bpmn.FINAL,
								Bpmn.IDENT_VARIABLE,
								Bpmn.INCOMING_FLOW_VARIABLE
						),
						Bpmn.TL_VARIABLE
				))
				.append(Constant.SPACE)
				.append(Lnt.PATTERN_MATCHING_OPERATOR)
				.append(Constant.LINE_FEED)

				// Seventh line:
				// if (ident == id) then
				.append(Utils.indentLNT(3))
				.append(Utils.indent(2))
				.append(Lnt.generateIfStatement(
						Lnt.generateEqualsComparison(
								Bpmn.IDENT_VARIABLE,
								Bpmn.ID_VARIABLE
						)
				))
				.append(Constant.LINE_FEED)

				// Eighth line:
				// return incf
				.append(Utils.indentLNT(4))
				.append(Utils.indent(2))
				.append(Lnt.generateReturnStatement(
						Bpmn.INCOMING_FLOW_VARIABLE
				))
				.append(Constant.LINE_FEED)

				// Ninth line:
				// else
				.append(Utils.indentLNT(3))
				.append(Utils.indent(2))
				.append(Lnt.ELSE)
				.append(Constant.LINE_FEED)

				// Tenth line:
				// return traverse_finals (tl, id)
				.append(Utils.indentLNT(4))
				.append(Utils.indent(2))
				.append(Lnt.generateReturnStatement(
						Lnt.generateFunctionCall(
								Bpmn.TRAVERSE_FINALS_LNT_FUNCTION,
								Bpmn.TL_VARIABLE,
								Bpmn.ID_VARIABLE
						)
				))
				.append(Constant.LINE_FEED)

				// Eleventh line:
				// end if
				.append(Utils.indentLNT(3))
				.append(Utils.indent(2))
				.append(Lnt.END_IF)
				.append(Constant.LINE_FEED)

				// Twelfth line:
				// | nil -> return nil
				.append(Utils.indentLNT(2))
				.append(Lnt.CASE_OPERATOR)
				.append(Constant.SPACE)
				.append(Lnt.EMPTY_LIST)
				.append(Lnt.SPACED_PATTERN_MATCHING_OPERATOR)
				.append(Lnt.generateReturnStatement(
						Lnt.EMPTY_LIST
				))
				.append(Constant.LINE_FEED)

				// Thirteenth line:
				// end case
				.append(Utils.indentLNT(1))
				.append(Lnt.END_CASE)
				.append(Constant.LINE_FEED)

				// Fourteenth line:
				// end function
				.append(Lnt.END_FUNCTION);

		this.writeLntSeparation(builder);
	}

	private void writeTraverseTasksLntFunction(final StringBuilder builder)
	{
		builder // First line:
				// (*----- "traverse_tasks()" LNT Function -----*)
				.append(Lnt.OPEN_MULTILINE_COMMENTARY)
				.append(Utils.getDashesStringOfSize(5))
				.append(Constant.SPACE)
				.append(Constant.DOUBLE_QUOTATION_MARK)
				.append("traverse_tasks()")
				.append(Constant.DOUBLE_QUOTATION_MARK)
				.append(Constant.SPACE)
				.append("LNT Function")
				.append(Constant.SPACE)
				.append(Utils.getDashesStringOfSize(5))
				.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
				.append(Constant.DOUBLE_LINE_FEED)

				// Second line:
				// (*----- Find incoming flows of taks -----*)
				.append(Lnt.OPEN_MULTILINE_COMMENTARY)
				.append(Utils.getDashesStringOfSize(5))
				.append(Constant.SPACE)
				.append("Find incoming flows of tasks")
				.append(Constant.SPACE)
				.append(Utils.getDashesStringOfSize(5))
				.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
				.append(Constant.DOUBLE_LINE_FEED)

				// Third line:
				// function traverse_tasks (tasks: TASKS, id: ID): IDS is
				.append(Lnt.generateFunctionHeader(
						Bpmn.TRAVERSE_TASKS_LNT_FUNCTION,
						Bpmn.SET_OF_IDS_LNT_TYPE,
						false,
						new Lnt.ParametersAndType(Bpmn.TASKS_VARIABLE, Bpmn.SET_OF_TASKS_LNT_TYPE),
						new Lnt.ParametersAndType(Bpmn.ID_VARIABLE, Bpmn.ID_LNT_TYPE)
				))
				.append(Constant.LINE_FEED)

				// Fourth line:
				// case tasks
				.append(Utils.indentLNT(1))
				.append(Lnt.CASE)
				.append(Constant.SPACE)
				.append(Bpmn.TASKS_VARIABLE)
				.append(Constant.LINE_FEED)

				// Fifth line:
				// var ident: ID, incf, outf: IDS, tl: TASKS in
				.append(Utils.indentLNT(2))
				.append(Lnt.generateVariableDeclarationStatement(
						new Lnt.VariablesAndType(Bpmn.IDENT_VARIABLE, Bpmn.ID_LNT_TYPE),
						new Lnt.VariablesAndType(Arrays.asList(Bpmn.INCOMING_FLOW_VARIABLE, Bpmn.OUTGOING_FLOW_VARIABLE), Bpmn.SET_OF_IDS_LNT_TYPE),
						new Lnt.VariablesAndType(Bpmn.TL_VARIABLE, Bpmn.SET_OF_TASKS_LNT_TYPE)
				))
				.append(Constant.LINE_FEED)

				// Sixth line:
				// cons (task (ident, incf, outf), tl) ->
				.append(Utils.indentLNT(2))
				.append(Utils.indent(2))
				.append(Lnt.generateFunctionCall(
						Lnt.PREDEFINED_CONSTRUCTOR_FUNCTION,
						Lnt.generateObjectWithArguments(
								Bpmn.TASK,
								Bpmn.IDENT_VARIABLE,
								Bpmn.INCOMING_FLOW_VARIABLE,
								Bpmn.OUTGOING_FLOW_VARIABLE
						),
						Bpmn.TL_VARIABLE
				))
				.append(Constant.SPACE)
				.append(Lnt.PATTERN_MATCHING_OPERATOR)
				.append(Constant.LINE_FEED)

				// Seventh line:
				// if (ident == id) then
				.append(Utils.indentLNT(3))
				.append(Utils.indent(2))
				.append(Lnt.generateIfStatement(
						Lnt.generateEqualsComparison(
								Bpmn.IDENT_VARIABLE,
								Bpmn.ID_VARIABLE
						)
				))
				.append(Constant.LINE_FEED)

				// Eighth line:
				// return incf
				.append(Utils.indentLNT(4))
				.append(Utils.indent(2))
				.append(Lnt.generateReturnStatement(
						Bpmn.INCOMING_FLOW_VARIABLE
				))
				.append(Constant.LINE_FEED)

				// Ninth line:
				// else
				.append(Utils.indentLNT(3))
				.append(Utils.indent(2))
				.append(Lnt.ELSE)
				.append(Constant.LINE_FEED)

				// Tenth line:
				// return traverse_tasks (tl, id)
				.append(Utils.indentLNT(4))
				.append(Utils.indent(2))
				.append(Lnt.generateReturnStatement(
						Lnt.generateFunctionCall(
								Bpmn.TRAVERSE_TASKS_LNT_FUNCTION,
								Bpmn.TL_VARIABLE,
								Bpmn.ID_VARIABLE
						)
				))
				.append(Constant.LINE_FEED)

				// Eleventh line:
				// end if
				.append(Utils.indentLNT(3))
				.append(Utils.indent(2))
				.append(Lnt.END_IF)
				.append(Constant.LINE_FEED)

				// Twelfth line:
				// | nil -> return nil
				.append(Utils.indentLNT(2))
				.append(Lnt.CASE_OPERATOR)
				.append(Constant.SPACE)
				.append(Lnt.EMPTY_LIST)
				.append(Lnt.SPACED_PATTERN_MATCHING_OPERATOR)
				.append(Lnt.generateReturnStatement(
						Lnt.EMPTY_LIST
				))
				.append(Constant.LINE_FEED)

				// Thirteenth line:
				// end case
				.append(Utils.indentLNT(1))
				.append(Lnt.END_CASE)
				.append(Constant.LINE_FEED)

				// Fourteenth line:
				// end function
				.append(Lnt.END_FUNCTION);

		this.writeLntSeparation(builder);
	}

	private void writeRemoveIncfLntFunction(final StringBuilder builder)
	{
		builder // First line:
				// (*----- "remove_incf()" LNT Function -----*)
				.append(Lnt.OPEN_MULTILINE_COMMENTARY)
				.append(Utils.getDashesStringOfSize(5))
				.append(Constant.SPACE)
				.append(Constant.DOUBLE_QUOTATION_MARK)
				.append("remove_incf()")
				.append(Constant.DOUBLE_QUOTATION_MARK)
				.append(Constant.SPACE)
				.append("LNT Function")
				.append(Constant.SPACE)
				.append(Utils.getDashesStringOfSize(5))
				.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
				.append(Constant.DOUBLE_LINE_FEED)

				// Second line:
				// (*----- Remove incoming flows from activetokens -----*)
				.append(Lnt.OPEN_MULTILINE_COMMENTARY)
				.append(Utils.getDashesStringOfSize(5))
				.append(Constant.SPACE)
				.append("Remove incoming flows from activetokens")
				.append(Constant.SPACE)
				.append(Utils.getDashesStringOfSize(5))
				.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
				.append(Constant.DOUBLE_LINE_FEED)

				// Third line:
				// function remove_incf (bpmn: BPROCESS, activeflows: IDS, mergeid: ID): IDS is
				.append(Lnt.generateFunctionHeader(
						Bpmn.REMOVE_INCOMING_FLOWS_LNT_FUNCTION,
						Bpmn.SET_OF_IDS_LNT_TYPE,
						false,
						new Lnt.ParametersAndType(Bpmn.BPMN_PROCESS_VARIABLE, Bpmn.BPMN_PROCESS_LNT_TYPE),
						new Lnt.ParametersAndType(Bpmn.ACTIVE_FLOWS_VARIABLE, Bpmn.SET_OF_IDS_LNT_TYPE),
						new Lnt.ParametersAndType(Bpmn.MERGE_ID_VARIABLE, Bpmn.ID_LNT_TYPE)
				))
				.append(Constant.LINE_FEED)

				// Fourth line:
				// var incf: IDS in
				.append(Utils.indentLNT(1))
				.append(Lnt.generateVariableDeclarationStatement(
						new Lnt.VariablesAndType(Bpmn.INCOMING_FLOW_VARIABLE, Bpmn.SET_OF_IDS_LNT_TYPE)
				))
				.append(Constant.LINE_FEED)

				// Fifth line:
				// incf := get_incf_by_id (bpmn, mergeid);
				.append(Utils.indentLNT(2))
				.append(Lnt.generateVariableAssignation(
						Bpmn.INCOMING_FLOW_VARIABLE,
						Lnt.generateFunctionCall(
								Bpmn.GET_INCOMING_FLOWS_BY_ID_LNT_FUNCTION,
								Bpmn.BPMN_PROCESS_VARIABLE,
								Bpmn.MERGE_ID_VARIABLE
						)
				))
				.append(Lnt.SEQUENTIAL_COMPOSITION_OPERATOR)
				.append(Constant.DOUBLE_LINE_FEED)

				// Sixth line:
				// return remove_ids_from_set (incf, activeflows)
				.append(Utils.indentLNT(2))
				.append(Lnt.generateReturnStatement(
						Lnt.generateFunctionCall(
								Bpmn.REMOVE_IDS_FROM_SET_LNT_FUNCTION,
								Bpmn.INCOMING_FLOW_VARIABLE,
								Bpmn.ACTIVE_FLOWS_VARIABLE
						)
				))
				.append(Constant.LINE_FEED)

				// Seventh line:
				// end var
				.append(Utils.indentLNT(1))
				.append(Lnt.END_VAR)
				.append(Constant.LINE_FEED)

				// Eighth line:
				// end function
				.append(Lnt.END_FUNCTION);

		this.writeLntSeparation(builder);
	}

	private void writeRemoveSyncLntFunction(final StringBuilder builder)
	{
		builder // First line:
				// (*----- "remove_sync()" LNT Function -----*)
				.append(Lnt.OPEN_MULTILINE_COMMENTARY)
				.append(Utils.getDashesStringOfSize(5))
				.append(Constant.SPACE)
				.append(Constant.DOUBLE_QUOTATION_MARK)
				.append("remove_sync()")
				.append(Constant.DOUBLE_QUOTATION_MARK)
				.append(Constant.SPACE)
				.append("LNT Function")
				.append(Constant.SPACE)
				.append(Utils.getDashesStringOfSize(5))
				.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
				.append(Constant.DOUBLE_LINE_FEED)

				// Second line:
				// function remove_sync (bpmn: BPROCESS, syncstore: IDS, mergeid: ID): IDS is
				.append(Lnt.generateFunctionHeader(
						Bpmn.REMOVE_SYNC_LNT_FUNCTON,
						Bpmn.SET_OF_IDS_LNT_TYPE,
						false,
						new Lnt.ParametersAndType(Bpmn.BPMN_PROCESS_VARIABLE, Bpmn.BPMN_PROCESS_LNT_TYPE),
						new Lnt.ParametersAndType(Bpmn.SYNC_STORE_VARIABLE, Bpmn.SET_OF_IDS_LNT_TYPE),
						new Lnt.ParametersAndType(Bpmn.MERGE_ID_VARIABLE, Bpmn.ID_LNT_TYPE)
				))
				.append(Constant.LINE_FEED)

				// Third line:
				// return remove_incf (bpmn, syncstore, mergeid)
				.append(Utils.indentLNT(1))
				.append(Lnt.generateReturnStatement(
						Lnt.generateFunctionCall(
								Bpmn.REMOVE_INCOMING_FLOWS_LNT_FUNCTION,
								Bpmn.BPMN_PROCESS_VARIABLE,
								Bpmn.SYNC_STORE_VARIABLE,
								Bpmn.MERGE_ID_VARIABLE
						)
				))
				.append(Constant.LINE_FEED)

				// Fourth line:
				// end function
				.append(Lnt.END_FUNCTION);

		this.writeLntSeparation(builder);
	}

	private void writeRemoveIdsFromSetLntFunction(final StringBuilder builder)
	{
		builder // First line:
				// (*----- "remove_ids_from_set()" LNT Function -----*)
				.append(Lnt.OPEN_MULTILINE_COMMENTARY)
				.append(Utils.getDashesStringOfSize(5))
				.append(Constant.SPACE)
				.append(Constant.DOUBLE_QUOTATION_MARK)
				.append("remove_ids_from_set()")
				.append(Constant.DOUBLE_QUOTATION_MARK)
				.append(Constant.SPACE)
				.append("LNT Function")
				.append(Constant.SPACE)
				.append(Utils.getDashesStringOfSize(5))
				.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
				.append(Constant.DOUBLE_LINE_FEED)

				// Second line:
				// (*----- Helper function to remove a set of IDS from the set ----- *)
				.append(Lnt.OPEN_MULTILINE_COMMENTARY)
				.append(Utils.getDashesStringOfSize(5))
				.append(Constant.SPACE)
				.append("Helper function to remove a set of IDS from the set")
				.append(Constant.SPACE)
				.append(Utils.getDashesStringOfSize(5))
				.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
				.append(Constant.DOUBLE_LINE_FEED)

				// Third line:
				// function remove_ids_from_set (toremove, inputset: IDS): IDS is
				.append(Lnt.generateFunctionHeader(
						Bpmn.REMOVE_IDS_FROM_SET_LNT_FUNCTION,
						Bpmn.SET_OF_IDS_LNT_TYPE,
						false,
						new Lnt.ParametersAndType(Arrays.asList(Bpmn.TO_REMOVE_VARIABLE, Bpmn.INPUT_SET_VARIABLE), Bpmn.SET_OF_IDS_LNT_TYPE)
				))
				.append(Constant.LINE_FEED)

				// Fourth line:
				// return minus (inputset, toremove)
				.append(Utils.indentLNT(1))
				.append(Lnt.generateReturnStatement(
						Lnt.generateFunctionCall(
								Lnt.PREDEFINED_FUNCTION_MINUS,
								Bpmn.INPUT_SET_VARIABLE,
								Bpmn.TO_REMOVE_VARIABLE
						)
				))
				.append(Constant.LINE_FEED)

				// Fifth line:
				// end function
				.append(Lnt.END_FUNCTION);

		this.writeLntSeparation(builder);
	}

	private void writeFindIncfNodesAllLntFunction(final StringBuilder builder)
	{
		builder // First line:
				// (*----- "find_incf_nodes_all()" LNT Function -----*)
				.append(Lnt.OPEN_MULTILINE_COMMENTARY)
				.append(Utils.getDashesStringOfSize(5))
				.append(Constant.SPACE)
				.append(Constant.DOUBLE_QUOTATION_MARK)
				.append("find_incf_nodes_all()")
				.append(Constant.DOUBLE_QUOTATION_MARK)
				.append(Constant.SPACE)
				.append("LNT Function")
				.append(Constant.SPACE)
				.append(Utils.getDashesStringOfSize(5))
				.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
				.append(Constant.DOUBLE_LINE_FEED)

				// Second line:
				// (*--------------------------------------------------------------------------*)
				.append(Lnt.OPEN_MULTILINE_COMMENTARY)
				.append(Lnt.MAX_DASHES_IN_MULTILINE_COMMENTARY)
				.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
				.append(Constant.LINE_FEED)

				// Third line:
				// (*--------------------------------------------------------------------------*)
				.append(Lnt.OPEN_MULTILINE_COMMENTARY)
				.append(Lnt.MAX_DASHES_IN_MULTILINE_COMMENTARY)
				.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
				.append(Constant.LINE_FEED)

				// Fourth line:
				// (*--------------------------------------------------------------------------*)
				.append(Lnt.OPEN_MULTILINE_COMMENTARY)
				.append(Lnt.MAX_DASHES_IN_MULTILINE_COMMENTARY)
				.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
				.append(Constant.LINE_FEED)

				// Sixth line:
				// (*------------Another version of code for process node traversal------------*)
				.append(Lnt.OPEN_MULTILINE_COMMENTARY)
				.append(Utils.getDashesStringOfSize(11))
				.append(Constant.SPACE)
				.append("Another version of code for process node traversal")
				.append(Constant.SPACE)
				.append(Utils.getDashesStringOfSize(11))
				.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
				.append(Constant.LINE_FEED)

				// Seventh line:
				// (*------------------Fix: Remove the code from final version-----------------*)
				.append(Lnt.OPEN_MULTILINE_COMMENTARY)
				.append(Utils.getDashesStringOfSize(7))
				.append(Constant.SPACE)
				.append("Fix: Remove the code from final version")
				.append(Constant.SPACE)
				.append(Utils.getDashesStringOfSize(6))
				.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
				.append(Constant.LINE_FEED)

				// Eighth line:
				// (*--------------------------------------------------------------------------*)
				.append(Lnt.OPEN_MULTILINE_COMMENTARY)
				.append(Lnt.MAX_DASHES_IN_MULTILINE_COMMENTARY)
				.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
				.append(Constant.LINE_FEED)

				// Ninth line:
				// (*--------------------------------------------------------------------------*)
				.append(Lnt.OPEN_MULTILINE_COMMENTARY)
				.append(Lnt.MAX_DASHES_IN_MULTILINE_COMMENTARY)
				.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
				.append(Constant.LINE_FEED)

				// Tenth line:
				// (*--------------------------------------------------------------------------*)
				.append(Lnt.OPEN_MULTILINE_COMMENTARY)
				.append(Lnt.MAX_DASHES_IN_MULTILINE_COMMENTARY)
				.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
				.append(Constant.DOUBLE_LINE_FEED)

				// Eleventh line:
				// (*----- Traverse across all nodes in search of the node -----*)
				.append(Lnt.OPEN_MULTILINE_COMMENTARY)
				.append(Utils.getDashesStringOfSize(5))
				.append(Constant.SPACE)
				.append("Traverse across all nodes in search of the node")
				.append(Constant.SPACE)
				.append(Utils.getDashesStringOfSize(5))
				.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
				.append(Constant.DOUBLE_LINE_FEED)

				// Twelfth line:
				// function find_incf_nodes_all (nodes: NODES, id: ID): IDS is
				.append(Lnt.generateFunctionHeader(
						Bpmn.FIND_ALL_INCOMING_FLOWS_NODES_LNT_FUNCTION,
						Bpmn.SET_OF_IDS_LNT_TYPE,
						false,
						new Lnt.ParametersAndType(Bpmn.NODES_VARIABLE, Bpmn.SET_OF_NODES_LNT_TYPE),
						new Lnt.ParametersAndType(Bpmn.ID_VARIABLE, Bpmn.ID_LNT_TYPE)
				))
				.append(Constant.LINE_FEED)

				// Thirteenth line:
				// case nodes
				.append(Utils.indentLNT(1))
				.append(Lnt.CASE)
				.append(Constant.SPACE)
				.append(Bpmn.NODES_VARIABLE)
				.append(Constant.LINE_FEED)

				// Fourteenth line:
				// var
				.append(Utils.indentLNT(2))
				.append(Lnt.VAR)
				.append(Constant.LINE_FEED)

				// Fifteenth line:
				// gateways: GATEWAYS, initial: INITIAL, finals: FINALS, tasks: TASKS,
				.append(Utils.indentLNT(3))
				.append(Lnt.generateVariablesDefinition(
						Bpmn.GATEWAYS_VARIABLE,
						Bpmn.SET_OF_GATEWAYS_LNT_TYPE
				))
				.append(Constant.COMA_AND_SPACE)
				.append(Lnt.generateVariablesDefinition(
						Bpmn.INITIAL_VARIABLE,
						Bpmn.INITIAL_EVENT_LNT_TYPE
				))
				.append(Constant.COMA_AND_SPACE)
				.append(Lnt.generateVariablesDefinition(
						Bpmn.FINALS_VARIABLE,
						Bpmn.SET_OF_END_EVENTS_LNT_TYPE
				))

				.append(Constant.COMA_AND_SPACE)
				.append(Lnt.generateVariablesDefinition(
						Bpmn.TASKS_VARIABLE,
						Bpmn.SET_OF_TASKS_LNT_TYPE
				))
				.append(Constant.COMA)
				.append(Constant.LINE_FEED)

				// Sixteenth line:
				// tl: NODES
				.append(Utils.indentLNT(3))
				.append(Lnt.generateVariablesDefinition(
						Bpmn.TL_VARIABLE,
						Bpmn.SET_OF_NODES_LNT_TYPE
				))
				.append(Constant.LINE_FEED)

				// Seventeenth line:
				// in
				.append(Utils.indentLNT(2))
				.append(Lnt.IN)
				.append(Constant.LINE_FEED)

				// Eighteenth line:
				// cons (g (gateways), tl) -> return find_incf_gatewaysv2 (gateways,
				.append(Utils.indentLNT(2))
				.append(Utils.indent(2))
				.append(Lnt.generateFunctionCall(
						Lnt.PREDEFINED_CONSTRUCTOR_FUNCTION,
						Lnt.generateObjectWithArguments(
								Bpmn.GATEWAYS_IDENTIFIER,
								Bpmn.GATEWAYS_VARIABLE
						),
						Bpmn.TL_VARIABLE
				))
				.append(Lnt.SPACED_PATTERN_MATCHING_OPERATOR)
				.append(Lnt.RETURN)
				.append(Constant.SPACE)
				.append(Bpmn.FIND_INCOMING_FLOWS_GATEWAYS_V2_LNT_FUNCTION)
				.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
				.append(Bpmn.GATEWAYS_VARIABLE)
				.append(Constant.COMA)
				.append(Constant.LINE_FEED)

				// Nineteenth line:
				// id, tl)
				.append(Utils.indent(64))
				.append(Bpmn.ID_VARIABLE)
				.append(Constant.COMA_AND_SPACE)
				.append(Bpmn.TL_VARIABLE)
				.append(Constant.RIGHT_PARENTHESIS)
				.append(Constant.LINE_FEED)

				// Twentieth line:
				// | cons (i (initial), tl) -> return find_incf_nodes_all (tl, id)
				.append(Utils.indentLNT(2))
				.append(Lnt.CASE_OPERATOR)
				.append(Constant.SPACE)
				.append(Lnt.generateFunctionCall(
						Lnt.PREDEFINED_CONSTRUCTOR_FUNCTION,
						Lnt.generateObjectWithArguments(
								Bpmn.INITIAL_NODES_IDENTIFIER,
								Bpmn.INITIAL_VARIABLE
						),
						Bpmn.TL_VARIABLE
				))
				.append(Lnt.SPACED_PATTERN_MATCHING_OPERATOR)
				.append(Lnt.generateReturnStatement(
						Lnt.generateFunctionCall(
								Bpmn.FIND_ALL_INCOMING_FLOWS_NODES_LNT_FUNCTION,
								Bpmn.TL_VARIABLE,
								Bpmn.ID_VARIABLE
						)
				))
				.append(Constant.LINE_FEED)

				// Twenty-first line:
				// | cons (f (finals), tl) -> return find_incf_finals (finals, id, tl)
				.append(Utils.indentLNT(2))
				.append(Lnt.CASE_OPERATOR)
				.append(Constant.SPACE)
				.append(Lnt.generateFunctionCall(
						Lnt.PREDEFINED_CONSTRUCTOR_FUNCTION,
						Lnt.generateObjectWithArguments(
								Bpmn.END_EVENTS_IDENTIFIER,
								Bpmn.FINALS_VARIABLE
						),
						Bpmn.TL_VARIABLE
				))
				.append(Lnt.SPACED_PATTERN_MATCHING_OPERATOR)
				.append(Lnt.generateReturnStatement(
						Lnt.generateFunctionCall(
								Bpmn.FIND_END_NODES_INCOMING_FLOWS_LNT_FUNCTION,
								Bpmn.FINALS_VARIABLE,
								Bpmn.ID_VARIABLE,
								Bpmn.TL_VARIABLE
						)
				))
				.append(Constant.LINE_FEED)

				// Twenty-second line:
				// | cons (t (tasks), tl) -> return find_incf_tasks (tasks, id, tl)
				.append(Utils.indentLNT(2))
				.append(Lnt.CASE_OPERATOR)
				.append(Constant.SPACE)
				.append(Lnt.generateFunctionCall(
						Lnt.PREDEFINED_CONSTRUCTOR_FUNCTION,
						Lnt.generateObjectWithArguments(
								Bpmn.TASKS_IDENTIFIER,
								Bpmn.TASKS_VARIABLE
						),
						Bpmn.TL_VARIABLE
				))
				.append(Lnt.SPACED_PATTERN_MATCHING_OPERATOR)
				.append(Lnt.generateReturnStatement(
						Lnt.generateFunctionCall(
								Bpmn.FIND_TASKS_INCOMING_FLOWS_LNT_FUNCTION,
								Bpmn.TASKS_VARIABLE,
								Bpmn.ID_VARIABLE,
								Bpmn.TL_VARIABLE
						)
				))
				.append(Constant.LINE_FEED)

				// Twenty-third line
				// | nil -> return nil
				.append(Utils.indentLNT(2))
				.append(Lnt.CASE_OPERATOR)
				.append(Constant.SPACE)
				.append(Lnt.EMPTY_LIST)
				.append(Constant.SPACE)
				.append(Lnt.SPACED_PATTERN_MATCHING_OPERATOR)
				.append(Lnt.generateReturnStatement(
						Lnt.EMPTY_LIST
				))
				.append(Constant.LINE_FEED)

				// Twenty-fourth line
				// end case
				.append(Utils.indentLNT(1))
				.append(Lnt.END_CASE)
				.append(Constant.LINE_FEED)

				// Twenty-fifth line
				// end function
				.append(Lnt.END_FUNCTION);

		this.writeLntSeparation(builder);
	}

	private void writeFindIncfGatewaysV2LntFunction(final StringBuilder builder)
	{
		builder // First line:
				// (*----- "find_incf_gatewaysv2()" LNT Function -----*)
				.append(Lnt.OPEN_MULTILINE_COMMENTARY)
				.append(Utils.getDashesStringOfSize(5))
				.append(Constant.SPACE)
				.append(Constant.DOUBLE_QUOTATION_MARK)
				.append("find_incf_gatewaysv2()")
				.append(Constant.DOUBLE_QUOTATION_MARK)
				.append(Constant.SPACE)
				.append("LNT Function")
				.append(Constant.SPACE)
				.append(Utils.getDashesStringOfSize(5))
				.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
				.append(Constant.DOUBLE_LINE_FEED)

				// Second line:
				// (*----- Find incoming flows of gateways -----*)
				.append(Lnt.OPEN_MULTILINE_COMMENTARY)
				.append(Utils.getDashesStringOfSize(5))
				.append(Constant.SPACE)
				.append("Find incoming flows of gateways")
				.append(Constant.SPACE)
				.append(Utils.getDashesStringOfSize(5))
				.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
				.append(Constant.DOUBLE_LINE_FEED)

				// Third, fourth and fifth lines:
				// function
				//    find_incf_gatewaysv2 (gateways: GATEWAYS, id: ID, nextnodes: NODES): IDS
				// is
				.append(Lnt.generateFunctionHeader(
						Bpmn.FIND_INCOMING_FLOWS_GATEWAYS_V2_LNT_FUNCTION,
						Bpmn.SET_OF_IDS_LNT_TYPE,
						true,
						new Lnt.ParametersAndType(Bpmn.GATEWAYS_VARIABLE, Bpmn.SET_OF_GATEWAYS_LNT_TYPE),
						new Lnt.ParametersAndType(Bpmn.ID_VARIABLE, Bpmn.ID_LNT_TYPE),
						new Lnt.ParametersAndType(Bpmn.NEXT_NODES_VARIABLE, Bpmn.SET_OF_NODES_LNT_TYPE)
				))
				.append(Constant.LINE_FEED)

				// Sixth line:
				// case gateways
				.append(Utils.indentLNT(1))
				.append(Lnt.CASE)
				.append(Constant.SPACE)
				.append(Bpmn.GATEWAYS_VARIABLE)
				.append(Constant.LINE_FEED)

				// Seventh line:
				// var
				.append(Utils.indentLNT(2))
				.append(Lnt.VAR)
				.append(Constant.LINE_FEED)

				// Eighth line:
				// ident: ID, pattern: GPATTERN, sort: GSORT, incf, outf: IDS,
				.append(Utils.indentLNT(3))
				.append(Lnt.generateVariablesDefinition(
						Bpmn.IDENT_VARIABLE,
						Bpmn.ID_LNT_TYPE
				))
				.append(Constant.COMA_AND_SPACE)
				.append(Lnt.generateVariablesDefinition(
						Bpmn.PATTERN_VARIABLE,
						Bpmn.GATEWAY_PATTERN_LNT_TYPE
				))
				.append(Constant.COMA_AND_SPACE)
				.append(Lnt.generateVariablesDefinition(
						Bpmn.SORT_VARIABLE,
						Bpmn.GATEWAY_TYPE_LNT_TYPE
				))
				.append(Constant.COMA_AND_SPACE)
				.append(Lnt.generateVariablesDefinition(
						Arrays.asList(Bpmn.INCOMING_FLOW_VARIABLE, Bpmn.OUTGOING_FLOW_VARIABLE),
						Bpmn.SET_OF_IDS_LNT_TYPE
				))
				.append(Constant.COMA)
				.append(Constant.LINE_FEED)

				// Ninth line:
				// tl: GATEWAYS
				.append(Utils.indentLNT(3))
				.append(Lnt.generateVariablesDefinition(
						Bpmn.TL_VARIABLE,
						Bpmn.SET_OF_GATEWAYS_LNT_TYPE
				))
				.append(Constant.LINE_FEED)

				// Tenth line:
				// in
				.append(Utils.indentLNT(2))
				.append(Lnt.IN)
				.append(Constant.LINE_FEED)

				// Eleventh line:
				// cons (gateway (ident, pattern, sort, incf, outf), tl) ->
				.append(Utils.indentLNT(2))
				.append(Utils.indent(2))
				.append(Lnt.generateFunctionCall(
						Lnt.PREDEFINED_CONSTRUCTOR_FUNCTION,
						Lnt.generateObjectWithArguments(
								Bpmn.GATEWAY,
								Bpmn.IDENT_VARIABLE,
								Bpmn.PATTERN_VARIABLE,
								Bpmn.SORT_VARIABLE,
								Bpmn.INCOMING_FLOW_VARIABLE,
								Bpmn.OUTGOING_FLOW_VARIABLE
						),
						Bpmn.TL_VARIABLE
				))
				.append(Constant.SPACE)
				.append(Lnt.PATTERN_MATCHING_OPERATOR)
				.append(Constant.LINE_FEED)

				// Twelfth line:
				// if (ident == id) then
				.append(Utils.indentLNT(3))
				.append(Utils.indent(2))
				.append(Lnt.generateIfStatement(
						Lnt.generateEqualsComparison(
								Bpmn.IDENT_VARIABLE,
								Bpmn.ID_VARIABLE
						)
				))
				.append(Constant.LINE_FEED)

				// Thirteenth line:
				// return incf
				.append(Utils.indentLNT(4))
				.append(Utils.indent(2))
				.append(Lnt.generateReturnStatement(
						Bpmn.INCOMING_FLOW_VARIABLE
				))
				.append(Constant.LINE_FEED)

				// Fourteenth line:
				// else
				.append(Utils.indentLNT(3))
				.append(Utils.indent(2))
				.append(Lnt.ELSE)
				.append(Constant.LINE_FEED)

				// Fifteenth line:
				// return find_incf_gatewaysv2 (tl, id, nextnodes)
				.append(Utils.indentLNT(4))
				.append(Utils.indent(2))
				.append(Lnt.generateReturnStatement(
						Lnt.generateFunctionCall(
								Bpmn.FIND_INCOMING_FLOWS_GATEWAYS_V2_LNT_FUNCTION,
								Bpmn.TL_VARIABLE,
								Bpmn.ID_VARIABLE,
								Bpmn.NEXT_NODES_VARIABLE
						)
				))
				.append(Constant.LINE_FEED)

				// Sixteenth line:
				// end if
				.append(Utils.indentLNT(3))
				.append(Utils.indent(2))
				.append(Lnt.END_IF)
				.append(Constant.LINE_FEED)

				// Seventeenth line:
				// | nil -> return find_incf_nodes_all (nextnodes, id)
				.append(Utils.indentLNT(2))
				.append(Lnt.CASE_OPERATOR)
				.append(Constant.SPACE)
				.append(Lnt.EMPTY_LIST)
				.append(Lnt.SPACED_PATTERN_MATCHING_OPERATOR)
				.append(Lnt.generateReturnStatement(
						Lnt.generateFunctionCall(
								Bpmn.FIND_ALL_INCOMING_FLOWS_NODES_LNT_FUNCTION,
								Bpmn.NEXT_NODES_VARIABLE,
								Bpmn.ID_VARIABLE
						)
				))
				.append(Constant.LINE_FEED)

				// Eighteenth line:
				// end case
				.append(Utils.indentLNT(1))
				.append(Lnt.END_CASE)
				.append(Constant.LINE_FEED)

				// Nineteenth line:
				// end function
				.append(Lnt.END_FUNCTION);

		this.writeLntSeparation(builder);
	}

	private void writeFindIncfFinalsLntFunction(final StringBuilder builder)
	{
		builder // First line:
				// (*----- "find_incf_finals()" LNT Function -----*)
				.append(Lnt.OPEN_MULTILINE_COMMENTARY)
				.append(Utils.getDashesStringOfSize(5))
				.append(Constant.SPACE)
				.append(Constant.DOUBLE_QUOTATION_MARK)
				.append("find_incf_finals()")
				.append(Constant.DOUBLE_QUOTATION_MARK)
				.append(Constant.SPACE)
				.append("LNT Function")
				.append(Constant.SPACE)
				.append(Utils.getDashesStringOfSize(5))
				.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
				.append(Constant.DOUBLE_LINE_FEED)

				// Second line:
				// (*----- Find incoming flows of end events -----*)
				.append(Lnt.OPEN_MULTILINE_COMMENTARY)
				.append(Utils.getDashesStringOfSize(5))
				.append(Constant.SPACE)
				.append("Find incoming flows of end events")
				.append(Constant.SPACE)
				.append(Utils.getDashesStringOfSize(5))
				.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
				.append(Constant.DOUBLE_LINE_FEED)

				// Third line:
				// function find_incf_finals (finals: FINALS, id: ID, nextnodes: NODES): IDS is
				.append(Lnt.generateFunctionHeader(
						Bpmn.FIND_END_NODES_INCOMING_FLOWS_LNT_FUNCTION,
						Bpmn.SET_OF_IDS_LNT_TYPE,
						false,
						new Lnt.ParametersAndType(Bpmn.FINALS_VARIABLE, Bpmn.SET_OF_END_EVENTS_LNT_TYPE),
						new Lnt.ParametersAndType(Bpmn.ID_VARIABLE, Bpmn.ID_LNT_TYPE),
						new Lnt.ParametersAndType(Bpmn.NEXT_NODES_VARIABLE, Bpmn.SET_OF_NODES_LNT_TYPE)
				))
				.append(Constant.LINE_FEED)

				// Fourth line:
				// case finals
				.append(Utils.indentLNT(1))
				.append(Lnt.CASE)
				.append(Constant.SPACE)
				.append(Bpmn.FINALS_VARIABLE)
				.append(Constant.LINE_FEED)

				// Fifth line:
				// var ident: ID, incf: IDS, tl: FINALS in
				.append(Utils.indentLNT(2))
				.append(Lnt.generateVariableDeclarationStatement(
						new Lnt.VariablesAndType(Bpmn.IDENT_VARIABLE, Bpmn.ID_LNT_TYPE),
						new Lnt.VariablesAndType(Bpmn.INCOMING_FLOW_VARIABLE, Bpmn.SET_OF_IDS_LNT_TYPE),
						new Lnt.VariablesAndType(Bpmn.TL_VARIABLE, Bpmn.SET_OF_END_EVENTS_LNT_TYPE)
				))
				.append(Constant.LINE_FEED)

				// Sixth line:
				// cons (final (ident, incf), tl) ->
				.append(Utils.indentLNT(2))
				.append(Utils.indent(2))
				.append(Lnt.generateFunctionCall(
						Lnt.PREDEFINED_CONSTRUCTOR_FUNCTION,
						Lnt.generateObjectWithArguments(
								Bpmn.FINAL,
								Bpmn.IDENT_VARIABLE,
								Bpmn.INCOMING_FLOW_VARIABLE
						),
						Bpmn.TL_VARIABLE
				))
				.append(Constant.SPACE)
				.append(Lnt.PATTERN_MATCHING_OPERATOR)
				.append(Constant.LINE_FEED)

				// Seventh line:
				// if (ident == id) then
				.append(Utils.indentLNT(3))
				.append(Utils.indent(2))
				.append(Lnt.generateIfStatement(
						Lnt.generateEqualsComparison(
								Bpmn.IDENT_VARIABLE,
								Bpmn.ID_VARIABLE
						)
				))
				.append(Constant.LINE_FEED)

				// Eighth line:
				// return incf
				.append(Utils.indentLNT(4))
				.append(Utils.indent(2))
				.append(Lnt.generateReturnStatement(
						Bpmn.INCOMING_FLOW_VARIABLE
				))
				.append(Constant.LINE_FEED)

				// Ninth line:
				// else
				.append(Utils.indentLNT(3))
				.append(Utils.indent(2))
				.append(Lnt.ELSE)
				.append(Constant.LINE_FEED)

				// Tenth line:
				// return find_incf_finals (tl, id, nextnodes)
				.append(Utils.indentLNT(4))
				.append(Utils.indent(2))
				.append(Lnt.generateReturnStatement(
						Lnt.generateFunctionCall(
								Bpmn.FIND_END_NODES_INCOMING_FLOWS_LNT_FUNCTION,
								Bpmn.TL_VARIABLE,
								Bpmn.ID_VARIABLE,
								Bpmn.NEXT_NODES_VARIABLE
						)
				))
				.append(Constant.LINE_FEED)

				// Eleventh line:
				// end if
				.append(Utils.indentLNT(3))
				.append(Utils.indent(2))
				.append(Lnt.END_IF)
				.append(Constant.LINE_FEED)

				// Twelfth line:
				// | nil -> return find_incf_nodes_all (nextnodes, id)
				.append(Utils.indentLNT(2))
				.append(Lnt.CASE_OPERATOR)
				.append(Constant.SPACE)
				.append(Lnt.EMPTY_LIST)
				.append(Lnt.SPACED_PATTERN_MATCHING_OPERATOR)
				.append(Lnt.generateReturnStatement(
						Lnt.generateFunctionCall(
								Bpmn.FIND_ALL_INCOMING_FLOWS_NODES_LNT_FUNCTION,
								Bpmn.NEXT_NODES_VARIABLE,
								Bpmn.ID_VARIABLE
						)
				))
				.append(Constant.LINE_FEED)

				// Thirteenth line:
				// end case
				.append(Utils.indentLNT(1))
				.append(Lnt.END_CASE)
				.append(Constant.LINE_FEED)

				// Fourteenth line:
				// end function
				.append(Lnt.END_FUNCTION);

		this.writeLntSeparation(builder);
	}

	private void writeFindIncfTasksLntFunction(final StringBuilder builder)
	{
		builder // First line:
				// (*----- "find_incf_tasks()" LNT Function -----*)
				.append(Lnt.OPEN_MULTILINE_COMMENTARY)
				.append(Utils.getDashesStringOfSize(5))
				.append(Constant.SPACE)
				.append(Constant.DOUBLE_QUOTATION_MARK)
				.append("find_incf_tasks()")
				.append(Constant.DOUBLE_QUOTATION_MARK)
				.append(Constant.SPACE)
				.append("LNT Function")
				.append(Constant.SPACE)
				.append(Utils.getDashesStringOfSize(5))
				.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
				.append(Constant.DOUBLE_LINE_FEED)

				// Second line:
				// (*-------- Find incoming flows of taks ------------*)
				.append(Lnt.OPEN_MULTILINE_COMMENTARY)
				.append(Utils.getDashesStringOfSize(5))
				.append(Constant.SPACE)
				.append("Find incoming flows of tasks")
				.append(Constant.SPACE)
				.append(Utils.getDashesStringOfSize(5))
				.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
				.append(Constant.DOUBLE_LINE_FEED)

				// Third line:
				// function find_incf_tasks (tasks: TASKS, id: ID, nextnodes: NODES): IDS is
				.append(Lnt.generateFunctionHeader(
						Bpmn.FIND_TASKS_INCOMING_FLOWS_LNT_FUNCTION,
						Bpmn.SET_OF_IDS_LNT_TYPE,
						false,
						new Lnt.ParametersAndType(Bpmn.TASKS_VARIABLE, Bpmn.SET_OF_TASKS_LNT_TYPE),
						new Lnt.ParametersAndType(Bpmn.ID_VARIABLE, Bpmn.ID_LNT_TYPE),
						new Lnt.ParametersAndType(Bpmn.NEXT_NODES_VARIABLE, Bpmn.SET_OF_NODES_LNT_TYPE)
				))
				.append(Constant.LINE_FEED)

				// Fourth line:
				// case tasks
				.append(Utils.indentLNT(1))
				.append(Lnt.CASE)
				.append(Constant.SPACE)
				.append(Bpmn.TASKS_VARIABLE)
				.append(Constant.LINE_FEED)

				// Fifth line:
				// var ident: ID, incf, outf: IDS, tl: TASKS in
				.append(Utils.indentLNT(2))
				.append(Lnt.generateVariableDeclarationStatement(
						new Lnt.VariablesAndType(Bpmn.IDENT_VARIABLE, Bpmn.ID_LNT_TYPE),
						new Lnt.VariablesAndType(Arrays.asList(Bpmn.INCOMING_FLOW_VARIABLE, Bpmn.OUTGOING_FLOW_VARIABLE), Bpmn.SET_OF_IDS_LNT_TYPE),
						new Lnt.VariablesAndType(Bpmn.TL_VARIABLE, Bpmn.SET_OF_TASKS_LNT_TYPE)
				))
				.append(Constant.LINE_FEED)

				// Sixth line:
				// cons (task (ident, incf, outf), tl) ->
				.append(Utils.indentLNT(2))
				.append(Utils.indent(2))
				.append(Lnt.generateFunctionCall(
						Lnt.PREDEFINED_CONSTRUCTOR_FUNCTION,
						Lnt.generateObjectWithArguments(
								Bpmn.TASK,
								Bpmn.IDENT_VARIABLE,
								Bpmn.INCOMING_FLOW_VARIABLE,
								Bpmn.OUTGOING_FLOW_VARIABLE
						),
						Bpmn.TL_VARIABLE
				))
				.append(Constant.SPACE)
				.append(Lnt.PATTERN_MATCHING_OPERATOR)
				.append(Constant.LINE_FEED)

				// Seventh line:
				// if (ident == id) then
				.append(Utils.indentLNT(3))
				.append(Utils.indent(2))
				.append(Lnt.generateIfStatement(
						Lnt.generateEqualsComparison(
								Bpmn.IDENT_VARIABLE,
								Bpmn.ID_VARIABLE
						)
				))
				.append(Constant.LINE_FEED)

				// Eighth line:
				// return incf
				.append(Utils.indentLNT(4))
				.append(Utils.indent(2))
				.append(Lnt.generateReturnStatement(
						Bpmn.INCOMING_FLOW_VARIABLE
				))
				.append(Constant.LINE_FEED)

				// Ninth line:
				// else
				.append(Utils.indentLNT(3))
				.append(Utils.indent(2))
				.append(Lnt.ELSE)
				.append(Constant.LINE_FEED)

				// Tenth line:
				// return find_incf_tasks (tl, id, nextnodes)
				.append(Utils.indentLNT(4))
				.append(Utils.indent(2))
				.append(Lnt.generateReturnStatement(
						Lnt.generateFunctionCall(
								Bpmn.FIND_TASKS_INCOMING_FLOWS_LNT_FUNCTION,
								Bpmn.TL_VARIABLE,
								Bpmn.ID_VARIABLE,
								Bpmn.NEXT_NODES_VARIABLE
						)
				))
				.append(Constant.LINE_FEED)

				// Eleventh line:
				// end if
				.append(Utils.indentLNT(3))
				.append(Utils.indent(2))
				.append(Lnt.END_IF)
				.append(Constant.LINE_FEED)

				// Twelfth line:
				// | nil -> return find_incf_nodes_all (nextnodes, id)
				.append(Utils.indentLNT(2))
				.append(Lnt.CASE_OPERATOR)
				.append(Constant.SPACE)
				.append(Lnt.EMPTY_LIST)
				.append(Lnt.SPACED_PATTERN_MATCHING_OPERATOR)
				.append(Lnt.generateReturnStatement(
						Lnt.generateFunctionCall(
								Bpmn.FIND_ALL_INCOMING_FLOWS_NODES_LNT_FUNCTION,
								Bpmn.NEXT_NODES_VARIABLE,
								Bpmn.ID_VARIABLE
						)
				))
				.append(Constant.LINE_FEED)

				// Thirteenth line:
				// end case
				.append(Utils.indentLNT(1))
				.append(Lnt.END_CASE)
				.append(Constant.LINE_FEED)

				// Fourteenth line:
				// end function
				.append(Lnt.END_FUNCTION);

		this.writeLntSeparation(builder);
	}

	private void writeModuleEpilogue(final StringBuilder builder)
	{
		builder.append(Lnt.END_MODULE);
	}
}