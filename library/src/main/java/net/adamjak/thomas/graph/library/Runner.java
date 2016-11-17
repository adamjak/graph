package net.adamjak.thomas.graph.library;

/**
 * Created by Tomas Adamjak on 9.11.2016.
 * Copyright 2016, Tomas Adamjak
 * License: The BSD 3-Clause License
 */
public class Runner {
//    private final Property property;
//
//    public Runner(Property property)
//    {
//        this.property = property;
//    }
//
//    public void start()
//    {
//        // TODO -- edit this method by property file
//
//		List<File> files = Utils.getListOfFilesInFolder(this.property.getFolderWithGraphs());
//
//		List<Graph<Integer>> listOfGraphs = new LinkedList<Graph<Integer>>();
//
//		for (File f : files)
//		{
//			if (f.getName().endsWith(".g6"))
//			{
//				try
//				{
//					listOfGraphs.add(GraphFactory.createGraphFromGraph6(f));
//				}
//				catch (IOException e) {}
//			}
//			else
//			{
//				try
//				{
//					String[] parts = f.getName().split("\\.");
//					Integer.valueOf(parts[parts.length - 1]);
//					listOfGraphs.addAll(GraphFactory.createGraphFromTextCatalog(f));
//				}
//				catch (NumberFormatException e) {}
//			}
//		}
//
//		ClassFinder classFinder = new ClassFinder();
//
//		Set<String> packageNames = new LinkedHashSet<String>();
//		packageNames.add("net.adamjak.thomas.graph.library.cubic.snarks");
//		if (this.property.getGraphAlgorithmsPackage() != null)
//		{
//			packageNames.add(this.property.getGraphAlgorithmsPackage());
//		}
//
//		Set<Class<?>> classes = classFinder.findClassesWhitchExtends(classFinder.findAnnotatedClasses(packageNames, true, Benchmarked.class), SnarkTest.class);
//
//		for (Graph<Integer> g : listOfGraphs)
//		{
//			ForkJoinPool forkJoinPool = new ForkJoinPool(classes.size());
//
//			Set<SnarkTest> snarkTests = new LinkedHashSet<SnarkTest>();
//
//			for (Class<?> c : classes)
//			{
//				SnarkTest snarkTest = null;
//				try
//				{
//					snarkTest = (SnarkTest) c.newInstance();
//					snarkTest.init(g);
//					snarkTests.add(snarkTest);
//				}
//				catch (InstantiationException e)
//				{
//					e.printStackTrace();
//				}
//				catch (IllegalAccessException e)
//				{
//					e.printStackTrace();
//				}
//			}
//
//			for (SnarkTest st : snarkTests)
//			{
//				forkJoinPool.execute(st);
//			}
//
//			SnarkTestResult snarkTestResult = null;
//
//			while (snarkTestResult == null)
//			{
//				for (SnarkTest st : snarkTests)
//				{
//					if (st.isDone())
//					{
//						try
//						{
//							snarkTestResult =st.getResult();
//						}
//						catch (InterruptedException e)
//						{
//							e.printStackTrace();
//						}
//						catch (ExecutionException e)
//						{
//							e.printStackTrace();
//						}
//					}
//				}
//			}
//
//			System.out.println("Snark test result " + snarkTestResult.getSnarkTesterClass().getSimpleName() + ":\n Time: " + snarkTestResult.getTimeInSeconds() + " second\n Snark:" + snarkTestResult.isSnark());
//		}
//	}
}
