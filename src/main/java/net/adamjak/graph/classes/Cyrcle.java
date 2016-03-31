package net.adamjak.graph.classes;

import java.util.Stack;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * Created by Tomas Adamjak on 25.3.2016.
 * Copyright 2016, Tomas Adamjak
 * License: The BSD 3-Clause License
 */
public class Cyrcle<T extends Comparable> implements Comparable<Cyrcle<T>>
{
	ConcurrentSkipListSet<Vertex<T>> cyrcleElements = new ConcurrentSkipListSet<Vertex<T>>();

	public Cyrcle<T> addVertexIntoCyrcle(Vertex<T> vertex)
	{
		this.cyrcleElements.add(vertex);
		return this;
	}

	public ConcurrentSkipListSet<Vertex<T>> getCyrcleElements()
	{
		return this.cyrcleElements;
	}

	public int getCyrcleSize()
	{
		return this.cyrcleElements.size();
	}

	public boolean hasCommonVertex (Cyrcle<T> cyrcle)
	{
		for (Vertex<T> v : cyrcle.getCyrcleElements())
		{
			if (this.cyrcleElements.contains(v)) return true;
		}

		return false;
	}

	@Override
	public String toString ()
	{
		String str = new String();
		
		str += "Cytcle:\n";
		for (Vertex<T> v : this.cyrcleElements)
		{
			str += v.toString();
		}
		
		return str;
	}

	@Override
	public boolean equals (Object obj)
	{
		if (obj == null) return false;
		if (!(obj instanceof Cyrcle<?>)) return false;

		Cyrcle<T> other;

		try
		{
			other = (Cyrcle<T>) obj;
		}
		catch (Exception e)
		{
			return false;
		}

		if (this.cyrcleElements.size() != other.getCyrcleElements().size()) return false;

		for (Vertex<T> v : this.cyrcleElements)
		{
			if (!other.getCyrcleElements().contains(v)) return false;
		}

		return true;
	}

	@Override
	public int compareTo (Cyrcle<T> cyrcle)
	{
		if (this.equals(cyrcle)) return 0;

		if (this.cyrcleElements.size() < cyrcle.getCyrcleElements().size())
		{
			return -1;
		}
		else
		{
			return 1;
		}
	}
}
