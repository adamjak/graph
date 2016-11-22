package net.adamjak.thomas.graph.application;

import net.adamjak.thomas.graph.application.gui.AppMainWindow;
import net.adamjak.thomas.graph.library.io.GraphInputOutputException;

import java.io.IOException;

public class Main
{
	public static void main (String args[]) throws IOException, GraphInputOutputException
	{
		AppMainWindow appMainWindow = new AppMainWindow();
		appMainWindow.setVisible(true);
	}
}
