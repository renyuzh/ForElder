package org.heartwings.care.falldetect;

public class LowPassFilter {
	final double inf=10000000.0;
	double historyValue=-inf;
	double a;
	public LowPassFilter(double tau, double dt)
	{
		a = tau/(tau+dt);
	}
	public double nextValue(double current)
	{
		if(historyValue==-inf)
		{
			historyValue=current;
			return current;
		}
		historyValue = historyValue*(1-a) + current*a;
		return historyValue;
	}
}
