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
	private T content;

	public Edge (T content, Vertex<T> start, Vertex<T> end)
	{
		this.content = content;
		this.start = start;
		this.end = end;
	}

	public T getContent ()
	{
		return this.content;
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
