package net.adamjak.thomas.graph.ui;

/**
 * Created by Tomas Adamjak on 1.11.2016.
 * Copyright 2016, Tomas Adamjak
 * License: The BSD 3-Clause License
 */
public class Property
{
	private String folderWithGraphs;
	private String graphAlgorithmsPackage;
	private Operations operation;

	// TODO: 1.11.2016 -- vytvorit triedu

	public static String getTextWithPropertyHelp()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("   Graph property file\n");
        sb.append("-------------------------\n");
        sb.append("Create with app:\n");
        sb.append("\tYou can create with simple Java GUI application whitch you can find on https://github.com/adamjak/graph-property-file-creator\n");
        sb.append("\n");
        sb.append("Create in text editro:\n");
        sb.append("\tyou can create property file with yourself in text editor like Vim or Notepad.\n");
        sb.append("\tPropertu file have to have 3 lines with 3 properties:\n");
        sb.append("\t 1. Line - graphAlgorithmPackage - You can create self graph algorithm implements my API and this propertie told app about package where classes are. If you haven't self graph algorithm value will be 'null'. For example: graphAlgorithmPackage=net.adamjak.thomas.graph\n");
        sb.append("\t 2. Line folderWithGraphs - Full path to folder where you have graphs whitch you want test. For example: folderWithGraphs=/home/bob/graph-theory/library\n");
        sb.append("\t 3. Line operation - Tell app what you want to do. You have 2 choices:\n");
        sb.append("\t\t * BENCHMARK_ALL_ALGORITHMS - When you have to test graph with all algorithms and benchmark.\n");
        sb.append("\t\t * ONLY_SNARK_TEST - When you have to only test if graph is snark.\n");
        sb.append("\t Example for third line: operation=BENCHMARK_ALL_ALGORITHMS\n");

		return sb.toString();
	}
}
