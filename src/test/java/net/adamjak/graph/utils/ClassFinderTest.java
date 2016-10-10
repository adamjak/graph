package net.adamjak.graph.utils;

import org.junit.Test;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
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

		Set<Class<?>> foundClasses = classFinder.findAnnotatedClasses(this.getClass().getPackage().getName(), false, TestAnnotation.class);

		assertEquals(1,foundClasses.size());
		assertEquals(AnnotatedClassFroTest.class, foundClasses.toArray()[0]);
	}

	@Test
	public void findClassesTest () throws Exception
	{
		ClassFinder classFinder = new ClassFinder();
		for (Class<?> c : classFinder.findClasses("net.adamjak.graph.utils", false))
		{
			System.out.println(c.getName());
		}
	}


	@TestAnnotation
	public class AnnotatedClassFroTest
	{

	}

	@Retention (RetentionPolicy.RUNTIME)
	public @interface TestAnnotation
	{
	}

}