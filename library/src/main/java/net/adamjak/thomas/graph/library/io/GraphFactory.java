package net.adamjak.thomas.graph.library.io;

import net.adamjak.thomas.graph.library.api.Edge;
import net.adamjak.thomas.graph.library.api.Graph;
import net.adamjak.thomas.graph.library.api.Vertex;
import net.adamjak.thomas.graph.library.classes.EdgeImpl;
import net.adamjak.thomas.graph.library.classes.GraphImpl;
import net.adamjak.thomas.graph.library.classes.VertexImpl;
import net.adamjak.thomas.graph.library.io.xsd.Graphml;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Tomas Adamjak on 20.12.2015.
 * Copyright 2015, Tomas Adamjak
 * License: The BSD 3-Clause License
 * <br /><br />
 * Final class whitch create {@link Graph} from various formats.
 *
 * @see GraphSaver
 */
public final class GraphFactory
{
	private GraphFactory ()
	{
		throw new IllegalStateException("Class " + this.getClass().getSimpleName() + " can not be initialized!");
	}

	public static SupportedFormats getFileFormat (File file)
	{
		String[] name = file.getName().split("\\.");
		String extension = name[name.length - 1];
		extension = extension.toLowerCase();
		if (extension.equals("xml") || extension.equals("graphml"))
		{
			return SupportedFormats.GRAPHML;
		}
		else if (extension.equals("g6"))
		{
			return SupportedFormats.GRAPH6;
		}
		else
		{
			return SupportedFormats.BRATISLAVA_TEXT_CATALOG;
		}
	}

	/*******************************************************
	 * XML format - GraphML
	 *******************************************************/

	/**
	 * Create graph from XML in GraphML format
	 *
	 * @param xml File with GraphML xml
	 * @return new Graph
	 */
	public static Graph<Integer> createGraphFromGraphml (File xml)
	{
		try
		{
			JAXBContext jaxbContext = JAXBContext.newInstance(Graphml.class);

			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

			Graphml graphml = (Graphml) jaxbUnmarshaller.unmarshal(xml);

			boolean canCast = canGraphIdCastToInteger(graphml);

			Graph<Integer> graph = GraphImpl.createGraph(Integer.class);

			int vertexId = 0;

			for (Graphml.Graph.Node n : graphml.getGraph().getNode())
			{
				Integer id;
				if (canCast)
				{
					id = Integer.valueOf(n.getId());
				}
				else
				{
					id = vertexId;
				}
				graph.addVertex(new VertexImpl<Integer>(id));
				vertexId++;
			}

			int edgeId = 0;

			for (Graphml.Graph.Edge e : graphml.getGraph().getEdge())
			{

				Vertex<Integer> startVertex = null;
				Vertex<Integer> endVertex = null;

				for (Vertex<Integer> v : graph.getStructure().keySet())
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

				Integer id;
				if (canCast)
				{
					id = Integer.valueOf(e.getId());
				}
				else
				{
					id = vertexId;
				}

				graph.addEdge(new EdgeImpl<Integer>(id, startVertex, endVertex, false));
				edgeId++;
			}

			return graph;
		}
		catch (JAXBException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	private static boolean canGraphIdCastToInteger (Graphml g)
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

	/**
	 * create new instance of Graph from graph6 format.
	 *
	 * @param file File in Graph6 format
	 * @return new Graph
	 * @see <code><a href="http://pallini.di.uniroma1.it/">pallini.di.uniroma1.it</a></code> - graph6 doc
	 */
	public static List<Graph<Integer>> createGraphFromGraph6 (File file) throws IOException
	{
		List<Graph<Integer>> graphList = new LinkedList<Graph<Integer>>();
		BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

		String line;
		while ((line = bufferedReader.readLine()) != null)
		{
			byte[] bytes = line.getBytes();

			int n = 0; // pocet vrcholov
			int start = 0;

			if (bytes[0] < 126)
			{
				n = bytes[0] - 63;
				start = 1;
			}
			else if (bytes[0] == 126 && bytes[1] < 126)
			{
				String strN = byteToBitesString(bytes[1]) + byteToBitesString(bytes[2]) + byteToBitesString(bytes[3]);
				n = Integer.parseInt(strN, 2);
				start = 4;
			}
			else
			{
				String strN = byteToBitesString(bytes[2]) +
						byteToBitesString(bytes[3]) +
						byteToBitesString(bytes[4]) +
						byteToBitesString(bytes[5]) +
						byteToBitesString(bytes[6]) +
						byteToBitesString(bytes[7]);
				n = Integer.parseInt(strN, 2);
				start = 8;
			}

			String mat = "";

			for (int i = start; i < bytes.length; i++)
			{
				mat += byteToBitesString(bytes[i]);
			}

			mat = mat.substring(0, (n * (n - 1) / 2));

			Graph<Integer> g = GraphImpl.createGraph(Integer.class);

			for (int i = 0; i < n; i++)
			{
				g.addVertex(new VertexImpl<Integer>(i));
			}

			int edgeNumber = 0;

			List<Vertex<Integer>> vertexList = g.getListOfVertexes();

			char[] charArray = mat.toCharArray();
			int k = 0;
			for (int i = 1; i < n; i++)
			{
				for (int j = 0; j < i; j++)
				{
					if (charArray[k] == '1')
					{
						g.addEdge(new EdgeImpl<Integer>(edgeNumber, vertexList.get(i), vertexList.get(j), false));
					}
					k++;
				}
			}

			graphList.add(g);
		}

		try
		{
			if (bufferedReader != null)
			{
				bufferedReader.close();
			}
		}
		catch (IOException e)
		{
		}

		return graphList;
	}

	private static String byteToBitesString (int c)
	{
		c = c - 63;
		String s = Integer.toBinaryString(c);

		if (s.length() < 6)
		{
			while (s.length() < 6)
			{
				s = "0" + s;
			}
		}

		return s;
	}

	/*******************************************************
	 * TXT catalog (using in Bratislava)
	 *******************************************************/

	/**
	 * Create graphs from text file
	 *
	 * @param txt (File)
	 * @return List of graphs from text file
	 */
	public static List<Graph<Integer>> createGraphFromTextCatalog (File txt) throws IOException
	{
		List<Graph<Integer>> graphs = new LinkedList<Graph<Integer>>();

		BufferedReader bufferedReader = null;

		int edgeNumber = 0;


		bufferedReader = new BufferedReader(new FileReader(txt));

		String line;

		do
		{
			line = bufferedReader.readLine();
		}
		while (line.trim().charAt(0) == '{' || line.trim().charAt(0) == '#');

		Integer graphCounts = Integer.parseInt(line);

		for (int i = 1; i <= graphCounts; i++)
		{
			do
			{
				line = bufferedReader.readLine();
			}
			while (line.trim().charAt(0) == '{' || line.trim().charAt(0) == '#');

			Integer cisloGrafu = Integer.parseInt(line);

			if (cisloGrafu == i)
			{
				Graph<Integer> g = GraphImpl.createGraph(Integer.class);

				do
				{
					line = bufferedReader.readLine();
				}
				while (line.trim().charAt(0) == '{' || line.trim().charAt(0) == '#');

				Integer pocetVrcholov = Integer.parseInt(line);

				for (int j = 0; j < pocetVrcholov; j++)
				{
					g.addVertex(new VertexImpl<Integer>(j));
				}

				for (int j = 0; j < pocetVrcholov; j++)
				{
					Vertex<Integer> v = g.getVertexByContent(j);

					do
					{
						line = bufferedReader.readLine();
					}
					while (line.trim().charAt(0) == '{' || line.trim().charAt(0) == '#');

					String[] neighbors = line.trim().split(" ");

					for (String s : neighbors)
					{
						Vertex<Integer> neighbor = g.getVertexByContent(Integer.parseInt(s));
						g.addEdge(new EdgeImpl<Integer>(edgeNumber, v, neighbor, false));
						edgeNumber++;
					}
				}

				graphs.add(g);
			}
		}


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


		return graphs;
	}

	/**
	 * Create clone from inserted graph.
	 *
	 * @param graph Instance of graph
	 * @return new Instance os graph
	 */
	public static <U extends Comparable> Graph<U> cloneGraph (Graph<U> graph)
	{
		Graph<U> newGraph = GraphImpl.createGraph(graph.getGraphType());
		newGraph.setDirected(graph.isDirected());

		for (Vertex<U> v : graph.getStructure().keySet())
		{
			newGraph.addVertex(new VertexImpl<U>(v.getContent()));
		}

		for (Vertex<U> v : newGraph.getStructure().keySet())
		{
			for (Edge<U> e : graph.getStructure().get(v))
			{
				newGraph.addEdge(new EdgeImpl<U>(e.getContent(), e.getStart(), e.getEnd(), e.isDirected()));
			}
		}

		return newGraph;
	}

	public static <U extends Comparable> Graph<U> permuteGraph (Graph<U> graph)
	{
		Graph<U> newGraph = GraphFactory.cloneGraph(graph);
		// TODO: 8.1.2016 - vyspekulovat prepermutovanie hrana a vrcholov 
		return newGraph;
	}
}
