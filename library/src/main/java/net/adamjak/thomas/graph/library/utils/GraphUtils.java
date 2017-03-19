package net.adamjak.thomas.graph.library.utils;

import net.adamjak.thomas.graph.library.api.Edge;
import net.adamjak.thomas.graph.library.api.Graph;
import net.adamjak.thomas.graph.library.api.Vertex;
import net.adamjak.thomas.graph.library.classes.EdgeImpl;
import net.adamjak.thomas.graph.library.classes.GraphImpl;
import net.adamjak.thomas.graph.library.classes.VertexImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Tomas Adamjak on 22.12.2016.
 * Copyright 2016, Tomas Adamjak
 * License: The BSD 3-Clause License
 */
public class GraphUtils
{
	/**
	 * @param graph Graph in whitch we search pairs
	 * @param <T> type of graph
	 * @return Return all double combinations of edges from inserted graph.
	 */
	public static <T extends Comparable> Set<Utils.Pair<Edge<T>>> getAllEdgePairs (Graph<T> graph)
	{
		if (graph == null) throw new IllegalArgumentException("Param graph can not be null.");

		List<Edge<T>> edges = graph.getListOfEdges();

		Set<Utils.Pair<Edge<T>>> pairList = new HashSet<Utils.Pair<Edge<T>>>();

		for (int i = 0; i < edges.size(); i++)
		{
			for (int j = i + 1; j < edges.size(); j++)
			{
				pairList.add(new Utils.Pair<Edge<T>>(edges.get(i), edges.get(j)));
			}
		}

		return pairList;
	}

	/**
	 * Create all dot-products from 2 inserted graphs.
	 *
	 * @param g1 First graph for dot-product
	 * @param g2 Second graph for dot-product
	 * @param <T> type of graphs
	 * @return Return <code>{@link List}&lt;{@link Graph}&lt;{@link Integer}&gt;&gt;</code> with new instance of dot-products graphs.
	 */
	public static <T extends Comparable> List<Graph<Integer>> createAllDotProducts (Graph<T> g1, Graph<T> g2)
	{
		List<Graph<Integer>> dotProducts = new LinkedList<Graph<Integer>>();

		List<Utils.Pair<Edge<T>>> g1EdgePairs = new LinkedList<Utils.Pair<Edge<T>>>(GraphUtils.getAllEdgePairs(g1));
		List<Edge<T>> g2Edges = g2.getListOfEdges();

		for (Utils.Pair<Edge<T>> g1Pair : g1EdgePairs)
		{
			for (Edge<T> g2Edge : g2Edges)
			{
				Graph<Integer> dotProduct = GraphImpl.createGraph(Integer.class);

				int vertexCounter = 0;
				int edgeCounter = 0;

				Map<Vertex<T>, Vertex<Integer>> mapForG1 = new HashMap<Vertex<T>, Vertex<Integer>>();
				Map<Vertex<T>, Vertex<Integer>> mapForG2 = new HashMap<Vertex<T>, Vertex<Integer>>();

				for (Vertex<T> v : g1.getListOfVertexes())
				{
					Vertex<Integer> newVertex = new VertexImpl<Integer>(vertexCounter++);
					mapForG1.put(v, newVertex);
					dotProduct.addVertex(newVertex);
				}

				for (Vertex<T> v : g2.getListOfVertexes())
				{
					if ((v == g2Edge.getStart() || v == g2Edge.getEnd()) == false)
					{
						Vertex<Integer> newVertex = new VertexImpl<Integer>(vertexCounter++);
						mapForG2.put(v, newVertex);
						dotProduct.addVertex(newVertex);
					}
				}

				for (Edge<T> e : g1.getListOfEdges())
				{
					if (g1Pair.isInPair(e) == false)
					{
						Edge<Integer> newEdge = new EdgeImpl<Integer>(edgeCounter++, mapForG1.get(e.getStart()), mapForG1.get(e.getEnd()), false);
						dotProduct.addEdge(newEdge);
					}
				}

				for (Edge<T> e : g2.getListOfEdges())
				{
					if ((g2.getEdgesContainsVertex(e.getStart()).contains(e) || g2.getEdgesContainsVertex(e.getEnd()).contains(e)) == false)
					{
						Edge<Integer> newEdge = new EdgeImpl<Integer>(edgeCounter++, mapForG2.get(e.getStart()), mapForG2.get(e.getEnd()), false);
						dotProduct.addEdge(newEdge);
					}
				}

				List<Vertex<T>> g2EdgeStartNeighbor = new ArrayList<Vertex<T>>(g2.getNeighborVertexes(g2Edge.getStart()));
				g2EdgeStartNeighbor.remove(g2Edge.getEnd());

				List<Vertex<T>> g2EdgeEndNeighbor = new ArrayList<Vertex<T>>(g2.getNeighborVertexes(g2Edge.getEnd()));
				g2EdgeEndNeighbor.remove(g2Edge.getStart());

				Vertex<T> v1 = g2EdgeStartNeighbor.get(0);
				Vertex<T> v2 = g2EdgeStartNeighbor.get(1);
				Vertex<T> v3 = g2EdgeEndNeighbor.get(0);
				Vertex<T> v4 = g2EdgeEndNeighbor.get(1);
				Vertex<T> va = g1Pair.getFirst().getStart();
				Vertex<T> vb = g1Pair.getFirst().getEnd();
				Vertex<T> vc = g1Pair.getSecond().getStart();
				Vertex<T> vd = g1Pair.getSecond().getEnd();

				dotProduct.addEdge(new EdgeImpl<Integer>(edgeCounter++, mapForG2.get(v1), mapForG1.get(va), false));
				dotProduct.addEdge(new EdgeImpl<Integer>(edgeCounter++, mapForG2.get(v2), mapForG1.get(vb), false));
				dotProduct.addEdge(new EdgeImpl<Integer>(edgeCounter++, mapForG2.get(v3), mapForG1.get(vc), false));
				dotProduct.addEdge(new EdgeImpl<Integer>(edgeCounter++, mapForG2.get(v4), mapForG1.get(vd), false));

				dotProducts.add(dotProduct);
			}
		}

		return dotProducts;
	}

	/**
	 * Create new {@link Graph} with {@link Integer} type. Vertexes and Edges will be numbered from 0.
	 *
	 * @param g graph whitch we want clone to new graph of integer type
	 * @param <T> type of graph
	 * @return Return new instance of <code>{@link Graph}&lt;{@link Integer}&gt;</code> with same structure like inserted graph.
	 * @throws IllegalArgumentException if param {@code g} is {@code null}.
	 */
	public static <T extends Comparable> Graph<Integer> cloneGraphToIntegerType (Graph<T> g) throws IllegalArgumentException
	{
		if (g == null) throw new IllegalArgumentException("Param 'g' can not be null!");

		Graph<Integer> newGraph = GraphImpl.createGraph(Integer.class);

		int vertexCounter = 0;
		int edgeCounter = 0;

		Map<Vertex<T>, Vertex<Integer>> map = new HashMap<Vertex<T>, Vertex<Integer>>();

		for (Vertex<T> v : g.getListOfVertexes())
		{
			Vertex<Integer> newVertex = new VertexImpl<Integer>(vertexCounter++);
			map.put(v, newVertex);
			newGraph.addVertex(newVertex);
		}

		for (Edge<T> e : g.getListOfEdges())
		{
			Edge<Integer> newEdge = new EdgeImpl<Integer>(edgeCounter++, map.get(e.getStart()), map.get(e.getEnd()), false);
			newGraph.addEdge(newEdge);
		}

		return newGraph;
	}

	/**
	 * @param g graph
	 * @param <T> type of graph
	 * @return Return int array with special adjacency matrix format from inserted graph.
	 * @throws IllegalArgumentException if param g is null
	 * @see Graph#getAdjacencyMatrix()
	 */
	public static <T extends Comparable> int[] getSpecialAdjacencyMatrix(Graph<T> g) throws IllegalArgumentException
	{
		if (g == null) throw new IllegalArgumentException("Param g can not be null.");

		Byte[][] matrix = g.getAdjacencyMatrix();

		int[] specialMatrix = new int[matrix.length * matrix.length];

		int k = 0;

		for (int i = 0; i < matrix.length; i++)
		{
			for (int j = 0; j < matrix[i].length; j++)
			{
				specialMatrix[k] = matrix[i][j];
				k++;
			}
		}

		return specialMatrix;
	}

	/**
	 * Create incidence matrix for inserted graph.
	 *
	 * @param g graph
	 * @param <T> graph type
	 * @return Incidence matrix in two dimension array
	 * @throws IllegalArgumentException if param g is null
	 * @see Graph#getAdjacencyMatrix()
	 */
	public static <T extends Comparable> Byte[][] getIncidenceMatrix (Graph<T> g) throws IllegalArgumentException
	{
		if (g == null) throw new IllegalArgumentException("Param g can not be null.");

		Byte[][] matrix = new Byte[g.getCountOfVertexes()][g.getListOfEdges().size()];

		for (int i = 0; i < g.getListOfVertexes().size(); i++)
		{
			Vertex<T> v = g.getListOfVertexes().get(i);

			for (int j = 0; j < g.getListOfEdges().size(); j++)
			{
				Edge<T> e = g.getListOfEdges().get(j);

				if (e.getStart().equals(v) || e.getEnd().equals(v))
				{
					matrix[i][j] = 1;
				}
				else
				{
					matrix[i][j] = 0;
				}
			}
		}

		return matrix;
	}

	/**
	 * @param g graph
	 * @param <T> type of graph
	 * @return Return int array with special incidence matrix format from inserted graph.
	 * @throws IllegalArgumentException if param g is null
	 * @see Graph#getAdjacencyMatrix()
	 * @see GraphUtils#getIncidenceMatrix(Graph)
	 */
	public static <T extends Comparable> int[] getSpecialIncidenceMatrix (Graph<T> g) throws IllegalArgumentException
	{
		if (g == null) throw new IllegalArgumentException("Param g can not be null.");

		Byte[][] matrix = GraphUtils.getIncidenceMatrix(g);

		int[] specialMatrix = new int[matrix.length * matrix[0].length];

		int k = 0;

		for (int i = 0; i < matrix.length; i++)
		{
			for (int j = 0; j < matrix[i].length; j++)
			{
				specialMatrix[k] = matrix[i][j];
				k++;
			}
		}

		return specialMatrix;
	}
}