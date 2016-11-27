package net.adamjak.thomas.graph.library.tests.snarks;

import net.adamjak.thomas.graph.library.classes.GraphImpl;
import net.adamjak.thomas.graph.library.tests.SnarkTestResult;

import java.util.concurrent.Callable;

/**
 * Created by Tomas Adamjak on 25.3.2016.
 * Copyright 2016, Tomas Adamjak
 * License: The BSD 3-Clause License
 */
public class SpanningTree<T extends Comparable> implements Callable<SnarkTestResult>
{
	private GraphImpl<T> graph;

	public SpanningTree (GraphImpl<T> graph)
	{
		this.graph = graph;
	}

	@Override
	public SnarkTestResult call () throws Exception
	{
		// TODO: 25.3.2016 -- porozumiet kostrovemu algoritmu

		return null;
	}
}
