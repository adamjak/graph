package net.adamjak.graph.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

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

}