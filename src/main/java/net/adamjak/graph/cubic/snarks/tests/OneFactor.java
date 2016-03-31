package net.adamjak.graph.cubic.snarks.tests;

import net.adamjak.graph.classes.Cycle;
import net.adamjak.graph.classes.Graph;
import net.adamjak.graph.cubic.snarks.SnarkTestResult;

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

	private boolean calculate(ConcurrentSkipListSet<Cycle<T>> cycleConcurrentSkipListSet)
	{
		for (Cycle<T> firstCycle : cycleConcurrentSkipListSet)
		{
			for (Cycle<T> secondCycle : cycleConcurrentSkipListSet)
			{
				if ((firstCycle.equals(secondCycle)) == false &&
						(firstCycle.getCyrcleSize() + secondCycle.getCyrcleSize()) == graph.getCountOfVertexes() &&
						(firstCycle.getCyrcleSize() % 2 == 0) &&
						(secondCycle.getCyrcleSize() % 2 == 0))
				{
					if (firstCycle.hasCommonVertex(secondCycle) == false)
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
