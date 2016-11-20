package net.adamjak.thomas.graph.formatconverter.gui;

import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 * Created by Tomas Adamjak on 20.11.2016.
 * Copyright 2016, Tomas Adamjak
 * License: The BSD 3-Clause License
 */
public class GraphFileFilter extends FileFilter
{

	/**
	 * Whether the given file is accepted by this filter.
	 *
	 * @param f
	 */
	@Override
	public boolean accept (File f)
	{
		if (f.isDirectory())
		{
			return true;
		}

		String[] name = f.getName().split("\\.");

		String extension = name[name.length - 1];
		extension = extension.toLowerCase();

		if (extension.equals("xml"))
		{
			return true;
		}
		else if (extension.equals("g6"))
		{
			return true;
		}
		else
		{
			try
			{
				Integer.valueOf(extension);
				return true;
			}
			catch (NumberFormatException e) {}
		}

		return false;
	}

	/**
	 * The description of this filter.
	 */
	@Override
	public String getDescription ()
	{
		return "Graph6 format, Text catalog from Bratislava, GraphML";
	}
}
