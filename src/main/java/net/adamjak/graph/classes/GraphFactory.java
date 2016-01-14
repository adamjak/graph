package net.adamjak.graph.classes;

import net.adamjak.graph.io.xsd.Graphml;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

	/**
	 * Create graph from XML in GraphML format
	 * @param xml File with GraphML xml
	 * @return new Graph
	 */
	public static Graph<String> createGraphFromGraphml(File xml)
	{
		try
		{
			JAXBContext jaxbContext = JAXBContext.newInstance(Graphml.class);

			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

			Graphml graphml = (Graphml) jaxbUnmarshaller.unmarshal(xml);

			Graph<String> graph = Graph.createGraph(String.class);

			for (Graphml.Graph.Node n : graphml.getGraph().getNode())
			{
				graph.addVertex(new Vertex<String>(n.getId()));
			}

			for (Graphml.Graph.Edge e : graphml.getGraph().getEdge())
			{

				Vertex<String> startVertex = null;
				Vertex<String> endVertex = null;

				for (Vertex<String> v : graph.getStructure().keySet())
				{
					if (v.getContent().equals(e.getSource()))
					{
						startVertex = v;
					}

					if (v.getContent().equals(e.getTarget()))
					{
						endVertex = v;
					}
				}

				graph.addEdge(new Edge<String>(e.getId(),startVertex,endVertex,false));

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
	// TODO: 24.12.2015 - zistit podrobnosti o formate g6 a implementovat citacku 
//	public static Graph createGraphFromG6(File file)
//	{
//		Path p = FileSystems.getDefault().getPath("", file.getPath());
//		try
//		{
//			byte[] fileData = Files.readAllBytes(p);
//			for (byte b : fileData)
//			{
//				System.out.println(b);
//			}
//		}
//		catch (IOException e)
//		{
//			e.printStackTrace();
//		}
//
//		return null;
//	}

	/*******************************************************
	 * TXT catalog (using in Bratislava)
	 *******************************************************/

	/**
	 * Create graphs from text file
	 * @param txt (File)
	 * @return List of graphs from text file
	 */
	public static List<Graph<Integer>> createGraphFromTextCatalog(File txt)
	{
		List<Graph<Integer>> graphs = new ArrayList<Graph<Integer>>();

		BufferedReader bufferedReader = null;

		int edgeNumber = 0;

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
							g.addEdge(new Edge<Integer>(edgeNumber,v,neighbor,false));
							edgeNumber++;
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

	/**
	 * Create clone from inserted graph.
	 * @param graph Instance of graph
	 * @return new Instance os graph
	 */
	public static <U extends Comparable> Graph<U> cloneGraph(Graph<U> graph)
	{
		Graph<U> newGraph = Graph.createGraph(graph.getGraphType());
		newGraph.setDirected(graph.isDirected());

		for (Vertex<U> v : graph.getStructure().keySet())
		{
			newGraph.addVertex(new Vertex<U>(v.getContent()));
		}

		for (Vertex<U> v : newGraph.getStructure().keySet())
		{
			for (Edge<U> e : graph.getStructure().get(v))
			{
				newGraph.addEdge(new Edge<U>(e.getContent(),e.getStart(),e.getEnd(),e.isDirected()));
			}
		}

		return newGraph;
	}

	public static <U extends Comparable> Graph<U> permuteGraph(Graph<U> graph)
	{
		Graph<U> newGraph = GraphFactory.cloneGraph(graph);
		// TODO: 8.1.2016 - vyspekulovat prepermutovanie hrana a vrcholov 
		return newGraph;
	}
}
