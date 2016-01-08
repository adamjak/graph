package net.adamjak.graph.cubic.snarks;

import net.adamjak.graph.classes.Graph;

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

	public void setTime (long time)
	{
		this.time = time;
	}

	public long getTime ()
	{
		return this.time;
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
