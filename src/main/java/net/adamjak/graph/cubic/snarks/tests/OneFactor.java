package net.adamjak.graph.cubic.snarks.tests;

import net.adamjak.graph.classes.Cyrcle;
import net.adamjak.graph.classes.Graph;
import net.adamjak.graph.classes.Vertex;
import net.adamjak.graph.cubic.snarks.SnarkTestResult;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * Created by Tomas Adamjak on 3.1.2016.
 * Copyright 2016, Tomas Adamjak
 * License: The BSD 3-Clause License
 */
public class OneFactor<T extends Comparable> implements Callable<SnarkTestResult>
{
	private Graph<T> graph;

	public OneFactor (Graph<T> graph)
	{
		this.graph = graph;
	}

	@Override
	public SnarkTestResult call() throws Exception
	{
		SnarkTestResult snarkTestResult = new SnarkTestResult();

		boolean result;

		long startTime = System.nanoTime();

		result = this.calculate(this.graph.getListOfAllCyrcles());

		long endTime = System.nanoTime();

		snarkTestResult.setSnark(result);

		snarkTestResult.setTime(endTime - startTime);

		return snarkTestResult;
	}

	private boolean calculate(ConcurrentSkipListSet<Cyrcle<T>> cyrcleConcurrentSkipListSet)
	{
		for (Cyrcle<T> firstCyrcle : cyrcleConcurrentSkipListSet)
		{
			for (Cyrcle<T> secondCyrcle : cyrcleConcurrentSkipListSet)
			{
				if ((firstCyrcle.equals(secondCyrcle)) == false &&
						(firstCyrcle.getCyrcleSize() + secondCyrcle.getCyrcleSize()) == graph.getCountOfVertexes() &&
						(firstCyrcle.getCyrcleSize() % 2 == 0) &&
						(secondCyrcle.getCyrcleSize() % 2 == 0))
				{
					if (firstCyrcle.hasCommonVertex(secondCyrcle) == false)
					{
						// TODO: 30.3.2016 -- najst perfektny matching
						return true;
					}
				}
			}
		}

		return false;
	}
}
