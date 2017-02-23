package net.adamjak.thomas.graph.application.run;

import net.adamjak.thomas.graph.application.commons.Settings;
import net.adamjak.thomas.graph.application.commons.SnarkTestTypes;
import net.adamjak.thomas.graph.library.api.Graph;
import net.adamjak.thomas.graph.library.interfaces.anot.Benchmarked;
import net.adamjak.thomas.graph.library.tests.GraphTest;
import net.adamjak.thomas.graph.library.tests.GraphTestResult;
import net.adamjak.thomas.graph.library.utils.ClassFinder;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;

/**
 * Created by Tomas Adamjak on 22.2.2017.
 * Copyright 2017, Tomas Adamjak
 * License: The BSD 3-Clause License
 */
class AlgorithmComparationTestRunner extends TestRunner
{
	public AlgorithmComparationTestRunner (File inputFile, File outputFile, int loops)
	{
		super(inputFile, outputFile, loops);
	}

	@Override
	public Map<String, Object> run ()
	{

		LOGGER.info("Start snark algorithm comparation.");

		ClassFinder classFinder = new ClassFinder();
		Set<String> packageNames = new LinkedHashSet<String>();
		packageNames.add(Settings.getInstance().getSetting("packageName"));
		LOGGER.debug(Settings.getInstance().getSetting("packageName"));
		Set<Class<?>> classes = classFinder.findClassesWhitchExtends(classFinder.findAnnotatedClasses(packageNames, Benchmarked.class), GraphTest.class);

		List<Graph<Integer>> graphsForTest = new LinkedList<Graph<Integer>>(super.graphs);

		GraphTestResult[][] results = new GraphTestResult[super.loops][graphsForTest.size()];

		for (int run = 0; run < super.loops; run++)
		{
			for (int i = 0; i < graphsForTest.size(); i++)
			{
				Graph<Integer> g = graphsForTest.get(i);
				LOGGER.debug("Classes size:" + classes.size());
				ForkJoinPool forkJoinPool = new ForkJoinPool(classes.size());
				Set<GraphTest> snarkTests = new LinkedHashSet<GraphTest>();

				for (Class<?> c : classes)
				{
					GraphTest snarkTest = null;
					try
					{
						snarkTest = (GraphTest) c.newInstance();
						snarkTest.init(g);
						snarkTests.add(snarkTest);
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

				for (GraphTest st : snarkTests)
				{
					forkJoinPool.execute(st);
				}

				GraphTestResult graphTestResult = null;

				while (graphTestResult == null)
				{
					for (GraphTest st : snarkTests)
					{
						if (st.isDone())
						{
							try
							{
								graphTestResult = st.getResult();
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

				int graphId = super.graphs.indexOf(g);

				String sout = "Testing run: " + (run + 1) + ", Graph id: " + graphId + ", Result: " + graphTestResult;
				LOGGER.info(sout);

				results[run][i] = graphTestResult;
			}
		}

		Map<String, Object> resultValues = new HashMap<String, Object>();
		resultValues.put("testType", SnarkTestTypes.ALGORITHM_COMPARATION);
		resultValues.put("test", "Snark algorithm comparation");
		resultValues.put("resultsData", results);
		resultValues.put("times", super.loops);

		return resultValues;
	}
}
