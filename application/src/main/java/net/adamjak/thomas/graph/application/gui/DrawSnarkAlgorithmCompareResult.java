package net.adamjak.thomas.graph.application.gui;

import net.adamjak.thomas.graph.library.tests.GraphTestResult;

import javax.swing.JPanel;

/**
 * Created by Tomas Adamjak on 3.12.2016.
 * Copyright 2016, Tomas Adamjak
 * License: The BSD 3-Clause License
 */
public class DrawSnarkAlgorithmCompareResult extends JPanel
{
	private GraphTestResult[][] data;

	public DrawSnarkAlgorithmCompareResult (GraphTestResult[][] resultsToDraw)
	{
		this.data = resultsToDraw;
	}
}
