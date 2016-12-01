package net.adamjak.thomas.graph.application.commons;

import javax.swing.JTable;
import javax.validation.constraints.NotNull;

/**
 * Created by Tomas Adamjak on 24.11.2016.
 * Copyright 2016, Tomas Adamjak
 * License: The BSD 3-Clause License
 */
public class Utils
{
	/**
	 * @return Return <b>{@code true}</b> if integer is in array else return <b>{@code false}</b>.
	 */
	public static boolean isIntInArray (int integer, int[] array)
	{
		for (int i : array)
		{
			if (integer == i)
			{
				return true;
			}
		}

		return false;
	}

	/**
	 * @param jTable JTable with data
	 * @return Return {@link Object} two-dimensional array with {@link JTable} data. First dimension are rows and second are columns.
	 * @throws IllegalArgumentException if {@code jTable} param is {@code null}.
	 */
	public static Object[][] getDataFromJTable (@NotNull JTable jTable) throws IllegalArgumentException
	{
		if (jTable == null) throw new IllegalArgumentException("JTable parame can not be null!");

		Object[][] data = new Object[jTable.getRowCount()][jTable.getColumnCount()];

		for (int row = 0; row < jTable.getRowCount(); row++)
		{
			for (int column = 0; column < jTable.getColumnCount(); column++)
			{
				data[row][column] = jTable.getValueAt(row,column);
			}
		}

		return data;
	}
}
