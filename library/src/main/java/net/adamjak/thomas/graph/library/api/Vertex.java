package net.adamjak.thomas.graph.library.api;

/**
 * Created by Tomas Adamjak on 3.7.2016.
 * Copyright 2016, Tomas Adamjak
 * License: The BSD 3-Clause License
 */
public interface Vertex<T extends Comparable> extends Comparable<Vertex<T>>
{
	/**
	 * @return Return content of vertex.
	 */
	T getContent ();

	@Override
	int compareTo (Vertex<T> vertex);
}
