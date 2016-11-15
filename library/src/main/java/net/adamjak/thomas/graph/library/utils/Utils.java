package net.adamjak.thomas.graph.library.utils;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

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
	public static int countCharInString (String string, Character character) throws IllegalArgumentException
	{
		if (character == null) throw new IllegalArgumentException("Second param (Character) can't be null.");

		if (string == null || string.isEmpty())
		{
			return 0;
		}

		int count = 0;
		for (char c : string.toCharArray())
		{
			if (c == character)
			{
				count++;
			}
		}

		return count;
	}

	/**
	 * @param clazz tested class
	 * @param findInterface Interface whitch find
	 * @return {@code true} - if clazz implements findInterface, <br />{@code false} - anythink else
	 * @throws IllegalArgumentException if params are {@code null} or param {@code findInterface} is not an interface.
	 */
	public static boolean implementsInterface (Class<?> clazz, Class<?> findInterface) throws IllegalArgumentException
	{
		if (clazz == null || findInterface == null) throw new IllegalArgumentException("Params can not be null");
		if (findInterface.isInterface() == false) throw new IllegalArgumentException("Param findInterface have to be interface.");

		for (Class<?> c : clazz.getInterfaces())
		{
			if (c.equals(findInterface)) return true;
		}

		return false;
	}

	/**
	 * @param folder folder whitch we want to scan
	 * @return {@link List}&lt;{@link File}&gt; - get all files in inserted folder and subfolders
	 * @throws IllegalArgumentException if param {@code folder} is {@code null} or if is not a folder
	 */
	public static List<File> getListofFilesInFolder (File folder) throws IllegalArgumentException
	{
		if (folder == null) throw new IllegalArgumentException("Param can not be null.");
		if (folder.isDirectory() == false) throw new IllegalArgumentException("Inserted file is not a folder.");

		List<File> files = new LinkedList<File>();

		for (File f : folder.listFiles())
		{
			if (f.isFile())
			{
				files.add(f);
			}
			else if (f.isDirectory())
			{
				files.addAll(getListofFilesInFolder(f));
			}
		}

		return files;
	}
}
