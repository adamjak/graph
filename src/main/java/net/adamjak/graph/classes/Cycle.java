package net.adamjak.graph.classes;

import java.util.concurrent.ConcurrentSkipListSet;

/**
 * Created by Tomas Adamjak on 25.3.2016.
 * Copyright 2016, Tomas Adamjak
 * License: The BSD 3-Clause License
 */
public class Cycle<T extends Comparable> implements Comparable<Cycle<T>>
{
	ConcurrentSkipListSet<Vertex<T>> cyrcleElements = new ConcurrentSkipListSet<Vertex<T>>();

	public Cycle<T> addVertexIntoCyrcle(Vertex<T> vertex)
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

	public boolean hasCommonVertex (Cycle<T> cycle)
	{
		for (Vertex<T> v : cycle.getCyrcleElements())
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
		if (!(obj instanceof Cycle<?>)) return false;

		Cycle<T> other;

		try
		{
			other = (Cycle<T>) obj;
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
	public int compareTo (Cycle<T> cycle)
	{
		if (this.equals(cycle)) return 0;

		if (this.cyrcleElements.size() < cycle.getCyrcleElements().size())
		{
			return -1;
		}
		else
		{
			return 1;
		}
	}
}
