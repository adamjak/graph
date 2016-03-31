package net.adamjak.graph.cubic.snarks.tests;

import net.adamjak.graph.classes.Graph;
import net.adamjak.graph.cubic.snarks.SnarkTestResult;

import java.util.concurrent.Callable;

/**
 * Created by Tomas Adamjak on 25.3.2016.
 * Copyright 2016, Tomas Adamjak
 * License: The BSD 3-Clause License
 */
public class SpanningTree<T extends Comparable> implements Callable<SnarkTestResult>
{
	private Graph<T> graph;

	public SpanningTree (Graph<T> graph)
	{
		this.graph = graph;
	}

	@Override
	public SnarkTestResult call () throws Exception
	{
		// TODO: 25.3.2016 -- porozumiet kosytrovemu algoritmu

		return null;
	}
}
