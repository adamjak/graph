package net.adamjak.thomas.graph.library;

import net.adamjak.thomas.graph.library.api.Graph;
import net.adamjak.thomas.graph.library.io.GraphFactory;
import net.adamjak.thomas.graph.library.tests.GraphTestResult;
import net.adamjak.thomas.graph.library.tests.snarks.Kowalik;

import java.io.File;
import java.io.IOException;

/**
 * Created by Tomas Adamjak on 5.3.2017.
 * Copyright 2017, Tomas Adamjak
 * License: The BSD 3-Clause License
 */
public class MainForKowalikTest
{
	public static void main (String[] args) throws IOException
	{
		String filePath = "/home/tadamjak/work/graphs_library/Generated_graphs.24.05.sn.cyc4.g6";
		Graph<Integer> g = GraphFactory.createGraphFromGraph6(new File(filePath)).get(0);

		Kowalik<Integer> kowalik = new Kowalik<Integer>();
		kowalik.init(g);
		GraphTestResult gtr = kowalik.call();

		System.out.println("Snark: " + gtr.getValue(GraphTestResult.SNARK_KEY));
		System.out.println("Time: " + gtr.getValue(GraphTestResult.TIME_KEY));
		System.out.println("Time with JNI: " + gtr.getValue("timeWithJni"));
	}
}
