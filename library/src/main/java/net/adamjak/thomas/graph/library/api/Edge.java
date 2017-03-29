package net.adamjak.thomas.graph.library.api;

/**
 * Created by Tomas Adamjak on 3.7.2016.
 * Copyright 2016, Tomas Adamjak
 * License: The BSD 3-Clause License
 */
public interface Edge<T extends Comparable>
{
	/**
	 * @return Return content of Edge.
	 */
	T getContent ();

	/**
	 * @return Return start Eeterx of Edge.
	 */
	Vertex<T> getStart ();

	/**
	 * @return Return end Eeterx of Edge.
	 */
	Vertex<T> getEnd ();

	void setDirected (boolean directed);

	/**
	 * @return Return {@code true} if Edge is dirested else return {@code false}.
	 */
	boolean isDirected ();

	int compareTo (Edge<T> e);

	void setColor (Integer color);

	/**
	 * @return Return Edge color.
	 */
	Integer getColor();
}
