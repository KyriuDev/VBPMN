package fr.inria.convecs.optimus.py_to_java;

import fr.inria.convecs.optimus.constants.*;
import fr.inria.convecs.optimus.py_to_java.cadp_compliance.generics.BpmnTypesBuilderGeneric;
import fr.inria.convecs.optimus.py_to_java.cadp_compliance.generics.Pif2LntGeneric;
import fr.inria.convecs.optimus.util.*;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Porting of the ``vbpmn.py'' code to Java code
 * @author Quentin NIVON (quentin.nivon@inria.fr)
 */

public class Vbpmn
{
	private static final Logger logger = LoggerFactory.getLogger(Vbpmn.class);

	private final String[] sysArgs;
	private final String outputFolder;
	private final boolean compareOrVerify;
	private final boolean forceBcgUsageModel1;
	private final boolean forceBcgUsageModel2;
	private final Balancement balancement;
	private final Collection<String> alphabetModel1;
	private final Collection<String> alphabetModel2;
	private final ArrayList<Pair<Long, String>> executionTimes;

	//Constructors

	public Vbpmn(final String[] sysArgs,
				 final String outputFolder)
	{
		this(sysArgs, outputFolder, true, null, null, Balancement.COMPUTE_BALANCEMENT);
	}

	public Vbpmn(final String[] sysArgs,
				 final String outputFolder,
				 final Balancement balancement)
	{
		this(sysArgs, outputFolder, true, null, null, balancement);
	}

	public Vbpmn(final String[] sysArgs,
				 final String outputFolder,
				 final boolean compareOrVerify)
	{
		this(sysArgs, outputFolder, compareOrVerify, null, null, Balancement.COMPUTE_BALANCEMENT);
	}

	public Vbpmn(final String[] sysArgs,
				 final String outputFolder,
				 final boolean compareOrVerify,
				 final Balancement balancement)
	{
		this(sysArgs, outputFolder, compareOrVerify, null, null, balancement);
	}

	public Vbpmn(final String[] sysArgs,
				 final String outputFolder,
				 final boolean compareOrVerify,
				 final Collection<String> alphabetModel1,
				 final Collection<String> alphabetModel2)
	{
		this(sysArgs, outputFolder, compareOrVerify, alphabetModel1, alphabetModel2, Balancement.COMPUTE_BALANCEMENT);
	}

	public Vbpmn(final String[] sysArgs,
				 final String outputFolder,
				 final boolean compareOrVerify,
				 final Collection<String> alphabetModel1,
				 final Collection<String> alphabetModel2,
				 final Balancement balancement)
	{
		this.sysArgs = sysArgs;
		//if (true) throw new IllegalStateException(Arrays.toString(sysArgs));
		this.outputFolder = outputFolder;
		this.compareOrVerify = compareOrVerify;
		this.forceBcgUsageModel1 = alphabetModel1 != null;
		this.forceBcgUsageModel2 = alphabetModel2 != null;
		this.executionTimes = new ArrayList<>();
		this.alphabetModel1 = alphabetModel1 == null ? new ArrayList<>() : alphabetModel1;
		this.alphabetModel2 = alphabetModel2 == null ? new ArrayList<>() : alphabetModel2;
		this.balancement = balancement;
	}

	//Public methods

	@SuppressWarnings("unchecked") //Prevents Java from outputting warnings concerning the cast of Class<capture of ?>
	// to Class<? extends Pif2LntGeneric>
	public boolean execute()
	{
		final long startTime = System.nanoTime();

		//Initialise parser
		final Namespace args = this.parseArgs();
		final File pif1 = new File((String) args.getList(Argument.MODELS).get(0));
		final File pif2 = new File((String) args.getList(Argument.MODELS).get(1));

		//Check if process is balanced or not
		final long checkProcessBalanceStartTime = System.nanoTime();
		final boolean processIsBalanced = this.balancement == Balancement.COMPUTE_BALANCEMENT ?
										((this.forceBcgUsageModel1 || PifUtil.isPifBalanced(pif1))
										&& (this.forceBcgUsageModel2 || PifUtil.isPifBalanced(pif2))) :
										this.balancement == Balancement.FORCE_BALANCEMENT;
		final long checkProcessBalanceEndTime = System.nanoTime();
		final long checkProcessBalanceTime = checkProcessBalanceEndTime - checkProcessBalanceStartTime;
		this.executionTimes.add(Pair.of(checkProcessBalanceTime, "Checking if the process is balanced took " + Utils.nanoSecToReadable(checkProcessBalanceTime)));

		//Get CADP version
		final long computeCADPVersionStartTime = System.nanoTime();
		final String cadpVersionDir = this.getCadpVersion();
		final long computeCADPVersionEndTime = System.nanoTime();
		final long computeCADPVersionTime = computeCADPVersionEndTime - computeCADPVersionStartTime;
		this.executionTimes.add(Pair.of(computeCADPVersionTime, "Retrieving the installed CADP version took " + Utils.nanoSecToReadable(computeCADPVersionTime)));

		//Load the good Pif2Lnt class (depending on the CADP version)
		final long pif2LntClassRetrievalStartTime = System.nanoTime();
		final Pif2LntGeneric pif2lnt = this.loadPif2LntGenericClass(cadpVersionDir);
		pif2lnt.setBalance(processIsBalanced);
		pif2lnt.setOutputFolder(this.outputFolder);
		final long pif2LntClassRetrievalEndTime = System.nanoTime();
		final long pif2LntClassRetrievalTime = pif2LntClassRetrievalEndTime - pif2LntClassRetrievalStartTime;
		this.executionTimes.add(Pair.of(pif2LntClassRetrievalTime, "Retrieving the Pif2Lnt class to use took " + Utils.nanoSecToReadable(pif2LntClassRetrievalTime)));

		//Load the good BpmnTypesBuilder class (depending on the CADP version)
		final long bpmnTypesBuilderClassRetrievalStartTime = System.nanoTime();
		final BpmnTypesBuilderGeneric bpmnTypesBuilder = this.loadBpmnTypesBuilderClass(cadpVersionDir);
		bpmnTypesBuilder.setOutputDirectory(this.outputFolder);
		bpmnTypesBuilder.dumpBpmnTypesFile();
		final long bpmnTypesBuilderClassRetrievalEndTime = System.nanoTime();
		final long bpmnTypesBuilderClassRetrievalTime = bpmnTypesBuilderClassRetrievalEndTime - bpmnTypesBuilderClassRetrievalStartTime;
		this.executionTimes.add(Pair.of(bpmnTypesBuilderClassRetrievalTime, "Retrieving the BpmnTypesBuilder class to use took " + Utils.nanoSecToReadable(bpmnTypesBuilderClassRetrievalTime)));

		//If in lazy mode, rebuild the BCG files only if needed
		final boolean lazy = args.get("lazy") != null
				&& args.getBoolean("lazy");

		//(Re)build the first model
		final long firstProcessConversionStartTime = System.nanoTime();
		final Triple<Integer, String, Collection<String>> result1 =
			this.forceBcgUsageModel1 ?
			new Triple<>(ReturnCode.TERMINATION_OK, FilenameUtils.getBaseName(pif1.getName()), this.alphabetModel1) :
			(
				lazy ?
				pif2lnt.load(pif1, this.compareOrVerify) :
				pif2lnt.generate(pif1, this.compareOrVerify)
			);
		final long firstProcessConversionEndTime = System.nanoTime();
		final long firstProcessConversionTime = firstProcessConversionEndTime - firstProcessConversionStartTime;
		this.executionTimes.add(Pair.of(firstProcessConversionTime, "The generation of the LNT code of the first process took " + Utils.nanoSecToReadable(firstProcessConversionTime)));

		//(Re)build the second model
		final long secondProcessConversionStartTime = System.nanoTime();
		final Triple<Integer, String, Collection<String>> result2;

		if (Comparison.LIST.contains(args.getString("operation")))
		{
			//We are comparing processes, thus we need to build the two processes
			result2 =
				this.forceBcgUsageModel2 ?
				new Triple<>(ReturnCode.TERMINATION_OK, FilenameUtils.getBaseName(pif2.getName()), this.alphabetModel2) :
				(
					lazy ?
					pif2lnt.load(pif2, this.compareOrVerify) :
					pif2lnt.generate(pif2, this.compareOrVerify)
				);
		}
		else
		{
			result2 = result1;
		}

		final long secondProcessConversionEndTime = System.nanoTime();
		final long secondProcessConversionTime = secondProcessConversionEndTime - secondProcessConversionStartTime;
		this.executionTimes.add(Pair.of(secondProcessConversionTime, "The generation of the LNT code of the second process took " + Utils.nanoSecToReadable(secondProcessConversionTime)));

		//If one of the models could not be loaded => ERROR
		if (result1.getLeft() != ReturnCode.TERMINATION_OK)
		{
			final String errorMessage = this.getErrorMessage(pif1, result1);
			System.out.println(errorMessage);
			logger.error(errorMessage);
			throw new IllegalStateException(errorMessage);
		}

		if (result2.getLeft() != ReturnCode.TERMINATION_OK)
		{
			final String errorMessage = this.getErrorMessage(pif2, result2);
			System.out.println(errorMessage);
			logger.error(errorMessage);
			throw new IllegalStateException(errorMessage);
		}

		/*
			Checks if we compare up to a context.
			TODO Gwen : refine synchronization sets computation (_EM vs _REC)
			TODO Pascal : what about if we have hiding and/or renaming + context-awareness? different alphabets should
			 be used?
		 */

		final ArrayList<String> syncSet1 = new ArrayList<>();
		final ArrayList<String> syncSet2 = new ArrayList<>();

		if (args.get("context") != null)
		{
			final String pifContextModel = args.getString("context");
			System.out.println("Converting \"" + pifContextModel + "\" to LTS...");
			Triple<Integer, String, Collection<String>> result = lazy ? pif2lnt.load(new File(pifContextModel)) : pif2lnt.generate(new File(pifContextModel));

			for (String symbol : result.getRight())
			{
				if (result1.getRight().contains(symbol))
				{
					syncSet1.add(symbol);
				}
				if (result2.getRight().contains(symbol))
				{
					syncSet2.add(symbol);
				}
			}
		}

		final boolean result;
		final long comparisonEvaluationStartTime = System.nanoTime();
		final String mode;

		if (this.compareOrVerify)
		{
			final Checker comparator;

			//Check whether we compare based on an equivalence or based on a property
			if (Comparison.LIST.contains(args.getString("operation")))
			{
				comparator = new ComparisonChecker(
					result1.getMiddle(),
					result2.getMiddle(),
					args.getString("operation"),
					args.getList("hiding"),
					args.get("exposemode") != null && args.getBoolean("exposemode"),
					args.get("renaming") == null ? new HashMap<>() : args.get("renaming"),
					args.getString("renamed") == null ? "all" : args.getString("renamed"),
					new ArrayList[]{syncSet1, syncSet2}
				);
				mode = "The comparison of the processes took ";
			}
			else
			{
				comparator = new FormulaChecker(
					result1.getMiddle(),
					result2.getMiddle(),
					args.getString("formula")
				);
				mode = "The evaluation of the formula took ";
			}

			result = comparator.call();
		}
		else
		{
			result = true;
			mode = "Not comparing nor evaluating took ";
		}

		final long comparisonEvaluationEndTime = System.nanoTime();
		final long comparisonEvaluationTime = comparisonEvaluationEndTime - comparisonEvaluationStartTime;
		this.executionTimes.add(Pair.of(comparisonEvaluationTime, mode + Utils.nanoSecToReadable(comparisonEvaluationTime)));

		//Perform comparison and process result
		final long endTime = System.nanoTime();
		final long totalTime = endTime - startTime;
		this.executionTimes.add(Pair.of(totalTime, "Overall execution took " + Utils.nanoSecToReadable(totalTime)));

		final File execTimeFile = new File(outputFolder + File.separator + "time.txt");
		final PrintStream printStream;

		try
		{
			printStream = new PrintStream(execTimeFile);
		}
		catch (FileNotFoundException e)
		{
			logger.error("Could not write \"time.txt\" file: {}", String.valueOf(e));
			throw new RuntimeException(e);
		}

		printStream.print("The execution took " + Utils.nanoSecToReadable(totalTime));
		printStream.flush();
		printStream.close();

		final int returnValue = result ? ReturnCode.TERMINATION_OK : ReturnCode.TERMINATION_ERROR;
		//System.out.println("Result: " + result);

		return result;
	}

	public ArrayList<Pair<Long, String>> times()
	{
		return this.executionTimes;
	}

	//Private methods

	private String getErrorMessage(final File pifProcess,
								   final Triple<Integer, String, Collection<String>> triple)
	{
		final String errorMessage;

		if (triple.getLeft() == ReturnCode.TERMINATION_UNBALANCED_INCLUSIVE_CYCLE)
		{
			errorMessage = "Unbalanced inclusive gateways inside loops are not supported by the current version of " +
					"VBPMN, but model \"" + pifProcess.getAbsolutePath() + "\" contains some. Please check your " +
					"BPMN process to correct this issue, or try with another model.";
		}
		else
		{
			errorMessage = "Error while loading model \"" + pifProcess.getAbsolutePath() + "\". Please verify " +
					"that your input model is correct (in particular, BPMN objects and flows should not contain the" +
					" \"-\" symbol in their \"id\" attribute).";
		}

		return errorMessage;
	}

	private Namespace parseArgs()
	{
		final ArgumentParser parser = ArgumentParsers.newFor("vbpmn").build()
				.description("Compares two PIF processes.")
				.version("${prog} 1.0");

		parser.addArgument(Argument.VERSION)
				.action(Arguments.version());

		parser.addArgument(Argument.MODELS)
				.metavar("Model")
				.nargs(2)
				.help("the models to compare (filenames of PIF files)");

		parser.addArgument(Argument.OPERATION)
				.metavar("OP")
				.choices(Operation.LIST)
				.help("the comparison operation");

		parser.addArgument(Argument.FORMULA)
				.metavar("Formula")
				.help("temporal logic formula to check (used only if operation is in " + Property.LIST + ")");

		parser.addArgument(Argument.HIDING)
				.nargs("*")
				.help("list of alphabet elements to hide or to expose (based on --exposemode)");

		parser.addArgument(Argument.EXPOSE_MODE)
				.action(Arguments.storeTrue())
				.help("decides whether the arguments for --hiding should be the ones hidden (default) or the ones" +
						" exposed (if this option is set)");

		parser.addArgument(Argument.CONTEXT)
				.metavar("Context")
				.help("context to compare with reference to (filename of a PIF file)");

		parser.addArgument(Argument.RENAMING)
				.metavar("old:new")
				.nargs("*")
				.setDefault(new HashMap<>())
				.help("list of renamings");

		parser.addArgument(Argument.RENAMED)
				.nargs("?")
				.choices(Selection.LIST)
				.setConst(Selection.DEFAULT)
				.setDefault(Selection.DEFAULT)
				.help("gives the model to apply renaming to (first, second, or all (default))");

		parser.addArgument(Argument.LAZY)
				.action(Arguments.storeTrue())
				.help("does not recompute the BCG model if it already exists and is more recent than the PIF model");

		//Parse arguments
		final Namespace args;

		try
		{
			args = parser.parseArgs(this.sysArgs);

			if (Property.LIST.contains(args.getString("operation"))
					&& args.get("formula") == null)
			{
				System.out.println("missing formula in presence of property based comparison.");
				logger.error("missing formula in presence of property based comparison.");
				throw new RuntimeException("missing formula in presence of property based comparison.");
			}
			if (!Property.LIST.contains(args.getString("operation"))
					&& args.get("formula") != null)
			{
				logger.warn("formula in presence of equivalence based comparison will not be used.");
				System.out.println("formula in presence of equivalence based comparison will not be used.");
			}
		}
		catch (ArgumentParserException e)
		{
			parser.printHelp();
			logger.error(String.valueOf(e));
			throw new IllegalStateException();
		}

		return args;
	}

	private String getCadpVersion()
	{
		final String cadpVersionDir;

		try
		{
			final Map<String, String> environment = System.getenv();

			/*logger.debug("Environment variables found:\n");

			for (String key : environment.keySet())
			{
				logger.debug("- {} : {}", key, environment.get(key));
			}*/

			if (System.getenv(EnvironmentVariable.CADP) == null)
			{
				final String errorMessage = "Environment variable $CADP is not set! Please fix this error and retry.";
				ErrorUtils.writeErrorFile(new File(this.outputFolder), errorMessage);
				logger.error(errorMessage);
				throw new RuntimeException(errorMessage);
			}

			if (System.getenv(EnvironmentVariable.PATH) != null
				&& !System.getenv(EnvironmentVariable.PATH).contains(Constant.CADP))
			{
				final String errorMessage = "Environment variable $PATH exists but does not contain \"cadp\" (" + System.getenv(EnvironmentVariable.PATH) + ")";
				ErrorUtils.writeErrorFile(new File(this.outputFolder), errorMessage);
				logger.error(errorMessage);
				throw new RuntimeException(errorMessage);
			}

			//logger.debug("CADP dir: \"{}\".", System.getenv("CADP"));

			final CommandManager commandManager = new CommandManager(
				Command.CADP_LIB,
				new File(outputFolder),
				Argument.MINUS_ONE
			);
			final int returnValue = commandManager.execute();

			if (returnValue != ReturnCode.TERMINATION_OK)
			{
				final String errorMessage = ErrorUtils.generateCommandErrorMessage(
					Command.CADP_LIB + Argument.MINUS_ONE,
					new File(this.outputFolder),
					commandManager.stdErr()
				);

				ErrorUtils.writeErrorFile(new File(this.outputFolder), errorMessage);
				logger.error(errorMessage);
				throw new RuntimeException(errorMessage);
			}

			//Split answer by spaces
			final String[] splitAnswer = commandManager.stdOut().split("\\s+");
			//The 2nd element is the version code, i.e. "2023k"
			cadpVersionDir = Constant.UNDERSCORE + splitAnswer[1].replace(" ", "").replace("-", "");
			//System.out.println("CADP VERSION: " + cadpVersionDir);
		}
		catch (IOException | InterruptedException e)
		{
			ErrorUtils.writeErrorFile(new File(this.outputFolder), e.toString());
			logger.error(e.toString());
			throw new RuntimeException(e);
		}

		return cadpVersionDir;
	}

	@SuppressWarnings("unchecked")
	private Pif2LntGeneric loadPif2LntGenericClass(final String cadpVersionDir)
	{
		final Pif2LntGeneric pif2lnt;

		try
		{
			//Load the Pif2Lnt class located in the package corresponding to the good CADP version
			final String classPath = "fr.inria.convecs.optimus.py_to_java.cadp_compliance." + cadpVersionDir + ".Pif2Lnt";
			final Class<? extends Pif2LntGeneric> pif2LntClass = (Class<? extends Pif2LntGeneric>) Class.forName(classPath);
			final Constructor<? extends Pif2LntGeneric> pif2LntConstructor = pif2LntClass.getDeclaredConstructor();
			pif2lnt = pif2LntConstructor.newInstance();
		}
		catch (ClassNotFoundException
			   | NoSuchMethodException
			   | InvocationTargetException
			   | InstantiationException
			   | IllegalAccessException e)
		{
			final String errorMessage = "Please make sure that you downloaded the latest version of VBPMN. \nIf yes," +
					" please check that the path \"<your_tomcat_installation_path>" + File.separator + "webapps" +
					File.separator + "transformation" + File.separator + "WEB-INF" + File.separator + "classes" +
					File.separator + "fr" + File.separator + "inria" + File.separator + "convecs" + File.separator +
					"optimus" + File.separator + "py_to_java" + File.separator + "cadp_compliance" + File.separator +
					cadpVersionDir + "\" exists and contains the file \"Pif2Lnt.class\". \nIf yes, please send an" +
					" email to the staff. Otherwise, please reinstall the latest version of VBPMN.";
			System.out.println(errorMessage);
			logger.error(errorMessage);
			ErrorUtils.writeErrorFile(new File(this.outputFolder), errorMessage + "\n\nError:\n\n" + e);
			throw new RuntimeException(errorMessage, e);
		}

		return pif2lnt;
	}

	@SuppressWarnings("unchecked")
	private BpmnTypesBuilderGeneric loadBpmnTypesBuilderClass(final String cadpVersionDir)
	{
		final BpmnTypesBuilderGeneric bpmnTypesBuilder;

		try
		{
			//Load the BpmnTypesBuilder class located in the package corresponding to the good version
			final String classPath = "fr.inria.convecs.optimus.py_to_java.cadp_compliance." + cadpVersionDir + ".BpmnTypesBuilder";
			final Class<? extends BpmnTypesBuilderGeneric> bpmnTypesBuilderClass = (Class<? extends BpmnTypesBuilderGeneric>) Class.forName(classPath);
			final Constructor<? extends BpmnTypesBuilderGeneric> bpmnTypesBuilderConstructor = bpmnTypesBuilderClass.getDeclaredConstructor();
			bpmnTypesBuilder = bpmnTypesBuilderConstructor.newInstance();
		}
		catch (ClassNotFoundException
			   | NoSuchMethodException
			   | InvocationTargetException
			   | InstantiationException
			   | IllegalAccessException e)
		{
			final String errorMessage = "Please make sure that you downloaded the latest version of VBPMN. \nIf yes," +
					" please check that the path \"<your_tomcat_installation_path>" + File.separator + "webapps" +
					File.separator + "transformation" + File.separator + "WEB-INF" + File.separator + "classes" +
					File.separator + "fr" + File.separator + "inria" + File.separator + "convecs" + File.separator +
					"optimus" + File.separator + "py_to_java" + File.separator + "cadp_compliance" + File.separator +
					cadpVersionDir + "\" exists and contains the file \"BpmnTypesBuilder.class\". \nIf yes, please" +
					" send an email to the staff. Otherwise, please reinstall the latest version of VBPMN.";
			System.out.println(errorMessage);
			logger.error(errorMessage);
			ErrorUtils.writeErrorFile(new File(this.outputFolder), errorMessage + "\n\nError:\n\n" + e);
			throw new RuntimeException(errorMessage, e);
		}

		return bpmnTypesBuilder;
	}

	//Sub-classes

	/*
		This class represents the superclass of all classes performing some formal checking on two LTS models (stores
		in BCG format files)
	 */
	abstract static class Checker
	{
		protected final String model1;
		protected final String model2;

		/**
		 * Sets up the Checker.
		 *
		 * @param model1 is the filename of the first model (LTS in a BCG file)
		 * @param model2 is the filename of the second model (LTS in a BCG file)
		 */
		public Checker(final String model1,
					   final String model2)
		{
			this.model1 = model1;
			this.model2 = model2;
		}

		/**
		 Generates the SVL script to check the property on both models.

		 @param filename is the filename of the SVL script to create.
		 */
		public abstract void genSVL(final String filename);

		/**
		 * Reification of a Checker as a callable object.
		 *
		 * TODO
		 */
		public abstract boolean call();
	}

	// This class is used to perform comparison operations on two models (LTS stored in two BCG format files)
	class ComparisonChecker extends Checker
	{
		private final String operation;
		private final List<String> hiding;
		private final String renamed;
		private final boolean exposeMode;
		private final Map<String, String> renaming;
		private final List<String>[] syncSets;

		/**
		 * Sets up the ComparisonChecker.
		 *
		 * @param model1 is the filename of the first model (LTS in a BCG file)
		 * @param model2 is the filename of the second model (LTS in a BCG file)
		 * @param operation is the comparison operation (in Vbpmn.OPERATIONS)
		 * @param hiding is the list of elements to hide (or to expose, wrt @exposeMode)
		 * @param exposeMode states whether the elements in @hiding should be exposed or not
		 * @param renaming is the correspondence between old labels and new labels
		 * @param syncSets is the couple of lists of alphabets to synchronize on (one for each model)
		 */
		public ComparisonChecker(final String model1,
								 final String model2,
								 final String operation,
								 final List<String> hiding,
								 final boolean exposeMode,
								 final Map<String, String> renaming,
								 final String renamed,
								 final List<String>[] syncSets)
		{
			super(model1, model2);

			if (!Operation.LIST.contains(operation)
				|| Operation.HIDING.equals(operation))
			{
				final String errorMessage = "Operation should be in " + Operation.LIST + " and \"_\" is only for hiding. " +
						"Received \"" + operation + "\".";
				logger.error(errorMessage);
				ErrorUtils.writeErrorFile(new File(outputFolder), errorMessage);
				throw new RuntimeException(errorMessage);
			}

			if (!Selection.LIST.contains(renamed))
			{
				final String errorMessage = "Selection should be in " + Selection.LIST + ". Received \"" + renamed + "\".";
				logger.error(errorMessage);
				ErrorUtils.writeErrorFile(new File(outputFolder), errorMessage);
				throw new RuntimeException(errorMessage);
			}

			this.operation = operation;
			this.hiding = hiding;
			this.renamed = renamed;
			this.exposeMode = exposeMode;
			this.renaming = renaming;
			this.syncSets = syncSets;
		}

		/**
		 * Generates an SVL script to check the property on both models.
		 *
		 * @param filename is the filename of the SVL script to create.
		 */
		@Override
		public void genSVL(final String filename)
		{
			final StringBuilder svlCommands = new StringBuilder();
			//Add commands to make copies of the models and not change them.
			final String workModel1 = this.model1 + Constant.WORK_SUFFIX;
			final String workModel2 = this.model2 + Constant.WORK_SUFFIX;
			svlCommands.append(PyToJavaUtils.parametrize(Template.SVL_COPY, this.model1, workModel1));
			svlCommands.append(PyToJavaUtils.parametrize(Template.SVL_COPY, this.model2, workModel2));

			//If required, perform hiding (on BOTH models).
			if (this.hiding != null)
			{
				final String hideMode;

				if (this.exposeMode)
				{
					hideMode = Svl.HIDE_ALL_BUT;
				}
				else
				{
					hideMode = Svl.HIDE;
				}

				svlCommands.append(PyToJavaUtils.parametrize(
					Template.SVL_HIDING,
					workModel1,
					hideMode,
					PyToJavaUtils.join(this.hiding, Separator.COMA))
				);

				svlCommands.append(PyToJavaUtils.parametrize(
					Template.SVL_HIDING,
					workModel2,
					hideMode,
					PyToJavaUtils.join(this.hiding, Separator.COMA))
				);
			}

			/*
				Perform renaming.
				Done AFTER having hidden TODO: is this ok? shouldn't we allow more freedom in the ordering of things?
			 */

			if (!this.renaming.isEmpty())
			{
				final ArrayList<String> renamings = new ArrayList<>();

				for (final String oldName : this.renaming.keySet())
				{
					final String newName = this.renaming.get(oldName);
					renamings.add(oldName + " " + Constant.RIGHT_ARROW + " " + newName);
				}

				switch (this.renamed)
				{
					case Selection.ALL:
						svlCommands.append(PyToJavaUtils.parametrize(
							Template.SVL_RENAMING,
							workModel1,
							PyToJavaUtils.join(renamings, Separator.COMA))
						);
						svlCommands.append(PyToJavaUtils.parametrize(
							Template.SVL_RENAMING,
							workModel2,
							PyToJavaUtils.join(renamings, Separator.COMA))
						);
						break;

					case Selection.FIRST:
						svlCommands.append(PyToJavaUtils.parametrize(
							Template.SVL_RENAMING,
							workModel1,
							PyToJavaUtils.join(renamings, Separator.COMA))
						);
						break;

					case Selection.SECOND:
						svlCommands.append(PyToJavaUtils.parametrize(
							Template.SVL_RENAMING,
							workModel2,
							PyToJavaUtils.join(renamings, Separator.COMA))
						);
						break;

					default:
						//Should never happen
						final String errorMessage = "The list of elements to rename is not empty but the selection" +
								"is \"" + this.renamed + "\"!";
						logger.error(errorMessage);
						ErrorUtils.writeErrorFile(new File(outputFolder), errorMessage);
						throw new IllegalStateException(errorMessage);
				}
			}

			/*
				Add the command to perform the comparison.
        		Equivalences are strong (by default) but we use branching in case of hiding. //TODO Branching by default is an error?
			 */

			svlCommands.append(PyToJavaUtils.parametrize(
				Template.SVL_COMPARISON_CHECKING,
				workModel1,
				Operation.LIST_BISIMULATOR.get(this.operation),
				Equivalence.BRANCHING,
				workModel2)
			);

			//TODO VERIFIER LA CREATION DU FICHIER AINSI QUE SON CONTENU
			final String template = PyToJavaUtils.parametrize(Template.SVL_CAESAR, svlCommands.toString());
			final File templateFile = new File(outputFolder + File.separator + filename);
			final PrintStream printStream;

			try
			{
				printStream = new PrintStream(templateFile);
			}
			catch (FileNotFoundException e)
			{
				throw new RuntimeException(e);
			}

			printStream.print(template);
			printStream.flush();
			printStream.close();
		}

		/**
		 * Checks if an equivalence or preorder yields between two models.
		 * Done by generating first an SVL script and then calling it.
		 *
		 * @return true if the equivalence/preorder yields, false otherwise.
		 */
		@Override
		public boolean call()
		{
			this.genSVL(Filename.CHECKER);

			try
			{
				//TODO CHECK FUNCTIONING + REVERIF
				final String command = Command.SVL;
				final String[] args = {
					Filename.CHECKER,
					Constant.RIGHT_ARROW,
					Filename.DIAGNOSTIC
				};
				final CommandManager commandManager = new CommandManager(command, new File(outputFolder), args);
				commandManager.execute();

				if (commandManager.returnValue() != ReturnCode.TERMINATION_OK)
				{
					final String errorMessage = ErrorUtils.generateCommandErrorMessage(
						ErrorUtils.inlineCommandAndArgs(command, Arrays.asList(args)),
						new File(outputFolder),
						commandManager.stdErr()
					);

					ErrorUtils.writeErrorFile(new File(outputFolder), errorMessage);
					logger.error(errorMessage);
					throw new RuntimeException(errorMessage);
				}

				final File resFile = new File(outputFolder + File.separator + Filename.DIAGNOSTIC);
				final PrintWriter printWriter;

				try
				{
					printWriter = new PrintWriter(resFile);
				}
				catch (IOException e)
				{
					throw new RuntimeException();
				}

				printWriter.println(commandManager.stdOut());
				printWriter.flush();
				printWriter.close();

				if (!commandManager.stdOut().contains(ReturnCode.TRUE)
					&& !commandManager.stdOut().contains(ReturnCode.FALSE))
				{
					final String errorMessage = ErrorUtils.generateCommandErrorMessage(
						ErrorUtils.inlineCommandAndArgs(command, Arrays.asList(args)),
						new File(outputFolder),
						"The verdict file \"" + Filename.DIAGNOSTIC + "\" does not contain \"" + ReturnCode.TRUE +
						"\" nor \"" + ReturnCode.FALSE + "\", although the verification ended without error. Please " +
						"refer to the corresponding  \".log\" file for more explanation of the issue."
					);

					ErrorUtils.writeErrorFile(new File(outputFolder), errorMessage);
					logger.error(errorMessage);
					throw new RuntimeException(errorMessage);
				}

				return commandManager.stdOut().contains(ReturnCode.TRUE);
			}
			catch (IOException | InterruptedException e)
			{
				throw new RuntimeException(e);
			}
		}
	}

	/*
		This class is used to perform model checking operations on two models (LTS stored in two BCG format files)
		wrt an MCL property (stored in an MCL file).
	 */
	class FormulaChecker extends Checker
	{
		private final String formula;

		/**
		 * Sets up the FormulaChecker.
		 *
		 * @param model1 is the filename of the first model (LTS in a BCG file)
		 * @param model2 is the filename of the second model (LTS in a BCG file)
		 * @param formula is the filename of the property file (MCL file)
		 */
		public FormulaChecker(final String model1,
							  final String model2,
							  final String formula)
		{
			super(model1, model2);
			this.formula = formula;
		}

		@Override
		public void genSVL(final String filename)
		{
			final StringBuilder svlCommands = new StringBuilder(PyToJavaUtils.parametrize(
				Template.SVL_FORMULA_CHECKING,
				this.model1,
				Constant.MCL_FORMULA_FILENAME
			));

			if (!this.model1.equals(this.model2))
			{
				svlCommands.append(PyToJavaUtils.parametrize(
					Template.SVL_FORMULA_CHECKING,
					this.model2,
					Constant.MCL_FORMULA_FILENAME
				));
			}

			final String template = PyToJavaUtils.parametrize(Template.SVL_CAESAR, svlCommands.toString());
			final File templateFile = new File(outputFolder + File.separator + filename);
			final PrintStream printStream;

			try
			{
				printStream = new PrintStream(templateFile);
			}
			catch (FileNotFoundException e)
			{
				throw new RuntimeException(e);
			}

			printStream.print(template);
			printStream.flush();
			printStream.close();
		}

		/**
		 * Checks if a formula yields on two models.
		 * Done by generating first an SVL script and then calling it.
		 *
		 * @return true if no errors were detected by SVL, false otherwise
		 */
		@Override
		public boolean call()
		{
			//Write formula to file
			final File formulaFile = new File(outputFolder + File.separator + Filename.FORMULA);
			final PrintStream printStream;

			try
			{
				printStream = new PrintStream(formulaFile);
			}
			catch (FileNotFoundException e)
			{
				throw new RuntimeException(e);
			}

			printStream.print(this.formula); //TODO: not very clean ...
			printStream.flush();
			printStream.close();

			//Generate SVL
			this.genSVL(Filename.CHECKER);

			try
			{
				final String command = Command.SVL;
				final String[] args = {
					Filename.CHECKER,
					Constant.RIGHT_ARROW,
					Filename.DIAGNOSTIC
				};
				final CommandManager commandManager = new CommandManager(command, new File(outputFolder), args);
				commandManager.execute();

				if (commandManager.returnValue() != ReturnCode.TERMINATION_OK)
				{
					final String errorMessage = ErrorUtils.generateCommandErrorMessage(
						ErrorUtils.inlineCommandAndArgs(command, Arrays.asList(args)),
						new File(outputFolder),
						commandManager.stdErr()
					);

					ErrorUtils.writeErrorFile(new File(outputFolder), errorMessage);
					logger.error(errorMessage);
					throw new RuntimeException(errorMessage);
				}

				final File resFile = new File(outputFolder + File.separator + Filename.DIAGNOSTIC);
				final PrintWriter printWriter;

				try
				{
					printWriter = new PrintWriter(resFile);
				}
				catch (IOException e)
				{
					throw new RuntimeException();
				}

				printWriter.println(commandManager.stdOut());
				printWriter.flush();
				printWriter.close();

				if (!commandManager.stdOut().contains(ReturnCode.TRUE)
					&& !commandManager.stdOut().contains(ReturnCode.FALSE))
				{
					final String errorMessage = ErrorUtils.generateCommandErrorMessage(
						ErrorUtils.inlineCommandAndArgs(command, Arrays.asList(args)),
						new File(outputFolder),
						"The verdict file \"" + Filename.DIAGNOSTIC + "\" does not contain \"" + ReturnCode.TRUE +
						"\" nor \"" + ReturnCode.FALSE + "\", although the verification ended without error. Please " +
						"refer to the corresponding \".log\" file for more explanation of the issue."
					);

					ErrorUtils.writeErrorFile(new File(outputFolder), errorMessage);
					logger.error(errorMessage);
					throw new RuntimeException(errorMessage);
				}

				return commandManager.stdOut().contains(ReturnCode.TRUE);
			}
			catch (IOException | InterruptedException e)
			{
				throw new RuntimeException(e);
			}
		}
	}
}
