package net.adamjak.thomas.graph.library.tests;

import net.adamjak.thomas.graph.library.api.Graph;

import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RecursiveTask;

/**
 * Created by Tomas Adamjak on 9.10.2016.
 * Copyright 2016, Tomas Adamjak
 * License: The BSD 3-Clause License
 */
public abstract class GraphTest<T extends Comparable> extends RecursiveTask<GraphTestResult> implements Callable<GraphTestResult>
{
	protected Graph<T> graph;

	public abstract void init (Graph<T> graph);

	/**
	 * Waits if necessary for the computation to complete, and then
	 * retrieves its result.
	 *
	 * @return the computed result
	 * @throws CancellationException if the computation was cancelled
	 * @throws ExecutionException if the computation threw an
	 * exception
	 * @throws InterruptedException if the current thread is not a
	 * member of a ForkJoinPool and was interrupted while waiting
	 */
	public final GraphTestResult getResult() throws ExecutionException, InterruptedException
	{
		return super.get();
	}
}
