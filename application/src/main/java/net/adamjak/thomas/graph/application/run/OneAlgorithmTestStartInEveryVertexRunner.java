package net.adamjak.thomas.graph.application.run;

import net.adamjak.thomas.graph.application.commons.SnarkTestTypes;
import net.adamjak.thomas.graph.library.api.Graph;
import net.adamjak.thomas.graph.library.tests.GraphTest;
import net.adamjak.thomas.graph.library.tests.GraphTestResult;
import net.adamjak.thomas.graph.library.utils.GraphUtils;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;

/**
 * Created by Tomas Adamjak on 27.3.2017.
 * Copyright 2017, Tomas Adamjak
 * License: The BSD 3-Clause License
 */
public class OneAlgorithmTestStartInEveryVertexRunner extends TestRunner
{
	private Class<?> algorithm;

	public OneAlgorithmTestStartInEveryVertexRunner (File inputFile, File outputFile, int loops, Class<?> algorithm)
	{
		super(inputFile, outputFile, loops);
		this.algorithm = algorithm;
	}

	public OneAlgorithmTestStartInEveryVertexRunner (List<Graph<Integer>> graphs, File outputFile, int loops, Class<?> algorithm)
	{
		super(graphs, outputFile, loops);
		this.algorithm = algorithm;
	}


	@Override
	public Map<String, Object> run ()
	{
		GraphTestResult[][][] results = new GraphTestResult[super.loops][super.graphs.size()][super.graphs.get(0).getCountOfVertexes()];

		for (int i = 0; i < super.loops; i++)
		{
			for (int j = 0; j < super.graphs.size(); j++)
			{
				List<GraphTest> graphTests = new LinkedList<GraphTest>();

				for (Graph g : GraphUtils.getListAllGraphHashMapImplFromGraph(super.graphs.get(j)))
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

				for (int k = 0; k < graphTests.size(); k++)
				{
					forkJoinPool.execute(graphTests.get(k));
				}

				boolean running = true;

				while (running)
				{
					running = false;

					for (int k = 0; k < graphTests.size(); k++)
					{
						GraphTest graphTest = graphTests.get(k);
						if (graphTest.isDone())
						{
							try
							{
								GraphTestResult graphTestResult = graphTest.getResult();
								results[i][j][k] = graphTestResult;

								LOGGER.debug("graph id " + j + " start in vertex " + k + " in " + (i + 1) + " run is done.");
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
						else
						{
							running = true;
						}
					}
				}
				forkJoinPool.shutdown();
			}
		}

		Map<String, Object> resultValues = new HashMap<String, Object>();
		resultValues.put("testType", SnarkTestTypes.ONE_ALGORITHM_START_IN_EVERY_VERTEX);
		resultValues.put("test", "Snark one algorithm test whitch starts in every vertex");
		resultValues.put("resultsData", results);
		resultValues.put("times", super.loops);

		return resultValues;
	}
}
