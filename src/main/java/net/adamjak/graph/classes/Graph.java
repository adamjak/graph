package net.adamjak.graph.classes;

import java.util.*;

/**
 * Created by Tomas Adamjak on 16.12.2015.
 * Copyright 2015, Tomas Adamjak
 * License: The BSD 3-Clause License
 */
public class Graph<T extends Comparable>
{
	private Map<Vertex<T>, List<Edge<T>>> structure = new TreeMap<Vertex<T>, List<Edge<T>>>();

	public Map<Vertex<T>, List<Edge<T>>> getStructure ()
	{
		return this.structure;
	}

	public void addVertex (Vertex<T> vertex)
	{
		if (this.structure.containsKey(vertex)) throw new IllegalArgumentException("Vertex '" + vertex + "' is already in graph.");

		this.structure.put(vertex,new ArrayList<Edge<T>>());
	}

	public void addEdge (Vertex<T> startVertex, Vertex<T> endVertex)
	{
		Edge<T> edge = new Edge<T>(startVertex,endVertex);
		this.addEdge(edge);
	}
	
	public void addEdge (Edge<T> edge)
	{
		if (this.structure.containsKey(edge.getStart())) throw new IllegalArgumentException("Vertex '" + edge.getStart() + "' is already in graph.");
		if (this.structure.containsKey(edge.getEnd())) throw new IllegalArgumentException("Vertex '" + edge.getEnd() + "' is already in graph.");

		if(this.structure.get(edge.getStart()).contains(edge) || this.structure.get(edge.getEnd()).contains(edge)) throw new IllegalArgumentException("Edge '" + edge + "' is already in graph.");

		this.structure.get(edge.getStart()).add(edge);
		this.structure.get(edge.getEnd()).add(edge);
	}

	public void print()
	{
		for (Vertex v : this.structure.keySet())
		{
			System.out.println(v);
			for (Edge e : this.structure.get(v))
			{
				System.out.println(" ∟ " + e);
			}
			System.out.println();
		}
	}
}
