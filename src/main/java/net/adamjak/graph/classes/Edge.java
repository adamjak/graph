package net.adamjak.graph.classes;

/**
 * Created by Tomas Adamjak on 16.12.2015.
 * Copyright 2015, Tomas Adamjak
 * License: The BSD 3-Clause License
 */
public class Edge<T extends Comparable> implements Comparable<Edge<T>>
{
	private Vertex<T> start;
	private Vertex<T> end;
	private T content;
	private boolean directed = false;
	private Integer color;

	public Edge (T content, Vertex<T> start, Vertex<T> end, boolean directed)
	{
		this.content = content;
		this.start = start;
		this.end = end;
		this.directed = directed;
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

	public void setDirected (boolean directed)
	{
		this.directed = directed;
	}

	public boolean isDirected ()
	{
		return this.directed;
	}

	public void setColor (Integer color)
	{
		this.color = color;
	}

	public Integer getColor ()
	{
		return this.color;
	}

	@Override
	public boolean equals (Object obj)
	{
		if (obj == null) return false;
		if (!(obj instanceof Edge<?>)) return false;

		Edge<T> other;
		try
		{
			other = (Edge<T>) obj;
		}
		catch (Exception e)
		{
			return false;
		}

		if (directed)
		{
			if (this.start.equals(other.getStart()) && this.end.equals(other.getEnd()))
			{
				return true;
			}
		}
		else
		{
			if ((this.start.equals(other.getStart()) && this.end.equals(other.getEnd())) || (this.start.equals(other.getEnd()) && this.end.equals(other.getStart())))
			{
				return true;
			}
		}

		return false;
	}

	@Override
	public String toString ()
	{
		String str = "Edge from: " + this.start + " to: " + this.end;

		if (color != null)
		{
			str += " Color: " + this.color;
		}

		return str;
	}

	@Override
	public int compareTo (Edge<T> e)
	{
		// FIXME: 18.1.2016 
		return this.content.compareTo(e.getContent());
	}
}
