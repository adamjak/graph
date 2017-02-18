package net.adamjak.thomas.graph.application.commons;

import net.adamjak.thomas.graph.library.interfaces.anot.Benchmarked;
import net.adamjak.thomas.graph.library.tests.GraphTest;
import net.adamjak.thomas.graph.library.utils.ClassFinder;

import javax.swing.JTable;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

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

	/**
	 * @return Return {@link Set} of {@link Class}es whitch extends from {@link GraphTest} and has {@link Benchmarked} annotation.
	 */
	public static Set<Class<?>> getAllTestClasses ()
	{
		ClassFinder classFinder = new ClassFinder();
		Set<String> packageNames = new LinkedHashSet<String>();
		packageNames.add(Settings.getInstance().getSetting("packageName"));
		return classFinder.findClassesWhitchExtends(classFinder.findAnnotatedClasses(packageNames, Benchmarked.class), GraphTest.class);
	}

	/**
	 * @param collection collection with classes
	 * @return String array with simple names of classes from collection
	 * @throws IllegalArgumentException if collection param is null
	 */
	public static String[] getSimpleNamesOfClassesFromCollection (Collection<Class<?>> collection) throws IllegalArgumentException
	{
		if (collection == null) throw new IllegalArgumentException("Param collection can not be null!");
		Class<?>[] clsArr = collection.toArray(new Class[1]);
		String[] names = new String[clsArr.length];

		for (int i = 0; i < clsArr.length; i++)
		{
			names[i] = clsArr[i].getSimpleName();
		}

		return names;
	}

	/**
	 * @param name simple name of test class
	 * @return Test {@link Class} by simple name. If name is not equals with class name return {@code null}.
	 * @throws IllegalArgumentException if name param is null
	 */
	public static Class<?> getTestClassByName (String name) throws IllegalArgumentException
	{
		if (name == null) throw new IllegalArgumentException("Param name can not be null.");

		for (Class<?> c : Utils.getAllTestClasses())
		{
			if (c.getSimpleName().equals(name)) return c;
		}

		return null;
	}

	/**
	 * @param html is is true in returned String will be HTML tags
	 * @return {@link String} with licence information.
	 */
	public static String getLicenseInfo (boolean html)
	{
		String message;

		if (html == true)
		{
			message = "<html>" +
					"<h2>Copyright (c) 2016, Tomáš Adamják</h2>" +
					"<p><strong>All rights reserved.</strong></p>" +
					"<p>Redistribution and use in source and binary forms, with or without modification,<br />" +
					"are permitted provided that the following conditions are met:</p>" +
					"<ul>" +
					"<li>Redistributions of source code must retain the above copyright notice,<br />" +
					"this list of conditions and the following disclaimer.</li>" +
					"<li>Redistributions in binary form must reproduce the above copyright notice,<br />" +
					"this list of conditions and the following disclaimer in the documentation<br />" +
					"and/or other materials provided with the distribution.</li>" +
					"<li>Neither the name of the <organization> nor the names of its contributors may be<br />" +
					"used to endorse or promote products derived from this software without specific prior written permission.</li>" +
					"</ul>" +
					"<p>THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS \"AS IS\" AND<br />" +
					"ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED<br />" +
					"WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE<br />" +
					"DISCLAIMED. IN NO EVENT SHALL Tomáš Adamják BE LIABLE FOR ANY<br />" +
					"DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES<br />" +
					"(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;<br />" +
					"LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND<br />" +
					"ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT<br />" +
					"(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS<br />" +
					"SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.</p>";
		}
		else
		{
			message = "Copyright (c) 2016, Tomáš Adamják\n" +
					"=================================\n\n" +
					"The BSD 3-Clause License\n" +
					"------------------------\n" +
					"All rights reserved.\n" +
					"Redistribution and use in source and binary forms, with or without modification,\n" +
					"are permitted provided that the following conditions are met:\n" +
					" * Redistributions of source code must retain the above copyright notice,\n" +
					"   this list of conditions and the following disclaimer.\n" +
					" * Redistributions in binary form must reproduce the above copyright notice,\n" +
					"   this list of conditions and the following disclaimer in the documentation\n" +
					"   and/or other materials provided with the distribution.\n" +
					" * Neither the name of the <organization> nor the names of its contributors may be\n" +
					"   used to endorse or promote products derived from this software without specific\n" +
					"   prior written permission.\n" +
					"\n" +
					"THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS \"AS IS\" AND\n" +
					"ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED\n" +
					"WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE\n" +
					"DISCLAIMED. IN NO EVENT SHALL Tomáš Adamják BE LIABLE FOR ANY\n" +
					"DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES\n" +
					"(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;\n" +
					"LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND\n" +
					"ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT\n" +
					"(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS\n" +
					"SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.";
		}

		return message;
	}

	/**
	 * @param html is is true in returned String will be HTML tags
	 * @return {@link String} with author information.
	 */
	public static String getAuthorInfo (boolean html)
	{
		String message;

		if (html == true)
		{
			message = "<html>" +
					"<h2>Tomáš Adamják</h2>" +
					"<p><strong>E-mail:</strong> <code>thomas@adamjak.net</code></p>" +
					"<p><strong>Web:</strong> <code>http://thomas.adamjak.net</code></p>" +
					"<html>";
		}
		else
		{
			message = "Tomáš Adamják\n" +
					"=============\n" +
					"E-mail: thomas@adamjak.net\n" +
					"Web:    http://thomas.adamjak.net";
		}

		return message;
	}

	/**
	 * Create one line string from array
	 *
	 * @param array     array with values
	 * @param separator String whitch will be separate values
	 * @param <T>       type of array
	 * @return One line string from array.
	 * @throws IllegalArgumentException if some param is null
	 */
	public static <T> String arrayToLineString (T[] array, String separator) throws IllegalArgumentException
	{
		if (array == null || separator == null) throw new IllegalArgumentException("Parameters can not be null!");

		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < array.length; i++)
		{
			if (i > 0) sb.append(separator);
			sb.append(array[i]);
		}

		return sb.toString();
	}
}
