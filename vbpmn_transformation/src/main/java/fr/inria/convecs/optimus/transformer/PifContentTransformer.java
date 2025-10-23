package fr.inria.convecs.optimus.transformer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import org.activiti.bpmn.BpmnAutoLayout;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.inria.convecs.optimus.pif.AndJoinGateway;
import fr.inria.convecs.optimus.pif.AndSplitGateway;
import fr.inria.convecs.optimus.pif.EndEvent;
import fr.inria.convecs.optimus.pif.InitialEvent;
import fr.inria.convecs.optimus.pif.OrJoinGateway;
import fr.inria.convecs.optimus.pif.OrSplitGateway;
import fr.inria.convecs.optimus.pif.Process;
import fr.inria.convecs.optimus.pif.SequenceFlow;
import fr.inria.convecs.optimus.pif.Task;
import fr.inria.convecs.optimus.pif.WorkflowNode;
import fr.inria.convecs.optimus.pif.XOrJoinGateway;
import fr.inria.convecs.optimus.pif.XOrSplitGateway;
import fr.inria.convecs.optimus.util.BpmnBuilder;

/**
 * @author ajayk Transforms PIF to BPMN
 */
public class PifContentTransformer implements ContentTransformer
{
	private static final Logger logger = LoggerFactory.getLogger(PifContentTransformer.class);
	private final File pifInput;
	private final File bpmnOutput;

	public PifContentTransformer(final File pifInput,
								 final File bpmnOutput)
	{
		this.pifInput = pifInput;
		this.bpmnOutput = bpmnOutput;
	}

	/*
	 * (non-Javadoc) Generates BPMN 2.0 XML from PIF TODO: Quick dirty
	 * implementation - refine
	 */
	@Override
	public void transform()
	{
		final JAXBContext jaxbContext;

		try
		{
			jaxbContext = JAXBContext.newInstance(Process.class);
			final Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			final Process pifProcess = (Process) jaxbUnmarshaller.unmarshal(this.pifInput);
			final BpmnModel model = new BpmnModel();
			final org.activiti.bpmn.model.Process bpmProcess = new org.activiti.bpmn.model.Process();
			final BpmnBuilder modelBuilder = new BpmnBuilder();
			bpmProcess.setId(pifProcess.getName());
			bpmProcess.setName(pifProcess.getName());

			final InitialEvent startNode = pifProcess.getBehaviour().getInitialNode();
			bpmProcess.addFlowElement(modelBuilder.createStartEvent(startNode.getId()));

			final List<JAXBElement<Object>> endNodes = pifProcess.getBehaviour().getFinalNodes();

			for (final JAXBElement<Object> node : endNodes)
			{
				final EndEvent event = (EndEvent) node.getValue();
				bpmProcess.addFlowElement(modelBuilder.createEndEvent(event.getId()));
			}

			final List<WorkflowNode> pifNodes = pifProcess.getBehaviour().getNodes();

			for (final WorkflowNode wfNode : pifNodes)
			{
				if (wfNode instanceof Task)
				{
					final Task task = (Task) wfNode;
					bpmProcess.addFlowElement(modelBuilder.createUserTask(task.getId(), task.getId()));
				}
				else if (wfNode instanceof AndJoinGateway)
				{
					final AndJoinGateway gateway = (AndJoinGateway) wfNode;
					bpmProcess.addFlowElement(modelBuilder.createParallelGateway(gateway.getId(), gateway.getId(), null));
				}
				else if (wfNode instanceof AndSplitGateway)
				{
					final AndSplitGateway gateway = (AndSplitGateway) wfNode;
					bpmProcess.addFlowElement(modelBuilder.createParallelGateway(gateway.getId(), gateway.getId(), null));
				}
				else if (wfNode instanceof XOrJoinGateway)
				{
					final XOrJoinGateway gateway = (XOrJoinGateway) wfNode;
					bpmProcess.addFlowElement(modelBuilder.createExclusiveGateway(gateway.getId(), gateway.getId(), null));
				}
				else if (wfNode instanceof XOrSplitGateway)
				{
					final XOrSplitGateway gateway = (XOrSplitGateway) wfNode;
					bpmProcess.addFlowElement(modelBuilder.createExclusiveGateway(gateway.getId(), gateway.getId(), null));
				}
				else if (wfNode instanceof OrJoinGateway)
				{
					final OrJoinGateway gateway = (OrJoinGateway) wfNode;
					bpmProcess.addFlowElement(modelBuilder.createInclusiveGateway(gateway.getId(), gateway.getId(), null));
				}
				else if (wfNode instanceof OrSplitGateway)
				{
					final OrSplitGateway gateway = (OrSplitGateway) wfNode;
					bpmProcess.addFlowElement(modelBuilder.createInclusiveGateway(gateway.getId(), gateway.getId(), null));
				}
				else
				{
					logger.error("Unable to determine the PIF node instance - {}: {}", wfNode.getId(), wfNode.getClass().getName());
				}
			}

			final List<SequenceFlow> flows = pifProcess.getBehaviour().getSequenceFlows();

			for (final SequenceFlow flow : flows)
			{
				final org.activiti.bpmn.model.SequenceFlow bpmFlow = modelBuilder.createSequenceFlow(
					flow.getSource().getId(),
					flow.getTarget().getId()
				);
				bpmProcess.addFlowElement(bpmFlow);
			}

			model.addProcess(bpmProcess);
			new BpmnAutoLayout(model).execute();
			final byte[] bpmnXml = new BpmnXMLConverter().convertToXML(model);
			final FileOutputStream fileOutputStream = new FileOutputStream(bpmnOutput);
			IOUtils.write(bpmnXml, fileOutputStream);
			fileOutputStream.close();
		}
		catch (JAXBException | IOException e)
		{
			bpmnOutput.delete();
			logger.error("Error transforming the input", e);
			throw new RuntimeException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.inria.convecs.optimus.transformer.ContentTransformer#generateOutput()
	 */
	@Override
	public void generateOutput()
	{
		// TODO Auto-generated method stub
	}
}
