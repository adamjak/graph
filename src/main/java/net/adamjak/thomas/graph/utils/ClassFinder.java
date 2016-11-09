package net.adamjak.thomas.graph.utils;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by Tomas Adamjak on 17.7.2016.
 * Copyright 2016, Tomas Adamjak
 * License: The BSD 3-Clause License
 */
public class ClassFinder
{
	private static final String FILE_PROTOCOL = "file";
	private static final String CLASS_SUFFIX = ".class";
	private static final String DOT = ".";

	private final ClassLoader classLoader;

	public ClassFinder ()
	{
		this.classLoader = Thread.currentThread().getContextClassLoader();
	}

	public ClassFinder (ClassLoader classLoader)
	{
		this.classLoader = classLoader;
	}

	/**
	 * @param classes {@link Set} of classes for search
	 * @param extendedClass
	 * @return Return only classes whitch extends class from param {@code extendedClass}.
	 */
	public Set<Class<?>> findClassesWhitchExtends (Set<Class<?>> classes, Class<?> extendedClass)
	{
		if (classes == null || extendedClass == null) throw new IllegalArgumentException("Params can not be null.");

		Set<Class<?>> classesWhitchExtends = new LinkedHashSet<Class<?>>();

		for (Class<?> c :classes)
		{
			if (c.getSuperclass().equals(extendedClass))
			{
				classesWhitchExtends.add(c);
			}
		}

		return classesWhitchExtends;
	}

	/**
	 * Find all annoted classes from package.
	 *
	 * @param packageNames {@link Set} of package name with annoted classes
	 * @param includeSubpackage set if method can find in subpackages
	 * @param annotation find annotation
	 * @return {@link Set}&lt;{@link Class}&lt;?&gt;&gt; of annoted classes.
	 */
	public Set<Class<?>> findAnnotatedClasses(Set<String> packageNames, boolean includeSubpackage, Class<? extends Annotation>... annotation)
	{
		Set<Class<?>> classes = findClasses(packageNames, includeSubpackage);
		Set<Class<?>> output = new LinkedHashSet<Class<?>>();

		if(classes == null || classes.isEmpty() || (annotation == null || annotation.length == 0))
		{
			return output;
		}

		for(Class<?> c : classes)
		{
			for(Class<? extends Annotation> ac : annotation)
			{

				if(c.getAnnotationsByType(ac).length > 0)
				{
					output.add(c);
					break;
				}
			}
		}
		return output;
	}

	/**
	 * Find all classes in package. If parameter {@code includeSubpackage} is true method find in subpackages too.
	 *
	 * @return {@link Set}&lt;{@link Class}&lt;?&gt;&gt; in package.
	 */
	public Set<Class<?>> findClasses(Set<String> packageNames, boolean includeSubpackage)
	{
		Set<Class<?>> output = new LinkedHashSet<Class<?>>();

		for (String packageName : packageNames)
		{
			String sourceName = this.changePackageNameToPath(packageName);
			Set<String> classNameSet = new LinkedHashSet<String>();

			Enumeration<URL> resources = null;
			try
			{
				resources = this.classLoader.getResources(sourceName);
			}
			catch (IOException e)
			{
				return new LinkedHashSet<>();
			}

			while (resources.hasMoreElements())
			{
				URL resUrl = resources.nextElement();
				if (ClassFinder.FILE_PROTOCOL.equals(resUrl.getProtocol()))
				{
					File file = new File(resUrl.getFile());
					classNameSet.addAll(this.findClassFileNames(file));
				}
			}

			String packageSearchPattern = packageName.replace(ClassFinder.DOT, File.separator);

			int maxDots = Utils.countCharInString(packageName, '.') + 1;

			for (String classFileName : classNameSet)
			{
				int startIndex = classFileName.lastIndexOf(packageSearchPattern);

				if (startIndex >= 0)
				{
					final int endIndex = classFileName.length() - ClassFinder.CLASS_SUFFIX.length();
					String className = classFileName.substring(startIndex, endIndex).replace(File.separator, ClassFinder.DOT);

					if (includeSubpackage || maxDots >= Utils.countCharInString(className, '.'))
					{
						Class<?> cls;
						try
						{
							cls = this.classLoader.loadClass(className);
							output.add(cls);
						}
						catch (ClassNotFoundException ex)
						{
						}
					}
				}
			}
		}
		return output;

	}

	/**
	 * Method get all class paths from file. If file is directory, method find in subdirectories too.
	 *
	 * @return {@link Set}&lt;{@link String}&gt; with class paths.
	 */
	private Set<String> findClassFileNames (File file)
	{
		Set<String> output = new LinkedHashSet<String>();

		if (file == null)
		{
			return output;
		}

		if (file.isDirectory())
		{
			for (File f : file.listFiles())
			{
				output.addAll(this.findClassFileNames(f));
			}
		}
		else
		{
			if (file.getName().endsWith(ClassFinder.CLASS_SUFFIX))
			{
				output.add(file.getPath());
			}
		}

		return output;
	}

	/**
	 * Method convert package name to real system path. Use {@link File#separator}.<br />
	 * Example for Linux:<br />
	 * <em>Input:</em> <code>net.adamjak.thomas</code><br />
	 * <em>Output:</em> <code>net/adamjak/thomas</code>
	 * @param value package name
	 * @return Real system path from package name or if value is {@code null} return {@code null}.
	 */
	private String changePackageNameToPath(String value)
	{
		if (value == null)
		{
			return null;
		}

		return value.replace(ClassFinder.DOT, File.separator);
	}
}
