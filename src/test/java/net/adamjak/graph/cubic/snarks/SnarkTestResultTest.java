package net.adamjak.graph.cubic.snarks;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Created by Tomas Adamjak on 9.10.2016.
 * Copyright 2016, Tomas Adamjak
 * License: The BSD 3-Clause License
 */
public class SnarkTestResultTest
{
	private SnarkTestResult snarkTestResult;

	@Before
	public void init () throws Exception
	{
		this.snarkTestResult = new SnarkTestResult();
	}

	@Test
	public void setTimeGetTimeTest () throws Exception
	{
		long newTime = 435L;
		this.snarkTestResult.setTime(newTime);
		assertEquals(newTime, this.snarkTestResult.getTime());
	}


	@Test
	public void getTimeInSecondsTest () throws Exception
	{
		long newTime = 4000000000L;
		this.snarkTestResult.setTime(newTime);

		Double expectedTime = (double) newTime / 1000000000.0;

		assertEquals(expectedTime, Double.valueOf(this.snarkTestResult.getTimeInSeconds()));
	}

	@Test
	public void setSnarkIsSnarkTest () throws Exception
	{
		this.snarkTestResult.setSnark(false);
		assertFalse(this.snarkTestResult.isSnark());
	}

}