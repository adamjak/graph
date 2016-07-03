package net.adamjak.graph.api;

/**
 * Created by Tomas Adamjak on 3.7.2016.
 * Copyright 2016, Tomas Adamjak
 * License: The BSD 3-Clause License
 */
public interface Edge<T extends Comparable>
{
	T getContent ();

	Vertex<T> getStart ();

	Vertex<T> getEnd ();

	void setDirected (boolean directed);

	boolean isDirected ();

	int compareTo (Edge<T> e);

	void setColor (Integer color);

	Integer getColor();
}
