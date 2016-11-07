package net.adamjak.thomas.graph.classes;

import net.adamjak.thomas.graph.api.Vertex;
import net.adamjak.thomas.graph.interfaces.Visitable;

/**
 * Created by Tomas Adamjak on 16.12.2015.
 * Copyright 2015, Tomas Adamjak
 * License: The BSD 3-Clause License
 */
public class VertexImpl<T extends Comparable> implements Visitable, Vertex<T>
{
	private T content;
	private boolean visited = false;

	public VertexImpl (T content)
	{
		this.content = content;
	}

	@Override
	public T getContent ()
	{
		return this.content;
	}

	@Override
	public String toString ()
	{
		return "VertexImpl: " + this.content.toString();
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

	@Override
	public boolean isVisited ()
	{
		return this.visited;
	}

	@Override
	public void setVisited (boolean visited)
	{
		this.visited = visited;
	}
}
