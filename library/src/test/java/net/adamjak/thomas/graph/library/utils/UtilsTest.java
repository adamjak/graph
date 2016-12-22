package net.adamjak.thomas.graph.library.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Tomas Adamjak on 17.7.2016.
 * Copyright 2016, Tomas Adamjak
 * License: The BSD 3-Clause License
 */
public class UtilsTest
{
	@Test
	public void countCharInString () throws Exception
	{
		String s = "a.b.c.d";
		char c = '.';

		int expectedvalue = 3;
		assertEquals(expectedvalue, Utils.countCharInString(s,c));
	}

	@Test
	public void pairEqualsTest () throws Exception
	{
		Utils.Pair<Integer> p1 = new Utils.Pair<>(1, 2);
		Utils.Pair<Integer> p2 = new Utils.Pair<>(2, 1);
		Utils.Pair<Integer> p3 = new Utils.Pair<>(2, 3);

		assertTrue(p1.equals(p2));
		assertFalse(p2.equals(p3));
	}

}