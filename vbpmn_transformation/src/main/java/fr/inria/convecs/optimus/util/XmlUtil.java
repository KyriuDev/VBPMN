package fr.inria.convecs.optimus.util;

import java.io.File;

import javax.xml.stream.XMLInputFactory;

import org.codehaus.stax2.XMLInputFactory2;
import org.codehaus.stax2.XMLStreamReader2;
import org.codehaus.stax2.validation.XMLValidationSchema;
import org.codehaus.stax2.validation.XMLValidationSchemaFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ajayk
 *
 */
public class XmlUtil
{
	private static final Logger logger = LoggerFactory.getLogger(XmlUtil.class);

	public static boolean isDocumentValid(final File inputXml,
										  final File schema)
	{
		boolean isValid = false;

		try
		{
			final XMLValidationSchemaFactory schemaFactory = XMLValidationSchemaFactory.newInstance(XMLValidationSchema.SCHEMA_ID_W3C_SCHEMA);
			final XMLValidationSchema xsd = schemaFactory.createSchema(schema);
			final XMLInputFactory2 inputFactory = (XMLInputFactory2) XMLInputFactory.newInstance();
			final XMLStreamReader2 streamReader = inputFactory.createXMLStreamReader(inputXml);
			streamReader.validateAgainst(xsd);

			while (streamReader.hasNext())
			{
				streamReader.next();
			}

			streamReader.closeCompletely();
			isValid = true;
		}
		catch (Exception e)
		{
			logger.warn("Error while validating {} against the schema {}", inputXml.getName(), schema.getName(), e);
		}

		return isValid;
	}
}
