package net.adamjak.thomas.graph.application.run;

import net.adamjak.thomas.graph.application.commons.SnarkTestTypes;

import java.io.File;

/**
 * Created by Tomas Adamjak on 19.2.2017.
 * Copyright 2017, Tomas Adamjak
 * License: The BSD 3-Clause License
 * <p>
 * <p>This class is builder for {@link TestRunner}.
 *
 * @author Tomas Adamjak
 * @since 2017-02-19
 */
public class RunnerBuilder
{
	private SnarkTestTypes testType;
	private File outputFile = new File("output.txt");
	private File inputFile;
	private int loops = 1;
	private Class<?> algorithmTest;

	public SnarkTestTypes getTestType ()
	{
		return testType;
	}

	public RunnerBuilder setTestType (SnarkTestTypes testType)
	{
		this.testType = testType;
		return this;
	}

	public File getOutputFile ()
	{
		return outputFile;
	}

	public RunnerBuilder setOutputFile (File outputFile)
	{
		this.outputFile = outputFile;
		return this;
	}

	public File getInputFile ()
	{
		return inputFile;
	}

	public RunnerBuilder setInputFile (File inputFile)
	{
		this.inputFile = inputFile;
		return this;
	}

	public int getLoops ()
	{
		return loops;
	}

	public RunnerBuilder setLoops (int loops)
	{
		this.loops = loops;
		return this;
	}

	public Class<?> getAlgorithmTest ()
	{
		return algorithmTest;
	}

	public RunnerBuilder setAlgorithmTest (Class<?> algorithmTest)
	{
		this.algorithmTest = algorithmTest;
		return this;
	}

	/**
	 * @return Return runner for run selected test.
	 * @throws RunnerException if some requiret param is null.
	 */
	public TestRunner build () throws RunnerException
	{
		if (this.inputFile == null) throw new RunnerException("Input file can not be null");
		if (this.testType == null) throw new RunnerException("Test type can not be null");
		if (this.testType == SnarkTestTypes.ONE_ALGORITHM && this.algorithmTest == null)
			throw new RunnerException("Algorithm can not be null if test type is one algorithm test.");

		if (this.testType == SnarkTestTypes.ONE_ALGORITHM)
		{
			return new OneAlgorithmTestRunner(this.inputFile, this.outputFile, this.loops, this.algorithmTest);
		}
		else if (this.testType == SnarkTestTypes.ONE_ALGORITHM)
		{
			return new AllAlgorithmTestRunner(this.inputFile, this.outputFile, this.loops);
		}
		else
		{
			return new AlgorithmComparationTestRunner(this.inputFile, this.outputFile, this.loops);
		}
	}
}
