package net.adamjak.graph.classes;

import net.adamjak.graph.io.xsd.Graphml;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;

/**
 * Created by Tomas Adamjak on 20.12.2015.
 * Copyright 2015, Tomas Adamjak
 * License: The BSD 3-Clause License
 */
public class GraphFactory
{
	public static Graph createGraphFromGraphml(File xml)
	{
		try
		{
			JAXBContext jaxbContext = JAXBContext.newInstance(Graphml.class);

			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

			Graphml graphml = (Graphml) jaxbUnmarshaller.unmarshal(xml);

			boolean canCast = GraphFactory.canGraphIdCastToInteger(graphml);

			Graph graph;
			if (canCast)
			{
				graph = new Graph<Integer>();
			}
			else
			{
				graph = new Graph<String>();
			}
			
			for (Graphml.Graph.Node n : graphml.getGraph().getNode())
			{
				if (canCast)
				{
					graph.addVertex(new Vertex<Integer>(Integer.parseInt(n.getId())));
				}
				else
				{
					graph.addVertex(new Vertex<String>(n.getId()));
				}
			}

			for (Graphml.Graph.Edge e : graphml.getGraph().getEdge())
			{

			}

			return graph;
		}
		catch (JAXBException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	private static boolean canGraphIdCastToInteger(Graphml g)
	{
		for (Graphml.Graph.Node n : g.getGraph().getNode())
		{
			try
			{
				Integer.parseInt(n.getId());
			}
			catch (NumberFormatException nfe)
			{
				return false;
			}
		}

		for (Graphml.Graph.Edge e : g.getGraph().getEdge())
		{
			try
			{
				Integer.parseInt(e.getId());
			}
			catch (NumberFormatException nfe)
			{
				return false;
			}
		}

		return true;
	}
}
