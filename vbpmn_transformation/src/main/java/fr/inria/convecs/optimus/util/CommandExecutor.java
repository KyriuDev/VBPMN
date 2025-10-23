package fr.inria.convecs.optimus.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ajayk
 *     Utility class to execute system commands. 
 *     Internally uses ProcessBuilder
 */
public class CommandExecutor
{
	private static final Logger logger = LoggerFactory.getLogger(CommandExecutor.class);
	private final List<String> command;
	private final File directory;
	private String stdOut;
	private String stdError;

	/**
	 *
	 * @param command Command and list of options
	 * @param directory The directory context where command will be executed.
	 */
	public CommandExecutor(final List<String> command,
						   final File directory)
	{
		this.command = command;
		this.directory = directory;
	}

	public int executeCommand()
	{
		final int returnValue;

		try
		{
			final ProcessBuilder processBuilder = new ProcessBuilder(this.command);
			processBuilder.directory(this.directory);
			final Process process = processBuilder.start();
			final InputStream output = process.getInputStream();
			final InputStream error = process.getErrorStream();

			this.stdOut = IOUtils.toString(output, StandardCharsets.UTF_8);
			this.stdError = IOUtils.toString(error, StandardCharsets.UTF_8);

			returnValue = process.waitFor();
		}
		catch (IOException ioe)
		{
			logger.warn("Execption executing the system command", ioe);
			throw new RuntimeException(ioe);
		}
		catch (InterruptedException ie)
		{
			logger.warn("InterruptedException - Unable to get the exit value", ie);
			throw new RuntimeException(ie);
		}

		return returnValue;
	}

	public String getErrors()
	{
		return stdError;
	}

	public String getOutput()
	{
		return stdOut;
	}
}
