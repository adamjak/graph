package net.adamjak.graph.cubic.snarks;

/**
 * Created by Tomas Adamjak on 3.1.2016.
 * Copyright 2016, Tomas Adamjak
 * License: The BSD 3-Clause License
 */
public class SnarkTestResult
{
	private boolean snark;
	private long time;
	// TODO: 3.1.2016 - dorobit sem graf 

	public SnarkTestResult(){}

	public SnarkTestResult(long time)
	{
		this.setTime(time);
	}

	public SnarkTestResult (boolean isSnark)
	{
		this.setSnark(isSnark);
	}

	public SnarkTestResult (long time, boolean isSnark)
	{
		this.setTime(time);
		this.setSnark(isSnark);
	}

	/**
	 * @param time time in nanoseconds
	 * @see System#nanoTime()
	 */
	public void setTime (long time)
	{
		this.time = time;
	}

	/**
	 * @return Time in nanoseconds
	 * @see System#nanoTime()
	 */
	public long getTime ()
	{
		return this.time;
	}

	public double getTimeInSeconds()
	{
		return (double) this.getTime() / 1000000000.0;
	}

	public void setSnark (boolean snark)
	{
		this.snark = snark;
	}

	public boolean isSnark ()
	{
		return this.snark;
	}
}
