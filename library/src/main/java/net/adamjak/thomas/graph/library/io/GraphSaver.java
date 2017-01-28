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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tomas Adamjak on 13.11.2016.
 * Copyright 2016, Tomas Adamjak
 * License: The BSD 3-Clause License
 * <br><br>
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
	 * Save {@link List}&lt;{@link Graph}&lt;T&gt;&gt; into text catalog using in Bratislava.
	 *
	 * @param graphsToSave graph whitch you want save
	 * @param fileToSave   file to which the program will save the graph
	 * @param <T>          Must implements {@link Comparable}
	 * @throws GraphInputOutputException when creating file error occurred
	 * @throws IllegalArgumentException  if some param is {@code null}
	 */
	public static <T extends Comparable> void graphsToTextCatalog (@NotNull List<Graph<T>> graphsToSave, @NotNull File fileToSave) throws GraphInputOutputException, IllegalArgumentException
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

	/**
	 * <p>Save {@link List}&lt;{@link Graph}&lt;T&gt;&gt; into graph6 format.</p>
	 * <p>Graph6 format documentation - <code><a href="http://pallini.di.uniroma1.it/">pallini.di.uniroma1.it</a></code>	</p>
	 *
	 * @param graphsToSave graph whitch you want save
	 * @param fileToSave   file to which the program will save the graph
	 * @param <T>          Must implements {@link Comparable}
	 * @throws GraphInputOutputException when creating file error occurred
	 * @throws IllegalArgumentException  if some param is {@code null}
	 */
	public static <T extends Comparable> void graphsToGraph6Format (@NotNull List<Graph<T>> graphsToSave, @NotNull File fileToSave) throws GraphInputOutputException, IllegalArgumentException
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

			for (Graph<T> g : graphsToSave)
			{
				List<Byte> bytes = new ArrayList<Byte>();

				int n = g.getCountOfVertexes();

				if (n <= 62)
				{
					bytes.add(Integer.valueOf(n + 63).byteValue());
				}
				else if (n >= 63 && n <= 258047)
				{
					bytes.add((byte) 126);
					String s = Integer.toBinaryString(n);
					while (s.length() < 18)
					{
						s = "0" + s;
					}
					bytes.add(binaryStringToBytePlus63(s.substring(0,6)));
					bytes.add(binaryStringToBytePlus63(s.substring(6,12)));
					bytes.add(binaryStringToBytePlus63(s.substring(12,18)));
				}
				else
				{
					bytes.add((byte) 126);
					bytes.add((byte) 126);
					String s = Integer.toBinaryString(n);
					while (s.length() < 36)
					{
						s = "0" + s;
					}
					bytes.add(binaryStringToBytePlus63(s.substring(0,6)));
					bytes.add(binaryStringToBytePlus63(s.substring(6,12)));
					bytes.add(binaryStringToBytePlus63(s.substring(12,18)));
					bytes.add(binaryStringToBytePlus63(s.substring(18,24)));
					bytes.add(binaryStringToBytePlus63(s.substring(24,30)));
					bytes.add(binaryStringToBytePlus63(s.substring(30,36)));
				}

				Byte[][] martix = g.getAdjacencyMatrix();
				String s = new String();
				for (int stlpec = 1; stlpec < martix.length; stlpec++)
				{
					for (int riadok = 0; riadok < stlpec; riadok++)
					{
						s = s + martix[riadok][stlpec];
					}
				}

				while (s.length() % 6 != 0)
				{
					s = s + "0";
				}

				for (int i = 0; i < s.length(); i = i+6)
				{
					bytes.add(binaryStringToBytePlus63(s.substring(i,i+6)));
				}


				bufferedWriter.write(new String(changeByteListToPrimitiveDataTypeArray(bytes),"UTF-8"));
				bufferedWriter.newLine();
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

	private static byte[] changeByteListToPrimitiveDataTypeArray (List<Byte> bytes)
	{
		byte[] array = new byte[bytes.size()];
		for (int i = 0; i < bytes.size(); i++)
		{
			array[i] = bytes.get(i);
		}

		return array;
	}

	private static byte binaryStringToBytePlus63 (String binaryString)
	{
		return Integer.valueOf(Integer.valueOf(binaryString,2) + 63).byteValue();
	}

}
