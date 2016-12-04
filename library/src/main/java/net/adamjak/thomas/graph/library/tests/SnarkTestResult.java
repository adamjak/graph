package net.adamjak.thomas.graph.library.tests;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Tomas Adamjak on 3.1.2016.
 * Copyright 2016, Tomas Adamjak
 * License: The BSD 3-Clause License
 */
public class SnarkTestResult implements GraphTestResult
{
	private Map<String,Object> prop = new TreeMap<String,Object>();
	private Boolean snark;
	private Long time;
	private Class<?> snarkTesterClass;

	/**
	 * Add value into result. If in result is value with same identifier method modify it.
	 *
	 * @param identifier identifier of value
	 * @param value      value whitch we want to add
	 * @throws IllegalArgumentException if any parameter is {@code null}.
	 */
	@Override
	public void addValue (String identifier, Object value) throws IllegalArgumentException
	{
		this.prop.put(identifier, value);
	}

	/**
	 * @param identifier identifier of value
	 * @return Return value by identifier
	 * @throws IllegalArgumentException if identifier param is {@code null} or if for identifier is not any value
	 */
	@Override
	public Object getValue (String identifier) throws IllegalArgumentException
	{
		try
		{
			return this.prop.get(identifier);
		}
		catch (NullPointerException e)
		{
			throw new IllegalArgumentException("Identifier " + identifier + " is not result");
		}

	}

	/**
	 * Verify that the value of a given identifier specified.
	 *
	 * @param identifier
	 * @return Return <b><code>true</code></b> if value os given identifier is specified else return <b><code>false</code></b>.
	 */
	@Override
	public boolean isValue (String identifier)
	{
		if (this.prop.containsKey(identifier))
		{
			return true;
		}

		return false;
	}


	public SnarkTestResult () {}

	public SnarkTestResult (long time)
	{
		this.setTime(time);
	}

	public SnarkTestResult (Boolean isSnark)
	{
		this.setSnark(isSnark);
	}

	public SnarkTestResult (Class<?> snarkTesterClass)
	{
		this.setSnarkTesterClass(snarkTesterClass);
	}

	public SnarkTestResult (long time, boolean isSnark)
	{
		this.setTime(time);
		this.setSnark(isSnark);
	}

	public SnarkTestResult (long time, Boolean isSnark, Class<?> snarkTesterClass)
	{
		this.setTime(time);
		this.setSnark(isSnark);
		this.setSnarkTesterClass(snarkTesterClass);
	}

	/**
	 * @param time time in nanoseconds
	 * @see System#nanoTime()
	 */
	public void setTime (long time)
	{
		this.time = time;
		this.addValue("time",time);
		this.addValue("timeInSeconds",this.getTimeInSeconds());
	}

	/**
	 * @return Time in nanoseconds
	 * @see System#nanoTime()
	 */
	public long getTime ()
	{
		return this.time;
	}

	public double getTimeInSeconds ()
	{
		return (double) this.getTime() / 1000000000.0;
	}

	public void setSnark (Boolean snark)
	{
		this.snark = snark;
		this.addValue("snark",snark);
	}

	public Boolean isSnark ()
	{
		return this.snark;
	}

	public Class<?> getSnarkTesterClass ()
	{
		return this.snarkTesterClass;
	}

	public void setSnarkTesterClass (Class<?> snarkTesterClass)
	{
		this.snarkTesterClass = snarkTesterClass;
		this.addValue("snarkTesterClass",snarkTesterClass);
	}

	@Override
	public String toString ()
	{
		StringBuilder sb = new StringBuilder();

		sb.append("SnarkTestResult: {");

		int i = 0;
		for (String key : this.prop.keySet())
		{
			if (i == 0)
			{
				sb.append(key + ":" + this.prop.get(key));
			}
			else
			{
				sb.append(", " + key + ":" + this.prop.get(key));
			}
			i++;
		}

		sb.append("}");

		return sb.toString();
	}
}
