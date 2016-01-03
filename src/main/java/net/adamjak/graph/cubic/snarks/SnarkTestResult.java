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
	// TODO: 3.1.2016 - dorobit sem graf 

	public void setSnark (boolean snark)
	{
		this.snark = snark;
	}

	public boolean isSnark ()
	{
		return this.snark;
	}
}
