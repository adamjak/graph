package net.adamjak.graph.classes;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * Created by Tomas Adamjak on 16.12.2015.
 * Copyright 2015, Tomas Adamjak
 * License: The BSD 3-Clause License
 */
public class Graph<T extends Comparable>
{
	private final Class<? extends T> graphType;
	private boolean directed = false;

	private Map<Vertex<T>, List<Edge<T>>> structure = new ConcurrentSkipListMap<Vertex<T>, List<Edge<T>>>();

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

		List<Edge<T>> spanningTreeEdges = new ArrayList<Edge<T>>();

		for (Vertex<T> vertex : this.structure.keySet())
		{
			List<Edge<T>> edges = this.structure.get(vertex);

			if (spanningTreeEdges.isEmpty())
			{
				for (Edge<T> edge : edges)
				{
					spanningTreeEdges.add(edge);
					components.put(edge.getStart(),0);
					components.put(edge.getEnd(),0);
				}
			}
			else
			{
				for (Edge<T> edge : edges)
				{
					if (components.get(edge.getStart()) != components.get(edge.getEnd()))
					{
						spanningTreeEdges.add(edge);
						int start = components.get(edge.getStart());
						int end = components.get(edge.getEnd());

						for (Vertex<T> v : components.keySet())
						{
							if (components.get(v) == start || components.get(v) == end)
							{
								components.put(v,start);
							}
						}
					}
				}
			}

		}
		
		return spanningTreeEdges;
	}

	public List<Edge<T>> getGraphMeat()
	{
		List<Edge<T>> meatEdges = new ArrayList<Edge<T>>();
		List<Edge<T>> spanningTree = this.getSpanningTree();

		for (Vertex<T> v : this.structure.keySet())
		{
			for (Edge<T> e : this.structure.get(v))
			{
				if (spanningTree.contains(e) == false && meatEdges.contains(e) == false)
				{
					meatEdges.add(e);
				}
			}
		}

		return meatEdges;
	}

	public Integer[][] getMatrixOfNeighborVertexes()
	{
		Map<Integer,Vertex<T>> vertexIndexes = new TreeMap<Integer, Vertex<T>>();

		int i = 0;
		for (Vertex<T> v : this.structure.keySet())
		{
			vertexIndexes.put(i,v);
			i++;
		}

		Integer[][] matrix = new Integer[this.structure.keySet().size()][this.structure.keySet().size()];

		for (i = 0; i < matrix.length; i++)
		{
			for (int j = 0; j < matrix.length; j++)
			{
				matrix[i][j] = 0;

				Vertex<T> topVertex = vertexIndexes.get(i);
				Vertex<T> leftVertex = vertexIndexes.get(j);

				if (topVertex.equals(leftVertex) == false)
				{
					List<Edge<T>> topVertexList = this.structure.get(topVertex);

					for (Edge<T> e : this.structure.get(leftVertex))
					{
						if (topVertexList.contains(e))
						{
							matrix[i][j] = 1;
							break;
						}
					}
				}
				else
				{
					for (Edge<T> e : this.structure.get(leftVertex))
					{
						if (e.getStart().equals(leftVertex) && e.getEnd().equals(leftVertex))
						{
							matrix[i][j] = 1;
							break;
						}
					}
				}
			}
		}

		return matrix;
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
