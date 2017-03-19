package net.adamjak.thomas.graph.library.tests.snarks;

import net.adamjak.thomas.graph.library.api.Graph;
import net.adamjak.thomas.graph.library.interfaces.anot.Benchmarked;
import net.adamjak.thomas.graph.library.tests.GraphTest;
import net.adamjak.thomas.graph.library.tests.GraphTestResult;
import net.adamjak.thomas.graph.library.tests.SnarkTestResult;
import net.adamjak.thomas.graph.library.utils.GraphUtils;
import net.adamjak.thomas.graph.library.utils.Utils;

/**
 * Created by Tomas Adamjak on 27.2.2017.
 * Copyright 2017, Tomas Adamjak
 * License: The BSD 3-Clause License
 */
@Benchmarked
public class Kowalik<T extends Comparable> extends GraphTest<T>
{
	static
	{
		System.loadLibrary("kowaliknative");
	}

	/**
	 * <p><u>Martix form example.</u></p>
	 * <p>Standard martix:<br/>
	 * <code>0 1 0<br />1 0 1<br />0 1 0</code></p>
	 * <p>Special martix form:<br/>
	 * <code>0 1 0 1 0 1 0 1 0</code></p>
	 *
	 * @param c matrix of neighbor vertexes in one line form
	 * @param i count of vertexes
	 * @return
	 */
	native double[] kowalikNative(int[] c, int i);

	@Override
	public void init (Graph<T> graph)
	{
		this.graph = graph;
	}

	/**
	 * Computes a result, or throws an exception if unable to do so.
	 *
	 * @return computed result
	 */
	@Override
	public GraphTestResult call ()
	{
		int[] matrix = GraphUtils.getSpecialAdjacencyMatrix(this.graph);
		int countOfVertexes = this.graph.getCountOfVertexes();

		GraphTestResult graphTestResult = new SnarkTestResult(this.getClass());

		double[] result;

		long startTime = System.nanoTime();

		result = this.kowalikNative(matrix, countOfVertexes);

		long endTime = System.nanoTime();

		graphTestResult.addValue(GraphTestResult.SNARK_KEY , result[0] == 1.0 ? true : false);

		graphTestResult.addValue(GraphTestResult.TIME_KEY, Utils.msToNs(result[1]));
		graphTestResult.addValue("timeWithJni", endTime - startTime);

		return graphTestResult;
	}

	/**
	 * The main computation performed by this task.
	 *
	 * @return the result of the computation
	 */
	@Override
	protected GraphTestResult compute ()
	{
		return this.call();
	}
}
