package net.adamjak.thomas.graph.application.gui;

import net.adamjak.thomas.graph.application.commons.Utils;
import net.adamjak.thomas.graph.library.api.Graph;
import net.adamjak.thomas.graph.library.interfaces.anot.Benchmarked;
import net.adamjak.thomas.graph.library.io.GraphFactory;
import net.adamjak.thomas.graph.library.tests.GraphTest;
import net.adamjak.thomas.graph.library.tests.GraphTestResult;
import net.adamjak.thomas.graph.library.utils.ClassFinder;
import net.adamjak.thomas.graph.library.utils.Settings;
import org.apache.log4j.Logger;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;

/**
 * Created by Tomas Adamjak on 22.11.2016.
 * Copyright 2016, Tomas Adamjak
 * License: The BSD 3-Clause License
 */
public class AppMainWindow extends JFrame
{
	// --------------------------------------------------
	// Constants
	// --------------------------------------------------
	private final static String WINDOW_TITLE = "Graph app";
	final static Logger LOGGER = Logger.getLogger(AppMainWindow.class);

	// --------------------------------------------------
	// Components
	// --------------------------------------------------
	private JPanel rootPanel;
	private JScrollPane jScrollPane;
	private JTable jTable;
	private JProgressBar jProgressBar;
	private JMenuBar jMenuBar;

	// --------------------------------------------------
	// Other
	// --------------------------------------------------
	private AppMainWindow appMainWindow;
	private List<Graph<Integer>> graphList = null;
	private String sourceFile;

	public AppMainWindow ()
	{
		super(WINDOW_TITLE);
		this.appMainWindow = this;

		setIconImage(new ImageIcon(getClass().getClassLoader().getResource("logo-image/logo_64.png")).getImage());

		setContentPane(rootPanel);
		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1000, 500);
		setJMenuBar(this.createJMenuBar());
	}

	private void createUIComponents ()
	{
		this.jTable = this.createTable();
	}

	private JTable createTable ()
	{
		String[] columnNames = {"ID",
				"Count of vertexes",
				"Time",
				"Info"};
		String[][] data = this.getTableData();

		JTable jTable = new JTable(data, columnNames);
		DefaultTableModel model = new DefaultTableModel(columnNames, data.length);
		jTable.setModel(model);
		return jTable;
	}

	private String[][] getTableData ()
	{
		String[][] data;
		if (this.graphList != null && this.graphList.size() > 0)
		{
			data = new String[this.graphList.size()][4];

			for (int i = 0; i < this.graphList.size(); i++)
			{
				data[i][0] = String.valueOf(i);
				data[i][1] = String.valueOf(this.graphList.get(i).getCountOfVertexes());
				data[i][2] = "";
				data[i][3] = "";
			}
		}
		else
		{
			data = new String[0][4];
		}

		return data;
	}

	private void repaintTable ()
	{
		DefaultTableModel model = (DefaultTableModel) this.jTable.getModel();

		while (model.getRowCount() > 0)
		{
			model.removeRow(0);
		}

		for (String[] s : this.getTableData())
		{
			model.addRow(s);
		}
	}

	private JMenuBar createJMenuBar ()
	{
		this.jMenuBar = new JMenuBar();

		// Menu File
		JMenu jmFile = new JMenu("File");

		// Menu File items
		JMenuItem jmiFileOpen = new JMenuItem("Open");
		jmiFileOpen.setAccelerator(GuiAccelerators.CTRL_O);
		jmiFileOpen.addActionListener(new AlJmiFileOpen());
		jmFile.add(jmiFileOpen);

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

		// Menu selection
		JMenu jmSelection = new JMenu("Selection");

		// Menu Selection items
		JMenuItem jmiSelectionSelectAll = new JMenuItem("Select all");
		jmiSelectionSelectAll.setAccelerator(GuiAccelerators.CTRL_A);
		jmiSelectionSelectAll.addActionListener(new AlJmiSelectionSelectAll());
		jmSelection.add(jmiSelectionSelectAll);
		JMenuItem jmiSelectionUnselectAll = new JMenuItem("Unselect all");
		jmiSelectionUnselectAll.setAccelerator(GuiAccelerators.CTRL_SHIFT_A);
		jmiSelectionUnselectAll.addActionListener(new AlJmiSelectionUnselectAll());
		jmSelection.add(jmiSelectionUnselectAll);
		JMenuItem jmiSelectionInvert = new JMenuItem("Invert");
		jmiSelectionInvert.setAccelerator(GuiAccelerators.CTRL_I);
		jmiSelectionInvert.addActionListener(new AlJmiSelectionInvert());
		jmSelection.add(jmiSelectionInvert);

		this.jMenuBar.add(jmSelection);

		// Menu Actions
		JMenu jmActions = new JMenu("Actions");

		// Menu Actions items
		JMenu jmActionsSnark = new JMenu("Snark tests");
		// Menu Action Snark items
		JMenuItem jmiActionSnarkTestAllAlgorithms = new JMenuItem("Run all algorithms");
		jmiActionSnarkTestAllAlgorithms.setAccelerator(GuiAccelerators.ALT_A);
		jmiActionSnarkTestAllAlgorithms.setEnabled(false);
		jmActionsSnark.add(jmiActionSnarkTestAllAlgorithms);

		jmActions.add(jmActionsSnark);

		JMenuItem jmiActionSnarkAlgorithmCompare = new JMenuItem("Run algorithm comparation");
		jmiActionSnarkAlgorithmCompare.setAccelerator(GuiAccelerators.ALT_C);
		jmiActionSnarkAlgorithmCompare.addActionListener(new AlJmiActionSnarkAlgorithmCompare());
		jmActionsSnark.add(jmiActionSnarkAlgorithmCompare);

		// Menu Actions Products
		JMenu jmActionsProducts = new JMenu("Products");
		// Menu Actions Products items
		JMenuItem jmiActionsProductsDot = new JMenuItem("Dot product");
		jmiActionsProductsDot.setAccelerator(GuiAccelerators.ALT_D);
		jmActionsProducts.add(jmiActionsProductsDot);
		JMenuItem jmiActionsProductsStar = new JMenuItem("Star product");
		jmiActionsProductsStar.setAccelerator(GuiAccelerators.ALT_S);
		jmActionsProducts.add(jmiActionsProductsStar);
		jmActionsProducts.setEnabled(false);
		jmActions.add(jmActionsProducts);

		this.jMenuBar.add(jmActions);

		// Menu About
		JMenu jmAbout = new JMenu("About");

		// Menu about items
		JMenuItem jmiAboutLicense = new JMenuItem("License");
		jmiAboutLicense.addActionListener(new AlJmiAboutLicense());
		jmAbout.add(jmiAboutLicense);

		JMenuItem jmiAboutAuthor = new JMenuItem("Author");
		jmiAboutAuthor.addActionListener(new AlJmiAboutAuthor());
		jmAbout.add(jmiAboutAuthor);

		this.jMenuBar.add(jmAbout);

		return this.jMenuBar;
	}

	private void doSnarkAlgorithmCompare ()
	{
		if (jTable.getSelectedRowCount() == 0)
		{
			JOptionPane.showMessageDialog(appMainWindow, "Please select at least one graph.", "Error!", JOptionPane.ERROR_MESSAGE);
			return;
		}

		int times = 0;

		while (times < 1)
		{
			String ans = JOptionPane.showInputDialog(appMainWindow, "How many times do you want to run?", "Setting", JOptionPane.QUESTION_MESSAGE);

			if (ans == null)
			{
				return;
			}

			try
			{
				times = Integer.valueOf(ans);
			}
			catch (NumberFormatException e)
			{
				JOptionPane.showMessageDialog(appMainWindow, "You must enter an integer greater than 0!", "Error.", JOptionPane.ERROR_MESSAGE);
			}
		}


		Thread t = new Thread(new DoSnarkAlgorithmCompare(times));
		t.start();
	}

	// ---------------------------------------------------
	// Listeners
	// ---------------------------------------------------

	// ---------------------------------------------------
	// File menu listeners
	// ---------------------------------------------------

	private class AlJmiFileOpen implements ActionListener
	{
		@Override
		public void actionPerformed (ActionEvent event)
		{
			LOGGER.info("File opening");
			JFileChooser jFileChooser = new JFileChooser();
			jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			jFileChooser.setMultiSelectionEnabled(true);

			int status = jFileChooser.showOpenDialog(appMainWindow);
			if (status == JFileChooser.APPROVE_OPTION)
			{
				List<Graph<Integer>> graphs = new LinkedList<Graph<Integer>>();

				for (File f : jFileChooser.getSelectedFiles())
				{
					sourceFile += f.getAbsolutePath() + " ";
					try
					{
						switch (GraphFactory.getFileFormat(f))
						{
							case GRAPH6:
								graphs.addAll(GraphFactory.createGraphFromGraph6(f));
								break;
							case GRAPHML:
								graphs.add(GraphFactory.createGraphFromGraphml(f));
								break;
							case BRATISLAVA_TEXT_CATALOG:
							default:
								graphs.addAll(GraphFactory.createGraphFromTextCatalog(f));
								break;
						}
					}
					catch (IOException e)
					{
						JOptionPane.showMessageDialog(appMainWindow, "File read error!\nFile:" + f.getName() + "\n" + e.getMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
						return;
					}
				}

				graphList = graphs;
				repaintTable();
				LOGGER.info("Open graph collection with " + graphs.size() + " graphs");
			}
		}
	}

	// ---------------------------------------------------
	// Selection menu listeners
	// ---------------------------------------------------

	private class AlJmiSelectionSelectAll implements ActionListener
	{
		@Override
		public void actionPerformed (ActionEvent event)
		{
			jTable.selectAll();
			LOGGER.info("Select all");
		}
	}

	private class AlJmiSelectionUnselectAll implements ActionListener
	{
		@Override
		public void actionPerformed (ActionEvent e)
		{
			jTable.clearSelection();
			LOGGER.info("Unselect all");
		}
	}

	private class AlJmiSelectionInvert implements ActionListener
	{
		@Override
		public void actionPerformed (ActionEvent e)
		{
			int[] selected = jTable.getSelectedRows();

			for (int i = 0; i < jTable.getRowCount(); i++)
			{
				if (Utils.isIntInArray(i, selected))
				{
					jTable.removeRowSelectionInterval(i, i);
				}
				else
				{
					jTable.addRowSelectionInterval(i, i);
				}
			}

			LOGGER.info("Invert selection");
		}
	}

	// ---------------------------------------------------
	// Action menu listeners
	// ---------------------------------------------------

	private class AlJmiActionSnarkAlgorithmCompare implements ActionListener
	{
		@Override
		public void actionPerformed (ActionEvent event)
		{
			doSnarkAlgorithmCompare();
		}
	}

	// ---------------------------------------------------
	// About menu listeners
	// ---------------------------------------------------

	private class AlJmiAboutLicense implements ActionListener
	{
		@Override
		public void actionPerformed (ActionEvent e)
		{
			LOGGER.info("Display license informations.");

			Icon icon = new ImageIcon(getClass().getClassLoader().getResource("logo-image/logo_64.jpeg"));

			String message = "<html>" +
					"<h2>Copyright (c) 2016, Tomáš Adamják</h2>" +
					"<p><strong>All rights reserved.</strong></p>" +
					"<p>Redistribution and use in source and binary forms, with or without modification,<br />" +
					"are permitted provided that the following conditions are met:</p>" +
					"<ul>" +
					"<li>Redistributions of source code must retain the above copyright notice,<br />" +
					"this list of conditions and the following disclaimer.</li>" +
					"<li>Redistributions in binary form must reproduce the above copyright notice,<br />" +
					"this list of conditions and the following disclaimer in the documentation<br />" +
					"and/or other materials provided with the distribution.</li>" +
					"<li>Neither the name of the <organization> nor the names of its contributors may be<br />" +
					"used to endorse or promote products derived from this software without specific prior written permission.</li>" +
					"</ul>" +
					"<p>THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS \"AS IS\" AND<br />" +
					"ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED<br />" +
					"WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE<br />" +
					"DISCLAIMED. IN NO EVENT SHALL Tomáš Adamják BE LIABLE FOR ANY<br />" +
					"DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES<br />" +
					"(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;<br />" +
					"LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND<br />" +
					"ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT<br />" +
					"(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS<br />" +
					"SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.</p>";

			JLabel formatedLabel = new JLabel(message);
			formatedLabel.setFont(new Font("sans-serif", Font.PLAIN, 14));

			JOptionPane.showMessageDialog(appMainWindow, formatedLabel, "The BSD 3-Clause License", JOptionPane.INFORMATION_MESSAGE, icon);
		}
	}

	private class AlJmiAboutAuthor implements ActionListener
	{
		@Override
		public void actionPerformed (ActionEvent e)
		{
			LOGGER.info("Display author informations.");

			Icon icon = new ImageIcon(getClass().getClassLoader().getResource("logo-image/logo_64.jpeg"));

			String message = "<html>" +
					"<h2>Tomáš Adamják</h2>" +
					"<p><strong>E-mail:</strong> <code>thomas@adamjak.net</code></p>" +
					"<p><strong>Web:</strong> <code>http://thomas.adamjak.net</code></p>" +
					"<html>";

			JLabel formatedLabel = new JLabel(message);
			formatedLabel.setFont(new Font("sans-serif", Font.PLAIN, 14));
			JOptionPane.showMessageDialog(appMainWindow, formatedLabel, "Author", JOptionPane.INFORMATION_MESSAGE, icon);
		}
	}

	// ---------------------------------------------------
	// Runners
	// ---------------------------------------------------

	private class DoSnarkAlgorithmCompare implements Runnable
	{
		private int times;

		public DoSnarkAlgorithmCompare (int times)
		{
			this.times = times;
		}

		@Override
		public void run ()
		{

			LOGGER.info("Start snark algorithm comparation.");
			jProgressBar.setValue(0);

			ClassFinder classFinder = new ClassFinder();
			Set<String> packageNames = new LinkedHashSet<String>();
			packageNames.add(Settings.getInstance().getSetting("packageName"));
			Set<Class<?>> classes = classFinder.findClassesWhitchExtends(classFinder.findAnnotatedClasses(packageNames, true, Benchmarked.class), GraphTest.class);

			int[] selectedRows = jTable.getSelectedRows();

			GraphTestResult[][] results = new GraphTestResult[times][selectedRows.length];

			for (int run = 0; run < times; run++)
			{
				for (int i : selectedRows)
				{
					Graph<Integer> g = graphList.get(i);
					ForkJoinPool forkJoinPool = new ForkJoinPool(classes.size());
					Set<GraphTest> snarkTests = new LinkedHashSet<GraphTest>();

					for (Class<?> c : classes)
					{
						GraphTest snarkTest = null;
						try
						{
							snarkTest = (GraphTest) c.newInstance();
							snarkTest.init(g);
							snarkTests.add(snarkTest);
						}
						catch (InstantiationException e)
						{
							e.printStackTrace();
						}
						catch (IllegalAccessException e)
						{
							e.printStackTrace();
						}
					}

					for (GraphTest st : snarkTests)
					{
						forkJoinPool.execute(st);
					}

					GraphTestResult graphTestResult = null;

					while (graphTestResult == null)
					{
						for (GraphTest st : snarkTests)
						{
							if (st.isDone())
							{
								try
								{
									graphTestResult = st.getResult();
								}
								catch (InterruptedException e)
								{
									e.printStackTrace();
								}
								catch (ExecutionException e)
								{
									e.printStackTrace();
								}
							}
						}
					}

					String sout = "Testing run: " + (run + 1) + ", Graph id: " + i + ", Result: " + graphTestResult;
					LOGGER.info(sout);

					jTable.setValueAt(graphTestResult.getValue("timeInSeconds"), i, 2);
					jTable.setValueAt(graphTestResult.toString(), i, 3);

					results[run][i] = graphTestResult;
				}

				jProgressBar.setValue(100 / times * (run + 1));
			}

			jProgressBar.setValue(100);

			Map<String, Object> resultValues = new HashMap<String, Object>();
			resultValues.put("test", "Snark algorithm comparation");
			resultValues.put("resultsData", results);
			resultValues.put("times", times);

			new ResultsWidnow(resultValues);
		}
	}


}
