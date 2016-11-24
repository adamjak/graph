package net.adamjak.thomas.graph.application.commons;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Tomas Adamjak on 24.11.2016.
 * Copyright 2016, Tomas Adamjak
 * License: The BSD 3-Clause License
 */
public class UtilsTest
{
	@Test
	public void isIntInArrayTestPositive () throws Exception
	{
		int i = 2;
		int[] arr = {1, 2, 3};

		assertTrue(Utils.isIntInArray(i, arr));
	}

	@Test
	public void isIntInArrayTestNegative () throws Exception
	{
		int i = 4;
		int[] arr = {1, 2, 3};

		assertFalse(Utils.isIntInArray(i, arr));
	}
}
