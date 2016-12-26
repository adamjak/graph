package net.adamjak.thomas.graph.library.utils;

import net.adamjak.thomas.graph.library.api.Edge;
import net.adamjak.thomas.graph.library.api.Graph;
import net.adamjak.thomas.graph.library.api.Vertex;
import net.adamjak.thomas.graph.library.classes.EdgeImpl;
import net.adamjak.thomas.graph.library.classes.GraphImpl;
import net.adamjak.thomas.graph.library.classes.VertexImpl;

/**
 * Created by Tomas Adamjak on 24.12.2016.
 * Copyright 2016, Tomas Adamjak
 * License: The BSD 3-Clause License
 */
public class InterestingGraphs
{
	/**
	 * @return Return Petersen graph.
	 * @see <a href="https://en.wikipedia.org/wiki/Petersen_graph">en.wikipedia.org/wiki/Petersen_graph</a>
	 */
	public static Graph<Integer> getPetersenGraph ()
	{
		Vertex<Integer> v1 = new VertexImpl<Integer>(1);
		Vertex<Integer> v2 = new VertexImpl<Integer>(2);
		Vertex<Integer> v3 = new VertexImpl<Integer>(3);
		Vertex<Integer> v4 = new VertexImpl<Integer>(4);
		Vertex<Integer> v5 = new VertexImpl<Integer>(5);
		Vertex<Integer> v6 = new VertexImpl<Integer>(6);
		Vertex<Integer> v7 = new VertexImpl<Integer>(7);
		Vertex<Integer> v8 = new VertexImpl<Integer>(8);
		Vertex<Integer> v9 = new VertexImpl<Integer>(9);
		Vertex<Integer> v10 = new VertexImpl<Integer>(10);

		Edge<Integer> e1 = new EdgeImpl<Integer>(1, v1, v2, false);
		Edge<Integer> e2 = new EdgeImpl<Integer>(2, v2, v3, false);
		Edge<Integer> e3 = new EdgeImpl<Integer>(3, v3, v4, false);
		Edge<Integer> e4 = new EdgeImpl<Integer>(4, v4, v5, false);
		Edge<Integer> e5 = new EdgeImpl<Integer>(5, v5, v1, false);
		Edge<Integer> e6 = new EdgeImpl<Integer>(6, v1, v6, false);
		Edge<Integer> e7 = new EdgeImpl<Integer>(7, v2, v7, false);
		Edge<Integer> e8 = new EdgeImpl<Integer>(8, v3, v8, false);
		Edge<Integer> e9 = new EdgeImpl<Integer>(9, v4, v9, false);
		Edge<Integer> e10 = new EdgeImpl<Integer>(10, v5, v10, false);
		Edge<Integer> e11 = new EdgeImpl<Integer>(11, v6, v8, false);
		Edge<Integer> e12 = new EdgeImpl<Integer>(12, v8, v10, false);
		Edge<Integer> e13 = new EdgeImpl<Integer>(13, v10, v7, false);
		Edge<Integer> e14 = new EdgeImpl<Integer>(14, v7, v9, false);
		Edge<Integer> e15 = new EdgeImpl<Integer>(15, v9, v6, false);

		Graph<Integer> graph = GraphImpl.createGraph(Integer.class);

		graph.addVertex(v1);
		graph.addVertex(v2);
		graph.addVertex(v3);
		graph.addVertex(v4);
		graph.addVertex(v5);
		graph.addVertex(v6);
		graph.addVertex(v7);
		graph.addVertex(v8);
		graph.addVertex(v9);
		graph.addVertex(v10);

		graph.addEdge(e1);
		graph.addEdge(e2);
		graph.addEdge(e3);
		graph.addEdge(e4);
		graph.addEdge(e5);
		graph.addEdge(e6);
		graph.addEdge(e7);
		graph.addEdge(e8);
		graph.addEdge(e9);
		graph.addEdge(e10);
		graph.addEdge(e11);
		graph.addEdge(e12);
		graph.addEdge(e13);
		graph.addEdge(e14);
		graph.addEdge(e15);

		return graph;
	}
}
