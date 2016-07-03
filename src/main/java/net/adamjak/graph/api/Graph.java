package net.adamjak.graph.api;

import net.adamjak.graph.classes.Cycle;

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
public interface Graph<T extends Comparable>
{
	void addVertex (Vertex<T> vertex);

	Vertex<T> getVertexByContent (T content);

	int getCountOfVertexes ();

	List<Vertex<T>> getListOfVertexes ();

	void addEdge (Edge<T> edge);

	Class<? extends T> getGraphType ();

	Map<Vertex<T>, List<Edge<T>>> getStructure ();

	List<Edge<T>> getSpanningTree ();

	void setDirected (boolean directed);

	boolean isDirected ();

	List<Edge<T>> getListOfEdges ();

	Set<Vertex<T>> getNeighborVertexes (Vertex<T> vertex);

	ConcurrentMap<Edge<T>,List<Edge<T>>> getMapOfNeighborEdges();

	ConcurrentSkipListSet<Cycle<T>> getListOfAllCyrcles();
}
