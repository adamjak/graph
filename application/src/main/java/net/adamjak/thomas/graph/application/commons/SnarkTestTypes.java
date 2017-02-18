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

	/**
	 * @return String array with names of {@link SnarkTestTypes}
	 */
	public static String[] getTestNames ()
	{
		String[] names = new String[SnarkTestTypes.values().length];
		SnarkTestTypes[] types = SnarkTestTypes.values();
		for (int i = 0; i < types.length; i++)
		{
			names[i] = types[i].name();
		}
		return names;
	}

	/**
	 * @param name name of snark test type
	 * @return {@link SnarkTestTypes} if name contains with some type name else return {@code null}
	 * @throws IllegalArgumentException if param name is null
	 */
	public static SnarkTestTypes getSnarkTestTypeByName (String name) throws IllegalArgumentException
	{
		if (name == null) throw new IllegalArgumentException("Param name can not be null!");
		for (SnarkTestTypes type : SnarkTestTypes.values())
		{
			if (type.name().equals(name))
			{
				return type;
			}
		}

		return null;
	}
}
