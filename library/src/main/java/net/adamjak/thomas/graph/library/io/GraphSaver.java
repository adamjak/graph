package net.adamjak.thomas.graph.library.io;

import net.adamjak.thomas.graph.library.api.Edge;
import net.adamjak.thomas.graph.library.api.Graph;
import net.adamjak.thomas.graph.library.api.Vertex;
import net.adamjak.thomas.graph.library.io.xsd.Graphml;
import net.adamjak.thomas.graph.library.io.xsd.ObjectFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.util.List;

/**
 * Created by Tomas Adamjak on 13.11.2016.
 * Copyright 2016, Tomas Adamjak
 * License: The BSD 3-Clause License
 * <br /><br />
 * Final class whitch save {@link Graph} into various formats.
 * @see GraphFactory
 */
public final class GraphSaver
{
	private GraphSaver()
	{
		throw new IllegalStateException("Class " + this.getClass().getSimpleName() + " can not be initialized!");
	}

	// TODO: 13.11.2016 -- vytvorit zapisovace

	/**
	 * Save {@link Graph}&lt;T&gt; into GraphMl xml.
	 * @param graphToSave graph whitch you want save
	 * @param fileToSave files to which the program will save the graph
	 * @param <T> Must implements {@link Comparable}
	 * @throws GraphInputOutputException when creating file error occurred
	 */
	public static <T extends Comparable> void graphToGraphMl (Graph<T> graphToSave, File fileToSave) throws GraphInputOutputException
	{
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

}
