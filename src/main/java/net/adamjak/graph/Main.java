package net.adamjak.graph;

import net.adamjak.graph.classes.Graph;
import net.adamjak.graph.classes.GraphFactory;
import net.adamjak.graph.io.xsd.Graphml;

import java.io.File;
import java.util.Calendar;
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
//		File file = new File("src/main/resources/ba/PETERSEN.10");
//		GraphFactory.createGraphFromTextCatalog(file).get(0).print();

		File file = new File("src/main/resources/xml/test.xml");
		GraphFactory.createGraphFromGraphml(file).print();

		long startTime = Calendar.getInstance().getTime().getTime();
		long endTime = Calendar.getInstance().getTime().getTime();


		System.out.println("Time: " + (endTime - startTime));

	}
}
