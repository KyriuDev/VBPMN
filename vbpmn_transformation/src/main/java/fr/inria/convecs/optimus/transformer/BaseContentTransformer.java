package fr.inria.convecs.optimus.transformer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;

import org.codehaus.stax2.XMLOutputFactory2;
import org.codehaus.stax2.XMLStreamWriter2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.inria.convecs.optimus.model.Node;
import fr.inria.convecs.optimus.model.Node.NodeType;
import fr.inria.convecs.optimus.model.Process;
import fr.inria.convecs.optimus.model.Sequence;

/**
 * @author ajayk
 *
 */
public class BaseContentTransformer implements ContentTransformer
{
	private static final String PIF_PREFIX = "pif";
	private static final Logger logger = LoggerFactory.getLogger(BaseContentTransformer.class);
	private static final String PIF_URI = "http://www.example.org/PIF";
	private static final String XSI_URI = "http://www.w3.org/2001/XMLSchema-instance";
	private final Process process;
	private final File output;

	public BaseContentTransformer(final Process process,
								  final File output)
	{
		this.process = process;
		this.output = output;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see fr.inria.convecs.optimus.transformer.ContentTransformer#transform()
	 */
	@Override
	public void transform()
	{
		XMLStreamWriter2 xmlStreamWriter = null;

		try
		{
			final XMLOutputFactory2 xmlOutputFactory = (XMLOutputFactory2) XMLOutputFactory.newFactory();
			xmlStreamWriter = (XMLStreamWriter2) xmlOutputFactory.createXMLStreamWriter(new FileWriter(output));
			xmlStreamWriter.writeStartDocument("utf-8", "1.0");
			xmlStreamWriter.setPrefix(PIF_PREFIX, PIF_URI);
			xmlStreamWriter.setPrefix("xsi", XSI_URI);
			xmlStreamWriter.writeStartElement(PIF_URI, "Process");
			xmlStreamWriter.writeNamespace(PIF_PREFIX, PIF_URI);
			xmlStreamWriter.writeNamespace("xsi", XSI_URI);
			this.writeElements(xmlStreamWriter);
			xmlStreamWriter.writeEndElement();
			xmlStreamWriter.writeEndDocument();
			xmlStreamWriter.closeCompletely();
		}
		catch (XMLStreamException | IOException ioe)
		{
			output.delete();

			if (xmlStreamWriter != null)
			{
				try
				{
					xmlStreamWriter.closeCompletely();
				}
				catch (XMLStreamException e)
				{
					throw new RuntimeException(e);
				}
			}

			logger.error("Error transforming the input", ioe);
			throw new RuntimeException(ioe);
		}
	}

	private void writeElements(final XMLStreamWriter2 xmlStreamWriter) throws XMLStreamException
	{
		xmlStreamWriter.writeStartElement(PIF_URI, "name");
		xmlStreamWriter.writeCharacters(this.process.getId());
		xmlStreamWriter.writeEndElement();

		// TODO: Handle the documentation
		xmlStreamWriter.writeStartElement(PIF_URI, "documentation");
		xmlStreamWriter.writeCharacters("Dummy text for documentation");
		xmlStreamWriter.writeEndElement();
		xmlStreamWriter.writeStartElement(PIF_URI, "behaviour");

		this.writeNodes(xmlStreamWriter);
		this.writeSequenceFlows(xmlStreamWriter);

		// InitialNode
		for (final Node node : this.process.getNodes(NodeType.INITIAL_EVENT))
		{
			xmlStreamWriter.writeStartElement(PIF_URI, "initialNode");
			xmlStreamWriter.writeCharacters(node.getId());
			xmlStreamWriter.writeEndElement();
		}

		for (final Node node : this.process.getNodes(NodeType.END_EVENT))
		{
			xmlStreamWriter.writeStartElement(PIF_URI, "finalNodes");
			xmlStreamWriter.writeCharacters(node.getId());
			xmlStreamWriter.writeEndElement();
		}

		xmlStreamWriter.writeEndElement();
	}

	private void writeSequenceFlows(final XMLStreamWriter2 xmlStreamWriter) throws XMLStreamException
	{
		final List<Sequence> sequenceList = this.process.getSequences();

		for (final Sequence sequence : sequenceList)
		{
			xmlStreamWriter.writeStartElement(PIF_URI, "sequenceFlows");
			xmlStreamWriter.writeAttribute("id", sequence.getId());
			xmlStreamWriter.writeAttribute("source", sequence.getSource());
			xmlStreamWriter.writeAttribute("target", sequence.getTarget());
			xmlStreamWriter.writeEndElement();
		}
	}

	private void writeNodes(final XMLStreamWriter2 xmlStreamWriter) throws XMLStreamException
	{
		final List<Node> nodeList = this.process.getNodes();

		for (final Node node : nodeList)
		{
			xmlStreamWriter.writeStartElement(PIF_URI, "nodes");
			xmlStreamWriter.writeAttribute("id", node.getId());
			xmlStreamWriter.writeAttribute(XSI_URI, "type", PIF_PREFIX + ":" + node.getType().toString());

			// incoming flows
			final List<String> incomingFlows = node.getIncomingFlows();

			if (incomingFlows != null)
			{
				for (final String flow : incomingFlows)
				{
					xmlStreamWriter.writeStartElement(PIF_URI, "incomingFlows");
					xmlStreamWriter.writeCharacters(flow);
					xmlStreamWriter.writeEndElement();
				}
			}

			// outgoing flows
			final List<String> outgoingFlows = node.getOutgoingFlows();

			if (outgoingFlows != null)
			{
				for (final String flow : outgoingFlows)
				{
					xmlStreamWriter.writeStartElement(PIF_URI, "outgoingFlows");
					xmlStreamWriter.writeCharacters(flow);
					xmlStreamWriter.writeEndElement();
				}
			}

			xmlStreamWriter.writeEndElement();
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see fr.inria.convecs.optimus.transformer.ContentTransformer#generateOutput()
	 */
	@Override
	public void generateOutput()
	{
		//TODO: implement handling of any specific type of output
	}
}
