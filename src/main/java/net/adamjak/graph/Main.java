package net.adamjak.graph;

import net.adamjak.graph.classes.Edge;
import net.adamjak.graph.classes.Graph;
import net.adamjak.graph.classes.GraphFactory;
import net.adamjak.graph.classes.Vertex;
import net.adamjak.graph.cubic.snarks.SnarkTestResult;
import net.adamjak.graph.cubic.snarks.tests.EdgeBackTrace;
import net.adamjak.graph.io.xsd.Graphml;

import java.io.File;
import java.util.Calendar;
import java.util.List;
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
		File file = new File("src/main/resources/ba/SC4.24");
		List<Graph<Integer>> listOfGraphs = GraphFactory.createGraphFromTextCatalog(file);

//		File file = new File("src/main/resources/xml/test.xml");
//		Graph<String> g = GraphFactory.createGraphFromGraphml(file);

		ExecutorService executorService = Executors.newFixedThreadPool(3);

		for (Graph<Integer> g : listOfGraphs)
		{
			try
			{
				SnarkTestResult snarkTestResult = executorService.submit(new EdgeBackTrace<Integer>(g)).get();
				System.out.println("Snark test result:\n Time: " + snarkTestResult.getTime() + "\n Snark:" + snarkTestResult.isSnark());
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
