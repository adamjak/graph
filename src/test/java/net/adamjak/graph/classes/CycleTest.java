package net.adamjak.graph.classes;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Tomas Adamjak on 2.4.2016.
 * Copyright 2016, Tomas Adamjak
 * License: The BSD 3-Clause License
 */
public class CycleTest
{

	@Test
	public void testAddVertexIntoCycle () throws Exception
	{
		// TODO: 2.4.2016
	}

	@Test
	public void testGetCycleElements () throws Exception
	{
		// TODO: 2.4.2016
	}

	@Test
	public void testGetCyrcleSize () throws Exception
	{
		Cycle<String> cycle = new Cycle<String>();
		cycle.addVertexIntoCycle(new VertexImpl<String>("v1"));
		cycle.addVertexIntoCycle(new VertexImpl<String>("v2"));
		int result = cycle.getCyrcleSize();
		int expectedResult = 2;
		assertEquals(expectedResult, result);
	}

	@Test
	public void testHasCommonVertexTrue () throws Exception
	{
		VertexImpl<String> v1 = new VertexImpl<String>("v1");
		VertexImpl<String> v2 = new VertexImpl<String>("v2");
		VertexImpl<String> v3 = new VertexImpl<String>("v3");

		Cycle<String> c1 = new Cycle<String>();
		c1.addVertexIntoCycle(v1);
		c1.addVertexIntoCycle(v2);

		Cycle<String> c2 = new Cycle<String>();
		c2.addVertexIntoCycle(v2);
		c2.addVertexIntoCycle(v3);

		boolean result = c1.hasCommonVertex(c2);
		boolean expectedResult = true;
		assertEquals(expectedResult, result);
	}

	@Test
	public void testHasCommonVertexFalse () throws Exception
	{
		VertexImpl<String> v1 = new VertexImpl<String>("v1");
		VertexImpl<String> v2 = new VertexImpl<String>("v2");
		VertexImpl<String> v3 = new VertexImpl<String>("v3");
		VertexImpl<String> v4 = new VertexImpl<String>("v4");

		Cycle<String> c1 = new Cycle<String>();
		c1.addVertexIntoCycle(v1);
		c1.addVertexIntoCycle(v2);

		Cycle<String> c2 = new Cycle<String>();
		c2.addVertexIntoCycle(v3);
		c2.addVertexIntoCycle(v4);

		boolean result = c1.hasCommonVertex(c2);
		boolean expectedResult = false;
		assertEquals(expectedResult, result);
	}

	@Test
	public void testEquals1 () throws Exception
	{
		VertexImpl<String> v1 = new VertexImpl<String>("v1");
		VertexImpl<String> v2 = new VertexImpl<String>("v2");
		VertexImpl<String> v3 = new VertexImpl<String>("v3");
		VertexImpl<String> v4 = new VertexImpl<String>("v4");

		Cycle<String> c1 = new Cycle<String>();
		c1.addVertexIntoCycle(v1);
		c1.addVertexIntoCycle(v2);

		Cycle<String> c2 = new Cycle<String>();
		c2.addVertexIntoCycle(v3);
		c2.addVertexIntoCycle(v4);

		boolean result = c1.equals(c2);
		boolean expectedResult = false;
		assertEquals(expectedResult, result);
	}

	@Test
	public void testEquals2 () throws Exception
	{
		VertexImpl<String> v1 = new VertexImpl<String>("v1");
		VertexImpl<String> v2 = new VertexImpl<String>("v2");
		VertexImpl<String> v3 = new VertexImpl<String>("v3");

		Cycle<String> c1 = new Cycle<String>();
		c1.addVertexIntoCycle(v1);
		c1.addVertexIntoCycle(v2);

		Cycle<String> c2 = new Cycle<String>();
		c2.addVertexIntoCycle(v3);
		c2.addVertexIntoCycle(v2);

		boolean result = c1.equals(c2);
		boolean expectedResult = false;
		assertEquals(expectedResult, result);
	}

	@Test
	public void testEquals3 () throws Exception
	{
		VertexImpl<String> v1 = new VertexImpl<String>("v1");
		VertexImpl<String> v2 = new VertexImpl<String>("v2");
		VertexImpl<String> v3 = new VertexImpl<String>("v3");
		VertexImpl<String> v4 = new VertexImpl<String>("v4");

		Cycle<String> c1 = new Cycle<String>();
		c1.addVertexIntoCycle(v1);
		c1.addVertexIntoCycle(v2);
		c1.addVertexIntoCycle(v3);
		c1.addVertexIntoCycle(v4);

		Cycle<String> c2 = new Cycle<String>();
		c2.addVertexIntoCycle(v1);
		c2.addVertexIntoCycle(v2);
		c2.addVertexIntoCycle(v4);
		c2.addVertexIntoCycle(v3);

		boolean result = c1.equals(c2);
		boolean expectedResult = false;
		assertEquals(expectedResult, result);
	}

	@Test
	public void testEquals4 () throws Exception
	{
		VertexImpl<String> v1 = new VertexImpl<String>("v1");
		VertexImpl<String> v2 = new VertexImpl<String>("v2");
		VertexImpl<String> v3 = new VertexImpl<String>("v3");
		VertexImpl<String> v4 = new VertexImpl<String>("v4");

		Cycle<String> c1 = new Cycle<String>();
		c1.addVertexIntoCycle(v1);
		c1.addVertexIntoCycle(v2);
		c1.addVertexIntoCycle(v3);
		c1.addVertexIntoCycle(v4);

		Cycle<String> c2 = new Cycle<String>();
		c2.addVertexIntoCycle(v1);
		c2.addVertexIntoCycle(v2);
		c2.addVertexIntoCycle(v3);
		c2.addVertexIntoCycle(v4);

		boolean result = c1.equals(c2);
		boolean expectedResult = true;
		assertEquals(expectedResult, result);
	}

	@Test
	public void testEquals5 () throws Exception
	{
		VertexImpl<String> v1 = new VertexImpl<String>("v1");
		VertexImpl<String> v2 = new VertexImpl<String>("v2");
		VertexImpl<String> v3 = new VertexImpl<String>("v3");
		VertexImpl<String> v4 = new VertexImpl<String>("v4");

		Cycle<String> c1 = new Cycle<String>();
		c1.addVertexIntoCycle(v1);
		c1.addVertexIntoCycle(v2);
		c1.addVertexIntoCycle(v3);
		c1.addVertexIntoCycle(v4);

		Cycle<String> c2 = new Cycle<String>();
		c2.addVertexIntoCycle(v1);
		c2.addVertexIntoCycle(v4);
		c2.addVertexIntoCycle(v3);
		c2.addVertexIntoCycle(v2);

		boolean result = c1.equals(c2);
		boolean expectedResult = true;
		assertEquals(expectedResult, result);
	}

	@Test
	public void testEquals6 () throws Exception
	{
		VertexImpl<String> v1 = new VertexImpl<String>("v1");
		VertexImpl<String> v2 = new VertexImpl<String>("v2");
		VertexImpl<String> v3 = new VertexImpl<String>("v3");
		VertexImpl<String> v4 = new VertexImpl<String>("v4");

		Cycle<String> c1 = new Cycle<String>();
		c1.addVertexIntoCycle(v1);
		c1.addVertexIntoCycle(v2);
		c1.addVertexIntoCycle(v3);
		c1.addVertexIntoCycle(v4);

		Cycle<String> c2 = new Cycle<String>();
		c2.addVertexIntoCycle(v1);
		c2.addVertexIntoCycle(v2);
		c2.addVertexIntoCycle(v3);

		boolean result = c1.equals(c2);
		boolean expectedResult = false;
		assertEquals(expectedResult, result);
	}

	@Test
	public void testEquals7 () throws Exception
	{
		VertexImpl<String> v1 = new VertexImpl<String>("v1");
		VertexImpl<String> v2 = new VertexImpl<String>("v2");
		VertexImpl<String> v3 = new VertexImpl<String>("v3");
		VertexImpl<String> v4 = new VertexImpl<String>("v4");

		Cycle<String> c1 = new Cycle<String>();
		c1.addVertexIntoCycle(v2);
		c1.addVertexIntoCycle(v3);
		c1.addVertexIntoCycle(v4);
		c1.addVertexIntoCycle(v1);

		Cycle<String> c2 = new Cycle<String>();
		c2.addVertexIntoCycle(v1);
		c2.addVertexIntoCycle(v2);
		c2.addVertexIntoCycle(v3);
		c2.addVertexIntoCycle(v4);

		boolean result = c1.equals(c2);
		boolean expectedResult = true;
		assertEquals(expectedResult, result);
	}

	@Test
	public void testCompareTo () throws Exception
	{

	}
}