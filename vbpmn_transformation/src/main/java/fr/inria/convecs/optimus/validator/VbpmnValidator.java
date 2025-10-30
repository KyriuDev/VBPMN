package fr.inria.convecs.optimus.validator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fr.inria.convecs.optimus.py_to_java.Vbpmn;
import fr.inria.convecs.optimus.util.CommandManager;
import fr.inria.convecs.optimus.util.ErrorUtils;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.inria.convecs.optimus.util.CommandExecutor;
import fr.inria.convecs.optimus.util.PifUtil;

/**
 * @author silverquick
 *
 */
public class VbpmnValidator implements ModelValidator
{
	private static final Logger logger = LoggerFactory.getLogger(VbpmnValidator.class);
	private final String scriptsFolder;
	private final String outputFolder;
	private String result;

	public VbpmnValidator(final String scriptsFolder,
						  final String outputFolder)
	{
		this.scriptsFolder = scriptsFolder;
		this.outputFolder = outputFolder;
		System.out.println("OUTPUT FOLDER: " + this.outputFolder);
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
		this.validateV2(modelFile, modelFile, options);
	}

	public void validateV2(final File modelFile1,
						   final File modelFile2,
						   final List<String> options)
	{
		final boolean isBalanced = PifUtil.isPifBalanced(modelFile1) && PifUtil.isPifBalanced(modelFile2);
		logger.debug("The input is balanced: {}", isBalanced);

		final ArrayList<String> command = new ArrayList<>();
		command.add(modelFile1.getAbsolutePath());
		command.add(modelFile2.getAbsolutePath());
		command.addAll(options);
		logger.debug("The command is: {}", command);

		final Vbpmn vbpmn = new Vbpmn(command.toArray(new String[0]), this.outputFolder);
		final boolean result = vbpmn.execute();

		final StringBuilder builder = new StringBuilder();
		builder.append(result)
				.append("|");

		try
		{
			final String dotModel1 = this.generateDotFile(modelFile1.getAbsolutePath().replace(".pif", ".bcg"));
			final String dotModel2 = this.generateDotFile(modelFile2.getAbsolutePath().replace(".pif", ".bcg"));

			builder.append(dotModel1)
					.append("|")
					.append(dotModel2);

			if (!result)
			{
				final String bcgFileName;
				final String dotFileName;

				if (options.contains("property-implied")
					|| options.contains("property-and"))
				{
					bcgFileName = "evaluator.bcg";
					dotFileName = "counterexample.dot";
				}
				else
				{
					bcgFileName = "bisimulator.bcg";
					dotFileName = "bisimulator.dot";
				}

				final File bcgFile = new File(this.outputFolder + File.separator + bcgFileName);
				final String dotBcg = this.generateDotFile(bcgFile.getAbsolutePath(), dotFileName);
				builder.append("|")
						.append(dotBcg);
			}
		}
		catch (IOException | InterruptedException e)
		{
			throw new RuntimeException(e);
		}

		this.result = builder.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.inria.convecs.optimus.validator.ModelValidator#validate(java.io.File, java.io.File,
	 * java.lang.String)
	 */
	public void validate(final File modelFile1,
						 final File modelFile2,
						 final List<String> options)
	{
		final Boolean isBalanced = PifUtil.isPifBalanced(modelFile1) && PifUtil.isPifBalanced(modelFile2);
		logger.debug("The input isBalanced? : {}", isBalanced);

		final List<String> vbpmnCommand = new ArrayList<>();
		vbpmnCommand.add("python");
		vbpmnCommand.add(scriptsFolder + File.separator + "vbpmn.py");
		vbpmnCommand.add(modelFile1.getAbsolutePath());
		vbpmnCommand.add(modelFile2.getAbsolutePath());
		vbpmnCommand.addAll(options);
		logger.debug("The command is: {}", vbpmnCommand);

		try
		{
			final File outputDirectory = new File(this.outputFolder);
			final CommandExecutor commandExecutor = new CommandExecutor(vbpmnCommand, outputDirectory);
			final int execResult = commandExecutor.executeCommand();
			logger.debug("The return value of execution of command is: {}", execResult);

			final String response = handleResponse(commandExecutor.getOutput().trim(), commandExecutor.getErrors().trim());
			final StringBuilder resultBuilder = new StringBuilder();

			if ("TRUE".equalsIgnoreCase(response)
				|| "FALSE".equalsIgnoreCase(response))
			{
				resultBuilder.append(response).append("|");
				final String dotModel1 = generateDotFile(modelFile1.getAbsolutePath().replace(".pif", ".bcg"));
				final String dotModel2 = generateDotFile(modelFile2.getAbsolutePath().replace(".pif", ".bcg"));
				resultBuilder.append(dotModel1)
						.append("|")
						.append(dotModel2);

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
					final String dotBcg = generateDotFile(bcgFile.getAbsolutePath());
					resultBuilder.append("|")
							.append(dotBcg);
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
			responseBuilder.append("Std error executing the command: ").append(stdErr);
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
				final int index = stdOut.lastIndexOf("\n");

				if (index == -1)
				{
					responseBuilder.append(stdOut);
				}
				else
				{
					final String lastLine = stdOut.substring(stdOut.lastIndexOf("\n")).trim();

					if (!("TRUE".equalsIgnoreCase(lastLine)
						|| "FALSE".equalsIgnoreCase(lastLine)))
					{
						responseBuilder.append(stdOut);
					}
					else
					{
						responseBuilder.append(lastLine);
					}
				}
			}
		} 
		else 
		{
			responseBuilder.append("*** Unable to process request - No Result Available ***");
		}

		return responseBuilder.toString();
	}

	private String generateDotFile(final String absolutePath) throws IOException, InterruptedException
	{
		final String dotFile = absolutePath.replace(".bcg", ".dot");
		logger.debug("dot file: {}", dotFile);

		final List<String> commandArgs = new ArrayList<>();
		commandArgs.add(absolutePath);
		commandArgs.add(dotFile);

		final CommandManager commandManager = new CommandManager("bcg_io", new File(this.outputFolder), commandArgs);
		final int execResult = commandManager.execute();
		logger.debug("The exec result of command [ {} ] is {}", commandArgs, execResult);

		if (execResult != 0)
		{
			final String errorMessage = ErrorUtils.generateCommandErrorMessage(
				"bcg.io " + absolutePath + " " + dotFile,
				new File(this.outputFolder),
				commandManager.stdErr()
			);

			ErrorUtils.writeErrorFile(new File(this.outputFolder), errorMessage);

			throw new RuntimeException(errorMessage);
		}

		final File outputFile = new File(dotFile);
		final String dotOutput = FileUtils.readFileToString(outputFile, "UTF-8").replaceAll("\\R", " "); // Java 8 carriage return replace

		return dotOutput.trim();
	}

	private String generateDotFile(final String absolutePath,
								   final String newName) throws IOException, InterruptedException
	{
		final String dotFile = absolutePath.replace(".bcg", ".dot");
		logger.debug("dot file: {}", dotFile);

		final List<String> commandArgs = new ArrayList<>();
		commandArgs.add(absolutePath);
		commandArgs.add(dotFile);

		final CommandManager commandManager = new CommandManager("bcg_io", new File(this.outputFolder), commandArgs);
		final int execResult = commandManager.execute();
		logger.debug("The exec result of command [ {} ] is {}", commandArgs, execResult);

		if (execResult != 0)
		{
			final String errorMessage = ErrorUtils.generateCommandErrorMessage(
				"bcg.io " + absolutePath + " " + dotFile,
				new File(this.outputFolder),
				commandManager.stdErr()
			);

			ErrorUtils.writeErrorFile(new File(this.outputFolder), errorMessage);

			throw new RuntimeException(errorMessage);
		}

		final File outputFile = new File(dotFile);
		final String dotOutput = FileUtils.readFileToString(outputFile, "UTF-8").replaceAll("\\R", " "); // Java 8 carriage return replace
		final File newDotFile = new File(this.outputFolder + File.separator + newName);

		if (!outputFile.renameTo(newDotFile))
		{
			final String errorMessage = "Error renaming DOT file \"" + outputFile.getAbsolutePath() + "\" to \"" + newDotFile.getAbsolutePath() + "\".";
			ErrorUtils.writeErrorFile(new File(this.outputFolder), errorMessage);
			throw new IllegalStateException(errorMessage);
		}

		return dotOutput.trim();
	}
}
