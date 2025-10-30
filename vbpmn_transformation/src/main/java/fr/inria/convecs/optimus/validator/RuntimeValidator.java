package fr.inria.convecs.optimus.validator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.inria.convecs.optimus.util.CommandExecutor;
import fr.inria.convecs.optimus.util.PifUtil;

/**
 * @author silverquick
 *
 */
public class RuntimeValidator implements ModelValidator
{
	private static final Logger logger = LoggerFactory.getLogger(RuntimeValidator.class);
	private final String scriptsFolder;
	private final String outputFolder;
	private String result;

	public RuntimeValidator(final String scriptsFolder,
							final String outputFolder)
	{
		this.scriptsFolder = scriptsFolder;
		this.outputFolder = outputFolder;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.inria.convecs.optimus.validator.ModelValidator#validate(java.io.File, java.lang.String)
	 */
	@Override
	public void validateV2(final File modelFile,
						   final List<String> options)
	{

		if (true)
		{
			//TODO USED TO VERIFY THAT IT IS NOT USED
			throw new UnsupportedOperationException();
		}

		this.validateV2(modelFile, modelFile, options);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.inria.convecs.optimus.validator.ModelValidator#validate(java.io.File, java.io.File,
	 * java.lang.String)
	 */
	@Override
	public void validateV2(final File modelFile1,
						   final File modelFile2,
						   final List<String> options)
	{
		final Boolean isBalanced = PifUtil.isPifBalanced(modelFile1);
		logger.debug("The input isBalanced? : {}", isBalanced);

		final List<String> vbpmnCommand = new ArrayList<>();
		vbpmnCommand.add("python");
		vbpmnCommand.add(scriptsFolder + File.separator + "vbpmn2.py");
		vbpmnCommand.add(modelFile1.getAbsolutePath());
		vbpmnCommand.add(modelFile2.getAbsolutePath());
		vbpmnCommand.addAll(options);
		logger.debug("The command is: {}", vbpmnCommand);

		try
		{
			final File outputDirectory = new File(outputFolder);
			Files.copy(
				new File(scriptsFolder + File.separator + "bpmntypes.lnt").toPath(),
				new File(outputFolder + File.separator + "bpmntypes.lnt").toPath()
			);
			final CommandExecutor commandExecutor = new CommandExecutor(vbpmnCommand, outputDirectory);
			final int execResult = commandExecutor.executeCommand();
			logger.debug("The return value of execution of command is: {}", execResult);

			final String response = handleResponse(commandExecutor.getOutput().trim(), commandExecutor.getErrors().trim());
			final StringBuilder resultBuilder = new StringBuilder();

			if ("TRUE".equalsIgnoreCase(response)
				|| "FALSE".equalsIgnoreCase(response))
			{
				resultBuilder.append(response)
						.append("|");
				final String dotModel1 = this.generateAutFile(modelFile1.getAbsolutePath().replace(".pif", ".bcg"));
				resultBuilder.append(dotModel1).append("|");

				if ("FALSE".equalsIgnoreCase(response))
				{
					final String bcgFileName;

					if (options.contains("property-implied")
						|| options.contains("property-and"))
					{
						bcgFileName = "evaluator.bcg";
					}
					else
					{
						bcgFileName = "bisimulator.bcg";
					}

					final File bcgFile = new File(outputFolder + File.separator + bcgFileName);
					final String autBcg = this.generateAutFile(bcgFile.getAbsolutePath());
					resultBuilder.append("|").append(autBcg);
				}

				this.result = resultBuilder.toString();
			}
			else 
			{
				this.result = response;
			}
		}
		catch (Exception e)
		{
			logger.error("Failed executing the command", e);
			throw new RuntimeException(e);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.inria.convecs.optimus.validator.ModelValidator#getResult()
	 */
	@Override
	public String getResult()
	{
		return this.result;
	}

	/**
	 * 
	 * @param
	 * @return
	 */
	private String handleResponse(final String stdOut,
								  final String stdErr)
	{
		final StringBuilder responseBuilder = new StringBuilder();

		if (stdErr != null
			&& !stdErr.isEmpty())
		{
			logger.debug("The stderr of command execution: {}", stdErr);
			responseBuilder.append("Std error executing the command: ")
					.append(stdErr);
		} 
		else if (stdOut != null
				&& !stdOut.isEmpty())
		{
			logger.debug("The stdout of command execution: {}", stdOut);

			// TODO: crude method -cleaner approach required
			if (stdOut.contains("ERROR")) 
			{
				responseBuilder.append("Internal error executing the command: ").append(stdOut);
			} 
			else 
			{
				final List<String> resp = Arrays.asList(stdOut.split("\n"));
				final List<String> respLastLines = new ArrayList<>(resp.subList(Math.max(0, resp.size() - 3), resp.size()));
				responseBuilder.append(respLastLines);
			}
		} 
		else 
		{
			responseBuilder.append("*** Unable to process request - No Result Available ***");
		}

		return responseBuilder.toString();
	}

	private String generateAutFile(final String absolutePath) throws IOException
	{
		final String autfile = absolutePath.replace(".bcg", ".aut");
		logger.debug("aut file: {}", autfile);

		final List<String> command = new ArrayList<>();
		command.add("bcg_io");
		command.add(absolutePath);
		command.add(autfile);

		final CommandExecutor commandExecutor = new CommandExecutor(command, new File(this.outputFolder));
		final int execResult = commandExecutor.executeCommand();

		logger.debug("The exec result of command [ {} ] is {}", command, execResult);

		if (execResult != 0)
		{
			throw new RuntimeException("Erorr executing BCG draw - " + commandExecutor.getErrors());
		}

		final File outputFile = new File(autfile);
		final String dotOutput = FileUtils.readFileToString(outputFile, "UTF-8").replaceAll("\\R", " "); // Java 8 carriage return replace

		return dotOutput.trim();
	}
}
