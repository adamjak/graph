package net.adamjak.graph.classes;

import net.adamjak.graph.api.Vertex;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Tomas Adamjak on 25.3.2016.
 * Copyright 2016, Tomas Adamjak
 * License: The BSD 3-Clause License
 */
public class Cycle<T extends Comparable> implements Comparable<Cycle<T>>
{
	List<Vertex<T>> cycleElements = new ArrayList<Vertex<T>>();

	public Cycle<T> addVertexIntoCycle(Vertex<T> vertex)
	{
		this.cycleElements.add(vertex);
		return this;
	}

	public List<Vertex<T>> getCycleElements()
	{
		return Collections.unmodifiableList(this.cycleElements);
	}

	public int getCyrcleSize()
	{
		return this.cycleElements.size();
	}

	/**
	 * hasCommonVertex - return true if 2 cycles has common vertex. Using List#contains
	 * @param cycle in which to seek common vertex
	 * @return boolean
	 */
	public boolean hasCommonVertex (Cycle<T> cycle)
	{
		for (Vertex<T> v : cycle.getCycleElements())
		{
			if (this.cycleElements.contains(v)) return true;
		}

		return false;
	}

	@Override
	public String toString ()
	{
		String str = new String();
		
		str += "Cycle:\n";
		for (Vertex<T> v : this.cycleElements)
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

		if (this.cycleElements.size() != other.getCycleElements().size()) return false;

		Vertex<T> firstVertex = this.getCycleElements().get(0);

		int posun = other.getCycleElements().lastIndexOf(firstVertex);

		if (posun < 0) return false;

		if (posun != 0)
		{
			other = this.rotateCycle(other,posun);
		}

		if (this.cycleElements.get(1).equals(other.getCycleElements().get(1)) == false)
		{
			other = this.flip(other);
		}

		for (int i = 0; i < this.cycleElements.size(); i++)
		{
			if (this.cycleElements.get(i).equals(other.getCycleElements().get(i)) == false)
			{
				return false;
			}
		}

		return true;
	}

	@Override
	public int compareTo (Cycle<T> cycle)
	{
		if (this.equals(cycle)) return 0;

		if (this.cycleElements.size() < cycle.getCycleElements().size())
		{
			return -1;
		}
		else
		{
			return 1;
		}
	}

	/**
	 * @param cycle whitch we want rotate
	 * @param times
	 *
	 * @return New rotated cycle.
	 */
	private Cycle<T> rotateCycle (Cycle<T> cycle, int times)
	{
		Cycle<T> newCycle = new Cycle<T>();

		for (int i = times; i < cycle.getCycleElements().size(); i++)
		{
			newCycle.addVertexIntoCycle(cycle.getCycleElements().get(i));
		}

		for (int i = 0; i < times; i++)
		{
			newCycle.addVertexIntoCycle(cycle.getCycleElements().get(i));
		}

		return newCycle;
	}

	/**
	 * @param cycle whitch we want flip
	 *
	 * @return New fliped cycle
	 */
	private Cycle<T> flip (Cycle<T> cycle)
	{
		Cycle<T> newCycle = new Cycle<T>();
		newCycle.addVertexIntoCycle(cycle.getCycleElements().get(0));

		for (int i = cycle.getCyrcleSize() - 1; i > 0; i--)
		{
			newCycle.addVertexIntoCycle(cycle.getCycleElements().get(i));
		}

		return newCycle;
	}
}
