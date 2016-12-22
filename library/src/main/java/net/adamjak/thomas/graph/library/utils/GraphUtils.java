package net.adamjak.thomas.graph.library.utils;

import net.adamjak.thomas.graph.library.api.Edge;
import net.adamjak.thomas.graph.library.api.Graph;
import net.adamjak.thomas.graph.library.api.Vertex;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Tomas Adamjak on 22.12.2016.
 * Copyright 2016, Tomas Adamjak
 * License: The BSD 3-Clause License
 */
public class GraphUtils
{
	/**
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
	 * Check if 2 graphs are isomorphous.
	 *
	 * @param g1  first graph
	 * @param g2  second graph
	 * @param <T> type of graphs
	 * @return <code>true</code> - if graphs are icomorphous<br /><code>false</code> - anytime else
	 */
	public static <T extends Comparable> boolean checkIsomorphism (Graph<T> g1, Graph<T> g2)
	{
		if (g1 == null || g2 == null) throw new IllegalArgumentException("Params g1 or g2 can not be null.");

		if (g1.getCountOfVertexes() != g2.getCountOfVertexes()) return false;
		if (g1.getListOfEdges().size() != g2.getListOfEdges().size()) return false;

		for (Vertex<T> v1 : g1.getListOfVertexes())
		{
			boolean haveTwin = false;

			for (Vertex<T> v2 : g2.getListOfVertexes())
			{
				if (g1.getNeighborVertexes(v1).equals(g2.getNeighborVertexes(v2)))
				{
					haveTwin = true;
				}
			}

			if (haveTwin == false)
			{
				return false;
			}
		}

		return true;
	}
}
