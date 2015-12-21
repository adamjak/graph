package net.adamjak.graph.classes;

/**
 * Created by Tomas Adamjak on 16.12.2015.
 * Copyright 2015, Tomas Adamjak
 * License: The BSD 3-Clause License
 */
public class Edge<T extends Comparable>
{
	private Vertex<T> start;
	private Vertex<T> end;

	public Edge (Vertex<T> start, Vertex<T> end)
	{
		this.start = start;
		this.end = end;
	}

	public Vertex<T> getStart ()
	{
		return this.start;
	}

	public Vertex<T> getEnd ()
	{
		return this.end;
	}

	@Override
	public String toString ()
	{
		return "Edge from: " + this.start + " to: " + this.end;
	}
}
