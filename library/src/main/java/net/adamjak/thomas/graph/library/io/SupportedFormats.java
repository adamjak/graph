package net.adamjak.thomas.graph.library.io;

/**
 * Created by Tomas Adamjak on 20.11.2016.
 * Copyright 2016, Tomas Adamjak
 * License: The BSD 3-Clause License
 */
public enum SupportedFormats
{
	/**
	 * Graph6 format
	 * @see <code><a href="http://pallini.di.uniroma1.it/">pallini.di.uniroma1.it</a></code> - graph6 doc
	 */
	GRAPH6,

	/**
	 * GraphML is an XML-based file format for graphs.
	 * @see <code><a href="http://graphml.graphdrawing.org/">graphml.graphdrawing.org</a></code>
	 */
	GRAPHML,

	/**
	 * Bratislava graph text catalog. It is a simple readable text format for graph saving.
	 */
	BRATISLAVA_TEXT_CATALOG
}
