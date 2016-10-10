package net.adamjak.graph.cubic.snarks;

import net.adamjak.graph.api.Graph;

import java.util.concurrent.Callable;

/**
 * Created by Tomas Adamjak on 9.10.2016.
 * Copyright 2016, Tomas Adamjak
 * License: The BSD 3-Clause License
 */
public interface SnarkTest<T extends Comparable> extends Callable<SnarkTestResult>
{
	void init (Graph<T> graph);
}
