package net.adamjak.thomas.graph.library.utils;

import com.google.common.reflect.ClassPath;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by Tomas Adamjak on 17.7.2016.
 * Copyright 2016, Tomas Adamjak
 * License: The BSD 3-Clause License
 */
public class ClassFinder
{
	private final ClassLoader classLoader;

	public ClassFinder ()
	{
		this.classLoader = getClass().getClassLoader();
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
	 * Find all annoted classes from package and subpackages.
	 *
	 * @param packageNames {@link Set} of package name with annoted classes
	 * @param annotation find annotation
	 * @return {@link Set}&lt;{@link Class}&lt;?&gt;&gt; of annoted classes.
	 */
	public Set<Class<?>> findAnnotatedClasses (Set<String> packageNames, Class<? extends Annotation>... annotation)
	{
		Set<Class<?>> classes = this.findClasses(packageNames);
		Set<Class<?>> annotatedClasses = new LinkedHashSet<Class<?>>();

		if(classes == null || classes.isEmpty() || (annotation == null || annotation.length == 0))
		{
			return annotatedClasses;
		}

		for(Class<?> c : classes)
		{
			for (Annotation a : c.getAnnotations())
			{
				for (Class<? extends Annotation> ac : annotation)
				{
					if (ac.equals(a.annotationType()))
					{
						annotatedClasses.add(c);
						break;
					}
				}
			}
		}

		return annotatedClasses;
	}

	/**
	 * Find all classes in package and subpackages.
	 *
	 * @return {@link Set}&lt;{@link Class}&lt;?&gt;&gt; found classes.
	 */
	public Set<Class<?>> findClasses (Set<String> packageNames)
	{
		Set<Class<?>> classes = new LinkedHashSet<Class<?>>();

		try
		{
			for (String packageName : packageNames)
			{
				Set<ClassPath.ClassInfo> classesInPackage = ClassPath.from(this.classLoader).getTopLevelClassesRecursive(packageName);
				for (ClassPath.ClassInfo ci : classesInPackage)
				{
					classes.add(ci.load());
				}
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return classes;
	}
}
