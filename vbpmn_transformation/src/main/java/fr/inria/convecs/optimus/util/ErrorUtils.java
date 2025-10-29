package fr.inria.convecs.optimus.util;

import fr.inria.convecs.optimus.constants.Constant;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ErrorUtils
{
	private static final String ERROR_FILE_BASE_NAME = "error";
	private static final String ERROR_FILE_EXTENSION = ".log";

	private ErrorUtils()
	{

	}

	public static String generateCommandErrorMessage(final String command,
													 final File workingDir,
													 final String error)
	{
		return "Error executing command \"" + command + "\". Please note that any relative path appearing in the " +
				"error takes its origin in the directory \"" + workingDir.getAbsolutePath() + "\", unless otherwise " +
				"specified.\nError:\n\n" + error;
	}

	public static String inlineCommandAndArgs(final List<String> commandAndArgs)
	{
		if (commandAndArgs.isEmpty())
		{
			throw new RuntimeException("There should be at least the command in the list!");
		}

		final ArrayList<String> commandsAndArgsArrayList = new ArrayList<>(commandAndArgs);
		final String command = commandsAndArgsArrayList.remove(0);

		return ErrorUtils.inlineCommandAndArgs(command, commandsAndArgsArrayList);
	}

	public static String inlineCommandAndArgs(final String... commandAndArgs)
	{
		return ErrorUtils.inlineCommandAndArgs(Arrays.asList(commandAndArgs));
	}

	public static String inlineCommandAndArgs(final String command,
											  final List<String> args)
	{
		final StringBuilder builder = new StringBuilder(command);

		for (final String arg : args)
		{
			builder.append(Constant.SPACE)
					.append(arg);
		}

		return builder.toString();
	}

	public static void writeErrorFile(final File workingDir,
									  final String errorMessage)
	{
		String errorFileName = ERROR_FILE_BASE_NAME + ERROR_FILE_EXTENSION;
		File errorFile = new File(workingDir.getAbsolutePath() + File.separator + errorFileName);
		int errorFileIndex = 1;

		while (errorFile.exists())
		{
			errorFileName = ERROR_FILE_BASE_NAME + Constant.UNDERSCORE + errorFileIndex++ + ERROR_FILE_EXTENSION;
			errorFile = new File(errorFileName);
		}

		try
		{
			final PrintWriter printWriter = new PrintWriter(errorFile);
			printWriter.println(errorMessage);
			printWriter.flush();
			printWriter.close();
		}
		catch (FileNotFoundException e)
		{
			throw new RuntimeException(e);
		}
	}
}
