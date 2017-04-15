package net.adamjak.thomas.graph.library.tests.snarks;

import net.adamjak.thomas.graph.library.api.Graph;
import net.adamjak.thomas.graph.library.classes.Cycle;
import net.adamjak.thomas.graph.library.interfaces.anot.Benchmarked;
import net.adamjak.thomas.graph.library.tests.GraphTest;
import net.adamjak.thomas.graph.library.tests.SnarkTestResult;

import java.util.concurrent.ConcurrentSkipListSet;

/**
 * Created by Tomas Adamjak on 3.1.2016.
 * Copyright 2016, Tomas Adamjak
 * License: The BSD 3-Clause License
 */
@Benchmarked
public class OneFactor<T extends Comparable> extends GraphTest<T>
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

		result = this.calculate(this.graph);

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

	private boolean calculate (Graph<T> graph)
	{
		ConcurrentSkipListSet<Cycle<T>> cycleConcurrentSkipListSet = graph.getListOfAllCyrcles();

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
						return false;
					}
				}
			}
		}

		return true;
	}
}
