package net.adamjak.thomas.graph.ui;

import java.util.Scanner;

/**
 * Created by Tomas Adamjak on 1.11.2016.
 * Copyright 2016, Tomas Adamjak
 * License: The BSD 3-Clause License
 */
public class Manager
{
	private final Scanner in;

	public Manager()
	{
		this.in = new Scanner(System.in);
	}

	public void start()
	{
		while (true)
		{
			System.out.print("Insert full property file path or [H] for display property file help or [Q] for exit: ");
			String input = this.in.nextLine();

			switch (input)
			{
				case "H":
				case "h":
					System.out.println(Property.getTextWithPropertyHelp());
					break;
				case "Q":
				case "q":
					System.exit(0);
					break;
				default:
					// TODO: 1.11.2016 -- ked zada cestu k property file
					break;
			}
		}
	}
}
