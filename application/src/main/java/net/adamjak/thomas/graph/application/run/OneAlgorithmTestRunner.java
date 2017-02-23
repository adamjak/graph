package net.adamjak.thomas.graph.application.run;

import net.adamjak.thomas.graph.application.commons.SnarkTestTypes;
import net.adamjak.thomas.graph.library.api.Graph;
import net.adamjak.thomas.graph.library.tests.GraphTest;
import net.adamjak.thomas.graph.library.tests.GraphTestResult;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;

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
	public Map<String, Object> run ()
	{
		GraphTestResult[][] results = new GraphTestResult[super.loops][super.graphs.size()];

		for (int i = 0; i < super.loops; i++)
		{
			List<GraphTest> graphTests = new LinkedList<GraphTest>();

			for (Graph g : super.graphs)
			{
				try
				{
					GraphTest graphTest = (GraphTest) this.algorithm.newInstance();
					graphTest.init(g);
					graphTests.add(graphTest);
				}
				catch (InstantiationException e)
				{
					e.printStackTrace();
				}
				catch (IllegalAccessException e)
				{
					e.printStackTrace();
				}
			}

			ForkJoinPool forkJoinPool = new ForkJoinPool(ForkJoinPool.getCommonPoolParallelism());

			for (int j = 0; j < graphTests.size(); j++)
			{
				forkJoinPool.execute(graphTests.get(j));
			}

			boolean running = true;

			while (running)
			{
				running = false;

				for (int j = 0; j < graphTests.size(); j++)
				{
					GraphTest graphTest = graphTests.get(j);
					if (graphTest.isDone())
					{
						if (results[i][j] == null)
						{
							try
							{
								GraphTestResult graphTestResult = graphTest.getResult();
								results[i][j] = graphTestResult;

								int graphId = super.graphs.indexOf(graphTest.getGraph());

								LOGGER.debug("graph id " + graphId + " in " + (i + 1) + " run is done.");
							}
							catch (ExecutionException e)
							{
								e.printStackTrace();
							}
							catch (InterruptedException e)
							{
								e.printStackTrace();
							}
						}
					}
					else
					{
						running = true;
					}
				}
			}
			forkJoinPool.shutdown();
		}

		Map<String, Object> resultValues = new HashMap<String, Object>();
		resultValues.put("testType", SnarkTestTypes.ONE_ALGORITHM);
		resultValues.put("test", "Snark one algorithm test");
		resultValues.put("resultsData", results);
		resultValues.put("times", super.loops);

		return resultValues;
	}
}
