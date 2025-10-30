package fr.inria.convecs.optimus.transformer;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.activiti.bpmn.BpmnAutoLayout;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ajayk
 */
public class BpmnContentTransformer implements ContentTransformer
{
	private static final Logger logger = LoggerFactory.getLogger(BpmnContentTransformer.class);
	private final String input;
	private String output;

	public BpmnContentTransformer(final String input)
	{
		this.input = input;
	}

	@Override
	public void transform()
	{
		InputStream inputstream = null;

		try
		{
			inputstream = IOUtils.toInputStream(this.input, StandardCharsets.UTF_8.name());
			final BpmnXMLConverter converter = new BpmnXMLConverter();
			final XMLInputFactory factory = XMLInputFactory.newInstance();
			final XMLStreamReader reader = factory.createXMLStreamReader(inputstream);
			final BpmnModel model = converter.convertToBpmnModel(reader);
			new BpmnAutoLayout(model).execute();
			final byte[] bpmnXml = new BpmnXMLConverter().convertToXML(model);
			this.output = new String(bpmnXml);
			inputstream.close();
		}
		catch (XMLStreamException | IOException ioe)
		{
			try
			{
				inputstream.close();
			}
			catch (IOException e)
			{
				throw new RuntimeException(e);
			}

			logger.error("Error transforming the input", ioe);
			throw new RuntimeException(ioe);
		}
	}

	@Override
	public void generateOutput()
	{
		// TODO Auto-generated method stub
	}
	
	public String getBpmnLayout()
	{
		return output;
	}
}
