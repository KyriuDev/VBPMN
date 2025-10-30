/**
 * 
 */

package fr.inria.convecs.optimus.service;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.servlet.ServletContext;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.inria.convecs.optimus.config.AppProperty;
import fr.inria.convecs.optimus.model.Process;
import fr.inria.convecs.optimus.parser.BaseContentHandler;
import fr.inria.convecs.optimus.parser.ContentHandler;
import fr.inria.convecs.optimus.transformer.BaseContentTransformer;
import fr.inria.convecs.optimus.transformer.ContentTransformer;
import fr.inria.convecs.optimus.util.VbpmnExceptionMapper;
import fr.inria.convecs.optimus.util.XmlUtil;
import fr.inria.convecs.optimus.validator.ModelValidator;
import fr.inria.convecs.optimus.validator.VbpmnValidator;

/**
 * @author silverquick TODO: dirty implementation - add a resource interface to invoke service,
 *         implement JSON data.
 *
 */
@Path("/validate")
public class ValidationService
{
	private static final Logger logger = LoggerFactory.getLogger(ValidationService.class);
	private static final String OUTPUT_PATH = AppProperty.getInstance().getFolder("OUTPUT_PATH");
	private static final String SCRIPTS_PATH = "/WEB-INF/classes";
	private static final String PIF_SCHEMA = "/pif.xsd";

	@Context
	ServletContext servletContext;

	/**
	 * This service is called by the VBPMN web interface when clicking on the "Submit" button of the
	 * "Model Comparison" page.
	 *
	 * @param fileStream1
	 * @param fileInfo1
	 * @param formData
	 * @return
	 */
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces("text/plain")
	@Path("/bpmn")
	public Response validateVbpmn(@FormDataParam("file1") final InputStream fileStream1,
								  @FormDataParam("file1") final FormDataContentDisposition fileInfo1,
								  @FormDataParam("file2") final InputStream fileStream2,
								  @FormDataParam("file2") final FormDataContentDisposition fileInfo2,
								  final FormDataMultiPart formData)
	{
		final Response httpResponse;

		try
		{
			final String outputDirPath = Files.createTempDirectory(Paths.get(OUTPUT_PATH), "vbpmn_").toAbsolutePath().toString();
			final File file1 = new File(outputDirPath + File.separator + fileInfo1.getFileName());
			final File file2 = new File(outputDirPath + File.separator + fileInfo2.getFileName());

			Files.copy(fileStream1, file1.toPath(), StandardCopyOption.REPLACE_EXISTING);
			Files.copy(fileStream2, file2.toPath(), StandardCopyOption.REPLACE_EXISTING);

			final List<File> fileList = new ArrayList<>();
			fileList.add(file1);
			fileList.add(file2);

			httpResponse = this.processRequest(fileList, formData, outputDirPath);
		}
		catch (Exception e)
		{
			logger.error("Error processing request: ", e);
			throw VbpmnExceptionMapper.createWebAppException(e);
		}

		return httpResponse;
	}

	/**
	 * This service is called by the VBPMN web interface when clicking on the "Submit" button of the
	 * "Verify Properties" page.
	 *
	 * @param fileStream1
	 * @param fileInfo1
	 * @param formData
	 * @return
	 */
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces("text/plain")
	@Path("/verify")
	public Response verifyProperties(@FormDataParam("file1") final InputStream fileStream1,
									 @FormDataParam("file1") final FormDataContentDisposition fileInfo1,
									 final FormDataMultiPart formData)
	{
		final Response httpResponse;

		try
		{
			final String outputDirPath = Files.createTempDirectory(Paths.get(OUTPUT_PATH), "vbpmn_").toAbsolutePath().toString();
			final File file1 = new File(outputDirPath + File.separator + fileInfo1.getFileName());

			Files.copy(fileStream1, file1.toPath(), StandardCopyOption.REPLACE_EXISTING);

			final List<File> fileList = new ArrayList<>();
			fileList.add(file1);

			httpResponse = this.processRequest(fileList, formData, outputDirPath);
		}
		catch (Exception e)
		{
			logger.error("Error processing request: ", e);
			throw VbpmnExceptionMapper.createWebAppException(e);
		}

		return httpResponse;
	}

	// TODO:Cleanup
	private Response processRequest(final List<File> fileList,
									final FormDataMultiPart formData,
									final String outputDir)
	{
		final String scriptsPath = this.servletContext.getRealPath(SCRIPTS_PATH);
		final Response httpResponse;

		try
		{
			if (fileList.isEmpty()
				|| fileList.size() > 2)
			{
				httpResponse = Response.status(Response.Status.BAD_REQUEST)
										.entity("You can only specify 1 or 2 files")
										.build();
			}
			else
			{
				final ModelValidator validator = new VbpmnValidator(scriptsPath, outputDir);
				final String result;
				final List<String> operationMode = new ArrayList<>();

				if (fileList.size() == 2)
				{
					final String mode = formData.getField("mode").getValue();
					final String option = formData.getField("option").getValue();

					operationMode.add(mode);

					if ("property-and".equals(mode)
						|| "property-implied".equals(mode))
					{
						final String formula = formData.getField("formula").getValue();
						operationMode.add("--formula");
						operationMode.add(formula);
					}
					else if ("hiding".equals(option))
					{
						final String hidingValue = formData.getField("hidingVal").getValue();

						if (hidingValue != null
							&& !hidingValue.isEmpty())
						{
							operationMode.add("--hiding");
							operationMode.add(hidingValue);

							if (formData.getField("exposeMode") != null)
							{
								operationMode.add("--exposemode");
							}
						}

						final String renameValue = formData.getField("renameVal").getValue();

						if (renameValue != null
							&& !renameValue.isEmpty())
						{
							final String renameOption = formData.getField("renameOption").getValue();
							operationMode.add("--renaming");
							operationMode.add(renameValue);

							if (!"none".equals(renameOption))
							{
								operationMode.add("--renamed");
								operationMode.add(renameOption);
							}
						}
					}

					final File input1 = this.parseAndTransform(fileList.get(0));
					final File input2 = this.parseAndTransform(fileList.get(1));
					validator.validateV2(input1, input2, operationMode);
				} 
				else 
				{
					final String formula = formData.getField("formula").getValue();
					operationMode.add("property-implied");
					operationMode.add("--formula");
					operationMode.add(formula);
					
					final File input1 = parseAndTransform(fileList.get(0));
					validator.validateV2(input1, operationMode);
				}

				result = validator.getResult();
				httpResponse = Response.status(Response.Status.OK)
										.header("Access-Control-Allow-Origin", "*")
										.header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
				            			.entity(result)
										.build();
			}
		}
		catch (Exception e)
		{
			logger.error("Exception while invoking VBPMN", e);
			throw new RuntimeException(e);
		}

		return httpResponse;
	}

	private File parseAndTransform(final File input)
	{
		try
		{
			final String pifSchema = Objects.requireNonNull(ValidationService.class.getResource(PIF_SCHEMA)).getFile();
			final ContentHandler baseHandler = new BaseContentHandler(input);
			baseHandler.handle();
			final Process processOutput = (Process) baseHandler.getOutput();
			final String outputFileName = input.getParentFile().getAbsolutePath() + File.separator + processOutput.getId() + ".pif";
			final File outputFile = new File(outputFileName);
			final ContentTransformer baseTransformer = new BaseContentTransformer(processOutput, outputFile);
			baseTransformer.transform();

			if (XmlUtil.isDocumentValid(outputFile, new File(pifSchema)))
			{
				return outputFile;
			}
			else
			{
				throw new RuntimeException("Unable to transform the file <Schema Validation Error>: " + input.getName());
			}
		}
		catch (Exception e)
		{
			logger.error("Unable to parse and transform the file ", e);
			throw new RuntimeException(e);
		}
	}
}
