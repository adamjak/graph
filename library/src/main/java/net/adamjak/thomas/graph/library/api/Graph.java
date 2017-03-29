package net.adamjak.thomas.graph.library.api;

import net.adamjak.thomas.graph.library.classes.Cycle;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * Created by Tomas Adamjak on 3.7.2016.
 * Copyright 2016, Tomas Adamjak
 * License: The BSD 3-Clause License
 */
public interface Graph<T extends Comparable> extends Cloneable
{
	/**
	 * Add new vertex into graph.
	 * @param vertex vertex to add
	 */
	void addVertex (Vertex<T> vertex);

	/**
	 * @param content content of vertex
	 * @return Return {@link Vertex} by inserted content.
	 */
	Vertex<T> getVertexByContent (T content);

	/**
	 * @return Return quantity of vertexes in graph.
	 */
	int getCountOfVertexes ();

	/**
	 * @return Return {@link List} of all vertexes.
	 */
	List<Vertex<T>> getListOfVertexes ();

	void addEdge (Edge<T> edge);

	/**
	 * @return Return type of graph.
	 */
	Class<? extends T> getGraphType ();

	/**
	 * @return Return graph structure in {@link Map}.
	 */
	Map<Vertex<T>, List<Edge<T>>> getStructure ();

	/**
	 * @return Return spanning tree of graph.
	 */
	List<Edge<T>> getSpanningTree ();

	void setDirected (boolean directed);

	boolean isDirected ();

	List<Edge<T>> getListOfEdges ();

	Set<Vertex<T>> getNeighborVertexes (Vertex<T> vertex);

	ConcurrentMap<Edge<T>,List<Edge<T>>> getMapOfNeighborEdges();

	ConcurrentSkipListSet<Cycle<T>> getListOfAllCyrcles();

	/**
	 * @param vertex Vertex by to be searched
	 * @return List of edges whitch start or end with inserted vertex.
	 */
	List<Edge<T>> getEdgesContainsVertex (Vertex<T> vertex);

	/**
	 * @return Return adjacency martix represented with two-dimensional array.
	 */
	Byte[][] getAdjacencyMatrix();

	// TODO: 15.11.2016 -- dorobit javadoc

	/**
	 * @return Return full clone.
	 */
	Graph<T> clone();
}
