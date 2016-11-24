package net.adamjak.thomas.graph.application.commons;

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
}
