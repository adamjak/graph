package net.adamjak.thomas.graph.library.utils;

import net.adamjak.thomas.graph.library.api.Edge;
import net.adamjak.thomas.graph.library.api.Graph;
import net.adamjak.thomas.graph.library.api.Vertex;
import net.adamjak.thomas.graph.library.classes.EdgeImpl;
import net.adamjak.thomas.graph.library.classes.GraphImpl;
import net.adamjak.thomas.graph.library.classes.VertexImpl;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Tomas Adamjak on 22.12.2016.
 * Copyright 2016, Tomas Adamjak
 * License: The BSD 3-Clause License
 */
public class GraphUtilsTest
{
	@Test
	public void getAllEdgePairsTest () throws Exception
	{
		Vertex<Integer> v1 = new VertexImpl<Integer>(1);
		Vertex<Integer> v2 = new VertexImpl<Integer>(2);
		Vertex<Integer> v3 = new VertexImpl<Integer>(3);
		Vertex<Integer> v4 = new VertexImpl<Integer>(4);

		Edge<Integer> e1 = new EdgeImpl<Integer>(1, v1, v2, false);
		Edge<Integer> e2 = new EdgeImpl<Integer>(2, v2, v3, false);
		Edge<Integer> e3 = new EdgeImpl<Integer>(3, v3, v4, false);
		Edge<Integer> e4 = new EdgeImpl<Integer>(4, v2, v4, false);

		Graph<Integer> g1 = GraphImpl.createGraph(Integer.class);
		g1.addVertex(v1);
		g1.addVertex(v2);
		g1.addVertex(v3);
		g1.addVertex(v4);
		g1.addEdge(e1);
		g1.addEdge(e2);
		g1.addEdge(e3);
		g1.addEdge(e4);

		Set<Utils.Pair<Edge<Integer>>> expected = new HashSet<Utils.Pair<Edge<Integer>>>(6);
		expected.add(new Utils.Pair<Edge<Integer>>(e1, e2));
		expected.add(new Utils.Pair<Edge<Integer>>(e1, e3));
		expected.add(new Utils.Pair<Edge<Integer>>(e1, e4));
		expected.add(new Utils.Pair<Edge<Integer>>(e2, e3));
		expected.add(new Utils.Pair<Edge<Integer>>(e2, e4));
		expected.add(new Utils.Pair<Edge<Integer>>(e3, e4));

		Set<Utils.Pair<Edge<Integer>>> result = GraphUtils.getAllEdgePairs(g1);

		assertTrue(expected.containsAll(result));
	}

	@Test
	public void createAllDotProductsTest () throws Exception
	{
		Graph<Integer> g1 = InterestingGraphs.getPetersenGraph();
		Graph<Integer> g2 = InterestingGraphs.getPetersenGraph();

		int countOfDotProducts = ((g1.getListOfEdges().size() * (g1.getListOfEdges().size() - 1)) / 2) * g2.getListOfEdges().size();

		List<Graph<Integer>> graphs = GraphUtils.createAllDotProducts(g1, g2);
		assertTrue(graphs.size() == countOfDotProducts);
	}

	@Test
	public void getSpecialAdjacencyMatrixTest () throws Exception
	{
		Vertex<Integer> v1 = new VertexImpl<Integer>(1);
		Vertex<Integer> v2 = new VertexImpl<Integer>(2);
		Vertex<Integer> v3 = new VertexImpl<Integer>(3);

		Edge<Integer> e1 = new EdgeImpl<Integer>(1, v1, v2, false);
		Edge<Integer> e2 = new EdgeImpl<Integer>(2, v2, v3, false);

		Graph<Integer> graph = GraphImpl.createGraph(Integer.class);
		graph.addVertex(v1);
		graph.addVertex(v2);
		graph.addVertex(v3);
		graph.addEdge(e1);
		graph.addEdge(e2);

		int[] result = GraphUtils.getSpecialAdjacencyMatrix(graph);
		int[] expectedResult = {0,1,0,1,0,1,0,1,0};

		assertEquals(expectedResult.length,result.length);

		for (int i = 0; i < result.length; i++)
		{
			assertEquals(expectedResult[i],result[i]);
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void getSpecialAdjacencyMatrixTestIllegalArgumentException () throws Exception
	{
		GraphUtils.getSpecialAdjacencyMatrix(null);
	}

	@Test
	public void getIncidenceMatrixTest () throws Exception
	{
		Vertex<Integer> v1 = new VertexImpl<Integer>(1);
		Vertex<Integer> v2 = new VertexImpl<Integer>(2);
		Vertex<Integer> v3 = new VertexImpl<Integer>(3);
		Vertex<Integer> v4 = new VertexImpl<Integer>(4);

		Edge<Integer> e1 = new EdgeImpl<Integer>(1, v1, v2, false);
		Edge<Integer> e2 = new EdgeImpl<Integer>(2, v1, v4, false);
		Edge<Integer> e3 = new EdgeImpl<Integer>(3, v1, v3, false);
		Edge<Integer> e4 = new EdgeImpl<Integer>(4, v3, v4, false);

		Graph<Integer> graph = GraphImpl.createGraph(Integer.class);
		graph.addVertex(v1);
		graph.addVertex(v2);
		graph.addVertex(v3);
		graph.addVertex(v4);
		graph.addEdge(e1);
		graph.addEdge(e2);
		graph.addEdge(e3);
		graph.addEdge(e4);

		Byte[][] expected = {
				{1,1,1,0},
				{1,0,0,0},
				{0,0,1,1},
				{0,1,0,1}
		};

		Byte[][] result = GraphUtils.getIncidenceMatrix(graph);

		for (int i = 0; i < expected.length; i++)
		{
			for (int j = 0; j < expected[i].length; j++)
			{
				assertEquals(expected[i][j],result[i][j]);
			}
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void getIncidenceMatrixTestIllegalArgumentException () throws Exception
	{
		GraphUtils.getIncidenceMatrix(null);
	}

}