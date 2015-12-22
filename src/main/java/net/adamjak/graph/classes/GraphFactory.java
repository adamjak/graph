package net.adamjak.graph.classes;

import net.adamjak.graph.io.xsd.Graphml;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tomas Adamjak on 20.12.2015.
 * Copyright 2015, Tomas Adamjak
 * License: The BSD 3-Clause License
 */
public class GraphFactory
{
	/*******************************************************
	 * XML format - GraphML
	 *******************************************************/

	public static Graph<? extends Comparable> createGraphFromGraphml(File xml)
	{
		try
		{
			JAXBContext jaxbContext = JAXBContext.newInstance(Graphml.class);

			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

			Graphml graphml = (Graphml) jaxbUnmarshaller.unmarshal(xml);

			Graph graph;
			if (GraphFactory.canGraphIdCastToInteger(graphml))
			{
				graph = Graph.createGraph(Integer.class);
			}
			else
			{
				graph = Graph.createGraph(String.class);
			}
			
			for (Graphml.Graph.Node n : graphml.getGraph().getNode())
			{
				if (graph.getGraphType() == Integer.class)
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

				if (graph.getGraphType() == Integer.class)
				{
					Vertex<Integer> startVertex;
					Vertex<Integer> endVertex;

					// FIXME: 22.12.2015 - citanie keySetu
				}
				else
				{

				}
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

	/*******************************************************
	 * Graph6 format
	 *******************************************************/
	// TODO: 22.12.2015 - vytvorit citacku na format Graph6

	/*******************************************************
	 * TXT catalog (using in Bratislava)
	 *******************************************************/

	public static List<Graph<Integer>> createGraphFromTextCatalog(File txt)
	{
		List<Graph<Integer>> graphs = new ArrayList<Graph<Integer>>();

		BufferedReader bufferedReader = null;

		try
		{
			bufferedReader = new BufferedReader(new FileReader(txt));

			String line;

			do
			{
				line = bufferedReader.readLine();
			}
			while (line.trim().charAt(0) == '{');

			Integer graphCounts = Integer.parseInt(line);

			for (int i = 1; i <= graphCounts; i++)
			{
				do
				{
					line = bufferedReader.readLine();
				}
				while (line.trim().charAt(0) == '{');

				Integer cisloGrafu = Integer.parseInt(line);

				if (cisloGrafu == i)
				{
					Graph<Integer> g = Graph.createGraph(Integer.class);

					do
					{
						line = bufferedReader.readLine();
					}
					while (line.trim().charAt(0) == '{');

					Integer pocetVrcholov = Integer.parseInt(line);

					for (int j = 0; j < pocetVrcholov; j++)
					{
						g.addVertex(new Vertex<Integer>(j));
					}

					for (int j = 0; j < pocetVrcholov; j++)
					{
						Vertex<Integer> v = g.getVertexByContent(j);

						do
						{
							line = bufferedReader.readLine();
						}
						while (line.trim().charAt(0) == '{');

						String[] neighbors = line.trim().split(" ");

						for (String s : neighbors)
						{
							Vertex<Integer> neighbor = g.getVertexByContent(Integer.parseInt(s));
							g.addEdge(new Edge<Integer>(null,v,neighbor));
						}
					}

					graphs.add(g);
				}
			}

		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
		}
		finally
		{
			try
			{
				if (bufferedReader != null)
				{
					bufferedReader.close();
				}
			}
			catch (IOException ioe)
			{
				ioe.printStackTrace();
			}
		}

		return graphs;
	}
}
