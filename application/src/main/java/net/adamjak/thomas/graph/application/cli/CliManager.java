package net.adamjak.thomas.graph.application.cli;

import net.adamjak.thomas.graph.application.commons.SnarkTestTypes;
import net.adamjak.thomas.graph.application.commons.Utils;
import net.adamjak.thomas.graph.application.gui.AppMainWindow;
import net.adamjak.thomas.graph.application.run.RunnerBuilder;
import net.adamjak.thomas.graph.application.run.RunnerException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.io.File;

/**
 * Created by Tomas Adamjak on 18.2.2017.
 * Copyright 2017, Tomas Adamjak
 * License: The BSD 3-Clause License
 */
public class CliManager
{
	private static String[] arguments = null;

	/**
	 * Manage CLI
	 *
	 * @param arguments input arguments from user
	 */
	public static void manage (String[] arguments)
	{
		CliManager.arguments = arguments;

		CommandLineParser parser = new DefaultParser();
		CommandLine cmd = null;
		try
		{
			cmd = parser.parse(CliManager.getOptions(), arguments);
		}
		catch (ParseException e)
		{
			CliManager.printError("Error in parse cli arguments!\n" + "Message: " + e.getMessage());
			System.exit(-1);
		}

		if (cmd.hasOption("author"))
		{
			System.out.println(Utils.getAuthorInfo(false));
		}
		else if (cmd.hasOption("license"))
		{
			System.out.println(Utils.getLicenseInfo(false));
		}
		else if (cmd.hasOption("gui"))
		{
			AppMainWindow appMainWindow = new AppMainWindow();
			appMainWindow.setVisible(true);
		}
		else if (cmd.hasOption("i") && cmd.hasOption("t"))
		{
			RunnerBuilder builder = new RunnerBuilder();
			builder.setInputFile(new File(cmd.getOptionValue("i")));
			builder.setTestType(SnarkTestTypes.getSnarkTestTypeByName(cmd.getOptionValue("t")));

			if (cmd.hasOption("a"))
			{
				builder.setAlgorithmTest(Utils.getTestClassByName(cmd.getOptionValue("a")));
			}

			if (cmd.hasOption("o"))
			{
				builder.setOutputFile(new File(cmd.getOptionValue("o")));
			}

			if (cmd.hasOption("l"))
			{
				try
				{
					builder.setLoops(Integer.valueOf(cmd.getOptionValue("l")));
				}
				catch (NumberFormatException e)
				{
					CliManager.printError("Error in parse loops count.");
					System.exit(-1);
				}
			}

			try
			{
				builder.build().run();
			}
			catch (RunnerException e)
			{
				CliManager.printError(e.getMessage());
				System.exit(-1);
			}

		}
		else
		{
			CliManager.printHelp();
		}
	}

	/**
	 * @return String array with original input arguments.
	 */
	public static String[] getArguments ()
	{
		return arguments;
	}

	/**
	 * @return Options whitch manage software
	 */
	private static Options getOptions ()
	{
		Options options = new Options();
		options.addOption("author", false, "print informations about author");
		options.addOption("license", false, "print license informations");
		options.addOption("gui", false, "open gui manager");
		options.addOption("o", "output", true, "output file for results, default is 'output.csv'");
		options.addOption("i", "input", true, "input file with graph, accepted format: Graph6, GraphML, Bratislava text format");
		options.addOption("l", "loops", true, "how many time software loop selected test, default is 1");
		options.addOption("t", "test", true, "select test, values:\n- " + Utils.arrayToLineString(SnarkTestTypes.getTestNames(), "\n- "));
		options.addOption("a", "algorithm", true, "select algorithm if in [-test] option is selected one algorithm test, values:\n- " + Utils.arrayToLineString(Utils.getSimpleNamesOfClassesFromCollection(Utils.getAllTestClasses()), "\n- "));
		options.addOption("h", "help", false, "print this help");

		return options;
	}

	/**
	 * Print CLI help message
	 */
	public static void printHelp ()
	{
		String example = "\nExample: java -jar app.jar -i graphs.g6 -o results.csv -t ONE_ALGORITHM -l 100\n\n";

		HelpFormatter formatter = new HelpFormatter();
		formatter.setWidth(90);
		formatter.printHelp("java -jar /path/to/application.jar [OPTIONS...]", example, CliManager.getOptions(), "");
	}

	/**
	 * Print error message into console.
	 *
	 * @param err error message to print
	 */
	public static void printError (String err)
	{
		int borderSize = 0;

		if (err.contains("\n"))
		{
			for (String s : err.split("\n"))
			{
				if (s.length() > borderSize) borderSize = s.length();
			}
		}

		for (int i = 0; i < borderSize; i++)
		{
			System.err.print("!");
		}
		System.err.println("");

		System.err.println(err);

		for (int i = 0; i < borderSize; i++)
		{
			System.err.print("!");
		}
	}
}
