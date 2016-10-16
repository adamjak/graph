package net.adamjak.graph.cubic.snarks.tests;

import net.adamjak.graph.api.Graph;
import net.adamjak.graph.classes.Cycle;
import net.adamjak.graph.cubic.snarks.SnarkTest;
import net.adamjak.graph.cubic.snarks.SnarkTestResult;
import net.adamjak.graph.interfaces.anot.Benchmarked;

import java.util.concurrent.ConcurrentSkipListSet;

/**
 * Created by Tomas Adamjak on 3.1.2016.
 * Copyright 2016, Tomas Adamjak
 * License: The BSD 3-Clause License
 */
@Benchmarked
public class OneFactor<T extends Comparable> extends SnarkTest<T>
{
	public OneFactor ()
	{}

	@Override
	public void init (Graph<T> graph)
	{
		this.graph = graph;
	}

	@Override
	public SnarkTestResult call()
	{
		SnarkTestResult snarkTestResult = new SnarkTestResult(this.getClass());

		boolean result;

		long startTime = System.nanoTime();

		result = this.calculate(this.graph.getListOfAllCyrcles());

		long endTime = System.nanoTime();

		snarkTestResult.setSnark(result);

		snarkTestResult.setTime(endTime - startTime);

		return snarkTestResult;
	}

	/**
	 * The main computation performed by this task.
	 *
	 * @return the result of the computation
	 */
	@Override
	protected SnarkTestResult compute ()
	{
		return this.call();
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
						return false;
					}
				}
			}
		}

		return true;
	}
}
