package net.adamjak.thomas.graph.formatconverter.gui;

import net.adamjak.thomas.graph.library.api.Graph;
import net.adamjak.thomas.graph.library.io.GraphFactory;
import net.adamjak.thomas.graph.library.io.GraphInputOutputException;
import net.adamjak.thomas.graph.library.io.GraphSaver;
import net.adamjak.thomas.graph.library.io.SupportedFormats;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Tomas Adamjak on 20.11.2016.
 * Copyright 2016, Tomas Adamjak
 * License: The BSD 3-Clause License
 */
public class MainWindow extends JFrame
{
	// --------------------------------------------------
	// Constants
	// --------------------------------------------------
	private final static String WINDOW_TITLE = "Simple graph format convertor";
	private final static String ERROR_DIALOG_TITLE = "Error!";
	private final static String OK_DIALOG_TITLE = "OK!";
	private final static JFileChooser J_FILE_CHOOSER = new JFileChooser();

	// --------------------------------------------------
	// Components
	// --------------------------------------------------
	private JButton selectGraphButton;
	private JTextField tfInputGraph;
	private JButton selectLocationButton;
	private JTextField tfNewGraphLocation;
	private JButton runButton;
	private JPanel rootPanel;
	private JComboBox jcbOutputFormat;
	private JProgressBar jpbProces;

	// --------------------------------------------------
	// Other
	// --------------------------------------------------
	private MainWindow mainWindow;

	public MainWindow ()
	{
		super(WINDOW_TITLE);

		mainWindow = this;

		ImageIcon imageIcon = new ImageIcon(getClass().getClassLoader().getResource("logo.png"));
		setIconImage(imageIcon.getImage());

		setContentPane(rootPanel);
		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		selectGraphButton.addActionListener(new AlSelectGraphButton());
		selectLocationButton.addActionListener(new AlSelectLocationButton());
		runButton.addActionListener(new AlRunButton());

	}

	private void createUIComponents ()
	{
		jcbOutputFormat = new JComboBox(SupportedFormats.values());
	}

	private class AlSelectGraphButton implements ActionListener
	{
		@Override
		public void actionPerformed (ActionEvent e)
		{
			J_FILE_CHOOSER.setMultiSelectionEnabled(true);
			int status = J_FILE_CHOOSER.showOpenDialog(mainWindow);
			if (status == JFileChooser.APPROVE_OPTION)
			{
				File[] files = J_FILE_CHOOSER.getSelectedFiles();
				for (int i = 0; i < files.length; i++)
				{
					if (i == 0)
					{
						tfInputGraph.setText(files[i].getAbsolutePath());
					}
					else
					{
						tfInputGraph.setText(tfInputGraph.getText() + ";" + files[i].getAbsolutePath());
					}
				}
			}
			J_FILE_CHOOSER.setMultiSelectionEnabled(false);
		}
	}

	private class AlSelectLocationButton implements ActionListener
	{
		@Override
		public void actionPerformed (ActionEvent e)
		{
			J_FILE_CHOOSER.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int status = J_FILE_CHOOSER.showOpenDialog(mainWindow);
			if (status == JFileChooser.APPROVE_OPTION)
			{
				tfNewGraphLocation.setText(J_FILE_CHOOSER.getSelectedFile().getAbsolutePath());
			}
		}
	}

	private class AlRunButton implements ActionListener
	{
		@Override
		public void actionPerformed (ActionEvent event)
		{
			boolean saveIntoOneFile = false;
			jpbProces.setValue(0);
			List<File> inputFiles = new LinkedList<File>();

			for (String path : tfInputGraph.getText().split(";"))
			{
				File f = new File(path);
				if (f.isDirectory())
				{
					JOptionPane.showMessageDialog(mainWindow, "Input file can not be a directory.\nFile:" + f.getAbsolutePath(), ERROR_DIALOG_TITLE, JOptionPane.ERROR_MESSAGE);
					return;
				}
				inputFiles.add(f);
			}

			File outputDirectory = new File(tfNewGraphLocation.getText());

			if (outputDirectory.isDirectory() == false)
			{
				JOptionPane.showMessageDialog(mainWindow, "Output location has to be directory.", ERROR_DIALOG_TITLE, JOptionPane.ERROR_MESSAGE);
				return;
			}

			SupportedFormats selectedFormat = (SupportedFormats) jcbOutputFormat.getSelectedItem();

			if (inputFiles.size() > 1 && selectedFormat != SupportedFormats.GRAPHML)
			{
				Object[] options = {"Save into one file", "Save file separately"};
				int choice = JOptionPane.showOptionDialog(mainWindow, //Component parentComponent
														  "Would you like save all graphs into 1 file?", //Object message,
														  "Choose a save operation", //String title
														  JOptionPane.YES_NO_OPTION, //int optionType
														  JOptionPane.QUESTION_MESSAGE, //int messageType
														  null, //Icon icon,
														  options, //Object[] options,
														  options[0]);//Object initialValue
				if (choice == 0)
				{
					saveIntoOneFile = true;
				}
			}

			List<Graph<Integer>> allGraphs = new LinkedList<Graph<Integer>>();

			int k = 1;
			for (File f : inputFiles)
			{
				SupportedFormats inputFileFormat = GraphFactory.getFileFormat(f);

				List<Graph<Integer>> graphList = null;

				switch (inputFileFormat)
				{
					case GRAPH6:
						try
						{
							graphList = GraphFactory.createGraphFromGraph6(f);
						}
						catch (IOException e)
						{
							JOptionPane.showMessageDialog(mainWindow, "File read error.\n" + e.getMessage(), ERROR_DIALOG_TITLE, JOptionPane.ERROR_MESSAGE);
							return;
						}
						break;
					case GRAPHML:
						graphList = new LinkedList<Graph<Integer>>();
						graphList.add(GraphFactory.createGraphFromGraphml(f));
						break;
					default:
						try
						{
							graphList = GraphFactory.createGraphFromTextCatalog(f);
						}
						catch (IOException e)
						{
							JOptionPane.showMessageDialog(mainWindow, "File read error.\n" + e.getMessage(), ERROR_DIALOG_TITLE, JOptionPane.ERROR_MESSAGE);
							return;
						}
						break;
				}

				if (saveIntoOneFile == true)
				{
					allGraphs.addAll(graphList);
					jpbProces.setValue(50 / inputFiles.size() * k);
				}
				else
				{
					String newFileName;
					switch (selectedFormat)
					{
						case GRAPH6:
							newFileName = outputDirectory.getAbsolutePath() + File.separator + f.getName() + "_convert.g6";
							try
							{
								GraphSaver.graphsToGraph6Format(graphList, new File(newFileName));
							}
							catch (GraphInputOutputException e)
							{
								JOptionPane.showMessageDialog(mainWindow, "File saving error.\n" + e.getMessage(), ERROR_DIALOG_TITLE, JOptionPane.ERROR_MESSAGE);
								return;
							}
							break;
						case BRATISLAVA_TEXT_CATALOG:
							int graphVertexes = 0;
							for (Graph g : graphList)
							{
								graphVertexes += g.getCountOfVertexes();
							}
							graphVertexes = graphVertexes / graphList.size();
							newFileName = outputDirectory.getAbsolutePath() + File.separator + f.getName() + "_convert." + graphVertexes;
							try
							{
								GraphSaver.graphsToTextCatalog(graphList, new File(newFileName));
							}
							catch (GraphInputOutputException e)
							{
								JOptionPane.showMessageDialog(mainWindow, "File saving error.\n" + e.getMessage(), ERROR_DIALOG_TITLE, JOptionPane.ERROR_MESSAGE);
								return;
							}
							break;
						case GRAPHML:
							try
							{
								for (int i = 0; i < graphList.size(); i++)
								{
									newFileName = outputDirectory.getAbsolutePath() + File.separator + f.getName() + "_convert_" + (i + 1) + ".xml";
									GraphSaver.graphToGraphMl(graphList.get(i), new File(newFileName));

									jpbProces.setValue(50 + ((50 / graphList.size()) * (i + 1)));
								}
							}
							catch (GraphInputOutputException e)
							{
								JOptionPane.showMessageDialog(mainWindow, "File saving error.\n" + e.getMessage(), ERROR_DIALOG_TITLE, JOptionPane.ERROR_MESSAGE);
								return;
							}
							break;
					}

					jpbProces.setValue(100 / inputFiles.size() * k);
				}

				k++;
			}

			if (saveIntoOneFile == true)
			{

				String newFileName = outputDirectory.getAbsolutePath() + File.separator + "all_graphs_convert";

				switch (selectedFormat)
				{
					case GRAPH6:
						newFileName += ".g6";
						try
						{
							GraphSaver.graphsToGraph6Format(allGraphs, new File(newFileName));
							jpbProces.setValue(100);
							JOptionPane.showMessageDialog(mainWindow, "Everythink done.", OK_DIALOG_TITLE, JOptionPane.INFORMATION_MESSAGE);
						}
						catch (GraphInputOutputException e)
						{
							JOptionPane.showMessageDialog(mainWindow, "File saving error.\n" + e.getMessage(), ERROR_DIALOG_TITLE, JOptionPane.ERROR_MESSAGE);
							return;
						}
						break;
					case BRATISLAVA_TEXT_CATALOG:
						newFileName += ".BAGRAPH";
						try
						{
							GraphSaver.graphsToTextCatalog(allGraphs, new File(newFileName));
							jpbProces.setValue(100);
							JOptionPane.showMessageDialog(mainWindow, "Everythink done.", OK_DIALOG_TITLE, JOptionPane.INFORMATION_MESSAGE);
						}
						catch (GraphInputOutputException e)
						{
							JOptionPane.showMessageDialog(mainWindow, "File saving error.\n" + e.getMessage(), ERROR_DIALOG_TITLE, JOptionPane.ERROR_MESSAGE);
							return;
						}
						break;
				}
			}
			else
			{
				jpbProces.setValue(100);
				JOptionPane.showMessageDialog(mainWindow, "Everythink done.", OK_DIALOG_TITLE, JOptionPane.INFORMATION_MESSAGE);
			}

		}
	}
}
