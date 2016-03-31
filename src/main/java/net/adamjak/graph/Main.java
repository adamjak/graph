package net.adamjak.graph;

import net.adamjak.graph.classes.Edge;
import net.adamjak.graph.classes.Graph;
import net.adamjak.graph.classes.GraphFactory;
import net.adamjak.graph.classes.Vertex;
import net.adamjak.graph.cubic.snarks.SnarkTestResult;
import net.adamjak.graph.cubic.snarks.tests.EdgeBackTrace;
import net.adamjak.graph.cubic.snarks.tests.OneFactor;
import net.adamjak.graph.io.xsd.Graphml;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Tomas Adamjak on 4.11.2015.
 * Copyright 2015, Tomas Adamjak
 * License: The BSD 3-Clause License
 */
public class Main
{
	public static void main (String args[])
	{
		File file = new File("src/main/resources/ba/G-MIDZI.72");
		List<Graph<Integer>> listOfGraphs = GraphFactory.createGraphFromTextCatalog(file);

//		File file = new File("src/main/resources/xml/test.xml");
//		Graph<String> g = GraphFactory.createGraphFromGraphml(file);

		ExecutorService executorService = Executors.newFixedThreadPool(1);

		for (Graph<Integer> g : listOfGraphs)
		{
			try
			{
				SnarkTestResult strEdgeBackTrace = executorService.submit(new EdgeBackTrace<Integer>(g)).get();
				SnarkTestResult strOneFactor = executorService.submit(new OneFactor<Integer>(g)).get();
				System.out.println("Snark test result Edge Back Trace:\n Time: " + strEdgeBackTrace.getTimeInSeconds() + " second\n Snark:" + strEdgeBackTrace.isSnark());
				System.out.println();
				System.out.println("Snark test result One Factor:\n Time: " + strOneFactor.getTimeInSeconds() + " second\n Snark:" + strOneFactor.isSnark());
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

		executorService.shutdown();
	}
}
