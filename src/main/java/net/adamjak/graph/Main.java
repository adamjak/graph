package net.adamjak.graph;

import net.adamjak.graph.api.Graph;
import net.adamjak.graph.classes.GraphFactory;
import net.adamjak.graph.cubic.snarks.SnarkTest;
import net.adamjak.graph.cubic.snarks.SnarkTestResult;
import net.adamjak.graph.interfaces.anot.Benchmarked;
import net.adamjak.graph.utils.ClassFinder;

import java.io.File;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;

/**
 * Created by Tomas Adamjak on 4.11.2015.
 * Copyright 2015, Tomas Adamjak
 * License: The BSD 3-Clause License
 */
public class Main
{
	public static void main (String args[])
	{
		File file = new File("src/main/resources/ba/SC4.24");
		List<Graph<Integer>> listOfGraphs = GraphFactory.createGraphFromTextCatalog(file);

		ClassFinder classFinder = new ClassFinder();

		String packageName = "net.adamjak.graph.cubic.snarks";

		Set<Class<?>> classes = classFinder.findClassesWhitchExtends(classFinder.findAnnotatedClasses(packageName, true, Benchmarked.class),SnarkTest.class);

		for (Graph<Integer> g : listOfGraphs)
		{
			ForkJoinPool forkJoinPool = new ForkJoinPool(classes.size());

			Set<SnarkTest> snarkTests = new LinkedHashSet<SnarkTest>();

			for (Class<?> c : classes)
			{
				SnarkTest snarkTest = null;
				try
				{
					snarkTest = (SnarkTest) c.newInstance();
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

			for (SnarkTest st : snarkTests)
			{
				forkJoinPool.execute(st);
			}

			SnarkTestResult snarkTestResult = null;

			while (snarkTestResult == null)
			{
				for (SnarkTest st : snarkTests)
				{
					if (st.isDone())
					{
						try
						{
							snarkTestResult =st.getResult();
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

			System.out.println("Snark test result " + snarkTestResult.getSnarkTesterClass().getSimpleName() + ":\n Time: " + snarkTestResult.getTimeInSeconds() + " second\n Snark:" + snarkTestResult.isSnark());

		}
	}
}
