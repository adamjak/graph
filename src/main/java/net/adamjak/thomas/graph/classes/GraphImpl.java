package net.adamjak.thomas.graph.classes;

import net.adamjak.thomas.graph.api.Edge;
import net.adamjak.thomas.graph.api.Graph;
import net.adamjak.thomas.graph.api.Vertex;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * Created by Tomas Adamjak on 16.12.2015.
 * Copyright 2015, Tomas Adamjak
 * License: The BSD 3-Clause License
 */
public class GraphImpl<T extends Comparable> implements Graph<T>
{
	private final Class<? extends T> graphType;
	private boolean directed = false;

	private ConcurrentMap<Vertex<T>, List<Edge<T>>> structure = new ConcurrentSkipListMap<Vertex<T>, List<Edge<T>>>();

	private GraphImpl ()
	{
		throw new IllegalStateException();
	}

	private GraphImpl (Class<? extends T> type)
	{
		this.graphType = type;
	}

	public static <U extends Comparable> GraphImpl<U> createGraph(Class<? extends U> type)
	{
		return new GraphImpl<U>(type);
	}

	@Override
	public void addVertex (Vertex<T> vertex)
	{
		if (this.structure.containsKey(vertex)) throw new IllegalArgumentException("VertexImpl '" + vertex + "' is already in graph.");

		this.structure.put(vertex,Collections.synchronizedList(new ArrayList<Edge<T>>()));
	}

	@Override
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

	@Override
	public int getCountOfVertexes ()
	{
		return this.getListOfVertexes().size();
	}

	@Override
	public List<Vertex<T>> getListOfVertexes ()
	{
		List<Vertex<T>> list = new ArrayList<Vertex<T>>();

		for (Vertex<T> v : this.structure.keySet())
		{
			list.add(v);
		}

		return list;
	}
	
	@Override
	public void addEdge (Edge<T> edge)
	{
		if (this.structure.containsKey(edge.getStart()) == false) throw new IllegalArgumentException("VertexImpl '" + edge.getStart() + "' is not in graph.");
		if (this.structure.containsKey(edge.getEnd()) == false) throw new IllegalArgumentException("VertexImpl '" + edge.getEnd() + "' is not in graph.");

		if (this.structure.get(edge.getStart()).contains(edge) == false)
		{
			this.structure.get(edge.getStart()).add(edge);
		}

		if (this.structure.get(edge.getEnd()).contains(edge) == false)
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

	@Override
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
	 * @return {@link List}&lt;{@link Edge}&lt;{@link T}&gt;&gt; with spanning tree edges
	 */
	@Override
	public List<Edge<T>> getSpanningTree ()
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

	/**
	 * @return List with all graph edges without spanning-tree edges.
	 * @see GraphImpl#getSpanningTree()
	 */
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

	public ConcurrentMap<Edge<T>,List<Edge<T>>> getMapOfNeighborEdges()
	{
		ConcurrentMap<Edge<T>,List<Edge<T>>> map = new ConcurrentSkipListMap<Edge<T>,List<Edge<T>>>();

		for (Edge<T> e : this.getListOfEdges())
		{
			List<Edge<T>> neighborEdges = Collections.synchronizedList(new ArrayList<Edge<T>>());

			for (Edge<T> edge : this.structure.get(e.getStart()))
			{
				if (neighborEdges.contains(edge) == false && edge.equals(e) == false)
				{
					neighborEdges.add(edge);
				}
			}

			for (Edge<T> edge : this.structure.get(e.getEnd()))
			{
				if (neighborEdges.contains(edge) == false && edge.equals(e) == false)
				{
					neighborEdges.add(edge);
				}
			}

			if (map.containsKey(e))
			{
				int i = 0;
				//throw new RuntimeException();
			}
			map.put(e,neighborEdges);
		}

		return map;
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

	@Override
	public void setDirected (boolean directed)
	{
		this.directed = directed;
	}

	@Override
	public boolean isDirected ()
	{
		return this.directed;
	}

	/**
	 * @return List of all graph edges
	 */
	@Override
	public List<Edge<T>> getListOfEdges ()
	{
		List<Edge<T>> edges = Collections.synchronizedList(new ArrayList<Edge<T>>());

		for (Vertex<T> v : this.structure.keySet())
		{
			for (Edge<T> e : this.structure.get(v))
			{
				if (edges.contains(e) == false)
				{
					edges.add(e);
				}
			}
		}

		return edges;
	}

	@Override
	public Set<Vertex<T>> getNeighborVertexes (Vertex<T> vertex)
	{
		Set<Vertex<T>> vertexes = new HashSet<Vertex<T>>();

		for (Edge<T> edge : this.structure.get(vertex))
		{
			if (edge.getStart().equals(vertex))
			{
				vertexes.add(edge.getEnd());
			}
			else
			{
				vertexes.add(edge.getStart());
			}
		}

		return vertexes;
	}

	/**
	 * @return List of all cyrcles in graph
	 */
	public ConcurrentSkipListSet<Cycle<T>> getListOfAllCyrcles()
	{
		ConcurrentSkipListSet<Cycle<T>> cycleConcurrentSkipListSet = new ConcurrentSkipListSet<Cycle<T>>();

		for (Vertex<T> rootVertex : this.structure.keySet())
		{
			ConcurrentLinkedQueue<List<Vertex<T>>> queue = new ConcurrentLinkedQueue<>();

			for (Vertex<T> v : this.getNeighborVertexes(rootVertex))
			{
				List<Vertex<T>> lhs = Collections.synchronizedList(new ArrayList<Vertex<T>>());
				lhs.add(rootVertex);
				lhs.add(v);
				queue.add(lhs);
			}

			while (queue.isEmpty() == false && queue.peek().size() <= this.structure.size())
			{
				List<Vertex<T>> lhs = queue.poll();

				for (Vertex<T> v : this.getNeighborVertexes(lhs.get(lhs.size()-1)))
				{
					if (lhs.contains(v) == false)
					{
						if (v.equals(rootVertex))
						{
							Cycle<T> cycle = new Cycle<T>();

							for (Vertex<T> cyrcleVertex : lhs)
							{
								cycle.addVertexIntoCycle(cyrcleVertex);
							}

							cycleConcurrentSkipListSet.add(cycle);
						}
						else
						{
							List<Vertex<T>> newPath = Collections.synchronizedList(new ArrayList<Vertex<T>>(lhs));
							newPath.add(v);
							queue.add(newPath);
						}
					}
				}
			}
		}

		return cycleConcurrentSkipListSet;
	}
}
