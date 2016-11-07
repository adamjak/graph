package net.adamjak.thomas.graph.ui;

/**
 * Created by Tomas Adamjak on 1.11.2016.
 * Copyright 2016, Tomas Adamjak
 * License: The BSD 3-Clause License
 */
public enum Operations
{
	/**
	 * When you have to test graph with all algorithms and benchmark.
	 */
	BENCHMARK_ALL_ALGORITHMS(1),

	/**
	 * When you have to only test if graph is snark.
	 */
	ONLY_SNARK_TEST(2);

	private int value;

	Operations (int value)
	{
		this.value = value;
	}

	public int getValue ()
	{
		return this.value;
	}
}
