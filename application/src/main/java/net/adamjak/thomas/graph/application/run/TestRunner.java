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
import java.util.Observable;

/**
 * Created by Tomas Adamjak on 19.2.2017.
 * Copyright 2017, Tomas Adamjak
 * License: The BSD 3-Clause License
 */
public abstract class TestRunner extends Observable
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

	public TestRunner (List<Graph<Integer>> graphs, File outputFile, int loops)
	{
		this.graphs = graphs;
		this.outputFile = outputFile;
		this.loops = loops;
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
		this.save(this.run(), false);
	}

	public void runAndSaveResultsWithRawData ()
	{
		this.save(this.run(), true);
	}

	private void save (Map<String, Object> results, boolean rawData)
	{
		SnarkTestTypes testType = (SnarkTestTypes) results.get("testType");

		if (this.outputFile.getName().split("\\.")[this.outputFile.getName().split("\\.").length - 1].toLowerCase().equals("ods"))
		{

			String[] columnNames;
			Object[][] data;

			if (testType == SnarkTestTypes.ALL_ALGORITHMS)
			{
				GraphTestResult[][][] graphTestResult = (GraphTestResult[][][]) results.get("resultsData");

				columnNames = String.valueOf("Algorithm,Graph ID,Avarage time,Standard deviation,Minimum,Maximum").split(",");
				data = new Object[graphTestResult[0].length][6];

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
			}
			else if (testType == SnarkTestTypes.ONE_ALGORITHM_START_IN_EVERY_VERTEX)
			{
				GraphTestResult[][][] graphTestResult = (GraphTestResult[][][]) results.get("resultsData");

				columnNames = String.valueOf("Graph ID,Start vertex,Avarage time,Standard deviation,Minimum,Maximum").split(",");
				data = new Object[graphTestResult[0].length][6];

				for (int vid = 0; vid < graphTestResult[0][0].length; vid++)
				{
					for (int graph = 0; graph < graphTestResult[0].length; graph++)
					{
						SummaryStatistics summaryStatistics = new SummaryStatistics();

						for (int run = 0; run < graphTestResult.length; run++)
						{
							summaryStatistics.addValue((double) graphTestResult[run][graph][vid].getValue("timeInSeconds"));
						}

						data[graph][0] = graph;
						data[graph][1] = vid;
						data[graph][2] = summaryStatistics.getMean();
						data[graph][3] = summaryStatistics.getStandardDeviation();
						data[graph][4] = summaryStatistics.getMin();
						data[graph][5] = summaryStatistics.getMax();
					}
				}
			}
			else
			{
				GraphTestResult[][] graphTestResult = (GraphTestResult[][]) results.get("resultsData");

				columnNames = String.valueOf("Graph ID,Avarage time,Standard deviation,Minimum,Maximum").split(",");
				data = new Object[graphTestResult[0].length][5];

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
			}

			try
			{
				SpreadSheet.createEmpty(new JTable(data, columnNames).getModel()).saveAs(outputFile);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}

			if (rawData == true)
			{
				if (testType == SnarkTestTypes.ALL_ALGORITHMS)
				{
					GraphTestResult[][][] graphTestResult = (GraphTestResult[][][]) results.get("resultsData");

					columnNames = String.valueOf("Class,Run,Graph,Time").split(",");
					data = new Object[graphTestResult.length * graphTestResult[0].length * graphTestResult[0][0].length][4];

					int row = 0;
					for (int i = 0; i < graphTestResult.length; i++)
					{
						for (int j = 0; j < graphTestResult[i].length; j++)
						{
							for (int k = 0; k < graphTestResult[i][j].length; k++)
							{
								data[row][0] = graphTestResult[i][j][k].getValue("algorithmClass");
								data[row][1] = i;
								data[row][2] = j;
								data[row][3] = graphTestResult[i][j][k].getValue("time");
								row++;
							}
						}
					}
				}
				else if (testType == SnarkTestTypes.ONE_ALGORITHM_START_IN_EVERY_VERTEX)
				{
					GraphTestResult[][][] graphTestResult = (GraphTestResult[][][]) results.get("resultsData");

					columnNames = String.valueOf("Run,Graph,Vertex,Time").split(",");
					data = new Object[graphTestResult.length * graphTestResult[0].length * graphTestResult[0][0].length][4];

					int row = 0;
					for (int i = 0; i < graphTestResult.length; i++)
					{
						for (int j = 0; j < graphTestResult[i].length; j++)
						{
							for (int k = 0; k < graphTestResult[i][j].length; k++)
							{
								data[row][0] = i;
								data[row][1] = j;
								data[row][2] = k;
								data[row][3] = graphTestResult[i][j][k].getValue("time");
								row++;
							}
						}
					}
				}
				else if (testType == SnarkTestTypes.ALGORITHM_COMPARATION)
				{
					GraphTestResult[][] graphTestResult = (GraphTestResult[][]) results.get("resultsData");

					columnNames = String.valueOf("Run,Graph,Time,Class").split(",");
					data = new Object[graphTestResult.length * graphTestResult[0].length][4];

					int row = 0;
					for (int i = 0; i < graphTestResult.length; i++)
					{
						for (int j = 0; j < graphTestResult[i].length; j++)
						{
							data[row][0] = i;
							data[row][1] = j;
							data[row][2] = graphTestResult[i][j].getValue("time");
							data[row][3] = ((Class<?>) graphTestResult[i][j].getValue(GraphTestResult.SNARK_TESTER_CLASS_KEY)).getSimpleName();
							row++;
						}
					}
				}
				else
				{
					GraphTestResult[][] graphTestResult = (GraphTestResult[][]) results.get("resultsData");

					columnNames = String.valueOf("Run,Graph,Time").split(",");
					data = new Object[graphTestResult.length * graphTestResult[0].length][3];

					int row = 0;
					for (int i = 0; i < graphTestResult.length; i++)
					{
						for (int j = 0; j < graphTestResult[i].length; j++)
						{
							data[row][0] = i;
							data[row][1] = j;
							data[row][2] = graphTestResult[i][j].getValue("time");
							row++;
						}
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
		}
		else
		{
			StringBuilder sbData = new StringBuilder();

			if (testType == SnarkTestTypes.ALL_ALGORITHMS)
			{
				GraphTestResult[][][] graphTestResult = (GraphTestResult[][][]) results.get("resultsData");

				sbData.append("Algorithm,Graph ID,Avarage time,Standard deviation,Minimum,Maximum\n");

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

						sbData.append(c.getSimpleName());
						sbData.append(",");
						sbData.append(graph);
						sbData.append(",");
						sbData.append(summaryStatistics.getMean());
						sbData.append(",");
						sbData.append(summaryStatistics.getStandardDeviation());
						sbData.append(",");
						sbData.append(summaryStatistics.getMin());
						sbData.append(",");
						sbData.append(summaryStatistics.getMax());
						sbData.append("\n");
					}
				}
			}
			else if (testType == SnarkTestTypes.ONE_ALGORITHM_START_IN_EVERY_VERTEX)
			{
				GraphTestResult[][][] graphTestResult = (GraphTestResult[][][]) results.get("resultsData");

				sbData.append("Graph ID,Start vertex,Avarage time,Standard deviation,Minimum,Maximum\n");

				for (int vid = 0; vid < graphTestResult[0][0].length; vid++)
				{
					for (int graph = 0; graph < graphTestResult[0].length; graph++)
					{
						SummaryStatistics summaryStatistics = new SummaryStatistics();

						for (int run = 0; run < graphTestResult.length; run++)
						{
							summaryStatistics.addValue((double) graphTestResult[run][graph][vid].getValue("timeInSeconds"));
						}

						sbData.append(graph);
						sbData.append(",");
						sbData.append(vid);
						sbData.append(",");
						sbData.append(summaryStatistics.getMean());
						sbData.append(",");
						sbData.append(summaryStatistics.getStandardDeviation());
						sbData.append(",");
						sbData.append(summaryStatistics.getMin());
						sbData.append(",");
						sbData.append(summaryStatistics.getMax());
						sbData.append("\n");
					}
				}
			}
			else
			{

				GraphTestResult[][] graphTestResult = (GraphTestResult[][]) results.get("resultsData");

				sbData.append("Graph ID,Avarage time,Standard deviation,Minimum,Maximum\n");

				for (int graph = 0; graph < graphTestResult[0].length; graph++)
				{
					SummaryStatistics summaryStatistics = new SummaryStatistics();

					for (int run = 0; run < graphTestResult.length; run++)
					{
						summaryStatistics.addValue((double) graphTestResult[run][graph].getValue("timeInSeconds"));
					}

					sbData.append(graph);
					sbData.append(",");
					sbData.append(summaryStatistics.getMean());
					sbData.append(",");
					sbData.append(summaryStatistics.getStandardDeviation());
					sbData.append(",");
					sbData.append(summaryStatistics.getMin());
					sbData.append(",");
					sbData.append(summaryStatistics.getMax());
					sbData.append("\n");
				}

			}

			this.saveStringIntoFile(this.outputFile, sbData.toString());

			if (rawData == true)
			{
				StringBuilder sbRawData = new StringBuilder();

				if (testType == SnarkTestTypes.ALL_ALGORITHMS)
				{
					GraphTestResult[][][] graphTestResult = (GraphTestResult[][][]) results.get("resultsData");

					sbRawData.append("Class,Run,Graph,Time\n");

					for (int i = 0; i < graphTestResult.length; i++)
					{
						for (int j = 0; j < graphTestResult[i].length; j++)
						{
							for (int k = 0; k < graphTestResult[i][j].length; k++)
							{
								sbRawData.append(graphTestResult[i][j][k].getValue("algorithmClass"));
								sbRawData.append(",");
								sbRawData.append(i);
								sbRawData.append(",");
								sbRawData.append(j);
								sbRawData.append(",");
								sbRawData.append(graphTestResult[i][j][k].getValue("time"));
								sbRawData.append("\n");
							}
						}
					}
				}
				else if (testType == SnarkTestTypes.ONE_ALGORITHM_START_IN_EVERY_VERTEX)
				{
					GraphTestResult[][][] graphTestResult = (GraphTestResult[][][]) results.get("resultsData");

					sbRawData.append("Run,Graph,Vertex,Time\n");

					for (int i = 0; i < graphTestResult.length; i++)
					{
						for (int j = 0; j < graphTestResult[i].length; j++)
						{
							for (int k = 0; k < graphTestResult[i][j].length; k++)
							{
								sbRawData.append(i);
								sbRawData.append(",");
								sbRawData.append(j);
								sbRawData.append(",");
								sbRawData.append(k);
								sbRawData.append(",");
								sbRawData.append(graphTestResult[i][j][k].getValue("time"));
								sbRawData.append("\n");
							}
						}
					}
				}
				else if (testType == SnarkTestTypes.ALGORITHM_COMPARATION)
				{
					GraphTestResult[][] graphTestResult = (GraphTestResult[][]) results.get("resultsData");

					sbRawData.append("Run,Graph,Time,Class\n");

					for (int i = 0; i < graphTestResult.length; i++)
					{
						for (int j = 0; j < graphTestResult[i].length; j++)
						{
							sbRawData.append(i);
							sbRawData.append(",");
							sbRawData.append(j);
							sbRawData.append(",");
							sbRawData.append(graphTestResult[i][j].getValue("time"));
							sbRawData.append(",");
							sbRawData.append(((Class<?>) graphTestResult[i][j].getValue(GraphTestResult.SNARK_TESTER_CLASS_KEY)).getSimpleName());
							sbRawData.append("\n");
						}
					}
				}
				else
				{
					GraphTestResult[][] graphTestResult = (GraphTestResult[][]) results.get("resultsData");

					sbRawData.append("Run,Graph,Time\n");

					for (int i = 0; i < graphTestResult.length; i++)
					{
						for (int j = 0; j < graphTestResult[i].length; j++)
						{
							sbRawData.append(i);
							sbRawData.append(",");
							sbRawData.append(j);
							sbRawData.append(",");
							sbRawData.append(graphTestResult[i][j].getValue("time"));
							sbRawData.append("\n");
						}
					}
				}

				this.saveStringIntoFile(new File(this.outputFile.getParent(), "raw_" + this.outputFile.getName()), sbRawData.toString());
			}
		}
	}

	/**
	 * @param f File whitch will contains inserted string
	 * @param s String to tave into file
	 */
	private void saveStringIntoFile (File f, String s)
	{
		BufferedWriter bw = null;
		FileWriter fw = null;

		try
		{
			fw = new FileWriter(f);
			bw = new BufferedWriter(fw);
			bw.write(s);
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
