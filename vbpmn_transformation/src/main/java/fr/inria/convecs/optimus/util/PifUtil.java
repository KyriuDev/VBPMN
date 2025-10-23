package fr.inria.convecs.optimus.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.inria.convecs.optimus.pif.Gateway;
import fr.inria.convecs.optimus.pif.OrJoinGateway;
import fr.inria.convecs.optimus.pif.OrSplitGateway;
import fr.inria.convecs.optimus.pif.Process;
import fr.inria.convecs.optimus.pif.SequenceFlow;
import fr.inria.convecs.optimus.pif.SplitGateway;
import fr.inria.convecs.optimus.pif.WorkflowNode;

/**
 * @author ajayk
 *
 */
public class PifUtil
{
	private static final Logger logger = LoggerFactory.getLogger(PifUtil.class);

	public static Boolean isPifBalanced(final File pifFile)
	{
		final Boolean result;

		try
		{
			final JAXBContext jaxbContext = JAXBContext.newInstance(Process.class);
			final Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			final Process process = (Process) jaxbUnmarshaller.unmarshal(pifFile);
			result = PifUtil.isProcessBalanced(process);
		}
		catch(JAXBException e)
		{
			logger.warn("Unable to check if the pif file is balanced", e);
			throw new IllegalStateException("Unable to check if the pif file is balanced.");
		}

		return result;
	}

	public static Boolean isProcessBalanced(final Process process)
	{
		final Stack<Gateway> gatewayStack = new Stack<>();
		final Boolean result;

		try
		{
			final List<WorkflowNode> gateways = process.getBehaviour().getNodes().stream()
					.filter(wfNode -> (wfNode instanceof Gateway))
					.collect(Collectors.toList());
			final WorkflowNode initial = process.getBehaviour().getInitialNode();
			final List<WorkflowNode> visited = new ArrayList<>();
			result = PifUtil.traverseNodes(initial, visited, gatewayStack);
		}
		catch(Exception e)
		{
			logger.warn("Unable to check if the process is balanced", e);
			throw new IllegalStateException("Unable to check if the pif file is balanced.");
		}

		return result;
	}

	private static Boolean traverseNodes(final WorkflowNode initial,
										 final List<WorkflowNode> visited,
										 final Stack<Gateway> gatewayStack)
	{
		if (initial == null)
		{
			return true;
		}

		if (!visited.contains(initial))
		{
			visited.add(initial);
			//logger.debug("Processing workflownode: {}", initial.getId());

			final List<JAXBElement<Object>> seqFlows = initial.getOutgoingFlows();

			if (seqFlows.isEmpty())
			{
				return gatewayStack.isEmpty();
			}

			for (final JAXBElement<Object> flowElement: seqFlows)
			{
				final SequenceFlow flow = (SequenceFlow) flowElement.getValue();
				final WorkflowNode targetNode = flow.getTarget();

				if (targetNode instanceof OrSplitGateway)
				{
					//logger.debug("Push SplitGateway: {}", targetNode.getId());
					gatewayStack.push((Gateway) targetNode);
				}

				if (targetNode instanceof OrJoinGateway)
				{
					//logger.debug("Found JoinGateway: {}", targetNode.getId());
					final Gateway sourceSplit = gatewayStack.pop();

					if (sourceSplit instanceof OrSplitGateway)
					{
						if (targetNode.getIncomingFlows().size() != sourceSplit.getOutgoingFlows().size())
						{
							return false;
						}
					}
				}

				return PifUtil.traverseNodes(targetNode, visited, gatewayStack);
			}
		}
		else
		{
			//Loop inclusivegateway
			//logger.warn("Found an inclusive gateway with loop!");
			return false;
		}

		return true;
	}
}
