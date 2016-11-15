package net.adamjak.thomas.graph.library.io;

import net.adamjak.thomas.graph.library.api.Edge;
import net.adamjak.thomas.graph.library.api.Graph;
import net.adamjak.thomas.graph.library.api.Vertex;
import net.adamjak.thomas.graph.library.io.xsd.Graphml;
import net.adamjak.thomas.graph.library.io.xsd.ObjectFactory;

import javax.validation.constraints.NotNull;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Created by Tomas Adamjak on 13.11.2016.
 * Copyright 2016, Tomas Adamjak
 * License: The BSD 3-Clause License
 * <br /><br />
 * Final class whitch save {@link Graph} into various formats.
 *
 * @see GraphFactory
 */
public final class GraphSaver
{
	private GraphSaver ()
	{
		throw new IllegalStateException("Class " + this.getClass().getSimpleName() + " can not be initialized!");
	}

	// TODO: 13.11.2016 -- vytvorit zapisovace

	/**
	 * Save {@link Graph}&lt;T&gt; into GraphMl xml.
	 *
	 * @param graphToSave graph whitch you want save
	 * @param fileToSave  file to which the program will save the graph
	 * @param <T>         Must implements {@link Comparable}
	 * @throws GraphInputOutputException when creating file error occurred
	 * @throws IllegalArgumentException  if some param is {@code null}
	 */
	public static <T extends Comparable> void graphToGraphMl (@NotNull Graph<T> graphToSave, @NotNull File fileToSave) throws GraphInputOutputException, IllegalArgumentException
	{
		if (graphToSave == null || fileToSave == null) throw new IllegalArgumentException("Params can not by null!");

		ObjectFactory objectFactory = new ObjectFactory();
		Graphml.Graph graph = objectFactory.createGraphmlGraph();
		graph.setId("G");

		List<Vertex<T>> vertexList = graphToSave.getListOfVertexes();

		for (int i = 0; i < vertexList.size(); i++)
		{
			Graphml.Graph.Node node = objectFactory.createGraphmlGraphNode();
			node.setId(String.valueOf(i));
			node.setValue(vertexList.get(i).getContent().toString());

			graph.getNode().add(node);
		}

		List<Edge<T>> edgeList = graphToSave.getListOfEdges();

		for (int i = 0; i < edgeList.size(); i++)
		{
			Graphml.Graph.Edge edge = objectFactory.createGraphmlGraphEdge();
			edge.setId(String.valueOf(i));
			edge.setValue(edgeList.get(i).getContent().toString());
			edge.setSource(String.valueOf(vertexList.indexOf(edgeList.get(i).getStart())));
			edge.setTarget(String.valueOf(vertexList.indexOf(edgeList.get(i).getEnd())));

			graph.getEdge().add(edge);
		}

		Graphml graphml = objectFactory.createGraphml();
		graphml.setGraph(graph);

		try
		{
			JAXBContext jaxbContext = JAXBContext.newInstance(Graphml.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			jaxbMarshaller.marshal(graphml, fileToSave);
		}
		catch (JAXBException e)
		{
			throw new GraphInputOutputException(e.getMessage());
		}
	}

	/**
	 * Save {@link Graph}&lt;T&gt; into text catalog using in Bratislava.
	 *
	 * @param graphsToSave graph whitch you want save
	 * @param fileToSave   file to which the program will save the graph
	 * @param <T>          Must implements {@link Comparable}
	 * @throws GraphInputOutputException when creating file error occurred
	 * @throws IllegalArgumentException  if some param is {@code null}
	 */
	public static <T extends Comparable> void graphsTotextCatalog (@NotNull List<Graph<T>> graphsToSave, @NotNull File fileToSave) throws GraphInputOutputException, IllegalArgumentException
	{
		if (graphsToSave == null || fileToSave == null) throw new IllegalArgumentException("Params can not by null!");

		BufferedWriter bufferedWriter = null;

		try
		{
			if (!fileToSave.exists())
			{
				fileToSave.createNewFile();
			}

			bufferedWriter = new BufferedWriter(new FileWriter(fileToSave.getAbsoluteFile()));
			bufferedWriter.write(String.valueOf(graphsToSave.size()));
			bufferedWriter.newLine();

			for (int i = 0; i < graphsToSave.size(); i++)
			{
				Graph<T> g = graphsToSave.get(i);

				bufferedWriter.write(String.valueOf(i + 1));
				bufferedWriter.newLine();

				bufferedWriter.write(String.valueOf(g.getCountOfVertexes()));
				bufferedWriter.newLine();

				List<Vertex<T>> vertexList = g.getListOfVertexes();

				for (Vertex<T> v : vertexList)
				{
					List<Edge<T>> edgeList = g.getEdgesContainsVertex(v);

					String line = "";

					for (Edge<T> e : edgeList)
					{
						Vertex<T> neighbor;
						if (e.getStart().equals(v))
						{
							neighbor = e.getEnd();
						}
						else
						{
							neighbor = e.getStart();
						}

						line += vertexList.indexOf(neighbor) + " ";
					}

					bufferedWriter.write(line.trim());
					bufferedWriter.newLine();
				}
			}

		}
		catch (IOException e)
		{
			throw new GraphInputOutputException(e.getMessage());
		}
		finally
		{
			try
			{
				if (bufferedWriter != null)
				{
					bufferedWriter.close();
				}
			}
			catch (IOException e) {}
		}
	}

}
