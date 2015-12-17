package org.heartwings.care.falldetect;

public abstract class FallDetector {
	private double sampleRate;
	public FallDetector(double sampleRate)
	{
		this.sampleRate = sampleRate;
	}
	public abstract boolean feedData(double timestamp, double accX, double accY, double accZ);
}
