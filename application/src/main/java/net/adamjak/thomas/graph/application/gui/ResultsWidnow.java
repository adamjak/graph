package net.adamjak.thomas.graph.application.gui;

import net.adamjak.thomas.graph.application.commons.Utils;
import net.adamjak.thomas.graph.library.tests.GraphTestResult;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.apache.log4j.Logger;
import org.jopendocument.dom.spreadsheet.SpreadSheet;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.TableColumn;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;

/**
 * Created by Tomas Adamjak on 2.12.2016.
 * Copyright 2016, Tomas Adamjak
 * License: The BSD 3-Clause License
 */
public class ResultsWidnow extends JFrame
{
	// --------------------------------------------------
	// Constants
	// --------------------------------------------------
	private final static String WINDOW_TITLE = "Results";
	final static Logger LOGGER = Logger.getLogger(AppMainWindow.class);

	// --------------------------------------------------
	// Components
	// --------------------------------------------------
	private JPanel rootPanel;
	private JTabbedPane jtpTabs;
	private JPanel jpResults;
	private JPanel jpCharts;
	private JTable jtResults;
	private JLabel jlNumberOfRuns;
	private JLabel jlTestType;
	private JMenuBar jMenuBar;

	// --------------------------------------------------
	// Others
	// --------------------------------------------------
	private final ResultsWidnow resultsWidnow;
	private Map<String, Object> results;

	public ResultsWidnow (Map<String, Object> results)
	{
		super(WINDOW_TITLE);
		this.resultsWidnow = this;
		this.results = results;

		setIconImage(new ImageIcon(getClass().getClassLoader().getResource("logo-image/logo_64.png")).getImage());

		setContentPane(rootPanel);
		pack();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(800, 500);

		if (this.results.containsKey("test"))
		{
			this.jlTestType.setText(this.results.get("test").toString());
		}

		if (this.results.containsKey("times"))
		{
			this.jlNumberOfRuns.setText(this.results.get("times").toString());
		}

		setJMenuBar(this.createJMenuBar());

		setVisible(true);

	}

	private void createUIComponents ()
	{
		this.jtResults = this.createJtResults();
	}

	private JTable createJtResults ()
	{
		if (this.results.containsKey("resultsData"))
		{
			GraphTestResult[][] results = (GraphTestResult[][]) this.results.get("resultsData");

			String[] columnNames = {"Graph ID", "Avarage time", "Standard deviation", "Minimum", "Maximum"};
			Object[][] data = new Object[results[0].length][5];

			for (int graph = 0; graph < results[0].length; graph++)
			{
				SummaryStatistics summaryStatistics = new SummaryStatistics();

				for (int run = 0; run < results.length; run++)
				{
					summaryStatistics.addValue((double) results[run][graph].getValue("timeInSeconds"));
				}

				data[graph][0] = graph;
				data[graph][1] = summaryStatistics.getMean();
				data[graph][2] = summaryStatistics.getStandardDeviation();
				data[graph][3] = summaryStatistics.getMin();
				data[graph][4] = summaryStatistics.getMax();
			}

			return new JTable(data, columnNames);
		}
		else
		{
			String[] columnNames = {"Description", "Result"};
			Object[][] data = new Object[this.results.keySet().size()][2];

			int i = 0;
			for (String key : this.results.keySet())
			{
				data[i][0] = key;
				data[i][1] = this.results.get(key);
				i++;
			}

			return new JTable(data, columnNames);
		}
	}

	private JMenuBar createJMenuBar ()
	{
		this.jMenuBar = new JMenuBar();

		// Menu File
		JMenu jmFile = new JMenu("File");

		JMenuItem jmiFileSaveResults = new JMenuItem("Save results");
		jmiFileSaveResults.setAccelerator(GuiAccelerators.CTRL_S);
		jmiFileSaveResults.addActionListener(new AlJmiFileSaveResults());

		jmFile.add(jmiFileSaveResults);

		jmFile.addSeparator();

		JMenuItem jmiFileClose = new JMenuItem("Close");
		jmiFileClose.setAccelerator(GuiAccelerators.CTRL_Q);
		jmiFileClose.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed (ActionEvent e)
			{
				LOGGER.info("The application will now be halted.");
				System.exit(0);
			}
		});
		jmFile.add(jmiFileClose);

		this.jMenuBar.add(jmFile);

		return this.jMenuBar;
	}

	// ---------------------------------------------------
	// Listeners
	// ---------------------------------------------------

	// ---------------------------------------------------
	// File menu listeners
	// ---------------------------------------------------

	private class AlJmiFileSaveResults implements ActionListener
	{
		@Override
		public void actionPerformed (ActionEvent event)
		{
			JFileChooser jfc = new JFileChooser();
			jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			jfc.setFileFilter(new FileFilter()
			{
				@Override
				public boolean accept (File f)
				{
					if (f.isDirectory() || f.getName().split("\\.")[f.getName().split("\\.").length - 1].toLowerCase().equals("csv") || f.getName().split("\\.")[f.getName().split("\\.").length - 1].toLowerCase().equals("ods"))
					{
						return true;
					}
					return false;
				}

				@Override
				public String getDescription ()
				{
					return "*.csv, *.ods";
				}
			});

			if (jfc.showOpenDialog(resultsWidnow) == JFileChooser.APPROVE_OPTION)
			{
				File f = jfc.getSelectedFile();

				if (f.exists() == false || (f.exists() == true && JOptionPane.showConfirmDialog(resultsWidnow, "File exit. Would you like to rewrite it?", "Warning", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION))
				{
					LOGGER.info("Save into " + f.getAbsolutePath() + " file.");

					if (f.getName().split("\\.")[f.getName().split("\\.").length - 1].toLowerCase().equals("ods"))
					{
						try
						{
							SpreadSheet.createEmpty(jtResults.getModel()).saveAs(f);
							JOptionPane.showMessageDialog(resultsWidnow, "Saved.", "OK", JOptionPane.INFORMATION_MESSAGE);
						}
						catch (IOException e)
						{
							JOptionPane.showMessageDialog(resultsWidnow, "File write error.\n" + e.getMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
						}
					}
					else
					{
						StringBuilder sb = new StringBuilder();

						Enumeration<TableColumn> tableColumnEnumeration = jtResults.getTableHeader().getColumnModel().getColumns();
						int col = 0;
						while (tableColumnEnumeration.hasMoreElements())
						{
							if (col == 0)
							{
								sb.append(tableColumnEnumeration.nextElement().getHeaderValue());
							}
							else
							{
								sb.append("," + tableColumnEnumeration.nextElement().getHeaderValue());
							}
							col++;
						}
						sb.append("\n");

						for (Object[] o : Utils.getDataFromJTable(jtResults))
						{
							for (int i = 0; i < jtResults.getColumnCount(); i++)
							{
								if (i == 0)
								{
									sb.append(o[i]);
								}
								else
								{
									sb.append("," + o[i]);
								}
							}
							sb.append("\n");
						}

						BufferedWriter bw = null;
						FileWriter fw = null;
						try
						{
							fw = new FileWriter(f);
							bw = new BufferedWriter(fw);
							bw.write(sb.toString());
							JOptionPane.showMessageDialog(resultsWidnow, "Saved.", "OK", JOptionPane.INFORMATION_MESSAGE);
						}
						catch (IOException e)
						{
							JOptionPane.showMessageDialog(resultsWidnow, "File write error.\n" + e.getMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
						}
						finally
						{
							try
							{
								if (bw != null) bw.close();
								if (fw != null) fw.close();
							}
							catch (IOException e)
							{
								JOptionPane.showMessageDialog(resultsWidnow, "File close error.\n" + e.getMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
							}
						}
					}
				}
			}
		}
	}
}
