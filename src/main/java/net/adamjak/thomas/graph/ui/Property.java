package net.adamjak.thomas.graph.ui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Tomas Adamjak on 1.11.2016.
 * Copyright 2016, Tomas Adamjak
 * License: The BSD 3-Clause License
 */
public class Property
{
	private File folderWithGraphs;
	private String graphAlgorithmsPackage = null;
	private Operations operation;

    /**
     * Create property class from property file.
     * @param file property file
     * @throws NullPointerException if the pathname argument is null
     * @throws IllegalArgumentException if param file is directory
     * @throws PropertyException if an error occurs while reading file or when property file has not correct values, keys or format.
     */
	public Property (String filePath) throws NullPointerException, IllegalArgumentException, PropertyException
    {
        this(new File(filePath));
    }
    
    /**
     * Create property class from property file.
     * @param file property file
     * @throws IllegalArgumentException if param file is null or if is directory
     * @throws PropertyException if an error occurs while reading file or when property file has not correct values, keys or format.
     */
    public Property (File file) throws IllegalArgumentException, PropertyException
    {
        if (file == null) throws new IllegalArgumentException("Param file can not by null");
        if (file.isDirectory()) throw new IllegalArgumentException("File is directory");
        
        BufferedReader br = null;

		try
        {
			String sCurrentLine;
            
			br = new BufferedReader(new FileReader(file));

			while ((sCurrentLine = br.readLine()) != null)
            {
				String[] line = sCurrentLine.split("=");
                if (line.length == 2)
                {
                    String key = line[0].trim();
                    String value = line[1].trim();
                    if (key.equals("folderWithGraphs"))
                    {
                        File folder = new File(value);
                        if (folder.isDirectory())
                        {
                            this.folderWithGraphs = folder;
                        }
                    }
                    else if (key.equals("graphAlgorithmsPackage"))
                    {
                        this.graphAlgorithmsPackage = value;
                    }
                    else if (key.equals("operation"))
                    {
                        if (value.equals(Operations.BENCHMARK_ALL_ALGORITHMS.name()))
                        {
                            this.operation = Operations.BENCHMARK_ALL_ALGORITHMS;
                        }
                        else if (value.equals(Operations.ONLY_SNARK_TEST.name()))
                        {
                            this.operation = Operations.ONLY_SNARK_TEST;
                        }
                    }
                }
                
			}
            
            if (this.folderWithGraphs == null || this.operation == null)
            {
                throw new PropertyException("Property file has not correct values, keys or format.");
            }

		}
        catch (IOException e)
        {
			throw new PropertyException(e.getMessage());
		}
        finally
        {
			try
            {
				if (br != null) br.close();
			}
            catch (IOException ex) {}
		}
    }

    /**
     * Create property class when you insert properties.
     * @param folderWithGraphs directory with graphs
     * @param graphAlgorithmsPackage name package with graph algorithms (can be null)
     * @param operation opertion whitch you want to do, select from {@link Operations}
     * @throws IllegalArgumentException if param folderWithGraphs or operation is {@code null}
     */
    public Property(File folderWithGraphs, String graphAlgorithmsPackage, Operations operation) throws IllegalArgumentException
    {
        if (folderWithGraphs == null || operation == null) throw new IllegalArgumentException("Params folderWithGraphs and operation can not be null.");
        
        this.folderWithGraphs = folderWithGraphs;
        this.graphAlgorithmsPackage = graphAlgorithmsPackage;
        this.operation = operation;
    }
    
    

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
