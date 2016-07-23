package net.adamjak.graph.utils;

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

	public Set<Class<?>> findAnnotatedClasses(String packageName, boolean includeSubpackage, Class<? extends Annotation>... annotation)
	{
		Set<Class<?>> classes = findClasses(packageName, includeSubpackage);
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

	public Set<Class<?>> findClasses(String packageName, boolean includeSubpackage)
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

		Set<Class<?>> output = new LinkedHashSet<Class<?>>();
		String packageSearchPattern = packageName.replace(ClassFinder.DOT,File.separator);

		int maxDots = Utils.countCharInString(packageName, '.') + 1;

		for(String classFileName : classNameSet)
		{
			int startIndex = classFileName.lastIndexOf(packageSearchPattern);

			if(startIndex >= 0)
			{
				final int endIndex = classFileName.length() - ClassFinder.CLASS_SUFFIX.length();
				String className = classFileName.substring(startIndex,endIndex).replace(File.separator, ClassFinder.DOT);

				if(includeSubpackage || maxDots>= Utils.countCharInString(className, '.'))
				{
					Class<?> cls;
					try
					{
						cls = this.classLoader.loadClass(className);
						output.add(cls);
					}
					catch (ClassNotFoundException ex) {}
				}
			}
		}
		return output;

	}

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

	private String changePackageNameToPath(String value)
	{
		if (value == null)
		{
			return null;
		}

		return value.replace(ClassFinder.DOT, File.separator);
	}
}
