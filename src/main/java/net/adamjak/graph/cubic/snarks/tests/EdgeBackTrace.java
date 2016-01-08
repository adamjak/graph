package net.adamjak.graph.cubic.snarks.tests;


import net.adamjak.graph.classes.Edge;
import net.adamjak.graph.classes.Graph;
import net.adamjak.graph.cubic.snarks.SnarkTestResult;

import java.util.*;
import java.util.concurrent.Callable;

/**
 * Created by Tomas Adamjak on 5.1.2016.
 * Copyright 2016, Tomas Adamjak
 * License: The BSD 3-Clause License
 */
public class EdgeBackTrace<T extends Comparable> implements Callable<SnarkTestResult>
{
	private Graph<T> graph;

	public EdgeBackTrace (Graph<T> graph)
	{
		this.graph = graph;
	}

	@Override
	public SnarkTestResult call () throws Exception
	{
		SnarkTestResult snarkTestResult = new SnarkTestResult();

		List<Edge<T>> edgesList = this.graph.getListOfEdges();
		Map<Edge<T>, List<Edge<T>>> neighborEdges =  this.graph.getMapOfNeighborEdges();

		long startTime = Calendar.getInstance().getTime().getTime();

		for (Edge<T> e : edgesList)
		{
			e.setColor(0); // 1. step
		}

		int i = 0;
		while (i < edgesList.size())
		{
			Edge<T> edge = edgesList.get(i);

			edge.setColor(edge.getColor() + 1); // 2. step

			if (edge.getColor() == 4)
			{
				edge.setColor(0); // 3. step

				if (i == 0)
				{
					snarkTestResult.setSnark(true); // 8. step
					break;
				}
				else
				{
					i--;
				}
			}
			else
			{
				boolean diferentColor = false;

				for (Edge<T> e : neighborEdges.get(edge))
				{
					if (e.getColor() == edge.getColor())
					{
						diferentColor = true;
					}
				}

				if (diferentColor == false)
				{
					i++;

					if (i == edgesList.size())
					{
						snarkTestResult.setSnark(false);
						break;
					}
				}
			}
		}

		long endTime = Calendar.getInstance().getTime().getTime();

		snarkTestResult.setTime(endTime - startTime);

		return snarkTestResult;
	}
}
