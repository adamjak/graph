package net.adamjak.thomas.graph.library.tests;

/**
 * Created by Tomas Adamjak on 27.11.2016.
 * Copyright 2016, Tomas Adamjak
 * License: The BSD 3-Clause License
 */
public interface GraphTestResult
{
	public static final String TIME_KEY = "time";
	public static final String SNARK_KEY = "snark";
	public static final String SNARK_TESTER_CLASS_KEY = "snarkTesterClass";

	/**
	 * Add value into result. If in result is value with same identifier method modify it.
	 * @param identifier identifier of value
	 * @param value value whitch we want to add
	 * @throws IllegalArgumentException if any parameter is {@code null}.
	 */
	void addValue(String identifier, Object value) throws IllegalArgumentException;

	/**
	 * @param identifier identifier of value
	 * @return Return value by identifier
	 * @throws IllegalArgumentException if identifier param is {@code null} or if for identifier is not any value
	 */
	Object getValue(String identifier) throws IllegalArgumentException;

	/**
	 * Verify that the value of a given identifier specified.
	 * @param identifier identifier of value
	 * @return Return <b><code>true</code></b> if value os given identifier is specified else return <b><code>false</code></b>.
	 */
	boolean isValue (String identifier);
}
