package net.adamjak.graph;

import net.adamjak.graph.api.Graph;
import net.adamjak.graph.classes.GraphFactory;
import net.adamjak.graph.interfaces.anot.Benchmark;
import net.adamjak.graph.utils.ClassFinder;

import java.io.File;
import java.util.List;

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

		String packageName = "net.adamjak.graph.cubic.snarks.tests";

		for (Class<?> c : classFinder.findAnnotatedClasses(packageName, true, Benchmark.class)) // FIXME: 29.7.2016
		{
			System.out.println(c.getName());
		}

//		File file = new File("src/main/resources/xml/test.xml");
//		GraphImpl<String> g = GraphFactory.createGraphFromGraphml(file);

//		ExecutorService executorService = Executors.newFixedThreadPool(1);
//
//		for (Graph<Integer> g : listOfGraphs)
//		{
//			try
//			{
//				SnarkTestResult strEdgeBackTrace = executorService.submit(new EdgeBackTrace<Integer>(g)).get();
//				//SnarkTestResult strOneFactor = executorService.submit(new OneFactor<Integer>(g)).get();
//				System.out.println("Snark test result EdgeImpl Back Trace:\n Time: " + strEdgeBackTrace.getTimeInSeconds() + " second\n Snark:" + strEdgeBackTrace.isSnark());
//				System.out.println();
//				//System.out.println("Snark test result One Factor:\n Time: " + strOneFactor.getTimeInSeconds() + " second\n Snark:" + strOneFactor.isSnark());
//			}
//			catch (InterruptedException e)
//			{
//				e.printStackTrace();
//			}
//			catch (ExecutionException e)
//			{
//				e.printStackTrace();
//			}
//		}
//
//		executorService.shutdown();
	}
}
