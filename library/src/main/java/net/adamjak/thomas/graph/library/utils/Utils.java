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
	 * @param string Text in which we want to calculate count of inserted characters
	 * @param character Character whitch we search in text
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
	 * @return {@code true} - if clazz implements findInterface, <br>{@code false} - anythink else
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
	public static List<File> getListOfFilesInFolder (File folder) throws IllegalArgumentException
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
				files.addAll(getListOfFilesInFolder(f));
			}
		}

		return files;
	}

	public static class Pair<T>
	{
		private final T first;
		private final T second;
		private final Class<?> type;

		public Pair (T first, T second)
		{
			this.first = first;
			this.second = second;
			this.type = first.getClass();
		}

		public T getFirst ()
		{
			return first;
		}

		public T getSecond ()
		{
			return second;
		}

		/**
		 * Check if inserted element is in pair.
		 *
		 * @param element for check
		 * @return Return {@code true} if one of pair elements equal with inserted element else return {@code false}.
		 * @throws IllegalArgumentException if inserted element is {@code null}
		 */
		public boolean isInPair (T element) throws IllegalArgumentException
		{
			if (element == null) throw new IllegalArgumentException("Element param can not be null");

			if (this.first.equals(element) || this.second.equals(element))
			{
				return true;
			}
			else
			{
				return false;
			}
		}

		@Override
		public boolean equals (Object obj)
		{
			if (obj == null) return false;
			if (obj instanceof Pair == false) return false;

			Pair<T> other = (Pair<T>) obj;

			if (this.getFirst().equals(other.getFirst()) && this.getSecond().equals(other.getSecond())) return true;
			if (this.getFirst().equals(other.getSecond()) && this.getSecond().equals(other.getFirst())) return true;

			return false;
		}

		@Override
		public int hashCode ()
		{
			return this.first.hashCode() + this.second.hashCode() + 97;
		}

		@Override
		public String toString ()
		{
			return "Pair of " + this.type.getSimpleName() + " first: " + this.first + " second: " + this.second;
		}
	}

	/**
	 * Convert int array into char array;
	 * <p>Example:<br>
	 *     <code>
	 *         int[] {1,2,3} =&gt; char[] {'1','2','3'}<br>
	 *         int[] {11,22,33} =&gt; char[] {'1','2','3'}
	 *     </code>
	 *
	 * @param array int array to convert
	 * @return Return new char array with elements from int array
	 */
	public static char[] intArrayToCharArray (int[] array)
	{
		char[] newArray = new char[array.length];

		for (int i = 0; i < array.length; i++)
		{
			newArray[i] = String.valueOf(array[i]).charAt(0);
		}

		return newArray;
	}

	/**
	 * Convert millisecond into nanosecond.
	 *
	 * @param ms millisecond value
	 * @return Return nanosecond from inserted millisecond.
	 */
	public static long msToNs (double ms)
	{
		return Double.valueOf(ms * 1000000).longValue();
	}
}
