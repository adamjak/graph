package net.adamjak.thomas.graph.application.commons;

/**
 * Created by Tomas Adamjak on 5.12.2016.
 * Copyright 2016, Tomas Adamjak
 * License: The BSD 3-Clause License
 */
public enum SnarkTestTypes
{
	ALGORITHM_COMPARATION("algorithm comparation"),
	ALL_ALGORITHMS("all algorithms"),
	ONE_ALGORITHM("one algorithmn");

	private String value;

	SnarkTestTypes (String value)
	{
		this.value = value;
	}

	public String getValue ()
	{
		return this.value;
	}

	public void setValue (String value)
	{
		this.value = value;
	}
}
