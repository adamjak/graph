package net.adamjak.thomas.graph.application.gui;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

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

	// --------------------------------------------------
	// Components
	// --------------------------------------------------
	private JPanel rootPanel;
	private JScrollPane jScrollPane;
	private JTable jTable;

	public AppMainWindow ()
	{
		super(WINDOW_TITLE);

		setIconImage(new ImageIcon(getClass().getClassLoader().getResource("logo.png")).getImage());

		setContentPane(rootPanel);
		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void createUIComponents ()
	{
		this.jTable = this.getTable();
	}

	private JTable getTable ()
	{
		String[] columnNames = {"ID",
				"Count of vertexes",
				"Snark",
				"Time",
				"Algorithm name"};

		Object[][] data = new Object[1][5];
		return new JTable(data, columnNames);
	}
}
