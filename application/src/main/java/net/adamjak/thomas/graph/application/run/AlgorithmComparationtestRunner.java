package net.adamjak.thomas.graph.application.run;

import net.adamjak.thomas.graph.library.tests.GraphTestResult;

import java.io.File;

/**
 * Created by Tomas Adamjak on 22.2.2017.
 * Copyright 2017, Tomas Adamjak
 * License: The BSD 3-Clause License
 */
class AlgorithmComparationtestRunner extends TestRunner
{
	public AlgorithmComparationtestRunner (File inputFile, File outputFile, int loops)
	{
		super(inputFile, outputFile, loops);
	}

	@Override
	public GraphTestResult[][] run ()
	{
		return null;
	}
}
