package net.adamjak.thomas.graph.application.gui;

import net.adamjak.thomas.graph.application.commons.SnarkTestTypes;
import net.adamjak.thomas.graph.application.commons.Utils;
import net.adamjak.thomas.graph.application.run.RunnerBuilder;
import net.adamjak.thomas.graph.application.run.RunnerException;
import net.adamjak.thomas.graph.library.api.Graph;
import net.adamjak.thomas.graph.library.io.GraphFactory;
import net.adamjak.thomas.graph.library.io.GraphInputOutputException;
import net.adamjak.thomas.graph.library.io.GraphSaver;
import net.adamjak.thomas.graph.library.io.SupportedFormats;
import net.adamjak.thomas.graph.library.utils.GraphUtils;
import org.apache.log4j.Logger;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
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
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
	private JLabel jlbProces;
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
		URL url = this.getClass().getClassLoader().getResource("loader.gif");
		Icon loader = new ImageIcon(url);
		this.jlbProces = new JLabel("Processing", loader, SwingConstants.CENTER);
		this.jlbProces.setVisible(false);
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

		for (SnarkTestTypes type : SnarkTestTypes.values())
		{
			JMenuItem jmiActionSnarkTest = new JMenuItem("Run " + type.getValue());
			jmiActionSnarkTest.addActionListener(new AlActionTest(type));
			jmActionsSnark.add(jmiActionSnarkTest);
		}


//		JMenuItem jmiActionSnarkTestAllAlgorithms = new JMenuItem("Run all algorithms");
//		jmiActionSnarkTestAllAlgorithms.setAccelerator(GuiAccelerators.ALT_A);
//		jmiActionSnarkTestAllAlgorithms.setEnabled(false);
//		jmActionsSnark.add(jmiActionSnarkTestAllAlgorithms);

//		JMenuItem jmiActionSnarkAlgorithmCompare = new JMenuItem("Run algorithm comparation");
//		jmiActionSnarkAlgorithmCompare.setAccelerator(GuiAccelerators.ALT_C);
//		jmiActionSnarkAlgorithmCompare.addActionListener(new AlActionTest(SnarkTestTypes.ALGORITHM_COMPARATION));
//		jmActionsSnark.add(jmiActionSnarkAlgorithmCompare);

		jmActions.add(jmActionsSnark);

		// Menu Actions Products
		JMenu jmActionsProducts = new JMenu("Products");
		// Menu Actions Products items
		JMenuItem jmiActionsProductsDot = new JMenuItem("Dot product");
		jmiActionsProductsDot.setAccelerator(GuiAccelerators.ALT_D);
		jmiActionsProductsDot.addActionListener(new AlJmiActionsProductsDot());
		jmActionsProducts.add(jmiActionsProductsDot);
		JMenuItem jmiActionsProductsStar = new JMenuItem("Star product");
		jmiActionsProductsStar.setAccelerator(GuiAccelerators.ALT_S);
		jmiActionsProductsStar.setEnabled(false);
		jmActionsProducts.add(jmiActionsProductsStar);
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

	private void doSnarkAllAlgorithms ()
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

		Thread t = new Thread(new DoSnarkAllAlgorithms(times));
		t.start();
	}

	private void doSnarkOneAlgorithm ()
	{
		if (jTable.getSelectedRowCount() == 0)
		{
			JOptionPane.showMessageDialog(appMainWindow, "Please select at least one graph.", "Error!", JOptionPane.ERROR_MESSAGE);
			return;
		}

		int times = 0;
		while (times < 1)
		{
			JFormattedTextField countOfRuns = new JFormattedTextField(NumberFormat.getInstance());
			JComboBox<Class<?>> algorithms = new JComboBox(Utils.getAllTestClasses().toArray());

			Object[] objects = {
					"How many times do you want to run?",
					countOfRuns,
					"Select algorithm class:",
					algorithms
			};

			int result = JOptionPane.showConfirmDialog(appMainWindow, objects, "Settings", JOptionPane.OK_CANCEL_OPTION);

			if (result == JOptionPane.OK_OPTION)
			{
				try
				{
					times = Integer.valueOf(countOfRuns.getText());
				}
				catch (NumberFormatException e)
				{
					JOptionPane.showMessageDialog(appMainWindow, "You must enter an integer greater than 0!", "Error.", JOptionPane.ERROR_MESSAGE);
					break;
				}

				Thread t = new Thread(new DoSnarkAlgorithm(times, (Class<?>) algorithms.getSelectedItem()));
				t.start();
			}
			else
			{
				return;
			}
		}


	}

	private void doDotProduct ()
	{
		if (jTable.getSelectedRowCount() != 2)
		{
			JOptionPane.showMessageDialog(appMainWindow, "Please select two graphs.", "Error!", JOptionPane.ERROR_MESSAGE);
			return;
		}

		JComboBox<SupportedFormats> jcbSupportedFormats = new JComboBox<SupportedFormats>(SupportedFormats.values());

		Object[] objects = {
				"Select format to save dot-products:", jcbSupportedFormats
		};

		if (JOptionPane.showConfirmDialog(appMainWindow, objects, "Options", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION)
		{
			JFileChooser jfc = new JFileChooser();
			jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);

			if (jfc.showOpenDialog(appMainWindow) == JFileChooser.APPROVE_OPTION)
			{
				Graph g1 = graphList.get(jTable.getSelectedRows()[0]);
				Graph g2 = graphList.get(jTable.getSelectedRows()[1]);


				Thread t = new Thread(new DoDotProducts(g1, g2, (SupportedFormats) jcbSupportedFormats.getSelectedItem(), jfc.getSelectedFile()));
				t.start();
			}
		}
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

	private class AlActionTest implements ActionListener
	{
		private final SnarkTestTypes testType;

		public AlActionTest (SnarkTestTypes type)
		{
			this.testType = type;
		}

		@Override
		public void actionPerformed (ActionEvent e)
		{
			switch (testType)
			{
				case ALGORITHM_COMPARATION:
					doSnarkAlgorithmCompare();
					break;
				case ALL_ALGORITHMS:
					doSnarkAllAlgorithms();
					break;
				case ONE_ALGORITHM:
					doSnarkOneAlgorithm();
					break;
			}
		}
	}

	// ---------------------------------------------------
	// Products menu listeners
	// ---------------------------------------------------

	private class AlJmiActionsProductsDot implements ActionListener
	{
		@Override
		public void actionPerformed (ActionEvent e)
		{
			doDotProduct();
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

			JLabel formatedLabel = new JLabel(Utils.getLicenseInfo(true));
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

			JLabel formatedLabel = new JLabel(Utils.getAuthorInfo(true));
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
			List<Graph<Integer>> graphsForTest = new LinkedList<Graph<Integer>>();
			for (int i : jTable.getSelectedRows())
			{
				graphsForTest.add(graphList.get(i));
			}

			jlbProces.setVisible(true);

			LOGGER.info("Create builder");
			RunnerBuilder builder = new RunnerBuilder();
			builder.setLoops(times);
			builder.setTestType(SnarkTestTypes.ALGORITHM_COMPARATION);
			builder.setGraphs(graphsForTest);

			Map<String, Object> resultValues = null;
			try
			{
				resultValues = builder.build().run();
			}
			catch (RunnerException e)
			{
				e.printStackTrace();
			}

			jlbProces.setVisible(false);

			new ResultsWidnow(resultValues);
		}
	}

	private class DoSnarkAllAlgorithms implements Runnable
	{
		private int times;

		public DoSnarkAllAlgorithms (int times)
		{
			this.times = times;
		}

		@Override
		public void run ()
		{

//			LOGGER.info("Start snark all algorithms.");
//			jProgressBar.setValue(0);
//
//			LOGGER.debug(Settings.getInstance().getSetting("packageName"));
//			List<Class<?>> classes = new ArrayList<>(Utils.getAllTestClasses());
//
//			List<Graph<Integer>> graphsForTest = new LinkedList<Graph<Integer>>();
//			for (int i : jTable.getSelectedRows())
//			{
//				graphsForTest.add(graphList.get(i));
//			}
//
//			GraphTestResult[][][] results = new GraphTestResult[times][graphsForTest.size()][classes.size()];
//
//			for (int run = 0; run < times; run++)
//			{
//				for (int i = 0; i < graphsForTest.size(); i++)
//				{
//					Graph<Integer> g = graphsForTest.get(i);
//
//					for (int j = 0; j < classes.size(); j++)
//					{
//						try
//						{
//							GraphTest graphTest = (GraphTest) classes.get(j).newInstance();
//							graphTest.init(g);
//							// TODO: 9.12.2016 -- doriesit co s dlho trvajucim vypoctom
//							LOGGER.debug("Start executor service");
//							ExecutorService executorService = Executors.newFixedThreadPool(1);
//							LOGGER.debug("submit into executor service");
//							GraphTestResult graphTestResult = (GraphTestResult) executorService.submit(graphTest).get();
//							LOGGER.debug("Shutdown executor service");
//							executorService.shutdown();
//
//							int graphId = graphList.indexOf(g);
//
//							String sout = "Testing run: " + (run + 1) + ", Graph id: " + graphId + ", Result: " + graphTestResult;
//							LOGGER.info(sout);
//
//							jTable.setValueAt(graphTestResult.getValue("timeInSeconds"), graphId, 2);
//							jTable.setValueAt(run + "/" + times, graphId, 3);
//
//							results[run][i][j] = graphTestResult;
//						}
//						catch (InstantiationException e)
//						{
//							e.printStackTrace();
//						}
//						catch (IllegalAccessException e)
//						{
//							e.printStackTrace();
//						}
//						catch (InterruptedException e)
//						{
//							e.printStackTrace();
//						}
//						catch (ExecutionException e)
//						{
//							e.printStackTrace();
//						}
//					}
//				}
//
//				jProgressBar.setValue(100 / times * (run + 1));
//			}
//
//			jProgressBar.setValue(100);
//
//			Map<String, Object> resultValues = new HashMap<String, Object>();
//			resultValues.put("test", "Snark algorithm comparation");
//			resultValues.put("resultsData", results);
//			resultValues.put("times", times);
//
//			new ResultsWidnow(resultValues);

			List<Graph<Integer>> graphsForTest = new LinkedList<Graph<Integer>>();
			for (int i : jTable.getSelectedRows())
			{
				graphsForTest.add(graphList.get(i));
			}

			jlbProces.setVisible(true);

			LOGGER.info("Create builder");
			RunnerBuilder builder = new RunnerBuilder();
			builder.setLoops(times);
			builder.setTestType(SnarkTestTypes.ALL_ALGORITHMS);
			builder.setGraphs(graphsForTest);

			Map<String, Object> resultValues = null;
			try
			{
				resultValues = builder.build().run();
			}
			catch (RunnerException e)
			{
				e.printStackTrace();
			}

			jlbProces.setVisible(false);

			new ResultsWidnow(resultValues);
		}
	}

	private class DoSnarkAlgorithm implements Runnable
	{
		private int times;
		private Class<?> graphTestClass;

		public DoSnarkAlgorithm (int times, Class<?> graphTestClass)
		{
			this.times = times;
			this.graphTestClass = graphTestClass;
		}

		@Override
		public void run ()
		{
//			LOGGER.info("Start snark one algorithm test");
//			jProgressBar.setValue(0);
//
//			GraphTestResult[][] results = new GraphTestResult[times][jTable.getSelectedRows().length];
//
//			for (int i = 0; i < this.times; i++)
//			{
//				List<GraphTest> graphTests = new LinkedList<GraphTest>();
//				for (int row : jTable.getSelectedRows())
//				{
//					try
//					{
//						GraphTest graphTest = (GraphTest) this.graphTestClass.newInstance();
//						graphTest.init(graphList.get(row));
//						graphTests.add(graphTest);
//					}
//					catch (InstantiationException e)
//					{
//						e.printStackTrace();
//					}
//					catch (IllegalAccessException e)
//					{
//						e.printStackTrace();
//					}
//				}
//
//				ForkJoinPool forkJoinPool = new ForkJoinPool(ForkJoinPool.getCommonPoolParallelism());
//
//				for (int j = 0; j < graphTests.size(); j++)
//				{
//					forkJoinPool.execute(graphTests.get(j));
//				}
//
//				boolean running = true;
//
//				while (running)
//				{
//					running = false;
//
//					for (int j = 0; j < graphTests.size(); j++)
//					{
//						GraphTest graphTest = graphTests.get(j);
//						if (graphTest.isDone())
//						{
//							if (results[i][j] == null)
//							{
//								try
//								{
//									GraphTestResult graphTestResult = graphTest.getResult();
//									results[i][j] = graphTestResult;
//
//									int graphId = graphList.indexOf(graphTest.getGraph());
//
//									jTable.setValueAt(graphTestResult.getValue("timeInSeconds"), graphId, 2);
//									jTable.setValueAt((i + 1) + "/" + times, graphId, 3);
//
//									LOGGER.debug("graph id " + graphId + " in " + (i + 1) + " run is done.");
//								}
//								catch (ExecutionException e)
//								{
//									e.printStackTrace();
//								}
//								catch (InterruptedException e)
//								{
//									e.printStackTrace();
//								}
//							}
//						}
//						else
//						{
//							running = true;
//						}
//					}
//				}
//				forkJoinPool.shutdown();
//				jProgressBar.setValue(100 / this.times * (i + 1));
//			}
//
//			jProgressBar.setValue(100);
//
//			Map<String, Object> resultValues = new HashMap<String, Object>();
//			resultValues.put("test", "Snark one algorithm test - " + this.graphTestClass.getSimpleName());
//			resultValues.put("resultsData", results);
//			resultValues.put("times", times);
//
//			new ResultsWidnow(resultValues);

			List<Graph<Integer>> graphsForTest = new LinkedList<Graph<Integer>>();
			for (int i : jTable.getSelectedRows())
			{
				graphsForTest.add(graphList.get(i));
			}

			jlbProces.setVisible(true);

			LOGGER.info("Create builder");
			RunnerBuilder builder = new RunnerBuilder();
			builder.setLoops(times);
			builder.setTestType(SnarkTestTypes.ONE_ALGORITHM);
			builder.setGraphs(graphsForTest);
			builder.setAlgorithmTest(this.graphTestClass);

			Map<String, Object> resultValues = null;
			try
			{
				resultValues = builder.build().run();
			}
			catch (RunnerException e)
			{
				e.printStackTrace();
			}

			jlbProces.setVisible(false);

			new ResultsWidnow(resultValues);
		}
	}

	private class DoDotProducts implements Runnable
	{
		private Graph g1;
		private Graph g2;
		private SupportedFormats format;
		private File file;

		public DoDotProducts (Graph g1, Graph g2, SupportedFormats format, File file)
		{
			this.g1 = g1;
			this.g2 = g2;
			this.format = format;
			this.file = file;
		}

		@Override
		public void run ()
		{
			jProgressBar.setValue(10);
			List<Graph<Integer>> dotProducts = GraphUtils.createAllDotProducts(this.g1, this.g2);
			jProgressBar.setValue(80);
			try
			{
				switch (this.format)
				{
					case GRAPH6:
						GraphSaver.graphsToGraph6Format(dotProducts, this.file);
						break;
					case GRAPHML:
						int i = 1;
						for (Graph<Integer> g : dotProducts)
						{
							File fileToSave = new File(this.file.getAbsolutePath() + "" + (i++));
							GraphSaver.graphToGraphMl(g, this.file);
						}
						break;
					case BRATISLAVA_TEXT_CATALOG:
					default:
						GraphSaver.graphsToTextCatalog(dotProducts, this.file);
						break;
				}
			}
			catch (GraphInputOutputException e)
			{
				JOptionPane.showMessageDialog(appMainWindow, e.getMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
			}

			jProgressBar.setValue(1);
			JOptionPane.showMessageDialog(appMainWindow, "Everything is OK. Dot-products were saved.", "Success", JOptionPane.INFORMATION_MESSAGE);
		}
	}
}
