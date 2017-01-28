package net.adamjak.thomas.graph.library.tests.snarks;

import net.adamjak.thomas.graph.library.api.Graph;
import net.adamjak.thomas.graph.library.tests.GraphTest;
import net.adamjak.thomas.graph.library.tests.GraphTestResult;
import net.adamjak.thomas.graph.library.tests.SnarkTestResult;

/**
 * Created by Tomas Adamjak on 25.3.2016.
 * Copyright 2016, Tomas Adamjak
 * License: The BSD 3-Clause License
 */
public class SpanningTree<T extends Comparable> extends GraphTest<T>
{
	private Graph<T> graph;

	public SpanningTree ()
	{
	}

	@Override
	public void init (Graph<T> graph)
	{
		this.graph = graph;
	}

	@Override
	public SnarkTestResult call () throws Exception
	{
		// TODO: 25.3.2016 -- porozumiet kostrovemu algoritmu

		return null;
	}

	/**
	 * The main computation performed by this task.
	 *
	 * @return the result of the computation
	 */
	@Override
	protected GraphTestResult compute ()
	{
		return null;
	}
}
