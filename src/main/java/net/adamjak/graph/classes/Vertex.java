package net.adamjak.graph.classes;

import java.util.Comparator;

/**
 * Created by Tomas Adamjak on 16.12.2015.
 * Copyright 2015, Tomas Adamjak
 * License: The BSD 3-Clause License
 */
public class Vertex<T extends Comparable> implements Comparable<T>
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
	public int compareTo (T t)
	{
		return this.content.compareTo(t);
	}
}
