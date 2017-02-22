package net.adamjak.thomas.graph.application.run;

import net.adamjak.thomas.graph.library.tests.GraphTestResult;

import java.io.File;

/**
 * Created by Tomas Adamjak on 22.2.2017.
 * Copyright 2017, Tomas Adamjak
 * License: The BSD 3-Clause License
 */
class OneAlgorithmTestRunner extends TestRunner
{
	private Class<?> algorithm;

	public OneAlgorithmTestRunner (File inputFile, File outputFile, int loops, Class<?> algorithm)
	{
		super(inputFile, outputFile, loops);
		this.algorithm = algorithm;
	}

	@Override
	public GraphTestResult[][] run ()
	{
		return null;
	}
}
