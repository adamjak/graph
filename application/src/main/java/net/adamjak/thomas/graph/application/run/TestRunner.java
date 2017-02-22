package net.adamjak.thomas.graph.application.run;

import net.adamjak.thomas.graph.library.tests.GraphTestResult;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.jopendocument.dom.spreadsheet.SpreadSheet;

import javax.swing.JTable;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Tomas Adamjak on 19.2.2017.
 * Copyright 2017, Tomas Adamjak
 * License: The BSD 3-Clause License
 */
public abstract class TestRunner
{
	private File inputFile;
	private File outputFile;
	private int loops;

	public abstract GraphTestResult[][] run ();

	public TestRunner (File inputFile, File outputFile, int loops)
	{
		this.inputFile = inputFile;
		this.outputFile = outputFile;
		this.loops = loops;
	}

	private void save (GraphTestResult[][] snarkTestResult)
	{
		if (this.outputFile.getName().split("\\.")[this.outputFile.getName().split("\\.").length - 1].toLowerCase().equals("ods"))
		{


			String[] columnNames = {"Graph ID", "Avarage time", "Standard deviation", "Minimum", "Maximum"};
			Object[][] data = new Object[snarkTestResult[0].length][5];

			for (int graph = 0; graph < snarkTestResult[0].length; graph++)
			{
				SummaryStatistics summaryStatistics = new SummaryStatistics();

				for (int run = 0; run < snarkTestResult.length; run++)
				{
					summaryStatistics.addValue((double) snarkTestResult[run][graph].getValue("timeInSeconds"));
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
		else
		{
			StringBuilder sb = new StringBuilder();

			sb.append("Graph ID,Avarage time,Standard deviation,Minimum,Maximum\n");

			for (int graph = 0; graph < snarkTestResult[0].length; graph++)
			{
				SummaryStatistics summaryStatistics = new SummaryStatistics();

				for (int run = 0; run < snarkTestResult.length; run++)
				{
					summaryStatistics.addValue((double) snarkTestResult[run][graph].getValue("timeInSeconds"));
				}

				sb.append(graph);
				sb.append(",")
				sb.append(summaryStatistics.getMean());
				sb.append(",")
				sb.append(summaryStatistics.getStandardDeviation());
				sb.append(",")
				sb.append(summaryStatistics.getMin());
				sb.append(",")
				sb.append(summaryStatistics.getMax());
				sb.append("\n")
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
