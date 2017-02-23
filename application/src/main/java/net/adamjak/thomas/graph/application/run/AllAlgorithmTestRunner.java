package net.adamjak.thomas.graph.application.run;

import net.adamjak.thomas.graph.application.commons.Settings;
import net.adamjak.thomas.graph.application.commons.SnarkTestTypes;
import net.adamjak.thomas.graph.application.commons.Utils;
import net.adamjak.thomas.graph.library.api.Graph;
import net.adamjak.thomas.graph.library.tests.GraphTest;
import net.adamjak.thomas.graph.library.tests.GraphTestResult;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Tomas Adamjak on 22.2.2017.
 * Copyright 2017, Tomas Adamjak
 * License: The BSD 3-Clause License
 */
class AllAlgorithmTestRunner extends TestRunner
{
	public AllAlgorithmTestRunner (File inputFile, File outputFile, int loops)
	{
		super(inputFile, outputFile, loops);
	}

	@Override
	public Map<String, Object> run ()
	{
		LOGGER.info("Start snark all algorithms.");

		LOGGER.debug(Settings.getInstance().getSetting("packageName"));
		List<Class<?>> classes = new ArrayList<>(Utils.getAllTestClasses());

		List<Graph<Integer>> graphsForTest = new LinkedList<Graph<Integer>>(super.graphs);

		GraphTestResult[][][] results = new GraphTestResult[super.loops][graphsForTest.size()][classes.size()];

		for (int run = 0; run < super.loops; run++)
		{
			for (int i = 0; i < graphsForTest.size(); i++)
			{
				Graph<Integer> g = graphsForTest.get(i);

				for (int j = 0; j < classes.size(); j++)
				{
					try
					{
						GraphTest graphTest = (GraphTest) classes.get(j).newInstance();
						graphTest.init(g);
						// TODO: 9.12.2016 -- doriesit co s dlho trvajucim vypoctom
						LOGGER.debug("Start executor service");
						ExecutorService executorService = Executors.newFixedThreadPool(1);
						LOGGER.debug("submit into executor service");
						GraphTestResult graphTestResult = (GraphTestResult) executorService.submit(graphTest).get();
						LOGGER.debug("Shutdown executor service");
						executorService.shutdown();

						int graphId = super.graphs.indexOf(g);

						String sout = "Testing run: " + (run + 1) + ", Graph id: " + graphId + ", Result: " + graphTestResult;
						LOGGER.info(sout);

						graphTestResult.addValue("algorithmClass", classes.get(j));
						results[run][i][j] = graphTestResult;
					}
					catch (InstantiationException e)
					{
						e.printStackTrace();
					}
					catch (IllegalAccessException e)
					{
						e.printStackTrace();
					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}
					catch (ExecutionException e)
					{
						e.printStackTrace();
					}
				}
			}
		}

		Map<String, Object> resultValues = new HashMap<String, Object>();
		resultValues.put("testType", SnarkTestTypes.ALL_ALGORITHMS);
		resultValues.put("test", "Snark all algorithms test");
		resultValues.put("resultsData", results);
		resultValues.put("times", super.loops);

		return resultValues;
	}
}
