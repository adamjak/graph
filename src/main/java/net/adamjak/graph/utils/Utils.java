package net.adamjak.graph.utils;

/**
 * Created by Tomas Adamjak on 17.7.2016.
 * Copyright 2016, Tomas Adamjak
 * License: The BSD 3-Clause License
 */
public class Utils
{
	/**
	 * Determine the number of specific characters in the string.
	 *
	 * @return Number of specific characters in the string.
	 * @throws IllegalArgumentException if second param {@code character} is {@code null}.
	 */
	public static int countCharInString(String string, Character character) throws IllegalArgumentException
	{
		if (character == null) throw new IllegalArgumentException("Second param (Character) can't be null.");

		if(string == null || string.isEmpty())
		{
			return 0;
		}

		int count = 0;
		for(char c: string.toCharArray())
		{
			if(c == character)
			{
				count++;
			}
		}

		return count;
	}
}
