package net.adamjak.graph.classes;

import java.util.*;

/**
 * Created by Tomas Adamjak on 16.12.2015.
 * Copyright 2015, Tomas Adamjak
 * License: The BSD 3-Clause License
 */
public class Graph<T extends Comparable>
{
	private final Class<? extends T> graphType;
	private boolean directed = false;

	private Map<Vertex<T>, List<Edge<T>>> structure = new TreeMap<Vertex<T>, List<Edge<T>>>();

	private Graph()
	{
		throw new IllegalStateException();
	}

	private Graph(Class<? extends T> type)
	{
		this.graphType = type;
	}

	public static <U extends Comparable> Graph<U> createGraph(Class<? extends U> type)
	{
		return new Graph<U>(type);
	}

	public void addVertex (Vertex<T> vertex)
	{
		if (this.structure.containsKey(vertex)) throw new IllegalArgumentException("Vertex '" + vertex + "' is already in graph.");

		this.structure.put(vertex,new ArrayList<Edge<T>>());
	}

	public Vertex<T> getVertexByContent (T content)
	{
		for (Vertex<T> v : this.structure.keySet())
		{
			if (v.getContent() == content)
			{
				return v;
			}
		}

		return null;
	}
	
	public void addEdge (Edge<T> edge)
	{
		if (!this.structure.containsKey(edge.getStart())) throw new IllegalArgumentException("Vertex '" + edge.getStart() + "' is not in graph.");
		if (!this.structure.containsKey(edge.getEnd())) throw new IllegalArgumentException("Vertex '" + edge.getEnd() + "' is not in graph.");

		if (!this.structure.get(edge.getStart()).contains(edge))
		{
			this.structure.get(edge.getStart()).add(edge);
		}

		if (!this.structure.get(edge.getEnd()).contains(edge))
		{
			this.structure.get(edge.getEnd()).add(edge);
		}
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

	public Class<? extends T> getGraphType ()
	{
		return this.graphType;
	}

	public Map<Vertex<T>, List<Edge<T>>> getStructure ()
	{
		return this.structure;
	}

	/**
	 * Kruskal's algorithm whitch returned spanning-tree of graph
	 * Complexity - O(E*log(E))
	 *
	 * @return List<Edge<T>> with spanning tree edges
	 */
	public List<Edge<T>> getSpanningTree()
	{
		Map<Vertex<T>, Integer> components = new TreeMap<Vertex<T>, Integer>();

		int i = 0;
		for (Vertex<T> vertex : this.structure.keySet())
		{
			components.put(vertex,i);
			i++;
		}

		// TODO: 31.12.2015 - dokoncit kostru 
		
		return null;
	}

	public void setDirected (boolean directed)
	{
		this.directed = directed;
	}

	public boolean isDirected ()
	{
		return this.directed;
	}
}
