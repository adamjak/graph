package net.adamjak.thomas.graph.library.utils;

import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * Created by Tomas Adamjak on 29.7.2016.
 * Copyright 2016, Tomas Adamjak
 * License: The BSD 3-Clause License
 */
public class ClassFinderTest
{
	@Test
	public void findAnnotatedClassesTest () throws Exception
	{
		ClassFinder classFinder = new ClassFinder();

		Set<Class<?>> foundClasses = classFinder.findAnnotatedClasses(new LinkedHashSet<String>(Arrays.asList(this.getClass().getPackage().getName())), TestAnnotation.class);

		assertEquals(1,foundClasses.size());
		assertEquals(AnnotatedClassFroTest.class, foundClasses.toArray()[0]);
	}

	@Test
	public void findClassesTest () throws Exception
	{
		ClassFinder classFinder = new ClassFinder();
		for (Class<?> c : classFinder.findClasses(new LinkedHashSet<String>(Arrays.asList(this.getClass().getPackage().getName()))))
		{
			System.out.println(c.getName());
		}
	}
}