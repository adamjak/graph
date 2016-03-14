package net.adamjak.graph.cubic.snarks.tests;

import net.adamjak.graph.classes.Vertex;
import net.adamjak.graph.cubic.snarks.SnarkTestResult;
import net.adamjak.graph.classes.Graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Tomas Adamjak on 3.1.2016.
 * Copyright 2016, Tomas Adamjak
 * License: The BSD 3-Clause License
 */
public class OneFactor<T extends Comparable> implements Callable<SnarkTestResult>
{
	private Graph<T> graph;

	public OneFactor (Graph<T> graph)
	{
		this.graph = graph;
	}

	@Override
	public SnarkTestResult call() throws Exception
	{
		List<Vertex<T>> listOfVertexes = this.graph.getListOfVertexes();
		for (Vertex<T> v : listOfVertexes)
		{
			v.setVisited(false);
		}

		int index = 0;

		Vertex<T> selectedVertex = listOfVertexes.get(index);

		// TODO: 15.3.2016 - ist na konzultaciu pochopit algoritmu 

		return null;
	}
}
