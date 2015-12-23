package net.adamjak.graph.classes;

import java.util.Comparator;

/**
 * Created by Tomas Adamjak on 16.12.2015.
 * Copyright 2015, Tomas Adamjak
 * License: The BSD 3-Clause License
 */
public class Vertex<T extends Comparable> implements Comparable<Vertex<T>>
{
	private T content;

	public Vertex(T content)
	{
		this.content = content;
	}

	public T getContent ()
	{
		return this.content;
	}

	@Override
	public String toString ()
	{
		return "Vertex: " + this.content.toString();
	}

	@Override
	public boolean equals (Object obj)
	{
		if (obj == null) return false;
		if (!(obj instanceof Vertex<?>)) return false;

		Vertex<T> other;

		try
		{
			other = (Vertex<T>) obj;
		}
		catch (Exception e)
		{
			return false;
		}

		return this.content.equals(other.getContent());
	}

	@Override
	public int compareTo (Vertex<T> vertex)
	{
		return this.content.compareTo(vertex.getContent());
	}
}
