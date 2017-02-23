package net.adamjak.thomas.graph.application.run;

import net.adamjak.thomas.graph.application.commons.SnarkTestTypes;
import net.adamjak.thomas.graph.library.api.Graph;
import net.adamjak.thomas.graph.library.io.GraphFactory;
import net.adamjak.thomas.graph.library.tests.GraphTestResult;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.apache.log4j.Logger;
import org.jopendocument.dom.spreadsheet.SpreadSheet;

import javax.swing.JTable;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Tomas Adamjak on 19.2.2017.
 * Copyright 2017, Tomas Adamjak
 * License: The BSD 3-Clause License
 */
public abstract class TestRunner
{
	final static Logger LOGGER = Logger.getLogger(TestRunner.class);

	protected File inputFile;
	protected File outputFile;
	protected int loops;
	protected List<Graph<Integer>> graphs;

	public abstract Map<String, Object> run ();

	public TestRunner (File inputFile, File outputFile, int loops)
	{
		this.inputFile = inputFile;
		this.outputFile = outputFile;
		this.loops = loops;

		this.init();
	}

	private void init ()
	{
		try
		{
			switch (GraphFactory.getFileFormat(this.inputFile))
			{
				case GRAPH6:
					this.graphs = GraphFactory.createGraphFromGraph6(this.inputFile);
					break;
				case GRAPHML:
					this.graphs = new ArrayList<Graph<Integer>>(1);
					this.graphs.add(GraphFactory.createGraphFromGraphml(this.inputFile));
					break;
				case BRATISLAVA_TEXT_CATALOG:
					this.graphs = GraphFactory.createGraphFromTextCatalog(this.inputFile);
					break;
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void runAndSaveResults ()
	{
		this.save(this.run());
	}

	private void save (Map<String, Object> results)
	{
		SnarkTestTypes testType = (SnarkTestTypes) results.get("testType");

		if (this.outputFile.getName().split("\\.")[this.outputFile.getName().split("\\.").length - 1].toLowerCase().equals("ods"))
		{

			if (testType == SnarkTestTypes.ALL_ALGORITHMS)
			{
				GraphTestResult[][][] graphTestResult = (GraphTestResult[][][]) results.get("resultsData");

				String[] columnNames = {"Algorithm", "Graph ID", "Avarage time", "Standard deviation", "Minimum", "Maximum"};
				Object[][] data = new Object[graphTestResult[0].length][6];

				for (int cls = 0; cls < graphTestResult[0][0].length; cls++)
				{
					Class<?> c = (Class<?>) graphTestResult[0][0][cls].getValue("algorithmClass");

					for (int graph = 0; graph < graphTestResult[0].length; graph++)
					{
						SummaryStatistics summaryStatistics = new SummaryStatistics();

						for (int run = 0; run < graphTestResult.length; run++)
						{
							summaryStatistics.addValue((double) graphTestResult[run][graph][cls].getValue("timeInSeconds"));
						}

						data[graph][0] = c.getSimpleName();
						data[graph][1] = graph;
						data[graph][2] = summaryStatistics.getMean();
						data[graph][3] = summaryStatistics.getStandardDeviation();
						data[graph][4] = summaryStatistics.getMin();
						data[graph][5] = summaryStatistics.getMax();
					}
				}

				try
				{
					SpreadSheet.createEmpty(new JTable(data, columnNames).getModel()).saveAs(outputFile);
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
			else
			{
				GraphTestResult[][] graphTestResult = (GraphTestResult[][]) results.get("resultsData");

				String[] columnNames = {"Graph ID", "Avarage time", "Standard deviation", "Minimum", "Maximum"};
				Object[][] data = new Object[graphTestResult[0].length][5];

				for (int graph = 0; graph < graphTestResult[0].length; graph++)
				{
					SummaryStatistics summaryStatistics = new SummaryStatistics();

					for (int run = 0; run < graphTestResult.length; run++)
					{
						summaryStatistics.addValue((double) graphTestResult[run][graph].getValue("timeInSeconds"));
					}

					data[graph][0] = graph;
					data[graph][1] = summaryStatistics.getMean();
					data[graph][2] = summaryStatistics.getStandardDeviation();
					data[graph][3] = summaryStatistics.getMin();
					data[graph][4] = summaryStatistics.getMax();
				}

				try
				{
					SpreadSheet.createEmpty(new JTable(data, columnNames).getModel()).saveAs(outputFile);
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}


		}
		else
		{
			StringBuilder sb = new StringBuilder();

			if (testType == SnarkTestTypes.ALL_ALGORITHMS)
			{
				GraphTestResult[][][] graphTestResult = (GraphTestResult[][][]) results.get("resultsData");

				sb.append("Algorithm,Graph ID,Avarage time,Standard deviation,Minimum,Maximum\n");

				for (int cls = 0; cls < graphTestResult[0][0].length; cls++)
				{
					Class<?> c = (Class<?>) graphTestResult[0][0][cls].getValue("algorithmClass");

					for (int graph = 0; graph < graphTestResult[0].length; graph++)
					{
						SummaryStatistics summaryStatistics = new SummaryStatistics();

						for (int run = 0; run < graphTestResult.length; run++)
						{
							summaryStatistics.addValue((double) graphTestResult[run][graph][cls].getValue("timeInSeconds"));
						}

						sb.append(c.getSimpleName());
						sb.append(",");
						sb.append(graph);
						sb.append(",");
						sb.append(summaryStatistics.getMean());
						sb.append(",");
						sb.append(summaryStatistics.getStandardDeviation());
						sb.append(",");
						sb.append(summaryStatistics.getMin());
						sb.append(",");
						sb.append(summaryStatistics.getMax());
						sb.append("\n");
					}
				}
			}
			else
			{

				GraphTestResult[][] graphTestResult = (GraphTestResult[][]) results.get("resultsData");

				sb.append("Graph ID,Avarage time,Standard deviation,Minimum,Maximum\n");

				for (int graph = 0; graph < graphTestResult[0].length; graph++)
				{
					SummaryStatistics summaryStatistics = new SummaryStatistics();

					for (int run = 0; run < graphTestResult.length; run++)
					{
						summaryStatistics.addValue((double) graphTestResult[run][graph].getValue("timeInSeconds"));
					}

					sb.append(graph);
					sb.append(",");
					sb.append(summaryStatistics.getMean());
					sb.append(",");
					sb.append(summaryStatistics.getStandardDeviation());
					sb.append(",");
					sb.append(summaryStatistics.getMin());
					sb.append(",");
					sb.append(summaryStatistics.getMax());
					sb.append("\n");
				}

			}

			BufferedWriter bw = null;
			FileWriter fw = null;

			try
			{
				fw = new FileWriter(this.outputFile);
				bw = new BufferedWriter(fw);
				bw.write(sb.toString());
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			finally
			{
				try
				{
					if (bw != null) bw.close();
					if (fw != null) fw.close();
				}
				catch (IOException ex)
				{
					ex.printStackTrace();
				}
			}
		}
	}
}
