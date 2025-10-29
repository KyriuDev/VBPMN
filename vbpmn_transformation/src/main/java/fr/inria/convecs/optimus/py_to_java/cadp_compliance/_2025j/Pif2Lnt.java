package fr.inria.convecs.optimus.py_to_java.cadp_compliance._2025j;

import fr.inria.convecs.optimus.constants.*;
import fr.inria.convecs.optimus.pif.Peer;
import fr.inria.convecs.optimus.pif.SequenceFlow;
import fr.inria.convecs.optimus.pif.WorkflowNode;
import fr.inria.convecs.optimus.py_to_java.PyToJavaUtils;
import fr.inria.convecs.optimus.py_to_java.cadp_compliance.generics.Pif2LntGeneric;
import fr.inria.convecs.optimus.util.*;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import org.jgrapht.alg.cycle.CycleDetector;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class Pif2Lnt extends Pif2LntGeneric
{
	private static final Logger logger = LoggerFactory.getLogger(Pif2Lnt.class);
	private static boolean TODO = true;

	public Pif2Lnt(boolean isBalanced)
	{
		super(isBalanced);
	}

	public Pif2Lnt()
	{

	}

	public boolean pairListContainsIdentifier(final Collection<Pair<String, Integer>> pairList,
											  final String identifier)
	{
		for (Pair<String, Integer> pair : pairList)
		{
			if (pair.getLeft().equals(identifier))
			{
				return true;
			}
		}

		return false;
	}

	/**
	 * Dumps alphabet (list of strings) in the given file.
	 *
	 * @param alphabet      is the alphabet to dump
	 * @param stringBuilder is the stringBuilder in which the alphabet is dumped
	 * @param addAny        is a boolean indicating whether to add "any" or not
	 */
	public boolean dumpAlphabet(final ArrayList<String> alphabet,
								final StringBuilder stringBuilder,
								final boolean addAny)
	{
		final int nbElem = alphabet.size();
		boolean lineJumped = false;

		if (nbElem > 0)
		{
			stringBuilder.append(Constant.LEFT_SQUARE_BRACKET);
			int counter = 1;
			int nbCharCurrentLine = 14;

			for (String element : alphabet)
			{
				if (counter != 1)
				{
					if (nbCharCurrentLine + element.length() + 1 > Lnt.MAX_CHAR_PER_LINE)
					{
						lineJumped = true;
						stringBuilder.append(Constant.LINE_FEED)
								.append(Utils.indent(Constant.PROCESS_INDENT_LENGTH));
						nbCharCurrentLine = Constant.PROCESS_INDENT_LENGTH + element.length();
					}
					else
					{
						nbCharCurrentLine += element.length();
					}
				}
				else
				{
					nbCharCurrentLine += element.length();
				}

				stringBuilder.append(element);

				counter++;

				if (counter <= nbElem)
				{
					stringBuilder.append(Constant.COMA_AND_SPACE);
					nbCharCurrentLine += 2;
				}
			}

			if (addAny)
			{
				stringBuilder.append(Constant.COLON_AND_SPACE)
						.append(Lnt.ANY);
			}

			stringBuilder.append(Constant.RIGHT_SQUARE_BRACKET);
			//stringBuilder.close();
		}

		return lineJumped;
	}

	/**
	 * Computes all combinations, in sorted order, any possible number from 1 to size(list)
	 *
	 * @param list the list on which combinations should be computed
	 */
	public ArrayList<ArrayList<String>> computeAllCombinations(final ArrayList<String> list)
	{
		final Collection<Collection<String>> allCombinations = PyToJavaUtils.getCombinationsOf(list);

		/*
			The PyToJavaUtils.getCombinationsOf(list) method does not necessarily return the combinations
			in ascending size, thus we need to reorder the combinations to match with the Python implementation
		 */

		final ArrayList<ArrayList<String>> orderedCombinations = new ArrayList<>();

		int currentSize = 1;
		boolean found = true;

		while (found)
		{
			found = false;

			for (Collection<String> combination : allCombinations)
			{
				if (combination.size() == currentSize)
				{
					orderedCombinations.add((ArrayList<String>) combination);
					found = true;
				}
			}

			currentSize++;
		}

		return orderedCombinations;
	}

	/**
	 * Takes a list of couples (ident,depth) resulting from the reachableOrJoin() method call
	 * and a number of outgoing flows. Checks if all flows lead to a same join.
	 *
	 * @param couplesList is the list of couples
	 * @param nbFlows     is the number of flows
	 * @return the join identifier if yes, "" otherwise.
	 */
	public String analyzeReachabilityResults(final ArrayList<Pair<String, Integer>> couplesList,
											 final int nbFlows)
	{
		//First, we check whether there is at least a corresponding join with depth 0 (there is at most one)
		boolean existJoin = false;
		String joinIdent = "";

		for (Pair<String, Integer> couple : couplesList)
		{
			if (couple.getRight() == 0)
			{
				joinIdent = couple.getLeft();
				existJoin = true;
				break;
			}
		}

		if (existJoin)
		{
			//We check if there are as many couples with the join identifiers as the number of flows
			int counter = 0;

			for (Pair<String, Integer> couple : couplesList)
			{
				if (couple.getLeft().equals(joinIdent)
					&& couple.getRight() == 0)
				{
					counter++;
				}
			}

			if (counter >= nbFlows) //You can have more splits in-between, thus more flows...
			{
				return joinIdent;
			}
			else
			{
				return "";
			}
		}
		else
		{
			return "";
		}
	}

	/**
	 * Abstract class for Nodes.
	 * Should not be directly used. Use child classes instead.
	 */
	abstract class Node
	{
		protected final String identifier;
		protected final ArrayList<Flow> incomingFlows;
		protected final ArrayList<Flow> outgoingFlows;
		protected final ArrayList<String> alphabet;

		Node(final String identifier,
			 final ArrayList<Flow> incomingFlows,
			 final ArrayList<Flow> outgoingFlows)
		{
			this.identifier = identifier;
			this.incomingFlows = incomingFlows;
			this.outgoingFlows = outgoingFlows;
			this.alphabet = new ArrayList<>();
		}

		ArrayList<String> alpha()
		{
			return this.alphabet;
		}

		String identifier()
		{
			return this.identifier;
		}

		void addIncomingFlow(final Flow flow)
		{
			this.incomingFlows.add(flow);
		}

		void addOutgoingFlow(final Flow flow)
		{
			this.outgoingFlows.add(flow);
		}

		ArrayList<Flow> incomingFlows()
		{
			return this.incomingFlows;
		}

		ArrayList<Flow> outgoingFlows()
		{
			return this.outgoingFlows;
		}

		Flow firstIncomingFlow()
		{
			return this.incomingFlows.get(0);
		}

		Flow firstOutgoingFlow()
		{
			return this.outgoingFlows.get(0);
		}

		abstract void writeMainLnt(final StringBuilder stringBuilder,
								   final int baseIndent);

		abstract void processLnt(final StringBuilder stringBuilder);

		abstract void writeLnt(final StringBuilder stringBuilder,
							   final int baseIndent);

		abstract ArrayList<Pair<String, Integer>> reachableOrJoin(final ArrayList<Pair<String, Integer>> visited,
																  final int depth);
	}

	/**
	 * Class for Flows.
	 */
	class Flow
	{
		protected final String identifier;
		protected final Node source;
		protected final Node target;

		Flow(final String identifier,
			 final Node source,
			 final Node target)
		{
			this.identifier = identifier;
			this.source = source;
			this.target = target;
		}

		//Generates the (generic) process for flows, only once
		void writeLnt(final StringBuilder stringBuilder)
		{
			stringBuilder.append(Lnt.PROCESS)
					.append(Constant.SPACE)
					.append(Bpmn.FLOW)
					.append(Constant.SPACE)
					.append(Constant.LEFT_SQUARE_BRACKET)
					.append(Bpmn.BEGIN)
					.append(Constant.COMA_AND_SPACE)
					.append(Bpmn.FINISH)
					.append(Constant.COLON_AND_SPACE)
					.append(Lnt.ANY)
					.append(Constant.RIGHT_SQUARE_BRACKET_AND_SPACE)
					.append(Constant.LEFT_PARENTHESIS)
					.append(Bpmn.IDENT_VARIABLE)
					.append(Constant.COLON_AND_SPACE)
					.append(Bpmn.ID_LNT_TYPE)
					.append(Constant.RIGHT_PARENTHESIS_AND_SPACE)
					.append(Lnt.IS)
					.append(Constant.LINE_FEED)
					.append(Utils.indentLNT(1))
					.append(Lnt.LOOP)
					.append(Constant.LINE_FEED)
					.append(Utils.indentLNT(2))
					.append(Bpmn.BEGIN)
					.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
					.append(Bpmn.IDENT_VARIABLE)
					.append(Constant.RIGHT_PARENTHESIS)
					.append(Lnt.SEQUENTIAL_COMPOSITION_OPERATOR)
					.append(Constant.LINE_FEED)
					.append(Utils.indentLNT(2))
					.append(Bpmn.FINISH)
					.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
					.append(Bpmn.IDENT_VARIABLE)
					.append(Constant.RIGHT_PARENTHESIS)
					.append(Constant.LINE_FEED)
					.append(Utils.indentLNT(1))
					.append(Lnt.END_LOOP)
					.append(Constant.LINE_FEED)
					.append(Lnt.END_PROCESS)
					.append(Constant.DOUBLE_LINE_FEED)
					.append(Lnt.STANDARD_SEPARATOR)
					.append(Constant.DOUBLE_LINE_FEED);
		}

		//Returns the source node
		Node getSource()
		{
			return this.source;
		}

		//Returns the target node
		Node getTarget()
		{
			return this.target;
		}

		String identifier()
		{
			return this.identifier;
		}

		void processLnt(final StringBuilder stringBuilder)
		{
			final int argsMinIndent = 21 + 2;
			int nbCharCurrentLine = argsMinIndent + this.identifier.length() + 2;

			stringBuilder.append(Bpmn.FLOW)
					.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
					.append(this.identifier)
					.append(Constant.COMA);

			if (nbCharCurrentLine + this.source.identifier().length() + 2 > Lnt.MAX_CHAR_PER_LINE)
			{
				stringBuilder.append(Constant.LINE_FEED)
						.append(Utils.indent(argsMinIndent));
				nbCharCurrentLine = argsMinIndent + this.source.identifier().length();
			}
			else
			{
				stringBuilder.append(Constant.SPACE);
				nbCharCurrentLine += this.source.identifier().length();
			}

			stringBuilder.append(this.source.identifier())
					.append(Constant.COMA);

			if (nbCharCurrentLine + this.target.identifier().length() + 2 > Lnt.MAX_CHAR_PER_LINE)
			{
				stringBuilder.append(Constant.LINE_FEED)
						.append(Utils.indent(argsMinIndent));
			}
			else
			{
				stringBuilder.append(Constant.SPACE);
			}

			stringBuilder.append(this.target.identifier())
					.append(Constant.RIGHT_PARENTHESIS);
		}
	}

	/**
	 * Class for InitialEvent
	 */
	class InitialEvent extends Node
	{
		InitialEvent(final String identifier,
					 final ArrayList<Flow> incomingFlows,
					 final ArrayList<Flow> outgoingFlows)
		{
			super(identifier, incomingFlows, outgoingFlows);
		}

		@Override
		void writeMainLnt(final StringBuilder stringBuilder,
						  final int baseIndent)
		{
			throw new NotImplementedException("Method \"writeMainLnt()\" should not be used on InitialEvent!");
		}

		//Generates the (generic) process for the initial event, only once
		@Override
		void writeLnt(final StringBuilder stringBuilder,
					  final int baseIndent)
		{
			stringBuilder.append(Lnt.PROCESS)
					.append(Constant.SPACE)
					.append(Bpmn.INITIAL_EVENT)
					.append(Constant.SPACE_AND_LEFT_SQUARE_BRACKET)
					.append(Bpmn.BEGIN)
					.append(Constant.COMA_AND_SPACE)
					.append(Bpmn.OUTGOING_FLOW)
					.append(Constant.COLON_AND_SPACE)
					.append(Lnt.ANY)
					.append(Constant.RIGHT_SQUARE_BRACKET_AND_SPACE)
					.append(Lnt.IS)
					.append(Constant.LINE_FEED)
					.append(Utils.indentLNT(1))
					.append(Lnt.VAR)
					.append(Constant.SPACE)
					.append(Bpmn.IDENT_VARIABLE)
					.append(Constant.COLON_AND_SPACE)
					.append(Bpmn.ID_LNT_TYPE)
					.append(Constant.SPACE)
					.append(Lnt.IN)
					.append(Constant.LINE_FEED)
					.append(Utils.indentLNT(2))
					.append(Bpmn.BEGIN)
					.append(Lnt.SEQUENTIAL_COMPOSITION_OPERATOR)
					.append(Constant.LINE_FEED)
					.append(Utils.indentLNT(2))
					.append(Bpmn.OUTGOING_FLOW)
					.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
					.append(Lnt.OUT_PARAMETER)
					.append(Bpmn.IDENT_VARIABLE)
					.append(Constant.SPACE)
					.append(Lnt.OF)
					.append(Constant.SPACE)
					.append(Bpmn.ID_LNT_TYPE)
					.append(Constant.RIGHT_PARENTHESIS)
					.append(Constant.LINE_FEED)
					.append(Utils.indentLNT(1))
					.append(Lnt.END_VAR)
					.append(Constant.LINE_FEED)
					.append(Lnt.END_PROCESS)
					.append(Constant.DOUBLE_LINE_FEED)
					.append(Lnt.STANDARD_SEPARATOR)
					.append(Constant.DOUBLE_LINE_FEED);
		}

		/**
		 * Seeks or joins, for an initial event, just a recursive call on the target node of the outgoing flow.
		 * Returns the list of reachable or joins.
		 *
		 * @param visited the list of visited elements
		 * @param depth   the current depth
		 * @return the list of reachable or joins
		 */
		@Override
		ArrayList<Pair<String, Integer>> reachableOrJoin(final ArrayList<Pair<String, Integer>> visited,
														 final int depth)
		{
			final ArrayList<Pair<String, Integer>> newVisited = new ArrayList<>(visited);
			newVisited.add(Pair.of(this.identifier, depth));

			return this.outgoingFlows.get(0).getTarget().reachableOrJoin(newVisited, depth);
		}

		@Override
		void processLnt(final StringBuilder stringBuilder)
		{
			stringBuilder
					.append(Bpmn.INITIAL)
					.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
					.append(this.identifier)
					.append(Constant.COMA_AND_SPACE);

			if (21 + this.identifier.length() + Bpmn.INITIAL.length() + 5 +
				this.outgoingFlows.get(0).identifier().length() > Lnt.MAX_CHAR_PER_LINE)
			{
				stringBuilder.append(Constant.LINE_FEED)
						.append(Utils.indent(23 + Bpmn.INITIAL.length()));
			}

			stringBuilder.append(this.outgoingFlows.get(0).identifier())
					.append(Constant.RIGHT_PARENTHESIS);
		}
	}

	/**
	 * Class for End Event
	 */
	class EndEvent extends Node
	{
		EndEvent(final String identifier,
				 final ArrayList<Flow> incomingFlows,
				 final ArrayList<Flow> outgoingFlows)
		{
			super(identifier, incomingFlows, outgoingFlows);
		}

		@Override
		void writeMainLnt(final StringBuilder stringBuilder,
						  final int baseIndent)
		{
			throw new NotImplementedException("Method \"writeMainLnt()\" should not be used on EndEvent!");
		}

		//Generates the (generic) process for final events, only once
		@Override
		void writeLnt(final StringBuilder stringBuilder,
					  final int baseIndent)
		{
			stringBuilder.append(Lnt.PROCESS)
					.append(Constant.SPACE)
					.append(Bpmn.END_EVENT)
					.append(Constant.SPACE_AND_LEFT_SQUARE_BRACKET)
					.append(Bpmn.INCOMING_FLOW_VARIABLE)
					.append(Constant.COMA_AND_SPACE)
					.append(Bpmn.FINISH)
					.append(Constant.COLON_AND_SPACE)
					.append(Lnt.ANY)
					.append(Constant.RIGHT_SQUARE_BRACKET_AND_SPACE)
					.append(Lnt.IS)
					.append(Constant.LINE_FEED)
					.append(Utils.indentLNT(1));

			if (isBalanced)
			{
				stringBuilder.append(Lnt.VAR)
						.append(Constant.SPACE)
						.append(Bpmn.IDENT_VARIABLE)
						.append(Constant.COLON_AND_SPACE)
						.append(Bpmn.ID_LNT_TYPE)
						.append(Constant.SPACE)
						.append(Lnt.IN)
						.append(Constant.LINE_FEED)
						.append(Utils.indentLNT(2))
						.append(Bpmn.INCOMING_FLOW_VARIABLE)
						.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
						.append(Lnt.OUT_PARAMETER)
						.append(Bpmn.IDENT_VARIABLE)
						.append(Constant.SPACE)
						.append(Lnt.OF)
						.append(Constant.SPACE)
						.append(Bpmn.ID_LNT_TYPE)
						.append(Constant.RIGHT_PARENTHESIS)
						.append(Lnt.SEQUENTIAL_COMPOSITION_OPERATOR)
						.append(Constant.LINE_FEED)
						.append(Utils.indentLNT(2))
						.append(Bpmn.FINISH)
						.append(Constant.LINE_FEED)
						.append(Utils.indentLNT(1))
						.append(Lnt.END_VAR)
						.append(Constant.LINE_FEED);
			}
			else
			{
				stringBuilder.append(Lnt.VAR)
						.append(Constant.SPACE)
						.append(Bpmn.IDENT_VARIABLE)
						.append(Constant.COLON_AND_SPACE)
						.append(Bpmn.ID_LNT_TYPE)
						.append(Constant.SPACE)
						.append(Lnt.IN)
						.append(Constant.LINE_FEED)
						.append(Utils.indentLNT(2))
						.append(Lnt.LOOP)
						.append(Constant.LINE_FEED)
						.append(Utils.indentLNT(3))
						.append(Bpmn.INCOMING_FLOW_VARIABLE)
						.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
						.append(Lnt.OUT_PARAMETER)
						.append(Bpmn.IDENT_VARIABLE)
						.append(Lnt.SPACED_OF)
						.append(Bpmn.ID_LNT_TYPE)
						.append(Constant.RIGHT_PARENTHESIS)
						.append(Lnt.SEQUENTIAL_COMPOSITION_OPERATOR_AND_SPACE)
						.append(Bpmn.FINISH)
						.append(Constant.LINE_FEED)
						.append(Utils.indentLNT(2))
						.append(Lnt.END_LOOP)
						.append(Constant.LINE_FEED)
						.append(Utils.indentLNT(1))
						.append(Lnt.END_VAR)
						.append(Constant.LINE_FEED);
			}

			stringBuilder.append(Lnt.END_PROCESS)
					.append(Constant.DOUBLE_LINE_FEED)
					.append(Lnt.STANDARD_SEPARATOR)
					.append(Constant.DOUBLE_LINE_FEED);
		}

		/**
		 * Seeks an or join, for an initial event, just a recursive call on the target node of the outgoing flow
		 *
		 * @param visited the list of visited nodes
		 * @param depth the current depth
		 * @return the list of reachable or joins if any
		 */
		@Override
		ArrayList<Pair<String, Integer>> reachableOrJoin(final ArrayList<Pair<String, Integer>> visited,
														 final int depth)
		{
			return new ArrayList<>();
		}

		@Override
		void processLnt(final StringBuilder stringBuilder)
		{
			final int argsIndent = Bpmn.END_EVENT.length() + Constant.SPACE_AND_LEFT_PARENTHESIS.length();
			final int flowMinIndent;
			int nbCharCurrentLine;

			stringBuilder.append(Bpmn.END_EVENT)
					.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
					.append(this.identifier)
					.append(Constant.COMA);

			if (this.incomingFlows.isEmpty())
			{
				throw new IllegalStateException("End event \"" + this.identifier + "\" has no incoming flow!");
			}

			final Flow firstFlow = this.incomingFlows.remove(0);

			if (firstFlow.identifier().length() + 21 + argsIndent + this.identifier.length() + 4 > Lnt.MAX_CHAR_PER_LINE)
			{
				stringBuilder.append(Constant.LINE_FEED)
						.append(Utils.indent(21 + argsIndent));
				nbCharCurrentLine = 21 + argsIndent;
			}
			else
			{
				stringBuilder.append(Constant.SPACE);
				nbCharCurrentLine = 21 + argsIndent + this.identifier.length() + 2;
			}

			flowMinIndent = nbCharCurrentLine + 1;
			stringBuilder.append(Constant.LEFT_CURVY_BRACKET)
					.append(firstFlow.identifier());
			nbCharCurrentLine += firstFlow.identifier().length() + 1;

			for (Flow inFlow : this.incomingFlows)
			{
				stringBuilder.append(Constant.COMA_AND_SPACE);
				nbCharCurrentLine += 2;

				if (nbCharCurrentLine + inFlow.identifier().length() + 2 > Lnt.MAX_CHAR_PER_LINE)
				{
					stringBuilder.append(Constant.LINE_FEED)
							.append(Utils.indent(flowMinIndent));
					nbCharCurrentLine = flowMinIndent + inFlow.identifier().length() + 2;
				}
				else
				{
					nbCharCurrentLine += inFlow.identifier().length() + 2;
				}

				stringBuilder.append(inFlow.identifier());
			}

			stringBuilder.append(Constant.RIGHT_CURVY_BRACKET)
					.append(Constant.RIGHT_PARENTHESIS);

			this.incomingFlows.add(0, firstFlow);
		}
	}

	/**
	 * Abstract class for Communication
	 */
	abstract class Communication extends Node
	{
		protected final String message;

		Communication(final String identifier,
					  final ArrayList<Flow> incomingFlows,
					  final ArrayList<Flow> outgoingFlows,
					  final String message)
		{
			super(identifier, incomingFlows, outgoingFlows);
			this.message = message;
		}

		@Override
		ArrayList<Pair<String, Integer>> reachableOrJoin(final ArrayList<Pair<String, Integer>> visited,
														 final int depth)
		{
			if (pairListContainsIdentifier(visited, this.identifier))
			{
				return new ArrayList<>();
			}
			else
			{
				final ArrayList<Pair<String, Integer>> newVisited = new ArrayList<>(visited);
				newVisited.add(Pair.of(this.identifier, depth));

				return this.outgoingFlows.get(0).getTarget().reachableOrJoin(newVisited, depth);
			}
		}
	}

	/**
	 * Class for Interaction
	 */
	class Interaction extends Communication
	{
		private final String sender;
		private final ArrayList<String> receivers;

		Interaction(final String identifier,
					final ArrayList<Flow> incomingFlows,
					final ArrayList<Flow> outgoingFlows,
					final String message,
					final String sender,
					final ArrayList<String> receivers)
		{
			super(identifier, incomingFlows, outgoingFlows, message);
			this.sender = sender;
			this.receivers = receivers;
		}

		//Generates the (generic) process for interactions, only once
		@Override
		void writeLnt(final StringBuilder stringBuilder,
					  final int baseIndent)
		{
			stringBuilder.append(Lnt.PROCESS)
					.append(Constant.SPACE)
					.append(Bpmn.INTERACTION)
					.append(Constant.SPACE_AND_LEFT_SQUARE_BRACKET)
					.append(Bpmn.INCOMING_FLOW_VARIABLE)
					.append(Constant.COMA_AND_SPACE)
					.append(Bpmn.INTER)
					.append(Constant.COMA_AND_SPACE)
					.append(Bpmn.OUTGOING_FLOW)
					.append(Constant.COLON_AND_SPACE)
					.append(Lnt.ANY)
					.append(Constant.RIGHT_SQUARE_BRACKET_AND_SPACE)
					.append(Lnt.IS)
					.append(Constant.LINE_FEED)
					.append(Lnt.VAR)
					.append(Constant.SPACE)
					.append(Bpmn.IDENT_VARIABLE)
					.append(Constant.COLON_AND_SPACE)
					.append(Bpmn.ID_LNT_TYPE)
					.append(Constant.SPACE)
					.append(Lnt.IN)
					.append(Constant.LINE_FEED)
					.append(Lnt.LOOP)
					.append(Constant.LINE_FEED)
					.append(Bpmn.INCOMING_FLOW_VARIABLE)
					.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
					.append(Lnt.OUT_PARAMETER)
					.append(Bpmn.IDENT_VARIABLE)
					.append(Lnt.SPACED_OF)
					.append(Bpmn.ID_LNT_TYPE)
					.append(Constant.RIGHT_PARENTHESIS)
					.append(Lnt.SEQUENTIAL_COMPOSITION_OPERATOR_AND_SPACE)
					.append(Bpmn.INTER)
					.append(Lnt.SEQUENTIAL_COMPOSITION_OPERATOR_AND_SPACE)
					.append(Bpmn.OUTGOING_FLOW)
					.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
					.append(Lnt.OUT_PARAMETER)
					.append(Bpmn.IDENT_VARIABLE)
					.append(Lnt.SPACED_OF)
					.append(Bpmn.ID_LNT_TYPE)
					.append(Constant.RIGHT_PARENTHESIS)
					.append(Constant.LINE_FEED)
					.append(Lnt.END_LOOP)
					.append(Constant.LINE_FEED)
					.append(Lnt.END_VAR)
					.append(Constant.LINE_FEED)
					.append(Lnt.END_PROCESS)
					.append(Constant.LINE_FEED);
		}

		@Override
		void processLnt(final StringBuilder stringBuilder)
		{
			//TODO Vérifier
			this.writeLnt(stringBuilder, 21);
		}

		/**
		 * Computes alphabet for an interaction.
		 *
		 * @return the alphabet
		 */
		ArrayList<String> alpha()
		{
			final ArrayList<String> alphabet = new ArrayList<>();
			final StringBuilder res = new StringBuilder(this.sender);
			res.append(Constant.UNDERSCORE);

			for (String e : this.receivers)
			{
				res.append(e)
					.append(Constant.UNDERSCORE);
			}

			res.append(this.message);
			alphabet.add(res.toString());

			return alphabet;
		}

		/**
		 * Generates process instantiation for main LNT process
		 */
		void writeMainLnt(final StringBuilder stringBuilder,
						  final int baseIndent)
		{
			//We assume one incoming flow and one outgoing flow
			stringBuilder.append(Bpmn.INTERACTION)
					.append(Constant.SPACE_AND_LEFT_SQUARE_BRACKET)
					.append(this.incomingFlows.get(0).identifier())
					.append(Constant.UNDERSCORE)
					.append(Bpmn.FINISH)
					.append(Constant.COMA_AND_SPACE)
					.append(this.sender)
					.append(Constant.UNDERSCORE);

			for (String e : this.receivers)
			{
				stringBuilder.append(e)
						.append(Constant.UNDERSCORE);
			}

			stringBuilder.append(this.message)
					.append(Constant.COMA)
					.append(this.outgoingFlows.get(0).identifier())
					.append(Constant.UNDERSCORE)
					.append(Bpmn.BEGIN)
					.append(Constant.RIGHT_SQUARE_BRACKET);
		}
	}

	/**
	 * Abstract class for MessageCommunication
	 */
	abstract class MessageCommunication extends Communication
	{
		MessageCommunication(final String identifier,
							 final ArrayList<Flow> incomingFlows,
							 final ArrayList<Flow> outgoingFlows,
							 final String message)
		{
			super(identifier, incomingFlows, outgoingFlows, message);
		}
	}

	/**
	 * Class for MessageSending
	 */
	class MessageSending extends MessageCommunication
	{
		MessageSending(final String identifier,
					   final ArrayList<Flow> incomingFlows,
					   final ArrayList<Flow> outgoingFlows,
					   final String message)
		{
			super(identifier, incomingFlows, outgoingFlows, message);
		}

		@Override
		void writeLnt(final StringBuilder stringBuilder,
					  final int baseIndent)
		{
			stringBuilder.append(Lnt.PROCESS)
					.append(Constant.SPACE)
					.append(Bpmn.MESSAGE_SENDING)
					.append(Constant.SPACE_AND_LEFT_SQUARE_BRACKET)
					.append(Bpmn.INCOMING_FLOW_VARIABLE)
					.append(Constant.COMA_AND_SPACE)
					.append(Bpmn.MESSAGE)
					.append(Constant.COMA_AND_SPACE)
					.append(Bpmn.OUTGOING_FLOW)
					.append(Constant.COLON_AND_SPACE)
					.append(Lnt.ANY)
					.append(Constant.RIGHT_SQUARE_BRACKET_AND_SPACE)
					.append(Lnt.IS)
					.append(Constant.LINE_FEED)
					.append(Lnt.VAR)
					.append(Constant.SPACE)
					.append(Bpmn.IDENT_VARIABLE)
					.append(Constant.COLON_AND_SPACE)
					.append(Bpmn.ID_LNT_TYPE)
					.append(Lnt.SPACED_IN)
					.append(Lnt.LOOP)
					.append(Constant.LINE_FEED)
					.append(Bpmn.INCOMING_FLOW_VARIABLE)
					.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
					.append(Lnt.OUT_PARAMETER)
					.append(Bpmn.IDENT_VARIABLE)
					.append(Lnt.SPACED_OF)
					.append(Bpmn.ID_LNT_TYPE)
					.append(Constant.RIGHT_PARENTHESIS)
					.append(Lnt.SEQUENTIAL_COMPOSITION_OPERATOR_AND_SPACE)
					.append(Bpmn.MESSAGE)
					.append(Lnt.SEQUENTIAL_COMPOSITION_OPERATOR_AND_SPACE)
					.append(Bpmn.OUTGOING_FLOW)
					.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
					.append(Lnt.OUT_PARAMETER)
					.append(Bpmn.IDENT_VARIABLE)
					.append(Lnt.SPACED_OF)
					.append(Bpmn.ID_LNT_TYPE)
					.append(Constant.RIGHT_PARENTHESIS)
					.append(Constant.LINE_FEED)
					.append(Lnt.END_LOOP)
					.append(Constant.LINE_FEED)
					.append(Lnt.END_VAR)
					.append(Constant.LINE_FEED)
					.append(Lnt.END_PROCESS)
					.append(Constant.LINE_FEED)
			;
		}

		@Override
		void processLnt(final StringBuilder stringBuilder)
		{
			this.writeLnt(stringBuilder, 21); //TODO vérifier
		}

		ArrayList<String> alpha()
		{
			final ArrayList<String> res = new ArrayList<>();
			res.add(this.message + Constant.UNDERSCORE + Bpmn.EMITTED_MESSAGE_SUFFIX);
			return res;
		}

		void writeMainLnt(final StringBuilder stringBuilder,
						  final int baseIndent)
		{
			stringBuilder.append(Bpmn.MESSAGE_SENDING)
					.append(Constant.SPACE_AND_LEFT_SQUARE_BRACKET)
					.append(this.incomingFlows.get(0).identifier())
					.append(Constant.UNDERSCORE)
					.append(Bpmn.FINISH)
					.append(Constant.COMA_AND_SPACE)
					.append(this.message)
					.append(Constant.UNDERSCORE)
					.append(Bpmn.EMITTED_MESSAGE_SUFFIX)
					.append(Constant.COMA_AND_SPACE)
					.append(this.outgoingFlows.get(0).identifier())
					.append(Constant.UNDERSCORE)
					.append(Bpmn.BEGIN)
					.append(Constant.RIGHT_SQUARE_BRACKET);
		}
	}

	/**
	 * Class for MessageReception
	 */
	class MessageReception extends MessageCommunication
	{
		MessageReception(final String identifier,
						 final ArrayList<Flow> incomingFlows,
						 final ArrayList<Flow> outgoingFlows,
						 final String message)
		{
			super(identifier, incomingFlows, outgoingFlows, message);
		}

		@Override
		void writeLnt(final StringBuilder stringBuilder,
					  final int baseIndent)
		{
			stringBuilder.append(Lnt.PROCESS)
					.append(Constant.SPACE)
					.append(Bpmn.MESSAGE_RECEPTION)
					.append(Constant.SPACE_AND_LEFT_SQUARE_BRACKET)
					.append(Bpmn.INCOMING_FLOW_VARIABLE)
					.append(Constant.COMA_AND_SPACE)
					.append(Bpmn.MESSAGE)
					.append(Constant.COMA_AND_SPACE)
					.append(Bpmn.OUTGOING_FLOW)
					.append(Constant.COLON_AND_SPACE)
					.append(Lnt.ANY)
					.append(Constant.RIGHT_SQUARE_BRACKET_AND_SPACE)
					.append(Lnt.IS)
					.append(Constant.LINE_FEED)
					.append(Lnt.VAR)
					.append(Constant.SPACE)
					.append(Bpmn.IDENT_VARIABLE)
					.append(Constant.COLON_AND_SPACE)
					.append(Bpmn.ID_LNT_TYPE)
					.append(Lnt.SPACED_IN)
					.append(Lnt.LOOP)
					.append(Constant.LINE_FEED)
					.append(Bpmn.INCOMING_FLOW_VARIABLE)
					.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
					.append(Lnt.OUT_PARAMETER)
					.append(Bpmn.IDENT_VARIABLE)
					.append(Lnt.SPACED_OF)
					.append(Bpmn.ID_LNT_TYPE)
					.append(Constant.RIGHT_PARENTHESIS)
					.append(Lnt.SEQUENTIAL_COMPOSITION_OPERATOR_AND_SPACE)
					.append(Bpmn.MESSAGE)
					.append(Lnt.SEQUENTIAL_COMPOSITION_OPERATOR_AND_SPACE)
					.append(Bpmn.OUTGOING_FLOW)
					.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
					.append(Lnt.OUT_PARAMETER)
					.append(Bpmn.IDENT_VARIABLE)
					.append(Lnt.SPACED_OF)
					.append(Bpmn.ID_LNT_TYPE)
					.append(Constant.RIGHT_PARENTHESIS)
					.append(Constant.LINE_FEED)
					.append(Lnt.END_LOOP)
					.append(Constant.LINE_FEED)
					.append(Lnt.END_VAR)
					.append(Constant.LINE_FEED)
					.append(Lnt.END_PROCESS)
					.append(Constant.LINE_FEED);
		}

		@Override
		void processLnt(final StringBuilder stringBuilder)
		{
			this.writeLnt(stringBuilder, 21); //TODO Vérifier
		}

		ArrayList<String> alpha()
		{
			final ArrayList<String> result = new ArrayList<>();
			result.add(this.message + Constant.UNDERSCORE + Bpmn.RECEIVED_MESSAGE_SUFFIX);
			return result;
		}

		void writeMainLnt(final StringBuilder stringBuilder, final int baseIndent)
		{
			stringBuilder.append(Bpmn.MESSAGE_RECEPTION)
					.append(Constant.SPACE_AND_LEFT_SQUARE_BRACKET)
					.append(this.incomingFlows.get(0).identifier())
					.append(Constant.UNDERSCORE)
					.append(Bpmn.FINISH)
					.append(this.message)
					.append(Constant.UNDERSCORE)
					.append(Bpmn.RECEIVED_MESSAGE_SUFFIX)
					.append(this.outgoingFlows.get(0).identifier())
					.append(Constant.UNDERSCORE)
					.append(Bpmn.BEGIN)
					.append(Constant.RIGHT_SQUARE_BRACKET);
		}
	}

	/**
	 * Class for Task
	 */
	class Task extends Node
	{
		Task(final String identifier,
			 final ArrayList<Flow> incomingFlows,
			 final ArrayList<Flow> outgoingFlows)
		{
			super(identifier, incomingFlows, outgoingFlows);
		}

		@Override
		void processLnt(final StringBuilder stringBuilder)
		{
			final int argsMinIndent = 21 + 6;
			final int incFlowsMinIndent;
			final int outFlowMinIndent;

			stringBuilder.append(Bpmn.TASK)
					.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
					.append(this.identifier)
					.append(Constant.COMA);

			int nbCharCurrentLine = argsMinIndent + this.identifier.length() + 1;

			if (this.incomingFlows.isEmpty())
			{
				throw new IllegalStateException("Task \"" + this.identifier + "\" has no incoming flows!");
			}

			final Flow firstIncFlow = this.incomingFlows.remove(0);

			if (nbCharCurrentLine + firstIncFlow.identifier().length() + 3 > Lnt.MAX_CHAR_PER_LINE)
			{
				stringBuilder.append(Constant.LINE_FEED)
						.append(Utils.indent(argsMinIndent));
				nbCharCurrentLine = argsMinIndent + firstIncFlow.identifier().length() + 1;
				incFlowsMinIndent = argsMinIndent + 1;
			}
			else
			{
				stringBuilder.append(Constant.SPACE);
				incFlowsMinIndent = nbCharCurrentLine + 2;
				nbCharCurrentLine += firstIncFlow.identifier().length() + 2;
			}

			stringBuilder.append(Constant.LEFT_CURVY_BRACKET)
					.append(firstIncFlow.identifier());

			for (Flow inFlow : this.incomingFlows)
			{
				stringBuilder.append(Constant.COMA_AND_SPACE);
				nbCharCurrentLine += 2;

				if (nbCharCurrentLine + inFlow.identifier().length() > Lnt.MAX_CHAR_PER_LINE)
				{
					stringBuilder.append(Constant.LINE_FEED)
							.append(Utils.indent(incFlowsMinIndent));
					nbCharCurrentLine = incFlowsMinIndent + inFlow.identifier().length();
				}

				stringBuilder.append(inFlow.identifier());
			}

			stringBuilder.append(Constant.RIGHT_CURVY_BRACKET)
					.append(Constant.COMA);
			nbCharCurrentLine += 2;

			if (this.outgoingFlows.isEmpty())
			{
				throw new IllegalStateException("Task \"" + this.identifier + "\" has no outgoing flows!");
			}

			final Flow firstOutFlow = this.outgoingFlows.remove(0);

			if (nbCharCurrentLine + firstOutFlow.identifier().length() + 3 > Lnt.MAX_CHAR_PER_LINE)
			{
				stringBuilder.append(Constant.LINE_FEED)
						.append(Utils.indent(argsMinIndent));
				nbCharCurrentLine = argsMinIndent + firstOutFlow.identifier().length() + 1;
				outFlowMinIndent = argsMinIndent + 1;
			}
			else
			{
				stringBuilder.append(Constant.SPACE);
				outFlowMinIndent = nbCharCurrentLine + 2;
				nbCharCurrentLine += firstIncFlow.identifier().length() + 2;
			}

			stringBuilder.append(Constant.LEFT_CURVY_BRACKET)
					.append(firstOutFlow.identifier());

			for (Flow outFlow : this.outgoingFlows)
			{
				stringBuilder.append(Constant.SPACE);
				nbCharCurrentLine += 2;

				if (nbCharCurrentLine + outFlow.identifier().length() > Lnt.MAX_CHAR_PER_LINE)
				{
					stringBuilder.append(Constant.LINE_FEED)
							.append(Utils.indent(outFlowMinIndent));
					nbCharCurrentLine = outFlowMinIndent + outFlow.identifier().length();
				}

				stringBuilder.append(outFlow.identifier());
			}

			stringBuilder.append(Constant.RIGHT_CURVY_BRACKET)
					.append(Constant.RIGHT_PARENTHESIS);

			this.incomingFlows.add(0, firstIncFlow);
			this.outgoingFlows.add(0, firstOutFlow);
		}

		@Override
		void writeLnt(final StringBuilder stringBuilder,
					  final int baseIndent)
		{
			final int nbInc = this.incomingFlows.size();
			final int nbOut = this.outgoingFlows.size();
			final String processPreamble = Lnt.PROCESS + Constant.SPACE + Bpmn.TASK + Constant.UNDERSCORE +
					nbInc + Constant.UNDERSCORE + nbOut + Constant.SPACE + Constant.LEFT_SQUARE_BRACKET;
			boolean lineJumped = false;
			stringBuilder.append(processPreamble);

			int nbCharCurrentLine = processPreamble.length();

			if (nbInc == 1)
			{
				stringBuilder.append(Bpmn.INCOMING_FLOW_VARIABLE)
						.append(Constant.COMA_AND_SPACE);

				nbCharCurrentLine = processPreamble.length() + 6;
			}
			else
			{
				int incCounter = 0;

				while (incCounter < nbInc)
				{
					final String flowId = Bpmn.INCOMING_FLOW_VARIABLE + incCounter;

					if (nbCharCurrentLine + flowId.length() + 1 > Lnt.MAX_CHAR_PER_LINE)
					{
						lineJumped = true;
						stringBuilder.append(Constant.LINE_FEED)
								.append(Utils.indent(Constant.PROCESS_INDENT_LENGTH));
						nbCharCurrentLine = Constant.PROCESS_INDENT_LENGTH + flowId.length() + 2;
					}
					else
					{
						nbCharCurrentLine += flowId.length() + 2;
					}

					stringBuilder.append(flowId)
							.append(Constant.COMA_AND_SPACE);
					incCounter++;
				}
			}

			if (nbCharCurrentLine + 6 > Lnt.MAX_CHAR_PER_LINE)
			{
				lineJumped = true;
				stringBuilder.append(Constant.LINE_FEED)
						.append(Utils.indent(Constant.PROCESS_INDENT_LENGTH));
				nbCharCurrentLine = Constant.PROCESS_INDENT_LENGTH + 6;
			}
			else
			{
				nbCharCurrentLine += 6;
			}

			stringBuilder.append(Bpmn.TASK)
					.append(Constant.COMA_AND_SPACE);

			if (nbOut == 1)
			{
				if (nbCharCurrentLine + 9 > Lnt.MAX_CHAR_PER_LINE)
				{
					lineJumped = true;
					stringBuilder.append(Constant.LINE_FEED)
							.append(Utils.indent(Constant.PROCESS_INDENT_LENGTH));
				}

				stringBuilder.append(Bpmn.OUTGOING_FLOW);
			}
			else
			{
				int outCounter = 0;

				while (outCounter < nbOut)
				{
					final String flowId = Bpmn.OUTGOING_FLOW + outCounter;

					if (nbCharCurrentLine + flowId.length() + 1 > Lnt.MAX_CHAR_PER_LINE)
					{
						lineJumped = true;
						stringBuilder.append(Constant.LINE_FEED)
								.append(Utils.indent(Constant.PROCESS_INDENT_LENGTH));
						nbCharCurrentLine = Constant.PROCESS_INDENT_LENGTH + flowId.length() + 2;
					}
					else
					{
						nbCharCurrentLine += flowId.length() + 2;
					}

					stringBuilder.append(flowId);
					outCounter++;

					if (outCounter < nbOut)
					{
						stringBuilder.append(Constant.COMA_AND_SPACE);
						nbCharCurrentLine += 2;
					}
				}
			}

			stringBuilder.append(Constant.COLON_AND_SPACE)
					.append(Lnt.ANY)
					.append(Constant.RIGHT_SQUARE_BRACKET)
					.append(lineJumped ? Constant.LINE_FEED : Constant.SPACE)
					.append(Lnt.IS)
					.append(Constant.LINE_FEED)
					.append(Utils.indentLNT(1))
					.append(Lnt.VAR)
					.append(Constant.SPACE)
					.append(Bpmn.IDENT_VARIABLE)
					.append(Constant.COLON_AND_SPACE)
					.append(Bpmn.ID_LNT_TYPE)
					.append(Constant.SPACE)
					.append(Lnt.IN)
					.append(Constant.LINE_FEED)
					.append(Utils.indentLNT(2))
					.append(Lnt.LOOP)
					.append(Constant.LINE_FEED)
					.append(Utils.indentLNT(3));

			if (nbInc == 1)
			{
				final String flowId = Bpmn.INCOMING_FLOW_VARIABLE + Constant.SPACE_AND_LEFT_PARENTHESIS +
						Lnt.OUT_PARAMETER + Bpmn.IDENT_VARIABLE + Lnt.SPACED_OF + Bpmn.ID_LNT_TYPE +
						Constant.RIGHT_PARENTHESIS + Lnt.SEQUENTIAL_COMPOSITION_OPERATOR;

				stringBuilder.append(flowId);
			}
			else
			{
				int incCounter = 0;
				stringBuilder.append(Utils.indentLNT(3))
						.append(Lnt.ALT)
						.append(Constant.LINE_FEED)
						.append(Utils.indentLNT(4));

				while (incCounter < nbInc)
				{
					stringBuilder.append(Bpmn.INCOMING_FLOW_VARIABLE)
							.append(incCounter)
							.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
							.append(Lnt.OUT_PARAMETER)
							.append(Bpmn.IDENT_VARIABLE)
							.append(Lnt.OF)
							.append(Bpmn.ID_LNT_TYPE)
							.append(Constant.RIGHT_PARENTHESIS);

					incCounter++;

					if (incCounter < nbInc)
					{
						stringBuilder.append(Constant.LINE_FEED)
								.append(Utils.indentLNT(3))
								.append(Lnt.ALT_OPERATOR)
								.append(Constant.LINE_FEED)
								.append(Utils.indentLNT(4));
					}
				}

				stringBuilder.append(Constant.LINE_FEED)
						.append(Lnt.END_ALT)
						.append(Lnt.SEQUENTIAL_COMPOSITION_OPERATOR);
			}

			stringBuilder.append(Constant.LINE_FEED)
					.append(Utils.indentLNT(3))
					.append(Bpmn.TASK)
					.append(Lnt.SEQUENTIAL_COMPOSITION_OPERATOR)
					.append(Constant.LINE_FEED)
					.append(Utils.indentLNT(3));

			if (nbOut == 1)
			{
				stringBuilder.append(Bpmn.OUTGOING_FLOW)
						.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
						.append(Lnt.OUT_PARAMETER)
						.append(Bpmn.IDENT_VARIABLE)
						.append(Lnt.SPACED_OF)
						.append(Bpmn.ID_LNT_TYPE)
						.append(Constant.RIGHT_PARENTHESIS)
						.append(Constant.LINE_FEED);
			}
			else
			{
				int outCounter = 0;

				stringBuilder.append(Constant.LINE_FEED)
						.append(Utils.indentLNT(3))
						.append(Lnt.ALT)
						.append(Constant.LINE_FEED);

				while (outCounter < nbOut)
				{
					stringBuilder.append(Utils.indentLNT(4))
							.append(Bpmn.OUTGOING_FLOW)
							.append(outCounter)
							.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
							.append(Lnt.OUT_PARAMETER)
							.append(Bpmn.IDENT_VARIABLE)
							.append(Lnt.SPACED_OF)
							.append(Bpmn.ID_LNT_TYPE)
							.append(Constant.RIGHT_PARENTHESIS);

					outCounter++;

					if (outCounter < nbOut)
					{
						stringBuilder.append(Constant.LINE_FEED)
								.append(Utils.indentLNT(3))
								.append(Lnt.ALT_OPERATOR)
								.append(Constant.LINE_FEED)
								.append(Utils.indentLNT(4));
					}
				}

				stringBuilder.append(Constant.LINE_FEED)
						.append(Utils.indentLNT(3))
						.append(Lnt.END_ALT)
						.append(Constant.LINE_FEED);
			}

			stringBuilder.append(Utils.indentLNT(2))
					.append(Lnt.END_LOOP)
					.append(Constant.LINE_FEED)
					.append(Utils.indentLNT(1))
					.append(Lnt.END_VAR)
					.append(Constant.LINE_FEED)
					.append(Lnt.END_PROCESS)
					.append(Constant.DOUBLE_LINE_FEED)
					.append(Lnt.STANDARD_SEPARATOR)
					.append(Constant.DOUBLE_LINE_FEED);
		}

		@Override
		ArrayList<Pair<String, Integer>> reachableOrJoin(final ArrayList<Pair<String, Integer>> visited,
														 final int depth)
		{
			if (pairListContainsIdentifier(visited, this.identifier))
			{
				return new ArrayList<>();
			}
			else
			{
				if (this.outgoingFlows.size() == 1)
				{
					final ArrayList<Pair<String, Integer>> newVisited = new ArrayList<>(visited);
					newVisited.add(Pair.of(this.identifier, depth));
					return this.outgoingFlows.get(0).getTarget().reachableOrJoin(newVisited, depth);
				}
				else
				{
					final ArrayList<Pair<String, Integer>> res = new ArrayList<>();

					for (Flow f : this.outgoingFlows)
					{
						final ArrayList<Pair<String, Integer>> newVisited = new ArrayList<>(visited);
						newVisited.add(Pair.of(this.identifier, depth));
						res.addAll(f.getTarget().reachableOrJoin(newVisited, depth));
					}

					return res;
				}
			}
		}

		ArrayList<String> alpha()
		{
			final ArrayList<String> res = new ArrayList<>();
			res.add(this.identifier);
			return res;
		}

		@Override
		void writeMainLnt(final StringBuilder stringBuilder,
						  final int baseIndent)
		{
			final int nbInc = this.incomingFlows.size();
			final int nbOut = this.outgoingFlows.size();
			final String taskIdentifier = Bpmn.TASK + Constant.UNDERSCORE + nbInc + Constant.UNDERSCORE +
					nbOut + Constant.SPACE + Constant.LEFT_SQUARE_BRACKET;
			stringBuilder.append(taskIdentifier);
			int nbCharCurrentLine = baseIndent + taskIdentifier.length();

			int incCounter = 0;

			while (incCounter < nbInc)
			{
				final String incFlowIdentifier = this.incomingFlows.get(incCounter).identifier() + Constant.UNDERSCORE +
						Bpmn.FINISH + Constant.COMA_AND_SPACE;

				if (incCounter == 0)
				{
					nbCharCurrentLine += incFlowIdentifier.length();
				}
				else
				{
					if (nbCharCurrentLine + incFlowIdentifier.length() > Lnt.MAX_CHAR_PER_LINE)
					{
						stringBuilder.append(Constant.LINE_FEED)
								.append(Utils.indent(baseIndent + taskIdentifier.length()));
						nbCharCurrentLine = baseIndent + taskIdentifier.length() + incFlowIdentifier.length();
					}
					else
					{
						nbCharCurrentLine += incFlowIdentifier.length();
					}
				}

				stringBuilder.append(incFlowIdentifier);
				incCounter++;
			}

			if (nbCharCurrentLine + this.identifier.length() + 1 > Lnt.MAX_CHAR_PER_LINE)
			{
				stringBuilder.append(Constant.LINE_FEED)
						.append(Utils.indent(baseIndent + taskIdentifier.length()));
				nbCharCurrentLine = baseIndent + taskIdentifier.length() + this.identifier.length() + 2;
			}
			else
			{
				nbCharCurrentLine += this.identifier.length() + 2;
			}

			stringBuilder.append(this.identifier);
			stringBuilder.append(Constant.COMA_AND_SPACE);

			int outCounter = 0;

			while (outCounter < nbOut)
			{
				final String outFlowIdentifier = this.outgoingFlows.get(outCounter).identifier() + Constant.UNDERSCORE +
						Bpmn.BEGIN;

				if (nbCharCurrentLine + outFlowIdentifier.length() + 1 > Lnt.MAX_CHAR_PER_LINE)
				{
					stringBuilder.append(Constant.LINE_FEED)
							.append(Utils.indent(baseIndent + taskIdentifier.length()));
					nbCharCurrentLine = baseIndent + taskIdentifier.length() + outFlowIdentifier.length();
				}
				else
				{
					nbCharCurrentLine += outFlowIdentifier.length();
				}

				stringBuilder.append(outFlowIdentifier);
				outCounter++;

				if (outCounter < nbOut)
				{
					stringBuilder.append(Constant.COMA_AND_SPACE);
					nbCharCurrentLine += 2;
				}
			}

			stringBuilder.append(Constant.RIGHT_SQUARE_BRACKET);
		}
	}

	/**
	 * Abstract class for Gateway
	 */
	abstract class Gateway extends Node
	{
		Gateway(final String identifier,
				final ArrayList<Flow> incomingFlows,
				final ArrayList<Flow> outgoingFlows)
		{
			super(identifier, incomingFlows, outgoingFlows);
		}

		ArrayList<String> alpha()
		{
			return new ArrayList<>();
		}

		void processLnt(final StringBuilder stringBuilder,
						final String pattern,
						final String type)
		{
			final int argsMinIndent = 21 + 9;
			final int incFlowMinIndent;
			final int outFlowMinIndent;
			int nbCharCurrentLine = argsMinIndent + this.identifier.length() + 2;

			stringBuilder.append(Bpmn.GATEWAY)
					.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
					.append(this.identifier)
					.append(Constant.COMA);

			if (nbCharCurrentLine + pattern.length() > Lnt.MAX_CHAR_PER_LINE)
			{
				stringBuilder.append(Constant.LINE_FEED)
						.append(Utils.indent(argsMinIndent));
				nbCharCurrentLine = argsMinIndent + pattern.length();
			}
			else
			{
				stringBuilder.append(Constant.SPACE);
				nbCharCurrentLine += pattern.length();
			}

			stringBuilder.append(pattern)
					.append(Constant.COMA);

			if (nbCharCurrentLine + type.length() > Lnt.MAX_CHAR_PER_LINE)
			{
				stringBuilder.append(Constant.LINE_FEED)
						.append(Utils.indent(argsMinIndent));
				nbCharCurrentLine = argsMinIndent + type.length();
			}
			else
			{
				stringBuilder.append(Constant.SPACE);
				nbCharCurrentLine += type.length();
			}

			stringBuilder.append(type)
					.append(Constant.COMA);

			if (this.incomingFlows.isEmpty())
			{
				throw new IllegalStateException("Gateway \"" + this.identifier + "\" has no incoming flows!");
			}

			final Flow firstIncFlow = this.incomingFlows.remove(0);

			if (nbCharCurrentLine + firstIncFlow.identifier().length() + 3 > Lnt.MAX_CHAR_PER_LINE)
			{
				stringBuilder.append(Constant.LINE_FEED)
						.append(Utils.indent(argsMinIndent));
				nbCharCurrentLine = argsMinIndent + firstIncFlow.identifier().length() + 3;
				incFlowMinIndent = argsMinIndent + 1;
			}
			else
			{
				incFlowMinIndent = nbCharCurrentLine + 5;
				nbCharCurrentLine += firstIncFlow.identifier().length() + 3;
				stringBuilder.append(Constant.SPACE);
			}

			stringBuilder.append(Constant.LEFT_CURVY_BRACKET)
					.append(firstIncFlow.identifier());

			for (Flow inFlow : this.incomingFlows)
			{
				stringBuilder.append(Constant.COMA_AND_SPACE);
				nbCharCurrentLine += 2;

				if (inFlow.identifier().length() + nbCharCurrentLine + 2 > Lnt.MAX_CHAR_PER_LINE)
				{
					stringBuilder.append(Constant.LINE_FEED)
							.append(Utils.indent(incFlowMinIndent));
					nbCharCurrentLine = incFlowMinIndent + inFlow.identifier().length();
				}
				else
				{
					nbCharCurrentLine += inFlow.identifier().length();
				}

				stringBuilder.append(inFlow.identifier());
			}

			stringBuilder.append(Constant.RIGHT_CURVY_BRACKET)
					.append(Constant.COMA);

			if (this.outgoingFlows.isEmpty())
			{
				throw new IllegalStateException("Gateway \"" + this.identifier + "\" has no outgoing flows!");
			}

			final Flow firstOutFlow = this.outgoingFlows.remove(0);

			if (nbCharCurrentLine + firstOutFlow.identifier().length() + 3 > Lnt.MAX_CHAR_PER_LINE)
			{
				stringBuilder.append(Constant.LINE_FEED)
						.append(Utils.indent(argsMinIndent));
				nbCharCurrentLine = argsMinIndent + firstOutFlow.identifier().length() + 3;
				outFlowMinIndent = argsMinIndent + 1;
			}
			else
			{
				outFlowMinIndent = nbCharCurrentLine + 5;
				nbCharCurrentLine += firstOutFlow.identifier().length() + 3;
				stringBuilder.append(Constant.SPACE);
			}

			stringBuilder.append(Constant.LEFT_CURVY_BRACKET)
					.append(firstOutFlow.identifier());

			for (Flow outFlow : this.outgoingFlows)
			{
				stringBuilder.append(Constant.COMA_AND_SPACE);
				nbCharCurrentLine += 2;

				if (outFlow.identifier().length() + nbCharCurrentLine + 2 > Lnt.MAX_CHAR_PER_LINE)
				{
					stringBuilder.append(Constant.LINE_FEED)
							.append(Utils.indent(outFlowMinIndent));
					nbCharCurrentLine = incFlowMinIndent + outFlow.identifier().length();
				}
				else
				{
					nbCharCurrentLine += outFlow.identifier().length();
				}

				stringBuilder.append(outFlow.identifier());
			}

			stringBuilder.append(Constant.RIGHT_CURVY_BRACKET)
					.append(Constant.RIGHT_PARENTHESIS);

			this.incomingFlows.add(0, firstIncFlow);
			this.outgoingFlows.add(0, firstOutFlow);
		}
	}

	/**
	 * Abstract graph for Split Gateway
	 */
	abstract class SplitGateway extends Gateway
	{
		SplitGateway(final String identifier,
					 final ArrayList<Flow> incomingFlows,
					 final ArrayList<Flow> outgoingFlows)
		{
			super(identifier, incomingFlows, outgoingFlows);
		}

		/**
		 * Generates process instantiation for all split gateways
		 *
		 * @param stringBuilder the builder to which the gateway should be dumped
		 */
		void writeMainLnt(final StringBuilder stringBuilder,
						  final int baseIndent)
		{
			final String incomingFlowIdentifier = Constant.SPACE + Constant.LEFT_SQUARE_BRACKET +
					this.incomingFlows.get(0).identifier() + Constant.UNDERSCORE + Bpmn.FINISH +
					Constant.COMA_AND_SPACE;
			final int nbOut = this.outgoingFlows.size();
			int i = 0;
			int nbCharCurrentLine = baseIndent + incomingFlowIdentifier.length();

			stringBuilder.append(incomingFlowIdentifier);

			while (i < nbOut)
			{
				final String outFlowIdentifier = this.outgoingFlows.get(i).identifier() + Constant.UNDERSCORE +
						Bpmn.BEGIN;

				if (nbCharCurrentLine + outFlowIdentifier.length() + 1 > Lnt.MAX_CHAR_PER_LINE)
				{
					stringBuilder.append(Constant.LINE_FEED)
							.append(Utils.indent(baseIndent));
					nbCharCurrentLine = baseIndent + outFlowIdentifier.length();
				}
				else
				{
					nbCharCurrentLine += outFlowIdentifier.length();
				}

				stringBuilder.append(outFlowIdentifier);
				i++;

				if (i < nbOut)
				{
					stringBuilder.append(Constant.COMA_AND_SPACE);
					nbCharCurrentLine += 2;
				}
			}

			stringBuilder.append(Constant.RIGHT_SQUARE_BRACKET);
		}

		/**
		 * For a split (generic), if not visited yet, recursive call on the target nodes of all outgoing flows.
		 * Returns the list of reachable or joins.
		 */
		ArrayList<Pair<String, Integer>> reachableOrJoin(final ArrayList<Pair<String, Integer>> visited,
														 final int depth)
		{
			if (pairListContainsIdentifier(visited, this.identifier))
			{
				return new ArrayList<>();
			}

			final ArrayList<Pair<String, Integer>> res = new ArrayList<>();

			for (Flow f : this.outgoingFlows)
			{
				final ArrayList<Pair<String, Integer>> temp = new ArrayList<>(visited);
				temp.add(Pair.of(this.identifier, depth));
				res.addAll(f.getTarget().reachableOrJoin(temp, depth));
			}

			return res;
		}

		void dumpMaude(final StringBuilder stringBuilder)
		{
			stringBuilder.append("        split(");
			stringBuilder.append(this.identifier);
			stringBuilder.append(Constant.COMA);
			stringBuilder.append("inclusive");
			stringBuilder.append(Constant.COMA);
			stringBuilder.append(this.incomingFlows.get(0).identifier());
			stringBuilder.append(",(");
			String separator = "";

			for (Flow ofl : this.outgoingFlows)
			{
				stringBuilder.append(separator);
				stringBuilder.append(ofl.identifier());
				separator = ",";
			}

			stringBuilder.append("))");
		}
	}

	/**
	 * Class for OrSplitGateway
	 */
	class OrSplitGateway extends SplitGateway
	{
		private String correspOrJoin;

		OrSplitGateway(final String identifier,
					   final ArrayList<Flow> incomingFlows,
					   final ArrayList<Flow> outgoingFlows)
		{
			super(identifier, incomingFlows, outgoingFlows);
			this.correspOrJoin = Constant.EMPTY_STRING;
		}

		String getCorrespOrJoin()
		{
			return this.correspOrJoin;
		}

		void setCorrespOrJoin(final String correspOrJoin)
		{
			this.correspOrJoin = correspOrJoin;
		}

		@Override
		void processLnt(final StringBuilder stringBuilder)
		{
			this.writeLnt(stringBuilder, 21); //TODO Vérifier
		}

		@Override
		void writeLnt(final StringBuilder stringBuilder,
					  final int baseIndent)
		{
			final int nbOut = this.outgoingFlows.size();
			//TODO: update the translation to consider properly the default semantics (if there is such a branch)

			//We translate the inclusive split by enumerating all combinations in a alt / par
			final ArrayList<String> outgoingFlowsAlphabet = new ArrayList<>();
			int nb = 1;

			while (nb <= nbOut)
			{
				outgoingFlowsAlphabet.add(Bpmn.OUTGOING_FLOW + Constant.UNDERSCORE + nb);
				nb++;
			}

			final ArrayList<ArrayList<String>> allOutgoingFlowsCombinations = computeAllCombinations(outgoingFlowsAlphabet);
			final int nbt = allOutgoingFlowsCombinations.size();

			final String processPreamble = Lnt.PROCESS + Constant.SPACE + Bpmn.INCLUSIVE_SPLIT_GATEWAY +
					Constant.UNDERSCORE + this.identifier + Constant.SPACE_AND_LEFT_SQUARE_BRACKET +
					Bpmn.INCOMING_FLOW_VARIABLE + Constant.COMA_AND_SPACE;
			stringBuilder.append(processPreamble);

			//We dump the process alphabet (flows + synchronization points if necessary)
			int nbg = 1;
			int nbCharCurrentLine = processPreamble.length();
			boolean lineJumped = false;

			while (nbg <= nbOut)
			{
				final String outgoingFlowIdentifier = Bpmn.OUTGOING_FLOW + Constant.UNDERSCORE + nbg;

				if (nbCharCurrentLine + outgoingFlowIdentifier.length() > Lnt.MAX_CHAR_PER_LINE)
				{
					lineJumped = true;
					stringBuilder.append(Constant.LINE_FEED)
							.append(Utils.indent(Constant.PROCESS_INDENT_LENGTH));
					nbCharCurrentLine = Constant.PROCESS_INDENT_LENGTH + outgoingFlowIdentifier.length();
				}
				else
				{
					nbCharCurrentLine += outgoingFlowIdentifier.length();
				}

				stringBuilder.append(outgoingFlowIdentifier);
				nbg++;

				if (nbg <= nbOut)
				{
					stringBuilder.append(Constant.COMA_AND_SPACE);
					nbCharCurrentLine += 2;
				}
			}

			if (nbt > 0
				&& (!isBalanced || !this.correspOrJoin.isEmpty()))
			{
				stringBuilder.append(Constant.COMA_AND_SPACE);
				nbCharCurrentLine += 2;
				int counter = 1;

				for (Collection<String> ignored : allOutgoingFlowsCombinations) //TODO Bizarre ....
				{
					final String identifier = (isBalanced && TODO ? this.correspOrJoin : this.identifier) +
							Constant.UNDERSCORE + counter;

					if (identifier.length() + nbCharCurrentLine > Lnt.MAX_CHAR_PER_LINE)
					{
						lineJumped = true;
						stringBuilder.append(Constant.LINE_FEED)
								.append(Utils.indent(Constant.PROCESS_INDENT_LENGTH));
						nbCharCurrentLine = Constant.PROCESS_INDENT_LENGTH + identifier.length();
					}
					else
					{
						nbCharCurrentLine += identifier.length();
					}

					stringBuilder.append(identifier);
					counter++;

					if (counter <= nbt)
					{
						stringBuilder.append(Constant.COMA_AND_SPACE);
						nbCharCurrentLine += 2;
					}
				}
			}

			stringBuilder.append(Constant.COLON_AND_SPACE)
					.append(Lnt.ANY)
					.append(Constant.RIGHT_SQUARE_BRACKET)
					.append(lineJumped ? Constant.LINE_FEED : Constant.SPACE)
					.append(Lnt.IS)
					.append(Constant.LINE_FEED)
					.append(Utils.indentLNT(1))
					.append(Lnt.VAR);

			int outgoingFlowsCounter = allOutgoingFlowsCombinations.size();
			final int minIndent;

			if (outgoingFlowsCounter > Constant.MAX_VARS_PER_LINE)
			{
				stringBuilder.append(Constant.LINE_FEED)
						.append(Utils.indentLNT(2));
				nbCharCurrentLine = 6;
				minIndent = 6;
			}
			else
			{
				stringBuilder.append(Constant.SPACE);
				nbCharCurrentLine = 7;
				minIndent = 7;
			}

			while (outgoingFlowsCounter > 0)
			{
				final String identifier = Bpmn.IDENT_VARIABLE + outgoingFlowsCounter;

				if (nbCharCurrentLine + identifier.length() > Lnt.MAX_CHAR_PER_LINE)
				{
					stringBuilder.append(Constant.LINE_FEED)
							.append(Utils.indent(minIndent));
					nbCharCurrentLine = minIndent + identifier.length();
				}
				else
				{
					nbCharCurrentLine += identifier.length();
				}

				stringBuilder.append(identifier);
				outgoingFlowsCounter--;

				if (outgoingFlowsCounter > 0)
				{
					stringBuilder.append(Constant.COMA_AND_SPACE);
					nbCharCurrentLine += 2;
				}
			}

			//TODO We generate unnecessary variables
			stringBuilder.append(Constant.COLON_AND_SPACE)
					.append(Bpmn.ID_LNT_TYPE);

			if (allOutgoingFlowsCombinations.size() > Constant.MAX_VARS_PER_LINE)
			{
				stringBuilder.append(Constant.LINE_FEED)
						.append(Utils.indentLNT(1));
			}
			else
			{
				stringBuilder.append(Constant.SPACE);
			}

			stringBuilder.append(Lnt.IN)
					.append(Constant.LINE_FEED)
					.append(Utils.indentLNT(2))
					.append(Lnt.VAR)
					.append(Constant.SPACE)
					.append(Bpmn.IDENT_VARIABLE)
					.append(Constant.COLON_AND_SPACE)
					.append(Bpmn.ID_LNT_TYPE)
					.append(Constant.SPACE)
					.append(Lnt.IN)
					.append(Constant.LINE_FEED)
					.append(Utils.indentLNT(3))
					.append(Lnt.LOOP)
					.append(Constant.LINE_FEED)
					.append(Utils.indentLNT(4))
					.append(Bpmn.INCOMING_FLOW_VARIABLE)
					.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
					.append(Lnt.OUT_PARAMETER)
					.append(Bpmn.IDENT_VARIABLE)
					.append(Lnt.SPACED_OF)
					.append(Bpmn.ID_LNT_TYPE)
					.append(Constant.RIGHT_PARENTHESIS)
					.append(Lnt.SEQUENTIAL_COMPOSITION_OPERATOR)
					.append(Constant.LINE_FEED)
					.append(Utils.indentLNT(4))
					.append(Lnt.ALT)
					.append(Constant.LINE_FEED);

			nb = 1;
			//Counter for generating synchro points
			int counter = 1;

			for (Collection<String> outgoingFlowsCombination : allOutgoingFlowsCombinations)
			{
				int nb2 = 1;
				stringBuilder.append(Utils.indentLNT(5));

				if (!isBalanced)
				{
					stringBuilder.append(this.identifier)
							.append(Constant.UNDERSCORE)
							.append(counter)
							.append(Lnt.SEQUENTIAL_COMPOSITION_OPERATOR)
							.append(Constant.LINE_FEED)
							.append(Utils.indentLNT(5));

					counter++;
				}

				if (outgoingFlowsCombination.size() > 1)
				{
					outgoingFlowsCounter = allOutgoingFlowsCombinations.size();
					stringBuilder.append(Lnt.PAR)
							.append(Constant.LINE_FEED);

					for (String outgoingFlowIdentifier : outgoingFlowsCombination)
					{
						stringBuilder.append(Utils.indentLNT(6))
								.append(outgoingFlowIdentifier)
								.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
								.append(Lnt.OUT_PARAMETER)
								.append(Bpmn.IDENT_VARIABLE)
								.append(outgoingFlowsCounter)
								.append(Lnt.SPACED_OF)
								.append(Bpmn.ID_LNT_TYPE)
								.append(Constant.RIGHT_PARENTHESIS);

						outgoingFlowsCounter--;
						nb2++;

						if (nb2 <= outgoingFlowsCombination.size())
						{
							stringBuilder.append(Constant.LINE_FEED)
									.append(Utils.indentLNT(5))
									.append(Lnt.PAR_OPERATOR)
									.append(Constant.LINE_FEED)
							;
						}
					}

					stringBuilder.append(Constant.LINE_FEED)
							.append(Utils.indentLNT(5))
							.append(Lnt.END_PAR);
				}
				else
				{
					stringBuilder.append(outgoingFlowsCombination.iterator().next())
							.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
							.append(Lnt.OUT_PARAMETER)
							.append(Bpmn.IDENT_VARIABLE)
							.append(Lnt.SPACED_OF)
							.append(Bpmn.ID_LNT_TYPE)
							.append(Constant.RIGHT_PARENTHESIS);
				}

				if (isBalanced)
				{
					//Add synchronization points if there's a corresponding join
					if (!this.correspOrJoin.isEmpty())
					{
						stringBuilder.append(Lnt.SEQUENTIAL_COMPOSITION_OPERATOR)
								.append(Constant.LINE_FEED)
								.append(Utils.indentLNT(5))
								.append(this.correspOrJoin)
								.append(Constant.UNDERSCORE)
								.append(counter);

						counter++;
					}
				}

				nb++;

				if (nb <= nbt)
				{
					stringBuilder.append(Constant.LINE_FEED)
							.append(Utils.indentLNT(4))
							.append(Lnt.ALT_OPERATOR)
							.append(Constant.LINE_FEED);
				}
			}

			stringBuilder.append(Constant.LINE_FEED)
					.append(Utils.indentLNT(4))
					.append(Lnt.END_ALT)
					.append(Constant.LINE_FEED)
					.append(Utils.indentLNT(3))
					.append(Lnt.END_LOOP)
					.append(Constant.LINE_FEED)
					.append(Utils.indentLNT(2))
					.append(Lnt.END_VAR)
					.append(Constant.LINE_FEED)
					.append(Utils.indentLNT(1))
					.append(Lnt.END_VAR)
					.append(Constant.LINE_FEED)
					.append(Lnt.END_PROCESS)
					.append(Constant.DOUBLE_LINE_FEED)
					.append(Lnt.STANDARD_SEPARATOR)
					.append(Constant.DOUBLE_LINE_FEED);
		}

		/**
		 * Generates process instantiation for main LNT process.
		 *
		 * @param stringBuilder the builder to which the gateway should be dumped
		 */
		void writeMainLnt(final StringBuilder stringBuilder,
						  final int baseIndent)
		{
			if (!this.correspOrJoin.isEmpty()
					|| !isBalanced)
			{
				final ArrayList<String> outgoingFlowsAlphabet = new ArrayList<>();
				int nb = 1;

				while (nb <= this.outgoingFlows.size())
				{
					outgoingFlowsAlphabet.add(Bpmn.OUTGOING_FLOW + Constant.UNDERSCORE + nb);
					nb++;
				}

				final ArrayList<ArrayList<String>> allCombinations = computeAllCombinations(outgoingFlowsAlphabet);
				final int nbCombi = allCombinations.size();
				int nbCharCurrentLine = baseIndent;

				if (isBalanced)
				{
					//We dump the synchronisation points
					if (nbCombi > 0)
					{
						int counter = 1;

						for (ArrayList<String> ignored : allCombinations) //TODO Bizarre...
						{
							final String identifier = this.correspOrJoin + Constant.UNDERSCORE + counter;

							if (identifier.length() + nbCharCurrentLine > Lnt.MAX_CHAR_PER_LINE)
							{
								stringBuilder.append(Constant.LINE_FEED)
										.append(Utils.indent(baseIndent));
								nbCharCurrentLine = baseIndent + identifier.length();
							}
							else
							{
								nbCharCurrentLine += identifier.length();
							}

							stringBuilder.append(identifier);
							counter++;

							if (counter <= nbCombi)
							{
								stringBuilder.append(Constant.COMA_AND_SPACE);
								nbCharCurrentLine += 2;
							}
						}

						stringBuilder.append(Constant.SPACE)
								.append(Constant.RIGHT_ARROW)
								.append(Constant.LINE_FEED)
								.append(Utils.indent(baseIndent + 3));
						nbCharCurrentLine = baseIndent + 3;
					}
				}

				//Process call + alphabet
				final String processIdentifier = Bpmn.INCLUSIVE_SPLIT_GATEWAY + Constant.UNDERSCORE +
						this.identifier + Constant.SPACE + Constant.LEFT_SQUARE_BRACKET;
				final String incFlowIdentifier = this.incomingFlows().get(0).identifier() + Constant.UNDERSCORE +
						Bpmn.FINISH + Constant.COMA_AND_SPACE;
				final int minIndent = nbCharCurrentLine + processIdentifier.length();
				nbCharCurrentLine += processIdentifier.length() + incFlowIdentifier.length();

				stringBuilder.append(processIdentifier)
						.append(incFlowIdentifier);

				int currentOutgoingFlowIndex = 0;

				while (currentOutgoingFlowIndex < this.outgoingFlows.size())
				{
					final String outFlowIdentifier = this.outgoingFlows.get(currentOutgoingFlowIndex).identifier() +
							Constant.UNDERSCORE + Bpmn.BEGIN;

					if (nbCharCurrentLine + outFlowIdentifier.length() > Lnt.MAX_CHAR_PER_LINE)
					{
						stringBuilder.append(Constant.LINE_FEED)
								.append(Utils.indent(minIndent));
						nbCharCurrentLine = minIndent + outFlowIdentifier.length();
					}
					else
					{
						nbCharCurrentLine += outFlowIdentifier.length();
					}

					stringBuilder.append(outFlowIdentifier);
					currentOutgoingFlowIndex++;

					if (currentOutgoingFlowIndex < this.outgoingFlows.size())
					{
						stringBuilder.append(Constant.COMA_AND_SPACE);
						nbCharCurrentLine += 2;
					}
				}

				if (nbCombi > 0)
				{
					stringBuilder.append(Constant.COMA_AND_SPACE);
					nbCharCurrentLine += 2;
					int counter = 1;

					for (ArrayList<String> ignored : allCombinations)
					{
						final String identifier = (isBalanced ? this.correspOrJoin : this.identifier) +
								Constant.UNDERSCORE + counter;

						if (nbCharCurrentLine + identifier.length() > Lnt.MAX_CHAR_PER_LINE)
						{
							stringBuilder.append(Constant.LINE_FEED)
									.append(Utils.indent(minIndent));
							nbCharCurrentLine = minIndent + identifier.length();
						}
						else
						{
							nbCharCurrentLine += identifier.length();
						}

						stringBuilder.append(identifier);
						counter++;

						if (counter <= nbCombi)
						{
							stringBuilder.append(Constant.COMA_AND_SPACE);
							nbCharCurrentLine += 2;
						}
					}
				}

				stringBuilder.append(Constant.RIGHT_SQUARE_BRACKET);
			}
			else
			{
				stringBuilder.append(Bpmn.INCLUSIVE_SPLIT_GATEWAY)
						.append(Constant.UNDERSCORE)
						.append(this.identifier);

				super.writeMainLnt(stringBuilder, baseIndent + this.identifier.length() + 9);
			}
		}

		/**
		 * For an or split, if not visited yet, recursive call on the target nodes of all outgoing flows.
		 * We increase the depth, to distinguish it from the split or being analyzed.
		 *
		 * @param visited the list of visited nodes
		 * @param depth the depth
		 * @return the list of reachable or joins.
		 */
		@Override
		ArrayList<Pair<String, Integer>> reachableOrJoin(final ArrayList<Pair<String, Integer>> visited,
														 final int depth)
		{
			if (pairListContainsIdentifier(visited, this.identifier))
			{
				return new ArrayList<>();
			}

			final ArrayList<Pair<String, Integer>> result = new ArrayList<>();

			for (final Flow f : this.outgoingFlows)
			{
				final ArrayList<Pair<String, Integer>> temp = new ArrayList<>(visited);
				temp.add(Pair.of(this.identifier, depth));
				result.addAll(f.getTarget().reachableOrJoin(temp, depth + 1));
			}

			return result;
		}

		public void dumpMaude(final StringBuilder stringBuilder)
		{
			if (isBalanced)
			{
				super.dumpMaude(stringBuilder);
			}
			else
			{
				stringBuilder.append("        split(");
				stringBuilder.append(this.identifier);
				stringBuilder.append(",inclusive,");
				stringBuilder.append(this.incomingFlows.get(0).identifier());
				stringBuilder.append(",(");
				int counter = this.outgoingFlows.size();

				for (Flow outFlow : this.outgoingFlows)
				{
					final Random random = new Random();
					final double proba = Math.round(random.nextDouble() * 100.0) / 100.0;
					counter--;
					stringBuilder.append(Constant.LEFT_PARENTHESIS);
					stringBuilder.append(outFlow.identifier());
					stringBuilder.append(Constant.COMA);
					stringBuilder.append(proba);
					stringBuilder.append(Constant.RIGHT_PARENTHESIS);

					if (counter > 0) stringBuilder.append(Constant.SPACE);
				}

				stringBuilder.append(Constant.RIGHT_PARENTHESIS)
						.append(Constant.RIGHT_PARENTHESIS);
			}
		}
	}

	/**
	 * Class for XOrSplitGateway
	 */
	class XOrSplitGateway extends SplitGateway
	{
		XOrSplitGateway(final String identifier,
						final ArrayList<Flow> incomingFlows,
						final ArrayList<Flow> outgoingFlows)
		{
			super(identifier, incomingFlows, outgoingFlows);
		}

		@Override
		void processLnt(final StringBuilder stringBuilder)
		{
			this.writeLnt(stringBuilder, 21); //TODO Vérifier
		}

		/**
		 * Generates the process for exclusive split gateway.
		 * Takes as input the number of outgoing flows.
		 *
		 * @param stringBuilder the builder to which the gateway should be dumped
		 */
		@Override
		void writeLnt(final StringBuilder stringBuilder,
					  final int baseIndent)
		{
			boolean lineJumped = false;
			final String processIdentifier = Lnt.PROCESS + Constant.SPACE + Bpmn.EXCLUSIVE_SPLIT_GATEWAY +
					Constant.UNDERSCORE + this.identifier + Constant.SPACE_AND_LEFT_SQUARE_BRACKET +
					Bpmn.INCOMING_FLOW_VARIABLE + Constant.COMA_AND_SPACE;
			stringBuilder.append(processIdentifier);
			int nb = 1;
			int nbCharCurrentLine = processIdentifier.length();

			while (nb <= this.outgoingFlows.size())
			{
				final String outFlowIdentifier = Bpmn.OUTGOING_FLOW + Constant.UNDERSCORE + nb;

				if (nbCharCurrentLine + outFlowIdentifier.length() > Lnt.MAX_CHAR_PER_LINE)
				{
					lineJumped = true;
					stringBuilder.append(Constant.LINE_FEED)
							.append(Utils.indent(Constant.PROCESS_INDENT_LENGTH));
					nbCharCurrentLine = Constant.PROCESS_INDENT_LENGTH + outFlowIdentifier.length();
				}
				else
				{
					nbCharCurrentLine += outFlowIdentifier.length();
				}

				stringBuilder.append(outFlowIdentifier);
				nb++;

				if (nb <= this.outgoingFlows.size())
				{
					stringBuilder.append(Constant.COMA_AND_SPACE);
					nbCharCurrentLine += 2;
				}
			}

			stringBuilder.append(Constant.COLON_AND_SPACE)
					.append(Lnt.ANY)
					.append(Constant.RIGHT_SQUARE_BRACKET)
					.append(lineJumped ? Constant.LINE_FEED : Constant.SPACE)
					.append(Lnt.IS)
					.append(Constant.LINE_FEED)
					.append(Utils.indentLNT(1))
					.append(Lnt.VAR)
					.append(Constant.SPACE)
					.append(Bpmn.IDENT_VARIABLE)
					.append(Constant.COLON_AND_SPACE)
					.append(Bpmn.ID_LNT_TYPE)
					.append(Constant.SPACE)
					.append(Lnt.IN)
					.append(Constant.LINE_FEED)
					.append(Utils.indentLNT(2))
					.append(Lnt.LOOP)
					.append(Constant.LINE_FEED)
					.append(Utils.indentLNT(3))
					.append(Bpmn.INCOMING_FLOW_VARIABLE)
					.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
					.append(Lnt.OUT_PARAMETER)
					.append(Bpmn.IDENT_VARIABLE)
					.append(Lnt.SPACED_OF)
					.append(Bpmn.ID_LNT_TYPE)
					.append(Constant.RIGHT_PARENTHESIS)
					.append(Lnt.SEQUENTIAL_COMPOSITION_OPERATOR)
					.append(Constant.LINE_FEED)
					.append(Utils.indentLNT(3))
					.append(Lnt.ALT)
					.append(Constant.LINE_FEED);

			nb = 1;

			while (nb <= this.outgoingFlows.size())
			{
				stringBuilder.append(Utils.indentLNT(4))
						.append(Bpmn.OUTGOING_FLOW)
						.append(Constant.UNDERSCORE)
						.append(nb)
						.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
						.append(Lnt.OUT_PARAMETER)
						.append(Bpmn.IDENT_VARIABLE)
						.append(Lnt.SPACED_OF)
						.append(Bpmn.ID_LNT_TYPE)
						.append(Constant.RIGHT_PARENTHESIS);

				nb++;

				if (nb <= this.outgoingFlows.size())
				{
					stringBuilder.append(Constant.LINE_FEED)
							.append(Utils.indentLNT(3))
							.append(Lnt.ALT_OPERATOR)
							.append(Constant.LINE_FEED);
				}
			}

			stringBuilder.append(Constant.LINE_FEED)
					.append(Utils.indentLNT(3))
					.append(Lnt.END_ALT)
					.append(Constant.LINE_FEED)
					.append(Utils.indentLNT(2))
					.append(Lnt.END_LOOP)
					.append(Constant.LINE_FEED)
					.append(Utils.indentLNT(1))
					.append(Lnt.END_VAR)
					.append(Constant.LINE_FEED)
					.append(Lnt.END_PROCESS)
					.append(Constant.DOUBLE_LINE_FEED)
					.append(Lnt.STANDARD_SEPARATOR)
					.append(Constant.DOUBLE_LINE_FEED);
		}

		void writeMainLnt(final StringBuilder stringBuilder,
						  final int baseIndent)
		{
			stringBuilder.append(Bpmn.EXCLUSIVE_SPLIT_GATEWAY)
					.append(Constant.UNDERSCORE)
					.append(this.identifier);

			super.writeMainLnt(stringBuilder, baseIndent + this.identifier.length() + 11);
		}

		ArrayList<Pair<String, Integer>> reachableOrJoin(final ArrayList<Pair<String, Integer>> visited,
														 final int depth)
		{
			return super.reachableOrJoin(visited, depth);
		}
	}

	/**
	 * Class for AndSplitGateway
	 */
	class AndSplitGateway extends SplitGateway
	{
		AndSplitGateway(final String identifier,
						final ArrayList<Flow> incomingFlows,
						final ArrayList<Flow> outgoingFlows)
		{
			super(identifier, incomingFlows, outgoingFlows);
		}

		@Override
		void processLnt(final StringBuilder stringBuilder)
		{
			this.writeLnt(stringBuilder, 21); //TODO Vérifier
		}

		/**
		 * Generates the process for parallel split gateway.
		 * Takes as input the number of outgoing flows.
		 *
		 * @param stringBuilder the builder to which the gateway should be dumped
		 */
		@Override
		void writeLnt(final StringBuilder stringBuilder,
					  final int baseIndent)
		{
			boolean lineJumped = false;
			final String processIdentifier = Lnt.PROCESS + Constant.SPACE + Bpmn.PARALLEL_SPLIT_GATEWAY +
					Constant.UNDERSCORE + this.identifier + Constant.SPACE_AND_LEFT_SQUARE_BRACKET +
					Bpmn.INCOMING_FLOW_VARIABLE + Constant.COMA_AND_SPACE;
			stringBuilder.append(processIdentifier);
			int nb = 1;

			int nbCharCurrentLine = processIdentifier.length();

			while (nb <= this.outgoingFlows.size())
			{
				final String flowIdentifier = Bpmn.OUTGOING_FLOW + Constant.UNDERSCORE + nb;

				if (nbCharCurrentLine + flowIdentifier.length() + 1 > Lnt.MAX_CHAR_PER_LINE)
				{
					lineJumped = true;
					stringBuilder.append(Constant.LINE_FEED).append(Utils.indent(Constant.PROCESS_INDENT_LENGTH));
					nbCharCurrentLine = Constant.PROCESS_INDENT_LENGTH + flowIdentifier.length();
				}
				else
				{
					nbCharCurrentLine += flowIdentifier.length();
				}

				stringBuilder.append(flowIdentifier);
				nb++;

				if (nb <= this.outgoingFlows.size())
				{
					stringBuilder.append(Constant.COMA_AND_SPACE);
					nbCharCurrentLine += 2;
				}
			}

			stringBuilder.append(Constant.COLON_AND_SPACE)
					.append(Lnt.ANY)
					.append(Constant.RIGHT_SQUARE_BRACKET)
					.append(lineJumped ? Constant.LINE_FEED : Constant.SPACE)
					.append(Lnt.IS)
					.append(Constant.LINE_FEED)
					.append(Utils.indentLNT(1))
					.append(Lnt.VAR);

			int variablesCounter = this.outgoingFlows.size();
			final int minIndentation;

			if (this.outgoingFlows.size() > Constant.MAX_VARS_PER_LINE)
			{
				stringBuilder.append(Constant.LINE_FEED).append(Utils.indentLNT(2));
				nbCharCurrentLine = 6;
				minIndentation = 6;
			}
			else
			{
				stringBuilder.append(Constant.SPACE);
				nbCharCurrentLine = 7;
				minIndentation = 7;
			}

			while (variablesCounter > 0)
			{
				final String flowIdentifier = Bpmn.IDENT_VARIABLE + variablesCounter;

				if (variablesCounter != this.outgoingFlows.size())
				{
					if (nbCharCurrentLine + flowIdentifier.length() > Lnt.MAX_CHAR_PER_LINE)
					{
						stringBuilder.append(Constant.LINE_FEED)
								.append(Utils.indent(minIndentation));
						nbCharCurrentLine = flowIdentifier.length() + minIndentation;
					}
					else
					{
						nbCharCurrentLine += flowIdentifier.length();
					}
				}
				else
				{
					nbCharCurrentLine += flowIdentifier.length();
				}

				stringBuilder.append(flowIdentifier);
				variablesCounter--;

				if (variablesCounter > 0)
				{
					stringBuilder.append(Constant.COMA_AND_SPACE);
					nbCharCurrentLine += 2;
				}
			}

			stringBuilder.append(Constant.COLON_AND_SPACE)
					.append(Bpmn.ID_LNT_TYPE);

			if (this.outgoingFlows.size() > Constant.MAX_VARS_PER_LINE)
			{
				stringBuilder.append(Constant.LINE_FEED)
						.append(Utils.indentLNT(1));
			}
			else
			{
				stringBuilder.append(Constant.SPACE);
			}

			stringBuilder.append(Lnt.IN)
					.append(Constant.LINE_FEED)
					.append(Utils.indentLNT(2))
					.append(Lnt.VAR)
					.append(Constant.SPACE)
					.append(Bpmn.IDENT_VARIABLE)
					.append(Constant.COLON_AND_SPACE)
					.append(Bpmn.ID_LNT_TYPE)
					.append(Constant.SPACE)
					.append(Lnt.IN)
					.append(Constant.LINE_FEED)
					.append(Utils.indentLNT(3))
					.append(Lnt.LOOP)
					.append(Constant.LINE_FEED)
					.append(Utils.indentLNT(4))
					.append(Bpmn.INCOMING_FLOW_VARIABLE)
					.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
					.append(Lnt.OUT_PARAMETER)
					.append(Bpmn.IDENT_VARIABLE)
					.append(Lnt.SPACED_OF)
					.append(Bpmn.ID_LNT_TYPE)
					.append(Constant.RIGHT_PARENTHESIS)
					.append(Lnt.SEQUENTIAL_COMPOSITION_OPERATOR)
					.append(Constant.LINE_FEED)
					.append(Utils.indentLNT(4))
					.append(Lnt.PAR)
					.append(Constant.LINE_FEED);

			nb = 1;
			variablesCounter = this.outgoingFlows.size();

			while (nb <= this.outgoingFlows.size())
			{
				stringBuilder.append(Utils.indentLNT(5))
						.append(Bpmn.OUTGOING_FLOW)
						.append(Constant.UNDERSCORE)
						.append(nb)
						.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
						.append(Lnt.OUT_PARAMETER)
						.append(Bpmn.IDENT_VARIABLE)
						.append(variablesCounter)
						.append(Lnt.SPACED_OF)
						.append(Bpmn.ID_LNT_TYPE)
						.append(Constant.RIGHT_PARENTHESIS);

				variablesCounter--;
				nb++;

				if (nb <= this.outgoingFlows.size())
				{
					stringBuilder.append(Constant.LINE_FEED)
							.append(Utils.indentLNT(4))
							.append(Lnt.PAR_OPERATOR)
							.append(Constant.LINE_FEED);
				}
			}

			stringBuilder.append(Constant.LINE_FEED)
					.append(Utils.indentLNT(4))
					.append(Lnt.END_PAR)
					.append(Constant.LINE_FEED)
					.append(Utils.indentLNT(3))
					.append(Lnt.END_LOOP)
					.append(Constant.LINE_FEED)
					.append(Utils.indentLNT(2))
					.append(Lnt.END_VAR)
					.append(Constant.LINE_FEED)
					.append(Utils.indentLNT(1))
					.append(Lnt.END_VAR)
					.append(Constant.LINE_FEED)
					.append(Lnt.END_PROCESS)
					.append(Constant.DOUBLE_LINE_FEED)
					.append(Lnt.STANDARD_SEPARATOR)
					.append(Constant.DOUBLE_LINE_FEED);
		}

		void writeMainLnt(final StringBuilder stringBuilder,
						  final int baseIndent)
		{
			stringBuilder.append(Bpmn.PARALLEL_SPLIT_GATEWAY)
					.append(Constant.UNDERSCORE)
					.append(this.identifier);

			super.writeMainLnt(stringBuilder, baseIndent + 11 + this.identifier.length());
		}

		ArrayList<Pair<String, Integer>> reachableOrJoin(final ArrayList<Pair<String, Integer>> visited,
														 final int depth)
		{
			return super.reachableOrJoin(visited, depth);
		}
	}

	/**
	 * Abstract class for JoinGateway
	 */
	abstract class JoinGateway extends Gateway
	{
		JoinGateway(final String identifier,
					final ArrayList<Flow> incomingFlows,
					final ArrayList<Flow> outgoingFlows)
		{
			super(identifier, incomingFlows, outgoingFlows);
		}

		/**
		 * Generates process instantiation for all join gateways.
		 *
		 * @param stringBuilder the builder to which the gateway should be dumped
		 */
		void writeMainLnt(final StringBuilder stringBuilder,
						  final int baseIndent)
		{
			//We assume one outgoing flow
			stringBuilder.append(Constant.SPACE)
					.append(Constant.LEFT_SQUARE_BRACKET);
			int i = 0;
			int nbCharCurrentLine = baseIndent;

			while (i < this.incomingFlows.size())
			{
				final String flowIdentifier = this.incomingFlows().get(i).identifier() + Constant.UNDERSCORE +
						Bpmn.FINISH + Constant.COMA_AND_SPACE;

				if (i == 0)
				{
					nbCharCurrentLine += flowIdentifier.length();
				}
				else
				{
					if (nbCharCurrentLine + flowIdentifier.length() > Lnt.MAX_CHAR_PER_LINE)
					{
						stringBuilder.append(Constant.LINE_FEED)
								.append(Utils.indent(baseIndent));
						nbCharCurrentLine = baseIndent + flowIdentifier.length();
					}
					else
					{
						nbCharCurrentLine += flowIdentifier.length();
					}
				}

				stringBuilder.append(flowIdentifier);
				i++;
			}

			final String outgoingFlowIdentifier = this.outgoingFlows.get(0).identifier() + Constant.UNDERSCORE +
					Bpmn.BEGIN + Constant.RIGHT_SQUARE_BRACKET;

			if (nbCharCurrentLine + outgoingFlowIdentifier.length() > Lnt.MAX_CHAR_PER_LINE)
			{
				stringBuilder.append(Constant.LINE_FEED)
						.append(Utils.indent(baseIndent));
			}

			stringBuilder.append(outgoingFlowIdentifier);
		}

		/**
		 * For a join (generic), if not visited yet, recursive call on the target node of the outgoing flow.
		 *
		 * @param visited the list of visited nodes
		 * @param depth the depth
		 * @return the list of reachable or joins
		 */
		ArrayList<Pair<String, Integer>> reachableOrJoin(final ArrayList<Pair<String, Integer>> visited,
														 final int depth)
		{
			if (pairListContainsIdentifier(visited, this.identifier))
			{
				return new ArrayList<>();
			}

			final ArrayList<Pair<String, Integer>> copy = new ArrayList<>(visited);
			copy.add(Pair.of(this.identifier, depth));
			return this.outgoingFlows.get(0).getTarget().reachableOrJoin(copy, depth);
		}
	}

	/**
	 * Class for OrJoinGateway
	 */
	class OrJoinGateway extends JoinGateway
	{
		private String correspondingOrSplit;

		OrJoinGateway(final String identifier,
					  final ArrayList<Flow> incomingFlows,
					  final ArrayList<Flow> outgoingFlows)
		{
			super(identifier, incomingFlows, outgoingFlows);
			this.correspondingOrSplit = Constant.EMPTY_STRING; //contains the identifier of the corresponding split (if there is one)
		}

		void setCorrespondingOrSplit(final String correspondingOrSplit)
		{
			this.correspondingOrSplit = correspondingOrSplit;
		}

		@Override
		void processLnt(final StringBuilder stringBuilder)
		{
			this.writeLnt(stringBuilder, 21); //TODO Vérifier
		}

		/**
		 * Generates the process for inclusive join gateway.
		 * Takes as input the number of incoming flows.
		 *
		 * @param stringBuilder the builder to which the gateway should be dumped
		 */
		@Override
		void writeLnt(final StringBuilder stringBuilder, final int baseIndent)
		{
			if (isBalanced)
			{
				boolean lineJumped = false;
				final ArrayList<String> incomingFlowsAlphabet = new ArrayList<>();
				int incomingFlowNumericalIdentifier = 1;

				while (incomingFlowNumericalIdentifier <= this.incomingFlows.size())
				{
					final String incomingFlowIdentifier = Bpmn.INCOMING_FLOW_VARIABLE + Constant.UNDERSCORE +
							incomingFlowNumericalIdentifier;
					incomingFlowsAlphabet.add(incomingFlowIdentifier);
					incomingFlowNumericalIdentifier++;
				}

				final ArrayList<ArrayList<String>> allIncomingFlowsCombinations = computeAllCombinations(incomingFlowsAlphabet);
				final String processPreamble = Lnt.PROCESS + Constant.SPACE + Bpmn.INCLUSIVE_MERGE_GATEWAY +
						Constant.UNDERSCORE + this.identifier + Constant.SPACE_AND_LEFT_SQUARE_BRACKET;
				stringBuilder.append(processPreamble);

				incomingFlowNumericalIdentifier = 1;
				int nbCharCurrentLine = processPreamble.length();

				while (incomingFlowNumericalIdentifier <= this.incomingFlows.size())
				{
					final String incomingFlowIdentifier = Bpmn.INCOMING_FLOW_VARIABLE + Constant.UNDERSCORE +
							incomingFlowNumericalIdentifier + Constant.COMA_AND_SPACE;

					if (incomingFlowNumericalIdentifier == 1)
					{
						nbCharCurrentLine += incomingFlowIdentifier.length();
					}
					else
					{
						if (nbCharCurrentLine + incomingFlowIdentifier.length() > Lnt.MAX_CHAR_PER_LINE)
						{
							stringBuilder.append(Constant.LINE_FEED)
									.append(Utils.indent(Constant.PROCESS_INDENT_LENGTH));
							lineJumped = true;
							nbCharCurrentLine = Constant.PROCESS_INDENT_LENGTH + incomingFlowIdentifier.length();
						}
						else
						{
							nbCharCurrentLine += incomingFlowIdentifier.length();
						}
					}

					stringBuilder.append(incomingFlowIdentifier);
					incomingFlowNumericalIdentifier++;
				}

				if (nbCharCurrentLine + 4 > Lnt.MAX_CHAR_PER_LINE)
				{
					stringBuilder.append(Constant.LINE_FEED).append(Utils.indent(Constant.PROCESS_INDENT_LENGTH));
					lineJumped = true;
					nbCharCurrentLine = Constant.PROCESS_INDENT_LENGTH + 4;
				}
				else
				{
					nbCharCurrentLine += 4;
				}

				stringBuilder.append(Bpmn.OUTGOING_FLOW);

				//we add to the alphabet potential additional synchronization points
				if (!allIncomingFlowsCombinations.isEmpty()
					&& !this.correspondingOrSplit.isEmpty())
				{
					int counter = 1;
					stringBuilder.append(Constant.COMA_AND_SPACE);
					nbCharCurrentLine += 2;

					for (ArrayList<String> ignored : allIncomingFlowsCombinations)
					{
						final String identifier = this.identifier + Constant.UNDERSCORE + counter;

						if (nbCharCurrentLine + identifier.length() > Lnt.MAX_CHAR_PER_LINE)
						{
							lineJumped = true;
							stringBuilder.append(Constant.LINE_FEED).append(Utils.indent(Constant.PROCESS_INDENT_LENGTH));
							nbCharCurrentLine = Constant.PROCESS_INDENT_LENGTH + identifier.length();
						}
						else
						{
							nbCharCurrentLine += identifier.length();
						}

						stringBuilder.append(identifier);
						counter++;

						if (counter <= allIncomingFlowsCombinations.size())
						{
							stringBuilder.append(Constant.COMA_AND_SPACE);
							nbCharCurrentLine += 2;
						}
					}
				}

				stringBuilder.append(Constant.COLON_AND_SPACE)
						.append(Lnt.ANY)
						.append(Constant.RIGHT_SQUARE_BRACKET)
						.append(lineJumped ? Constant.LINE_FEED : Constant.SPACE)
						.append(Lnt.IS)
						.append(Constant.LINE_FEED)
						.append(Utils.indentLNT(1))
						.append(Lnt.VAR);

				int variablesCounter = allIncomingFlowsCombinations.size();
				final int minIndent;

				if (allIncomingFlowsCombinations.size() > Constant.MAX_VARS_PER_LINE)
				{
					stringBuilder.append(Constant.LINE_FEED)
							.append(Utils.indentLNT(2));
					nbCharCurrentLine = 6;
					minIndent = 6;
				}
				else
				{
					stringBuilder.append(Constant.SPACE);
					nbCharCurrentLine = 7;
					minIndent = 7;
				}

				while (variablesCounter > 0) //TODO: we generate unnecessary variables
				{
					final String identifier = Bpmn.IDENT_VARIABLE + variablesCounter;

					if (variablesCounter == allIncomingFlowsCombinations.size())
					{
						nbCharCurrentLine += identifier.length();
					}
					else
					{
						if (nbCharCurrentLine + identifier.length() > Lnt.MAX_CHAR_PER_LINE)
						{
							stringBuilder.append(Constant.LINE_FEED)
									.append(Utils.indent(minIndent));
							nbCharCurrentLine = minIndent + identifier.length();
						}
						else
						{
							nbCharCurrentLine += identifier.length();
						}
					}

					stringBuilder.append(identifier);
					variablesCounter--;

					if (variablesCounter > 0)
					{
						stringBuilder.append(Constant.COMA_AND_SPACE);
						nbCharCurrentLine += 2;
					}
				}

				stringBuilder.append(Constant.COLON_AND_SPACE)
						.append(Bpmn.ID_LNT_TYPE);

				if (allIncomingFlowsCombinations.size() > Constant.MAX_VARS_PER_LINE)
				{
					stringBuilder.append(Constant.LINE_FEED)
							.append(Utils.indentLNT(1));
				}
				else
				{
					stringBuilder.append(Constant.SPACE);
				}

				stringBuilder.append(Lnt.IN)
						.append(Constant.LINE_FEED)
						.append(Utils.indentLNT(2))
						.append(Lnt.VAR)
						.append(Constant.SPACE)
						.append(Bpmn.IDENT_VARIABLE)
						.append(Constant.COLON_AND_SPACE)
						.append(Bpmn.ID_LNT_TYPE)
						.append(Constant.SPACE)
						.append(Lnt.IN)
						.append(Constant.LINE_FEED)
						.append(Utils.indentLNT(3))
						.append(Lnt.LOOP)
						.append(Constant.LINE_FEED)
						.append(Utils.indentLNT(4))
						.append(Lnt.ALT)
						.append(Constant.LINE_FEED);

				incomingFlowNumericalIdentifier = 1;
				int counter = 1;

				for (final ArrayList<String> incomingFlowsCombination : allIncomingFlowsCombinations)
				{
					stringBuilder.append(Utils.indentLNT(5));
					int nb2 = 1;

					// add synchronization points if there's a corresponding split
					if (!this.correspondingOrSplit.isEmpty())
					{
						stringBuilder.append(this.identifier)
								.append(Constant.UNDERSCORE)
								.append(counter)
								.append(Lnt.SEQUENTIAL_COMPOSITION_OPERATOR)
								.append(Constant.LINE_FEED)
								.append(Utils.indentLNT(5));

						counter++;
					}

					if (incomingFlowsCombination.size() > 1)
					{
						variablesCounter = allIncomingFlowsCombinations.size();
						stringBuilder.append(Lnt.PAR)
								.append(Constant.LINE_FEED);

						for (String element : incomingFlowsCombination)
						{
							stringBuilder.append(Utils.indentLNT(6))
									.append(element)
									.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
									.append(Lnt.OUT_PARAMETER)
									.append(Bpmn.IDENT_VARIABLE)
									.append(variablesCounter)
									.append(Lnt.SPACED_OF)
									.append(Bpmn.ID_LNT_TYPE)
									.append(Constant.RIGHT_PARENTHESIS);

							variablesCounter--;
							nb2++;

							if (nb2 <= incomingFlowsCombination.size())
							{
								stringBuilder.append(Constant.LINE_FEED)
										.append(Utils.indentLNT(5))
										.append(Lnt.PAR_OPERATOR)
										.append(Constant.LINE_FEED)
								;
							}
						}

						stringBuilder.append(Constant.LINE_FEED)
								.append(Utils.indentLNT(5))
								.append(Lnt.END_PAR);
					}
					else
					{
						stringBuilder.append(incomingFlowsCombination.iterator().next())
								.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
								.append(Lnt.OUT_PARAMETER)
								.append(Bpmn.IDENT_VARIABLE)
								.append(Lnt.SPACED_OF)
								.append(Bpmn.ID_LNT_TYPE)
								.append(Constant.RIGHT_PARENTHESIS);
					}

					incomingFlowNumericalIdentifier++;

					if (incomingFlowNumericalIdentifier <= allIncomingFlowsCombinations.size())
					{
						stringBuilder.append(Constant.LINE_FEED)
								.append(Utils.indentLNT(4))
								.append(Lnt.ALT_OPERATOR)
								.append(Constant.LINE_FEED)
						;
					}
				}

				stringBuilder.append(Constant.LINE_FEED)
						.append(Utils.indentLNT(4))
						.append(Lnt.END_ALT)
						.append(Lnt.SEQUENTIAL_COMPOSITION_OPERATOR)
						.append(Constant.LINE_FEED)
						.append(Utils.indentLNT(4))
						.append(Bpmn.OUTGOING_FLOW)
						.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
						.append(Lnt.OUT_PARAMETER)
						.append(Bpmn.IDENT_VARIABLE)
						.append(Lnt.SPACED_OF)
						.append(Bpmn.ID_LNT_TYPE)
						.append(Constant.RIGHT_PARENTHESIS)
						.append(Constant.LINE_FEED)
						.append(Utils.indentLNT(3))
						.append(Lnt.END_LOOP)
						.append(Constant.LINE_FEED)
						.append(Utils.indentLNT(2))
						.append(Lnt.END_VAR)
						.append(Constant.LINE_FEED);
			}
			else
			{
				final String processIdentifier = Lnt.PROCESS + Constant.SPACE + Bpmn.INCLUSIVE_MERGE_GATEWAY +
						Constant.UNDERSCORE + this.identifier + Constant.SPACE_AND_LEFT_SQUARE_BRACKET;
				stringBuilder.append(processIdentifier);
				int nb = 1;
				int nbCharCurrentLine = processIdentifier.length();
				boolean lineJumped = false;

				while (nb <= this.incomingFlows.size())
				{
					final String incFlowIdentifier = Bpmn.INCOMING_FLOW_VARIABLE + Constant.UNDERSCORE + nb +
							Constant.COMA_AND_SPACE;

					if (nb == 1)
					{
						nbCharCurrentLine += incFlowIdentifier.length();
					}
					else
					{
						if (nbCharCurrentLine + incFlowIdentifier.length() > Lnt.MAX_CHAR_PER_LINE)
						{
							lineJumped = true;
							stringBuilder.append(Constant.LINE_FEED)
									.append(Utils.indent(Constant.PROCESS_INDENT_LENGTH));
							nbCharCurrentLine = Constant.PROCESS_INDENT_LENGTH + incFlowIdentifier.length();
						}
						else
						{
							nbCharCurrentLine += incFlowIdentifier.length();
						}
					}

					stringBuilder.append(incFlowIdentifier);
					nb++;
				}

				if (nbCharCurrentLine + 5 > Lnt.MAX_CHAR_PER_LINE)
				{
					lineJumped = true;
					stringBuilder.append(Constant.LINE_FEED)
							.append(Utils.indent(Constant.PROCESS_INDENT_LENGTH));
					nbCharCurrentLine = Constant.PROCESS_INDENT_LENGTH + 5;
				}
				else
				{
					nbCharCurrentLine += 5;
				}

				stringBuilder.append(Bpmn.OUTGOING_FLOW)
						.append(Constant.COMA_AND_SPACE);

				if (nbCharCurrentLine + 12 > Lnt.MAX_CHAR_PER_LINE)
				{
					lineJumped = true;
					stringBuilder.append(Constant.LINE_FEED).append(Utils.indent(Constant.PROCESS_INDENT_LENGTH));
					nbCharCurrentLine = Constant.PROCESS_INDENT_LENGTH + 12;
				}
				else
				{
					nbCharCurrentLine += 12;
				}

				stringBuilder.append(Bpmn.MOVE_ON_FLOW)
						.append(Constant.COLON_AND_SPACE)
						.append(Lnt.ANY)
						.append(Constant.RIGHT_SQUARE_BRACKET_AND_SPACE);

				final int nbCharToConsider = lineJumped ? 14 : 17;

				if (nbCharCurrentLine + nbCharToConsider > Lnt.MAX_CHAR_PER_LINE)
				{
					lineJumped = true;
					stringBuilder.append(Constant.LINE_FEED).append(Utils.indent(Constant.PROCESS_INDENT_LENGTH));
				}

				stringBuilder.append(Constant.LEFT_PARENTHESIS)
						.append(Bpmn.MERGE_ID_VARIABLE)
						.append(Constant.COLON_AND_SPACE)
						.append(Bpmn.ID_LNT_TYPE)
						.append(Constant.RIGHT_PARENTHESIS)
						.append(lineJumped ? Constant.LINE_FEED : Constant.SPACE)
						.append(Lnt.IS)
						.append(Constant.LINE_FEED)
						.append(Utils.indentLNT(1))
						.append(Lnt.VAR)
						.append(Constant.SPACE)
						.append(Bpmn.MERGE_STATUS_VARIABLE)
						.append(Constant.COLON_AND_SPACE)
						.append(Lnt.BOOLEAN_TYPE)
						.append(Constant.COMA_AND_SPACE)
						.append(Bpmn.IDENT_VARIABLE)
						.append(Constant.COLON_AND_SPACE)
						.append(Bpmn.ID_LNT_TYPE)
						.append(Constant.SPACE)
						.append(Lnt.IN)
						.append(Constant.LINE_FEED)
						.append(Utils.indentLNT(2))
						.append(Lnt.LOOP)
						.append(Constant.LINE_FEED)
						.append(Utils.indentLNT(3))
						.append(Bpmn.MERGE_STATUS_VARIABLE)
						.append(Lnt.SPACED_VARIABLE_ASSIGNATION_OPERATOR)
						.append(Lnt.FALSE)
						.append(Lnt.SEQUENTIAL_COMPOSITION_OPERATOR)
						.append(Constant.LINE_FEED)
						.append(Utils.indentLNT(3))
						.append(Lnt.WHILE)
						.append(Constant.SPACE)
						.append(Bpmn.MERGE_STATUS_VARIABLE)
						.append(Lnt.SPACED_EQUALS_OPERATOR)
						.append(Lnt.FALSE)
						.append(Constant.SPACE)
						.append(Lnt.LOOP)
						.append(Constant.LINE_FEED)
						.append(Utils.indentLNT(4))
						.append(Lnt.ALT)
						.append(Constant.LINE_FEED);

				nb = 1;

				while (nb <= this.incomingFlows.size())
				{
					stringBuilder.append(Utils.indentLNT(5))
							.append(Bpmn.INCOMING_FLOW_VARIABLE)
							.append(Constant.UNDERSCORE)
							.append(nb)
							.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
							.append(Lnt.OUT_PARAMETER)
							.append(Bpmn.IDENT_VARIABLE)
							.append(Lnt.SPACED_OF)
							.append(Bpmn.ID_LNT_TYPE)
							.append(Constant.RIGHT_PARENTHESIS);

					nb++;

					if (nb <= this.incomingFlows.size())
					{
						stringBuilder.append(Constant.LINE_FEED)
								.append(Utils.indentLNT(4))
								.append(Lnt.ALT_OPERATOR)
								.append(Constant.LINE_FEED)
						;
					}
				}

				stringBuilder.append(Constant.LINE_FEED)
						.append(Utils.indentLNT(4))
						.append(Lnt.ALT_OPERATOR)
						.append(Constant.LINE_FEED)
						.append(Utils.indentLNT(5))
						.append(Bpmn.MOVE_ON_FLOW)
						.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
						.append(Bpmn.MERGE_ID_VARIABLE)
						.append(Constant.RIGHT_PARENTHESIS)
						.append(Lnt.SEQUENTIAL_COMPOSITION_OPERATOR)
						.append(Constant.LINE_FEED)
						.append(Utils.indentLNT(5))
						.append(Bpmn.MERGE_STATUS_VARIABLE)
						.append(Lnt.SPACED_VARIABLE_ASSIGNATION_OPERATOR)
						.append(Lnt.TRUE)
						.append(Constant.LINE_FEED)
						.append(Utils.indentLNT(4))
						.append(Lnt.END_ALT)
						.append(Constant.LINE_FEED)
						.append(Utils.indentLNT(3))
						.append(Lnt.END_LOOP)
						.append(Lnt.SEQUENTIAL_COMPOSITION_OPERATOR)
						.append(Constant.LINE_FEED)
						.append(Utils.indentLNT(3))
						.append(Bpmn.OUTGOING_FLOW)
						.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
						.append(Lnt.OUT_PARAMETER)
						.append(Bpmn.IDENT_VARIABLE)
						.append(Lnt.SPACED_OF)
						.append(Bpmn.ID_LNT_TYPE)
						.append(Constant.RIGHT_PARENTHESIS)
						.append(Constant.LINE_FEED)
						.append(Utils.indentLNT(2))
						.append(Lnt.END_LOOP)
						.append(Constant.LINE_FEED);
			}

			stringBuilder.append(Utils.indentLNT(1))
					.append(Lnt.END_VAR)
					.append(Constant.LINE_FEED)
					.append(Lnt.END_PROCESS)
					.append(Constant.DOUBLE_LINE_FEED)
					.append(Lnt.STANDARD_SEPARATOR)
					.append(Constant.DOUBLE_LINE_FEED);
		}

		//Generates process instantiation for main LNT process
		void writeMainLnt(final StringBuilder stringBuilder,
						  final int baseIndent)
		{
			if (isBalanced)
			{
				if (!this.correspondingOrSplit.isEmpty())
				{
					final ArrayList<String> incomingFlowsAlphabet = new ArrayList<>();
					int nb = 1;

					while (nb <= this.incomingFlows.size())
					{
						incomingFlowsAlphabet.add(Bpmn.INCOMING_FLOW_VARIABLE + Constant.UNDERSCORE + nb);
						nb++;
					}

					final ArrayList<ArrayList<String>> allCombinations = computeAllCombinations(incomingFlowsAlphabet);
					final int nbCombi = allCombinations.size();
					int nbCharCurrentLine = baseIndent;

					//We dump synchronization points
					if (nbCombi > 0)
					{
						int counter = 1;

						for (ArrayList<String> ignored : allCombinations)
						{
							final String identifier = this.identifier + Constant.UNDERSCORE + counter;

							if (nbCharCurrentLine + identifier.length() > Lnt.MAX_CHAR_PER_LINE)
							{
								stringBuilder.append(Constant.LINE_FEED)
										.append(Utils.indent(baseIndent));
								nbCharCurrentLine = baseIndent + identifier.length();
							}
							else
							{
								nbCharCurrentLine += identifier.length();
							}

							stringBuilder.append(identifier);
							counter++;

							if (counter <= nbCombi)
							{
								stringBuilder.append(Constant.COMA_AND_SPACE);
								nbCharCurrentLine += 2;
							}
						}

						stringBuilder.append(Constant.SPACE)
								.append(Lnt.PATTERN_MATCHING_OPERATOR)
								.append(Constant.LINE_FEED)
								.append(Utils.indent(baseIndent + 3));

						nbCharCurrentLine = baseIndent + 3;
					}

					//Process call + alphabet
					final String processIdentifier = Bpmn.INCLUSIVE_MERGE_GATEWAY + Constant.UNDERSCORE +
							this.identifier + Constant.SPACE_AND_LEFT_SQUARE_BRACKET;
					final int minIndent = nbCharCurrentLine + processIdentifier.length();
					stringBuilder.append(processIdentifier);
					nbCharCurrentLine = baseIndent + processIdentifier.length();
					int i = 0;

					while (i < this.incomingFlows.size())
					{
						final String incFlowIdentifier = this.incomingFlows.get(i).identifier() + Constant.UNDERSCORE +
								Bpmn.FINISH + Constant.COMA_AND_SPACE;

						if (i == 0)
						{
							nbCharCurrentLine += incFlowIdentifier.length();
						}
						else
						{
							if (nbCharCurrentLine + incFlowIdentifier.length() > Lnt.MAX_CHAR_PER_LINE)
							{
								stringBuilder.append(Constant.LINE_FEED)
										.append(Utils.indent(minIndent));
								nbCharCurrentLine = minIndent + incFlowIdentifier.length();
							}
							else
							{
								nbCharCurrentLine += incFlowIdentifier.length();
							}
						}

						stringBuilder.append(incFlowIdentifier);
						i++;
					}

					final String outFlowIdentifier = this.outgoingFlows.get(0).identifier() + Constant.UNDERSCORE +
							Bpmn.BEGIN;

					if (nbCharCurrentLine + outFlowIdentifier.length() + 1 > Lnt.MAX_CHAR_PER_LINE)
					{
						stringBuilder.append(Constant.LINE_FEED)
								.append(Utils.indent(minIndent));
						nbCharCurrentLine = minIndent + outFlowIdentifier.length();
					}
					else
					{
						nbCharCurrentLine += outFlowIdentifier.length();
					}

					stringBuilder.append(outFlowIdentifier);

					if (nbCombi > 0)
					{
						int counter = 1;
						stringBuilder.append(Constant.COMA_AND_SPACE);
						nbCharCurrentLine += 2;

						for (ArrayList<String> ignored : allCombinations)
						{
							final String identifier = this.identifier + Constant.UNDERSCORE + counter;

							if (nbCharCurrentLine + identifier.length() > Lnt.MAX_CHAR_PER_LINE)
							{
								stringBuilder.append(Constant.LINE_FEED)
										.append(Utils.indent(minIndent));
								nbCharCurrentLine = minIndent + identifier.length();
							}
							else
							{
								nbCharCurrentLine += identifier.length();
							}

							stringBuilder.append(identifier);
							counter++;

							if (counter <= nbCombi)
							{
								stringBuilder.append(Constant.COMA_AND_SPACE);
								nbCharCurrentLine += 2;
							}
						}
					}

					stringBuilder.append(Constant.RIGHT_SQUARE_BRACKET);
				}
				else
				{
					stringBuilder.append(Bpmn.INCLUSIVE_MERGE_GATEWAY)
							.append(Constant.UNDERSCORE)
							.append(this.identifier);

					super.writeMainLnt(stringBuilder, baseIndent);
				}
			}
			else
			{
				final String processIdentifier = Bpmn.INCLUSIVE_MERGE_GATEWAY + Constant.UNDERSCORE + this.identifier +
						Constant.SPACE_AND_LEFT_SQUARE_BRACKET;
				stringBuilder.append(processIdentifier);
				int nbCharCurrentLine = baseIndent + processIdentifier.length();
				final int minIndent = nbCharCurrentLine + 1;

				//We assume one outgoing flow
				final int nbInc = this.incomingFlows.size();
				int i = 0;

				while (i < nbInc)
				{
					final String incomingFlowIdentifier = this.incomingFlows.get(i).identifier() + Constant.UNDERSCORE +
							Bpmn.FINISH + Constant.COMA_AND_SPACE;

					if (i == 0)
					{
						nbCharCurrentLine += incomingFlowIdentifier.length();
					}
					else
					{
						if (nbCharCurrentLine + incomingFlowIdentifier.length() > Lnt.MAX_CHAR_PER_LINE)
						{
							stringBuilder.append(Constant.LINE_FEED)
									.append(Utils.indent(minIndent));
							nbCharCurrentLine = minIndent + incomingFlowIdentifier.length();
						}
						else
						{
							nbCharCurrentLine += incomingFlowIdentifier.length();
						}
					}

					stringBuilder.append(incomingFlowIdentifier);
					i++;
				}

				final String outgoingFlowIdentifier = this.outgoingFlows.get(0).identifier() + Constant.UNDERSCORE +
						Bpmn.BEGIN + Constant.COMA_AND_SPACE;

				if (nbCharCurrentLine + outgoingFlowIdentifier.length() > Lnt.MAX_CHAR_PER_LINE)
				{
					stringBuilder.append(Constant.LINE_FEED).append(Utils.indent(minIndent));
					nbCharCurrentLine = minIndent + outgoingFlowIdentifier.length();
				}
				else
				{
					nbCharCurrentLine += outgoingFlowIdentifier.length();
				}

				stringBuilder.append(outgoingFlowIdentifier);

				if (nbCharCurrentLine + 7 > Lnt.MAX_CHAR_PER_LINE)
				{
					stringBuilder.append(Constant.LINE_FEED).append(Utils.indent(minIndent));
					nbCharCurrentLine = minIndent + 7;
				}
				else
				{
					nbCharCurrentLine += 7;
				}

				stringBuilder.append(Bpmn.MOVE_ON_FLOW)
						.append(Constant.RIGHT_SQUARE_BRACKET);

				if (nbCharCurrentLine + this.identifier.length() + 3 > Lnt.MAX_CHAR_PER_LINE)
				{
					stringBuilder.append(Constant.LINE_FEED)
							.append(Utils.indent(minIndent));
				}

				stringBuilder.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
						.append(this.identifier)
						.append(Constant.RIGHT_PARENTHESIS);
			}
		}

		/**
		 * For an or join, if not visited yet, recursive call on the target node of the outgoing flow.
		 * We store the result and we decrease the depth.
		 *
		 * @param visited the list of visited nodes
		 * @param depth the depth
		 * @return the list of reachable or joins
		 */
		ArrayList<Pair<String, Integer>> reachableOrJoin(final ArrayList<Pair<String, Integer>> visited,
														 final int depth)
		{
			if (pairListContainsIdentifier(visited, this.identifier))
			{
				return new ArrayList<>();
			}

			final ArrayList<Pair<String, Integer>> temp = new ArrayList<>(visited);
			temp.add(Pair.of(this.identifier, depth));

			final ArrayList<Pair<String, Integer>> result = new ArrayList<>();
			result.add(Pair.of(this.identifier, depth));
			result.addAll(this.outgoingFlows.get(0).getTarget().reachableOrJoin(temp, depth - 1));

			return result;
		}
	}

	/**
	 * Class for XOrJoinGateway
	 */
	class XOrJoinGateway extends JoinGateway
	{
		XOrJoinGateway(final String identifier,
					   final ArrayList<Flow> incomingFlows,
					   final ArrayList<Flow> outgoingFlows)
		{
			super(identifier, incomingFlows, outgoingFlows);
		}

		@Override
		void processLnt(final StringBuilder stringBuilder)
		{
			this.writeLnt(stringBuilder, 21); //TODO Vérifier
		}

		/**
		 * Generates the process for exclusive join gateway.
		 * Takes as input the number of incoming flows.
		 *
		 * @param stringBuilder the builder to which the gateway should be dumped
		 */
		@Override
		void writeLnt(final StringBuilder stringBuilder,
					  final int baseIndent)
		{
			boolean lineJumped = false;
			final String processIdentifier = Lnt.PROCESS + Constant.SPACE + Bpmn.EXCLUSIVE_MERGE_GATEWAY +
					Constant.UNDERSCORE + this.identifier + Constant.SPACE_AND_LEFT_SQUARE_BRACKET;
			stringBuilder.append(processIdentifier);
			int nbCharCurrentLine = processIdentifier.length();
			int nb = 1;

			while (nb <= this.incomingFlows.size())
			{
				final String incFlowIdentifier = Bpmn.INCOMING_FLOW_VARIABLE + Constant.UNDERSCORE + nb + Constant.COMA_AND_SPACE;

				if (nb == 1)
				{
					nbCharCurrentLine += incFlowIdentifier.length();
				}
				else
				{
					if (nbCharCurrentLine + incFlowIdentifier.length() > Lnt.MAX_CHAR_PER_LINE)
					{
						lineJumped = true;
						stringBuilder.append(Constant.LINE_FEED)
								.append(Utils.indent(Constant.PROCESS_INDENT_LENGTH));
						nbCharCurrentLine = Constant.PROCESS_INDENT_LENGTH + incFlowIdentifier.length();
					}
					else
					{
						nbCharCurrentLine += incFlowIdentifier.length();
					}
				}

				stringBuilder.append(incFlowIdentifier);
				nb++;
			}

			final String outFlowIdentifier = Bpmn.OUTGOING_FLOW + Constant.COLON_AND_SPACE + Lnt.ANY +
					Constant.RIGHT_SQUARE_BRACKET;
			final int nbCharToConsider = lineJumped ? outFlowIdentifier.length() : outFlowIdentifier.length() + 3;

			if (nbCharToConsider + nbCharCurrentLine > Lnt.MAX_CHAR_PER_LINE)
			{
				stringBuilder.append(Constant.LINE_FEED)
						.append(Utils.indent(Constant.PROCESS_INDENT_LENGTH));
			}

			stringBuilder.append(outFlowIdentifier)
					.append(lineJumped ? Constant.LINE_FEED : Constant.SPACE)
					.append(Lnt.IS)
					.append(Constant.LINE_FEED)
					.append(Utils.indentLNT(1))
					.append(Lnt.VAR)
					.append(Constant.SPACE)
					.append(Bpmn.IDENT_VARIABLE)
					.append(Constant.COLON_AND_SPACE)
					.append(Bpmn.ID_LNT_TYPE)
					.append(Constant.SPACE)
					.append(Lnt.IN)
					.append(Constant.LINE_FEED)
					.append(Utils.indentLNT(2))
					.append(Lnt.LOOP)
					.append(Constant.LINE_FEED)
					.append(Utils.indentLNT(3))
					.append(Lnt.ALT)
					.append(Constant.LINE_FEED);

			nb = 1;

			while (nb <= this.incomingFlows.size())
			{
				stringBuilder.append(Utils.indentLNT(4))
						.append(Bpmn.INCOMING_FLOW_VARIABLE)
						.append(Constant.UNDERSCORE)
						.append(nb)
						.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
						.append(Lnt.OUT_PARAMETER)
						.append(Bpmn.IDENT_VARIABLE)
						.append(Lnt.SPACED_OF)
						.append(Bpmn.ID_LNT_TYPE)
						.append(Constant.RIGHT_PARENTHESIS);

				nb++;

				if (nb <= this.incomingFlows.size())
				{
					stringBuilder.append(Constant.LINE_FEED)
							.append(Utils.indentLNT(3))
							.append(Lnt.ALT_OPERATOR)
							.append(Constant.LINE_FEED);
				}
			}

			stringBuilder.append(Constant.LINE_FEED)
					.append(Utils.indentLNT(3))
					.append(Lnt.END_ALT)
					.append(Lnt.SEQUENTIAL_COMPOSITION_OPERATOR)
					.append(Constant.LINE_FEED)
					.append(Utils.indentLNT(3))
					.append(Bpmn.OUTGOING_FLOW)
					.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
					.append(Lnt.OUT_PARAMETER)
					.append(Bpmn.IDENT_VARIABLE)
					.append(Lnt.SPACED_OF)
					.append(Bpmn.ID_LNT_TYPE)
					.append(Constant.RIGHT_PARENTHESIS)
					.append(Constant.LINE_FEED)
					.append(Utils.indentLNT(2))
					.append(Lnt.END_LOOP)
					.append(Constant.LINE_FEED)
					.append(Utils.indentLNT(1))
					.append(Lnt.END_VAR)
					.append(Constant.LINE_FEED)
					.append(Lnt.END_PROCESS)
					.append(Constant.DOUBLE_LINE_FEED)
					.append(Lnt.STANDARD_SEPARATOR)
					.append(Constant.DOUBLE_LINE_FEED);
		}

		void writeMainLnt(final StringBuilder stringBuilder,
						  final int baseIndent)
		{
			stringBuilder.append(Bpmn.EXCLUSIVE_MERGE_GATEWAY)
					.append(Constant.UNDERSCORE)
					.append(this.identifier);

			super.writeMainLnt(stringBuilder, baseIndent + this.identifier.length() + 10);
		}

		ArrayList<Pair<String, Integer>> reachableOrJoin(final ArrayList<Pair<String, Integer>> visited,
														 final int depth)
		{
			return super.reachableOrJoin(visited, depth);
		}
	}

	/**
	 * Class for AndJoinGateway
	 */
	class AndJoinGateway extends JoinGateway
	{
		AndJoinGateway(final String identifier,
					   final ArrayList<Flow> incomingFlows,
					   final ArrayList<Flow> outgoingFlows)
		{
			super(identifier, incomingFlows, outgoingFlows);
		}

		@Override
		void processLnt(final StringBuilder stringBuilder)
		{
			this.writeLnt(stringBuilder, 21); //TODO Vérifier
		}

		/**
		 * Generates the process for parallel join gateway.
		 * Takes as input the number of incoming flows.
		 *
		 * @param stringBuilder the builder to which the gateway should be dumped
		 */
		@Override
		void writeLnt(final StringBuilder stringBuilder,
					  final int baseIndent)
		{
			boolean lineJumped = false;
			final String toWrite = Lnt.PROCESS + Constant.SPACE + Bpmn.PARALLEL_MERGE_GATEWAY + Constant.UNDERSCORE +
					this.identifier + Constant.SPACE_AND_LEFT_SQUARE_BRACKET;
			stringBuilder.append(toWrite);
			int nb = 1;

			int nbCharCurrentLine = toWrite.length();

			while (nb <= this.incomingFlows.size())
			{
				final String flowName = Bpmn.INCOMING_FLOW_VARIABLE + Constant.UNDERSCORE + nb + Constant.COMA_AND_SPACE;

				if (nbCharCurrentLine + flowName.length() > Lnt.MAX_CHAR_PER_LINE)
				{
					lineJumped = true;
					stringBuilder.append(Constant.LINE_FEED)
							.append(Utils.indent(Constant.PROCESS_INDENT_LENGTH));
					nbCharCurrentLine = Constant.PROCESS_INDENT_LENGTH;
				}
				else
				{
					nbCharCurrentLine += flowName.length();
				}

				stringBuilder.append(flowName);
				nb++;
			}

			final String outFlowName = Bpmn.OUTGOING_FLOW + Constant.COLON_AND_SPACE + Lnt.ANY +
					Constant.RIGHT_SQUARE_BRACKET;
			final int nbCharToConsider = lineJumped ? outFlowName.length() : outFlowName.length() + 3;

			if (nbCharCurrentLine + nbCharToConsider > Lnt.MAX_CHAR_PER_LINE)
			{
				lineJumped = true;
				stringBuilder.append(Constant.LINE_FEED)
						.append(Utils.indent(Constant.PROCESS_INDENT_LENGTH));
			}

			stringBuilder.append(outFlowName)
					.append(lineJumped ? Constant.LINE_FEED : Constant.SPACE)
					.append(Lnt.IS)
					.append(Constant.LINE_FEED)
					.append(Utils.indentLNT(1))
					.append(Lnt.VAR);

			int variablesCounter = this.incomingFlows.size();
			final int minIndent;

			if (this.incomingFlows.size() > Constant.MAX_VARS_PER_LINE)
			{
				stringBuilder.append(Constant.LINE_FEED)
						.append(Utils.indentLNT(2));
				nbCharCurrentLine = 6;
				minIndent = 6;
			}
			else
			{
				stringBuilder.append(Constant.SPACE);
				nbCharCurrentLine = 7;
				minIndent = 7;
			}

			while (variablesCounter > 0)
			{
				final String identifier = Bpmn.IDENT_VARIABLE + variablesCounter;

				if (variablesCounter != this.incomingFlows.size())
				{
					if (nbCharCurrentLine + identifier.length() > Lnt.MAX_CHAR_PER_LINE)
					{
						stringBuilder.append(Constant.LINE_FEED)
								.append(Utils.indent(minIndent));
						nbCharCurrentLine = minIndent + identifier.length();
					}
					else
					{
						nbCharCurrentLine += identifier.length();
					}
				}
				else
				{
					nbCharCurrentLine += identifier.length();
				}

				stringBuilder.append(identifier);
				variablesCounter--;

				if (variablesCounter > 0)
				{
					stringBuilder.append(Constant.COMA_AND_SPACE);
					nbCharCurrentLine += 2;
				}
			}

			stringBuilder.append(Constant.COLON_AND_SPACE)
					.append(Bpmn.ID_LNT_TYPE);

			if (this.incomingFlows.size() > Constant.MAX_VARS_PER_LINE)
			{
				stringBuilder.append(Constant.LINE_FEED)
						.append(Utils.indentLNT(1));
			}
			else
			{
				stringBuilder.append(Constant.SPACE);
			}

			stringBuilder.append(Lnt.IN)
					.append(Constant.LINE_FEED)
					.append(Utils.indentLNT(2))
					.append(Lnt.VAR)
					.append(Constant.SPACE)
					.append(Bpmn.IDENT_VARIABLE)
					.append(Constant.COLON_AND_SPACE)
					.append(Bpmn.ID_LNT_TYPE)
					.append(Constant.SPACE)
					.append(Lnt.IN)
					.append(Constant.LINE_FEED)
					.append(Utils.indentLNT(3))
					.append(Lnt.LOOP)
					.append(Constant.LINE_FEED)
					.append(Utils.indentLNT(4))
					.append(Lnt.PAR)
					.append(Constant.LINE_FEED);

			nb = 1;
			variablesCounter = this.incomingFlows.size();

			while (nb <= this.incomingFlows.size())
			{
				stringBuilder.append(Utils.indentLNT(5))
						.append(Bpmn.INCOMING_FLOW_VARIABLE)
						.append(Constant.UNDERSCORE)
						.append(nb)
						.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
						.append(Lnt.OUT_PARAMETER)
						.append(Bpmn.IDENT_VARIABLE)
						.append(variablesCounter)
						.append(Lnt.SPACED_OF)
						.append(Bpmn.ID_LNT_TYPE)
						.append(Constant.RIGHT_PARENTHESIS);

				variablesCounter--;
				nb++;

				if (nb <= this.incomingFlows.size())
				{
					stringBuilder.append(Constant.LINE_FEED)
							.append(Utils.indentLNT(4))
							.append(Lnt.PAR_OPERATOR)
							.append(Constant.LINE_FEED);
					//stringBuilder.append(Utils.indentLNT(4));
				}
			}

			stringBuilder.append(Constant.LINE_FEED)
					.append(Utils.indentLNT(4))
					.append(Lnt.END_PAR)
					.append(Lnt.SEQUENTIAL_COMPOSITION_OPERATOR)
					.append(Constant.LINE_FEED)
					.append(Utils.indentLNT(4))
					.append(Bpmn.OUTGOING_FLOW)
					.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
					.append(Lnt.OUT_PARAMETER)
					.append(Bpmn.IDENT_VARIABLE)
					.append(Lnt.SPACED_OF)
					.append(Bpmn.ID_LNT_TYPE)
					.append(Constant.RIGHT_PARENTHESIS)
					.append(Constant.LINE_FEED)
					.append(Utils.indentLNT(3))
					.append(Lnt.END_LOOP)
					.append(Constant.LINE_FEED)
					.append(Utils.indentLNT(2))
					.append(Lnt.END_VAR)
					.append(Constant.LINE_FEED)
					.append(Utils.indentLNT(1))
					.append(Lnt.END_VAR)
					.append(Constant.LINE_FEED)
					.append(Lnt.END_PROCESS)
					.append(Constant.DOUBLE_LINE_FEED)
					.append(Lnt.STANDARD_SEPARATOR)
					.append(Constant.DOUBLE_LINE_FEED);
		}

		void writeMainLnt(final StringBuilder stringBuilder,
						  final int baseIndent)
		{
			stringBuilder.append(Bpmn.PARALLEL_MERGE_GATEWAY)
					.append(Constant.UNDERSCORE)
					.append(this.identifier);

			super.writeMainLnt(stringBuilder, baseIndent + 10 + this.identifier.length());
		}

		ArrayList<Pair<String, Integer>> reachableOrJoin(final ArrayList<Pair<String, Integer>> visited,
														 final int depth)
		{
			return super.reachableOrJoin(visited, depth);
		}
	}

	/**
	 * Class for Processes described in PIF.
	 * Attributes: a name, a list of nodes, a list of flows, an initial node, a list of final nodes.
	 */
	class Process
	{
		private final ArrayList<Node> nodes;
		private final ArrayList<Node> finals;
		private final ArrayList<Flow> flows;
		private String name;
		private Node initial;

		Process()
		{
			this.name = Constant.EMPTY_STRING;
			this.nodes = new ArrayList<>();
			this.flows = new ArrayList<>();
			this.initial = null;
			this.finals = new ArrayList<>();
		}

		String name()
		{
			return this.name;
		}

		Node getNode(final String identifier)
		{
			if (identifier.equals(this.initial.identifier()))
			{
				return this.initial;
			}

			for (final Node node : this.finals)
			{
				if (node.identifier().equals(identifier))
				{
					return node;
				}
			}

			for (final Node node : this.nodes)
			{
				if (node.identifier().equals(identifier))
				{
					return node;
				}
			}

			throw new IllegalStateException("No node found with identifier \"" + identifier + "\".");
		}

		void addFlow(final Flow flow)
		{
			if (flow.getSource().identifier().equals(this.initial.identifier()))
			{
				this.initial.addOutgoingFlow(flow);
			}

			for (final Node node : this.finals)
			{
				if (flow.getTarget().identifier().equals(node.identifier()))
				{
					node.addIncomingFlow(flow);
				}
			}

			for (final Node node : this.nodes)
			{
				if (flow.getSource().identifier().equals(node.identifier()))
				{
					node.addOutgoingFlow(flow);
				}
				if (flow.getTarget().identifier().equals(node.identifier()))
				{
					node.addIncomingFlow(flow);
				}
			}
		}

		/**
		 * Computes the process alphabet
		 *
		 * @return the process alphabet
		 */
		ArrayList<String> getProcessAlphabet()
		{
			final ArrayList<String> alphabet = new ArrayList<>();

			for (final Node node : this.nodes)
			{
				alphabet.addAll(node.alpha());
			}

			return alphabet;
		}

		/**
		 * This method applies a pre-processing to the whole process
		 * and computes correspondences between or splits/merges.
		 */
		void reachableOrJoin()
		{
			//We traverse all process nodes and call this computation for all inclusive splits
			for (final Node node : this.nodes)
			{
				if (node instanceof OrSplitGateway)
				{
					final ArrayList<Pair<String, Integer>> resTmp = node.reachableOrJoin(new ArrayList<>(), -1);
					final String res = analyzeReachabilityResults(resTmp, node.outgoingFlows().size());

					if (!res.isEmpty())
					{
						((OrSplitGateway) node).setCorrespOrJoin(res); //we update the split attribute
						final Node joinNode = this.getNode(res); //we retrieve the object corresponding to the join id
						((OrJoinGateway) joinNode).setCorrespondingOrSplit(node.identifier()); //we update the join attribute
					}
				}
			}
		}

		/**
		 * Computes the list with the additional synchronization points for corresponding or splits/joins.
		 *
		 * @return the list of synchronisation points
		 */
		ArrayList<String> computeAdditionalSynchroPoints()
		{
			final ArrayList<String> res = new ArrayList<>();

			for (final Node node : this.nodes)
			{
				if (node instanceof OrSplitGateway)
				{
					final String identifierToUse;

					if (isBalanced)
					{
						if (((OrSplitGateway) node).getCorrespOrJoin().isEmpty())
						{
							continue;
						}

						identifierToUse = ((OrSplitGateway) node).getCorrespOrJoin();
					}
					else
					{
						identifierToUse = node.identifier();
					}

					final ArrayList<String> outgoingFlowsAlphabet = new ArrayList<>();
					int nb = 1;

					while (nb <= node.outgoingFlows().size())
					{
						outgoingFlowsAlphabet.add(Bpmn.OUTGOING_FLOW + Constant.UNDERSCORE + nb);
						nb++;
					}

					final ArrayList<ArrayList<String>> allOutgoingFlowsCombinations = computeAllCombinations(outgoingFlowsAlphabet);

					for (int counter = 1; counter <= allOutgoingFlowsCombinations.size(); counter++)
					{
						res.add(identifierToUse + Constant.UNDERSCORE + counter);
					}
				}
			}

			return res;
		}

		Pair<String, Integer> getFlowMsgsAndLineLength(final int minIndent,
													   final int nbCharsAlreadyWritten)
		{
			final StringBuilder flowBuilder = new StringBuilder();
			int counter = 1;
			int nbCharCurrentLine = nbCharsAlreadyWritten;

			for (Flow flow : this.flows)
			{
				final String beginFlowIdentifier = flow.identifier() + Constant.UNDERSCORE + Bpmn.BEGIN + Constant.COMA_AND_SPACE;
				final String finishFlowIdentifier = flow.identifier() + Constant.UNDERSCORE + Bpmn.FINISH;

				if (counter == 1)
				{
					nbCharCurrentLine += beginFlowIdentifier.length();
				}
				else
				{
					if (nbCharCurrentLine + beginFlowIdentifier.length() > Lnt.MAX_CHAR_PER_LINE)
					{
						flowBuilder.append(Constant.LINE_FEED)
								.append(Utils.indent(minIndent));
						nbCharCurrentLine = minIndent + beginFlowIdentifier.length();
					}
					else
					{
						nbCharCurrentLine += beginFlowIdentifier.length();
					}
				}

				flowBuilder.append(beginFlowIdentifier);

				if (nbCharCurrentLine + finishFlowIdentifier.length() > Lnt.MAX_CHAR_PER_LINE)
				{
					flowBuilder.append(Constant.LINE_FEED)
							.append(Utils.indent(minIndent));
					nbCharCurrentLine = minIndent + finishFlowIdentifier.length();
				}
				else
				{
					nbCharCurrentLine += finishFlowIdentifier.length();
				}

				flowBuilder.append(finishFlowIdentifier);

				counter++;

				if (counter <= this.flows.size())
				{
					flowBuilder.append(Constant.COMA_AND_SPACE);
					nbCharCurrentLine += 2;
				}
			}

			return Pair.of(flowBuilder.toString(), nbCharCurrentLine);
		}

		void processDump(final StringBuilder stringBuilder)
		{
			final int argsIndent = Lnt.RETURN.length() + Constant.SPACE.length() + Bpmn.PROCESS_IDENTIFIER.length() +
					Constant.SPACE_AND_LEFT_PARENTHESIS.length() + 3;

			stringBuilder.append(Lnt.FUNCTION)
					.append(Constant.SPACE)
					.append(Bpmn.MAIN_FUNCTION_IDENTIFIER)
					.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
					.append(Constant.RIGHT_PARENTHESIS)
					.append(Constant.COLON_AND_SPACE)
					.append(Bpmn.BPMN_PROCESS_LNT_TYPE)
					.append(Constant.SPACE)
					.append(Lnt.IS)
					.append(Constant.LINE_FEED)
					.append(Utils.indentLNT(1))
					.append(Lnt.RETURN)
					.append(Constant.SPACE)
					.append(Bpmn.PROCESS_IDENTIFIER)
					.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
					.append(this.name)
					.append(Constant.COMA)
					.append(Constant.LINE_FEED)
					.append(Utils.indent(argsIndent))
					.append(Constant.LEFT_CURVY_BRACKET)
					.append(Bpmn.INITIAL_NODES_IDENTIFIER)
					.append(Constant.SPACE_AND_LEFT_PARENTHESIS);

			this.initial.processLnt(stringBuilder);

			stringBuilder.append(Constant.RIGHT_PARENTHESIS)
					.append(Constant.COMA)
					.append(Constant.LINE_FEED)
					.append(Utils.indent(argsIndent + 1));

			//handle final
			stringBuilder.append(Bpmn.FINAL_NODES_IDENTIFIER)
					.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
					.append(Constant.LEFT_CURVY_BRACKET);

			boolean first = true;

			for (final Node finalNode : this.finals)
			{
				if (first)
				{
					first = false;
				}
				else
				{
					stringBuilder.append(Constant.COMA)
							.append(Constant.LINE_FEED)
							.append(Utils.indent(argsIndent + 5));
				}

				finalNode.processLnt(stringBuilder);
			}

			stringBuilder.append(Constant.RIGHT_CURVY_BRACKET)
					.append(Constant.RIGHT_PARENTHESIS)
					.append(Constant.COMA)
					.append(Constant.LINE_FEED);

			//TODO: eliminate iterating twice / Separate printer class?
			//handle tasks
			stringBuilder.append(Utils.indent(argsIndent + 1))
					.append(Bpmn.TASKS_IDENTIFIER)
					.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
					.append(Constant.LEFT_CURVY_BRACKET);

			first = true;

			for (final Node node : this.nodes)
			{
				if (node instanceof Task)
				{
					if (first)
					{
						first = false;
					}
					else
					{
						stringBuilder.append(Constant.COMA)
								.append(Constant.LINE_FEED)
								.append(Utils.indent(argsIndent + 5));
					}

					node.processLnt(stringBuilder);
				}
			}

			stringBuilder.append(Constant.RIGHT_CURVY_BRACKET)
					.append(Constant.RIGHT_PARENTHESIS)
					.append(Constant.COMA)
					.append(Constant.LINE_FEED);

			//handle gateways
			stringBuilder.append(Utils.indent(argsIndent + 1))
					.append(Bpmn.GATEWAYS_IDENTIFIER)
					.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
					.append(Constant.LEFT_CURVY_BRACKET);

			first = true;

			for (final Node node : this.nodes)
			{
				if (node instanceof Gateway)
				{
					if (first)
					{
						first = false;
					}
					else
					{
						stringBuilder.append(Constant.COMA)
								.append(Constant.LINE_FEED)
								.append(Utils.indent(argsIndent + 5));
					}

					if (node instanceof XOrJoinGateway)
					{
						((XOrJoinGateway) node).processLnt(stringBuilder, Bpmn.MERGE_TYPE, Bpmn.EXCLUSIVE_TYPE);
					}
					if (node instanceof XOrSplitGateway)
					{
						((XOrSplitGateway) node).processLnt(stringBuilder, Bpmn.SPLIT_TYPE, Bpmn.EXCLUSIVE_TYPE);
					}
					if (node instanceof OrJoinGateway)
					{
						((OrJoinGateway) node).processLnt(stringBuilder, Bpmn.MERGE_TYPE, Bpmn.INCLUSIVE_TYPE);
					}
					if (node instanceof OrSplitGateway)
					{
						((OrSplitGateway) node).processLnt(stringBuilder, Bpmn.SPLIT_TYPE, Bpmn.INCLUSIVE_TYPE);
					}
					if (node instanceof AndJoinGateway)
					{
						((AndJoinGateway) node).processLnt(stringBuilder, Bpmn.MERGE_TYPE, Bpmn.PARALLEL_TYPE);
					}
					if (node instanceof AndSplitGateway)
					{
						((AndSplitGateway) node).processLnt(stringBuilder, Bpmn.SPLIT_TYPE, Bpmn.PARALLEL_TYPE);
					}
				}
			}

			stringBuilder.append(Constant.RIGHT_CURVY_BRACKET)
					.append(Constant.RIGHT_PARENTHESIS)
					.append(Constant.RIGHT_CURVY_BRACKET)
					.append(Constant.COMA)
					.append(Constant.LINE_FEED);

			//Handle flows
			stringBuilder.append(Utils.indent(argsIndent))
					.append(Constant.LEFT_CURVY_BRACKET);

			first = true;

			for (final Flow flow : this.flows)
			{
				if (first)
				{
					first = false;
				}
				else
				{
					stringBuilder.append(Constant.COMA)
							.append(Constant.LINE_FEED)
							.append(Utils.indent(argsIndent + 1));
				}

				flow.processLnt(stringBuilder);
			}

			stringBuilder.append(Constant.RIGHT_CURVY_BRACKET)
					.append(Constant.RIGHT_PARENTHESIS)
					.append(Constant.LINE_FEED)
					.append(Lnt.END_FUNCTION)
					.append(Constant.DOUBLE_LINE_FEED)
					.append(Lnt.STANDARD_SEPARATOR)
					.append(Constant.DOUBLE_LINE_FEED);
		}

		//TODO A vérifier : passage de networkx à JGraphT
		boolean checkInclusiveCycle()
		{
			final DefaultDirectedGraph<Node, DefaultEdge> directedGraph = new DefaultDirectedGraph<>(DefaultEdge.class);

			for (final Flow flow : this.flows)
			{
				directedGraph.addVertex(flow.getSource());
				directedGraph.addVertex(flow.getTarget());
				directedGraph.addEdge(flow.getSource(), flow.getTarget());
			}

			final CycleDetector<Node, DefaultEdge> cycleDetector = new CycleDetector<>(directedGraph);

			for (final Node node : directedGraph.vertexSet())
			{
				if (node instanceof OrJoinGateway)
				{
					if (cycleDetector.detectCyclesContainingVertex(node))
					{
						return true;
					}
				}
			}

			return false;
		}

		void generateScheduler(final StringBuilder stringBuilder)
		{
			final String processIdentifier = Lnt.PROCESS + Constant.SPACE + Bpmn.SCHEDULER_IDENTIFIER +
					Constant.SPACE_AND_LEFT_SQUARE_BRACKET;
			stringBuilder.append(processIdentifier);
			final Pair<String, Integer> flowMsgsAndPosition = this.getFlowMsgsAndLineLength(processIdentifier.length(), processIdentifier.length());
			stringBuilder.append(flowMsgsAndPosition.getLeft());

			//Add split synchro params
			final ArrayList<String> synchroParams = this.computeAdditionalSynchroPoints();
			int nbCharCurrentLine = flowMsgsAndPosition.getRight();

			if (!synchroParams.isEmpty())
			{
				for (String synchroParam : synchroParams)
				{
					stringBuilder.append(Constant.COMA_AND_SPACE);
					nbCharCurrentLine += 2;

					if (nbCharCurrentLine + synchroParam.length() > Lnt.MAX_CHAR_PER_LINE)
					{
						stringBuilder.append(Constant.LINE_FEED)
								.append(Utils.indent(processIdentifier.length()));
						nbCharCurrentLine = processIdentifier.length() + synchroParam.length();
					}
					else
					{
						nbCharCurrentLine += synchroParam.length();
					}

					stringBuilder.append(synchroParam);
				}
			}

			//This parameter stores the set of active flows/tokens
			stringBuilder.append(Constant.COMA_AND_SPACE);
			nbCharCurrentLine += 2;

			if (nbCharCurrentLine + 11 > Lnt.MAX_CHAR_PER_LINE)
			{
				stringBuilder.append(Constant.LINE_FEED)
						.append(Utils.indent(processIdentifier.length()));
			}

			stringBuilder.append(Bpmn.MOVE_ON_FLOW)
					.append(Constant.COLON_AND_SPACE)
					.append(Lnt.ANY)
					.append(Constant.RIGHT_SQUARE_BRACKET)
					.append(Constant.LINE_FEED)
					.append(Utils.indent(processIdentifier.length() - 1))
					.append(Constant.LEFT_PARENTHESIS)
					.append(Bpmn.ACTIVE_FLOWS_VARIABLE)
					.append(Constant.COLON_AND_SPACE)
					.append(Bpmn.SET_OF_IDS_LNT_TYPE)
					.append(Constant.COMA_AND_SPACE)
					.append(Bpmn.BPMN_PROCESS_VARIABLE)
					.append(Constant.COLON_AND_SPACE)
					.append(Bpmn.BPMN_PROCESS_LNT_TYPE)
					.append(Constant.COMA_AND_SPACE)
					.append(Bpmn.SYNC_STORE_VARIABLE)
					.append(Constant.COLON_AND_SPACE)
					.append(Bpmn.SET_OF_IDS_LNT_TYPE)
					.append(Constant.COMA)
					.append(Constant.LINE_FEED)
					.append(Utils.indent(processIdentifier.length()))
					.append(Bpmn.MERGE_STORE_VARIABLE)
					.append(Constant.COLON_AND_SPACE)
					.append(Bpmn.SET_OF_IDS_LNT_TYPE)
					.append(Constant.COMA_AND_SPACE)
					.append(Bpmn.PAR_STORE_VARIABLE)
					.append(Constant.COLON_AND_SPACE)
					.append(Bpmn.SET_OF_IDS_LNT_TYPE)
					.append(Constant.RIGHT_PARENTHESIS)
					.append(Constant.LINE_FEED)
					.append(Lnt.IS)
					.append(Constant.LINE_FEED);

			final ArrayList<String> identSet = new ArrayList<>();
			final ArrayList<String> flowAltStrings = new ArrayList<>();
			final ArrayList<String> incJoinBegin = new ArrayList<>();
			final ArrayList<String> parJoinBegin = new ArrayList<>();
			final int nodeMinIndent = 9;
			final String ident1 = Bpmn.IDENT_VARIABLE + "1";
			final String ident2 = Bpmn.IDENT_VARIABLE + "2";

			for (final Node node : this.nodes)
			{
				final StringBuilder nodeBuilder = new StringBuilder();

				if (node instanceof Task)
				{
					nodeBuilder.append(Utils.indent(nodeMinIndent))
							.append(Lnt.OPEN_MULTILINE_COMMENTARY)
							.append(Constant.COMMENTS_DASHES)
							.append(Constant.SPACE)
							.append("Task of ID")
							.append(Constant.SPACE_AND_DOUBLE_QUOTATION_MARK)
							.append(node.identifier())
							.append(Constant.DOUBLE_QUOTATION_MARK_AND_SPACE)
							.append(Constant.COMMENTS_DASHES)
							.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
							.append(Constant.LINE_FEED)
							.append(Utils.indent(nodeMinIndent));

					final String incomingFlowIdentifier = node.incomingFlows().get(0).identifier() + Constant.UNDERSCORE +
							Bpmn.FINISH + Constant.SPACE_AND_LEFT_PARENTHESIS + Lnt.OUT_PARAMETER + ident1 + Lnt.SPACED_OF +
							Bpmn.ID_LNT_TYPE + Constant.RIGHT_PARENTHESIS + Lnt.SEQUENTIAL_COMPOSITION_OPERATOR +
							Constant.LINE_FEED;

					final String outFlowIdentifier = node.outgoingFlows().get(0).identifier() + Constant.UNDERSCORE +
							Bpmn.BEGIN + Constant.SPACE_AND_LEFT_PARENTHESIS + Lnt.OUT_PARAMETER + ident2 + Lnt.SPACED_OF +
							Bpmn.ID_LNT_TYPE + Constant.RIGHT_PARENTHESIS + Lnt.SEQUENTIAL_COMPOSITION_OPERATOR +
							Constant.LINE_FEED;

					nodeBuilder.append(incomingFlowIdentifier)
							.append(Utils.indent(nodeMinIndent))
							.append(outFlowIdentifier)
							.append(this.getSchedulerString(
								Collections.singletonList(ident1),
								Collections.singletonList(ident2),
								Bpmn.SYNC_STORE_VARIABLE,
								Bpmn.MERGE_STORE_VARIABLE,
								Bpmn.PAR_STORE_VARIABLE,
								nodeMinIndent
							));

					identSet.add(ident1);
					identSet.add(ident2);
				}
				else if (node instanceof XOrSplitGateway)
				{
					nodeBuilder.append(Utils.indent(nodeMinIndent))
							.append(Lnt.OPEN_MULTILINE_COMMENTARY)
							.append(Constant.COMMENTS_DASHES)
							.append(Constant.SPACE)
							.append("Exclusive split gateway of ID")
							.append(Constant.SPACE_AND_DOUBLE_QUOTATION_MARK)
							.append(node.identifier())
							.append(Constant.DOUBLE_QUOTATION_MARK_AND_SPACE)
							.append(Constant.COMMENTS_DASHES)
							.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
							.append(Constant.LINE_FEED)
							.append(Utils.indent(nodeMinIndent))
							.append(node.firstIncomingFlow().identifier())
							.append(Constant.UNDERSCORE)
							.append(Bpmn.FINISH)
							.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
							.append(Lnt.OUT_PARAMETER)
							.append(ident1)
							.append(Lnt.SPACED_OF)
							.append(Bpmn.ID_LNT_TYPE)
							.append(Constant.RIGHT_PARENTHESIS)
							.append(Lnt.SEQUENTIAL_COMPOSITION_OPERATOR)
							.append(Constant.LINE_FEED)
							.append(Utils.indent(nodeMinIndent));

					identSet.add(ident1);
					boolean first = true;
					int counter = 2;
					nodeBuilder.append(Lnt.ALT)
							.append(Constant.LINE_FEED);

					for (final Flow flow : node.outgoingFlows())
					{
						final String currentIdent = Bpmn.IDENT_VARIABLE + counter;

						if (first)
						{
							first = false;
						}
						else
						{
							nodeBuilder.append(Constant.LINE_FEED)
									.append(Utils.indent(nodeMinIndent))
									.append(Lnt.ALT_OPERATOR)
									.append(Constant.LINE_FEED);
						}

						identSet.add(currentIdent);

						nodeBuilder.append(Utils.indent(nodeMinIndent))
								.append(Utils.indentLNT(1))
								.append(flow.identifier())
								.append(Constant.UNDERSCORE)
								.append(Bpmn.BEGIN)
								.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
								.append(Lnt.OUT_PARAMETER)
								.append(currentIdent)
								.append(Lnt.SPACED_OF)
								.append(Bpmn.ID_LNT_TYPE)
								.append(Constant.RIGHT_PARENTHESIS)
								.append(Lnt.SEQUENTIAL_COMPOSITION_OPERATOR)
								.append(Constant.LINE_FEED)
								.append(this.getSchedulerString(
									Collections.singletonList(ident1),
									Collections.singletonList(currentIdent),
									Bpmn.SYNC_STORE_VARIABLE,
									Bpmn.MERGE_STORE_VARIABLE,
									Bpmn.PAR_STORE_VARIABLE,
									nodeMinIndent + 3
								));

						counter++;
					}

					nodeBuilder.append(Constant.LINE_FEED)
							.append(Utils.indent(nodeMinIndent))
							.append(Lnt.END_ALT)
							.append(Constant.LINE_FEED);
				}
				else if (node instanceof XOrJoinGateway)
				{
					nodeBuilder.append(Utils.indent(nodeMinIndent))
							.append(Lnt.OPEN_MULTILINE_COMMENTARY)
							.append(Constant.COMMENTS_DASHES)
							.append(Constant.SPACE)
							.append("XOrJoinGateway of ID")
							.append(Constant.SPACE_AND_DOUBLE_QUOTATION_MARK)
							.append(node.identifier())
							.append(Constant.DOUBLE_QUOTATION_MARK_AND_SPACE)
							.append(Constant.COMMENTS_DASHES)
							.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
							.append(Constant.LINE_FEED)
							.append(Utils.indent(nodeMinIndent))
							.append(Lnt.ALT)
							.append(Constant.LINE_FEED);

					boolean first = true;

					for (final Flow flow : node.incomingFlows())
					{
						if (first)
						{
							first = false;
						}
						else
						{
							nodeBuilder.append(Constant.LINE_FEED)
									.append(Utils.indent(nodeMinIndent))
									.append(Lnt.ALT_OPERATOR)
									.append(Constant.LINE_FEED);
						}

						nodeBuilder.append(Utils.indent(nodeMinIndent))
								.append(Utils.indentLNT(1))
								.append(flow.identifier())
								.append(Constant.UNDERSCORE)
								.append(Bpmn.FINISH)
								.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
								.append(Lnt.OUT_PARAMETER)
								.append(ident2)
								.append(Lnt.SPACED_OF)
								.append(Bpmn.ID_LNT_TYPE)
								.append(Constant.RIGHT_PARENTHESIS);
					}

					identSet.add(ident1);
					identSet.add(ident2);

					nodeBuilder.append(Constant.LINE_FEED)
							.append(Utils.indent(nodeMinIndent))
							.append(Lnt.END_ALT)
							.append(Lnt.SEQUENTIAL_COMPOSITION_OPERATOR)
							.append(Constant.LINE_FEED)
							.append(Utils.indent(nodeMinIndent))
							.append(node.firstOutgoingFlow().identifier())
							.append(Constant.UNDERSCORE)
							.append(Bpmn.BEGIN)
							.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
							.append(Lnt.OUT_PARAMETER)
							.append(ident1)
							.append(Lnt.SPACED_OF)
							.append(Bpmn.ID_LNT_TYPE)
							.append(Constant.RIGHT_PARENTHESIS)
							.append(Lnt.SEQUENTIAL_COMPOSITION_OPERATOR)
							.append(Constant.LINE_FEED)
							.append(this.getSchedulerString(
								Collections.singletonList(ident2),
								Collections.singletonList(ident1),
								Bpmn.SYNC_STORE_VARIABLE,
								Bpmn.MERGE_STORE_VARIABLE,
								Bpmn.PAR_STORE_VARIABLE,
								nodeMinIndent
							));
				}
				else if (node instanceof AndSplitGateway)
				{
					nodeBuilder.append(Utils.indent(nodeMinIndent))
							.append(Lnt.OPEN_MULTILINE_COMMENTARY)
							.append(Constant.COMMENTS_DASHES)
							.append(Constant.SPACE)
							.append("AndSplitGateway of ID")
							.append(Constant.SPACE_AND_DOUBLE_QUOTATION_MARK)
							.append(node.identifier())
							.append(Constant.DOUBLE_QUOTATION_MARK_AND_SPACE)
							.append(Constant.COMMENTS_DASHES)
							.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
							.append(Constant.LINE_FEED)
							.append(Utils.indent(nodeMinIndent))
							.append(node.firstIncomingFlow().identifier())
							.append(Constant.UNDERSCORE)
							.append(Bpmn.FINISH)
							.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
							.append(Lnt.OUT_PARAMETER)
							.append(ident1)
							.append(Lnt.SPACED_OF)
							.append(Bpmn.ID_LNT_TYPE)
							.append(Constant.RIGHT_PARENTHESIS)
							.append(Lnt.SEQUENTIAL_COMPOSITION_OPERATOR)
							.append(Constant.LINE_FEED)
							.append(Utils.indent(nodeMinIndent))
							.append(Lnt.PAR)
							.append(Constant.LINE_FEED);

					boolean first = true;
					int counter = 2;
					final ArrayList<String> outgoingFlowsIdentifiers = new ArrayList<>();

					for (final Flow flow : node.outgoingFlows())
					{
						final String currentIdent = Bpmn.IDENT_VARIABLE + counter;

						if (first)
						{
							first = false;
						}
						else
						{
							nodeBuilder.append(Constant.LINE_FEED)
									.append(Utils.indent(nodeMinIndent))
									.append(Lnt.PAR_OPERATOR)
									.append(Constant.LINE_FEED);
						}

						nodeBuilder.append(Utils.indent(nodeMinIndent))
								.append(Utils.indentLNT(1))
								.append(flow.identifier())
								.append(Constant.UNDERSCORE)
								.append(Bpmn.BEGIN)
								.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
								.append(Lnt.OUT_PARAMETER)
								.append(currentIdent)
								.append(Lnt.SPACED_OF)
								.append(Bpmn.ID_LNT_TYPE)
								.append(Constant.RIGHT_PARENTHESIS);

						identSet.add(currentIdent);
						outgoingFlowsIdentifiers.add(currentIdent);
						counter++;
					}

					nodeBuilder.append(Constant.LINE_FEED)
							.append(Utils.indent(nodeMinIndent))
							.append(Lnt.END_PAR)
							.append(Lnt.SEQUENTIAL_COMPOSITION_OPERATOR)
							.append(Constant.LINE_FEED)
							.append(this.getSchedulerString(
								Collections.singletonList(ident1),
								outgoingFlowsIdentifiers,
								Bpmn.SYNC_STORE_VARIABLE,
								Bpmn.MERGE_STORE_VARIABLE,
								Bpmn.PAR_STORE_VARIABLE,
								nodeMinIndent
							));
				}
				else if (node instanceof AndJoinGateway)
				{
					nodeBuilder.append(Utils.indent(nodeMinIndent))
							.append(Lnt.OPEN_MULTILINE_COMMENTARY)
							.append(Constant.COMMENTS_DASHES)
							.append(Constant.SPACE)
							.append("AndJoinGateway of ID")
							.append(Constant.SPACE_AND_DOUBLE_QUOTATION_MARK)
							.append(node.identifier())
							.append(Constant.DOUBLE_QUOTATION_MARK_AND_SPACE)
							.append(Constant.COMMENTS_DASHES)
							.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
							.append(Constant.LINE_FEED)
							.append(Utils.indent(nodeMinIndent))
							.append(Lnt.ALT)
							.append(Constant.LINE_FEED);

					boolean first = true;

					for (final Flow incomingFlow : node.incomingFlows())
					{
						if (first)
						{
							first = false;
							identSet.add(Bpmn.IDENT_VARIABLE);
						}
						else
						{
							nodeBuilder.append(Constant.LINE_FEED)
									.append(Utils.indent(nodeMinIndent))
									.append(Lnt.ALT_OPERATOR)
									.append(Constant.LINE_FEED);
						}

						final String syncString = Bpmn.INSERT_LNT_FUNCTION + Constant.SPACE_AND_LEFT_PARENTHESIS +
								Bpmn.IDENT_VARIABLE + Constant.COMA_AND_SPACE + Bpmn.SYNC_STORE_VARIABLE +
								Constant.RIGHT_PARENTHESIS;

						final String parStoreString = Bpmn.INSERT_LNT_FUNCTION + Constant.SPACE_AND_LEFT_PARENTHESIS +
								node.identifier() + Constant.COMA_AND_SPACE + Bpmn.PAR_STORE_VARIABLE +
								Constant.RIGHT_PARENTHESIS;

						nodeBuilder.append(Utils.indent(nodeMinIndent))
								.append(Utils.indentLNT(1))
								.append(incomingFlow.identifier())
								.append(Constant.UNDERSCORE)
								.append(Bpmn.FINISH)
								.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
								.append(Lnt.OUT_PARAMETER)
								.append(Bpmn.IDENT_VARIABLE)
								.append(Lnt.SPACED_OF)
								.append(Bpmn.ID_LNT_TYPE)
								.append(Constant.RIGHT_PARENTHESIS)
								.append(Lnt.SEQUENTIAL_COMPOSITION_OPERATOR)
								.append(Constant.LINE_FEED)
								.append(this.getSchedulerString(
									new ArrayList<>(),
									new ArrayList<>(),
									syncString,
									Bpmn.MERGE_STORE_VARIABLE,
									parStoreString,
									nodeMinIndent + 3
								))
						;
					}

					nodeBuilder.append(Constant.LINE_FEED)
							.append(Utils.indent(nodeMinIndent))
							.append(Lnt.END_ALT)
							.append(Constant.LINE_FEED);

					identSet.add(ident1);

					final StringBuilder parJoinBeginBuilder = new StringBuilder();

					//Parallel merge join TODO: Clean up
					parJoinBeginBuilder.append(Utils.indentLNT(5))
							.append(node.firstOutgoingFlow().identifier())
							.append(Constant.UNDERSCORE)
							.append(Bpmn.BEGIN)
							.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
							.append(Lnt.OUT_PARAMETER)
							.append(ident1)
							.append(Lnt.SPACED_OF)
							.append(Bpmn.ID_LNT_TYPE)
							.append(Constant.RIGHT_PARENTHESIS)
							.append(Lnt.SEQUENTIAL_COMPOSITION_OPERATOR)
							.append(Constant.LINE_FEED)
							.append(Utils.indentLNT(5))
							.append(Bpmn.SCHEDULER_IDENTIFIER)
							.append(Constant.SPACE_AND_LEFT_SQUARE_BRACKET);

					final int minIndent = 26;
					final Pair<String, Integer> flowMsgsAndLineLength = this.getFlowMsgsAndLineLength(minIndent, minIndent);
					parJoinBeginBuilder.append(flowMsgsAndLineLength.getLeft())
							.append(Constant.COMA_AND_SPACE);
					final ArrayList<String> synchroPoints = this.computeAdditionalSynchroPoints();
					nbCharCurrentLine = flowMsgsAndLineLength.getRight() + 2;

					for (String synchroPoint : synchroPoints)
					{
						if (synchroPoint.length() + nbCharCurrentLine + 2 > Lnt.MAX_CHAR_PER_LINE)
						{
							parJoinBeginBuilder.append(Constant.LINE_FEED).append(Utils.indent(minIndent));
							nbCharCurrentLine = minIndent + synchroPoint.length() + 2;
						}
						else
						{
							nbCharCurrentLine += synchroPoint.length() + 2;
						}

						parJoinBeginBuilder.append(synchroPoint)
								.append(Constant.COMA_AND_SPACE);
					}

					if (nbCharCurrentLine + 7 > Lnt.MAX_CHAR_PER_LINE)
					{
						parJoinBeginBuilder.append(Constant.LINE_FEED)
								.append(Utils.indent(minIndent));
					}

					parJoinBeginBuilder.append(Bpmn.MOVE_ON_FLOW)
							.append(Constant.RIGHT_SQUARE_BRACKET)
							.append(Constant.LINE_FEED)
							.append(Utils.indent(minIndent - 1))
							.append(Constant.LEFT_PARENTHESIS)
							.append(Bpmn.UNION_LNT_FUNCTION)
							.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
							.append(Constant.LEFT_CURVY_BRACKET)
							.append(ident1)
							.append(Constant.RIGHT_CURVY_BRACKET)
							.append(Constant.COMA)
							.append(Constant.LINE_FEED)
							.append(Utils.indent(minIndent + 7))
							.append(Bpmn.REMOVE_INCOMING_FLOWS_LNT_FUNCTION)
							.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
							.append(Bpmn.BPMN_PROCESS_VARIABLE)
							.append(Constant.COMA_AND_SPACE)
							.append(Bpmn.ACTIVE_FLOWS_VARIABLE)
							.append(Constant.COMA_AND_SPACE)
							.append(Bpmn.MERGE_ID_VARIABLE)
							.append(Constant.RIGHT_PARENTHESIS)
							.append(Constant.RIGHT_PARENTHESIS)
							.append(Constant.COMA)
							.append(Constant.LINE_FEED)
							.append(Utils.indent(minIndent))
							.append(Bpmn.BPMN_PROCESS_VARIABLE)
							.append(Constant.COMA_AND_SPACE)
							.append(Bpmn.REMOVE_SYNC_LNT_FUNCTON)
							.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
							.append(Bpmn.BPMN_PROCESS_VARIABLE)
							.append(Constant.COMA_AND_SPACE)
							.append(Bpmn.SYNC_STORE_VARIABLE)
							.append(Constant.COMA_AND_SPACE)
							.append(Bpmn.MERGE_ID_VARIABLE)
							.append(Constant.RIGHT_PARENTHESIS)
							.append(Constant.COMA)
							.append(Constant.LINE_FEED)
							.append(Utils.indent(minIndent))
							.append(Bpmn.MERGE_STORE_VARIABLE)
							.append(Constant.COMA_AND_SPACE)
							.append(Bpmn.REMOVE_LNT_FUNCTION)
							.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
							.append(Bpmn.MERGE_ID_VARIABLE)
							.append(Constant.COMA_AND_SPACE)
							.append(Bpmn.PAR_STORE_VARIABLE)
							.append(Constant.RIGHT_PARENTHESIS)
							.append(Constant.RIGHT_PARENTHESIS)
							.append(Constant.LINE_FEED);

					parJoinBegin.add(parJoinBeginBuilder.toString());
				}
				else if (node instanceof OrSplitGateway)
				{
					nodeBuilder.append(Utils.indent(nodeMinIndent))
							.append(Lnt.OPEN_MULTILINE_COMMENTARY)
							.append(Constant.COMMENTS_DASHES)
							.append(Constant.SPACE)
							.append("OrSplitGateway of ID")
							.append(Constant.SPACE_AND_DOUBLE_QUOTATION_MARK)
							.append(node.identifier())
							.append(Constant.DOUBLE_QUOTATION_MARK_AND_SPACE)
							.append(Constant.COMMENTS_DASHES)
							.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
							.append(Constant.LINE_FEED)
							.append(Utils.indent(nodeMinIndent))
							.append(node.firstIncomingFlow().identifier())
							.append(Constant.UNDERSCORE)
							.append(Bpmn.FINISH)
							.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
							.append(Lnt.OUT_PARAMETER)
							.append(ident1)
							.append(Lnt.SPACED_OF)
							.append(Bpmn.ID_LNT_TYPE)
							.append(Constant.RIGHT_PARENTHESIS)
							.append(Lnt.SEQUENTIAL_COMPOSITION_OPERATOR)
							.append(Constant.LINE_FEED)
							.append(Utils.indent(nodeMinIndent))
							.append(Lnt.ALT)
							.append(Constant.LINE_FEED);

					identSet.add(ident1);

					//We translate the inclusive split by enumerating all combinations in an alt/par
					final ArrayList<String> outgoingFlowsAlphabet = new ArrayList<>();
					int counter = 2;

					for (final Flow outgoingFlow : node.outgoingFlows())
					{
						outgoingFlowsAlphabet.add(outgoingFlow.identifier() + Constant.UNDERSCORE + Bpmn.BEGIN);
						identSet.add(Bpmn.IDENT_VARIABLE + counter);
						counter++;
					}

					final ArrayList<ArrayList<String>> allOutgoingFlowsCombinations = computeAllCombinations(outgoingFlowsAlphabet);
					final ArrayList<String> outgoingFlowsIdentifiers = new ArrayList<>();
					int nb = 1;
					int cter = 1;

					for (final ArrayList<String> outgoingFlowsCombination : allOutgoingFlowsCombinations)
					{
						int nb2 = 1;

						nodeBuilder.append(Utils.indent(nodeMinIndent))
								.append(Utils.indentLNT(1))
								.append(node.identifier())
								.append(Constant.UNDERSCORE)
								.append(cter)
								.append(Lnt.SEQUENTIAL_COMPOSITION_OPERATOR)
								.append(Constant.LINE_FEED)
								.append(Utils.indent(nodeMinIndent))
								.append(Utils.indentLNT(1));

						cter++;

						if (outgoingFlowsCombination.size() > 1)
						{
							int combiCounter = allOutgoingFlowsCombinations.size();

							nodeBuilder.append(Lnt.PAR)
									.append(Constant.LINE_FEED);

							for (final String outgoingFlowIdentifier : outgoingFlowsCombination)
							{
								final String currentIdentifier = Bpmn.IDENT_VARIABLE + combiCounter;

								nodeBuilder.append(Utils.indent(nodeMinIndent))
										.append(Utils.indentLNT(2))
										.append(outgoingFlowIdentifier)
										.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
										.append(Lnt.OUT_PARAMETER)
										.append(currentIdentifier)
										.append(Lnt.SPACED_OF)
										.append(Bpmn.ID_LNT_TYPE)
										.append(Constant.RIGHT_PARENTHESIS);

								outgoingFlowsIdentifiers.add(currentIdentifier);
								identSet.add(currentIdentifier);
								combiCounter--;
								nb2++;

								if (nb2 <= outgoingFlowsCombination.size())
								{
									nodeBuilder.append(Constant.LINE_FEED)
											.append(Utils.indent(nodeMinIndent))
											.append(Utils.indentLNT(1))
											.append(Lnt.PAR_OPERATOR)
											.append(Constant.LINE_FEED);
								}
							}

							nodeBuilder.append(Constant.LINE_FEED)
									.append(Utils.indent(nodeMinIndent))
									.append(Utils.indentLNT(1))
									.append(Lnt.END_PAR)
									.append(Lnt.SEQUENTIAL_COMPOSITION_OPERATOR)
									.append(Constant.LINE_FEED)
									.append(this.getSchedulerString(
										Collections.singletonList(ident1),
										outgoingFlowsIdentifiers,
										Bpmn.SYNC_STORE_VARIABLE,
										Bpmn.MERGE_STORE_VARIABLE,
										Bpmn.PAR_STORE_VARIABLE,
										nodeMinIndent + 3
									));
						}
						else
						{
							nodeBuilder.append(outgoingFlowsCombination.iterator().next())
									.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
									.append(Lnt.OUT_PARAMETER)
									.append(Bpmn.IDENT_VARIABLE)
									.append(Lnt.SPACED_OF)
									.append(Bpmn.ID_LNT_TYPE)
									.append(Constant.RIGHT_PARENTHESIS)
									.append(Lnt.SEQUENTIAL_COMPOSITION_OPERATOR)
									.append(Constant.LINE_FEED)
									.append(this.getSchedulerString(
										Collections.singletonList(ident1),
										Collections.singletonList(Bpmn.IDENT_VARIABLE),
										Bpmn.SYNC_STORE_VARIABLE,
										Bpmn.MERGE_STORE_VARIABLE,
										Bpmn.PAR_STORE_VARIABLE,
										nodeMinIndent + 3
									));

							identSet.add(Bpmn.IDENT_VARIABLE);
						}

						nb++;

						if (nb <= allOutgoingFlowsCombinations.size())
						{
							nodeBuilder.append(Constant.LINE_FEED)
									.append(Utils.indent(nodeMinIndent))
									.append(Lnt.ALT_OPERATOR)
									.append(Constant.LINE_FEED);
						}
					}

					nodeBuilder.append(Constant.LINE_FEED)
							.append(Utils.indent(nodeMinIndent))
							.append(Lnt.END_ALT)
							.append(Constant.LINE_FEED);
				}
				else if (node instanceof OrJoinGateway)
				{
					nodeBuilder.append(Utils.indent(nodeMinIndent))
							.append(Lnt.OPEN_MULTILINE_COMMENTARY)
							.append(Constant.COMMENTS_DASHES)
							.append(Constant.SPACE)
							.append("OrJoinGateway of ID")
							.append(Constant.SPACE_AND_DOUBLE_QUOTATION_MARK)
							.append(node.identifier())
							.append(Constant.DOUBLE_QUOTATION_MARK_AND_SPACE)
							.append(Constant.COMMENTS_DASHES)
							.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
							.append(Constant.LINE_FEED)
							.append(Utils.indent(nodeMinIndent))
							.append(Lnt.ALT)
							.append(Constant.LINE_FEED);

					boolean first = true;

					for (final Flow incomingFlow : node.incomingFlows())
					{
						if (first)
						{
							first = false;
							identSet.add(Bpmn.IDENT_VARIABLE);
						}
						else
						{
							nodeBuilder.append(Constant.LINE_FEED)
									.append(Utils.indent(nodeMinIndent))
									.append(Lnt.ALT_OPERATOR)
									.append(Constant.LINE_FEED);
						}

						final String syncString = Bpmn.INSERT_LNT_FUNCTION + Constant.SPACE_AND_LEFT_PARENTHESIS +
								Bpmn.IDENT_VARIABLE + Constant.COMA_AND_SPACE + Bpmn.SYNC_STORE_VARIABLE +
								Constant.RIGHT_PARENTHESIS;

						final String mergeStoreString = Bpmn.INSERT_LNT_FUNCTION + Constant.SPACE_AND_LEFT_PARENTHESIS +
								node.identifier() + Constant.COMA_AND_SPACE + Bpmn.MERGE_STORE_VARIABLE +
								Constant.RIGHT_PARENTHESIS;

						nodeBuilder.append(Utils.indent(nodeMinIndent))
								.append(Utils.indentLNT(1))
								.append(incomingFlow.identifier())
								.append(Constant.UNDERSCORE)
								.append(Bpmn.FINISH)
								.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
								.append(Lnt.OUT_PARAMETER)
								.append(Bpmn.IDENT_VARIABLE)
								.append(Lnt.SPACED_OF)
								.append(Bpmn.ID_LNT_TYPE)
								.append(Constant.RIGHT_PARENTHESIS)
								.append(Lnt.SEQUENTIAL_COMPOSITION_OPERATOR)
								.append(Constant.LINE_FEED)
								.append(this.getSchedulerString(
									new ArrayList<>(),
									new ArrayList<>(),
									syncString,
									mergeStoreString,
									Bpmn.PAR_STORE_VARIABLE,
									nodeMinIndent + 3
								));
					}

					nodeBuilder.append(Constant.LINE_FEED)
							.append(Utils.indent(nodeMinIndent))
							.append(Lnt.END_ALT)
							.append(Constant.LINE_FEED);

					identSet.add(ident1);

					//Inclusive merge join TODO: Clean up
					final StringBuilder incJoinBeginBuilder = new StringBuilder()
							.append(Utils.indentLNT(5))
							.append(node.firstOutgoingFlow().identifier())
							.append(Constant.UNDERSCORE)
							.append(Bpmn.BEGIN)
							.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
							.append(Lnt.OUT_PARAMETER)
							.append(ident1)
							.append(Lnt.SPACED_OF)
							.append(Bpmn.ID_LNT_TYPE)
							.append(Constant.RIGHT_PARENTHESIS)
							.append(Lnt.SEQUENTIAL_COMPOSITION_OPERATOR)
							.append(Constant.LINE_FEED)
							.append(Utils.indentLNT(5))
							.append(Bpmn.SCHEDULER_IDENTIFIER)
							.append(Constant.SPACE_AND_LEFT_SQUARE_BRACKET);

					final int minIndent = 26;
					final Pair<String, Integer> flowMsgsAndLineLength = this.getFlowMsgsAndLineLength(minIndent, minIndent);
					incJoinBeginBuilder.append(flowMsgsAndLineLength.getLeft());

					nbCharCurrentLine = minIndent + flowMsgsAndLineLength.getRight();

					final ArrayList<String> synchroPoints = this.computeAdditionalSynchroPoints();

					incJoinBeginBuilder.append(Constant.COMA_AND_SPACE);
					nbCharCurrentLine += 2;

					for (String synchroPoint : synchroPoints)
					{
						//final String synchroPointWithComa = synchroPoint + ", ";

						if (nbCharCurrentLine + synchroPoint.length() + 2 > Lnt.MAX_CHAR_PER_LINE)
						{
							incJoinBeginBuilder.append(Constant.LINE_FEED).append(Utils.indent(minIndent));
							nbCharCurrentLine = minIndent + synchroPoint.length() + 2;
						}
						else
						{
							nbCharCurrentLine += synchroPoint.length() + 2;
						}

						incJoinBeginBuilder.append(synchroPoint)
								.append(Constant.COMA_AND_SPACE);
					}

					if (nbCharCurrentLine + 7 > Lnt.MAX_CHAR_PER_LINE)
					{
						incJoinBeginBuilder.append(Constant.LINE_FEED)
								.append(Utils.indent(minIndent));
					}

					incJoinBeginBuilder.append(Bpmn.MOVE_ON_FLOW)
							.append(Constant.RIGHT_SQUARE_BRACKET)
							.append(Constant.LINE_FEED)
							.append(Utils.indent(minIndent - 1))
							.append(Constant.LEFT_PARENTHESIS)
							.append(Bpmn.UNION_LNT_FUNCTION)
							.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
							.append(Constant.LEFT_CURVY_BRACKET)
							.append(ident1)
							.append(Constant.RIGHT_CURVY_BRACKET)
							.append(Constant.COMA)
							.append(Constant.LINE_FEED)
							.append(Utils.indent(minIndent + 7))
							.append(Bpmn.REMOVE_INCOMING_FLOWS_LNT_FUNCTION)
							.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
							.append(Bpmn.BPMN_PROCESS_VARIABLE)
							.append(Constant.COMA_AND_SPACE)
							.append(Bpmn.ACTIVE_FLOWS_VARIABLE)
							.append(Constant.COMA_AND_SPACE)
							.append(Bpmn.MERGE_ID_VARIABLE)
							.append(Constant.RIGHT_PARENTHESIS)
							.append(Constant.RIGHT_PARENTHESIS)
							.append(Constant.COMA)
							.append(Constant.LINE_FEED)
							.append(Utils.indent(minIndent))
							.append(Bpmn.BPMN_PROCESS_VARIABLE)
							.append(Constant.COMA_AND_SPACE)
							.append(Bpmn.REMOVE_SYNC_LNT_FUNCTON)
							.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
							.append(Bpmn.BPMN_PROCESS_VARIABLE)
							.append(Constant.COMA_AND_SPACE)
							.append(Bpmn.SYNC_STORE_VARIABLE)
							.append(Constant.COMA_AND_SPACE)
							.append(Bpmn.MERGE_ID_VARIABLE)
							.append(Constant.RIGHT_PARENTHESIS)
							.append(Constant.COMA)
							.append(Constant.LINE_FEED)
							.append(Utils.indent(minIndent))
							.append(Bpmn.REMOVE_LNT_FUNCTION)
							.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
							.append(Bpmn.MERGE_ID_VARIABLE)
							.append(Constant.COMA_AND_SPACE)
							.append(Bpmn.MERGE_STORE_VARIABLE)
							.append(Constant.RIGHT_PARENTHESIS)
							.append(Constant.COMA_AND_SPACE)
							.append(Bpmn.PAR_STORE_VARIABLE)
							.append(Constant.RIGHT_PARENTHESIS)
							.append(Constant.LINE_FEED);

					incJoinBegin.add(incJoinBeginBuilder.toString());
				}
				else
				{
					nodeBuilder.append(Utils.indent(nodeMinIndent))
							.append(Lnt.OPEN_MULTILINE_COMMENTARY)
							.append(Constant.COMMENTS_DASHES)
							.append(Constant.SPACE)
							.append("ERROR: Unable to select element of ID")
							.append(Constant.SPACE_AND_DOUBLE_QUOTATION_MARK)
							.append(node.identifier())
							.append(Constant.DOUBLE_QUOTATION_MARK_AND_SPACE)
							.append(Constant.COMMENTS_DASHES)
							.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
							.append(Constant.LINE_FEED);
				}

				flowAltStrings.add(nodeBuilder.toString());
			}

			//Generate var
			identSet.add(ident1); //For initial/final
			stringBuilder.append(Utils.indentLNT(1))
					.append(Lnt.VAR);

			final int minIndent;
			final boolean newLineRequiredForVars = identSet.size() > Constant.MAX_VARS_PER_LINE - 1; //-1 because we add ``mergeid'' at the end

			if (newLineRequiredForVars)
			{
				stringBuilder.append(Constant.LINE_FEED)
						.append(Utils.indentLNT(2));
				nbCharCurrentLine = minIndent = 6;
			}
			else
			{
				stringBuilder.append(Constant.SPACE);
				nbCharCurrentLine = minIndent = 7;
			}

			for (final String currentIdent : new HashSet<>(identSet)) //TODO Intéret ? Randomiser ?
			{
				if (currentIdent.length() + nbCharCurrentLine + 2 > Lnt.MAX_CHAR_PER_LINE)
				{
					stringBuilder.append(Constant.LINE_FEED).append(Utils.indent(minIndent));
					nbCharCurrentLine = minIndent + currentIdent.length() + 2;
				}
				else
				{
					nbCharCurrentLine += currentIdent.length() + 2;
				}

				stringBuilder.append(currentIdent)
						.append(Constant.COMA_AND_SPACE);
			}

			final int nbCharToConsider = newLineRequiredForVars ? 11 : 14;

			if (nbCharCurrentLine + nbCharToConsider > Lnt.MAX_CHAR_PER_LINE)
			{
				stringBuilder.append(Constant.LINE_FEED)
						.append(Utils.indent(minIndent));
			}

			stringBuilder.append(Bpmn.MERGE_ID_VARIABLE)
					.append(Constant.COLON_AND_SPACE)
					.append(Bpmn.ID_LNT_TYPE);

			if (newLineRequiredForVars)
			{
				stringBuilder.append(Constant.LINE_FEED)
						.append(Utils.indentLNT(1));
			}
			else
			{
				stringBuilder.append(Constant.SPACE);
			}

			stringBuilder.append(Lnt.IN)
					.append(Constant.LINE_FEED)
					.append(Utils.indentLNT(2))
					.append(Lnt.ALT)
					.append(Constant.LINE_FEED);

			//Handle initial node
			stringBuilder.append(Utils.indent(nodeMinIndent))
					.append(Lnt.OPEN_MULTILINE_COMMENTARY)
					.append(Constant.COMMENTS_DASHES)
					.append(Constant.SPACE)
					.append("Initial node")
					.append(Constant.SPACE)
					.append(Constant.COMMENTS_DASHES)
					.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
					.append(Constant.LINE_FEED)
					.append(Utils.indent(nodeMinIndent))
					.append(this.initial.firstOutgoingFlow().identifier())
					.append(Constant.UNDERSCORE)
					.append(Bpmn.BEGIN)
					.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
					.append(Lnt.OUT_PARAMETER)
					.append(ident1)
					.append(Lnt.SPACED_OF)
					.append(Bpmn.ID_LNT_TYPE)
					.append(Constant.RIGHT_PARENTHESIS)
					.append(Lnt.SEQUENTIAL_COMPOSITION_OPERATOR)
					.append(Constant.LINE_FEED)
					.append(this.getSchedulerString(
						new ArrayList<>(),
						Collections.singletonList(ident1),
						Bpmn.SYNC_STORE_VARIABLE,
						Bpmn.MERGE_STORE_VARIABLE,
						Bpmn.PAR_STORE_VARIABLE,
						9
					));

			for (final String flow : flowAltStrings)
			{
				stringBuilder.append(Constant.LINE_FEED)
						.append(Utils.indentLNT(2))
						.append(Lnt.ALT_OPERATOR)
						.append(Constant.LINE_FEED)
						.append(flow);
			}

			stringBuilder.append(Constant.LINE_FEED)
					.append(Utils.indentLNT(2))
					.append(Lnt.ALT_OPERATOR)
					.append(Constant.LINE_FEED);

			//Handle final node
			stringBuilder.append(Utils.indent(nodeMinIndent))
					.append(Lnt.OPEN_MULTILINE_COMMENTARY)
					.append(Constant.COMMENTS_DASHES)
					.append(Constant.SPACE)
					.append("Final node")
					.append(Constant.SPACE)
					.append(Constant.COMMENTS_DASHES)
					.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
					.append(Constant.LINE_FEED)
					.append(Utils.indent(nodeMinIndent))
					.append(this.finals.get(0).firstIncomingFlow().identifier())
					.append(Constant.UNDERSCORE)
					.append(Bpmn.FINISH)
					.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
					.append(Lnt.OUT_PARAMETER)
					.append(ident1)
					.append(Lnt.SPACED_OF)
					.append(Bpmn.ID_LNT_TYPE)
					.append(Constant.RIGHT_PARENTHESIS)
					.append(Lnt.SEQUENTIAL_COMPOSITION_OPERATOR)
					.append(Constant.LINE_FEED)
					.append(this.getSchedulerString(
						Collections.singletonList(ident1),
						new ArrayList<>(),
						Bpmn.SYNC_STORE_VARIABLE,
						Bpmn.MERGE_STORE_VARIABLE,
						Bpmn.PAR_STORE_VARIABLE,
						9
					))
					.append(Constant.LINE_FEED)
					.append(Utils.indentLNT(2))
					.append(Lnt.ALT_OPERATOR)
					.append(Constant.LINE_FEED)
					.append(Utils.indentLNT(3))
					.append(Bpmn.MERGE_ID_VARIABLE)
					.append(Lnt.SPACED_VARIABLE_ASSIGNATION_OPERATOR)
					.append(Lnt.ANY)
					.append(Constant.SPACE)
					.append(Bpmn.ID_LNT_TYPE)
					.append(Constant.SPACE)
					.append(Lnt.WHERE)
					.append(Constant.SPACE)
					.append(Bpmn.MEMBER_LNT_FUNCTION)
					.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
					.append(Bpmn.MERGE_ID_VARIABLE)
					.append(Constant.COMA_AND_SPACE)
					.append(Bpmn.MERGE_STORE_VARIABLE)
					.append(Constant.RIGHT_PARENTHESIS)
					.append(Lnt.SEQUENTIAL_COMPOSITION_OPERATOR)
					.append(Constant.LINE_FEED)
					.append(Utils.indentLNT(3))
					.append(Lnt.IF)
					.append(Constant.SPACE)
					.append(Bpmn.IS_MERGE_POSSIBLE_V2_LNT_FUNCTION)
					.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
					.append(Bpmn.BPMN_PROCESS_VARIABLE)
					.append(Constant.COMA_AND_SPACE)
					.append(Bpmn.ACTIVE_FLOWS_VARIABLE)
					.append(Constant.COMA_AND_SPACE)
					.append(Bpmn.MERGE_ID_VARIABLE)
					.append(Constant.RIGHT_PARENTHESIS_AND_SPACE)
					.append(Lnt.AND)
					.append(Constant.LINE_FEED)
					.append(Utils.indentLNT(3))
					.append(Utils.indent(3))
					.append(Bpmn.IS_SYNCHRONISATION_DONE_LNT_FUNCTION)
					.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
					.append(Bpmn.BPMN_PROCESS_VARIABLE)
					.append(Constant.COMA_AND_SPACE)
					.append(Bpmn.ACTIVE_FLOWS_VARIABLE)
					.append(Constant.COMA_AND_SPACE)
					.append(Bpmn.SYNC_STORE_VARIABLE)
					.append(Constant.COMA_AND_SPACE)
					.append(Bpmn.MERGE_ID_VARIABLE)
					.append(Constant.RIGHT_PARENTHESIS_AND_SPACE)
					.append(Lnt.THEN)
					.append(Constant.LINE_FEED)
					.append(Utils.indentLNT(4))
					.append(Bpmn.MOVE_ON_FLOW)
					.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
					.append(Bpmn.MERGE_ID_VARIABLE)
					.append(Constant.RIGHT_PARENTHESIS)
					.append(Lnt.SEQUENTIAL_COMPOSITION_OPERATOR)
					.append(Constant.LINE_FEED) //mergeid no longer requires "!" (see HISTORY v2022-k)
			;

			if (incJoinBegin.isEmpty())
			{
				stringBuilder.append(this.getSchedulerString(
					new ArrayList<>(),
					new ArrayList<>(),
					Bpmn.SYNC_STORE_VARIABLE,
					Bpmn.MERGE_STORE_VARIABLE,
					Bpmn.PAR_STORE_VARIABLE,
					12
				));
			}
			else
			{
				int i = 1;
				stringBuilder.append(Utils.indentLNT(4))
						.append(Lnt.ALT)
						.append(Constant.LINE_FEED);

				for (final String incJoin : incJoinBegin)
				{
					stringBuilder.append(incJoin);

					if (i < incJoinBegin.size())
					{
						stringBuilder.append(Constant.LINE_FEED)
								.append(Utils.indentLNT(4))
								.append(Lnt.ALT_OPERATOR)
								.append(Constant.LINE_FEED);
					}

					i++;
				}

				stringBuilder.append(Constant.LINE_FEED)
						.append(Utils.indentLNT(4))
						.append(Lnt.END_ALT);
			}

			stringBuilder.append(Constant.LINE_FEED)
					.append(Utils.indentLNT(3))
					.append(Lnt.ELSE)
					.append(Constant.LINE_FEED)
					.append(Utils.indentLNT(4))
					.append(Bpmn.SCHEDULER_IDENTIFIER)
					.append(Constant.SPACE_AND_LEFT_SQUARE_BRACKET);

			final Pair<String, Integer> flowMsgsAndLineLength = this.getFlowMsgsAndLineLength(23, 23);

			stringBuilder.append(flowMsgsAndLineLength.getLeft());
			final ArrayList<String> synchroPoints = this.computeAdditionalSynchroPoints();
			nbCharCurrentLine = flowMsgsAndLineLength.getRight() + 2;
			stringBuilder.append(Constant.COMA_AND_SPACE);

			for (final String synchroPoint : synchroPoints)
			{
				if (synchroPoint.length() + nbCharCurrentLine + 2 > Lnt.MAX_CHAR_PER_LINE)
				{
					stringBuilder.append(Constant.LINE_FEED).append(Utils.indent(23));
					nbCharCurrentLine = 23 + synchroPoint.length() + 2;
				}
				else
				{
					nbCharCurrentLine += synchroPoint.length() + 2;
				}

				stringBuilder.append(synchroPoint)
						.append(Constant.COMA_AND_SPACE);
			}

			if (nbCharCurrentLine + 7 > Lnt.MAX_CHAR_PER_LINE)
			{
				stringBuilder.append(Constant.LINE_FEED).
						append(Utils.indent(23));
			}

			stringBuilder.append(Bpmn.MOVE_ON_FLOW)
					.append(Constant.RIGHT_SQUARE_BRACKET)
					.append(Constant.LINE_FEED)
					.append(Utils.indent(22))
					.append(Constant.LEFT_PARENTHESIS)
					.append(Bpmn.ACTIVE_FLOWS_VARIABLE)
					.append(Constant.COMA_AND_SPACE)
					.append(Bpmn.BPMN_PROCESS_VARIABLE)
					.append(Constant.COMA_AND_SPACE)
					.append(Bpmn.SYNC_STORE_VARIABLE)
					.append(Constant.COMA_AND_SPACE)
					.append(Bpmn.MERGE_STORE_VARIABLE)
					.append(Constant.COMA_AND_SPACE)
					.append(Bpmn.PAR_STORE_VARIABLE)
					.append(Constant.RIGHT_PARENTHESIS)
					.append(Constant.LINE_FEED)
					.append(Utils.indentLNT(3))
					.append(Lnt.END_IF)
					.append(Constant.LINE_FEED)
					.append(Utils.indentLNT(2))
					.append(Lnt.ALT_OPERATOR)
					.append(Constant.LINE_FEED)
					.append(Utils.indentLNT(3))
					.append(Bpmn.MERGE_ID_VARIABLE)
					.append(Lnt.SPACED_VARIABLE_ASSIGNATION_OPERATOR)
					.append(Lnt.ANY)
					.append(Constant.SPACE)
					.append(Bpmn.ID_LNT_TYPE)
					.append(Constant.SPACE)
					.append(Lnt.WHERE)
					.append(Constant.SPACE)
					.append(Bpmn.MEMBER_LNT_FUNCTION)
					.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
					.append(Bpmn.MERGE_ID_VARIABLE)
					.append(Constant.COMA_AND_SPACE)
					.append(Bpmn.PAR_STORE_VARIABLE)
					.append(Constant.RIGHT_PARENTHESIS)
					.append(Lnt.SEQUENTIAL_COMPOSITION_OPERATOR)
					.append(Constant.LINE_FEED)
					.append(Utils.indentLNT(3))
					.append(Lnt.IF)
					.append(Constant.SPACE)
					.append(Bpmn.IS_MERGE_POSSIBLE_PAR_LNT_FUNCTION)
					.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
					.append(Bpmn.BPMN_PROCESS_VARIABLE)
					.append(Constant.COMA_AND_SPACE)
					.append(Bpmn.SYNC_STORE_VARIABLE)
					.append(Constant.COMA_AND_SPACE)
					.append(Bpmn.MERGE_ID_VARIABLE)
					.append(Constant.RIGHT_PARENTHESIS_AND_SPACE)
					.append(Lnt.THEN)
					.append(Constant.LINE_FEED);

			if (parJoinBegin.isEmpty())
			{
				stringBuilder.append(this.getSchedulerString(
					new ArrayList<>(),
					new ArrayList<>(),
					Bpmn.SYNC_STORE_VARIABLE,
					Bpmn.MERGE_STORE_VARIABLE,
					Bpmn.PAR_STORE_VARIABLE,
					12
				));
			}
			else
			{
				int i = 1;

				stringBuilder.append(Utils.indentLNT(4))
						.append(Lnt.ALT)
						.append(Constant.LINE_FEED);

				for (final String parJoin : parJoinBegin)
				{
					stringBuilder.append(parJoin);

					if (i < parJoinBegin.size())
					{
						stringBuilder.append(Constant.LINE_FEED)
								.append(Utils.indentLNT(4))
								.append(Lnt.ALT_OPERATOR)
								.append(Constant.LINE_FEED);
					}

					i++;
				}

				stringBuilder.append(Constant.LINE_FEED)
						.append(Utils.indentLNT(4))
						.append(Lnt.END_ALT);
			}

			stringBuilder.append(Constant.LINE_FEED)
					.append(Utils.indentLNT(3))
					.append(Lnt.ELSE)
					.append(Constant.LINE_FEED)
					.append(Utils.indentLNT(4))
					.append(Bpmn.SCHEDULER_IDENTIFIER)
					.append(Constant.SPACE_AND_LEFT_SQUARE_BRACKET);

			final int minIndent2 = 23;
			final Pair<String, Integer> flowMsgsAndLineLength2 = this.getFlowMsgsAndLineLength(minIndent2, minIndent2);
			stringBuilder.append(flowMsgsAndLineLength2.getLeft())
					.append(Constant.COMA_AND_SPACE);
			final ArrayList<String> synchroPoints2 = this.computeAdditionalSynchroPoints();
			nbCharCurrentLine = flowMsgsAndLineLength2.getRight() + 2;

			for (String synchroPoint : synchroPoints2)
			{
				if (synchroPoint.length() + nbCharCurrentLine + 2 > Lnt.MAX_CHAR_PER_LINE)
				{
					stringBuilder.append(Constant.LINE_FEED).append(Utils.indent(minIndent2));
					nbCharCurrentLine = minIndent2 + synchroPoint.length() + 2;
				}
				else
				{
					nbCharCurrentLine += synchroPoint.length() + 2;
				}

				stringBuilder.append(synchroPoint)
						.append(Constant.COMA_AND_SPACE);
			}

			if (nbCharCurrentLine + 7 > Lnt.MAX_CHAR_PER_LINE)
			{
				stringBuilder.append(Constant.LINE_FEED)
						.append(Utils.indent(minIndent2));
			}

			stringBuilder.append(Bpmn.MOVE_ON_FLOW)
					.append(Constant.RIGHT_SQUARE_BRACKET)
					.append(Constant.LINE_FEED)
					.append(Utils.indent(minIndent2 - 1))
					.append(Constant.LEFT_PARENTHESIS)
					.append(Bpmn.ACTIVE_FLOWS_VARIABLE)
					.append(Constant.COMA_AND_SPACE)
					.append(Bpmn.BPMN_PROCESS_VARIABLE)
					.append(Constant.COMA_AND_SPACE)
					.append(Bpmn.SYNC_STORE_VARIABLE)
					.append(Constant.COMA_AND_SPACE)
					.append(Bpmn.MERGE_STORE_VARIABLE)
					.append(Constant.COMA_AND_SPACE)
					.append(Bpmn.PAR_STORE_VARIABLE)
					.append(Constant.RIGHT_PARENTHESIS)
					.append(Constant.LINE_FEED)
					.append(Utils.indentLNT(3))
					.append(Lnt.END_IF)
					.append(Constant.LINE_FEED)
					.append(Utils.indentLNT(2))
					.append(Lnt.END_ALT)
					.append(Constant.LINE_FEED)
					.append(Utils.indentLNT(1))
					.append(Lnt.END_VAR)
					.append(Constant.LINE_FEED)
					.append(Lnt.END_PROCESS)
					.append(Constant.DOUBLE_LINE_FEED)
					.append(Lnt.STANDARD_SEPARATOR)
					.append(Constant.DOUBLE_LINE_FEED);
		}

		String getSchedulerString(final List<String> incIds,
								  final List<String> outIds,
								  final String syncString,
								  final String mergeStoreString,
								  final String parStoreString,
								  final int minIndent)
		{

			final String schedulerString = Bpmn.SCHEDULER_IDENTIFIER + Constant.SPACE_AND_LEFT_SQUARE_BRACKET +
					Lnt.ELLIPSIS + Constant.RIGHT_SQUARE_BRACKET_AND_SPACE + Constant.LEFT_PARENTHESIS;
			final String unionString = Bpmn.UNION_LNT_FUNCTION + Constant.SPACE_AND_LEFT_PARENTHESIS;
			final StringBuilder schedulerStringBuilder = new StringBuilder()
					.append(Utils.indent(minIndent))
					.append(schedulerString)
					.append(unionString)
					.append(Constant.LEFT_CURVY_BRACKET);

			int i = 1;
			int nbCharCurrentLine = schedulerStringBuilder.length();

			for (final String outId : outIds)
			{
				if (i == 1)
				{
					nbCharCurrentLine += outId.length();
				}
				else
				{
					if (nbCharCurrentLine + outId.length() > Lnt.MAX_CHAR_PER_LINE)
					{
						final int indentation = schedulerString.length() + unionString.length() + minIndent + 1;
						schedulerStringBuilder.append(Constant.LINE_FEED)
								.append(Utils.indent(indentation));
						nbCharCurrentLine = indentation + outId.length();
					}
					else
					{
						nbCharCurrentLine += outId.length();
					}
				}

				schedulerStringBuilder.append(outId);

				if (i < outIds.size())
				{
					schedulerStringBuilder.append(Constant.COMA_AND_SPACE);
					nbCharCurrentLine += 2;
				}

				i++;
			}

			schedulerStringBuilder.append(Constant.RIGHT_CURVY_BRACKET)
					.append(Constant.COMA_AND_SPACE);
			nbCharCurrentLine += 3;

			final String firstIncId = incIds.isEmpty() ? Constant.EMPTY_STRING : incIds.get(0);
			final String removeIdsString = Bpmn.REMOVE_IDS_FROM_SET_LNT_FUNCTION + Constant.LEFT_PARENTHESIS +
					Constant.LEFT_CURVY_BRACKET + firstIncId;

			if (nbCharCurrentLine + removeIdsString.length() > Lnt.MAX_CHAR_PER_LINE)
			{
				final int indentation = minIndent + schedulerString.length() + unionString.length();
				schedulerStringBuilder.append(Constant.LINE_FEED)
						.append(Utils.indent(indentation));
				nbCharCurrentLine = indentation + removeIdsString.length();
			}
			else
			{
				nbCharCurrentLine += removeIdsString.length();
			}

			schedulerStringBuilder.append(removeIdsString);
			final int removeIdsParamsMinIndent = nbCharCurrentLine - firstIncId.length() - 1;

			for (int j = 1; j < incIds.size(); j++)
			{
				final String incId = incIds.get(j);
				schedulerStringBuilder.append(Constant.COMA_AND_SPACE);
				nbCharCurrentLine += 2;

				if (nbCharCurrentLine + incId.length() > Lnt.MAX_CHAR_PER_LINE)
				{
					schedulerStringBuilder.append(Constant.LINE_FEED)
							.append(Utils.indent(removeIdsParamsMinIndent));
					nbCharCurrentLine = removeIdsParamsMinIndent + incId.length();
				}
				else
				{
					nbCharCurrentLine += incId.length();
				}

				schedulerStringBuilder.append(incId);
			}

			schedulerStringBuilder.append(Constant.RIGHT_CURVY_BRACKET)
					.append(Constant.COMA_AND_SPACE);
			nbCharCurrentLine += 3;

			final String activeFlows = Bpmn.ACTIVE_FLOWS_VARIABLE + Constant.RIGHT_PARENTHESIS +
					Constant.RIGHT_PARENTHESIS + Constant.COMA_AND_SPACE;

			if (nbCharCurrentLine + activeFlows.length() > Lnt.MAX_CHAR_PER_LINE)
			{
				schedulerStringBuilder.append(Constant.LINE_FEED)
						.append(Utils.indent(removeIdsParamsMinIndent));
				nbCharCurrentLine = removeIdsParamsMinIndent + activeFlows.length();
			}
			else
			{
				nbCharCurrentLine += activeFlows.length();
			}

			schedulerStringBuilder.append(activeFlows);

			if (nbCharCurrentLine + 6 > Lnt.MAX_CHAR_PER_LINE)
			{
				schedulerStringBuilder.append(Constant.LINE_FEED)
						.append(Utils.indent(minIndent + schedulerString.length()));
				nbCharCurrentLine = minIndent + schedulerString.length() + 6;
			}
			else
			{
				nbCharCurrentLine += 6;
			}

			schedulerStringBuilder.append(Bpmn.BPMN_PROCESS_VARIABLE)
					.append(Constant.COMA_AND_SPACE);

			if (nbCharCurrentLine + syncString.length() + 2 > Lnt.MAX_CHAR_PER_LINE)
			{
				schedulerStringBuilder.append(Constant.LINE_FEED)
						.append(Utils.indent(minIndent + schedulerString.length()));
				nbCharCurrentLine = minIndent + schedulerString.length() + syncString.length() + 2;
			}
			else
			{
				nbCharCurrentLine += syncString.length() + 2;
			}

			schedulerStringBuilder.append(syncString)
					.append(Constant.COMA_AND_SPACE);

			if (nbCharCurrentLine + mergeStoreString.length() + 2 > Lnt.MAX_CHAR_PER_LINE)
			{
				schedulerStringBuilder.append(Constant.LINE_FEED)
						.append(Utils.indent(minIndent + schedulerString.length()));
				nbCharCurrentLine = minIndent + schedulerString.length() + mergeStoreString.length() + 2;
			}
			else
			{
				nbCharCurrentLine += mergeStoreString.length() + 2;
			}

			schedulerStringBuilder.append(mergeStoreString)
					.append(Constant.COMA_AND_SPACE);

			if (nbCharCurrentLine + parStoreString.length() + 1 > Lnt.MAX_CHAR_PER_LINE)
			{
				schedulerStringBuilder.append(Constant.LINE_FEED)
						.append(Utils.indent(minIndent + schedulerString.length()));
			}

			schedulerStringBuilder.append(parStoreString)
					.append(Constant.RIGHT_PARENTHESIS);

			return schedulerStringBuilder.toString();
		}

		/**
		 * Generates file with process element ids
		 */
		void generateIdFile()
		{
			final StringBuilder idFileBuilder = new StringBuilder();
			final int nbIdentifiers = this.nodes.size() + this.finals.size() + this.flows.size() + 2;
			final String natBits = computeRequiredNatBits(nbIdentifiers);

			//Generates an ID type for all identifiers
			idFileBuilder.append(Lnt.MODULE)
					.append(Constant.SPACE)
					.append(Bpmn.ID_MODULE_NAME)
					.append(Constant.SPACE)
					.append(Lnt.WITH)
					.append(Constant.SPACE)
					.append(Lnt.GET)
					.append(Constant.COMA_AND_SPACE)
					.append(Lnt.LOWER_THAN_OPERATOR)
					.append(Constant.COMA_AND_SPACE)
					.append(Lnt.EQUALS_OPERATOR)
					.append(Constant.SPACE)
					.append(Lnt.IS)
					.append(natBits)
					.append(Constant.DOUBLE_LINE_FEED)
					.append(Lnt.OPEN_MULTILINE_COMMENTARY)
					.append(Constant.SPACE)
					.append("Data type for identifiers, useful for scheduling purposes")
					.append(Constant.SPACE)
					.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
					.append(Constant.LINE_FEED)
					.append(Lnt.TYPE)
					.append(Constant.SPACE)
					.append(Bpmn.ID_LNT_TYPE)
					.append(Constant.SPACE)
					.append(Lnt.IS)
					.append(Constant.LINE_FEED)
					.append(this.name);

			for (final Node node : this.nodes)
			{
				idFileBuilder.append(Constant.COMA)
						.append(Constant.LINE_FEED)
						.append(node.identifier());
			}

			idFileBuilder.append(Constant.COMA)
					.append(Constant.LINE_FEED)
					.append(this.initial.identifier());

			for (final Node finalNode : this.finals)
			{
				idFileBuilder.append(Constant.COMA)
						.append(Constant.LINE_FEED)
						.append(finalNode.identifier());
			}

			for (final Flow flow : this.flows)
			{
				idFileBuilder.append(Constant.COMA)
						.append(Constant.LINE_FEED)
						.append(flow.identifier());
			}

			idFileBuilder.append(Constant.COMA_AND_SPACE)
					.append(Bpmn.DUMMY_ID)
					.append(Constant.LINE_FEED)
					.append(Lnt.WITH)
					.append(Constant.SPACE)
					.append(Lnt.EQUALS_OPERATOR)
					.append(Constant.COMA_AND_SPACE)
					.append(Lnt.NOT_EQUALS_OPERATOR)
					.append(Constant.LINE_FEED)
					.append(Lnt.END_TYPE)
					.append(Constant.DOUBLE_LINE_FEED)
					.append(Lnt.END_MODULE)
					.append(Constant.LINE_FEED);

			final File file = new File(outputFolder + File.separator + Constant.ID_FILENAME);
			final PrintWriter printWriter;

			try
			{
				printWriter = new PrintWriter(file);
				printWriter.write(idFileBuilder.toString());
				printWriter.flush();
				printWriter.close();
			}
			catch (FileNotFoundException e)
			{
				throw new RuntimeException(e);
			}
		}

		//Generates an LNT module and process for a BPMN 2.0 process
		void genLNT()
		{
			if (!isBalanced)
			{
				this.generateIdFile();
			}

			final String fileName = this.name + Lnt.FILE_EXTENSION;
			final File file = new File(outputFolder + File.separator + fileName);
			final StringBuilder lntBuilder = new StringBuilder();
			final String natBits = isBalanced ? computeRequiredNatBits(this.flows.size()) : Constant.EMPTY_STRING;

			lntBuilder.append(Lnt.MODULE)
					.append(Constant.SPACE)
					.append(this.name)
					.append(isBalanced ? Constant.EMPTY_STRING : Constant.LEFT_PARENTHESIS + Bpmn.TYPES_LNT_MODULE + Constant.RIGHT_PARENTHESIS)
					.append(Constant.SPACE)
					.append(Lnt.WITH)
					.append(Constant.SPACE)
					.append(Lnt.GET)
					.append(Constant.COMA_AND_SPACE)
					.append(Lnt.LOWER_THAN_OPERATOR)
					.append(Constant.COMA_AND_SPACE)
					.append(Lnt.EQUALS_OPERATOR)
					.append(Constant.SPACE)
					.append(Lnt.IS)
					.append(natBits)
					.append(Constant.DOUBLE_LINE_FEED)
					.append(Lnt.STANDARD_SEPARATOR)
					.append(Constant.DOUBLE_LINE_FEED);

			if (isBalanced)
			{
				//Generates an ID type for all flow identifiers
				lntBuilder.append(Lnt.OPEN_MULTILINE_COMMENTARY)
						.append(Constant.SPACE)
						.append("Data type for flow identifiers, useful for scheduling purposes")
						.append(Constant.SPACE)
						.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
						.append(Constant.LINE_FEED)
						.append(Lnt.TYPE)
						.append(Constant.SPACE)
						.append(Bpmn.ID_LNT_TYPE)
						.append(Constant.SPACE)
						.append(Lnt.IS)
						.append(Constant.LINE_FEED)
						.append(Utils.indentLNT(1));

				int counter = this.flows.size();

				for (final Flow flow : this.flows)
				{
					lntBuilder.append(flow.identifier());
					counter--;

					if (counter > 0)
					{
						lntBuilder.append(Constant.COMA)
								.append(Constant.LINE_FEED)
								.append(Utils.indentLNT(1));
					}
				}

				lntBuilder.append(Constant.LINE_FEED)
						.append(Utils.indentLNT(1))
						.append(Lnt.WITH)
						.append(Constant.SPACE)
						.append(Lnt.EQUALS_OPERATOR)
						.append(Constant.COMA_AND_SPACE)
						.append(Lnt.NOT_EQUALS_OPERATOR)
						.append(Constant.LINE_FEED)
						.append(Lnt.END_TYPE)
						.append(Constant.DOUBLE_LINE_FEED)
						.append(Lnt.STANDARD_SEPARATOR)
						.append(Constant.DOUBLE_LINE_FEED);
			}

			if (this.initial != null)
			{
				this.initial.writeLnt(lntBuilder, 0);
			}

			//Generates one process for final events and events, this is enough because generic processes
			if (!this.finals.isEmpty())
			{
				this.finals.get(0).writeLnt(lntBuilder, 0);
			}

			if (!this.flows.isEmpty())
			{
				this.flows.get(0).writeLnt(lntBuilder); //TODO: ConditionalFlow?
			}

			//Generates LNT processes for all other nodes
			final ArrayList<String> specialNodes = new ArrayList<>(); //We keep track of nodes that need to be translated only once

			for (Node n : this.nodes)
			{
				if (n instanceof Interaction
					|| n instanceof MessageSending
					|| n instanceof MessageReception
					|| n instanceof Task)
				{
					if (!specialNodes.contains(n.getClass().getName()))
					{
						if (n instanceof Task)
						{
							//a task is identified with its number of incoming and outgoing flows
							final StringBuilder classNameBuilder = new StringBuilder(n.getClass().getName())
									.append(Constant.UNDERSCORE)
									.append(n.incomingFlows().size())
									.append(Constant.UNDERSCORE)
									.append(n.outgoingFlows().size());

							if (!specialNodes.contains(classNameBuilder.toString()))
							{
								specialNodes.add(classNameBuilder.toString());
								n.writeLnt(lntBuilder, 0);
							}
						}
						else
						{
							specialNodes.add(n.getClass().getName());
							n.writeLnt(lntBuilder, 0);
						}
					}
				}
				else
				{
					n.writeLnt(lntBuilder, 0);
				}
			}

			/*
				Note: up to here, translation patterns are independent of the actual tasks, comm, etc.
				The actual names will be used only in the MAIN process when computing the process alphabet
				and instantiating processes.
			 */

			if (!isBalanced)
			{
				//Scheduler process generation
				this.generateScheduler(lntBuilder);

				//Generate process
				this.processDump(lntBuilder);
			}

			lntBuilder.append(Lnt.PROCESS)
					.append(Constant.SPACE)
					.append(Bpmn.MAIN_LNT_PROCESS_IDENTIFIER)
					.append(Constant.SPACE);

			final ArrayList<String> processAlphabet = this.getProcessAlphabet();
			final boolean lineJumped = dumpAlphabet(processAlphabet, lntBuilder, true);

			lntBuilder.append(lineJumped ? Constant.LINE_FEED : Constant.SPACE)
					.append(Lnt.IS)
					.append(Constant.LINE_FEED);

			//Computes additional synchros for or splits/joins
			final ArrayList<String> additionalSynchroPoints = this.computeAdditionalSynchroPoints();

			lntBuilder.append(Utils.indentLNT(1))
					.append(Lnt.HIDE)
					.append(Constant.LINE_FEED)
					.append(Utils.indentLNT(2))
					.append(Bpmn.BEGIN)
					.append(Constant.COMA_AND_SPACE)
					.append(Bpmn.FINISH);

			final int indentBase;

			int nbCharCurrentLine = 21;

			if (isBalanced)
			{
				indentBase = 0;

				if (!this.flows.isEmpty())
				{
					lntBuilder.append(Constant.COMA_AND_SPACE);
					nbCharCurrentLine += 2;
					int cter = 1;

					for (final Flow flow : this.flows)
					{
						final String beginFlowIdentifier = flow.identifier() + Constant.UNDERSCORE + Bpmn.BEGIN + Constant.COMA_AND_SPACE;;
						final String finishFlowIdentifier = flow.identifier() + Constant.UNDERSCORE + Bpmn.FINISH;

						if (nbCharCurrentLine + beginFlowIdentifier.length() > Lnt.MAX_CHAR_PER_LINE)
						{
							lntBuilder.append(Constant.LINE_FEED)
									.append(Utils.indentLNT(2));
							nbCharCurrentLine = 6 + beginFlowIdentifier.length();
						}
						else
						{
							nbCharCurrentLine += beginFlowIdentifier.length();
						}

						lntBuilder.append(beginFlowIdentifier);

						if (nbCharCurrentLine + finishFlowIdentifier.length() + 1 > Lnt.MAX_CHAR_PER_LINE)
						{
							lntBuilder.append(Constant.LINE_FEED)
									.append(Utils.indentLNT(2));
							nbCharCurrentLine = 6 + finishFlowIdentifier.length();
						}
						else
						{
							nbCharCurrentLine += finishFlowIdentifier.length();
						}

						lntBuilder.append(finishFlowIdentifier);
						cter++;

						if (cter <= this.flows.size())
						{
							lntBuilder.append(Constant.COMA_AND_SPACE);
							nbCharCurrentLine += 2;
							//we hide additional synchros for or splits/joins as well
						}
					}

					int nb = 0;

					if (!additionalSynchroPoints.isEmpty())
					{
						lntBuilder.append(Constant.COMA_AND_SPACE);
						nbCharCurrentLine += 2;

						for (String synchroPoint : additionalSynchroPoints)
						{
							if (nbCharCurrentLine + synchroPoint.length() + 1 > Lnt.MAX_CHAR_PER_LINE)
							{
								lntBuilder.append(Constant.LINE_FEED)
										.append(Utils.indentLNT(2));
								nbCharCurrentLine = 6 + synchroPoint.length();
							}
							else
							{
								nbCharCurrentLine += synchroPoint.length();
							}

							lntBuilder.append(synchroPoint);
							nb++;

							if (nb < additionalSynchroPoints.size())
							{
								lntBuilder.append(Constant.COMA_AND_SPACE);
								nbCharCurrentLine += 2;
							}
						}
					}
				}

				lntBuilder.append(Constant.COLON_AND_SPACE)
						.append(Lnt.ANY)
						.append(Constant.LINE_FEED)
						.append(Utils.indentLNT(1))
						.append(Lnt.IN)
						.append(Constant.LINE_FEED)
						.append(Utils.indentLNT(2))
						.append(Lnt.PAR)
						.append(Constant.LINE_FEED)
						.append(Utils.indentLNT(3));

				//Synchronizations on all begin/finish flows
				nbCharCurrentLine = 9;

				if (!this.flows.isEmpty())
				{
					int cter = 1;

					for (final Flow flow : this.flows)
					{
						final String beginFlowIdentifier = flow.identifier() + Constant.UNDERSCORE + Bpmn.BEGIN + Constant.COMA_AND_SPACE;
						final String finishFlowIdentifier = flow.identifier() + Constant.UNDERSCORE + Bpmn.FINISH;

						if (nbCharCurrentLine + beginFlowIdentifier.length() > Lnt.MAX_CHAR_PER_LINE)
						{
							lntBuilder.append(Constant.LINE_FEED)
									.append(Utils.indentLNT(3));
							nbCharCurrentLine = 9 + beginFlowIdentifier.length();
						}
						else
						{
							nbCharCurrentLine += beginFlowIdentifier.length();
						}

						lntBuilder.append(beginFlowIdentifier);

						if (nbCharCurrentLine + finishFlowIdentifier.length() + 1 > Lnt.MAX_CHAR_PER_LINE)
						{
							lntBuilder.append(Constant.LINE_FEED)
									.append(Utils.indentLNT(3));
							nbCharCurrentLine = 9 + finishFlowIdentifier.length();
						}
						else
						{
							nbCharCurrentLine += finishFlowIdentifier.length();
						}

						lntBuilder.append(finishFlowIdentifier);
						cter++;

						if (cter <= this.flows.size())
						{
							lntBuilder.append(Constant.COMA_AND_SPACE);
							nbCharCurrentLine += 2;
						}
					}
				}
			}
			else
			{
				indentBase = 3;

				if (!this.flows.isEmpty())
				{
					final Pair<String, Integer> flowMsgsAndLineLength = this.getFlowMsgsAndLineLength(6, 15);

					lntBuilder.append(Constant.COMA_AND_SPACE)
							.append(flowMsgsAndLineLength.getLeft())
							.append(Constant.COMA_AND_SPACE);

					nbCharCurrentLine = flowMsgsAndLineLength.getRight();

					//We hide additional synchros for or splits/joins as well
					if (!additionalSynchroPoints.isEmpty())
					{
						nbCharCurrentLine += 2;

						for (final String additionalSynchroPoint : additionalSynchroPoints)
						{
							if (nbCharCurrentLine + additionalSynchroPoint.length() + 2 > Lnt.MAX_CHAR_PER_LINE)
							{
								lntBuilder.append(Constant.LINE_FEED).append(Utils.indentLNT(2));
								nbCharCurrentLine = additionalSynchroPoint.length() + 8;
							}
							else
							{
								nbCharCurrentLine += additionalSynchroPoint.length() + 2;
							}

							lntBuilder.append(additionalSynchroPoint)
									.append(Constant.COMA_AND_SPACE);
						}
					}
				}

				lntBuilder.append(Bpmn.MOVE_ON_FLOW)
						.append(Constant.COLON_AND_SPACE)
						.append(Lnt.ANY)
						.append(Constant.LINE_FEED)
						.append(Utils.indentLNT(1))
						.append(Lnt.IN)
						.append(Constant.LINE_FEED)
						.append(Utils.indentLNT(2))
						.append(Lnt.PAR)
						.append(Constant.LINE_FEED)
						.append(Utils.indentLNT(3))
						.append(Bpmn.MOVE_ON_FLOW)
						.append(Constant.COMA_AND_SPACE);

				//We start with the scheduler
				//Synchronization on all begin/finish flows
				nbCharCurrentLine = 17;

				if (!this.flows.isEmpty())
				{
					final Pair<String, Integer> flowMsgsAndLineLength = this.getFlowMsgsAndLineLength(9, 17);
					lntBuilder.append(flowMsgsAndLineLength.getLeft());
					nbCharCurrentLine = flowMsgsAndLineLength.getRight();
				}

				for (String synchroPoint : additionalSynchroPoints)
				{
					lntBuilder.append(Constant.COMA_AND_SPACE);
					nbCharCurrentLine += 2;

					if (nbCharCurrentLine + synchroPoint.length() + 2 > Lnt.MAX_CHAR_PER_LINE)
					{
						lntBuilder.append(Constant.LINE_FEED)
								.append(Utils.indentLNT(3));
						nbCharCurrentLine = synchroPoint.length() + 9;
					}
					else
					{
						nbCharCurrentLine += synchroPoint.length();
					}

					lntBuilder.append(synchroPoint);
				}

				lntBuilder.append(Constant.LINE_FEED)
						.append(Utils.indentLNT(2))
						.append(Lnt.IN)
						.append(Constant.LINE_FEED)
						.append(Utils.indentLNT(3))
						.append(Lnt.OPEN_MULTILINE_COMMENTARY)
						.append(Constant.SPACE)
						.append("we first generate the scheduler, necessary for keeping")
						.append(Constant.LINE_FEED)
						.append(Utils.indentLNT(3))
						.append("track of tokens, and triggering inclusive merge gateways")
						.append(Constant.SPACE)
						.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
						.append(Constant.DOUBLE_LINE_FEED)
						.append(Utils.indentLNT(3))
						.append(Bpmn.SCHEDULER_IDENTIFIER)
						.append(Constant.SPACE_AND_LEFT_SQUARE_BRACKET)
						.append(Lnt.ELLIPSIS)
						.append(Constant.RIGHT_SQUARE_BRACKET_AND_SPACE)
						.append(Constant.LEFT_PARENTHESIS)
						.append(Lnt.EMPTY_LIST)
						.append(Constant.COMA_AND_SPACE)
						.append(Bpmn.MAIN_FUNCTION_IDENTIFIER)
						.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
						.append(Constant.RIGHT_PARENTHESIS)
						.append(Constant.COMA_AND_SPACE)
						.append(Lnt.EMPTY_LIST)
						.append(Constant.COMA_AND_SPACE)
						.append(Lnt.EMPTY_LIST)
						.append(Constant.COMA_AND_SPACE)
						.append(Lnt.EMPTY_LIST)
						.append(Constant.RIGHT_PARENTHESIS)
						.append(Constant.LINE_FEED)
						.append(Utils.indentLNT(2))
						.append(Lnt.PAR_OPERATOR)
						.append(Constant.LINE_FEED)
						.append(Utils.indentLNT(3))
						.append(Lnt.PAR)
						.append(Constant.SPACE)
						.append(Lnt.OPEN_MULTILINE_COMMENTARY)
						.append(Constant.SPACE)
						.append("synchronizations on all begin/finish flow messages")
						.append(Constant.SPACE)
						.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
						.append(Constant.LINE_FEED);

				//Synchronizations on all begin/finish flows
				if (!this.flows.isEmpty())
				{
					lntBuilder.append(Utils.indentLNT(4))
							.append(this.getFlowMsgsAndLineLength(12, 12).getLeft());
				}
			}

			lntBuilder.append(Constant.LINE_FEED)
					.append(Utils.indentLNT(2))
					.append(Utils.indent(indentBase))
					.append(Lnt.IN)
					.append(Constant.LINE_FEED)
					.append(Utils.indentLNT(3))
					.append(Utils.indent(indentBase));

			//Interleaving of all flow processes
			lntBuilder.append(Lnt.PAR)
					.append(Constant.SPACE)
					.append(Lnt.OPEN_MULTILINE_COMMENTARY)
					.append(Constant.SPACE)
					.append("we then generate interleaving of all flow processes")
					.append(Constant.SPACE)
					.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
					.append(Constant.LINE_FEED);

			int cter = 1;

			for (final Flow flow : this.flows)
			{
				nbCharCurrentLine = 18 + indentBase;
				//TODO: take conditional flows into account
				lntBuilder.append(Utils.indentLNT(4))
						.append(Utils.indent(indentBase))
						.append(Bpmn.FLOW)
						.append(Constant.SPACE_AND_LEFT_SQUARE_BRACKET);

				final String beginIdentifier = flow.identifier() + Constant.UNDERSCORE + Bpmn.BEGIN + Constant.COMA_AND_SPACE;
				final String finishIdentifier = flow.identifier() + Constant.UNDERSCORE + Bpmn.FINISH + Constant.RIGHT_SQUARE_BRACKET;

				lntBuilder.append(beginIdentifier);
				nbCharCurrentLine += beginIdentifier.length();

				if (nbCharCurrentLine + finishIdentifier.length() > Lnt.MAX_CHAR_PER_LINE)
				{
					lntBuilder.append(Constant.LINE_FEED)
							.append(Utils.indent(18 + indentBase));
					nbCharCurrentLine = 18 + indentBase + finishIdentifier.length();
				}
				else
				{
					nbCharCurrentLine += finishIdentifier.length();
				}

				lntBuilder.append(finishIdentifier);

				//final String paramIdentifier = " (" + flow.identifier() + ")";

				if (nbCharCurrentLine + flow.identifier().length() + 3 > Lnt.MAX_CHAR_PER_LINE)
				{
					lntBuilder.append(Constant.LINE_FEED)
							.append(Utils.indent(16 + indentBase));
				}

				lntBuilder.append(Constant.SPACE_AND_LEFT_PARENTHESIS)
						.append(flow.identifier())
						.append(Constant.RIGHT_PARENTHESIS);
				cter++;

				if (cter <= this.flows.size())
				{
					lntBuilder.append(Constant.LINE_FEED)
							.append(Utils.indentLNT(3))
							.append(Utils.indent(indentBase))
							.append(Lnt.PAR_OPERATOR)
							.append(Constant.LINE_FEED);
				}
			}

			lntBuilder.append(Constant.LINE_FEED)
					.append(Utils.indentLNT(3))
					.append(Utils.indent(indentBase))
					.append(Lnt.END_PAR)
					.append(Constant.LINE_FEED)
					.append(Utils.indentLNT(2))
					.append(Utils.indent(indentBase))
					.append(Lnt.PAR_OPERATOR)
					.append(Constant.LINE_FEED);

			//Interleaving of all node processes
			lntBuilder.append(Utils.indentLNT(3))
					.append(Utils.indent(indentBase))
					.append(Lnt.PAR)
					.append(Constant.SPACE)
					.append(Lnt.OPEN_MULTILINE_COMMENTARY)
					.append(Constant.SPACE)
					.append("we finally generate interleaving of all node processes")
					.append(Constant.SPACE)
					.append(Lnt.CLOSE_MULTILINE_COMMENTARY)
					.append(Constant.LINE_FEED)
					.append(Utils.indentLNT(4))
					.append(Utils.indent(indentBase));

			//Process instantiation for initial node
			final String beginIdentifier = this.initial.outgoingFlows().get(0).identifier() + Constant.UNDERSCORE +
					Bpmn.BEGIN + Constant.RIGHT_SQUARE_BRACKET;

			lntBuilder.append(Bpmn.INITIAL_EVENT)
					.append(Constant.SPACE_AND_LEFT_SQUARE_BRACKET)
					.append(Bpmn.BEGIN)
					.append(Constant.COMA_AND_SPACE); //We assume a single output flow

			nbCharCurrentLine = 25 + indentBase;

			if (nbCharCurrentLine + beginIdentifier.length() > Lnt.MAX_CHAR_PER_LINE)
			{
				lntBuilder.append(Constant.LINE_FEED)
						.append(Utils.indent(18 + indentBase));
			}

			lntBuilder.append(beginIdentifier)
					.append(Constant.LINE_FEED)
					.append(Utils.indentLNT(3))
					.append(Utils.indent(indentBase))
					.append(Lnt.PAR_OPERATOR)
					.append(Constant.LINE_FEED);

			cter = 1;

			//Processes instantiations for final nodes
			for (final Node finalNode : this.finals)
			{
				final String finalNodeWithIdentifier = Bpmn.END_EVENT + Constant.SPACE_AND_LEFT_SQUARE_BRACKET +
						finalNode.incomingFlows().get(0).identifier() + Constant.UNDERSCORE + Bpmn.FINISH +
						Constant.COMA_AND_SPACE;

				lntBuilder.append(Utils.indentLNT(4))
						.append(Utils.indent(indentBase))
						.append(finalNodeWithIdentifier);

				nbCharCurrentLine = finalNodeWithIdentifier.length() + 12 + indentBase;

				if (nbCharCurrentLine + 7 > Lnt.MAX_CHAR_PER_LINE)
				{
					lntBuilder.append(Constant.LINE_FEED)
							.append(Utils.indent(24))
							.append(Utils.indent(indentBase));
				}

				lntBuilder.append(Bpmn.FINISH)
						.append(Constant.RIGHT_SQUARE_BRACKET); //We assume a single incoming flow

				cter++;

				if (cter <= this.flows.size())
				{
					lntBuilder.append(Constant.LINE_FEED)
							.append(Utils.indentLNT(3))
							.append(Utils.indent(indentBase))
							.append(Lnt.PAR_OPERATOR)
							.append(Constant.LINE_FEED);
				}
			}

			//Processes instantiations for all other nodes
			cter = 1;

			for (final Node node : this.nodes)
			{
				lntBuilder.append(Utils.indentLNT(4))
						.append(Utils.indent(indentBase));
				node.writeMainLnt(lntBuilder, 12 + indentBase);
				cter++;

				if (cter <= this.nodes.size())
				{
					lntBuilder.append(Constant.LINE_FEED)
							.append(Utils.indentLNT(3))
							.append(Utils.indent(indentBase))
							.append(Lnt.PAR_OPERATOR)
							.append(Constant.LINE_FEED);
				}
			}

			lntBuilder.append(Constant.LINE_FEED)
					.append(Utils.indentLNT(3))
					.append(Utils.indent(indentBase))
					.append(Lnt.END_PAR)
					.append(Constant.LINE_FEED)
					.append(Utils.indentLNT(2))
					.append(Utils.indent(indentBase))
					.append(Lnt.END_PAR)
					.append(Constant.LINE_FEED);

			if (!isBalanced)
			{
				lntBuilder.append(Utils.indentLNT(2))
						.append(Lnt.END_PAR)
						.append(Constant.LINE_FEED);
			}

			lntBuilder.append(Utils.indentLNT(1))
					.append(Lnt.END_HIDE)
					.append(Constant.LINE_FEED)
					.append(Lnt.END_PROCESS)
					.append(Constant.DOUBLE_LINE_FEED)
					.append(Lnt.STANDARD_SEPARATOR)
					.append(Constant.DOUBLE_LINE_FEED)
					.append(Lnt.END_MODULE)
					.append(Constant.LINE_FEED);

			final PrintWriter printWriter;

			try
			{
				printWriter = new PrintWriter(file);
				printWriter.write(lntBuilder.toString());
				printWriter.flush();
				printWriter.close();
			}
			catch (FileNotFoundException e)
			{
				throw new RuntimeException(e);
			}
		}

		/**
		 * Generates an SVL file
		 */
		void genSVL()
		{
			final String fileName = this.name + Svl.FILE_EXTENSION;
			final StringBuilder svlCommandBuilder = new StringBuilder()
					.append(Svl.SHELL_LINE_STARTING_SYMBOL)
					.append(Constant.SPACE)
					.append(Svl.CAESAR_OPEN_OPTIONS)
					.append(Constant.EQUALS)
					.append(Constant.DOUBLE_QUOTATION_MARK)
					.append(CaesarOpenOption.SILENT)
					.append(Constant.SPACE)
					.append(CaesarOpenOption.WARNING)
					.append(Constant.DOUBLE_QUOTATION_MARK)
					.append(Constant.LINE_FEED)
					.append(Svl.SHELL_LINE_STARTING_SYMBOL)
					.append(Constant.SPACE)
					.append(Svl.CAESAR_OPTIONS)
					.append(Constant.EQUALS)
					.append(Constant.DOUBLE_QUOTATION_MARK)
					.append(CaesarOption.MORE)
					.append(Constant.SPACE)
					.append(CaesarOption.CAT)
					.append(Constant.SPACE)
					.append(CaesarOption.GARBAGE_COLLECTION)
					.append(Constant.DOUBLE_QUOTATION_MARK)
					.append(Constant.DOUBLE_LINE_FEED)
					.append(Svl.SHELL_LINE_STARTING_SYMBOL)
					.append(Constant.SPACE)
					.append(Svl.DEFAULT_PROCESS_FILE)
					.append(Constant.EQUALS)
					.append(Constant.DOUBLE_QUOTATION_MARK)
					.append(this.name)
					.append(Lnt.FILE_EXTENSION)
					.append(Constant.DOUBLE_QUOTATION_MARK)
					.append(Constant.DOUBLE_LINE_FEED)
					.append(Constant.DOUBLE_QUOTATION_MARK)
					.append(this.name)
					.append(Constant.UNDERSCORE)
					.append(Constant.RAW)
					.append(Bcg.FILE_EXTENSION)
					.append(Constant.DOUBLE_QUOTATION_MARK_AND_SPACE)
					.append(Constant.EQUALS)
					.append(Constant.SPACE)
					.append(Svl.GENERATION)
					.append(Svl.SPACED_OF)
					.append(Constant.DOUBLE_QUOTATION_MARK)
					.append(Lnt.MAIN);

			final ArrayList<String> processAlphabet = this.getProcessAlphabet();
			dumpAlphabet(processAlphabet, svlCommandBuilder, false);

			svlCommandBuilder.append(Constant.DOUBLE_QUOTATION_MARK)
					.append(Svl.SEQUENTIAL_COMPOSITION_OPERATOR)
					.append(Constant.DOUBLE_LINE_FEED)
					.append(Constant.DOUBLE_QUOTATION_MARK)
					.append(this.name)
					.append(Bcg.FILE_EXTENSION)
					.append(Constant.DOUBLE_QUOTATION_MARK)
					.append(Constant.SPACE)
					.append(Constant.EQUALS)
					.append(Constant.SPACE)
					.append(Svl.BRANCHING_REDUCTION)
					.append(Svl.SPACED_OF)
					.append(Constant.DOUBLE_QUOTATION_MARK)
					.append(this.name)
					.append(Constant.UNDERSCORE)
					.append(Constant.RAW)
					.append(Bcg.FILE_EXTENSION)
					.append(Constant.DOUBLE_QUOTATION_MARK)
					.append(Svl.SEQUENTIAL_COMPOSITION_OPERATOR)
					.append(Constant.DOUBLE_LINE_FEED);

			final File svlFile = new File(outputFolder + File.separator + fileName);

			//System.out.println("Absolute path: " + svlFile.getAbsolutePath());
			//System.out.println("Working Directory = " + System.getProperty("user.dir"));

			final PrintWriter printWriter;

			try
			{
				printWriter = new PrintWriter(svlFile);
				printWriter.write(svlCommandBuilder.toString());
				printWriter.flush();
				printWriter.close();
			}
			catch (FileNotFoundException e)
			{
				throw new RuntimeException(e);
			}

			if (!svlFile.setExecutable(true))
			{
				throw new IllegalStateException("Unable to make the SVL script executable. Please check your rights" +
						" on the current working directory.");
			}
		}

		/**
		 * This method takes as input a file.pif and generates a PIF Python object
		 *
		 * @param file the pif file
		 */
		void buildProcessFromFile(final File file)
		{
			//Open XML document specified in the filename
			final fr.inria.convecs.optimus.pif.Process process;

			try
			{
				final JAXBContext jaxbContext = JAXBContext.newInstance(fr.inria.convecs.optimus.pif.Process.class);
				final Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
				process = (fr.inria.convecs.optimus.pif.Process) jaxbUnmarshaller.unmarshal(file);
			}
			catch (JAXBException e)
			{
				System.out.println("An error occured while parsing xml document \"" + file.getName() + "\".");
				System.out.println("Unrecognized element, the message was \"" + e.getMessage() + "\".");
				throw new RuntimeException(e);
			}

			this.name = process.getName();

			//We first create all nodes without incoming/outgoing flows
			for (final WorkflowNode workflowNode : process.getBehaviour().getNodes())
			{
				//Initial and final events
				if (workflowNode instanceof fr.inria.convecs.optimus.pif.InitialEvent)
				{
					this.initial = new InitialEvent(
						workflowNode.getId(),
						new ArrayList<>(),
						new ArrayList<>()
					);
				}
				if (workflowNode instanceof fr.inria.convecs.optimus.pif.EndEvent)
				{
					this.finals.add(new EndEvent(
						workflowNode.getId(),
						new ArrayList<>(),
						new ArrayList<>())
					);
				}

				//Tasks / Emissions / Receptions / Interactions
				if (workflowNode instanceof fr.inria.convecs.optimus.pif.Task)
				{
					this.nodes.add(new Task(
						workflowNode.getId(),
						new ArrayList<>(),
						new ArrayList<>())
					);
				}
				if (workflowNode instanceof fr.inria.convecs.optimus.pif.MessageSending
					&& isBalanced)
				{
					this.nodes.add(new MessageSending(
						workflowNode.getId(),
						new ArrayList<>(),
						new ArrayList<>(),
						((fr.inria.convecs.optimus.pif.MessageSending) workflowNode).getMessage().getId()
					));
				}
				if (workflowNode instanceof fr.inria.convecs.optimus.pif.MessageReception
					&& isBalanced)
				{
					this.nodes.add(new MessageReception(
						workflowNode.getId(),
						new ArrayList<>(),
						new ArrayList<>(),
						((fr.inria.convecs.optimus.pif.MessageReception) workflowNode).getMessage().getId()
					));
				}
				if (workflowNode instanceof fr.inria.convecs.optimus.pif.Interaction
					&& isBalanced)
				{
					final ArrayList<String> receivingPeers = new ArrayList<>();

					for (JAXBElement<Object> JAXBObject : ((fr.inria.convecs.optimus.pif.Interaction) workflowNode).getReceivingPeers())
					{
						final Peer peer = (Peer) JAXBObject.getValue();
						receivingPeers.add(peer.getId());
					}

					this.nodes.add(new Interaction(
						workflowNode.getId(),
						new ArrayList<>(),
						new ArrayList<>(),
						((fr.inria.convecs.optimus.pif.Interaction) workflowNode).getMessage().getId(),
						((fr.inria.convecs.optimus.pif.Interaction) workflowNode).getInitiatingPeer().getId(),
						receivingPeers
					));
				}

				//Split gateways
				if (workflowNode instanceof fr.inria.convecs.optimus.pif.AndSplitGateway)
				{
					this.nodes.add(new AndSplitGateway(
						workflowNode.getId(),
						new ArrayList<>(),
						new ArrayList<>()
					));
				}
				if (workflowNode instanceof fr.inria.convecs.optimus.pif.OrSplitGateway)
				{
					this.nodes.add(new OrSplitGateway(
						workflowNode.getId(),
						new ArrayList<>(),
						new ArrayList<>()
					));
				}
				if (workflowNode instanceof fr.inria.convecs.optimus.pif.XOrSplitGateway)
				{
					this.nodes.add(new XOrSplitGateway(
						workflowNode.getId(),
						new ArrayList<>(),
						new ArrayList<>()
					));
				}

				//Join gateways
				if (workflowNode instanceof fr.inria.convecs.optimus.pif.AndJoinGateway)
				{
					this.nodes.add(new AndJoinGateway(
						workflowNode.getId(),
						new ArrayList<>(),
						new ArrayList<>()
					));
				}
				if (workflowNode instanceof fr.inria.convecs.optimus.pif.OrJoinGateway)
				{
					this.nodes.add(new OrJoinGateway(
						workflowNode.getId(),
						new ArrayList<>(),
						new ArrayList<>()
					));
				}
				if (workflowNode instanceof fr.inria.convecs.optimus.pif.XOrJoinGateway)
				{
					this.nodes.add(new XOrJoinGateway(
						workflowNode.getId(),
						new ArrayList<>(),
						new ArrayList<>()
					));
				}
			}

			//Creation of flow objects
			for (final SequenceFlow sequenceFlow : process.getBehaviour().getSequenceFlows())
			{
				final Flow flow = new Flow(
					sequenceFlow.getId(),
					this.getNode(sequenceFlow.getSource().getId()),
					this.getNode(sequenceFlow.getTarget().getId())
				);
				this.flows.add(flow);
				this.addFlow(flow);
			}
		}
	}

	private String computeRequiredNatBits(final int nbIdentifiers)
	{
		if (nbIdentifiers <= (1 << Cadp.DEFAULT_NAT_BITS))
		{
			return Constant.EMPTY_STRING;
		}

		int nbIncrement = 1;

		while (nbIdentifiers > (1 << (Cadp.DEFAULT_NAT_BITS + nbIncrement)))
		{
			nbIncrement++;
		}

		return Constant.SPACE + Lnt.PRAGMA_NAT_BITS + Constant.SPACE + (Cadp.DEFAULT_NAT_BITS + nbIncrement);
	}

	/**
	 * Computes the LTS model (BCG file) for a PIF model.
	 *
	 * @param pifFileName    is the name of the PIF file.
	 * @param smartReduction is true if a smart reduction is done on the LTS when loading it, false otherwise.
	 * @param debug          is true if debug information are displayed, false otherwise.
	 * @return (Integer, String, Collection < String >), return code, name of the model
	 * (can be different from the filename) and its alphabet.
	 */
	@Override
	public Triple<Integer, String, Collection<String>> generate(final File pifFileName,
																final boolean generateLTS,
																final boolean smartReduction,
																final boolean debug)
	{
		final Process process = new Process();
		//Load PIF model
		process.buildProcessFromFile(pifFileName);
		final String pifModelName = process.name();
		//Pre-processing: compute correspondences between or splits/joins
		process.reachableOrJoin();

		if (!isBalanced)
		{
			//Check for cycles in process involving inclusive gateway
			final boolean cycleExists = process.checkInclusiveCycle();

			if (cycleExists)
			{
				return Triple.of(ReturnCode.TERMINATION_UNBALANCED_INCLUSIVE_CYCLE, pifModelName, process.getProcessAlphabet());
			}
		}

		//Generate the LNT code for the model
		process.genLNT();

		if (generateLTS)
		{
			//Compute the LTS from the LNT code using SVL, possibly with a smart reduction
			process.genSVL();

			final CommandManager commandManager = new CommandManager(Command.SVL, new File(outputFolder), pifModelName);

			try
			{
				commandManager.execute();

				if (commandManager.returnValue() != ReturnCode.TERMINATION_OK)
				{
					final String errorMessage = ErrorUtils.generateCommandErrorMessage(
						ErrorUtils.inlineCommandAndArgs(Command.SVL, pifModelName),
						new File(outputFolder),
						commandManager.stdErr()
					);

					ErrorUtils.writeErrorFile(new File(outputFolder), errorMessage);
					logger.error(errorMessage);
				}
			}
			catch (IOException | InterruptedException e)
			{
				final String errorMessage = ErrorUtils.generateCommandErrorMessage(
					ErrorUtils.inlineCommandAndArgs(Command.SVL, pifModelName),
					new File(outputFolder),
					e.toString()
				);

				ErrorUtils.writeErrorFile(new File(outputFolder), errorMessage);
				logger.error(errorMessage);

				return Triple.of(ReturnCode.TERMINATION_ERROR, pifModelName, process.getProcessAlphabet());
			}
		}

		return Triple.of(ReturnCode.TERMINATION_OK, pifModelName, process.getProcessAlphabet()); //TODO use return value from SVL call
	}

	/**
	 * Gets the name and the alphabet of the LTS for the PIF model.
	 *
	 * @param pifFileName    is the name of the PIF file.
	 * @param smartReduction is true if a smart reduction is done on the LTS when loading it, false otherwise.
	 * @param debug          is true if debug information are displayed, false otherwise.
	 * @return (Integer, String, Collection < String >), return code, name of the model
	 * (can be different from the filename) and its alphabet.
	 */
	@Override
	public Triple<Integer, String, Collection<String>> load(final File pifFileName,
															final boolean generateLTS,
															final boolean smartReduction,
															final boolean debug)
	{
		final Process process = new Process();
		process.buildProcessFromFile(pifFileName);
		final String pifModelName = process.name();
		final String ltsFileName = process.name() + Bcg.FILE_EXTENSION;

		if (this.needsRebuild(pifFileName, ltsFileName))
		{
			return this.generate(pifFileName, generateLTS, smartReduction, debug);
		}
		else
		{
			return Triple.of(ReturnCode.TERMINATION_OK, pifModelName, process.getProcessAlphabet());
		}
	}
}
