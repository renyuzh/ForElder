package org.heartwings.care.falldetect;

public class ThresholdFallDetectorGyroscope {
	// oldest ... 5 4 3 2 1
	double[] slideWindowX;
	double[] slideWindowZ;
	double thresholdDegree;
	double stickTime;
	int windowSize;

	double lastTimeMill = -1;

	boolean isFallDown = false;
	double lastFallDownTime = 0;

	/**
	 * @param windowSize
	 *            Recommended for 40 (0.8s)
	 * @param threshold
	 *            The threshold in degree to determine the fall down
	 * @param stickTime
	 *            The time that this detector will stay
	 */
	public ThresholdFallDetectorGyroscope(int windowSize,
			double thresholdDegree, double stickTimeMills) {
		slideWindowX = new double[windowSize];
		slideWindowZ = new double[windowSize];
		for (int i = 0; i < windowSize; i++) {
			slideWindowX[i] = 0.0;
			slideWindowZ[i] = 0.0;
		}
		this.thresholdDegree = thresholdDegree;
		this.windowSize = windowSize;
		this.stickTime = stickTimeMills;
	}

	public boolean getStatus(double timestampMills) {
		return (timestampMills - lastFallDownTime < stickTime);
	}

	public double getMaxX() {
		double M = -1, sumX = 0, sumZ = 0;
		for (int i = windowSize - 1; i >= 0; i--) {
			sumX += slideWindowX[i];
			sumZ += slideWindowZ[i];
			M = Math.max(M, Math.abs(sumX));
			M = Math.max(M, Math.abs(sumZ));
		}
		return M;
	}

	public boolean feedGyroscope(double timestampMills, double X, double Y,
			double Z) {
		double dt;
		if (lastTimeMill != -1) {
			dt = timestampMills - lastTimeMill;
		} else {
			dt = 20;
		}
		lastTimeMill = timestampMills;
		for (int i = 0; i < windowSize - 1; i++) {
			slideWindowX[i] = slideWindowX[i + 1];
			slideWindowZ[i] = slideWindowZ[i + 1];
		}
		slideWindowX[windowSize - 1] = X;
		slideWindowZ[windowSize - 1] = Z;
		double sumX = 0.0, sumZ = 0.0;
		for (int i = windowSize - 1; i >= 0; i--) {
			sumX += slideWindowX[i];
			sumZ += slideWindowZ[i];
			// TODO 缺少ABS造成很大的问题！
			if (Math.abs(sumX) >= thresholdDegree
					|| Math.abs(sumZ) >= thresholdDegree) {
				isFallDown = true;
				lastFallDownTime = timestampMills;
				return true;
			}
		}
		if (timestampMills - lastFallDownTime < stickTime) {
			return true;
		}
		return false;
	}
}
