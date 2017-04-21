package net.adamjak.thomas.graph.application.commons;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

/**
 * Created by Tomas Adamjak on 21.4.2017.
 * Copyright 2017, Tomas Adamjak
 * License: The BSD 3-Clause License
 */
public class StatisticsUtils
{
	private final static double[] limits005 = {1.412, 1.689, 1.869, 1.996, 2.093, 2.172, 2.237, 2.294, 2.343, 2.387, 2.426, 2.461, 2.493, 2.523, 2.551, 2.577, 2.6, 2.623, 2.644, 2.664, 2.683, 2.701, 2.717};
	private final static double[] limits001 = {1.414, 1.723, 1.955, 2.13, 2.265, 2.374, 2.464, 2.54, 2.606, 2.663, 2.714, 2.759, 2.8, 2.837, 2.871, 2.903, 2.932, 2.959, 2.984, 3.008, 3.03, 3.051, 3.071};

	public enum GrubbsLevel
	{
		L001(limits001), L005(limits005);

		private final double[] limit;

		private GrubbsLevel (double[] limit)
		{
			this.limit = limit;
		}

		public Double getCriticalValue (int number)
		{
			if (number < 3)
			{
				return Double.NaN;
			}

			int num = number - 3;

			if (num > this.limit.length)
			{
				return this.limit[this.limit.length - 1];
			}

			return this.limit[num];
		}
	}

	public enum NormCritical
	{
		U0200(0.842), U0100(1.282), U0050(1.645), U0025(1.960), U0010(2.326), U0005(2.576);

		private final double criticalValue;

		NormCritical (double criticalValue) {this.criticalValue = criticalValue;}

		public double getCriticalValue ()
		{
			return criticalValue;
		}
	}

	public static DescriptiveStatistics statisticsWithoutExtremes (DescriptiveStatistics inputStatistics, GrubbsLevel grubbsLevel) throws IllegalArgumentException
	{
		if (inputStatistics == null || grubbsLevel == null)
			throw new IllegalArgumentException("Params inputStatistics and grubbsLevel can not be null.");

		int countInput = inputStatistics.getValues().length;
		Double avgInput = inputStatistics.getMean();
		Double stdInput = inputStatistics.getStandardDeviation();
		Double s = stdInput * Math.sqrt((countInput - 1.0) / countInput);
		Double criticalValue = grubbsLevel.getCriticalValue(countInput);

		DescriptiveStatistics outputStatistic = new DescriptiveStatistics();

		for (double inpVal : inputStatistics.getValues())
		{
			double test = Math.abs(inpVal - avgInput) / s;

			if (test <= criticalValue)
			{
				outputStatistic.addValue(inpVal);
			}
		}

		return outputStatistic;
	}

	public static double getConfidenceInterval (DescriptiveStatistics inputStatistics, NormCritical uAlpha) throws IllegalArgumentException
	{
		if (inputStatistics == null || uAlpha == null)
			throw new IllegalArgumentException("Params inputStatistics or uAlpha can not be null!");

		return (inputStatistics.getStandardDeviation() * uAlpha.getCriticalValue()) / Math.sqrt(inputStatistics.getValues().length);
	}
}
