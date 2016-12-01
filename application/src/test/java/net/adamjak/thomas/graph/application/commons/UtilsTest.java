package net.adamjak.thomas.graph.application.commons;

import org.junit.Test;

import javax.swing.JTable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Tomas Adamjak on 24.11.2016.
 * Copyright 2016, Tomas Adamjak
 * License: The BSD 3-Clause License
 */
public class UtilsTest
{
	@Test
	public void isIntInArrayTestPositive () throws Exception
	{
		int i = 2;
		int[] arr = {1, 2, 3};

		assertTrue(Utils.isIntInArray(i, arr));
	}

	@Test
	public void isIntInArrayTestNegative () throws Exception
	{
		int i = 4;
		int[] arr = {1, 2, 3};

		assertFalse(Utils.isIntInArray(i, arr));
	}

	@Test
	public void getDataFromJTableTest() throws Exception
	{
		String[] columnNames = {"First Name",
				"Last Name",
				"Sport",
				"# of Years",
				"Vegetarian"};
		Object[][] data = {
				{"Kathy", "Smith",
						"Snowboarding", new Integer(5), new Boolean(false)},
				{"John", "Doe",
						"Rowing", new Integer(3), new Boolean(true)},
				{"Sue", "Black",
						"Knitting", new Integer(2), new Boolean(false)},
				{"Jane", "White",
						"Speed reading", new Integer(20), new Boolean(true)},
				{"Joe", "Brown",
						"Pool", new Integer(10), new Boolean(false)}
		};
		JTable table = new JTable(data, columnNames);
		Object[][] returnedData = Utils.getDataFromJTable(table);

		for (int row = 0 ; row < data.length; row++)
		{
			for (int column = 0; column < data[row].length; column++)
			{
				assertEquals(data[row][column],returnedData[row][column]);
			}
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void getDataFromJTableTestIllegalArgumentException() throws Exception
	{
		Utils.getDataFromJTable(null);
	}
}
