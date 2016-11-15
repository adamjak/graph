package net.adamjak.thomas.graph.application;

import net.adamjak.thomas.graph.library.api.Graph;
import net.adamjak.thomas.graph.library.io.GraphFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Main
{
	public static void main (String args[]) throws IOException
	{
		File sc424 = new File("/home/tadamjak/work/graphs_library/SC4.24");
		List<Graph<Integer>> graphListSc424 = GraphFactory.createGraphFromTextCatalog(sc424);

		System.out.println("SC4.24 count - " + graphListSc424.size());

		File g2004sncyc4g6 =  new File("/home/tadamjak/work/graphs_library/Generated_graphs.20.04.sn.cyc4.g6");
		List<Graph<Integer>> graphListG2004sncyc4g6 = GraphFactory.createGraphFromGraph6(g2004sncyc4g6);

		System.out.println("Generated_graphs.20.04.sn.cyc4.g6 count - " + graphListG2004sncyc4g6.size());
	}
}
