package net.adamjak.thomas.graph.library.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

/**
 * Created by Tomas Adamjak on 30.11.2016.
 * Copyright 2016, Tomas Adamjak
 * License: The BSD 3-Clause License
 */
public class Settings
{
	private static Settings instance = null;
	private Properties prop = new Properties();

	private Settings ()
	{
		throw new IllegalStateException("Can not call singleton constructor.");
	}

	private Settings (URL url)
	{
		InputStream input = null;
		try
		{
			input = new FileInputStream(url.getFile());
			this.prop.load(input);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (input != null)
			{
				try
				{
					input.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}

	}

	public static Settings getInstance ()
	{
		if (instance == null)
		{
			instance = new Settings(Settings.class.getClassLoader().getResource("config.properties"));
		}
		return instance;
	}

	public String getSetting(String key)
	{
		return this.prop.getProperty(key);
	}
}
