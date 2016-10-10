package net.adamjak.graph.cubic.snarks.tests;


import net.adamjak.graph.api.Edge;
import net.adamjak.graph.api.Graph;
import net.adamjak.graph.cubic.snarks.SnarkTestResult;
import net.adamjak.graph.cubic.snarks.SnarkTest;
import net.adamjak.graph.interfaces.anot.Benchmarked;

import java.util.List;
import java.util.Map;

/**
 * Created by Tomas Adamjak on 5.1.2016.
 * Copyright 2016, Tomas Adamjak
 * License: The BSD 3-Clause License
 */
@Benchmarked
public class EdgeBackTrace<T extends Comparable> implements SnarkTest<T>
{
	private Graph<T> graph;

	public EdgeBackTrace () {}

	public void init (Graph<T> graph)
	{
		this.graph = graph;
	}

	@Override
	public SnarkTestResult call () throws Exception
	{
		SnarkTestResult snarkTestResult = new SnarkTestResult();

		List<Edge<T>> edgesList = this.graph.getListOfEdges();
		Map<Edge<T>, List<Edge<T>>> neighborEdges =  this.graph.getMapOfNeighborEdges();

		boolean result;

		long startTime = System.nanoTime();

		result = this.calculate(edgesList, neighborEdges); // calculate routine

		long endTime = System.nanoTime();

		snarkTestResult.setSnark(result);

		snarkTestResult.setTime(endTime - startTime);

		return snarkTestResult;
	}

	private boolean calculate (List<Edge<T>> edgesList, Map<Edge<T>, List<Edge<T>>> neighborEdges)
	{
		for (Edge<T> e : edgesList)
		{
			e.setColor(0); // 1. step
		}

		int i = 0;
		while (true)
		{
			Edge<T> edge = edgesList.get(i);

			edge.setColor(edge.getColor() + 1); // 2. step

			if (edge.getColor().equals(4))
			{
				edge.setColor(0); // 3. step

				if (i == 0)
				{
					return true; // 8. step
				}
				else
				{
					i--; // 4. step
				}
			}
			else
			{
				boolean diferentColor = false;

				for (Edge<T> e : neighborEdges.get(edge)) // 5. step
				{
					if (e.getColor().equals(edge.getColor()))
					{
						diferentColor = true;
					}
				}

				if (diferentColor == false)
				{
					i++; // 6. step

					if (i == edgesList.size())
					{
						return false; // 7. step
					}
				}
			}
		}
	}
}
