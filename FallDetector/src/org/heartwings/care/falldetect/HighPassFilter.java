package org.heartwings.care.falldetect;

/**
 * @author Wave
 */
public class HighPassFilter { 
	LowPassFilter lpf;
	public HighPassFilter(double tau, double dt)
	{
		lpf = new LowPassFilter(tau, dt);
	}
	public double nextValue(double current)
	{
		return current - lpf.nextValue(current);
	}
}
