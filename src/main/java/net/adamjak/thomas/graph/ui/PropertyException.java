package net.adamjak.thomas.graph.ui;

/**
 * Created by Tomas Adamjak on 9.11.2016.
 * Copyright 2016, Tomas Adamjak
 * License: The BSD 3-Clause License
 */
public class PropertyException extends Exception
{
    public PropertyException()
    {
    }

    public PropertyException(String message)
    {
        super("Property Exception:" + message);
    }

    public PropertyException(String message, Throwable cause)
    {
        super("Property Exception:" + message, cause);
    }

    public PropertyException(Throwable cause)
    {
        super(cause);
    }
    
}